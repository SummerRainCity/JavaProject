package alan.Internet.Impl;

import java.io.BufferedReader;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 * 客户端：TCP22222
 * 此类作用：处理用户收发聊天消息
 * */
public class MainSenRecTCP
{
	private static final String IPaddress = IP_About.IPaddress;
	private static final int Prot = IP_About.MainProt;//端口(主)
	private static Socket socket; //地址+端口号
	private static final String CODED = "UTF-8";//定义字符流中“发送”与“接收”的编码
	
	private static OutputStream sendIO;//字节流发送（关闭使用）
	private static InputStream receiveIO;//字节流接收（关闭使用）
	private static BufferedWriter bw;//字符流发送（关闭使用）
	private static BufferedReader br;//字符流接收（关闭使用）
	
	//构造私有：不可创建对象
	private MainSenRecTCP() {}
	
	//静态代码块，初始化套接字
	//注：初次直接调用(static方法)本类任何方法会执行静态代码块一次，往后不再执行！仅做初始化用。
	static {
		try {
			socket = new Socket(IPaddress,Prot);//初始化套接字（三次握手）
		} catch (UnknownHostException e) {
			// System.out.println("初始化Socket异常：UnknownHostException");
			// e.printStackTrace();
		} catch (IOException e) {
			// System.out.println("初始化Socket异常：IOException");
			// e.printStackTrace();
		}
	}
	
	//发送数据方法（字节流）
	public static void sendDataByte(String str) {
		//发送数据（字节流）
		try {
			//获取此套接字的发送流
			sendIO = socket.getOutputStream();
			//将str数据发送到socket指定的网络位置
			sendIO.write(str.getBytes());
		} catch (IOException e) {
			//System.out.println("发送数据失败！");
			//e.printStackTrace();System.exit(0);
		}
	}

	//接收数据（阻塞似方法，接收数据方法-字节流）
	public static String receiveDataByte() {
		byte[] bys = new byte[1024];
		int len = 0;//将保存接收数据的长度
		try {
			//接收数据流
			receiveIO = socket.getInputStream();
			//接收数据
			len = receiveIO.read(bys); // 阻塞方法，等待对方发送数据
		} catch (IOException e) {
			//System.out.println("接收数据异常：IOException");
			//e.printStackTrace();
		}
		return new String(bys, 0, len).trim();//返回的字符串处理了两端空白
	}
	
	//发送数据（字符流）
	public static void sendDataChar(String str) {
		// 把通道内流包装(包装Socket通道传输流，使用BufferedWriter的操作进行通信)
		try {
			bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(),CODED));//指定了发送编码
			bw.write(str);//发送数据
			bw.newLine();//发送换行
			bw.flush();
		} catch (IOException e) {
			try {
				bw.close();
			} catch (IOException e1) {
			}
			System.out.println("发送数据异常！");
		}
	}
	
	//接收数据（阻塞似方法，接收数据方法-字符流）
	public static String receiveDataChar(JFrame JF) {
		//发送数据，字符流
		String line = null;
		try {
			br = new BufferedReader(new InputStreamReader(socket.getInputStream(),CODED));//指定了接收编码
			line = br.readLine();//接收数据
		} catch (IOException e) {
			//System.out.println("字符流接收数据异常：IOException");//一般是断开链接是这样的
			Close();//释放资源
		}
		try {
			line = line.trim(); //断开链接才抛
		} catch (NullPointerException e) {
			JOptionPane.showMessageDialog(JF, "你与服务端断开了链接！", "提示", JOptionPane.ERROR_MESSAGE);
			try {
				br.close();
			} catch (IOException e1) {
			}
			Close();//释放资源
			System.exit(0);
		}
		return line; //一旦返回null，就是严重问题（断开链接！）
	}
	
	//关闭总套接字
	public static void Close() {
		try {
			socket.close();
		} catch (IOException e) {}
	}
}