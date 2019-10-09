package alan.User;

import java.util.Iterator;
import java.util.LinkedList;

import alan.Event.StartAction;

/**************************************************
 * 注：仅供维护联系人列表类使用（这意味着本类是仅存储“账号”用的）
 * 用户列表盒子——List集合（底层数据结构是链表，增删快，查询慢）
 * 作用：此线程控制列表联系人状态。
 * 
 * 注：此类本质是account，姓名是由DatabaseRead提供
 ***************************************************/
public class UserList 
{
	//保存自己的账户+密码（StartAction类初始化）
	public static String Account = null; //永久保存本身账号
	public static String Password = null; //登录成功后此变量为null

	//LinkedList集合将保存“账号”
	private static LinkedList<String> list;
	
	static {
		list = new LinkedList<String>();
	};
	
	//添加联系人到列表（添加到“联系人维护列表”）
	public static void join(String account) 
	{
		//如果，此列表中没有此元素则添加。
		if(!(list.contains(account))) {
			list.add(account);
			StartAction.JTAlist.append("【"+DatabaseRead.getName(account)+"】\r\n");
		}
	}
	
	//移除联系人
	public static void delete(String account) 
	{
		//移除
		list.remove(account);
		//刷新联系人列表
		refreshList();
	}
	
	// 刷新右边的联系人列表
	public static void refreshList()
	{
		//清空列表
		StartAction.JTAlist.setText("");
		//遍历
		Iterator<String> it = list.iterator();
		while(it.hasNext()) {
			String account = (String)it.next();
			StartAction.JTAlist.append("【"+DatabaseRead.getName(account)+"】\r\n");
		}
	}
	
	//返回当前联系人的字符串数组（复选框用）
	public static String[] getList() 
	{
		int length = list.size();//数组长度定义需要
		String[] array = new String[length];
		Iterator<String> it = list.iterator();
		for(int x = 0; it.hasNext() ;x++) {
			array[x] = (String)it.next();//(String)it.next()返回的是账号，所以
//			array[x] = DatabaseRead.getName((String)it.next());//根据账号返回昵称
		}
		return array;
	}
	
	//返回有几个联系人
	public static int count() {
		return list.size();
	}
}