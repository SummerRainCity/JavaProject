package alan.Event;
import java.awt.Robot;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;
import javax.swing.JTextArea;

import alan.Internet.Impl.ClientListRepairTCP;
import alan.Internet.Impl.MainSenRecTCP;
import alan.Threads.ReceiveSendMessageThread;
import alan.Viem.SingleChatRun;
import alan.Viem.subset.GUIFriendInfo;
import alan.Viem.subset.GUISettingRun;
import alan.Viem.subset.Personal_Information;

/**《正式聊天-主要》
 * 主界面的---事件处理
 * 
 * 了解一下JTextPane的作用
 * */
public class ClientAction  implements ActionListener,ItemListener
{
	//用户提示消息声明
	private JFrame jframe;//用于提示与居中，别赋值为null（将传入“消息接收线程”提示是否与服务器断开链接）
	private Robot robot;//模拟键盘使用Ctrl+X清空消息框

	//快捷键复选设置
	private boolean JRB_flag = true; //切换快捷键
	private JRadioButton JRB_one = null;//Checkbox、JRadioButton
	private JRadioButton JRB_two = null;
	
	public JTextArea JTArecord;//聊天记录（传入主要）
	private JTextArea JTAmessage;//发送消息的框（本类控制）
	private JButton Bsend;//“发送”按钮（本类控制）
	private JButton ButtMSGEmpty;
	public static JLabel Tip;//ClientListRepairThread线程控制-以ClientAction.Tip.setText("");形式改变此
	
	public ClientAction() {}
	
	//传入JFrame作消息提示（暂时不需要）
	public ClientAction(JFrame jframe, JTextArea jTArecord, JTextArea jTAmessage, JButton bsend, JButton ButtMSGEmpty,JRadioButton JRB_one,JRadioButton JRB_two, JLabel Tip) {
		this.JRB_one = JRB_one;
		this.JRB_two = JRB_two;
		this.jframe = jframe;
		this.JTArecord = jTArecord;
		this.JTAmessage = jTAmessage;
		this.Bsend = bsend;
		this.ButtMSGEmpty = ButtMSGEmpty;
		ClientAction.Tip = Tip;
		init(); //初始化客户端配置
	}

	//初始化客户端配置
	private void init() 
	{
		//启动接收消息线程-聊天消息（静态接收线程-Main）
		ReceiveSendMessageThread crt = new ReceiveSendMessageThread(jframe);
		Thread th = new Thread(crt, "ReceiveChatRecordThread");
		th.setDaemon(true);// 设置“保护线程”，伴随主线程死亡而死亡
		th.start();// 开启“接收消息”线程
		
		//确认信号，可用于让服务器通知所有人XXX上线（主要让让服务器将此用户保存到Map集合）
		MainSenRecTCP.sendDataChar(" ");
		
		//添加“消息框”监听：用户在消息框是否按下了Enter键（发送数据）
		JTAmessage.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent evt) 
			{
				if(JRB_flag) {
					if (evt.getKeyChar() == KeyEvent.VK_ENTER) send();
				}else {
					if (evt.isControlDown() && evt.getKeyChar() == KeyEvent.VK_ENTER) send();
				}
			}
		});
		JTAmessage.requestFocus();//聚焦该窗口
		
		//按钮“发送”事件
		Bsend.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				send();
			}
		});

		//“清空聊天记录”
		ButtMSGEmpty.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int result = JOptionPane.showConfirmDialog(jframe, "是否清空聊天记录？", "提示", JOptionPane.OK_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE);
                if(result == JOptionPane.OK_OPTION){
                	JTArecord.setText("");
                }
			}
		});
		
		//如果用户点击“关闭”按钮
		jframe.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				jframe.setDefaultCloseOperation(0); //必须写，否则会出现点“取消”时该窗体会隐藏。
				int result = JOptionPane.showConfirmDialog(jframe, "你即将退出", "关闭", JOptionPane.OK_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE);
                if(result == JOptionPane.OK_OPTION){
                	//通知服务器，当前用户断开链接
    				MainSenRecTCP.Close();
    				ClientListRepairTCP.Close();
    				System.exit(0);
                }
			}
		});
	}
	
	//使用Ctrl+A 组合 Ctrl+X
	private void emptyJTAmessage() {
		JTAmessage.requestFocus();// 聚焦该窗口
		try {
			robot = new Robot();
		} catch (Exception e) {}
		// 按下键清空
		robot.keyPress(KeyEvent.VK_CONTROL);// 按下Ctrl键
		robot.keyPress(KeyEvent.VK_A);// 按下
		robot.keyRelease(KeyEvent.VK_CONTROL);// 释放
		robot.keyRelease(KeyEvent.VK_A);// 释放
		robot.keyPress(KeyEvent.VK_CONTROL);// 按下Ctrl键
		robot.keyPress(KeyEvent.VK_X);// 按下
		robot.keyRelease(KeyEvent.VK_CONTROL);// 释放
		robot.keyRelease(KeyEvent.VK_X);// 释放
		JTAmessage.requestFocus();// 聚焦该窗口
	}
	
	//发送消息框数据
	private void send() 
	{
		String s = JTAmessage.getText();
		//判断用户是否发送空内容
		if ((s.trim()).equals("")) {
			JOptionPane.showMessageDialog(jframe, "内容不能为空，请重新输入！", "提示", JOptionPane.PLAIN_MESSAGE);// WARNING_MESSAGE
			emptyJTAmessage();// 清空发送消息框
			return;
		} //字数限制128
		else if((JTAmessage.getText().length()) > 128) {
			JOptionPane.showMessageDialog(jframe,"字数超过128","字数超限",JOptionPane.PLAIN_MESSAGE);
			return;
		}
		MainSenRecTCP.sendDataChar((JTAmessage.getText()));//发送数据（静态）
		emptyJTAmessage();//清空发送消息框
	}

	//处理菜单栏
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getActionCommand() == "上传文件(F)") {
			JOptionPane.showMessageDialog(jframe, "还没上传文件的功能！", "选择文件", JOptionPane.PLAIN_MESSAGE);
		}
		else if(e.getActionCommand() == "界面设置(S)") {
			GUISettingRun.SingleColumnMode(jframe).setVisible(true);
		}
		else if(e.getActionCommand() == "帮助(H)") {
			JOptionPane.showMessageDialog(jframe, "Author：AlancoldCity\r\nTest Version：1.5\r\nSpecial Thanks：plus、CheatE\r\nEmail：AlanLw721@outlook.com","ChatRoom Test Version 1.5",JOptionPane.DEFAULT_OPTION);
		}
		
		if(e.getActionCommand() == "好友信息(L)") {
			GUIFriendInfo.SingleMode(jframe).setVisible(true);
		}
		else if(e.getActionCommand() == "单人聊天(C)") {
			SingleChatRun.SingleColumnMode(jframe).setVisible(true);
		}
		else if(e.getActionCommand() == "个人信息(M)") {
			Personal_Information.SingleColumnMode(jframe).setVisible(true);
		}
	}

	//复选框事件处理（快捷键）
	@Override
	public void itemStateChanged(ItemEvent e) 
	{
		if (e.getSource() == JRB_one)JRB_flag = true;
		if (e.getSource() == JRB_two)JRB_flag = false;
	}
}
