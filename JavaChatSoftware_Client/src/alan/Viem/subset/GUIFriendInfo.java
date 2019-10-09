package alan.Viem.subset;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import alan.User.DatabaseRead;
/**
 * 此类读取服务端传来的好友信息文件并以列表的形式显示。
 * 关于此类依赖类：此类仅提供表格，而数据来源于DatabaseRead类，DatabaseRead会去读取服务端传来的用户数据文件
 * 文件内容仅：
 * 		1，账号
 * 		2，昵称
 ***/
public class GUIFriendInfo  extends JFrame
{
	private static final long serialVersionUID = 5285075464650449039L;
		//单列模式-标记
		private static GUIFriendInfo ThisObject = null;
		
		private GUIFriendInfo(JFrame jframe) 
		{
			init(jframe);
		}

		private void init(JFrame jframe)
		{
			this.setTitle("好友信息");
			this.setIconImage(new ImageIcon("datarelated/conf/security/policy/unlimited/ClientIMain.gif").getImage());//设置了窗体图标
			int len = DatabaseRead.Length();
			String[][] tableData = new String[len][2];
			
			String[] columnTitle = {"账号" , "昵称"};
			
			DatabaseRead.getClientList(tableData);
			
			JTable table = new JTable(tableData , columnTitle);
			this.add(new JScrollPane(table));
			this.setResizable(true);
			this.pack();
			this.setLocationRelativeTo(jframe);// 窗体在屏幕中心居中（系统方法）
			this.addWindowListener(new WindowAdapter() {
				@Override
				public void windowClosing(WindowEvent e) {
					Exit();
				}
			});
		}
		
		//单列模式
		public static GUIFriendInfo SingleMode(JFrame jframe)
		{
			if (ThisObject == null) {
				ThisObject = new GUIFriendInfo(jframe);
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
