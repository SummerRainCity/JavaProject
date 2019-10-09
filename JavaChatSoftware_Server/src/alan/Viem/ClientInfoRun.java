package alan.Viem;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import alan.User.DataBase;

public class ClientInfoRun extends JFrame
{
	private static final long serialVersionUID = -4707063603933639113L;

	//单列模式-标记
	private static ClientInfoRun ThisObject = null;
	
	private ClientInfoRun() {
		init();
	}

	private void init()
	{
		this.setTitle("All client info（database）");
		
		int len = DataBase.Length();
		
		// 定义二维数组作为表格数据
		String[][] tableData = new String[len][2];//第一个下标是行(伴随客户端个数变化)，第二个是列数。7个联系人就是7行
		
		// 定义一维数据作为列标题
		String[] columnTitle = {"账号" , "昵称"};
		
		//调用数据库函数，返回当前联系人列表数据
		DataBase.getClientList(tableData);//注：这是返回的本地数据库信息
		
		// 以二维数组和一维数组来创建一个JTable对象
		JTable table = new JTable(tableData , columnTitle);
		// 将JTable对象放在JScrollPane中，
		// 并将该JScrollPane放在窗口中显示出来
		this.add(new JScrollPane(table));
		this.setResizable(false);
		this.pack();
		this.setLocationRelativeTo(null);// 窗体在屏幕中心居中（系统方法）
		// 关闭按钮-事件监听。
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				Exit();
			}
		});
	}
	
	//单列模式
	public static ClientInfoRun SingleMode() {
		if (ThisObject == null) {
			ThisObject = new ClientInfoRun();
			return ThisObject;
		} else {
			return ThisObject;
		}
	}
	
	//关闭按钮
	private void Exit() {
		ThisObject = null;// 单列模式标记
		this.dispose();
	}
}
