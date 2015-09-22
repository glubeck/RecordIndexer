package client;

import model.User;

/**
 * contains user information for the paramaters of the ValidateUser
 * method in the Client Communicator class
 * @author Grant
 *
 */
public class ValidateUser_Params {

	private User user;

	public ValidateUser_Params(User user) {
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
