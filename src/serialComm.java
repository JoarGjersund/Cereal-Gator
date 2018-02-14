
import java.awt.Graphics;

import javax.swing.JOptionPane;

import jssc.SerialPort;
import jssc.SerialPortEvent;
import jssc.SerialPortEventListener;
import jssc.SerialPortException;

public class serialComm implements SerialPortEventListener {
	SerialPort serialPort;
	String command;
	window parent;
	String comPort;
	public String data;
	public int baudRate = 9600;
	boolean portInit, getTweets = false;

	public serialComm(String port, int bR, window p) {
		baudRate = bR;

		parent = p;
		comPort = port;
		try {

			this.close();

			serialPort = new SerialPort(port);

			serialPort.openPort();// Open serial port

			serialPort.setParams(baudRate, 8, 1, 0);// Set params.

			serialPort.addEventListener(this);
			portInit = true;

		} catch (SerialPortException ex) {

			if (port.equals("")) {
				JOptionPane.showMessageDialog(parent, "No port selected!");
			} else {
				JOptionPane.showMessageDialog(parent,
						"Oh no! Port (" + port + ") seems busy! If problem persists try restarting your system");
			}
			System.out.println(ex);

		}
	}

	public void listen() {

	}

	public void write(String text) {
		try {
			serialPort.writeString(text);
			System.out.println(text);
		} catch (SerialPortException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("seial monitor error");
		}
	}

	public void close() {

		if (portInit) {
			if (serialPort.isOpened()) {
				System.out.println("port closed");
				try {
					serialPort.removeEventListener();
					serialPort.closePort();// Close serial port
				} catch (SerialPortException e) {
					System.out.println("feil her..");
					System.out.println(e);
				}
			}

		} else {
			System.out.println("port was not open");
		}
	}

	@Override
	public void serialEvent(SerialPortEvent arg0) {
		// TODO Auto-generated method stub
		if (arg0.isRXCHAR() && arg0.getEventValue() > 0) {
			try {
				// data +=serialPort.readString();
				String data = serialPort.readString();

				parent.terminalEcho(data);


			} catch (SerialPortException e) {
				// TODO Auto-generated catch block
				System.out.print("feil");
				e.printStackTrace();
			}
		}

	}
}
