package alan.Internet.Impl;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * 此类作用：仅用于一对一之间的通信
 */
public class SingleChatTCP {
	private final String IPaddress = IP_About.IPaddress;
	private final int Prot = IP_About.SingleProt;// 端口:32600
	public Socket socket; // 地址+端口号=>Socket
	private BufferedReader br;
	private BufferedWriter bw;
	private final String CODED = "UTF-8";// 定义字符流中“发送”与“接收”的编码

	//不能无限创建对象
	public SingleChatTCP() {
		try {
			socket = new Socket(IPaddress, Prot);// 初始化套接字（三次握手）
		} catch (UnknownHostException e) {
		} catch (IOException e) {
		}
	}

	// 发送数据（字符流）
	public void senData(String str) throws UnsupportedEncodingException, IOException {
		bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), CODED));// 指定了发送编码
		bw.write(str);// 发送数据
		bw.newLine();
		bw.flush();
	}

	// 发送数据（指定Socket）-JFrame类型变量，用于窗口提示
	public boolean senData(String str, Socket sock) {
		try {
			bw = new BufferedWriter(new OutputStreamWriter(sock.getOutputStream(), CODED));// 指定了发送编码
			bw.write(str);// 发送数据
			bw.newLine();
			bw.flush();
		} catch (IOException e) { // 如果对方已经断开链接，则执行下面的语句
			return true;
		}
		return false;
	}

	// 接收数据（阻塞似方法，接收数据方法-字符流）
	public String recData() {
		String line = null;
		try {
			br = new BufferedReader(new InputStreamReader(socket.getInputStream(), CODED));// 指定了接收编码
			line = br.readLine();// 接收数据
		} catch (IOException e) {
			System.out.println("不能再发送数据");
		}
		return (line.trim());
	}

	// 接收数据（指定Socket）
	public String recData(Socket sock) {
		String line = null;
		try {
			br = new BufferedReader(new InputStreamReader(sock.getInputStream(), CODED));// 指定了接收编码
			line = br.readLine();// 接收数据
		} catch (IOException e) {
			System.out.println("不能再接收数据");
		}
		return (line.trim());
	}

	// 释放资源
	public void Close() {
		try {
			socket.shutdownOutput();// 告诉服务器不再发送数据。
			socket.shutdownInput();// 告诉服务器不再接受数据。
			socket.close();
		} catch (IOException e) {
		}
	}
}
