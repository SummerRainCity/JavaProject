package alan.Thread.RepairList;

import java.io.IOException;

import alan.Internet.TCPClientListRepair;
import alan.User.ClientBoxRepair;

/**
 * 客户端联系人列表维护线程 
 * ---------------------------
 */
public class ClientListRepairThread implements Runnable 
{
	@Override
	public void run() 
	{
		while(true)
		{
			// 等待用户连接（仅用于接收“账号”）
			try {
				TCPClientListRepair.socket = TCPClientListRepair.ss.accept();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			// 接收账号--加集合
			ClientListRepairThreadSon clrts = new ClientListRepairThreadSon();
			Thread th = new Thread(clrts, "接收账号");
			th.setDaemon(true);// 守护线程
			th.start();
			try {
				// 此线程执行完后，才执行接下来的
				th.join();
			} catch (InterruptedException e) {}
		}
	}
}

/**
 * 接收账号并且加入集合
 * 
 * 时间:2019年9月28日21:33:48
 * 账号接收
 * */
class ClientListRepairThreadSon implements Runnable
{
	private StringBuffer Account = new StringBuffer();
	
	public ClientListRepairThreadSon() {}
	
	@Override
	public void run() 
	{
		//接收账号
		Account.append(TCPClientListRepair.recData(TCPClientListRepair.socket));
		
		//正则表达式2~6个汉字：[\u4e00-\u9fa5]{2,6}
		boolean isChi = Account.toString().matches("[\\d]{4,11}");//正则判断
		//告诉所有“客户端”增加了联系人
		if (isChi) {
			ClientBoxRepair.addClient(Account.toString(), TCPClientListRepair.socket);
		}
		
		Account.delete(0, Account.length());
	}
}