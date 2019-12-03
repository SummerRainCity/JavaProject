package alan.Event;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import alan.FileIO.ReceiveFile;
import alan.Internet.Impl.IP_About;
import alan.Internet.Impl.MainSenRecTCP;
import alan.Threads.ClientListRepairThread;
import alan.User.UserList;
import alan.Viem.MainGUIRun;

/**《初级界面(既：登录界面的)-事件处理》
 * 注：public String getActionCommand()是ActionEvent类下的一个返回
 * 按钮事件标签的一个方法，可以用于判断用户到底按下了那个按钮
 * 
 * e.getSource()则是返回事件的对象，这种用法适合在WindowRun类直接实现ActionListener
 * 最好各个按钮对象均在类成员位置
 * */
//处理登录窗体事件
public class StartAction implements ActionListener
{
	//用户姓名（指向StartGUIRun类中的new的JTextFiel，是传入的参数）
	private JTextField JTuserName;//账号
	private JPasswordField Jpassword;//文本框（输入密码）
//	private JCheckBox like_one, like_two;
	public static JTextArea JTArecord;//聊天记录（传递）
	public static JTextArea JTAlist;//联系人列表（维护联系人列表线程用）
	
	static {
		JTAlist = new JTextArea();//初始化联系人列表
		JTArecord = new JTextArea();//聊天记录（传递）
	}
	
	//用户提示消息声明
	private JFrame jframe;//用于提示，别赋值为null
	JLabel msg = new JLabel();//仅用于消息提示
	
	//构造接收的是StartGUIRun类的JFrame变量，用于配合提示消息用。
	public StartAction(JFrame J,JTextField JTuserName, JPasswordField Jpassword,JCheckBox like_one,JCheckBox like_two) {
		this.jframe = J;
		this.JTuserName = JTuserName;
		this.Jpassword = Jpassword;
//		this.like_one = like_one;
//		this.like_two =  like_two;

		//给文本框添加按键事件，检测Enter键
		init();
	}
	
	//字符串处理-缓冲字符
	private StringBuilder sbAccount = new StringBuilder();//用于“账号”
	private StringBuilder sbPassword = new StringBuilder();//用于“密码”
	
	private void init() 
	{
		//添加“姓名文本框”监听：用户在消息框是否按下了Enter键
		JTuserName.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent evt) {
				if (evt.getKeyChar() == KeyEvent.VK_ENTER) {
					Jpassword.requestFocus();// 聚焦
				}
			}
		});
		
		//添加“密码文本框”监听：用户在消息框是否按下了Enter键
		Jpassword.addKeyListener(new KeyAdapter() 
		{
			public void keyPressed(KeyEvent evt) {
				if (evt.getKeyChar() == KeyEvent.VK_ENTER) {
					try {
						linkServer();
					} catch (Exception e2) {
						msg.setText("服务器未启动或网络异常！");
						JOptionPane.showMessageDialog(jframe,msg,"连接异常",JOptionPane.WARNING_MESSAGE);//警告提示框
						JTuserName.requestFocus();//聚焦姓名文本框
						sbAccount.delete(0, sbAccount.length());//清空sb数据
						return;//出现问题退回去
					}
				}
			}
		});
	}

	@Override
	public void actionPerformed(ActionEvent e) 
	{
		/****************暂时不调复选框*****************
		//复选框监听
		Object obj = e.getSource(); // 获取事件对象，时间对象的变量名
		if(obj == like_one) {
			boolean flag = like_one.isSelected();//判断该复选框是否为选中状态
			if (flag)
				System.out.println("勾选了记住密码");
			else
				System.out.println("取消了记住密码");
		}
		if(obj == like_two) {
			boolean flag = like_two.isSelected();
			if (flag)
				System.out.println("勾选了自动登录");
			else
				System.out.println("取消了自动登录");
		}
		*******************************************/

		if(e.getActionCommand()=="登录") 
		{
			try {
				jframe.setTitle("连接中...");//IP_About.IPaddress
				linkServer();
			} catch (Exception e2) {
				msg.setText("服务器未启动或网络异常！");
				jframe.setTitle("连接异常，请检查网络或者重启窗口！");
				JOptionPane.showMessageDialog(jframe,msg,"连接异常",JOptionPane.WARNING_MESSAGE);//警告提示框
				JTuserName.requestFocus();//聚焦姓名文本框
				sbAccount.delete(0, sbAccount.length());//清空sb数据
				//e2.printStackTrace();//程序检测用，实际应用则注释掉
				return;//出现问题退回去
			}
		}
		else if(e.getActionCommand()=="注册") {
			JOptionPane.showMessageDialog(jframe, "不予注册！", "提示", JOptionPane.PLAIN_MESSAGE);
		}
		else if(e.getActionCommand()=="设置") {
			new SettingsGUI(jframe);
		}
		else if(e.getActionCommand()=="说明") {
			JOptionPane.showMessageDialog(jframe, 
					"1、提示音：“滴滴滴”：有消息\r\n"+
					"2、提示音：“咳咳”：有人上线\r\n"+
					"3、提示音：“冒泡”：有人下线\r\n"+
					"4、Alt+Enter：快捷发送消息\r\n"+
					"5、Enter：单聊快捷发送消息",
					"说明",JOptionPane.DEFAULT_OPTION);
		}
		else if(e.getActionCommand()=="帮助") {
			JOptionPane.showMessageDialog(jframe, "Author：AlancoldCity\r\nTest Version：1.5\r\nSpecial Thanks：plus、CheatE\r\nEmail：AlanLw721@outlook.com","ChatRoom Test Version 1.5",JOptionPane.DEFAULT_OPTION);
		}
	}
	
	@SuppressWarnings("deprecation")
	private void linkServer() throws InterruptedException 
	{
		sbAccount.append(JTuserName.getText());//获取文本框内容：“账号”
		sbPassword.append(Jpassword.getText());
		UserList.Account = sbAccount.toString();
		UserList.Password = sbPassword.toString();
		
		//账号、密码不能为空
		if(UserList.Account.equals("") || UserList.Password.equals("")) {
			jframe.setTitle("MY");
			msg.setText("账号或密码不能为空！");
			JOptionPane.showMessageDialog(jframe,msg,"提示",JOptionPane.WARNING_MESSAGE);//警告提示框
			JTuserName.requestFocus();//聚焦姓名文本框
			return;
		}

		//正则表达式检测，“姓名”是否符合规范
//		String pattern = "[\u4e00-\u9fa5]{2,6}"; //限制2~6个汉字组成
		String pattern = "[\\d]{4,11}"; //限制4~11数值组成
		boolean isOK = sbAccount.toString().matches(pattern);//正则判断
		
		if(!isOK) {
			jframe.setTitle("MY");
			JOptionPane.showMessageDialog(jframe,"账号不符合规范！","Tip",JOptionPane.WARNING_MESSAGE);//警告提示框
			sbAccount.delete(0, sbAccount.length());//情况缓冲区数据
			JTuserName.requestFocus();
			return;
		}
		
		//发送“账号”+“密码”至服务器（端口22222）
		MainSenRecTCP.sendDataChar(UserList.Account + "=" + UserList.Password);
		
		String strInfo = MainSenRecTCP.receiveDataByte();//接收服务端对应自己的昵称
		if(strInfo.equals("X")) {
			jframe.setTitle("账号或密码错误");
			JOptionPane.showMessageDialog(jframe,"账号或密码错误！","提示",JOptionPane.WARNING_MESSAGE);//警告提示框
			return;
		}else {
			
			//接收来自服务器内存的好友数据（文件）
			new ReceiveFile().recFile();
			
			UserList.Password = null;//清空本地密码（已经登录成功）
			//调用“主界面”
			RunMainGUI(strInfo); //strInfo参数由服务端ClientLinkThreadAddSon线程发送，链接成功后发的是昵称
		}
		
		//开启“联系人列表”维护线程（端口33333）//严格来讲叫做命令接收线程。
		ClientListRepairThread surt = new ClientListRepairThread(UserList.Account);
		Thread surtth = new Thread(surt, "联系人列表维护线程");
		surtth.setDaemon(true);// “保护线程”
		surtth.start();// 开启
	}

	private void RunMainGUI(String strInfo) {
		//客户端代码（正式开始聊天）
		MainGUIRun client = new MainGUIRun(strInfo);
		//释放与当前所有相关的资源
		jframe.dispose();//释放jframe窗体
		client.setVisible(true);// 显示窗体
		sbAccount = null;msg = null;JTuserName = null;
	}
}

//设置
@SuppressWarnings("serial")
class SettingsGUI extends JFrame
{
	private JButton ButtonOk = new JButton("确定");
	private JLabel Lable = new JLabel("IP address:");//标签“昵称”
	private JTextField JT_IPAddress = new JTextField("已使用默认地址");
	
	public SettingsGUI(JFrame jframe) 
	{
		//布局（东西南北）
		super.setTitle("设置");
		super.setLayout(null);//布局
		super.setSize(320, 110);
		super.setResizable(false);
		super.setLocationRelativeTo(jframe);//窗体在指定窗口居中（系统方法）
		super.setIconImage(new ImageIcon("datarelated/conf/security/policy/unlimited/ClientIStart.gif").getImage());//设置了窗体图标
		
		init();//窗体初始化
		this.setVisible(true);//显示窗体
	}
	
	//位置与大小
	private void init() {
		// 关闭按钮
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				Exit();
			}
		});
		//标签
		Lable.setBounds(8, 10, 180, 27);
		Lable.setFont(new Font("宋体", Font.BOLD, 12));
		this.add(Lable);
		
		//文本框
		JT_IPAddress.setBounds(90, 10, 120, 23);
		this.add(JT_IPAddress);
		
		//按钮
		ButtonOk.setBounds(220, 10, 70, 24);
		ButtonOk.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				ButtonOkMethod();
			}
		});
		this.add(ButtonOk);
	}
	
	protected void ButtonOkMethod() {
		IP_About.IPaddress = JT_IPAddress.getText();
		this.dispose();
	}

	private void Exit() {
		this.dispose();
	}
}
