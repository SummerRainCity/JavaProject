package alan.Thread.SingleChat;

import java.io.IOException;
import java.io.UnsupportedEncodingException;


import alan.Internet.TCPSingleChat;
import alan.User.ClientBoxRepair;

/**
 * 《客户端一对一聊天处理线程(Main)》
 * 注：此类依赖“线程维护协议-port=33333”去控制客户端，使其客户端弹出单人聊天框。
 * */
/*
 * 思路：
 * 		先是“发起方”发起连接(发起Socket链接)，然后“发起方”会告诉服务器它是谁、他要和谁建立单
 * 聊连接；服务端会找到“发起方”指定的人，并发送指令给被动方使其激活单聊框（这里被动方也会对服
 * 务器发送Socket链接）。接下来开启线程，让这两个Socket通信。
 * */
public class SingleChatTh implements Runnable
{
	private TCPSingleChat TSC = new TCPSingleChat();
	private String name_one;
	private String name_two;

	@Override
	public void run() 
	{
		boolean flag = false; //作用：如果

		while (true) 
		{
			//等待“客户端01”链接...
			waitingOneLink();
			
			//等待“客户端02”链接...
			do {
				// 让“维护协议”去通知对应客户端，唤醒对应“name_two”的单聊界面
				flag = ClientBoxRepair.rouseClientSingle(name_two);
				if(flag) {
					try {
						TSC.socket_one.shutdownInput();
						TSC.socket_one.shutdownOutput();
						TSC.socket_one.close();
					} catch (IOException e) {}
					break; //如果对应的人已经不在了，那么开启第二socket已经没有意义
				}
				flag = waitingTwoLink(); /*加循环作用：防止出现双方在建立通信链接期间有第三者插足，从而出现BUG！
				如果有，那么关闭socket_two，重启accept接收。（当然，前面的单聊指令得重写发送给socket_two）*/
			}while(flag);
			
			if(!flag) { //当条件满足“没有第三者插足”、“对方没有下线”的情况，则开启线程。
				// 开启两个线程，分别实现socket_one、socket_two数据接收、转发功能
				SingleChatThSon_one sctso = new SingleChatThSon_one(TSC, name_one);
				SingleChatThSon_two sctst = new SingleChatThSon_two(TSC);
				Thread sctsoth = new Thread(sctso, "Socket-01-RecMsg");
				Thread sctstth = new Thread(sctst, "Socket-02-RecMsg");
				sctsoth.setDaemon(true);// 设置“保护线程”
				sctstth.setDaemon(true);// 设置“保护线程”
				sctsoth.start();
				sctstth.start();
			}
		}
	}
	
	// 接收“name_one”-Socket-开启（此方法仅此类使用）
	private void waitingOneLink() 
	{
		try {
			TSC.socket_one = TSC.ss.accept();
			name_one = TSC.recData(TSC.socket_one);// 接收“one”的“姓名”(主动发起)
			name_two = TSC.recData(TSC.socket_one);// 接收来自one的“目标的姓名”
		} catch (IOException e) {
			System.out.println("单聊客户端链接时异常，alan\\Thread\\SingleChat\\SingleChatTh.java出错！");
			e.printStackTrace();
		}
	}

	// 接收“name_two”-Socket-开启（此方法仅此类使用）
	private boolean waitingTwoLink() 
	{
		try {
			TSC.socket_two = TSC.ss.accept();// name_two端被动激活后会发起TCP链接，这里接收
			TSC.senData(name_one, TSC.socket_two);// 发送one的姓名给two，two知道是谁与他单聊
		} catch (UnsupportedEncodingException e) { // 发送失败则视为->等待的非“客户端02”，而是别的客户端
			try {
				TSC.socket_two.shutdownInput();
				TSC.socket_two.shutdownOutput();
				TSC.socket_two.close();
			} catch (IOException e1) {return true;}
			return true;
		} catch (IOException e) {
			try {
				TSC.socket_two.shutdownInput();
				TSC.socket_two.shutdownOutput();
				TSC.socket_two.close();
			} catch (IOException e1) {return true;}
			return true;
		}
		return false;
	}
}