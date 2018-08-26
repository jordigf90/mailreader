package mailreader;

import javax.mail.*;
import javax.mail.Flags.Flag;
import javax.mail.search.FlagTerm;
import java.util.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class mailreader {

	private static final String String = null;

	public static void main(String[] args) throws Exception {

		    String msg_body = null;
		    String[] body_array = null;
		    
		    String sqlvalues = null;
		
		 	Session session = Session.getDefaultInstance(new Properties( ));
		    Store store = session.getStore("imaps");
		    store.connect("imap.googlemail.com", 993, "jordi.gf90@gmail.com", "barcelona541542BA");
		    Folder inbox = store.getFolder( "INBOX" );
		    inbox.open( Folder.READ_WRITE );

		    // Fetch unseen messages from inbox folder
		    Message[] messages = inbox.search(
		        new FlagTerm(new Flags(Flags.Flag.SEEN), false));
		    	

		    // Sort messages from recent to oldest
		    Arrays.sort( messages, ( m1, m2 ) -> {
		      try {
		        return m2.getSentDate().compareTo( m1.getSentDate() );
		      } catch ( MessagingException e ) {
		    	 
		        throw new RuntimeException( e );
		        
		      }
		    } );

		    for ( Message message : messages ) {
		    	
		    	String from = message.getFrom()[0].toString();
		        String sentdate = message.getSentDate().toString();
		        String subject = message.getSubject();
		        
		        if (from.substring(26).toString().equals("jordi.gf90@gmail.com>")){
			      if (message.getContentType().contains("TEXT/PLAIN")){
			    	  msg_body = message.getContent().toString();
			    	  message.setFlag(Flag.SEEN, true);
			      }else{
			    	  System.out.println("Error: No Plain Text");
			    	  break;
			      }
			      
			    	
		        }


		    }
		    
		    if (msg_body != null){
			    body_array = msg_body.split("\\[tag\\]");
			    for (int i = 0; i < body_array.length; i++) {
			    	sqlvalues = sqlvalues + "'" +body_array[i]+"',";
			    }
			    
			    String query = sqlvalues.substring(7);
			    query = query.substring(0, query.length() - 6);
				String sql_command = "INSERT INTO envios_table ("+query+");";
				
				System.out.println(sql_command);
			    
		    }else{
		    	System.out.println("No New Messages");
		    }
		    

		   

	}
}
