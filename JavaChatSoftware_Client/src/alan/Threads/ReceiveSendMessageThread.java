package alan.Threads;

import javax.swing.JFrame;

import alan.Event.StartAction;
import alan.Internet.Impl.MainSenRecTCP;
import alan.User.UserList;
import alan.Util.UiUtil;

/* 
 * 此类：仅用接收服务端转发过来的消息
 * 
 * 静态接收！
 * 
 * 保存到：JTArecord
 * */
public class ReceiveSendMessageThread implements Runnable
{
	private JFrame jframe;//用于提示，如果服务器主动断开链接。
	private StringBuffer Msg = new StringBuffer();
	
	public ReceiveSendMessageThread() {}
	
	public ReceiveSendMessageThread(JFrame jframe) {
		this.jframe = jframe;
	}
	
	@Override
	public void run() 
	{
		while (true) 
		{
			// 接收来自服务端的消息
			Msg.append(MainSenRecTCP.receiveDataChar(jframe));
			
			// 分析需要播放什么声音
			playMusic();
			
			// 添加到JTArecord大框
			StartAction.JTArecord.append(Msg.toString() + "\r\n");
			
			// 清空sb
			Msg.delete(0, Msg.length());
		}
	}
	
	//提示音-分析与播放
	private void playMusic() 
	{
		// 来消息提示音，判断逻辑：自己说的话不得有提示音 & 来自服务端的提示用户上线也不得有此提示音。
		if (!(Msg.toString().startsWith("【" + UserList.Account + "】")) && !(Msg.toString().startsWith("----"))) {
			UiUtil.playMsg();
		}

		// 用户上线提示音，判断逻辑：来自服务端的提示自己上线 & 来自服务端通知XXX"下线"，不得有此提示音
		else if (!(Msg.toString().contains(UserList.Account)) && (Msg.toString().contains("[上线]"))) {
			UiUtil.playSys();
		}
		
		// 有用户下线提示音
		else if (Msg.toString().contains("[下线]")) {
			UiUtil.playExit();
		}
	}
}
