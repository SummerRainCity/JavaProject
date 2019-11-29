package 键盘检测;

/**
 * 作者：@author AlancoldCity
 * 日期：2019-11-29
 * 功能：
 */
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Control_adjustment extends JFrame {

	// 定义变量
	JButton jbu = new JButton("按钮");
	JLabel biaoqian = new JLabel("QQ");

	public static void main(String[] args) {
		new Control_adjustment();
	}

	// 构造函数
	public Control_adjustment() {
		this.setLayout(null);

		jbu.setBounds(50, 50, 100, 40);
		biaoqian.setBounds(10, 10, 100, 50);
		this.add(jbu);
		this.add(biaoqian);

		//用法
		SetupMon mp = new SetupMon(jbu, this);
		this.addKeyListener(mp);// 注册监听

		this.setSize(600, 300);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);
	}
}

/*
 * 目的：实现关于自由布局下，控件的大小宽高实时调试
 * 
 * 使用方法：
 * 1、可以使用方向键：上下左右键，操作组建的移动
 * 2、使用Alt+“+”调整组建的宽度增大 
 * 3、使用Alt+“-”调整组建的宽度减小
 * 4、使用Alt+“/”调整组建的高度增大 
 * 5、使用Alt+“*”调整组建的高度减小
 * 
 * 使用格一（外部设置jframe.setFocusable(true)）
 * SetupMon obj = new SetupMon([传入组建:按钮|标签|文本框...]);
 * this.addKeyListener(obj);// 注册监听
 * 
 * 使用格二： 
 * SetupMon obj = new SetupMon([传入组建:按钮|标签|文本框...], JFrame);
 * this.addKeyListener(obj);// 注册监听
 * 
 * 时间：2019年11月29日
 */
class SetupMon extends JPanel implements KeyListener {
	private static final long serialVersionUID = 1L;
	Component dx = null;
	int x, y, width, height;

	public SetupMon() {
	}

	public SetupMon(Component dx) {
		this.dx = dx;
		this.x = dx.getX();
		this.y = dx.getY();
		this.width = dx.getWidth();
		this.height = dx.getHeight();
		System.out.println("初始坐标：" + x + "-" + y + "-" + width + "-" + height);
	}

	public SetupMon(Component dx, JFrame jframe) {
		this.dx = dx;
		this.x = dx.getX();
		this.y = dx.getY();
		this.width = dx.getWidth();
		this.height = dx.getHeight();
		jframe.setFocusable(true);
		System.out.println("初始坐标：" + x + "-" + y + "-" + width + "-" + height);
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if (e.isAltDown() && e.getKeyCode() == KeyEvent.VK_ADD) {
			width += 5;
			dx.setBounds(x, y, width, height);
		} else if (e.isAltDown() && e.getKeyCode() == KeyEvent.VK_SUBTRACT) {
			width -= 5;
			dx.setBounds(x, y, width, height);
		} else

		if (e.isAltDown() && e.getKeyCode() == KeyEvent.VK_DIVIDE) {
			height += 5;
			dx.setBounds(x, y, width, height);
		} else if (e.isAltDown() && e.getKeyCode() == KeyEvent.VK_MULTIPLY) {
			height -= 5;
			dx.setBounds(x, y, width, height);
		} else

		if (e.getKeyCode() == KeyEvent.VK_UP) {
			y -= 2;
			dx.setBounds(x, y, width, height);
		} else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
			y += 2;
			dx.setBounds(x, y, width, height);
		} else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
			x -= 2;
			dx.setBounds(x, y, width, height);
		} else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
			x += 2;
			dx.setBounds(x, y, width, height);
		}
		System.out.println("当前组建坐标[x=" + this.x + ",y=" + this.y + "]--当前组建大小[width=" + this.width + ",height="
				+ this.height + "]");
	}

	@Override
	public void keyReleased(KeyEvent e) {
	}

	@Override
	public void keyTyped(KeyEvent e) {
	}
}
