package alan.Threads;

import javax.swing.JOptionPane;

import alan.Event.ClientAction;
import alan.Internet.Impl.ClientListRepairTCP;
import alan.Internet.Impl.MainSenRecTCP;
import alan.User.DatabaseRead;
import alan.User.UserList;
import alan.Viem.SingleChatRun;
import alan.Viem.subset.GUIModifyPassword;

/**
 * 此线程：用于维护用户列表用。
 * 1，主要用于“删”“增”联系人。
 * 2，次要用于接收远程服务端命令（既：服务端要求此客户端退出！服务端要求此客户端唤醒单聊线程）
 * */
public class ClientListRepairThread implements Runnable 
{
	private String account;//发送用
	private StringBuffer sb = new StringBuffer();
	
	public ClientListRepairThread(String account) {
		this.account = account;
	}
	
	@Override
	public void run() 
	{
		//先发送姓名，三次握手（此处是元服务端建立“维护列表线程”的链接，port=33333）
		ClientListRepairTCP.senData(account);

		while(true) 
		{
			try {
				//如果对方发来数据
				sb.append(ClientListRepairTCP.recData());
				//此线程中，接收的一定是[标记+姓名]。其中标记“+”表示增加联系人，“-”标记表示移除某个联系人。
				if (sb.toString().startsWith("+")) {
					UserList.join((sb.toString().substring(1)));
					ClientAction.Tip.setText("在线联系人：" + UserList.count());// 标签变动
				} 
				else if (sb.toString().startsWith("-")) {
					UserList.delete((sb.toString().substring(1)));
					ClientAction.Tip.setText("在线联系人：" + UserList.count());// 标签变动
				}
				// 这里是接收服务端的“移除客户端”命令。
				else if(sb.toString().startsWith("REMOVE")){
					MainSenRecTCP.Close();
					ClientListRepairTCP.Close();
				}
				//服务器“激活单聊线程”-被动激活
				else if(sb.toString().startsWith("Single")){
					new SingleChatRun("被动激活单聊GUI");
				}
				else if(sb.toString().startsWith("m")){
					String line = sb.toString().substring(1);
					String[] data = line.split("=");
					DatabaseRead.setName(data[0], data[1]);//更新数据库(内存)
					UserList.refreshList();//刷新本地内存好友列表
				}
				else if(sb.toString().equals("Success")){
					JOptionPane.showMessageDialog(null, "密码更改成功！", "提示", JOptionPane.PLAIN_MESSAGE);
					GUIModifyPassword.ThisObject.dispose();//关闭此窗口
				}
				else if(sb.toString().equals("Failure")){
					JOptionPane.showMessageDialog(null, "原密码错误，请重新输入！","提示",JOptionPane.WARNING_MESSAGE);
				}
				
				//清空数据
				sb.delete(0, sb.length());
			} catch (NullPointerException e) {
				break;
			}
		}
	}
}