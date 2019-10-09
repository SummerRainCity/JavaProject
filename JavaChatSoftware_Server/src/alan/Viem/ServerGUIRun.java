package alan.Viem;

import java.awt.BorderLayout;
import java.awt.Font;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import alan.EventAndInit.ServerAction;

/* 
 * 《服务器--界面》
 * 注：本类仅负责界面布局！
 * */
@SuppressWarnings("serial")
public class ServerGUIRun extends JFrame
{
	JMenuBar menuBar;//菜单栏
	JMenu start;//菜单
	JMenuItem JMI_removedClient,JMI_about,JMI_show;//菜单项
	//文本区域
	private JTextArea JTArecord = new JTextArea();//聊天记录
	private JTextArea JTAlist = new JTextArea();//联系人列表
	private JLabel Tip = new JLabel("在线名单");//标签

	//无参构
	public ServerGUIRun() {
		super.setTitle("Server");
		//super.setLayout(new BorderLayout());//流式布局
		super.setLayout(null);
		super.setSize(700, 500);// 长宽
		super.setResizable(false); // 禁止调整大小
		super.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);// 设置用户点击关闭
		super.setLocationRelativeTo(null);// 窗体在屏幕中心居中
		
		//指定打包后的图标位置，在JRE路径下
		super.setIconImage(new ImageIcon("datarelated/conf/security/policy/unlimited/ServerMain.gif").getImage());// 设置了窗体图标

		//给组件设置属性（组件的位置、大小）
		init();

		//设置界面各个框的属性（颜色、字体、字体大小）
		setAttribute();
		
		this.setVisible(true);// 显示窗体
		
		//此类任务完成，开始下一步
		setActionMethod();//各个组件的事件处理交给ServerAction类处理
	}

	//初始化窗体（添加各个组件及位置）
	private void init() 
	{
		JPanel panel=new JPanel();//创建一个小的面板
		panel.setLayout(new BorderLayout());//将次面板设置为流式布局;
		//菜单栏设置
		menuBar = new JMenuBar();// 菜单栏
		start = new JMenu("开始");// 菜单
		JMI_removedClient = new JMenuItem("移除联系人(R)");// 菜单项
		JMI_show = new JMenuItem("查看信息(L)");
		JMI_about = new JMenuItem("关于(A)");// 菜单项
		start.add(JMI_removedClient); // 添加菜单项
		start.add(JMI_show);
		start.add(JMI_about);
		menuBar.add(start);// 向“菜单栏”添加“菜单”
		panel.add(menuBar,BorderLayout.NORTH);//先添加菜单到JFrame（北方）
		panel.setBounds(0, 0, 684, 24);//设置面板panel在Frame中的位置
		this.add(panel);
		
		// 标签：在线联系人
		Tip.setBounds(505, 30, 100, 20);
		this.add(Tip);
		
		//聊天记录框
		JPanel panel1=new JPanel();//创建�?个普通面�?
		panel1.setBounds(15, 35, 470, 360);//设置普�?�面板位置和大小，不能省�?
		panel1.setLayout(new BorderLayout());//让JTextArea平铺整个JPanel
		JTArecord.setLineWrap(true);//不允许左右滚动条出现
		panel1.add(JTArecord);//将文本域添加进普通面�?
		JScrollPane scrol1 = new JScrollPane();//创建滚动条面�?
		scrol1.setBounds(15, 35, 470, 360);//设置滚动条面板位置和大小
		scrol1.getViewport().add(panel1);//（这是关键！同样不能用add）
		this.add(scrol1);//将滚动条面板加到大窗�?
		
		
		//联系人列表
		int x = 505, y = 55,w = 160,h = 275;
		JPanel panel2=new JPanel();
		panel2.setBounds(x, y, w, h);
		panel2.setLayout(new BorderLayout());
		panel2.add(JTAlist);
		JScrollPane scrol2 = new JScrollPane();
		scrol2.setBounds(x, y, w, h);
		scrol2.getViewport().add(panel2);
		this.add(scrol2);
		
	}
	
	//设置各个框相关属性。
	public void setAttribute() {
		//设置“聊天框”相关属性
		JTArecord.setEditable(false);//只读设置
		//JTArecord.setBackground(new Color(175,172,238));//设置背景颜色
		JTArecord.setFont(new Font("微软雅黑", Font.LAYOUT_LEFT_TO_RIGHT, 11));//设置字体
		
		//联系人列表相关设定
		JTAlist.setFont(new Font("微软雅黑", Font.ITALIC, 12));//设置字体
		//JTAlist.setBackground(new Color(185,132,234));//设置背景颜色
		JTAlist.setEditable(false);//只读模式
		
		// 设置“标签”字体
		Tip.setFont(new Font("微软雅黑", Font.ITALIC, 13));
	}
	
	//把需要处理的事件，交给actionEvent
	private void setActionMethod() {
		//传递“消息记录框”、“联系人列表框”
		ServerAction severAction = new ServerAction(this, JTArecord,JTAlist);
		JMI_removedClient.addActionListener(severAction);
		JMI_show.addActionListener(severAction);
		JMI_about.addActionListener(severAction);
	}
}