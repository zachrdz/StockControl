package core.settings.models;


import javax.ejb.Remote;

import core.settings.models.Session;

@Remote
public interface AuthenticationBeanRemote {
	public Session authenticateUser(String email, String password);
}
