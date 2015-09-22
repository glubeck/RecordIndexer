package client;

import model.*;

/**
 * contains the user and project information for the parameters of 
 * the getFields method of the ClientCommunicator class
 * @author Grant
 *
 */
public class GetFields_Params {

	private User user;
	private Project project;
	
	public GetFields_Params(User user, Project project) {
		this.user = user;
		this.project = project;
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

	public Project getProject() {
		return project;
	}

	public void setProject(Project project) {
		this.project = project;
	}
	
	
}
