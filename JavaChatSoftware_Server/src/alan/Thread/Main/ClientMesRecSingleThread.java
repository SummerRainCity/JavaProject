package alan.Thread.Main;

import java.net.Socket;

import alan.EventAndInit.ServerAction;
import alan.Internet.TCPSenRecMain;
import alan.Thread.UDPcommand.RecCommand;
import alan.User.ClientBoxMain;
import alan.User.ClientBoxRepair;
import alan.User.DataBase;
import alan.Util.Util;

/**
 * <单个客户端维护线程>
 * 此线程定位：ClientLinkThread子线程-核心线程[维护、检测用户连接状态、消息转发]
 * 
 * 此线程作用：
 * 1，主要负责接收用户发来的消息
 * 2，每个用户对应一个接收线程
 * 3，此线程处理用户是否存在链接的情况(判断是否在线)
 * 4，此线程是while(true)线程，用户越多则越多(一个用户对应一个线程)
 * 5，此线程会把一个用户发来的消息转发给所有用户(自动维护客户端客户列表)。
 * 
 * 注：消息转发在没有转发之前已经在此线程拼接好了（【+UserName+】：消息...），客户端线程判断即可。
 * */
public class ClientMesRecSingleThread implements Runnable
{
	private String userAccount;//将用于查找当前是谁发来消息，去Map集合查找
	private Socket socketUser;//保存当前用户Socket
	private String userName = null;//保存当前用户的用户名
	private StringBuffer Msg = new StringBuffer();//接收消息用户
	
	public ClientMesRecSingleThread(Socket s) {
		//当前用户的Socket
		this.socketUser = s;
	}
	
	@Override
	public void run() 
	{
		//收到确认信号，设置了当前线程是那个用户的“userName”就此设置完成
		init();
		
		while(true) 
		{
			//获取该用户的消息
			try {
				Msg.append(TCPSenRecMain.receiveDataChar(socketUser));
			} catch (Exception e) {
				ServerAction.JTArecord.append("[" + userName + "]" + Util.getTime() + "[离线]\r\n"); // 更新大框(左)
				break;//结束当前线程。
			}
			
			//刷新实时数据
			if(RecCommand.flag) {
				userName = DataBase.getName(userAccount);
				RecCommand.flag = false;
			}

			//更新“服务端”聊天消息列表框（大框）
			Msg.insert(0, userName+"("+userAccount+")：");//将姓名插入到最前面
			Msg.append("\r\n");//换行（在服务端消息框中是换行的）
			// 同步代码块
			synchronized (ClientLinkThread.LOCK) {
				ServerAction.JTArecord.append(Msg.toString());
			}
			
			//消息转发（静态转发，转发的目的地是所有联系人）【关键】
			ClientBoxMain.messageForwarding(Msg.toString());
			//清空消息
			Msg.delete(0, Msg.length());
		}
		
		//有人离开，在本地集合中移除此用户（否则出现连环报错）
		ClientBoxMain.deleteClient(userAccount);
		//有人离开，那么就完全刷新“服务器”右侧的“联系人”列表
		ClientBoxMain.refreshClientList();
		//有人离开，告知到所有客户端，在聊天列表中显示，谁下线了。
		ClientBoxMain.messageForwarding("----<"+userName+":"+userAccount+">---[下线]----");
		//有人离开，那么告诉客户端列表维护线程，需要移除那个人（在“静态维护类”）
		ClientBoxRepair.delClient(userAccount); //此方法也移除了本地维护列表中相应的联系人。
	}

	private void init() 
	{
		//第一次接收用户的确认信号，其实是空的。在客户端ClientAction类中可以看到发送了一个空格符
		Msg.append(TCPSenRecMain.receiveDataChar(socketUser));//接收确认信号
		Msg.delete(0, Msg.length());
		
		//将以确认信号的Socket来获取当前用户的“账号”
		//此方法的关键，在集合中找到此线程对应是那个“账号”
		//上一个父线程，已经接收了此客户端的链接，Socket已经存在Map<Name,Socket>集合中
		userAccount = ClientBoxMain.getClinetName(socketUser);
		
		//以当前用户的“账号”获取当前用户的“昵称”
		userName = DataBase.getName(userAccount);//ongoing数据库中获取
		
		//告知到所有客户端聊天列表，谁上线了。
		ClientBoxMain.messageForwarding("----<"+userName+"："+userAccount+">-"+Util.getTime()+"---[上线]----");
	}
}