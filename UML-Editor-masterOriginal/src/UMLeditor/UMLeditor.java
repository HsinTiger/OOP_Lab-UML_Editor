package UMLeditor;

import java.awt.BorderLayout;
import javax.swing.JFrame;

@SuppressWarnings("serial")
public class UMLeditor extends JFrame{
	private ToolBar toolbar;
	private Canvas canvas;
	private MenuBar menubar;
	public UMLeditor() {
		canvas = Canvas.getInstance();   // Canvas is singleton 
		toolbar = new ToolBar();
		menubar = new MenuBar();
		getContentPane().setLayout(new BorderLayout());
		getContentPane().add(menubar, BorderLayout.NORTH);
		getContentPane().add(toolbar, BorderLayout.WEST);
		getContentPane().add(canvas, BorderLayout.CENTER);  //用預設塞進frame的大小設定canvas畫布大小
	}
	
	public static void main(String[] args) {
		UMLeditor mainWindow = new UMLeditor();
		mainWindow.setTitle("UML editor");
		mainWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainWindow.setSize(1200, 700);
		mainWindow.setLocationRelativeTo(null);  //將視窗設定在螢幕正中間
		mainWindow.setVisible(true);
	}
	
	
}
