package alan.User;

import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import alan.EventAndInit.ServerAction;
import alan.Internet.TCPSenRecMain;

/**
 * Map<key, value> <键--值>
 * 此类作用
 * 1，保存所有用户<UserName, Socket>
 * 2，联系人在线与离线的列表维护
 * 3，增加了接收姓名仅限制汉字(2~6)之间。这是为了防止服务端中联系人列表出现怪异的姓名现象。（主Box）
 * */
public class ClientBoxMain 
{
	// 保存“客户端”用的
	private static HashMap<String, Socket> Box;

	//私有构造
	private ClientBoxMain() {}
	
	static {
		Box = new HashMap<String, Socket>();
	}

	// 添加一个客户端（有线程自动完成，不管）
	public static boolean addClient(String account, Socket socket) {
		if(Box.containsKey(account)) { //如果昵称重复|| 则放弃添加
			return false;
		}
		Box.put(account, socket);
		return true;
	}

	// 移除一个客户端（某个客户端断开链接）
	public static void deleteClient(String userName) {
		Box.remove(userName);
	}

	// 获取指定用户名的Socket对象（单独聊天需要）
	public static Socket getClient(String userName) {
		Socket s = Box.get(userName); // 根据用户名返回对应的Socket对象
		if (s == null) 
			return null;
		else
			return s;
	}

	// 返回有多少个客户（返回在线人数）
	public static int sizeClient() {
		return Box.size();
	}

	// 根据Socket对象返回用户名（知道是谁发的）
	public static String getClinetName(Socket socketValue) {
		// 返回的是键值对对象的集合
		Set<Map.Entry<String, Socket>> set = Box.entrySet();
		// 开始查找
		for (Map.Entry<String, Socket> kvm : set) {
			// 如果找到指定的“Socket”，则返回姓名
			if (kvm.getValue() == socketValue) {
				return kvm.getKey();
			}
		}
		return null;
	}
	
	//消息转发（全部联系人【重要】）
	public static void messageForwarding(String Message) {
		//遍历
		Set<String> set = Box.keySet();//获取所有有“值”的“键”
		for(String key: set) {
			Socket sValue = Box.get(key);//根据“键”返回“Socket”
			TCPSenRecMain.sendDataChar(Message, sValue);
		}
	}
	
	//服务端联系人列表维护（完全刷新，尽量少用）
	public static void refreshClientList() {
		//清空当前联系人列表
		ServerAction.JTAlist.setText("");
		// 获取所有有“值”的“键”
		Set<String> set = Box.keySet();
		for (String account : set) {
			ServerAction.JTAlist.append("【" + DataBase.getName(account) + "】\r\n"); // 更新联系人列表(右)
		}
	}
	
	//返回联系人列表的所有用户姓名信息。
	public static String[] getClientList() 
	{
		String temp;
		int length = Box.size();
		String[] array = new String[length];
		Set<String> set = Box.keySet();
		int i = 0;//下标
		for (String keyAccount : set) {
			//array[i] = keyAccount; //这其实返回的是“账号”
			temp = DataBase.getName(keyAccount);//数据库操作类根据账号返回昵称
			array[i] = temp + "=>" + keyAccount;//“昵称”+“账号”拼接
			i++;
		}
		return array;
	}
}