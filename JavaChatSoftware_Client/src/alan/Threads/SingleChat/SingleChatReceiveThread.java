package alan.Threads.SingleChat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

import javax.swing.JTextArea;

import alan.Internet.Impl.SingleChatTCP;

/**
 * 此线程仅由SingleChatRunSon类控制--依赖于SingleChatRunSon类(单聊主界面)
 * 作用：仅做接收，接收单人聊天中对方的消息。
 * 原理：实际上是接收服务端转发过来的数据，转发的是对方的消息数据。
 */
public class SingleChatReceiveThread implements Runnable
{
	private SingleChatTCP SCTCP;//TCP协议对象，用于数据发送与接收-被动激活用
	private StringBuilder sb = new StringBuilder();
	private BufferedReader br = null;//接收数据用
	private JTextArea Arecord = new JTextArea();//聊天记录框
	private String name;//对方姓名
	private Socket socket;//对方Socket
	
	public SingleChatReceiveThread() {}
	public SingleChatReceiveThread(String account, JTextArea arecord, Socket socket, SingleChatTCP SCTCP) {
		this.SCTCP = SCTCP;
		this.name = account;//原本是账号，要从数据库获取昵称
		this.Arecord = arecord;
		this.socket = socket;
	}

	@Override
	public void run() {
		while(true)
		{
			try {
				br = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));// 指定了接收编码
				sb.append((br.readLine()).trim());// 接收数据
			} catch (IOException | NullPointerException e) {
				break;
			}
			Arecord.append("["+name+"]："+sb.toString()+"\r\n");
			sb.delete(0, sb.length());
		}
		//跳出循环只有一种可能，
		SCTCP.Close();
	}
}
