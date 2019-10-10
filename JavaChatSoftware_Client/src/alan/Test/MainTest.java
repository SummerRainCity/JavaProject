package alan.Test;
import javax.swing.UIManager;

import alan.Viem.StartGUIRun;

public class MainTest 
{
	public static void main(String[] args) throws InterruptedException 
	{
		try {
//			UIManager.setLookAndFeel(javax.swing.plaf.nimbus.NimbusLookAndFeel.class.getName());
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
//			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
//			UIManager.setLookAndFeel("com.sun.java.swing.plaf.motif.MotifLookAndFeel");
//			UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
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
