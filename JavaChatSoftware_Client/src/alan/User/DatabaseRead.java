package alan.User;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.JOptionPane;
/**************************************
 * 此类功能：此类将读取好友信息（账号、昵称）
 * 		读取的文件来源于服务器
***************************************/
public class DatabaseRead 
{
	private static ArrayList<User> list  = new ArrayList<User>();//保存读取后的数据
	private static File file = null;//将保存数据文件
	
	//静态初始化
	static {
		file = new File((new File("").getAbsolutePath()+"\\datarelated\\conf\\security\\policy\\us\\userInfo.txt"));
//		file = new File("userInfo.txt");
		try {
			// 从文件中读取用户名和密码
			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"));
			String line = null;
			while ((line = br.readLine()) != null) {
				// 用户名=密码（用到了String类下的split方法正则表达式才分字符串）
				String[] datas = line.split("=");
				list.add(new User(datas[0], datas[1]));
			}
			br.close();
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "读取数据库数据失败，错误路径："+file, "错误", JOptionPane.ERROR_MESSAGE);
			System.exit(-1);
		}
	};
	
	//核对是否存在此账户
	public static boolean checkAccount(String account) 
	{
		Iterator<User> it =list.iterator();
		while (it.hasNext()) 
		{
			User us = (User) it.next();
			if(us.getAccount().equals(account))
				return true;
		}
		return false;
	}
	
	//根据账号返回该账号人的昵称（内存）
	public static String getName(String account) 
	{
		Iterator<User> it =list.iterator();
		while (it.hasNext()) 
		{
			User us = (User) it.next();
			if(us.getAccount().equals(account))
				return us.getName();
		}
		return null;
	}
	
	//更改指定账号人的姓名(刷新客户端本地内存)
	public static void setName(String account, String name) 
	{
		Iterator<User> it =list.iterator();
		while (it.hasNext()) 
		{
			User us = (User) it.next();
			if(us.getAccount().equals(account)){
				us.setName(name);
				break;
			}
		}
	}
	
	//把当前用户的信息给表格
	public static void getClientList(String[][] tableData)
	{
		Iterator<User> it =list.iterator();
		for (int i = 0; i < tableData.length; i++) 
		{
			it.hasNext(); //迭代不断指向下一个数据
			User us = (User) it.next(); 
			for (int k = 0; k < tableData[i].length; k++) 
			{
				if (k == 0) {
					tableData[i][k] = us.getAccount();
				} else {
					tableData[i][k] = us.getName();
				}
			}
		}
	}
	
	//返回联系人个数
	public static int Length() {
		return list.size();
	}
}