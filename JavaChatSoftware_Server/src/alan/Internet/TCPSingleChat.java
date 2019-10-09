package alan.Internet;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * 此静态类：一对一聊天TCP传输类（Main）
 * 端口：32600
 * */
public class TCPSingleChat 
{
	private final String CODED = "UTF-8";//定义字符流中“发送”与“接收”的编码
	public ServerSocket ss; //服务端口
	public Socket socket_one; //客户端01-Temp
	public Socket socket_two; //客户端02-Temp
	
	public TCPSingleChat()
	{
		try {
			ss = new ServerSocket(32600);
		} catch (IOException e) {
			System.out.println("服务端维护TCP：32600-初始化SeverSocket失败");
			e.printStackTrace();
		}
	}

	//发送数据（字符流）
	public void senData(String str, Socket s) throws UnsupportedEncodingException, IOException {
		BufferedWriter bw;
		bw = new BufferedWriter(new OutputStreamWriter(s.getOutputStream(), CODED));// 指定了发送编码
		bw.write(str);// 发送数据
		bw.newLine();
		bw.flush();
	}
	
	//接收数据（阻塞似方法，接收数据方法-字符流）
	public String recData(Socket socket) throws UnsupportedEncodingException, IOException {
		BufferedReader br;
		String line = null;
		br = new BufferedReader(new InputStreamReader(socket.getInputStream(), CODED));// 指定了接收编码
		line = br.readLine();// 接收数据
		return (line.trim());
	}
}
