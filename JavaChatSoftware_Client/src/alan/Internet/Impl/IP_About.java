package alan.Internet.Impl;
/**
 * IP地址统一管理静态类(SERVER)
 * */
public class IP_About 
{
	private IP_About() {}
	public static String IPaddress = "106.15.194.163";//服务端IP地址106.15.194.163
	public static final int RepairProt = 33333;//维护联系人-端口22222
	public static final int MainProt = 22222;//消息通信主要-端口33333
	public static final int SingleProt = 32600;//一对一单人聊天-端口32600
	public static final int UDPProt = 8372;//UDP端口
	public static final int DatabaseFileProt = 39654;
}