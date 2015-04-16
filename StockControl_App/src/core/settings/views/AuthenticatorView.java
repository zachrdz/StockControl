/**
 * 
 */
package core.settings.views;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import core.mdi.MasterFrame;
import core.settings.controllers.AuthenticatorController;

/**
 * @author Zach
 *
 */
public class AuthenticatorView extends JInternalFrame{
	private static final long serialVersionUID = 1L;

	private JButton loginBtn;

	private JTextField username;
	private JPasswordField password;

	public AuthenticatorView(MasterFrame m){
		title = "Login";
		setSize(450, 200);
    	
		GridLayout gLayoutParent = new GridLayout(2,1);
    	GridLayout gLayoutChild1 = new GridLayout(2,2);
    	GridLayout gLayoutChild2 = new GridLayout(2,1);
    	this.setLayout(gLayoutParent);
    	
    	JPanel childPane1 = new JPanel();
        childPane1.setLayout(gLayoutChild1);
    	
    	gLayoutChild1.setHgap(15);
        JLabel usernameLabel =new JLabel("User Name: ");
        usernameLabel.setFont(new Font("Arial",Font.TRUETYPE_FONT,12));
        childPane1.add(usernameLabel);
        username = new JTextField("");
        childPane1.add(username);
        
        JLabel passwordLabel =new JLabel("Password: ");
        passwordLabel.setFont(new Font("Arial",Font.TRUETYPE_FONT,12));
        childPane1.add(passwordLabel);
        password = new JPasswordField("");
        childPane1.add(password);
        this.add(childPane1);
        
        JPanel childPane2 = new JPanel();
        childPane2.setLayout(gLayoutChild2);
        childPane2.add(new JLabel(""));
 
        loginBtn = new JButton("Login");        
        loginBtn.setFont(new Font("Arial",Font.TRUETYPE_FONT,12));
        childPane2.add(loginBtn); 
        
        this.add(childPane2);
        
        loginBtn.setActionCommand(AuthenticatorController.LOGIN_COMMAND);
        
        resizable = false;
        closable = false;
        maximizable = false;
        iconable = false;
        
        //pack();
        Dimension desktopSize = m.getDesktop().getSize();
		Dimension jInternalFrameSize = getSize();
		int dWidth = (desktopSize.width == 0) ? 1008 : desktopSize.width;
		int dHeight = (desktopSize.height == 0) ? 707 : desktopSize.height;
		setLocation((dWidth - jInternalFrameSize.width)/2, (dHeight - jInternalFrameSize.height)/2); 
		
        m.getDesktop().add(this);
        m.getDesktop().getDesktopManager().activateFrame(this);
        setVisible(true);
    }
	
	public JInternalFrame getFrame(){
		return this;
	}
	
	public void addButtonListener(ActionListener listener) {
		loginBtn.addActionListener(listener);
	}

	public JTextField getUsername() {
		return username;
	}

	public void setUsername(JTextField username) {
		this.username = username;
	}

	public JPasswordField getPassword() {
		return password;
	}

	public void setPassword(JPasswordField password) {
		this.password = password;
	}
}
