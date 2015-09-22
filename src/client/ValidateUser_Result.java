package client;

import model.User;

/**
 * contains user information for the result of the ValidateUser method
 * in the client Communicator
 * @author Grant
 *
 */
public class ValidateUser_Result {
	
	private User user;
	private boolean valid = false;
	private boolean inDatabase = true;

	private String URL_PATH;
	
	public ValidateUser_Result(User user) {
		this.user = user;
	}
	
	public String toString() {
		if(valid) {
		if(user == null)
			return "FALSE";
		
		StringBuilder sb = new StringBuilder("TRUE\n" + user.getFirstname() + "\n" + user.getLastname() + 
				"\n" + user.getIndexedRecords() + "\n");

		return sb.toString();
		}
		else
			return "FAILED";
	}

	public boolean isValid() {
		return valid;
	}

	public void setValid(boolean valid) {
		this.valid = valid;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
	
	public String getURL_PATH() {
		return URL_PATH;
	}

	public void setURL_PATH(String uRL_PATH) {
		URL_PATH = uRL_PATH;
	}

	public boolean isInDatabase() {
		return inDatabase;
	}

	public void setInDatabase(boolean inDatabase) {
		this.inDatabase = inDatabase;
	}
	
	
}
