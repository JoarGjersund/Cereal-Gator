import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

public class fileHandler {

	public void export(String data){
		save(data);
		System.out.print(data);
	}
	
	public String getFile() {
		JFileChooser jfc = new JFileChooser();
		jfc.setFileSelectionMode(JFileChooser.FILES_ONLY);
		jfc.showOpenDialog(null);
		System.out.println(jfc.getSelectedFile());
		String content = "";
		try {

			DataInputStream dis = new DataInputStream(new FileInputStream(jfc.getSelectedFile()));

			byte[] datainBytes = new byte[dis.available()];
			dis.readFully(datainBytes);
			dis.close();

			content = new String(datainBytes, 0, datainBytes.length);

		} catch (Exception ex) {
			ex.printStackTrace();
			System.out.println("feil");
		}


		return content;
	}

	public void save(String data) {

		JFileChooser jfc = new JFileChooser();
		jfc.setFileSelectionMode(JFileChooser.FILES_ONLY);
		jfc.showSaveDialog(null);
		
		
		BufferedWriter writer = null;
		try {
			while (new File(jfc.getSelectedFile().getAbsolutePath()).exists()) {
				int confirm = JOptionPane.showConfirmDialog(null, "File with same name exists, overwrite?");
				if (confirm==JOptionPane.OK_OPTION){

				break;
				}
			}
				writer = new BufferedWriter(new FileWriter(jfc.getSelectedFile().getAbsolutePath()));
				writer.write(data);
				File file = new File(jfc.getSelectedFile().getAbsolutePath());
				file.createNewFile();
				


		} catch (IOException e) {
		} finally {
			try {
				if (writer != null)
					writer.close();
			} catch (IOException e) {
			}
		}
	}
}
