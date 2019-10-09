package alan.User;

import java.net.Socket;
import java.util.LinkedHashMap;
import java.util.Set;

import alan.Internet.TCPClientListRepair;

/**
 * 联系人维护列表专用
 * 供“TCP-Port33333使用”
 * 1，本类主要负责联系人列表在线维护
 * 2，本类含有可以让远程客户端退出程序的功能。
 * 
 * 此类主要针对客户端进行控制，相关类请查看ClientBoxMain
 * */
public class ClientBoxRepair 
{
	// 保存“客户端”用的
	private static LinkedHashMap<String, Socket> Boxwh;

	// 私有构造
	private ClientBoxRepair() {}

	static {
		Boxwh = new LinkedHashMap<String, Socket>();
	}
	
	//根据“账号”返回一个socket
	public static Socket returnSocket(String account) 
	{
		Set<String> value = Boxwh.keySet();// 获取所有有“值”的“键”
		for (String key : value) // 每次Name、Socket都是不同的键值对存在
		{
			if (key.equals(account)) {
				return Boxwh.get(key);// “键”返回“值”
			}
		}
		return null;
	}
	
	// 添加一个客户端（有线程自动完成，不管）
	public static void addClient(String account, Socket socket) 
	{
		Boxwh.put(account, socket);
		
		//通知除当前客户端以外的所有客户端有XXX上线（Tell：告诉）
		tellAllClient(account);
		
		//刷新当前客户端的列表（仅针对当前客户端传输）
		refreshCurrentClientList(account,socket);
	}
	
	//【注意】：如果发现客户端接收用户列表出现完整现象，说明是服务端发送数据太快
	//通知除当前客户端的所有客户端，XXX上线。（Socket变化，Name不变）
	private static void tellAllClient(String tellAccount) {
		/**
		 * 作用：更新所有客户端，XXX上线（Socket变，Name固定）
		 * */
		Set<String> temp = Boxwh.keySet();// 获取所有有“值”的“键”
		for (String bName : temp) // 每次Name、Socket都是不同的键值对存在
		{
			Socket socket = Boxwh.get(bName);// “键”返回“值”
			if (!(tellAccount.equals(bName))) {
				TCPClientListRepair.senData("+" + tellAccount, socket);
				//System.out.println("通知所有客户端，姓名："+tellName);
				Sleep(500);//防止发送过快，客户端接收失败
			}
		}
	}

	//更新当前上线的用户列表（Socket固定不变，Name不断变化）
	private static void refreshCurrentClientList(String currentAccount,Socket currentSocket)
	{
		/**
		 * 更新当前用户的列表。（仅针对当前用户之间的数据传输）
		 * 流程：
		 * 		1，Socket就应该是当前用户的Socket对象
		 * 		2，然后把当前列表的数据全部发给当前用户对象。
		 * */
		Set<String> temp = Boxwh.keySet();// 获取所有有“值”的“键”
		for (String listName : temp) //每次“名字”在变化，但是Socket不会变化。
		{
			if(!(currentAccount.equals(listName))) {
				TCPClientListRepair.senData("+" + listName, currentSocket);
				Sleep(500);//防止发送过快，客户端接收失败
			}
		}
	}
	
	//向所有客户端-更新某个客户端的昵称（时间：2019年9月28日23:00:07）
	public static void refreshCurrentClientList(String account, String newName)
	{
		Set<String> set1 = Boxwh.keySet();// 获取所有有“值”的“键”
		for (String bName : set1)
		{
			Socket socket = Boxwh.get(bName);// “键”返回“值”
			if (!(account.equals(bName))) { // 不能删除本身
				TCPClientListRepair.senData("m" + account+"="+newName, socket);
			}
		}
	}

	//告诉所有联系人，需要删除谁
	//（由主要线程ClientMesRecThreadSon控制，因为该线程再测试谁在线非常精确，此方法就不予TCP33333合作）
	// 为了精确的判断某联系人是否离线，与主线程中的TCP22222合作，并由主线程调用此方法
	public static void delClient(String delName) {
		// 更新自身（本类）集合
		deleteClient(delName);
		// 更新客户端
		Set<String> set1 = Boxwh.keySet();// 获取所有有“值”的“键”
		for (String bName : set1) // 每次Name、Socket都是不同的键值对存在
		{
			Socket socket = Boxwh.get(bName);// “键”返回“值”
			if (!(delName.equals(bName))) { // 不能删除本身
				TCPClientListRepair.senData("-" + delName, socket);
			}
		}
	}

	// 移除一个客户端（本地集合表操作）
	private static void deleteClient(String userName) {
		Boxwh.remove(userName);
	}
	
	// 延时（用于更新用户列表用，根据测试，数据发送过快，客户端没法快速接收。）
	private static void Sleep(int ms) {
		try {
			Thread.sleep(700);
		} catch (InterruptedException e) {
			//e.printStackTrace();
		}
	}
	
	//发送指令“REMOVE”到指定客户端，该客户端会退出聊天！
	public static void RemoveClient(String userName) {
		// 根据“Name”返回“Socket”
		Socket socket = Boxwh.get(userName);
		// 发送退出程序命令。
		TCPClientListRepair.senData("REMOVE", socket);
	}
	
	//发送指令“Single”到指定客户端，该客户端会激活单聊GUI
	public static boolean rouseClientSingle(String userName) {
		Socket socket = Boxwh.get(userName); //如果对方还下线，返回的是null
		if (socket != null) {
			TCPClientListRepair.senData("Single", socket);// 发送指令
			return false;
		}
		return true;
	}
}