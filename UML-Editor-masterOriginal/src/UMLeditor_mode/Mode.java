package UMLeditor_mode;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import UMLeditor.Canvas;

//Mode其實是一個實做出MouseListener, MouseMotionListener的Class
public class Mode implements MouseListener, MouseMotionListener {
	protected Canvas canvas = Canvas.getInstance();   // Canvas is singleton 

	//將以下每個function都實做過 即便是空func 這樣才可以用!!!
	public void mouseClicked(MouseEvent e) {
	}
	public void mouseEntered(MouseEvent e) {
	}
	public void mouseExited(MouseEvent e) {
	}
	public void mousePressed(MouseEvent e) {
	}
	public void mouseReleased(MouseEvent e) {
	}



	public void mouseDragged(MouseEvent e) {
	}
	public void mouseMoved(MouseEvent e) {
	}
}
