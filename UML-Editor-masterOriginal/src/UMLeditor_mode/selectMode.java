package UMLeditor_mode;

import java.awt.Point;
import java.awt.event.MouseEvent;
import java.util.List;

import UMLeditor_shape.Line;
import UMLeditor_shape.Port;
import UMLeditor_shape.Shape;

public class selectMode extends Mode {
	private List<Shape> shapes;
	private Point startP = null;
	private String judgeInside = null;
	private Line selectedLine = null;
	private Port OriginalPort = null;
	
	public void mousePressed(MouseEvent e) {
		// reset，其他原本被選到的或方框選取都要恢復
		canvas.reset();

		startP = e.getPoint();  //會多重選取的話 方框的左上角起點？那起點應該為四個角都行


		shapes = canvas.getShapeList();
		System.out.println(startP);
		/* find which basic object, record its reference */
		//單擊選中一個basic_obj 一次只能選中一個,line,useCaseObj,ClassObj,已存在的group
		for (int i = shapes.size() - 1; i >= 0; i--) {
			Shape shape = shapes.get(i);
			judgeInside = shape.inside(e.getPoint());
			System.out.println(e.getPoint());
			if (judgeInside != null) {
				canvas.selectedObj = shape;
				break;
			}
		}
		//不論如何canvas都要repaint
		canvas.repaint();  //會call paint
	}

	public void mouseDragged(MouseEvent e) {
		int moveX = e.getX() - startP.x;  //動態移動座標距離
		int moveY = e.getY() - startP.y;
		/* object selected,若一開始mousePress有選到單個物件,則接下來的拖曳視同移動該物件 */
		if (canvas.selectedObj != null) {
			// move Line object
			if (judgeInside.equals("insideLine")) {
				/** 因為此行不合法，shape裡沒有line的方法 canvas.selectedObj.resetStartEnd(e.getPoint());**/
				selectedLine = (Line) canvas.selectedObj; //downCasting 交給reference of line指向他
				OriginalPort = selectedLine.getOriginalPortsOfLine(selectedLine.getselectedFlagOfLine()); //更動line前先保留他原本連哪個port
				selectedLine.resetStartEnd(e.getPoint());  //看要移動的是線段的起點還是尾巴，在上面inside函數已經判斷過
				//canvas.tempLine = selectedLine;
			}
			else {
				canvas.selectedObj.resetLocation(moveX, moveY);
			}

			////???????每次重新第二次press時不是都會初始化startP嗎？yes
			///所以下面兩行程式碼其實是要防止連續拖曳的情況（即中途沒有經過第二次press）
			///因為的確有可能重複觸發mouseDragged事件,不需經過多次mousePressed
			startP.x = e.getX();
			startP.y = e.getY();
		}
		/* group area selected,支援四個方向方框select */
		else {
			//
			if (e.getX() > startP.x && e.getY() > startP.y){
				canvas.SelectedArea.setBounds(startP.x, startP.y, Math.abs(moveX), Math.abs(moveY));
			}
			else if(e.getX() > startP.x && e.getY() < startP.y){
				canvas.SelectedArea.setBounds(startP.x, e.getY(), Math.abs(moveX), Math.abs(moveY));
			}
			else if (e.getX() < startP.x && e.getY() > startP.y) {
				canvas.SelectedArea.setBounds(e.getX(), startP.y, Math.abs(moveX), Math.abs(moveY));
			}
			else if (e.getX() < startP.x && e.getY() < startP.y) {
				canvas.SelectedArea.setBounds(e.getX(), e.getY(), Math.abs(moveX), Math.abs(moveY));
			}
			/**
        if (e.getX() > startP.x)
            canvas.SelectedArea.setBounds(startP.x, startP.y, Math.abs(moveX), Math.abs(moveY));
        else
            canvas.SelectedArea.setBounds(e.getX(), e.getY(), Math.abs(moveX), Math.abs(moveY));
             **/
		}
		canvas.repaint();
	}

	public void mouseReleased(MouseEvent e) {
		/* object select */
		if (canvas.selectedObj != null) {
			// move Line object
			if (judgeInside.equals("insideLine")) {  //顯然這邊要更嚴謹!!!!!!!!!!!!!!!!!!
				selectedLine = (Line) canvas.selectedObj;
				reconnectLine(e.getPoint());
				System.out.println("不一定合法");
			}
		}
		/* group area selected,設定SelectedArea大小,繪製部分交給canvas class來做 */
		else {
			canvas.SelectedArea.setSize(Math.abs(e.getX() - startP.x), Math.abs(e.getY() - startP.y));
		}
		canvas.repaint();
	}

	//連線重調到不同port
	private void reconnectLine(Point p) {
		boolean Connect_success = false;
		for (int i = 0; i < shapes.size(); i++) {
			Shape shape = shapes.get(i);
			int portIndex;
			String judgeInside = shape.inside(p);   //在此為區域變數 與成員變數不同
			if (judgeInside != null && judgeInside != "insideLine") {
				/* if shape inside the group */
				if (judgeInside.equals("insideGroup")) {
					shape = shape.getSelectedShape();
					portIndex = Integer.parseInt(shape.inside(p));
				}
				else {  //judgeInside == "insideBasicObj"
					portIndex = Integer.parseInt(judgeInside);
				}
				Connect_success = true;
				/**
				if (Connect_success = true) { //代表不合法連線,應該回到原本線段的樣子
					//reset 起終點port && reset 起終點座標of port
					selectedLine.resetPort(shape.getPort(portIndex), selectedLine);
					selectedLine.resetLocation();
					System.out.println("合法");
					break;
				}
				 **/
				//reset 起終點port && reset 起終點座標of port
				selectedLine.resetPort(shape.getPort(portIndex), selectedLine);
				selectedLine.resetLocation();
				System.out.println("合法");
				break;
			}
		}
		if(!Connect_success){  //Connect_success == false
			System.out.println("不合法");
			selectedLine.resetPort(OriginalPort, selectedLine);
			selectedLine.resetLocation();
			OriginalPort = null;
		}


	}

}
