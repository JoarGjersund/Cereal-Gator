import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.swing.BoundedRangeModel;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.text.DefaultCaret;

public class window extends JFrame {
	serialComm sC;
	fileHandler fH;
	TwitterMotor tm;
	window This;
	Thread t1;
	Timer t;
	String allData = "";
	canvas c;
	JCheckBox autoScroll;
	JTextArea textArea;
	JTextField textField;
	JButton sendButton, sendFile;
	JScrollPane scrollPane;
	JPanel listPane, miniPane, filePane;
	BoundedRangeModel model;
	DefaultCaret caret;
	menu menu;
	Controller controller;
	boolean dispTerm, init, tm_init = false;
	boolean autoScrollBolean, twitterGo = true;
	int baudInt = 9600;

	window() {
		This = this;
		this.setTitle("Cereal Gator Alpha| Serial Communication on steroids!");
		this.setName("Cereal Gator | Alpha");
		this.setLayout(null);
		listPane = new JPanel();
		miniPane = new JPanel();
		miniPane.setLayout(new FlowLayout());
		filePane = new JPanel();
		filePane.setLayout(new FlowLayout());
		listPane.setLayout(new BoxLayout(listPane, BoxLayout.PAGE_AXIS));
		c = new canvas();
		controller = new Controller(this);
		fH = new fileHandler();
		listPane.add(c);
		c.repaint();
		menu = new menu(this);

		this.setSize(600, 600);
		listPane.setBounds(0, 0, this.getWidth() - 20, this.getHeight() - 100);
		this.add(listPane);

		this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				if (init) {
					sC.close();
				}
				dispose();
				System.exit(0);
			}
		});

		this.setVisible(true);
	}


	public void displayTerminal(boolean x) {

		if (x && x != dispTerm) {
			dispTerm = x;
			textField = new JTextField();
			textField.setPreferredSize(new Dimension(300, 25));
			textField.setText("<command>");
			textField.addMouseListener(new MouseListener() {

				@Override
				public void mouseClicked(MouseEvent arg0) {
					// TODO Auto-generated method stub
					textField.selectAll();
				}

				@Override
				public void mouseEntered(MouseEvent arg0) {
					// TODO Auto-generated method stub

				}

				@Override
				public void mouseExited(MouseEvent arg0) {
					// TODO Auto-generated method stub

				}

				@Override
				public void mousePressed(MouseEvent arg0) {
					// TODO Auto-generated method stub

				}

				@Override
				public void mouseReleased(MouseEvent arg0) {
					// TODO Auto-generated method stub

				}

			});

			textArea = new JTextArea();
			textArea.setDoubleBuffered(true);
			// textArea.setEditable(false);
			textArea.setWrapStyleWord(true);
			textArea.setLineWrap(true);

			caret = (DefaultCaret) textArea.getCaret();

			scrollPane = new JScrollPane(textArea);
			scrollPane.setPreferredSize(new Dimension(200, this.getHeight() - 100));

			sendButton = new JButton("Send to port");
			sendButton.setActionCommand("Send to port");
			sendButton.addActionListener(controller);

			sendFile = new JButton("Send File to port");
			sendFile.setActionCommand("File to port");
			sendFile.addActionListener(controller);

			autoScroll = new JCheckBox("Auto scroll");
			autoScroll.setSelected(true);
			autoScroll.setActionCommand("autoScroll");
			autoScroll.addActionListener(controller);

			miniPane.add(textField);
			miniPane.add(sendButton);
			miniPane.add(autoScroll);

			filePane.add(sendFile);

			listPane.add(scrollPane);
			listPane.add(miniPane);
			listPane.add(filePane);

			JScrollBar scrollBar = scrollPane.getVerticalScrollBar();
			model = scrollBar.getModel();
			scrollBar.addAdjustmentListener(new AdjustmentListener() {

				public void adjustmentValueChanged(AdjustmentEvent e) {

					if (autoScrollBolean) {
						caret.setDot(textArea.getText().length());
						caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
					} else {

						if (model.getValue() == model.getMaximum() - model.getExtent()) {
							caret.setDot(textArea.getText().length());
							caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
						} else {
							caret.setUpdatePolicy(DefaultCaret.NEVER_UPDATE);
						}

					}
				}
			});

		} else if (!x && x != dispTerm) {
			listPane.remove(scrollPane);
			listPane.remove(miniPane);
			listPane.remove(filePane);
			dispTerm = x;
		}

		this.setVisible(true);
		this.repaint();
	}

	public void terminal(String port, int baudRate) {

		if (!init) {
			init = true;
			sC = new serialComm(port, baudRate, this);
			sC.listen();

			this.displayTerminal(true);
		} else {
			System.out.print("already open");
		}
	}

	public void close() {
		sC.close();
		init = false;
		this.displayTerminal(false);
	}

	public void changeBaud() {
		baudInt = 0;
		while (baudInt <= 0) {
			String baudStr = JOptionPane.showInputDialog("Enter baud rate", sC.baudRate);

			baudInt = Integer.parseInt(baudStr);

		}

		init = false;
		sC.close();
		terminal(sC.comPort, baudInt);

	}

	public void terminalEcho(String data) {

		textArea.append(data);
		
		if (sC.getTweets) {
			
			allData = new StringBuilder().append(allData).append(data).toString();
			if (allData.contains("\n") ){
			if (allData.contains("#"+tm.tcf.hashtag)) {
				if (!allData.equals("")){
					tm.TweetIt(allData);
				}
				
			}
			allData="";
			
			}
		}
		

	}

	public void write() {
		if (sC != null) {
			sC.write(textField.getText());
		}
	}

	public void write(String text) {
		if (sC != null) {
			sC.write(text);
			sC.getTweets=true;
		}
	}

	public void listenForTweets() {
		if (!tm_init) {
			tm_init = true;
			tm = new TwitterMotor();
			twitterGo = true;
			

		}
		startTweetTimer();

	}



	public TwitterMotor getTwitterMotor() {
		if (!tm_init) {
			tm_init = true;
			tm = new TwitterMotor();
		}
		return tm;
	}

	public void startTweetTimer() {

		new Timer().schedule(new TimerTask() {
			public void run() {

				if (twitterGo) {
					menu.m4.setForeground(Color.RED);
					tm.getTweet(This);
					menu.m4.setForeground(Color.BLACK);

				}

			}
		}, 1, 10000);
	}

	public serialComm getSC() {

		return sC;
	}

	public void writeFile() {
		String dataF = fH.getFile();
		System.out.print(dataF);
		sC.write(dataF);
	}

	public String getTerminalData() {
		return textArea.getText();
	}

}
