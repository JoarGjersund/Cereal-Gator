import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import jssc.SerialPortList;

public class menu implements ActionListener {
TwitterMotor tm;
JFrame s;
window w;
fileHandler fH;
JMenuBar mb;
public JMenu m1,m2,m3, m4;
String selectedPort = "";
serialComm sc;
	menu(JFrame q) {
		s = q;
		w = (window) q;
		fH = new fileHandler();
		mb = new JMenuBar();

		m1 = new JMenu("Port");
		m2 = new JMenu("Port Actions");
		
		m3 = new JMenu("Data");
		
		m4 = new JMenu("Twitter");
		m4.setToolTipText("this will flash red during query");

		makeMenu();
	}
	
	public void makeMenu(){
		m1.removeAll();
		m2.removeAll();
		m3.removeAll();
		m4.removeAll();
	

		String[] portNames = SerialPortList.getPortNames();
		for (int i = 0; i < portNames.length; i++) {
			System.out.println(portNames[i]);

			JMenuItem m11 = new JMenuItem(portNames[i]);
			if (selectedPort.equals(portNames[i])){
				m11.setArmed(true);
				m11.setForeground(Color.GRAY);
			}
			m1.add(m11);
			m11.addActionListener(this);
		}


		
		JMenuItem m10 = new JMenuItem("Refresh...");

		m1.add(m10);
		m10.addActionListener(this);
		
		JMenuItem m12 = new JMenuItem("close port");
		JMenuItem m13 = new JMenuItem("Terminal");
		JMenuItem m14 = new JMenuItem("Baud rate");
		m14.setToolTipText(""+w.baudInt);
		
		m2.add(m12);
		m2.add(m13);
		m2.add(m14);
		
		m12.addActionListener(this);
		m13.addActionListener(this);
		m14.addActionListener(this);
		
		
		JMenuItem m30 = new JMenuItem("save data to file...");
		m30.addActionListener(this);
		m3.add(m30);
		
		
		
		JMenuItem m40 = new JMenuItem("open config file");
		m40.addActionListener(this);
		m4.add(m40);
		
		JMenuItem m41 = new JMenuItem("generate config file");
		m41.addActionListener(this);
		m4.add(m41);
		
		JMenuItem m42 = new JMenuItem("Connect");
		m42.addActionListener(this);
		m4.add(m42);
		
		JMenuItem m43 = new JMenuItem("Disconnect");
		m43.addActionListener(this);
		m4.add(m43);
		

		
		mb.add(m1);
		mb.add(m2);
		mb.add(m3);
		mb.add(m4);
		s.setJMenuBar(mb);
		

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub

		JMenuItem i = (JMenuItem) e.getSource();


		switch (i.getActionCommand()){
			
		case "Refresh...": this.makeMenu(); break;
		case "close port": w.close(); break;
		case "Terminal": w.terminal(selectedPort, w.baudInt); break;
		case "save data to file...": fH.export(w.getTerminalData()); break;
		case "Baud rate": w.changeBaud(); this.makeMenu(); break;
		case "open config file": w.getTwitterMotor().loadConfigFile(); break;
		case "generate config file": w.getTwitterMotor().generateConfigFile(); break;
		case "Connect": w.listenForTweets(); w.twitterGo=true; break;
		case "Disconnect": w.twitterGo=false; break;
		default: selectedPort = i.getActionCommand(); this.makeMenu(); break;
		}

			


	}
}
