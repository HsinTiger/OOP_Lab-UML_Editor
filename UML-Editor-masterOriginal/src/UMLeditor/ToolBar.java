package UMLeditor;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;

import UMLeditor_mode.Mode;
import UMLeditor_mode.createLineMode;
import UMLeditor_mode.createObjMode;
import UMLeditor_mode.selectMode;
import UMLeditor_shape.Shape;

@SuppressWarnings("serial")
public class ToolBar extends JToolBar{
	private int ToolNum = 6;
	private Color myColor = new Color(20, 171, 175);
	private Color iniColor = new Color(100, 100, 200);


	private JButton holdBtn = null;
	private Canvas canvas;		//composite而非inherit

	private ToolBtn selectBtn;//=new ToolBtn("select", new selectMode());;
	private ToolBtn associateBtn;//=new ToolBtn("associate", new createLineMode("associate"));
	private ToolBtn generalBtn;//=new ToolBtn("general", new createLineMode("general"));
	private ToolBtn compositeBtn;//=new ToolBtn("composite", new createLineMode("composite"));
	private ToolBtn classBtn;//=new ToolBtn("class", new createObjMode("class"));
	private ToolBtn usecaseBtn;//=new ToolBtn("usecase", new createObjMode("usecase"));


	public ToolBar() {
		canvas = Canvas.getInstance();   // Canvas is singleton單例模式
		this.setLayout(new GridLayout(ToolNum, 1, 5, 10));
		this.setBackground(new Color(178, 85, 180));
		this.setFloatable(false);

		//JTextArea btnS = new JTextArea("select");
		//ImageIcon selectIcon = new ImageIcon("img/select.png");
		//
		selectBtn = new ToolBtn("select", new selectMode());
		add(selectBtn);

		//JTextArea btnA = new JTextArea("associate");
		//ImageIcon associateIcon = new ImageIcon("img/associate.png");
		//
		associateBtn = new ToolBtn("associate", new createLineMode("associate"));
		add(associateBtn);

		//JTextArea btnG = new JTextArea("general");
		//ImageIcon generalIcon = new ImageIcon("img/general.png");
		//
		generalBtn = new ToolBtn("general", new createLineMode("general"));
		add(generalBtn);

		//JTextArea btnC = new JTextArea("composite");
		//ImageIcon compositeIcon = new ImageIcon("img/composite.png");
		//
		compositeBtn = new ToolBtn("composite", new createLineMode("composite"));
		add(compositeBtn);

		//JTextArea btnClass = new JTextArea("class");
		//ImageIcon classIcon = new ImageIcon("img/class.png");
		//
		classBtn = new ToolBtn("class", new createObjMode("class"));
		add(classBtn);

		//JTextArea btnUse_case = new JTextArea("use_case");
		//ImageIcon usecaseIcon = new ImageIcon("img/usecase.png");
		//
		usecaseBtn = new ToolBtn("usecase", new createObjMode("usecase"));  //下方實作巢狀class ToolBtn
		add(usecaseBtn);
		System.out.println("i am used");

	}
	private class ToolBtn extends JButton{	//6個tool btn全是Jbutton的後代
		protected Mode ToolMode;  //每個button都代表一個ｍｏｄｅ的ＡＰＩ

		public ToolBtn(String ToolName, Mode ToolMode) {
			this.ToolMode = ToolMode;	//initial to which tool_mode
			this.setToolTipText(ToolName);	//使用者＿滑鼠tips
			this.setFocusable(true); //讓他可以被選中？以API預設的方式呈現焦點

			switch (ToolName) {
				case "select":
					setText("select");
					break;
				case "associate":
					setText("associate");
					break;
				case "general":
					setText("general");
					break;
				case "composite":
					setText("composite");
					break;
				case "class":
					setText("class");
					break;
				case "usecase":
					setText("usecase");
					break;
			}
			this.setOpaque(true); //btn背景不透明
			this.setBorderPainted(false);  //讓按鈕各顆獨立於整行toolbar出來
			this.setBackground(iniColor);
			this.setRolloverEnabled(false);
			this.addActionListener(new toolListener());  //在ToolBtn中加入監聽器,下方實作toolListener 巢狀class
		}
		class toolListener implements ActionListener {
			public void actionPerformed(ActionEvent e) {
				setBtnInitial(selectBtn);
				setBtnInitial(associateBtn);
				setBtnInitial(generalBtn);
				setBtnInitial(compositeBtn);
				setBtnInitial(classBtn);
				setBtnInitial(usecaseBtn);

				holdBtn = (JButton) e.getSource();	//返回哪個ToolBtn引起的事件 將其轉型為Jbtn

				System.out.println(e.getActionCommand());
				System.out.println(holdBtn);

				holdBtn.setBackground(myColor);


				canvas.currentMode = ToolMode;  //幫Canvas畫布區域進行狀態轉換
				canvas.setCurrentMode();

				canvas.reset();		//畫布物件清空（包含原＿被選中基礎物件以及鼠標方框），並且重畫
				canvas.repaint();
			}
		}
	}

	private void setBtnInitial(ToolBtn whichBtn){
		whichBtn.setBackground(iniColor);
	}

}
