package UMLeditor_shape;

import java.awt.Graphics;
import java.awt.Point;

public abstract class Shape {
	protected int x1, y1, x2, y2;  //左上，右下座標
	public boolean group_selected = false;		//該shape 屬不屬於某個group


	public abstract void draw(Graphics g);
	public abstract void show(Graphics g);    //why show? change to abstract, 被選中時顯示四角
	public int getX1(){
		return x1;
	}
	public int getY1(){
		return y1;
	}
	public int getX2(){
		return x2;
	}
	public int getY2(){
		return y2;
	}
	public void changeName(String name){}
	public String inside(Point p){
		return null;
	} //

	//
	public void resetLocation(){}   // for Line 
	public void resetLocation(int moveX, int moveY){}  // for Basic object and Group

	// Basic object
	public Port getPort(int portIndex){
		return null;
	}

	// Group
	//public void resetSelectedShape() {}  //已被選中之obj釋放掉
	public Shape getSelectedShape() {
		return null;
	}  //用於了解哪個Group之成員obj被選中
	
}
