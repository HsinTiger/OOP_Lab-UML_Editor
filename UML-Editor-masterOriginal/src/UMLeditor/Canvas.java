package UMLeditor;
/**
import java.awt.*;
import java.util.*;
 **/
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.EventListener;
import java.util.List;

import javax.swing.*;

import UMLeditor_mode.Mode;
import UMLeditor_shape.Group; //下面會用到
import UMLeditor_shape.Shape;

@SuppressWarnings("serial")
public class Canvas extends JPanel {
	private static Canvas instance = null; // for singleton單例模式 節省資源
	/* Singleton design pattern */
	private Canvas() {
		// Exists only to defeat instantiation.  不給任意的new
	}
	public static Canvas getInstance() {
		if (instance == null) {
			instance = new Canvas();
		}
		return instance;
	}
	//-------------------------------------------------------------------------------
	private EventListener listener = null;  //EventListener是任何listener的父類別
	protected Mode currentMode = null;

	private List<Shape> shapes = new ArrayList<Shape>(); //有哪些shape基本物件在畫布上，含use_case及class、lines們，多型

	public Shape tempLine = null;  //尚未畫完的line__畫到一半 以shape參考承接，等等要用多型


	public Rectangle SelectedArea = new Rectangle();
	public Shape selectedObj = null; //shape 不是abstract嗎？可以令出來是怎麼回事



	public void setCurrentMode() {//????????畫布上加入滑鼠ｌｉｓｔｅｎｅｒ
		this.removeMouseListener((MouseListener) listener);
		this.removeMouseMotionListener((MouseMotionListener) listener);
		listener = currentMode;
		this.addMouseListener((MouseListener) listener);
		this.addMouseMotionListener((MouseMotionListener) listener);
	}
	
	public void reset() { //畫布畫面清空，白板擦乾淨
		if(selectedObj != null){
			//selectedObj.resetSelectedShape();   //這方法實作在哪裡呢？其實他啥也沒做
			selectedObj = null;  //真正清空畫布基礎物件的是這裡
		}
		SelectedArea.setBounds(0, 0, 0, 0);  //清空畫布方框
	}



	
	public void addShape(Shape shape) {
		shapes.add(shape);
	} //畫布上新增的物件們
	public List<Shape> getShapeList() {
		return this.shapes;
	}

	public void GroupShape() {
		Group group = new Group();
		for (int i = 0; i < shapes.size(); i++) {
			Shape shape = shapes.get(i);
			if (shape.group_selected) {  //shape list(shapes)裡的某個shape有沒有被選中，看其四角有沒有都被包含在選匡裡面
				group.addShapes(shape);
				shapes.remove(i);
				i--;		//當前這個shape已被選中，for loop下一輪從下一個開始
			}
		}
		group.setBounds();
		shapes.add(group);		//Group是繼承自shape,所以可以加進這個list???
		reset();
		repaint();
	}
	public void removeGroup() {
		Group group = (Group) selectedObj;  //當前在畫布上選中一組group???down_casting
		List<Shape> groupShapes = group.getShapes();  //group內的function 將group物件轉換成list形式
		for(int i = 0; i < groupShapes.size(); i++){
			//Shape shape = groupShapes.get(i);
			shapes.add(groupShapes.get(i));
		}
		shapes.remove(selectedObj);
		reset();
		repaint();
	}

	public void changeObjName(String name) {
		if(selectedObj != null){
			selectedObj.changeName(name);  //運用shape父類別達成多型
			repaint();
		}
		else{
			JFrame Alert = new JFrame("Alert");
			Alert.setSize(200, 100);
			JTextArea YouWrong = new JTextArea("You didn't\n" +
					"select any object\n" +
					"to change name,\n" +
					"dude!");
			YouWrong.setSelectedTextColor(Color.green);
			Alert.getContentPane().add(YouWrong);
			Alert.setLocationRelativeTo(null);
			Alert.setVisible(true);
		}
	}

	//check該特定要檢驗的物件是否有完全被選中
	private boolean checkSelectedArea(Shape shape) {
		Point upperleft = new Point(shape.getX1(), shape.getY1());
		Point lowerright = new Point(shape.getX2(), shape.getY2());		//得到該shape基本物件ＯＲ群組的，左上右下邊界座標
		/* show ports of selected objects */
		if (SelectedArea.contains(upperleft) && SelectedArea.contains(lowerright)) {
			return true;
		}
		return false;
	}

	//Canvas畫布區域＿背景等基礎設定，以及所有需要被draw在畫布上的各種物件及情況
	@Override //repaint會呼叫到update 再來是 paint
	public void paint(Graphics g) {			//丟進來的只會是Graphics2D或另一個非abstract Class
		/* set canvas area */
		Dimension dim = this.getSize();  //沒有this也可以？ canvas是預設大小
		g.setColor(new Color(35, 37, 37));
		g.fillRect(0, 0, dim.width, dim.height);
		/* set painting color */
		g.setColor(new Color(150, 180, 180));		//在設定g這幅畫中接下來的畫筆顏色，並交給Graphics2D g2
		Graphics2D g2 = (Graphics2D) g;  //down_casting
		g2.setStroke(new BasicStroke(3,BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER));
		
		/* paint all shape objects */
		for (int i = shapes.size() - 1; i >= 0; i--) {
			Shape shape = shapes.get(i);
			shape.group_selected = false;
			shape.draw(g);		//將設定好的g各種屬性畫出來在canvas，實作在哪？？？？？？？？？？？其實這裡是多型，每個基本物件的draw不同
			/* check group select */
			//當物件被鼠標方框選到，就秀出其四角以表示被選中
			if (!SelectedArea.isEmpty() && checkSelectedArea(shape)) {
				shape.show(g);   //???????????????其實這裡是多型，每個基本物件的show不同
				shape.group_selected = true;
			}
		}

		/* 畫出被使用者動態拖曳中的線段tempLine */
		if (tempLine != null) {
			tempLine.draw(g);		//實作又在哪裡？？？？？？多型，templine可能是多種line屬性之一，畫出來當然不同
			//如果有templine 則要畫出來
		}
		/* show ports when object is selected for draw，且一次只能選中一個obj */
		if (this.selectedObj != null) {
			selectedObj.show(g);
		}
		/*匡選group時的顏色匡＿draw的部分*/
		if (!SelectedArea.isEmpty()) {
			int alpha = 85; // 33% transparent
			g.setColor(new Color(37, 148, 216, alpha));
			g.fillRect(SelectedArea.x, SelectedArea.y, SelectedArea.width, SelectedArea.height);

			g.setColor(new Color(37, 148, 216));
			g.drawRect(SelectedArea.x, SelectedArea.y, SelectedArea.width, SelectedArea.height);  //長方形邊框顏色

		}
	}
}
