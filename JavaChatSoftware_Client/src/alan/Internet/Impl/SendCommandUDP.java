package alan.Internet.Impl;
/**
 * 注：UDP用于客户端向服务器传递个人修改的信息
 * */
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class SendCommandUDP 
{
	public void sendCommand(String comd) 
	{
		DatagramSocket ds = null;
		try {
			// 创建UDP对象
			ds = new DatagramSocket();

			// 创建数据并把数据打包
			byte[] bys = comd.getBytes();
			DatagramPacket dp = new DatagramPacket(bys, bys.length, InetAddress.getByName(IP_About.IPaddress), IP_About.UDPProt);

			// 调用Socket对象的发送方法发送数据包
			ds.send(dp);
		} catch (IOException e) {}
		// 释放资源
		ds.close();
	}
	
	public String receiveData() 
	{
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
				ds.close();// 释放资源
				return cmd;
			}
		} catch (IOException e) {}
		ds.close();// 释放资源
		return null;
	}
}