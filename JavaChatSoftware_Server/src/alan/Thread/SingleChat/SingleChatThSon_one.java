package alan.Thread.SingleChat;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.Socket;

import alan.Internet.TCPSingleChat;

public class SingleChatThSon_one implements Runnable
{
	private String name_one;
	private Socket socket_one;
	private Socket socket_two;
	private TCPSingleChat TSC;//此线程操作
	private StringBuffer sb = new StringBuffer();
	
	public SingleChatThSon_one() {}
	public SingleChatThSon_one(TCPSingleChat TSC,String name_one) {
		this.TSC = TSC;
		this.name_one = name_one;
		this.socket_one = TSC.socket_one;
		this.socket_two = TSC.socket_two;
	}

	@Override
	public void run() 
	{
		try { //提示：XXX向你发起会话
			TSC.senData("---"+name_one+"向你发起会话【系统提示】---\r\n", socket_two);
		} catch (UnsupportedEncodingException e1) {} catch (IOException e1) {}
		
		while (true)
		{
			try {
				sb.append(TSC.recData(socket_one));//接受“我的”数据
				TSC.senData(sb.toString(), socket_two);//转发给two
			} catch (UnsupportedEncodingException e) {
				break;
			} catch (IOException | NullPointerException e) {
				break;
			}
			sb.delete(0, sb.length());// 情况缓冲区
		}
		try {
			TSC.senData("---对方已关闭此窗口【系统提示】---", socket_two);
			socket_two.shutdownOutput();//告诉对应客户端，不再转发数据。
			socket_one.shutdownOutput();
			socket_one.close();
			socket_two.close();
		} catch (UnsupportedEncodingException e) {} catch (IOException e) {}
	}
}
