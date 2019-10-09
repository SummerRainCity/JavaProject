package alan.User;
/*
 * 数据库操作类
 * 
 * 单词：According（根据）
 * */

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.JOptionPane;
public class DataBase 
{
	private static ArrayList<User> list  = new ArrayList<User>();//保存读取后的数据
	private static File file = null;//将保存数据文件
	
	//静态初始化
	static {
		file = new File(new File("").getAbsolutePath()+"\\database.txt");
		
		try {
			// 从文件中读取用户名和密码
			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"));
//			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
			String line = null;
			while ((line = br.readLine()) != null) 
			{
				// 用户名=密码（用到了String类下的split方法正则表达式才分字符串）
				String[] datas = line.split("=");
				list.add(new User(datas[0], datas[1], datas[2]));
			}
			br.close();
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "SERVER读取数据库数据失败，错误路径："+file, "错误", JOptionPane.ERROR_MESSAGE);
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
	
	//根据账号返回该账号人的昵称
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
	
	//修改指定账号的昵称（内存）
	public static void setName(String account,String newname) 
	{
		Iterator<User> it = list.iterator();
		while (it.hasNext()) {
			User us = (User) it.next();
			if (us.getAccount().equals(account)){
				us.setName(newname);
				break;
			}
		}
	}
	
	//修改指定账号的密码（内存）
	public static void setPassword(String account,String newPassword) 
	{
		Iterator<User> it = list.iterator();
		while (it.hasNext()) {
			User us = (User) it.next();
			if (us.getAccount().equals(account)){
				us.setPassword(newPassword);
				break;
			}
		}
	}
	
	//核对指定账户的密码是否正确
	public static boolean checkPassword(String account, String password) 
	{
		Iterator<User> it =list.iterator();
		while (it.hasNext()) 
		{
			User us = (User) it.next();
//			System.out.println("字符串"+account+"与字符串"+us.getAccount()+"比较结果："+us.getAccount().equalsIgnoreCase(account));
//			System.out.println("account长度："+account.length()+"\r\n"+"us.getAccount()长度："+us.getAccount().length());
//			char[] chs = us.getAccount().toCharArray();
//			for (int i = 0; i < chs.length; i++) {
//				System.out.println(chs[i]);
//			}
			System.out.println();
			if(us.getAccount().equalsIgnoreCase(account) && us.getPassword().equals(password)){
				return true;
			}
		}
		return false;
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
	
	//（磁盘数据）
	public synchronized static void refreshAllData() 
	{
		try {
			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), "UTF-8"));
			Iterator<User> it = list.iterator();
			while (it.hasNext()) 
			{
				User us = (User) it.next();
				us.setAccount(new String(us.getAccount().toCharArray()));
				bw.write(us.getAccount() + "=" + us.getPassword() + "=" + us.getName());
				bw.newLine();// 换行
				bw.flush();
			}
			bw.close();
		} catch (IOException e) {
		}
	}
	
	//返回联系人个数
	public static int Length() {
		return list.size();
	}
	
	//遍历所有数据(Test)
	public static void Traverse_list() 
	{
		Iterator<User> it =list.iterator(); // Iterator是接口，实际上返回的是子类对象
		while (it.hasNext()) 
		{
			User us = (User) it.next();
			System.out.println(
					"账号：" + us.getAccount() + "\r\n密码：" + us.getPassword() + "\r\n姓名：" + us.getName() + "\r\n");
		}
	}
}