import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class canvas extends JPanel {
	
	canvas(){
		this.setVisible(true);
	}
public void paint(Graphics g){

	g.setColor(Color.red);
//	g.fillRect(100, 100, 400, 400);
}

}

