package alan.EventAndInit;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import alan.User.ClientBoxMain;
import alan.User.ClientBoxRepair;

/**
 * 移除联系人-操作类【单列模式】
 * */
@SuppressWarnings("serial")
class SingleChatInitRun extends JFrame
{
	//单列模式-标记
	private static SingleChatInitRun ThisObject = null;
	//组合框
	private JComboBox<String> selectBox = new JComboBox<String>();
	//获取本地联系人列表到选框
	private ComboBoxModel<String> List = new DefaultComboBoxModel<String>(ClientBoxMain.getClientList());
	//“发送”按钮
	private JButton RemoveOK = new JButton("确定(O)");
	
	private SingleChatInitRun() {
		super.setTitle("移除联系人");
		super.setLayout(null);//自由布局（组件位置自己计算）
		super.setSize(420, 120);// 长，宽
		super.setResizable(false); // 禁止调整大小
		super.setLocationRelativeTo(null);// 窗体在屏幕中心居中（系统方法）
		init();
	}

	private void init() {
		//给复选框添加联系人
		selectBox.setModel(List);
		
		//“按钮”、“多选框”位置与大小
		RemoveOK.setBounds(290, 27, 90, 25);//左右、上下
		selectBox.setBounds(30, 27, 220, 25);//左右、上下
		
		//“确定”按钮-事件处理
		RemoveOK.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				ButtonOK();//调用本类方法，否则这里不能访问selectBox变量
			}
		});
		
		//关闭事件监听
		// 关闭按钮-事件监听。
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				Exit();
			}
		});
		
		this.add(RemoveOK);
		this.add(selectBox);
	}

	//按下“确定”键-处理
	private void ButtonOK() 
	{
		if(List.getSize() <= 0) { //联系人列表为空
			JOptionPane.showMessageDialog(this,"没有可选择的联系人！","提示",JOptionPane.WARNING_MESSAGE);
			return;
		}
		//获取当前所选择的联系人。
		String selectAccount = this.selectBox.getSelectedItem().toString();
		
		String[] data = selectAccount.split("=>");//“=>”分割“昵称”+“账号”
		
		// 通知到所有客户端的聊天大框。
		ClientBoxMain.messageForwarding("----Tip：Client "+data[0]+" has been removed.[server]");
		//对“Name”发送REMOVE指令，将其移除（必须）
		ClientBoxRepair.RemoveClient(data[1]);//移除时-发送的是账号
		
		JOptionPane.showMessageDialog(this,"以下客户端已移除：\r\n昵称："+data[0] + "\r\n账号："+ data[1] + "\r\n","提示",JOptionPane.INFORMATION_MESSAGE);
		this.dispose();//退出当前窗体。
	}
	
	//单列模式
	public static SingleChatInitRun SingleMode() {
		if(ThisObject == null) {
			ThisObject = new SingleChatInitRun();
			return ThisObject;
		}else {
			return ThisObject;
		}
	}
	
	//关闭按钮
	private void Exit() {
		ThisObject = null;//单列模式标记
		this.dispose();
	}
}