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
import javax.swing.JTextField;

import alan.Internet.Impl.SendCommandUDP;
import alan.User.DatabaseRead;
import alan.User.UserList;
/**
 * 此类仅展示自己的个人资料
 * 
 * 此类可以调用密码修改类GUIModifyPassword
 * */
public class Personal_Information extends JFrame
{
	private static final long serialVersionUID = 661609315318691853L;
	private static Personal_Information ThisObject = null;

	private JFrame Myjframe = this;
	private JFrame GuiMainjframe = null;//将保存顶层父句柄，将用于由姓名修改的窗体标题
	private JTextField JTuserName = new JTextField(DatabaseRead.getName(UserList.Account));//昵称
	private String backupName = JTuserName.getText(); //备份昵称
	private JLabel biaoq_name = new JLabel("昵称："); 
	private JButton Butt_modifyPassword = new JButton("修改密码(S)");
	private JButton Butt_modifySave = new JButton("保存修改(C)");
	private JLabel biaoq_account  = new JLabel("账号："+UserList.Account);//标签“账号”
	
	private Personal_Information(JFrame jframe) 
	{
		this.GuiMainjframe = jframe;//这个是主界面的JFrame
		super.setTitle("个人信息");
		super.setLayout(null);//自由布局（组件位置自己计算）
		super.setSize(360, 200);// 长，宽
		super.setResizable(false); // 禁止调整大小
		super.setLocationRelativeTo(GuiMainjframe);// 窗体在GuiMainjframe中心居中（系统方法）
		super.setIconImage(new ImageIcon("datarelated/conf/security/policy/unlimited/ClientIMain.gif").getImage());//设置了窗体图标
		init();
	}
	
	private void init() 
	{
		biaoq_account.setBounds(40, 10, 200, 30);
		JTuserName.setBounds(80, 40, 100, 28);
		biaoq_name.setBounds(40, 40, 200, 30);
		Butt_modifyPassword.setBounds(110, 120, 100, 25);
		Butt_modifySave.setBounds(230, 120, 100, 25);
		
		// 关闭按钮
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				Exit();
			}
		});
		
		//“确定”按钮
		Butt_modifySave.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				//空
				if(JTuserName.getText().isEmpty()) {
					JOptionPane.showMessageDialog(Myjframe,"昵称不能为空！","提示",JOptionPane.WARNING_MESSAGE);return;
				}
				
				//原名未动
				if(JTuserName.getText().equals(backupName)) Exit();
				
				//限定汉字组成
				String pattern = "[\u4e00-\u9fa5]{1,9}"; //限制2~6个汉字组成
				if(!(JTuserName.getText().toString().matches(pattern))){
					JOptionPane.showMessageDialog(Myjframe,"昵称必须是汉字且个数不能超过9个","提示",JOptionPane.WARNING_MESSAGE);return;
				}
				
				//发送指令-让服务端刷新数据库磁盘文件，刷新服务端内存数据
				SendCommandUDP scUdp = new SendCommandUDP();
				scUdp.sendCommand("n"+UserList.Account+":"+JTuserName.getText());//前面的“n”表示修改名字（指令）
				//刷新本地数据内存(DataBase)
				DatabaseRead.setName(UserList.Account, JTuserName.getText());
				backupName = JTuserName.getText();
				GuiMainjframe.setTitle("Client："+JTuserName.getText());
				JOptionPane.showMessageDialog(Myjframe, "已保存！", "提示", JOptionPane.PLAIN_MESSAGE);
				Exit();
			}
		});
		
		Butt_modifyPassword.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Myjframe.setVisible(false);//隐藏当前父窗体
				GUIModifyPassword.SingleColumnMode(GuiMainjframe, Myjframe).setVisible(true);//传入的是顶层父窗体、当前子窗体
			}
		});
		
		this.add(biaoq_name);
		this.add(JTuserName);
		this.add(biaoq_account);
		this.add(Butt_modifySave);
		this.add(Butt_modifyPassword);
	}
	
	public static Personal_Information SingleColumnMode(JFrame jframe) {
		if (ThisObject == null) {
			ThisObject = new Personal_Information(jframe);
			return ThisObject;
		} else {
			return ThisObject;
		}
	}
	private void Exit() {
		ThisObject = null; //标记还原(必须，否则当前窗体在内存中还存在)
		this.dispose();
	}
}