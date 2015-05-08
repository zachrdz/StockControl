/**
 * 
 */
package core.session.views;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import core.mdi.models.MasterFrame;
import core.session.controllers.AuthenticatorController;

/**
 * @author Zach
 *
 */
public class AuthenticatorView extends JInternalFrame{
	private static final long serialVersionUID = 1L;

	private JButton loginBtn, cancelBtn;

	private JTextField username;
	private JPasswordField password;

	public AuthenticatorView(MasterFrame m){
		title = "Login";
		setSize(450, 200);
    	
		GridLayout gLayoutParent = new GridLayout(4,2);
		gLayoutParent.setHgap(15);
		this.setLayout(gLayoutParent);

    	
        JLabel usernameLabel =new JLabel("User Name: ");
        usernameLabel.setFont(new Font("Arial",Font.TRUETYPE_FONT,12));
        add(usernameLabel);
        username = new JTextField("");
        add(username);
        
        JLabel passwordLabel =new JLabel("Password: ");
        passwordLabel.setFont(new Font("Arial",Font.TRUETYPE_FONT,12));
        add(passwordLabel);
        password = new JPasswordField("");
        add(password);
        
        add(new JLabel(""));
        add(new JLabel(""));
        
        cancelBtn = new JButton("Cancel");        
        cancelBtn.setFont(new Font("Arial",Font.TRUETYPE_FONT,12));    
        add(cancelBtn); 
        
        loginBtn = new JButton("Ok");        
        loginBtn.setFont(new Font("Arial",Font.TRUETYPE_FONT,12));    
        add(loginBtn); 
        
        loginBtn.setActionCommand(AuthenticatorController.LOGIN_COMMAND);
        cancelBtn.setActionCommand(AuthenticatorController.CANCEL_COMMAND);
        
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
		cancelBtn.addActionListener(listener);
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
