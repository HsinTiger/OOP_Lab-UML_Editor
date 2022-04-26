package UMLeditor_mode;

import java.awt.event.MouseEvent;
import UMLeditor_shape.BasicObj;

public class createObjMode extends Mode{
	private String objType = null;
	private ShapeFactoryInterface factory = new ShapeFactory();



	public createObjMode(String objType) {
		this.objType = objType;
	}


	@Override
	public void mousePressed(MouseEvent e) { //在畫布上建立基本物件 可能是class or use_case
		BasicObj basicObj = factory.createObj(objType, e.getPoint());
		canvas.addShape(basicObj);		//建立完畫上去
		canvas.repaint();   //會先呼叫update再來是paint
	}

}
