package alan.Internet.Impl;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

/**
 * 客户端：TCP33333
 * 此类作用：维护联系人列表
 * */
public class ClientListRepairTCP
{
	private static final String IPaddress = IP_About.IPaddress;
	private static final int Prot = IP_About.RepairProt;//端口
	private static Socket socket; //具体用户
	
	private static final String CODED = "UTF-8";//定义字符流中“发送”与“接收”的编码
	
	private ClientListRepairTCP() {}
	
	static {
		try {
			socket = new Socket(IPaddress,Prot);
		} catch (IOException e) {
			System.out.println("客户端维护初始化Socket失败");
			e.printStackTrace();
		}
	}

	//发送数据（字符流）
	public static void senData(String str) {
		BufferedWriter bw;
		try {
			bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(),CODED));//指定了发送编码
			bw.write(str);//发送数据
			bw.newLine();
			bw.flush();
		} catch (IOException e) {
			//System.out.println("发送数据异常！");
			Close();
		}
	}
	
	//接收数据（阻塞似方法，接收数据方法-字符流）
	public static String recData() {
		BufferedReader br;
		String line = null;
		try {
			br = new BufferedReader(new InputStreamReader(socket.getInputStream(),CODED));//指定了接收编码
			line = br.readLine();//接收数据
		} catch (IOException e) {
			//System.out.println("维护联系人列表线程-字符流接收数据异常：IOException");
			//e.printStackTrace();
			Close();
		}
		return (line.trim());
	}
	
	//关闭套接字
	public static void Close() {
		try {
			socket.close();
		} catch (IOException e) {
			// System.out.println("关闭套接字异常：IOException");
			// e.printStackTrace();
		}
	}
}