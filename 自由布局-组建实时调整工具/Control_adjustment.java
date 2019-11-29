/**
 * 作者：@author SummerRainCity
 * 日期：2019-11-29
 * 功能：使用键盘按键实时调整自由布局下的组建
 * 思路：添加键盘监听器，按下按键调用组件函数setBounds(x,y,width,height)使得组件
 * 更新自身状态。如果你尝试使用for循环去执行setBounds函数，若每次循环参数不一致，
 * 你会发现组件的状态也会发生变化，利用此特性与键盘事件监听结合即可实时控制组件的
 * 位置、大小。
 *
 * 注：由于不知道到底传入那个组件，所以使用的Component类引用接它的子类对象，使其
 * 调整组件位置、大小时不局限于某个具体的组件。
 */
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/*
 * 目的：实现关于自由布局下，控件的大小宽高实时调整	
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
		System.out.println("当前组件坐标[x=" + this.x + ",y=" + this.y + "]--当前组件大小[width=" + this.width + ",height="
				+ this.height + "]");
	}

	@Override
	public void keyReleased(KeyEvent e) {}
	@Override
	public void keyTyped(KeyEvent e) {}
}

//例子：
public class Control_adjustment extends JFrame {
	JButton jbu = new JButton("按钮");//标签组件
	JLabel biaoqian = new JLabel("标签");//按钮组件

	public static void main(String[] args) {
		new Control_adjustment();
	}

	public Control_adjustment() {
		this.setLayout(null);

		jbu.setBounds(50, 50, 100, 40);
		biaoqian.setBounds(10, 10, 100, 50);
		this.add(jbu);
		this.add(biaoqian);

		//用法：传入标签biaoqian
		//SetupMon mp = new SetupMon(jbu, this);
		//this.addKeyListener(mp);

		//用法：传入按钮jbu
		SetupMon mp = new SetupMon(jbu, this);
		this.addKeyListener(mp);

		this.setSize(600, 300);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);
	}
}