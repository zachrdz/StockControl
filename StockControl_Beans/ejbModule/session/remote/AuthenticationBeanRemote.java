package session.remote;


import javax.ejb.Remote;

import session.models.Session;

@Remote
public interface AuthenticationBeanRemote {
	public Session authenticateUser(String email, String password);
}
