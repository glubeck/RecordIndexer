package client;

import model.User;

/**
 * contains user information for the parameters
 * of the GetProjects method in the Client Communicator Class
 * @author Grant
 *
 */
public class GetProjects_Params {

	private User user;

	public GetProjects_Params(User user) {
		this.user = user;
	}

	public String toString() {		
		return null;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
	
}
