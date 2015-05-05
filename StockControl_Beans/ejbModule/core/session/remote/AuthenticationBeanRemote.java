package core.session.remote;


import javax.ejb.Remote;

import core.session.models.obj.Session;

@Remote
public interface AuthenticationBeanRemote{
	public Session authenticateUser(String email, String password);
}
