package alan.FileIO;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

import javax.swing.JOptionPane;

import alan.Internet.Impl.IP_About;

public class ReceiveFile 
{
	//客户端接收文件
		public void recFile()
		{
			// 创建客户端Socket对象
			Socket s = null;
			try {
				s = new Socket(IP_About.IPaddress, IP_About.DatabaseFileProt);
			} catch (IOException e) {}
			
			BufferedReader br = null;
			BufferedWriter bw = null;
			try {
				//封装通道内的流（接收客户端发来的数据）
				br = new BufferedReader(new InputStreamReader(s.getInputStream()));//Input和Reader表接收
				// 封装输出文件，讲自动生成此文件。
				bw = new BufferedWriter(new FileWriter(new File("").getAbsolutePath()+"\\datarelated\\conf\\security\\policy\\us\\userInfo.txt"));
//				BufferedWriter bw = new BufferedWriter(new FileWriter("userInfo.txt"));
			} catch (IOException e) {
				JOptionPane.showMessageDialog(null, "接收文件时写入路径错误，错误信息："+new File("").getAbsolutePath()+"\\datarelated\\conf\\security\\policy\\us\\userInfo.txt", "错误", JOptionPane.ERROR_MESSAGE);
				System.exit(-1);
			}
			
			try {
				String line = null;
				while((line=br.readLine())!=null) 
				{
					bw.write(line);
					bw.newLine();
					bw.flush();
				}
				s.close();
				bw.close();
			} catch (Exception e) {}
		}
}
