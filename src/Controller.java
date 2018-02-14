import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JMenuItem;

public class Controller implements ActionListener {
window w;
	Controller(window x){
		w = x;
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		switch (e.getActionCommand()){
		
		case "Send to port": w.write(); break;
		case "File to port": w.writeFile(); break;
		case "autoScroll": JCheckBox x = (JCheckBox) e.getSource(); w.autoScrollBolean=x.isSelected(); w.displayTerminal(true); break;
		default:  System.out.println("something wrogn"); break;
		}
	}

}
