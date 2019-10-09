package alan.Util;
/**********************
 * 《工具包-改图标、窗体居中》
 本类作用：
 * 1，专门处理用户界面的图标、背景、窗体位置....等设置
 ***********************/
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Toolkit;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.JFrame;

public class Util 
{
    private Util(){}
    
    // 修改窗体程序图标（传递的是窗体对象，你可以用画图软件生成）
    public static void setFrameImage(JFrame jf){
        // 获取工具包对象public static Toolkit getDefaultToolkit()
        Toolkit tk = Toolkit.getDefaultToolkit();
        // 根据路径获取图片（只支持jpg）
        //注：获取包下的是相对路径[包名+图名]
        Image i = tk.getImage("src\\cn\\itcast\\resource\\isqico.jpg");
        // 给窗体设置图标
        jf.setIconImage(i);
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
    
    //获取当前时间
    public static String getTime(){
    	//格式：yyyy-MM-dd HH:mm:ss
    	Date d = new Date();//声明时间对象
    	String time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(d);
    	return time;
    }
}