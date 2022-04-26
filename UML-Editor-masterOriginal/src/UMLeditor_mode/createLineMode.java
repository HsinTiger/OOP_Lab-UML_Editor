package UMLeditor_mode;

import java.awt.Point;
import java.awt.event.MouseEvent;
import java.util.List;

import UMLeditor_shape.Line;
import UMLeditor_shape.Shape;

public class createLineMode extends Mode {
	private String lineType = null; //決定是哪一種line
	private ShapeFactoryInterface factory = new ShapeFactory();  //等等用以交給factory建立物件

	private Point startP = null;	//先確定起點有效再接著走

	private List<Shape> shapes;		//private Point findConnectedObj(Point p, String target)會用到

	private int portIndex_1 = -1, portIndex_2 = -1;//是哪兩個port被選出來
	private Shape shape_1 = null, shape_2 = null;//用以檢查線是被選要連在哪兩個 shape上

	public createLineMode(String lineType) {
		this.lineType = lineType;
	}

	//這段霧煞煞 判斷被選中的最近shape,他要做為這條線的起點或終點shape
	private Point findConnectedObj(Point p, String target) {
		for (int i = 0; i < shapes.size(); i++) {
			Shape shape = shapes.get(i);
			/* 逐一檢查,跑for loop, check if or not mouse pressed inside the basic object */
			int portIndex;
			String judgeInside = shape.inside(p); //多型inside(p)


			if (judgeInside != null && judgeInside != "insideLine") {  //鼠標的座標位於當前shape範圍內，且該shape非line
				/* if shape inside the group */
				if(judgeInside == "insideGroup"){
					shape = shape.getSelectedShape();
					portIndex = Integer.parseInt(shape.inside(p));  //轉型成int
					//System.out.println(judgeInside);
				}
				else
					portIndex = Integer.parseInt(judgeInside);  //轉型成int

				/* if inside the basic object, get the location of relative port */
				switch (target) {
					case "first":
						shape_1 = shape;
						portIndex_1 = portIndex;
						break;
					case "second":
						shape_2 = shape;
						portIndex_2 = portIndex;
						break;
				}
				//把該port之絕對座標記錄下來回傳，即該port之中心
				Point portLocation = new Point();
				portLocation.setLocation(shape.getPort(portIndex).getCenterX(), shape.getPort(portIndex).getCenterY());
				return portLocation;
			}

		}
		return null;
	}
	//在此mode時,對畫布按下滑鼠建立物件-線條起點
	public void mousePressed(MouseEvent e) {
		shapes = canvas.getShapeList();
		/* find which basic object, record its reference and port number */
		startP = findConnectedObj(e.getPoint(), "first");
	}
	//在此mode時,按完滑鼠後 對畫布拖曳滑鼠建立物件-線條終點 mouseDrag會隨著持續拖曳不斷被觸發
	public void mouseDragged(MouseEvent e) {
		/* show dragged line */
		if (startP != null) {  //起點有效,一開始有選中任何物體(shape)
			Line line = factory.createLine(lineType, startP, e.getPoint()); //根據持續拖曳的終點 搭上起點繪製線條
			canvas.tempLine = line; //告訴canvas有templine的存在，須要畫出來
			canvas.repaint(); //呼叫ｐａｉｎｔ
		}
	}
	//在此mode時,放開滑鼠-線條終點 mouseDrag會隨著持續拖曳不斷被觸發
	public void mouseReleased(MouseEvent e) {
		Point endP = null;
		if (startP != null) {	//起點有效
			/* find which basic object, record its reference and port number */
			endP = findConnectedObj(e.getPoint(), "second");

			// if end of line inside the basic object ,確定建立此條靜態線段
			if (endP != null) {
				Line line = factory.createLine(lineType, startP, endP);
				canvas.addShape(line);  //正式加入canvas之shape list成員名單內 不再是templine

				//port&line之間的相對應新進成員設定
				/* add relative ports to line */
				line.setPorts(shape_1.getPort(portIndex_1), shape_2.getPort(portIndex_2));
				/* add line to relative port of two basic object */
				shape_1.getPort(portIndex_1).addLine(line);
				shape_2.getPort(portIndex_2).addLine(line);
			}
			// reset ,之所以沒有找到合法的port就會返回線段原本的樣子是因為這裡！！！！！
			canvas.tempLine = null;
			canvas.repaint();  //必定要記得每次更新都要重畫paint

			startP = null; //給下一條線重新偵測起點
		}
	}

}
