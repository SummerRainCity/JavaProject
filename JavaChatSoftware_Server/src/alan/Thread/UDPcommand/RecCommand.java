package alan.Thread.UDPcommand;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

import alan.Internet.IP_About;
import alan.Internet.TCPClientListRepair;
import alan.User.ClientBoxMain;
import alan.User.ClientBoxRepair;
import alan.User.DataBase;

/**
 * 此类接收客户端发来的指令（UDP接收）
 * */
public class RecCommand implements Runnable
{
	public static boolean flag = false;//标记：是否更新数据库及内存

	@Override
	public void run() 
	{
		// 创建接收用的UDP对象
		DatagramSocket ds = null;
		try {
			ds = new DatagramSocket(IP_About.UDPProt); // 参数是接收端端口
			while (true) 
			{
				// 创建一个接收包（接收容器）
				// DatagramPacket(byte[] buf,int length)
				byte[] bys = new byte[1024];
				DatagramPacket dp = new DatagramPacket(bys, bys.length);

				// 接收数据
				ds.receive(dp); // 阻塞方法（一致等待对方发来数据）
				// 解析数据包，并显示在控制台
				String cmd = new String(bys, 0, dp.getLength()); // public int getLength()：获取数据的实际长度

				//解析命令并执行
				parseCommand(cmd);
			}
		} catch (IOException e) {
			// 释放资源
			ds.close();
		}
	}
	
	//此方法根据传来的指令-修改数据库数据及更新内存数据
	private void parseCommand(String command) {
		if(command.startsWith("n"))
		{
			command = command.substring(1);//返回去掉第一个字符后的字符串
			//“:”正则分析
			String[] acnm = command.split(":");//acnm[0]就是当前账号
			//根据账号修改“内存”数据(flag标记将更新传输数据)
			DataBase.setName(acnm[0], acnm[1]);
			RecCommand.flag = true;

			//更新本地联系人维护列表（内存更新）
			ClientBoxMain.refreshClientList();
			
			//更新所有客户端内存（远程内存更新）【使用ClientBoxRepair类进行远程指令操作】
			ClientBoxRepair.refreshCurrentClientList(acnm[0], acnm[1]);
			//刷新数据库
			DataBase.refreshAllData();
		}
		else if(command.startsWith("p")) 
		{
			command = command.substring(1);
			String[] acnm = command.split(":");
			//核对原密码是否正确
			if(DataBase.checkPassword(acnm[0], acnm[1])) 
			{
				//如果认证正确就设置新密码
				DataBase.setPassword(acnm[0], acnm[2]);
				//刷新数据库
				DataBase.refreshAllData();
				//通过“维护协议”，通知改密成功
				TCPClientListRepair.senData("Success", ClientBoxRepair.returnSocket(acnm[0]));
			}else {
				//改密失败
				TCPClientListRepair.senData("Failure", ClientBoxRepair.returnSocket(acnm[0]));
			}
		}
	}
}