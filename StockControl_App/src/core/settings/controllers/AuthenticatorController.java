/**
 * 
 */
package core.settings.controllers;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import core.mdi.MasterFrame;
import core.settings.models.Session;
import core.settings.models.User;
import core.settings.models.AuthenticatorModel;
import core.settings.views.AuthenticatorView;

public class AuthenticatorController implements ActionListener{
	public static final String LOGIN_COMMAND = "LOGIN_COMMAND";
	
	private AuthenticatorView view;
	private AuthenticatorModel model;
	private MasterFrame m;
    
    public AuthenticatorController(AuthenticatorView view, MasterFrame m){
    	this.m = m;
    	this.view = view;
		this.model = m.getAuthenticatorModel();
		initView();
    }
    
    public void initView(){        
    	view.addButtonListener(this);
    }
    
    @Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		switch (e.getActionCommand()) {
		case LOGIN_COMMAND:
			User credentials = getTextInput();
			Session session = model.authenticateUser(credentials.getEmail(), credentials.getPassword());
			
			if(null != session){
				m.setSession(session);
				m.updateJMenuBar();
				view.getFrame().dispose();
			}
			else {
				m.displayChildMessage("Invalid Credentials!");
			}
			break;
		}
	}
    
    private User getTextInput(){    	
    	String username = view.getUsername().getText();
    	String password = new String(view.getPassword().getPassword());
    	
    	User newUser = new User();
    	newUser.setEmail(username);
    	newUser.setPassword(password);
    	
    	return newUser;
    }
}