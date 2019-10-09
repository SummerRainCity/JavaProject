package alan.Viem.subset;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import alan.Internet.Impl.SendCommandUDP;
import alan.User.UserList;
/**
 * 此类是Personal_Information的子类
 * 此类功能：修改密码
 * 
 * 依赖：ClientListRepairThread（这个线程接收服务端指令，知道密码是否更改成功）
 * 依赖：此类仅由Personal_Information(个人信息类)调用
 * */
public class GUIModifyPassword extends JFrame
{
	//标签：
	JLabel biaoq_origin = new JLabel("原密码：");
	JLabel biaoq_now = new JLabel("新的密码：");
	JLabel biaoq_check = new JLabel("确认密码：");
	
	//原密码
	JTextField JF_origin = new JTextField(15);
	//现密码（密码框）
	JPasswordField JF_now = new JPasswordField();
	//确认现密码（密码框）
	JPasswordField JF_check = new JPasswordField();
	
	//确认修改（按钮）
	JButton Butt_OK = new JButton("确认修改");
	//取消修改（按钮）
	JButton Butt_Cancel = new JButton("取消");
	
	private GUIModifyPassword(JFrame SuperF_jframe, JFrame F_jframe)//第一个参数“超级父句柄”仅用于居中
	{
		this.Father_Jframe = F_jframe;//用于显示当前句柄的父句柄（既：显示“个人资料页”）
		super.setTitle("密码修改");
		super.setLayout(null);//自由布局（组件位置自己计算）
		super.setSize(400, 300);// 长，宽
		super.setResizable(false); // 禁止调整大小
		super.setLocationRelativeTo(SuperF_jframe);// 窗体在当前句柄的父句柄中心居中
		super.setIconImage(new ImageIcon("datarelated/conf/security/policy/unlimited/ClientIMain.gif").getImage());//设置了窗体图标
		init();
	}
	
	private void init() 
	{
		//标签
		biaoq_origin.setBounds(82, 40, 60, 20);//左右、上下
		biaoq_now.setBounds(70, 90, 70, 20);
		biaoq_check.setBounds(70, 140, 70, 20);
		this.add(biaoq_origin);
		this.add(biaoq_now);
		this.add(biaoq_check);
		
		//文本框
		JF_origin.setBounds(130, 40, 200, 23);
		JF_now.setBounds(130, 90, 200, 23);
		JF_check.setBounds(130,140, 200, 23);
		this.add(JF_origin);
		this.add(JF_now);
		this.add(JF_check);
		
		//按钮
		Butt_OK.setBounds(70, 200, 100, 25);
		Butt_Cancel.setBounds(230, 200, 100, 25);
		this.add(Butt_OK);
		this.add(Butt_Cancel);
		
		//按钮事件
		Butt_OK.addActionListener(new ActionListener() {
			@SuppressWarnings("deprecation")
			@Override
			public void actionPerformed(ActionEvent e) {
				//先判断空
				if((JF_origin.getText().isEmpty()) || (JF_now.getText().isEmpty()) || (JF_check.getText().isEmpty()))
				{
					JOptionPane.showMessageDialog(Myjframe,"密码项均不能为空","提示",JOptionPane.WARNING_MESSAGE);//警告提示框
					return;
				}
				//密码个数限制
				if(JF_now.getText().length() < 4 || JF_check.getText().length() > 15) 
				{
					JOptionPane.showMessageDialog(Myjframe,"密码个数(4~15)！","提示",JOptionPane.WARNING_MESSAGE);
					return;
				}
				//判断新密码连词是否一致
				if(!(JF_now.getText().equals(JF_check.getText()))) {
					JOptionPane.showMessageDialog(Myjframe,"两次密码输入不一致！","提示",JOptionPane.WARNING_MESSAGE);//警告提示框
					return;
				}
				//发送数据
				SendCommandUDP scUdp = new SendCommandUDP();
				scUdp.sendCommand("p"+UserList.Account+":"+JF_origin.getText()+":"+JF_check.getText());//格式：[指令]账号+原密码+现密码
				
				//接收反馈
				//JOptionPane.showMessageDialog(Myjframe, "密码更改成功！", "提示", JOptionPane.PLAIN_MESSAGE);
				//这个反馈由“维护线程来完成”
			}
		});
		Butt_Cancel.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Exit();
			}
		});
		
		// 关闭按钮
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				Exit();
			}
		});
	}

	/*************************************************************/
	private JFrame Father_Jframe = null;//保存当前句柄的父句柄
	private JFrame Myjframe = this;//仅用于提示框
	private static final long serialVersionUID = 8225604573181643002L;
	public static GUIModifyPassword ThisObject = null; //公开此，是为了维护列表线程能根据密码是否更改成功，关闭当前窗体
	public static GUIModifyPassword SingleColumnMode(JFrame SuperF_jframe, JFrame F_jframe) {
		if (ThisObject == null) {
			ThisObject = new GUIModifyPassword(SuperF_jframe, F_jframe);
			return ThisObject;
		} else {
			return ThisObject;
		}
	}
	private void Exit() {
		ThisObject = null; //标记还原(必须，否则当前窗体在内存中还存在)
		this.dispose();
		this.Father_Jframe.setVisible(true);//显示当前句柄父窗体
	}
}