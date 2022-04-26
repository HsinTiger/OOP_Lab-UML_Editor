package UMLeditor;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JTextField;

@SuppressWarnings("serial")
public class MenuBar extends JMenuBar {
	private Canvas canvas;
	private JMenu File_Menu;
	private JMenu Edit_Menu;

	public MenuBar() {
		canvas = Canvas.getInstance();   // Canvas is singleton

		/* --- File menu --- */
		File_Menu = new JMenu("File");
		add(File_Menu);
		/* --- Edit menu --- */
		Edit_Menu = new JMenu("Edit");
		add(Edit_Menu);

		JMenuItem MIofEdit_Menu;
		MIofEdit_Menu = new JMenuItem("Change object name");
		Edit_Menu.add(MIofEdit_Menu);
		MIofEdit_Menu.addActionListener(new ChangeNameListener());

		MIofEdit_Menu = new JMenuItem("Group");
		Edit_Menu.add(MIofEdit_Menu);
		MIofEdit_Menu.addActionListener(new GroupObjectListener());

		MIofEdit_Menu = new JMenuItem("Ungroup");
		Edit_Menu.add(MIofEdit_Menu);
		MIofEdit_Menu.addActionListener(new UngroupObjectListener());
	}

	//Menubar內的觸發功能按鍵
	class ChangeNameListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			changeNameForm();
		}
	}
	class GroupObjectListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			//canvas.addGroup();
			canvas.GroupShape();
		}
	}
	class UngroupObjectListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			canvas.removeGroup();
		}
	}




	//only call by ChangeNameListener of private,改名表單
	private void changeNameForm() {
		//frame框架
		JFrame inputTextFrame = new JFrame("Change Object Name");
		inputTextFrame.setSize(400, 250);
		inputTextFrame.getContentPane().setLayout(new GridLayout(0, 1));

		//text field
		JPanel TxtPanel = new JPanel();
		TxtPanel.setLayout(new BoxLayout(TxtPanel, BoxLayout.X_AXIS));
		JTextField Text =  new JTextField("New Object Name");
		TxtPanel.add(Text);
		inputTextFrame.getContentPane().add(TxtPanel);

		//check button field
		JPanel BtnPanel = new JPanel();
		BtnPanel.setLayout(new BoxLayout(BtnPanel, BoxLayout.Y_AXIS));	//按鈕垂直排列
		JButton confirm = new JButton("OK");
		confirm.setAlignmentX(CENTER_ALIGNMENT);
		BtnPanel.add(confirm);
		JButton cancel = new JButton("Cancel");
		cancel.setAlignmentX(CENTER_ALIGNMENT);
		BtnPanel.add(cancel);
		inputTextFrame.getContentPane().add(BtnPanel);


		inputTextFrame.setLocationRelativeTo(null);
		inputTextFrame.setVisible(true);
		
		//button事件竊聽器
		confirm.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				canvas.changeObjName(Text.getText());  //將從text field得到的text抓過來
				inputTextFrame.dispose();
			}
		});
		cancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				inputTextFrame.dispose();
			}
		});
	}
}
