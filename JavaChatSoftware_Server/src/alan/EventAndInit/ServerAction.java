package alan.EventAndInit;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;

import alan.Thread.Main.ClientLinkThread;
import alan.Thread.RepairList.ClientListRepairThread;
import alan.Thread.SingleChat.SingleChatTh;
import alan.Thread.UDPcommand.RecCommand;
import alan.Util.Util;
import alan.Viem.ClientInfoRun;

/**
 * 《服务器-界面事件处理-主要》
 * 注：此类处理事件、和初始化文本框
 * 
 * 1，此类中的JTArecord、JTAlist分别是“聊天记录”框，“联系人列表”
 * 它们将在以后不断更新。
 * 		1.1，一个用户上线，将更新“联系人姓名”+“时间”
 * */
/*
 * 单词：Message【消息、信息】
 * */
public class ServerAction implements ActionListener
{
	JFrame jframe = null; //父句柄
	public static JTextArea JTArecord;//服务端-用户上线详细信息
	public static JTextArea JTAlist;//联系人列表（全局控制）

	//此类做各个事件处理
	public ServerAction(JFrame jframe, JTextArea jTArecord, JTextArea jTAlist) 
	{
		ServerAction.JTArecord = jTArecord;
		ServerAction.JTAlist = jTAlist;
		
		init();//初始化-文本框信息-作者相关
		
		//调用此方法启动相关的线程
		StartRelatedThread();
	}

	private void StartRelatedThread() 
	{
		/*******************开启客户端联系人列表维护线程************************
		 此线程用于：让服务端、客户端的客户列表保持一致，如果有人下线，那么服务端与
		 客户端的联系人列表数据是将同步。
		【传输协议：TCP->port=22222】
		************************************************************************/
		ClientListRepairThread clrt = new ClientListRepairThread();
		Thread clrtth = new Thread(clrt,"Contact list maintenance thread");
		clrtth.setDaemon(true);// 设置“保护线程”
		clrtth.setPriority(6);
		clrtth.start();// 开启线程
		
		/**********************开启ClientLinkThread线程[Main主线程]*****************
		 此ClientLinkThread线程为客户端核心处理线程
		 大致流程：
		 	1，当前线程循环接收Socket(当前父线程-accept阻塞)
		 		1.1，接收此Socket昵称，接收完毕当前子线程死亡(子线程1)
		 		1.2，基于当前父线程Socket，循环接收该Socket发来的公共消息(子线程2)
		 [用户名接收线程(非循环)]-线程名：ClientLinkThreadSon
		 [不断接收用户消息线程(也用于判断用户在线情况)]-线程名-CliMesRecThread
		 【传输协议：TCP->port=33333】
		***********************************************************************/
		ClientLinkThread rct = new ClientLinkThread();
		Thread rctth = new Thread(rct,"Receive msg Thread");
		rctth.setDaemon(true);// 设置“保护线程”
		rctth.setPriority(8);
		rctth.start();// 开启线程
		
		/****************************开启“单聊”处理线程***************************
		大致流程：
			(1)等待Client01链接->(2)链接成功->(3)等待Client02链接->(4)有第三者插足回到3->
		(5)Client02链接成功->(6)将开启一个新线程用于处理Client01余Client02之间的通信。
		【传输协议：TCP->port=32600】
		***********************************************************************/
		SingleChatTh csct = new SingleChatTh();
		Thread csctth = new Thread(csct,"Single Chat Thread");
		csctth.setDaemon(true);// 设置“保护线程”
		csctth.setPriority(7);
		csctth.start();// 开启线程
		
		/*************************开启“UDP客户端指令接收线程”************************
			此线程作用：仅用于数据库操作，例如用户修改昵称，则此线程讲接收命令，服务器将对指定用户的账
		户信息进行修改
		**************************************************************************/
		// 创建线程：就绪
		Thread t = new Thread(new RecCommand(), "RecSendCommand(DataBase)");
		t.setPriority(7);
		t.setDaemon(true);// 设置“保护线程”
		t.start();
	}

	private void init() {
		//初始化
		ServerAction.JTArecord.append("Chat server | Java version\r\n");
		ServerAction.JTArecord.append("Author : AlancoldCity\r\n");
		ServerAction.JTArecord.append("Mailbox : AlanLw721@outlook.com\r\n");
		ServerAction.JTArecord.append("Time : 9 June 2019 | basically completed.\r\n");
		ServerAction.JTArecord.append("--------------------------------------------------------------------------------------------\r\n");
		ServerAction.JTArecord.append("Server Start Time："+Util.getTime()+"\r\n");//时间
		ServerAction.JTArecord.append("--------------------------------------------------------------------------------------------\r\n");
	}

	//事件处理
	@Override
	public void actionPerformed(ActionEvent e) 
	{
		if(e.getActionCommand() == "移除联系人(R)") {
			SingleChatInitRun.SingleMode().setVisible(true);; //启动删除联系人的窗体
		}
		else if(e.getActionCommand() == "查看信息(L)") {
			ClientInfoRun.SingleMode().setVisible(true);;
		}
		else if(e.getActionCommand() == "关于(A)") {
			JOptionPane.showMessageDialog(jframe, "Author：AlancoldCity\r\nTest Version：1.5\r\nSpecial Thanks：plus、CheatE\r\nEmail：AlanLw721@outlook.com","ChatRoom Test Version 1.5",JOptionPane.DEFAULT_OPTION);
		}
	}
}
