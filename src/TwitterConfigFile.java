import java.io.Serializable;

import javax.swing.JOptionPane;

public class TwitterConfigFile implements Serializable {

	public String consumerKey, consumerSecret, accessToken, tokenSecret, hashtag, interval;
	

	public TwitterConfigFile() {
		// TODO Auto-generated constructor stub
		consumerKey="";
		consumerSecret="";
		accessToken="";
		tokenSecret="";
		interval="1";
	}
}
