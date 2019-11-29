package ���̼��;

/**
 * ���ߣ�@author AlancoldCity
 * ���ڣ�2019-11-29
 * ���ܣ�
 */
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Control_adjustment extends JFrame {

	// �������
	JButton jbu = new JButton("��ť");
	JLabel biaoqian = new JLabel("QQ");

	public static void main(String[] args) {
		new Control_adjustment();
	}

	// ���캯��
	public Control_adjustment() {
		this.setLayout(null);

		jbu.setBounds(50, 50, 100, 40);
		biaoqian.setBounds(10, 10, 100, 50);
		this.add(jbu);
		this.add(biaoqian);

		//�÷�
		SetupMon mp = new SetupMon(jbu, this);
		this.addKeyListener(mp);// ע�����

		this.setSize(600, 300);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);
	}
}

/*
 * Ŀ�ģ�ʵ�ֹ������ɲ����£��ؼ��Ĵ�С���ʵʱ����
 * 
 * ʹ�÷�����
 * 1������ʹ�÷�������������Ҽ��������齨���ƶ�
 * 2��ʹ��Alt+��+�������齨�Ŀ������ 
 * 3��ʹ��Alt+��-�������齨�Ŀ�ȼ�С
 * 4��ʹ��Alt+��/�������齨�ĸ߶����� 
 * 5��ʹ��Alt+��*�������齨�ĸ߶ȼ�С
 * 
 * ʹ�ø�һ���ⲿ����jframe.setFocusable(true)��
 * SetupMon obj = new SetupMon([�����齨:��ť|��ǩ|�ı���...]);
 * this.addKeyListener(obj);// ע�����
 * 
 * ʹ�ø���� 
 * SetupMon obj = new SetupMon([�����齨:��ť|��ǩ|�ı���...], JFrame);
 * this.addKeyListener(obj);// ע�����
 * 
 * ʱ�䣺2019��11��29��
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
		System.out.println("��ʼ���꣺" + x + "-" + y + "-" + width + "-" + height);
	}

	public SetupMon(Component dx, JFrame jframe) {
		this.dx = dx;
		this.x = dx.getX();
		this.y = dx.getY();
		this.width = dx.getWidth();
		this.height = dx.getHeight();
		jframe.setFocusable(true);
		System.out.println("��ʼ���꣺" + x + "-" + y + "-" + width + "-" + height);
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
		System.out.println("��ǰ�齨����[x=" + this.x + ",y=" + this.y + "]--��ǰ�齨��С[width=" + this.width + ",height="
				+ this.height + "]");
	}

	@Override
	public void keyReleased(KeyEvent e) {
	}

	@Override
	public void keyTyped(KeyEvent e) {
	}
}
