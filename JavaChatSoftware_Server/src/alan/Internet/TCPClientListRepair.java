package alan.Internet;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * 此类作用：维护联系人列表
 * 
 * 注：端口不一样（端口：33333）
 * */
public class TCPClientListRepair 
{
	private static final String CODED = "UTF-8";//定义字符流中“发送”与“接收”的编码
	public static ServerSocket ss; //服务端口
	public static Socket socket; //具体用户(变量)
	private TCPClientListRepair() {}
	
	static {
		try {
			ss = new ServerSocket(IP_About.RepairProt);
		} catch (IOException e) {
			System.out.println("服务端维护TCP初始化SeverSocket失败");
			e.printStackTrace();
		}
	}

	//发送数据（字符流）
	public static void senData(String str, Socket s) {
		BufferedWriter bw;
		try {
			bw = new BufferedWriter(new OutputStreamWriter(s.getOutputStream(),CODED));//指定了发送编码
			bw.write(str);//发送数据
			bw.newLine();
			bw.flush();
		} catch (IOException e) {
			System.out.println("维护列表发送数据异常！");
		}
	}
	
	//接收数据（阻塞似方法，接收数据方法-字符流）
	public static String recData(Socket socket) {
		BufferedReader br;
		String line = null;
		try {
			br = new BufferedReader(new InputStreamReader(socket.getInputStream(),CODED));//指定了接收编码
			line = br.readLine();//接收数据
		} catch (IOException e) {
			System.out.println("维护列表接收异常：IOException");
			e.printStackTrace();
		}
		return (line.trim());
	}
	
	//关闭套接字
	public static void Close() {
		try {
			socket.close();
		} catch (IOException e) {
			System.out.println("关闭套接字异常：IOException");
			e.printStackTrace();
		}
	}
}