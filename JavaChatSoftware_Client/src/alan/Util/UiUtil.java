package alan.Util;
/*******************************************************************************
 * 《工具包-改图标、窗体居中》
 本类作用：
 * 1，专门处理用户界面的图标、背景、窗体位置....等设置
 * 2，消息声音提示
 * 
 * 注：提示音播放不是由Java本身的类解决的，而是C语言写好的音频播放可执行程序，Java仅负责调用。
 * msgPaly.exe程序：来消息提示。
 * sysPaly.exe程序：有人上线提示音。
 * exitPaly.exe程序：有人下线提示音。
 *******************************************************************************/
//单词：Login（注册、登录）
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Toolkit;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import java.io.File;

public class UiUtil 
{
	// 有人发来消息-提示此音（滴滴滴滴滴...）
	private static final String MsgPath = (new File("").getAbsolutePath())+"\\datarelated\\bin\\plugin2\\msgPaly.exe";
	// 有人上线提示音（new File("")是获取当前路径+后面的指定路径）
	private static final String SysPath = (new File("").getAbsolutePath())+"\\datarelated\\bin\\plugin2\\sysPaly.exe";
	// 有人离线提示音
	private static final String ExiPath = (new File("").getAbsolutePath())+"\\datarelated\\bin\\plugin2\\exitPaly.exe";
	
    private UiUtil(){}
    
    // 修改窗体程序图标（传递的是窗体对象，你可以用画图软件生成）
    public static void setFrameImage(JFrame JF){
        // 获取工具包对象public static Toolkit getDefaultToolkit()
//        Toolkit tk = Toolkit.getDefaultToolkit();
//        
//        // 根据路径获取图片（只支持jpg）
//        //注：获取包下的是相对路径[包名+图名]
//        Image i = tk.getImage("src\\cn\\itcast\\resource\\isqico.jpg");
//        // 给窗体设置图标
//        JF.setIconImage(i);
        
        // 设置图标
		ImageIcon image = new ImageIcon(JF.getClass().getResource("ClientICO.gif"));
		Image icon = image.getImage();
		JF.setIconImage(icon);
    }
    
    // 窗体居中
    public static void setFrameCenter(JFrame jf){
        // 向获取屏幕的宽高，先获取工具类Toolkit
        Toolkit tk = Toolkit.getDefaultToolkit();
        // 获取屏幕的宽和高
        Dimension d = tk.getScreenSize(); //Dimension这个类封装了宽高
        double srceenWidth = d.getWidth();
        double srceenHeight = d.getHeight();
        // 获取窗体的宽、高
        int frameWidth = jf.getWidth();
        int frameHeight = jf.getHeight();
        // 获取新的宽高
        int width = (int)(srceenWidth-frameWidth)/2;
        int height = (int)(srceenHeight-frameHeight)/2;
        // 设置窗体新坐标
        jf.setLocation(width, height);
    }
    
	//设置背景图
    @SuppressWarnings("deprecation")
	public static void setBackMap(JFrame JF, String filename)
    { 
    	((JPanel)JF.getContentPane()).setOpaque(false); 
    	ImageIcon img = new ImageIcon("datarelated/conf/security/policy/unlimited/" + filename); //注：Java中“/”和“\\”都可以表示路径，效果等价。
    	JLabel background = new JLabel(img);
    	JF.getLayeredPane().add(background, new Integer(Integer.MIN_VALUE)); 
    	background.setBounds(0, 0, img.getIconWidth(), img.getIconHeight()); 
    }
	
	// 播放一次来消息的声音（模仿QQ声音）
	public static void playMsg() 
	{
		try {
			//“/b”表示静默运行，不弹出黑框框
			Runtime.getRuntime().exec("cmd /c start /b "+MsgPath);
		} catch (IOException e) {}
    }
	
	// 有好友上线提示音（模仿QQ联系人上线提示音）
	public static void playSys() 
	{
		try {
			Runtime.getRuntime().exec("cmd /c start /b "+SysPath);
		} catch (IOException e) {}
    }
	
	//有人离线-提示音
	public static void playExit() 
	{
		try {
			Runtime.getRuntime().exec("cmd /c start /b "+ExiPath);
		} catch (IOException e) {}
    }
	
	//获取当前时间
    public static String getTime(){
    	//格式：yyyy-MM-dd HH:mm:ss
    	Date d = new Date();//声明时间对象
    	String time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(d);
    	return time;
    }
}