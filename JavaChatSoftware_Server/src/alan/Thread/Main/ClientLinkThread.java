package alan.Thread.Main;

import java.io.IOException;

import alan.EventAndInit.ServerAction;
import alan.FIleIOServer.SendFile;
import alan.Internet.TCPSenRecMain;
import alan.User.ClientBoxMain;
import alan.User.DataBase;
import alan.Util.Util;

/**
 * 不断接收用户连接。
 * 连接时，还要接收用户名。
 * 
 * 此线程作用：
 * 1，等待“客户端”链接
 * 2，更新Map集合(value)
 * 
 * 注：此线程的子线程ClientLinkThreadSon作为接受姓名用(如果此线程accept不返回，那么子线程不会执行)
 * */
public class ClientLinkThread implements Runnable
{
	// 防止同时接收，同步“锁”（仅消息接收线程ClientMesRecThreadSon使用）
	// 公共“锁”，表示同一个对象。
	public static Object LOCK = new Object();
	// 决定最终要不要添加联系人，防止重复
	public static boolean Clientflag = false;
	
	public ClientLinkThread() {}
	
	@Override
	public void run() 
	{
		while(true) //直接控制着新用户连接
		{
			try {
				// 等待用户链接...
				TCPSenRecMain.socket = TCPSenRecMain.ss.accept();
			} catch (Exception e) {
				System.out.println("等待用户连接异常！");
				e.printStackTrace();
			}
			
			// 启动线程 接收“账户”+“密码”信息（此线程接收姓名立刻终止）【不适用线程接收名字会出现BUG】
			ClientLinkThreadAddSon rclts = new ClientLinkThreadAddSon();
			Thread th = new Thread(rclts, "等待接收用户名");
			th.setDaemon(true);// 设置“保护线程”，伴随主线程死亡而死亡
			th.setPriority(8);
			th.start();
			try {
				th.join(); //当前线程执行完，才执行下面的线程。
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
			
			if (ClientLinkThread.Clientflag) { //如果 添加当前联系人成功则开启线程，否则...
				// 开启当前用户的“接收消息”线程（此线程将无限监听当前用户发来的消息，直到当前用户断开链接为止）
				ClientMesRecSingleThread cmrt = new ClientMesRecSingleThread(TCPSenRecMain.socket);
				Thread cmrtth = new Thread(cmrt, "接收用户消息线程");
				cmrtth.setDaemon(true);// 设置“保护线程”
				cmrtth.setPriority(6);
				cmrtth.start();// 开启线程
			}else {
				//释放当前socket，并返回错误信息：已有此联系人。
				try {
					TCPSenRecMain.socket.shutdownInput();
					TCPSenRecMain.socket.shutdownOutput();
					TCPSenRecMain.socket.close();
				} catch (IOException e1) {}
			}
		}
	}
}


/*************************************************
 * 此线程定位：ClientLinkThread子线程-仅接收姓名
 * 
 * 此线程的作用是接收父线程指定的客户端发来的“姓名”
 * 
 * 成功后效果：
 * 1，更新服务端“联系人列表”
 * 2，将“姓名”保存到Map集合(key)
 * 3，更新“客户端”的“联系人列表”
 * 
 * 注：此线程受ClientLinkThread父线程支配
 * 
 * 此类仅接收“姓名”，接受完成既消失
 **************************************************/
class ClientLinkThreadAddSon implements Runnable
{
	public ClientLinkThreadAddSon() {}

	@Override
	public void run() 
	{
		//System.out.println("开启接收用户名的线程...");
		String user = TCPSenRecMain.receiveDataChar(TCPSenRecMain.socket);// 接收客户端发来的“账户”+“密码”
		
		String[] udat = user.split("=");
//		System.out.println("服务端接收到\r\n账号："+udat[0]+"\r\n密码："+udat[1]);
		//去数据库查找是否有此人
		boolean isChi = DataBase.checkPassword(udat[0], udat[1]);
		
		if(isChi) //如果“账号”与“密码”符合标准，则添加
		{
			// 将联系人加入Box盒子中（用于刷新本地联系人列表）
			ClientLinkThread.Clientflag = ClientBoxMain.addClient(udat[0], TCPSenRecMain.socket);
		}else {
			TCPSenRecMain.sendDataByte("X", TCPSenRecMain.socket);//发送“X”，表示密码或者账号错误
			ClientLinkThread.Clientflag = false; //这是客户端重复登录的情况
		}
		if (ClientLinkThread.Clientflag) {
			String userName = DataBase.getName(udat[0]);//获取此“账号”的“昵称。”
			ServerAction.JTArecord.append("[" + userName + "]"+udat[0]+"-"+ Util.getTime() + "[上线]\r\n"); // 更新大框(左)
			ServerAction.JTAlist.append("【" + userName + "】\r\n"); // 更新联系人列表(右)
			TCPSenRecMain.sendDataByte(userName, TCPSenRecMain.socket);
			
			//向好友发送本地好友数据
			new SendFile().sendFile();
		}
	}
}
