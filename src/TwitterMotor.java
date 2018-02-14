import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import javax.swing.DefaultListModel;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import twitter4j.GeoLocation;
import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Status;
import twitter4j.StatusUpdate;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

public class TwitterMotor {

	String hashtag = "javajoar";
	window window;
	TwitterConfigFile tcf;
	long sinceID = 0;

	TwitterMotor() {
		tcf = new TwitterConfigFile();
	}

	public void generateConfigFile() {

		JOptionPane.showMessageDialog(null, "Please have your twitter access info ready. Visit https://dev.twitter.com/oauth/overview for more info");
		tcf = new TwitterConfigFile();
		tcf.consumerKey = JOptionPane.showInputDialog("ConsumerKey:");
		tcf.consumerSecret = JOptionPane.showInputDialog("ConsumerSecret:");
		tcf.accessToken = JOptionPane.showInputDialog("AccessToken");
		tcf.tokenSecret = JOptionPane.showInputDialog("TokenSecret");
		tcf.hashtag = JOptionPane.showInputDialog("Hashtag (used for filtering out what tweets to send and receive):");
		tcf.interval = JOptionPane
				.showInputDialog("How often do you want to check for new tweets? (every ____ minute)");
		
		try {
		      Integer.parseInt(tcf.interval);
		} catch (NumberFormatException e) {
			
			tcf.interval = JOptionPane.showInputDialog("You did not answare the last question correctly. Write number of minutes as an integer");
		}
		try {
		      Integer.parseInt(tcf.interval);
		} catch (NumberFormatException e) {
			tcf.interval="1";
		}
		
        JOptionPane.showMessageDialog(null, 
    		 	"ConsumerKey: "+tcf.consumerKey+"\n"
    		 + "ConsumerSecret: "+tcf.consumerSecret+"\n"
    		 + "AccessToken: "+tcf.accessToken+"\n"
    		 + "AccessTokenSecret: "+tcf.tokenSecret+"\n"
    		 + "last tweet with hashtag: "+tcf.hashtag+", will be sent to serial port every "
    		 + tcf.interval+" minute."
    		 );
		
		
		JFileChooser jfc = new JFileChooser();
		jfc.setFileSelectionMode(JFileChooser.FILES_ONLY);
		jfc.setSelectedFile(new File("TwitterConfigFile.ser"));
		jfc.showSaveDialog(null);

		while (new File(jfc.getSelectedFile().getAbsolutePath()).exists()) {
			int confirm = JOptionPane.showConfirmDialog(null, "File with same name exists, overwrite?");
			if (confirm == JOptionPane.OK_OPTION) {

				break;
			}else{
				jfc.showSaveDialog(null);
			}
		}

		try {
			FileOutputStream fileOut = new FileOutputStream(jfc.getSelectedFile().getAbsolutePath());
			ObjectOutputStream out = new ObjectOutputStream(fileOut);
			out.writeObject(tcf);
			out.close();
			fileOut.close();
			System.out.printf("Serialized data is saved");
		} catch (IOException i) {
			i.printStackTrace();
		}

	}
	
	public void loadConfigFile() {
		
		JFileChooser jfc = new JFileChooser();
		jfc.setFileSelectionMode(JFileChooser.FILES_ONLY);
		jfc.setSelectedFile(new File("TwitterConfigFile.ser"));
		jfc.showOpenDialog(null);
		System.out.println(jfc.getSelectedFile());
		
	      try
	      {
	         FileInputStream fileIn = new FileInputStream(jfc.getSelectedFile());
	         ObjectInputStream in = new ObjectInputStream(fileIn);
	        tcf = (TwitterConfigFile) in.readObject();

	         in.close();
	         fileIn.close();
	         
	         JOptionPane.showMessageDialog(null, 
	        		 	"ConsumerKey: "+tcf.consumerKey+"\n"
	        		 + "ConsumerSecret: "+tcf.consumerSecret+"\n"
	        		 + "AccessToken: "+tcf.accessToken+"\n"
	        		 + "AccessTokenSecret: "+tcf.tokenSecret+"\n"
	        		 + "last tweet with hashtag: "+tcf.hashtag+", will be sent to serial port every "
	        		 + tcf.interval+" minute."
	        		 );
	      }catch(IOException i)
	      {
	    	  JOptionPane.showMessageDialog(null, "File does not meet the criteria, select another file.");
	    	  System.out.println("wrong file");
	         i.printStackTrace();
	         return;
	      }catch(ClassNotFoundException c)
	      {
	    	  JOptionPane.showMessageDialog(null, "file not found");
	         System.out.println("class not found");
	         c.printStackTrace();
	         return;
	      }
		
		
	}

	public ConfigurationBuilder connectTwitter() {

		
		if (tcf==null || tcf.accessToken.equals("") || tcf.consumerKey.equals("") || tcf.tokenSecret.equals("") || tcf.consumerSecret.equals("")){
			System.out.print(tcf.accessToken);
			int val = JOptionPane.showConfirmDialog(null, "You need access details to use this function. Do you want to generate a new config file?");
			if (val==JOptionPane.YES_OPTION){
				generateConfigFile();
			}else{
				window.twitterGo=false;
			}
		}
		
		ConfigurationBuilder cb = new ConfigurationBuilder();

		cb.setDebugEnabled(true);
		cb.setOAuthConsumerKey(tcf.consumerKey);
		cb.setOAuthConsumerSecret(tcf.consumerSecret); 
		cb.setOAuthAccessToken(tcf.accessToken); 
		cb.setOAuthAccessTokenSecret(tcf.tokenSecret); 
		return cb;
	}

	public void getTweet(window w) {
		window = w;
		String lastTweet = null;
		ConfigurationBuilder cb = connectTwitter();
		TwitterFactory tf = new TwitterFactory(cb.build());
		Twitter twitter = tf.getInstance();

		Query query = new Query(hashtag);
		query.setSinceId(sinceID);

		QueryResult result;

		try {
			result = twitter.search(query);
			for (Status status : result.getTweets()) {
				System.out.print(sinceID);
				lastTweet = status.getText();
				if (status.getId() > sinceID) {
					sinceID = status.getId();
				}
			}
			if (lastTweet != null) {
				w.write(lastTweet);
				lastTweet = null;
			}

		} catch (TwitterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void TweetIt(String text) {

		ConfigurationBuilder cb = connectTwitter();
		TwitterFactory tf = new TwitterFactory(cb.build());
		Twitter twitter = tf.getInstance();
		Status status;
		try {
			StatusUpdate updatedata = new StatusUpdate(text);
			GeoLocation location = new GeoLocation(58.3405000, 8.5934300);
			updatedata.setLocation(location);
			status = twitter.updateStatus(updatedata);
			int statusId;
			statusId = (int) status.getId();
			
			System.out.println(text+" has been tweeted");

		} catch (TwitterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
