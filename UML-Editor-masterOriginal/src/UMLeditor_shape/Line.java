package UMLeditor_shape;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.geom.Line2D;

public abstract class Line extends Shape{
	//lines attributes
	protected Port[] ports = new Port[2];  //every line connects two ports


	public abstract void draw(Graphics g);	//draw line method留下此func給後代line多型
	private String selectedFlag = null;  //resetStartEnd，getselectedFlagOfLine(),getOriginalPortsOfLine要用且只有inside函數會改變其值
	//

	public void setPorts(Port port_1, Port port_2) {
		this.ports[0] = port_1;
		this.ports[1] = port_2;
	}

	//Set up the Graphic G to show？？？？？？？？？？
	public void show(Graphics g) {
		g.setColor(new Color(50, 171, 175));  //line被選中 此處判斷有瑕疵
		this.draw(g); //draw method will implement in later class
 		g.setColor(Color.white);//?????????
	}

	//reset 起終點座標 for "templine"
	public void resetStartEnd(Point p) {
		if(selectedFlag.equals("start")){
			this.x1 = p.x;
			this.y1 = p.y;
		}
		else if(selectedFlag.equals("end")) {
			this.x2 = p.x;
			this.y2 = p.y;
		}
	}
	//reset 起終點座標of port
	public void resetLocation(){ //connect any two ports
		this.x1 = (int) ports[0].getCenterX();  //get the center co-ordinate of rectangle by awt
		this.y1 = (int) ports[0].getCenterY();
		this.x2 = (int) ports[1].getCenterX();
		this.y2 = (int) ports[1].getCenterY();
	}
	//將line連到新的port, reset 起終點port
	public void resetPort(Port port, Line line) {
		port.addLine(line);
		if(selectedFlag.equals("start")){
			this.ports[0].removeLine(line);
			this.ports[0] = port;
		}
		else if(selectedFlag.equals("end")){
			this.ports[1].removeLine(line);
			this.ports[1] = port;
		}
	}
	//only inside func can call it,private
	private double distance(Point p) {
		Line2D line = new Line2D.Double(x1, y1, x2, y2);  //the line
		return line.ptLineDist(p.getX(), p.getY()); //any point p distance to the line
	}
	//line的inside函數會順便判別"insideLine"與否＆＆selectedFlag = "start"or"end";不一定都成立
	public String inside(Point p) {
		int tolerance = 3;
		if(distance(p) < tolerance) {
			double distToStart = Math.sqrt(Math.pow((p.x - x1),2) + Math.pow((p.y - y1), 2));
			double distToEnd = Math.sqrt(Math.pow((p.x - x2),2) + Math.pow((p.y - y2), 2));
			if((distToStart < distToEnd)&&(distToStart < 10)){  //有選到起點或終點
				selectedFlag = "start";
				System.out.println(selectedFlag + "is to be set");
			}
			else if((distToStart >= distToEnd)&&(distToEnd < 10)){
				selectedFlag = "end";
				System.out.println(selectedFlag + "is to be set");
			}
			return "insideLine";		//有選到線段的一部分
		}
		else
			return null;
	}

	public Port getOriginalPortsOfLine(String selectedFlag){
		if (selectedFlag.equals("start")){
			return ports[0];
		}
		else if (selectedFlag.equals("end")) {
			return ports[1];
		}
		else{
			return null;
		}
	}
	public String getselectedFlagOfLine(){
		return selectedFlag;
	}



}