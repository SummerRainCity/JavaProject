package alan.Viem;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.Socket;

import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

import alan.Internet.Impl.SingleChatTCP;
import alan.Threads.SingleChat.SingleChatReceiveThread;
import alan.User.DatabaseRead;
import alan.User.UserList;

/**
 * 《单人聊天-总》
 * 依赖关系：本类(SingleChatRun)依赖于MainGUIRun类存在，由MainGUIRun类“单聊”按钮激活。
 * 
 * 构架：界面+功能实现
 * 1，选择界面
 * 2，正式聊天界面
 * 3，此类使用“单列模式”。
 * 
 * 时间：2019年6月25日17:43:41（正在改进中......）
 * */

/************************************************
 * SingleChatRun类：仅提供联系人的【选择联系人->确定】。
 * 状态：已经完全完成。
 * 
 ************************************************/
@SuppressWarnings("serial")
public class SingleChatRun extends JFrame
{
	//单列模式使用
	private static SingleChatRun ThisObject = null;
	
	//组合框（复选框）
	private JComboBox<String> selectBox = new JComboBox<String>();
	//TCP协议对象，用于数据发送与接收
	private SingleChatTCP SCTCP;
	//获取本地联系人列表到选框
	private ComboBoxModel<String> List = new DefaultComboBoxModel<String>(UserList.getList());
	//“发送”按钮
	private JButton ButtOK = new JButton("确定(O)");
	
	//【主动激活】-（有复选框选择联系人...）
	private SingleChatRun(JFrame jframe) 
	{
		super.setTitle("选择账号");
		super.setLayout(null);//自由布局（组件位置自己计算）
		super.setSize(320, 120);// 长，宽
		super.setResizable(false); // 禁止调整大小
		super.setLocationRelativeTo(jframe);// 窗体在jframe中心居中
		super.setIconImage(new ImageIcon("datarelated/conf/security/policy/unlimited/ClientIMain.gif").getImage());//设置了窗体图标
		init();
		//this.setVisible(true);//单列模式->主调控制显示
	}
	
	//【被动激活】（与本类无关，直接调用界面）
	public SingleChatRun(String passive) {
		SCTCP = new SingleChatTCP();//（内部已经三次握手）
		new SingleChatRunSon(SCTCP).setVisible(true);
	}
	
	private void init() 
	{
		//给复选框添加联系人
		selectBox.setModel(List);
		
		//“按钮”、“多选框”位置与大小
		ButtOK.setBounds(200, 27, 80, 25);//左右、上下
		selectBox.setBounds(20, 27, 100, 25);//左右、上下
		
		//“确定”按钮-事件处理
		ButtOK.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				ButtonOK();//调用本类方法，否则这里不能访问selectBox变量
			}
		});
		
		// 关闭按钮
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				Exit();
			}
		});

		this.add(ButtOK);
		this.add(selectBox);
	}
	
	//【主动激活】按下“确定”键-处理
	private void ButtonOK() 
	{
		if(List.getSize() <= 0) { //联系人列表为空不执行下面的语句。
			JOptionPane.showMessageDialog(this,"没有可选择的联系人！","提示",JOptionPane.WARNING_MESSAGE);
			return;
		}
		/************与服务端单聊线程交互*************/
		SCTCP = new SingleChatTCP();//此时此刻内部进行三次握手。。
		//发“自己”的账号给服务器
		try {
			SCTCP.senData(UserList.Account);
		} catch (UnsupportedEncodingException e1) {
			SCTCP.Close();
			JOptionPane.showMessageDialog(this,"建立连接失败，请关闭后重试！","提示",JOptionPane.WARNING_MESSAGE);
			return;
		} catch (IOException | NullPointerException e1) {
			SCTCP.Close();
			JOptionPane.showMessageDialog(this,"建立连接失败，请关闭后重试！","提示",JOptionPane.WARNING_MESSAGE);
			return;
		}
		
		try {
			Thread.sleep(1000);//为什么要延时？因为发太快，服务器接收不过来，会死锁。
		} catch (InterruptedException e) {}
		
		//获取当前所选联系人。
		String selectAccount = this.selectBox.getSelectedItem().toString();
		//发“所选择的名字”，供服务器查找
		try {
			SCTCP.senData(selectAccount);
		} catch (UnsupportedEncodingException e) {} catch (IOException e) {}
		
		//生成单人聊天窗口-（主动激活）
		SingleChatRunSon scmr = new SingleChatRunSon(selectAccount,SCTCP);
		this.dispose();//释放当前窗体
		scmr.setVisible(true);//显示窗口
	}
	
	private void Exit() {
		ThisObject = null;//单列模式标记null
		this.dispose();
	}
	
	//选择窗体->“单列模式”（作用：此类只能创建一次对象）
	public static SingleChatRun SingleColumnMode(JFrame jframe) {
		if (ThisObject == null) {
			ThisObject = new SingleChatRun(jframe);
			return ThisObject;
		} else {
			return ThisObject;
		}
	}
}

/*********************************************
 * SingleChatMainRun类：正式处理一对一聊天
 * 依赖关系：仅由SingleChatRun类激活。
 * 状态：正在完成中......
 ********************************************/
@SuppressWarnings("serial")
class SingleChatRunSon extends JFrame
{
	private SingleChatTCP SCTCP;//TCP协议对象，用于数据发送与接收-被动激活用
	private StringBuilder sb = new StringBuilder();
	private JTextArea Arecord = new JTextArea();//聊天记录大框
	private JTextField JText = new JTextField();//将要发送消息的文本框
	private JButton BuffSend = new JButton("发送(S)");//“发送”按钮
	private String name;//对方名字
	private String MyName = DatabaseRead.getName(UserList.Account);
	private Socket socket;//对方的Socket
	
	//【主动激活】
	public SingleChatRunSon(String account,SingleChatTCP SCTCP) {
		this.SCTCP = SCTCP;
		this.name = DatabaseRead.getName(account);//原本是账号，要从数据库获取昵称
		this.socket = SCTCP.socket;
		super.setTitle("你与"+ this.name + "的对话");
		super.setLayout(null);// 自由布局（组件位置自己计算）
		super.setSize(435, 370);// 长，宽
		super.setResizable(false); // 禁止调整大小
		super.setLocationRelativeTo(null);// 窗体在屏幕中心居中（系统方法）
		super.setIconImage(new ImageIcon("datarelated/conf/security/policy/unlimited/ClientIMain.gif").getImage());//设置了窗体图标
		init(); //对窗体的相关属性初始化
		receiveDate(); //接收数据
		sendData(); //发送数据
	}
	
	//【被动激活】
	public SingleChatRunSon(SingleChatTCP SCTCP) {
		//与服务器单聊TCP建立连接，并接收发起会话方的“名字”
		this.SCTCP = SCTCP;
		this.name = DatabaseRead.getName(SCTCP.recData());//根据发来的账号去找到姓名
		this.socket = SCTCP.socket;//接收的是对方的Socket
		super.setTitle(MyName+"与你的对话");
		super.setLayout(null);//自由布局（组件位置自己计算）
		super.setSize(435, 370);// 长，宽
		super.setResizable(false); // 禁止调整大小
		super.setLocationRelativeTo(null);// 窗体在屏幕中心居中（系统方法）
		super.setIconImage(new ImageIcon("datarelated/conf/security/policy/unlimited/ClientIMain.gif").getImage());//设置了窗体图标
		init(); //对窗体的相关属性初始化
		receiveDate(); //接收数据
		sendData();//处理发送与接收问题
	}

	private void init() 
	{
		//聊天记录框（位置与大小）
		JPanel panel1=new JPanel();
		int ZY = 15,SX = 13;//位置
		panel1.setBounds(ZY, SX, 390, 250);
		panel1.setLayout(new BorderLayout());
		Arecord.setLineWrap(true);//不允许左右滚动条出现
		panel1.add(Arecord);
		JScrollPane scrol1 = new JScrollPane();
		scrol1.setBounds(ZY, SX, 390, 250);
		scrol1.getViewport().add(panel1);
		Arecord.setEditable(false);//设置“只读”模式
		scrol1.setBorder(new TitledBorder(new EtchedBorder() , "聊天记录" , TitledBorder.LEFT ,TitledBorder.TOP));//添加边框
		this.add(scrol1);
		
		//发送消息框（位置与大小）
		JPanel panel2=new JPanel();
		panel2.setBounds(15, 270, 310, 50);
		panel2.setLayout(new BorderLayout());
		JText.setBounds(0, 0, 310, 50);
		panel2.setBorder(new TitledBorder(new EtchedBorder() , "发送消息" , TitledBorder.LEFT ,TitledBorder.TOP));//添加边框
		panel2.add(JText);
		//按钮；位置、大小
		BuffSend.setBounds(328, 275, 75, 42);
		
		//添加组件
		this.add(panel2);
		this.add(BuffSend);
		
		// 关闭按钮-事件监听。
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				Exit();
			}
		});
	}
	
	//“接收”相关
	private void receiveDate() 
	{
		// 开启接收对方消息线程（该线程仅操作this.JTextArea变量）【开启单人消息接收线程】
		SingleChatReceiveThread scrt = new SingleChatReceiveThread(this.name, this.Arecord, this.socket, SCTCP);
		Thread scrtth = new Thread(scrt, "接收消息线程(Single)");
		scrtth.setDaemon(true);// 设置“保护线程”
		scrtth.setPriority(6);
		scrtth.start();
	}
	
	//“发送”相关
	private void sendData() 
	{
		//按钮“发送”事件
		BuffSend.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				buttonSendData();
			}
		});
		
		//文本框“发送”Enter监听
		JText.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent evt) {
				if (evt.getKeyChar() == KeyEvent.VK_ENTER) {
					buttonSendData();
				}
			}
		});
	}

	//点击按钮-发送消息（sendData方法的子方法）
	private void buttonSendData() 
	{
		boolean flag = false;
		sb.append(JText.getText());// 获取文本框信息
		if (sb.toString().isEmpty()) {
			JOptionPane.showMessageDialog(this, "内容不能为空，请重新输入！", "提示", JOptionPane.PLAIN_MESSAGE);
			return;
		}
		flag = SCTCP.senData(sb.toString(), this.socket);// 发送数据
		if(flag) {
			JOptionPane.showMessageDialog(this, "消息发送失败，可能对方已关闭当前窗口或下线！", "提示", JOptionPane.WARNING_MESSAGE);
			return;
		}

		Arecord.append("["+MyName+"]："+sb.toString()+"\r\n");//自己消息更新本地聊天框
		sb.delete(0, sb.length());// 清空缓冲区数据
		JText.setText("");
		JText.requestFocus();// 聚焦文本框
	}
	
	//【主动】释放当前窗体资源，不会退出程序本身。
	private void Exit() {
		SCTCP.Close();// 关闭当前窗口通信的Socket//对方断开链接，线程SingleChatReceiveThread自动释放
		socket = null;
		SCTCP = null;
		this.dispose();
	}
}