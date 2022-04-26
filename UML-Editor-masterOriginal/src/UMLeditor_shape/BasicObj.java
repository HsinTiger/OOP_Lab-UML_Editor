package UMLeditor_shape;

import java.awt.*;

public abstract class BasicObj extends Shape{
	private int offset = 5; //
	protected int width, height;

	protected String objectName = "Object Name";
	protected Font font = new Font(Font.SERIF, Font.ITALIC, 14);

	protected Port[] ports = new Port[4];

	/*只留了一個draw方法給兒子實現*/
	public abstract void draw(Graphics g);

	//選中畫出四角 port繼承rec所以有這四個屬性
	public void show(Graphics g) {
		for(int i = 0; i < ports.length; i++) {
			g.fillRect(ports[i].x, ports[i].y, ports[i].width, ports[i].height);
			g.setColor(new Color(150, 100, 100));
			g.drawRect(ports[i].x, ports[i].y, ports[i].width, ports[i].height);
			g.setColor(new Color(150, 180, 180));
		}
	}
	
	public String inside(Point p) {
		Point center = new Point();
		center.x = (x1 + x2) / 2;
		center.y = (y1 + y2) / 2;
		Point[] points = { new Point(x1, y1), new Point(x2, y1), new Point(x2, y2), new Point(x1, y2) };

		for (int i = 0; i < points.length; i++) {
			Polygon t = new Polygon();
			// points tuple(0,1,center) (1,2,center) (2,3,center) (3,0,center) 北東南西四瓣
			int secondIndex = ((i + 1) % 4);
			t.addPoint(points[i].x, points[i].y);
			t.addPoint(points[secondIndex].x, points[secondIndex].y);
			t.addPoint(center.x, center.y);
			//上面在判斷點落於shape內四瓣之哪瓣
			if (t.contains(p)) {
				System.out.println("Yes,you point into the BasicObj.And the part is "+Integer.toString(i));
				return Integer.toString(i);  //會return你是選到這個物件的第幾個part 北東南西
			}
		}
		return null;
	}
	
	public Port getPort(int portIndex) {
		return ports[portIndex];
	}
	
	public void resetLocation(int moveX, int moveY) {
		/**
		int x1 = this.x1 + moveX;
		int y1 = this.y1 + moveY;
		this.x1 = x1;
		this.y1 = y1;
		this.x2 = x1 + width;
		this.y2 = y1 + height;
		 **/
		this.x1 = this.x1 + moveX;
		this.y1 = this.y1 + moveY;
		this.x2 = this.x1 + width;
		this.y2 = this.y1 + height;
		int[] xpoint = {(x1+x2)/2, x2 + offset, (x1+x2)/2, x1 - offset}; //北東南西四個位子的port之中心座標
		int[] ypoint = {y1 - offset, (y1+y2)/2, y2+offset, (y1+y2)/2};

		//當今天基本物件位置更動,ports跟對應的line都要跟著變,而lines是跟著port跑的,port是跟著basicObj跑的
		for(int i = 0; i < ports.length; i++) {
			ports[i].setPort(xpoint[i], ypoint[i], offset);
			ports[i].resetLines();
		}
	}
	
	public void changeName(String name){
		this.objectName = name;
	}
	
	protected void createPorts() {
		int[] xpoint = {(x1+x2)/2, x2 + offset, (x1+x2)/2, x1 - offset};
		int[] ypoint = {y1 - offset, (y1+y2)/2, y2+offset, (y1+y2)/2};

		for(int i = 0; i < ports.length; i++) {
			Port port = new Port();
			port.setPort(xpoint[i], ypoint[i], offset);
			ports[i] = port;
		}
	}
}
