package alan.FIleIOServer;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;

import javax.swing.JOptionPane;

import alan.Internet.IP_About;

public class SendFile 
{
	// 服务端发送文件
		public void sendFile()
		{
			ServerSocket ss = null;
			Socket s = null;
			try {
				ss = new ServerSocket(IP_About.DatabaseFileProt);
				s = ss.accept();
			} catch (IOException e) {
				
			}
			
			BufferedReader br = null;
			BufferedWriter bw = null;
			try {
				// 创建键盘输入流对象
				br = new BufferedReader(new FileReader(new File("").getAbsolutePath()+"\\database.txt"));// 键盘录入流
				// 把通道内流包装(包装Socket通道传输流，使用BufferedWriter的操作进行通信)
				bw = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));// Output表发送
			} catch (IOException e) {
				JOptionPane.showMessageDialog(null, "SERVER发送文件异常，错误路径："+new File("").getAbsolutePath()+"\\database.txt", "错误", JOptionPane.ERROR_MESSAGE);
				System.exit(-1);
			}

			// 发送数据
			String line = null;
			try {
				while ((line = br.readLine()) != null) // 文本文件读取到末尾返回null
				{
					String[] data = line.split("=");
					bw.write(data[0]+"="+data[2]);
					bw.newLine();
					bw.flush();
				}
			} catch (Exception e) {}
			try {
				br.close();
				s.shutdownOutput();// B方案解决
				ss.close();
			} catch (IOException e) {}
		}
}
