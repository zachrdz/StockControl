package core.session.models.remote;


import javax.ejb.Remote;

import core.session.models.obj.Session;

@Remote
public interface AuthenticatorRemote {
	public Session authenticateUser(String email, String password);
}
