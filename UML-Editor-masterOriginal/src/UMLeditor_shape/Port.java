package UMLeditor_shape;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("serial")
public class Port extends Rectangle{		//Port surely is a Square
	//every port can have lots of lines that connect it
	private List<Line> lines = new ArrayList<Line>(); 

	//Give me the port_center and offset
	public void setPort(int center_x, int center_y, int offset) {
		int x = center_x - offset;
		int y = center_y - offset;
		int w = offset * 2;
		int h = offset * 2;
		setBounds(x, y, w, h);
	}

	//Port lines connect
	public void addLine(Line line) {
		lines.add(line);
	}
	public void removeLine(Line line) {
		lines.remove(line);
	}

	//Lines can't change their position,  lines必須跟著自己對應的port位移一起跑
	public void resetLines() {
		for(int i = 0; i < lines.size(); i++){
			Line line = lines.get(i);
			line.resetLocation();
		}
	}
}
