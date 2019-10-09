package alan.Viem.subset;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JSlider;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import alan.Event.StartAction;

/**
 * 注：此类是ClientAction类的控制子类，依赖ClientAction。也仅由ClientAction类控制启动
 * 作用：此类可以控制及改变各个框的颜色
 * 
 * 单列模式：此窗体应该只允许打开一次，所以应应用单列模式。
 **/
@SuppressWarnings("serial")
public class GUISettingRun extends JFrame 
{
	private int Backup_R;//备份当前红，用于“关闭”执行
	private int Backup_G;//备份当前绿，用于“关闭”执行
	private int Backup_B;//备份当前蓝，用于“关闭”执行
	private int Backup_R_list;//备份
	private int Backup_G_list;//备份
	private int Backup_B_list;//备份
	
	//单列模式设计
	private static GUISettingRun ThisObject = null;
	
	// 分割线
	private JSeparator JSline = new JSeparator(SwingConstants.HORIZONTAL);

	// 滑动三个值会不断改变
	int R = 175;
	int G = 172;
	int B = 238;

	private JPanel panel = new JPanel();
	private JLabel TipTitle = new JLabel("聊天室背景");
	private JLabel TipRed = new JLabel("R");
	private JLabel TipGreen = new JLabel("G");
	private JLabel TipBlue = new JLabel("B");
	private JLabel valueRedTip = new JLabel(R + "");// 数值提示：红
	private JLabel valueGreenTip = new JLabel(G + "");// 数值提示：绿
	private JLabel valueBlueTip = new JLabel(B + "");// 数值提示：蓝

	// 创建一个滑块，最小值、最大值、初始值
	private final JSlider HuaKuaiRed = new JSlider(0, 255, 0);// 一个滑块控制一种颜色-R
	private final JSlider HuaKuaiGreen = new JSlider(0, 255, 0);// 一个滑块控制一种颜色-G
	private final JSlider HuaKuaiBlue = new JSlider(0, 255, 0);// 一个滑块控制一种颜色-B

	/********************** 下面是联系人列表相关 *************************/
	int listR = 185;
	int listG = 132;
	int listB = 234;

	private JLabel listTipTitle = new JLabel("联系人列表背景");
	private JLabel listTipRed = new JLabel("R");
	private JLabel listTipGreen = new JLabel("G");
	private JLabel listTipBlue = new JLabel("B");
	private JLabel listValueRedTip = new JLabel(listR + "");// 数值提示：红
	private JLabel listValueGreenTip = new JLabel(listG + "");// 数值提示：绿
	private JLabel listValueBlueTip = new JLabel(listB + "");// 数值提示：蓝
	// 创建一个滑块，最小值、最大值、初始值。
	private final JSlider listHuaKuaiRed = new JSlider(0, 255, 0);
	private final JSlider listHuaKuaiGreen = new JSlider(0, 255, 0);
	private final JSlider listHuaKuaiBlue = new JSlider(0, 255, 0);

	/*********************** 按钮 *************************/
	private JButton ButtonOK = new JButton("保存更改");
	private JButton ButtonRec = new JButton("恢复默认");

	private GUISettingRun(JFrame jframe)
	{
		super.setTitle("界面设置");
		super.setSize(380, 280);
		super.setLayout(null);// 自由布局（组件位置自己计算）
		super.setResizable(false); // 禁止调整大小
		panel.setLayout(null);// panel也要自由布局
		super.setLocationRelativeTo(jframe);// 窗体居中
		super.setIconImage(new ImageIcon("datarelated/conf/security/policy/unlimited/ClientIMain.gif").getImage());//设置了窗体图标

		// 关闭按钮-事件监听。
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				Exit();
			}
		});
		
		//备份JTArecord原色
		Backup_R = StartAction.JTArecord.getBackground().getRed();
		Backup_G = StartAction.JTArecord.getBackground().getGreen();
		Backup_B = StartAction.JTArecord.getBackground().getBlue();
		//备份JTAlist原色
		Backup_R_list = StartAction.JTAlist.getBackground().getRed();
		Backup_G_list = StartAction.JTAlist.getBackground().getGreen();
		Backup_B_list = StartAction.JTAlist.getBackground().getBlue();
		
		init();// 相关组件属性设置

		setActionMethod();// 事件处理

		this.setContentPane(panel);
		//this.setVisible(true);//单列模式下，显示应该由主调控制，这样用户在每次点击时会提示此窗体。
	}

	private void init()
	{
		// 标题
		TipTitle.setBounds(160, 0, 100, 20);
		// 标签
		TipRed.setBounds(20, 20, 40, 15);// 左右、上下
		TipGreen.setBounds(20, 40, 40, 15);// 左右、上下
		TipBlue.setBounds(20, 60, 40, 15);// 左右、上下
		// 滑块
		HuaKuaiRed.setBounds(50, 20, 255, 20);// 左右、上下
		HuaKuaiGreen.setBounds(50, 40, 255, 20);// 左右、上下
		HuaKuaiBlue.setBounds(50, 60, 255, 20);// 左右、上下
		// 滑动数值变化标签。
		valueRedTip.setBounds(320, 17, 100, 20);// 滑动数值变化。
		valueGreenTip.setBounds(320, 37, 100, 20);// 左右、上下
		valueBlueTip.setBounds(320, 57, 100, 20);
		// 分割线
		JSline.setBounds(20, 90, 340, 2);

		// 添加滑块到内容面板
		panel.add(TipTitle);
		panel.add(valueRedTip);
		panel.add(valueGreenTip);
		panel.add(valueBlueTip);// 添加标签变化值
		panel.add(HuaKuaiRed);
		panel.add(HuaKuaiGreen);
		panel.add(HuaKuaiBlue);// 添加滑动
		panel.add(TipRed);
		panel.add(TipGreen);
		panel.add(TipBlue);
		panel.add(JSline);

		/********************************* list相关 ******************************/
		listTipTitle.setBounds(145, 100, 100, 20);// 标题
		listTipRed.setBounds(20, 120, 40, 15);// 左右、上下
		listTipGreen.setBounds(20, 140, 40, 15);// 左右、上下
		listTipBlue.setBounds(20, 160, 40, 15);// 左右、上下
		// 滑块
		listHuaKuaiRed.setBounds(50, 120, 255, 20);// 左右、上下
		listHuaKuaiGreen.setBounds(50, 140, 255, 20);// 左右、上下
		listHuaKuaiBlue.setBounds(50, 160, 255, 20);// 左右、上下
		// 滑动条
		listValueRedTip.setBounds(320, 117, 100, 20);// 滑动数值变化。
		listValueGreenTip.setBounds(320, 137, 100, 20);// 左右、上下
		listValueBlueTip.setBounds(320, 157, 100, 20);

		panel.add(listTipTitle);// 添加“标题”
		panel.add(listTipRed);
		panel.add(listTipGreen);
		panel.add(listTipBlue);// 添加固定“标签”
		panel.add(listHuaKuaiRed);
		panel.add(listHuaKuaiGreen);
		panel.add(listHuaKuaiBlue);// 添加滑块
		panel.add(listValueRedTip);
		panel.add(listValueGreenTip);
		panel.add(listValueBlueTip);// 添加“变化标签”

		/*********************************************************/
		ButtonRec.setBounds(255, 200, 90, 25);//恢复默认设置按钮
		ButtonRec.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				StartAction.JTArecord.setBackground(new Color(175, 172, 238));// 默认颜色
				HuaKuaiRed.setValue(175);HuaKuaiGreen.setValue(172);HuaKuaiBlue.setValue(238);
				
				StartAction.JTAlist.setBackground(new Color(185, 132, 234));//默认颜色
				listHuaKuaiRed.setValue(185);listHuaKuaiGreen.setValue(132);listHuaKuaiBlue.setValue(234);
			}
		});
		panel.add(ButtonRec);
		
		ButtonOK.setBounds(150, 200, 90, 25);//“保存更改”按钮
		ButtonOK.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				ButtonOK_Exit();//此方法直接退出当亲窗体，并不更改当前设置的背景颜色
			}
		});
		panel.add(ButtonOK);
	}

	// 事件处理
	private void setActionMethod() 
	{
		//根据大框的背景颜色-设置当前进度条的数值
		HuaKuaiRed.setValue(StartAction.JTArecord.getBackground().getRed());// 设置当前大框背景颜色“R”值
		HuaKuaiGreen.setValue(StartAction.JTArecord.getBackground().getGreen());// 设置当前大框背景颜色“G”值
		HuaKuaiBlue.setValue(StartAction.JTArecord.getBackground().getBlue());// 设置当前大框背景颜色“B”值
		
		//初始化一次“变化标签”（让此程序一运行，使标签就能得到正确的当前数据）
		R = HuaKuaiRed.getValue();valueRedTip.setText("" + R);// 标签变化值
		G = HuaKuaiGreen.getValue();valueGreenTip.setText("" + G);// 标签变化值
		B = HuaKuaiBlue.getValue();valueBlueTip.setText("" + B);// 标签变化值
		
		// 添加刻度改变监听器
		HuaKuaiRed.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				R = HuaKuaiRed.getValue();
				valueRedTip.setText("" + R);// 标签变化值
				StartAction.JTArecord.setBackground(new Color(R, G, B));// 设置背景颜色
			}
		});
		HuaKuaiGreen.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				G = HuaKuaiGreen.getValue();
				valueGreenTip.setText("" + G);// 标签变化值
				StartAction.JTArecord.setBackground(new Color(R, G, B));// 设置背景颜色
			}
		});
		HuaKuaiBlue.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				B = HuaKuaiBlue.getValue();
				valueBlueTip.setText("" + B);// 标签变化值
				StartAction.JTArecord.setBackground(new Color(R, G, B));// 设置背景颜色
			}
		});
		/****************************** List *******************************/
		listHuaKuaiRed.setValue(StartAction.JTAlist.getBackground().getRed());// 设置当前大框背景颜色“R”值
		listHuaKuaiGreen.setValue(StartAction.JTAlist.getBackground().getGreen());// 设置当前大框背景颜色“G”值
		listHuaKuaiBlue.setValue(StartAction.JTAlist.getBackground().getBlue());// 设置当前大框背景颜色“B”值
		
		//初始化一次“变化标签”（让此程序一运行，使标签就能得到正确的当前数据）
		listR = listHuaKuaiRed.getValue();listValueRedTip.setText("" + listR);// 标签变化值
		listG = listHuaKuaiGreen.getValue();listValueGreenTip.setText("" + listG);// 标签变化值
		listB = listHuaKuaiBlue.getValue();listValueBlueTip.setText("" + listB);// 标签变化值

		// 向“列表”添加刻度改变监听器
		listHuaKuaiRed.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				listR = listHuaKuaiRed.getValue();
				listValueRedTip.setText("" + listR);// 标签变化值
				StartAction.JTAlist.setBackground(new Color(listR, listG, listB));// 设置背景颜色
			}
		});
		listHuaKuaiGreen.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				listG = listHuaKuaiGreen.getValue();
				listValueGreenTip.setText("" + listG);// 标签变化值
				StartAction.JTAlist.setBackground(new Color(listR, listG, listB));// 设置背景颜色
			}
		});
		listHuaKuaiBlue.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				listB = listHuaKuaiBlue.getValue();
				listValueBlueTip.setText("" + listB);// 标签变化值
				StartAction.JTAlist.setBackground(new Color(listR, listG, listB));// 设置背景颜色
			}
		});
	}

	//按钮“确定”执行方法
	private void ButtonOK_Exit() {
		ThisObject = null;//单列模式“标志”还原
		this.dispose();
	}
	
	//用户点击“关闭”按钮，视为不保存更改。需要点“确定”按钮。所以关闭时恢复默认值
	private void Exit() {
		//恢复之前的颜色
		StartAction.JTArecord.setBackground(new Color(Backup_R, Backup_G, Backup_B));// 恢复之前的颜色
		StartAction.JTAlist.setBackground(new Color(Backup_R_list, Backup_G_list, Backup_B_list));//默认颜色
		
		//单列模式“标志”还原
		ThisObject = null;
		this.dispose();
	}
	
	//单列模式（作用：此类只能创建一次对象）
	public static GUISettingRun SingleColumnMode(JFrame jframe) {
		if(ThisObject == null) {
			ThisObject = new GUISettingRun(jframe);
			return ThisObject;
		}else {
			return ThisObject;
		}
	}
}
