package alan.Internet;

/**
 * @author Ruoton
 * 发送、接收接口。具体实现
 * -------------------------
 */
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
/**
 * 此类作用：
 * 1，主要-提供用户之间“消息”之间的传输(端口：22222)
 * 2，此类定义为：公共聊天静态类(Main)
 * */
public class TCPSenRecMain
{
	private static final String CODED = "UTF-8";//定义字符流中“发送”与“接收”的编码
	public static ServerSocket ss; //服务端口
	public static Socket socket; //具体用户

	//构造
	public TCPSenRecMain() {}
	
	static {
		try {
			ss = new ServerSocket(IP_About.MainProt);
		} catch (IOException e) {
			System.out.println("服务端初始化SeverSocket失败");
			e.printStackTrace();
		}
	}
	
	//发送数据方法（字节流）
	public synchronized static void sendDataByte(String str, Socket s) {
		//发送数据（字节流）
		OutputStream sendIO;
		try {
			//获取此套接字的发送流
			sendIO = s.getOutputStream();
			//将str数据发送到socket指定的网络位置
			sendIO.write(str.getBytes());
			sendIO.flush();
		} catch (IOException e) {
			System.out.println("发送数据失败！");
			e.printStackTrace();System.exit(0);
		}
	}

	//接收数据（阻塞似方法，接收数据方法-字节流）
	public static String receiveDataByte() {
		byte[] bys = new byte[2048];
		int len = 0;//将保存接收数据的长度
		try {
			//接收数据流
			InputStream receiveIO = socket.getInputStream();
			//接收数据
			len = receiveIO.read(bys); // 阻塞方法，等待对方发送数据
		} catch (IOException e) {
			System.out.println("接收数据异常：IOException");
			e.printStackTrace();
		}
		return new String(bys, 0, len).trim();//返回的字符串处理了两端空白
	}
	
	//发送数据（字符流）
	public static void sendDataChar(String str, Socket s) {
		// 把通道内流包装(包装Socket通道传输流，使用BufferedWriter的操作进行通信)
		BufferedWriter bw;
		try {
			bw = new BufferedWriter(new OutputStreamWriter(s.getOutputStream(),CODED));//指定了发送编码
			bw.write(str);//发送数据
			bw.newLine();
			bw.flush();
		} catch (IOException e) {
			System.out.println("发送数据异常！");
		}
	}
	
	//接收数据（阻塞似方法，接收数据方法-字符流）
	public static String receiveDataChar(Socket socket) {
		//发送数据，字符流
		BufferedReader br;
		String line = null;
		try {
			br = new BufferedReader(new InputStreamReader(socket.getInputStream(),CODED));//指定了接收编码
			line = br.readLine();//接收数据
		} catch (IOException e) {
			System.out.println("字符流接收数据异常：IOException");
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