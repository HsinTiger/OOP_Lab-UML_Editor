package UMLeditor_shape;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

public class Group extends Shape {
	private List<Shape> shapes = new ArrayList<Shape>(); //該group內的所有shape成員
	private Rectangle bounds = new Rectangle();
	private Shape selectedShape = null;  //決定是否單純秀出整個group或者有某個shape被特別選中

	public void draw(Graphics g) { // show bounds
		for (int i = 0; i < shapes.size(); i++) {
			Shape shape = shapes.get(i);
			shape.draw(g);
		}
	}

	public void show(Graphics g) {
		int alpha = 85; // 33% transparent
		int offset = 10;
		g.setColor(new Color(110, 219, 181, alpha));
		g.fillRect(bounds.x - offset, bounds.y - offset, bounds.width + offset * 2, bounds.height + offset * 2);
		g.setColor(new Color(110, 219, 181));
		g.drawRect(bounds.x - offset, bounds.y - offset, bounds.width + offset * 2, bounds.height + offset * 2);
		g.setColor(Color.white);		//????????
		if (selectedShape != null) {  //決定是否單純秀出整個group或者有某個shape被特別選中
			selectedShape.show(g);
		}
	}

	public void resetLocation(int moveX, int moveY) {
		for (int i = 0; i < shapes.size(); i++) {
			Shape shape = shapes.get(i);
			shape.resetLocation(moveX, moveY);
		}
		resetBounds(moveX, moveY);  //??????
	}

	//有沒有在group的任何成員shape內
	public String inside(Point p) {
		for (int i = 0; i < shapes.size(); i++) {
			Shape shape = shapes.get(i);
			String judgeInside = shape.inside(p);
			if (judgeInside != null) {
				selectedShape = shape;
				return "insideGroup";
			}
		}
		return null;
	}

	public void changeName(String name) {
		selectedShape.changeName(name); //basicObj多型
	}

	public void resetSelectedShape() {
		selectedShape = null;
	}
	public Shape getSelectedShape() {
		return selectedShape;
	}
	
	public void setBounds() {
		/* find most left,right,up and down objects, set group bounds！！！！！！！！！！！！！！！ */
		Point upLeftP, bottomRightP;
		int leftX = Integer.MAX_VALUE, rightX = Integer.MIN_VALUE;
		int upY = Integer.MAX_VALUE, bottomY = Integer.MIN_VALUE;

		for (int i = 0; i < shapes.size(); i++) {
			Shape shape = shapes.get(i);
			if (shape.getX1() < leftX) {
				leftX = shape.getX1();
			}
			if (shape.getX2() > rightX) {
				rightX = shape.getX2();
			}
			if (shape.getY1() < upY) {
				upY = shape.getY1();
			}
			if (shape.getY2() > bottomY) {
				bottomY = shape.getY2();
			}
		}
		upLeftP = new Point(leftX, upY);
		bottomRightP = new Point(rightX, bottomY);

		bounds.setBounds(upLeftP.x, upLeftP.y, Math.abs(upLeftP.x - bottomRightP.x),
				Math.abs(upLeftP.y - bottomRightP.y)); //左上角起點、width、height
		// 畢竟Group也是Shape的子孫 所以set parent shape coordinate
		x1 = bounds.x;
		y1 = bounds.y;
		x2 = bounds.x + bounds.width;
		y2 = bounds.y + bounds.height;
	}

	protected void resetBounds(int moveX, int moveY) {
		bounds.setLocation(bounds.x + moveX, bounds.y + moveY);
		//此時bounds.x;bounds.y;已改變位址，即左上角座標
		x1 = bounds.x;
		y1 = bounds.y;
		x2 = bounds.x + bounds.width;
		y2 = bounds.y + bounds.height;
	}

	public void addShapes(Shape shape) {
		shapes.add(shape);
	}
	public List<Shape> getShapes() {
		return shapes;
	}  //將所有List內的shape返回去
	

}
