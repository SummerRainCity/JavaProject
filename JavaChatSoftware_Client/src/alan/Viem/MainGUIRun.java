package alan.Viem;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

import alan.Event.ClientAction;
import alan.Event.StartAction;

/* 
 * 《客户端聊天主界面》
 * 1，设置了各个界面的字体
 * */
@SuppressWarnings("serial")
public class MainGUIRun extends JFrame
{
	//快捷键相关
	private Container cont = this.getContentPane();
	private JRadioButton JRB_one = new JRadioButton("Enter", true);
	private JRadioButton JRB_two = new JRadioButton("Ctrl+Enter");
	private JPanel JRB_panl = new JPanel();
	//菜单栏项
	JMenuBar JMenuBar;//菜单栏
	JMenu Jstart, Jfriend;//菜单
	JMenuItem JMI_sendFile,JMI_about,JMI_Setting;//Jstart的菜单项
	JMenuItem JMI_singleChat,JMI_allInfo, JMI_myInfo;//Jfriend的菜单项
	//文本区域
	private JTextArea JTArecord = StartAction.JTArecord;//聊天记录
	private static JTextArea JTAlist = StartAction.JTAlist;//联系人列表
	private JTextArea JTAmessage = new JTextArea();//消息框（参数是初始化信息）
	private JButton ButtBsend = new JButton("发送(S)");//“发送”按钮
	private JButton ButtMSGEmpty = new  JButton("清空记录(E)");
	private JLabel Tip = new JLabel("在线好友(O)");//此标签将不断变化-ClientListRepairThread线程控制

	//无参构造
	public MainGUIRun(String strInfo) 
	{
		//布局（东西南北）
//		super.setTitle("Client："+UserList.Account);
		super.setTitle("Client："+strInfo);
		super.setLayout(null);//自由布局
		super.setSize(700, 510);// 长宽
		super.setResizable(false); // 禁止调整大小
		super.setLocationRelativeTo(null);// 窗体在屏幕中心居中（系统方法）
		super.setIconImage(new ImageIcon("datarelated/conf/security/policy/unlimited/ClientIMain.gif").getImage());//设置了窗体图标

		//给组件设置属性（组件的位置）
		init();

		//设置界面各个框的属性（字体大小）
		setAttribute();
		
		//this.setVisible(true);// 显示窗体（应由主调者使用）
		setActionMethod();//各个组件的事件处理交给ClientAction类
	}

	//初始化窗体（添加各个组件及位置）
	private void init() 
	{
		//添加菜单栏
		JPanel panel=new JPanel();//创建一个小的面板
		panel.setLayout(new BorderLayout());//将次面板设置为流式布局;
		//菜单栏设置
		JMenuBar = new JMenuBar();// 菜单栏
		
		Jstart = new JMenu("选项");// 菜单
		JMI_sendFile = new JMenuItem("上传文件(F)");// 菜单项
		JMI_Setting = new JMenuItem("界面设置(S)");
		JMI_about = new JMenuItem("帮助(H)");// 菜单项
		Jstart.add(JMI_sendFile); // 添加子菜单项
		Jstart.add(JMI_Setting);
		Jstart.add(JMI_about);
		
		Jfriend = new JMenu("好友");// 菜单
		JMI_singleChat  = new JMenuItem("单人聊天(C)");
		JMI_allInfo  = new JMenuItem("好友信息(L)");
		JMI_myInfo  = new JMenuItem("个人信息(M)");
		Jfriend.add(JMI_singleChat);
		Jfriend.add(JMI_allInfo);
		Jfriend.add(JMI_myInfo);
		
		JMenuBar.add(Jstart);// 向“菜单栏”添加“选项”
		JMenuBar.add(Jfriend);

		panel.add(JMenuBar,BorderLayout.NORTH);//先添加菜单到JFrame（北方）
		panel.setBounds(0, 0, 684, 24);//设置面板panel在Frame中的位置
		this.add(panel);
		
		//快捷键
		JRB_panl.setBorder(BorderFactory.createTitledBorder("发送消息[快捷键]"));
		JRB_panl.setBounds(500, 345, 166, 90);
		// 设置排版
		JRB_panl.setLayout(null);
		// 面板中加入两个单选按钮
		JRB_panl.add(this.JRB_one);
		JRB_panl.add(this.JRB_two);
		// 设置按钮组(防止多选)
		ButtonGroup group = new ButtonGroup();
		group.add(this.JRB_one);
		group.add(this.JRB_two);
		JRB_one.setBounds(10, 20, 150, 30);
		JRB_two.setBounds(10, 50, 150, 30);
		// 面板加入到框架的容器中
		cont.add(JRB_panl);
		this.add(JRB_panl);
		
		//聊天记录
		JPanel panel1=new JPanel();//创建一个普通面板
		panel1.setBounds(15, 30, 470, 300);//设置普通面板位置和大小，不能省略
		panel1.setLayout(new BorderLayout());//让JTextArea平铺整个JPanel
		//文本域嵌入普通面板后用setBounds()设置JTextArea好像并没有用
		//可以很明显看出仅在有文字的部分会显示白色，其他部分显示灰色。
		JTArecord.setLineWrap(true);//不允许左右滚动条出现
		panel1.add(JTArecord);//将文本域添加进普通面板
		JScrollPane scrol1 = new JScrollPane();//创建滚动条面板
		scrol1.setBounds(15, 30, 470, 300);//设置滚动条面板位置和大小
		/*将普通面板嵌入带滚动条的面板，所在位置由后者决定*/
		scrol1.getViewport().add(panel1);//（这是关键！同样不能用add）将普通面板加到滚动面板里
		scrol1.setBorder(new TitledBorder(new EtchedBorder() , "消息记录" , TitledBorder.CENTER ,TitledBorder.TOP));//添加边框
		this.add(scrol1);//将滚动条面板加到大窗体
		
		// 标签：在线联系人
		Tip.setBounds(505, 30, 140, 20);
		this.add(Tip);
		
		//联系人列表
		JPanel panel2=new JPanel();
//		panel2.setBounds(505, 55, 160, 275);
		panel2.setBounds(505, 55, 160, 271);
		panel2.setLayout(new BorderLayout());
		panel2.add(JTAlist);
		JScrollPane scrol2 = new JScrollPane();
//		scrol2.setBounds(505, 55, 160, 275);
		scrol2.setBounds(505, 55, 160, 271);
		scrol2.getViewport().add(panel2);
		this.add(scrol2);
		
		//消息框
		JPanel panel3=new JPanel();
		int ZY = 15,SX = 345;//位置
		panel3.setBounds(ZY, SX, 470, 90);
		panel3.setLayout(new BorderLayout());
		JTAmessage.setLineWrap(true);//不允许左右滚动条出现
		panel3.add(JTAmessage);
		JScrollPane scrol3 = new JScrollPane();
		scrol3.setBounds(ZY, SX, 470, 90);
		scrol3.getViewport().add(panel3);
		scrol3.setBorder(new TitledBorder(new EtchedBorder() , "编辑消息" , TitledBorder.LEFT ,TitledBorder.TOP));//添加边框
		this.add(scrol3);
		
		//清空聊天记录
		ButtMSGEmpty.setBounds(270, 440, 120, 25);
		this.add(ButtMSGEmpty);
		
		//添加按钮（消息发送）
		ButtBsend.setBounds(404, 440, 80, 25);
		this.add(ButtBsend);
	}
	
	public void setAttribute()
	{
		JTArecord.setEditable(false);//设置“只读”模式
		//JTArecord.setEnabled(false);//设置“只读”模式（不可选）
		JTArecord.setBackground(new Color(175,172,238));//设置背景颜色
		JTArecord.setFont(new Font("微软雅黑", Font.LAYOUT_LEFT_TO_RIGHT, 13));//设置字体

		//联系人列表相关设置
		JTAlist.setFont(new Font("微软雅黑", Font.ITALIC, 12));//设置字体
		JTAlist.setBackground(new Color(185,132,234));//设置背景颜色
		JTAlist.setEditable(false);
		
		//消息框
		JTAmessage.setFont(new Font("微软雅黑", Font.ROMAN_BASELINE, 14));//ITALIC是斜体
		
		// 设置"标签"字体
		Tip.setFont(new Font("微软雅黑", Font.ITALIC, 13));
	}
	
	//把需要处理的事件，交给actionEvent类
	private void setActionMethod() 
	{
		// 创建事件处理对象（第一个参数是：JFrame）
		ClientAction ca = new ClientAction(this,JTArecord, JTAmessage, ButtBsend, ButtMSGEmpty,  JRB_one, JRB_two,Tip);//传递this是要在主界面作提示
		JMI_sendFile.addActionListener(ca);
		JMI_singleChat.addActionListener(ca);
		JMI_about.addActionListener(ca);
		JMI_Setting.addActionListener(ca);
		JMI_allInfo.addActionListener(ca);
		JMI_myInfo.addActionListener(ca);
		// 单选按钮设置监听器
		JRB_one.addItemListener(ca);
		JRB_two.addItemListener(ca);
	}
}