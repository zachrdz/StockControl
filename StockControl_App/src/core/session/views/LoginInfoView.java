/**
 * 
 */
package core.session.views;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JTextPane;

/**
 * @author Zach
 *
 */
public class LoginInfoView extends JFrame{
	private static final long serialVersionUID = 1L;
	private ArrayList<String> usernames = new ArrayList<String>();
	private ArrayList<String> passwords = new ArrayList<String>();
	
	public LoginInfoView(){
		run();
	}
	
	public void run(){
		usernames.add("invManager@gmail.com");
		passwords.add("Password111");
		
		usernames.add("prodManager@gmail.com");
		passwords.add("Password222");
		
		usernames.add("admin@gmail.com");
		passwords.add("Password333");
		
		setTitle("Login Info (Copy & Paste)");
		setSize(400,130);
		GridLayout gLayout = new GridLayout(3,2);
		gLayout.setHgap(20);
		gLayout.setVgap(5);
		setLayout(gLayout);
		getContentPane().setBackground(Color.WHITE);
		
		for(int i = 0; i < usernames.size(); i++){
			JTextPane userTxt = new JTextPane();
			JTextPane passTxt = new JTextPane();
			
			userTxt.setFont(new Font("Arial",Font.PLAIN,15));
			passTxt.setFont(new Font("Arial",Font.PLAIN,15));
			
			userTxt.setContentType("text/html"); // let the text pane know this is what you want
			userTxt.setEditable(false); // as before
			userTxt.setBackground(null); // this is the same as a JLabel
			userTxt.setBorder(null); // remove the border
			
			passTxt.setContentType("text/html"); // let the text pane know this is what you want
			passTxt.setEditable(false); // as before
			passTxt.setBackground(null); // this is the same as a JLabel
			passTxt.setBorder(null); // remove the border
			
			userTxt.setText("<html><center>"+usernames.get(i)+"</center></html>");
			passTxt.setText("<html><center>"+passwords.get(i)+"</center></html>");
			
			add(userTxt);
			add(passTxt);
		}
		
		//pack();
		setVisible(true);
	}
}
