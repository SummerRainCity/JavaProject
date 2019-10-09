package alan.Thread.SingleChat;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.Socket;

import alan.Internet.TCPSingleChat;

public class SingleChatThSon_two implements Runnable
{
	private Socket socket_one;
	private Socket socket_two;
	private StringBuffer sb = new StringBuffer();
	private TCPSingleChat TSC;
	
	public SingleChatThSon_two() {}
	public SingleChatThSon_two(TCPSingleChat TSC) {
		this.TSC = TSC;
		this.socket_one = TSC.socket_one;
		this.socket_two = TSC.socket_two;
	}
	
	@Override
	public void run() 
	{
		try {
			TSC.senData("----你发起了会话【系统提示】----\r\n", socket_one);
		} catch (UnsupportedEncodingException e1) {} catch (IOException e1) {}

		while (true)
		{
			try {
				sb.append(TSC.recData(socket_two));
				TSC.senData(sb.toString(), socket_one);
			} catch (UnsupportedEncodingException e) {
				break;
			} catch (IOException | NullPointerException e) {
				break;
			}
			sb.delete(0, sb.length());
		}
		try {
			TSC.senData("----对方已关闭此窗口【系统提示】----", socket_one);
			socket_two.shutdownOutput();//告诉对应客户端，不再转发数据。
			socket_one.shutdownOutput();
			socket_one.close();
			socket_two.close();
		} catch (UnsupportedEncodingException e) {} catch (IOException e) {}
	}
}
