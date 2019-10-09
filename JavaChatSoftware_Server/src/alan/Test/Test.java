package alan.Test;

import javax.swing.UIManager;

import alan.Viem.ServerGUIRun;

public class Test 
{
	public static void main(String[] args) 
	{
		try {
//			UIManager.setLookAndFeel(javax.swing.plaf.nimbus.NimbusLookAndFeel.class.getName());//还可以（可用）
//			UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");//windows风格-OK-就是当前风格
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());//当前系统风格-OK
//			UIManager.setLookAndFeel("com.sun.java.swing.plaf.motif.MotifLookAndFeel");//Motif风格，是蓝黑（默然黑）-OK
//			UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());//跨平台的Java风格-原生
			} catch (Exception e) {}
		
		new ServerGUIRun();
	}
}
