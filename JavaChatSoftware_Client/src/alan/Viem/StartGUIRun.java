


package alan.Viem;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import alan.Event.StartAction;
import alan.Util.UiUtil;
/* 客户端
 * 本类仅负责界面各个组件的属性设置
 * */
@SuppressWarnings("serial")
public class StartGUIRun extends JFrame
{
	JMenuBar menuBar;//菜单栏
	JMenu menu;//菜单
	JMenuItem JMI_one,JMI_help,JMI_show;//菜单项（分别是：“设置”，“帮助”）
	JLabel biaoq_nc,biaoq_mm;//标签“昵称”，标签“密码”
	JTextField JFuserAccount;//文本框（输入姓名）
	JPasswordField Jpassword;//文本框（输入密码）
	JButton Butt_Login, Butt_register;//按钮“登录”与“注册”
	JCheckBox like_one, like_two; //两个复选框：“记住密码”与“自动登录”
	JPanel Jpl;//JPanel容器
	
	//int number = (int)(Math.random()*100)+1;//电脑产生随机数（1~100）
	
	public StartGUIRun() 
	{
//		UiUtil.setBackMap(this, "1.jpg");// 设置背景图
		UiUtil.setBackMap(this, ((int)(Math.random()*9)+1)+".jpg");//随机- 设置背景图

		//布局（东西南北）
		super.setTitle("MY");
		super.setLayout(new BorderLayout());//流式布局
		super.setSize(480, 350);//长宽450, 350
		
		super.setResizable(false); //禁止调整大小
		//用户点退出程序
		super.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
                    System.exit(0);
			}
		});
		
		super.setLocationRelativeTo(null);//窗体在屏幕中心居中（系统方法）
		//datarelated/conf/security/policy/unlimited/ClientIStart.gif
		super.setIconImage(new ImageIcon("datarelated/conf/security/policy/unlimited/ClientIStart.gif").getImage());//设置了窗体图标
		
		init();//窗体初始化
		setActionMethod();//将本类的“姓名”和相关“按钮”加事件
		this.setVisible(true);//显示窗体
	}
	
	//窗体初始化
	private void init() {
		//菜单栏设置
		menuBar = new JMenuBar();//菜单栏
		menu = new JMenu("开始");//菜单
		JMI_one = new JMenuItem("设置");//菜单项
		JMI_show = new JMenuItem("说明");
		JMI_help = new JMenuItem("帮助");//菜单项
		menu.add(JMI_one); //添加菜单项
		menu.add(JMI_show);
		menu.add(JMI_help);
		menuBar.add(menu);//向“菜单栏”添加“菜单”
		
		//两个标签
		biaoq_nc = new JLabel("账号");//标签
		biaoq_mm = new JLabel("密码");//标签
		biaoq_nc.setForeground(new Color(255, 255, 255));//“昵称”字体颜色
		biaoq_nc.setFont(new Font("宋体", Font.BOLD, 18));
		biaoq_nc.setBounds(100, 40, 180, 27);//“标签”昵称位置大小
		biaoq_mm.setForeground(new Color(255, 255, 255));//“昵称”字体颜色
		biaoq_mm.setFont(new Font("宋体", Font.BOLD, 18));
		biaoq_mm.setBounds(100, 120, 180, 27);//“标签”位置大小
		
		//两个文本框
		JFuserAccount = new JTextField(15);//文本框（输入昵称）
		JFuserAccount.setBounds(145, 40, 200, 27);//“文本框”位置大小
		JFuserAccount.setFont(new Font("宋体", Font.BOLD, 16));
		Jpassword = new JPasswordField();//文本框（输入密码）
		Jpassword.setBounds(145, 120, 200, 27);//“文本框”位置大小
		
		//两个按钮
		Butt_Login = new JButton("登录");
		Butt_Login.setBounds(100, 200, 100, 27);//位置大小（左右，上下）
		Butt_register =  new JButton("注册");
		Butt_register.setBounds(250, 200, 100, 27);//位置大小
		
		//两个复选框
		like_one = new JCheckBox("记住密码");
		like_one.setBounds(100, 160, 75, 16);
		like_two = new JCheckBox("自动登录");
		like_two.setBounds(270, 160, 75, 16);
		
		//JPanel容器
		Jpl = new JPanel();
		Jpl.setOpaque(false);//把背景面板变成透明
		Jpl.setLayout(null);//自由布局
		//Jpl.setBackground(Color.CYAN);//背景颜色
		
		this.add(menuBar,BorderLayout.NORTH);//先添加菜单到JFrame（北方）
		//把其余主键添加到JPanel
		Jpl.add(biaoq_nc);//向JLabel添加“标签”
		Jpl.add(biaoq_mm);
		Jpl.add(JFuserAccount);//向JLabel添加“文本框”
		Jpl.add(Jpassword);
		Jpl.add(Butt_Login);//向JLabel添加“链接”按钮
		Jpl.add(Butt_register);//向JLabel添加“注册”按钮
		Jpl.add(like_one);
		Jpl.add(like_two);
		this.add(Jpl);//向JFrame添加JPanel
	}
	
	//把需要处理的事件，交给actionEvent类
	private void setActionMethod() {
		//创建事件处理对象（传递：当前对象JFrame、文本框）
		StartAction actionEvent = new StartAction(this,JFuserAccount, Jpassword, like_one, like_two);
		// 给界面可能被点击的按钮添加事件监听
		Butt_Login.addActionListener(actionEvent);
		Butt_register.addActionListener(actionEvent);
		JMI_one.addActionListener(actionEvent);
		JMI_help.addActionListener(actionEvent);
		JMI_show.addActionListener(actionEvent);
		like_one.addActionListener(actionEvent);//给复选框添加事件监听
		like_two.addActionListener(actionEvent);//给复选框添加事件监听
	}
}