package alan.Test;
import javax.swing.UIManager;

import alan.Viem.StartGUIRun;

/*时间：2019年6月3日12:18:32*/
public class MainTest 
{
	public static void main(String[] args) throws InterruptedException 
	{
		try {
//			UIManager.setLookAndFeel(javax.swing.plaf.nimbus.NimbusLookAndFeel.class.getName());//还可以（可用）
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");//windows风s格-OK-就是当前风格
//			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());//当前系统风格-OK
//			UIManager.setLookAndFeel("com.sun.java.swing.plaf.motif.MotifLookAndFeel");//Motif风格，是蓝黑（默然黑）-OK
//			UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());//跨平台的Java风格-原生
			} catch (Exception e) {}
//		try {
//			for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
//				javax.swing.UIManager.setLookAndFeel(info.getClassName());
//				Thread.sleep(2000);
				new StartGUIRun();
//			}
//		} catch (Exception ex) {
//		}
	}
}