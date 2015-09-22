package client;

import java.util.List;

import model.*;


/**
 * contains projects information for the result of the
 * GetProjects method in the ClientCommunicator class
 * @author Grant
 *
 */
public class GetProjects_Result {

	private List<Project> projects;
	private boolean valid = true;

	private String URL_PATH;
	
	public List<Project> getProjects() {
		return projects;
	}

	public String toString() {
		
		StringBuilder sb = new StringBuilder("");
		
		for(Project p : projects) {
			
			sb.append(p.getID() + "\n" + p.getTitle() + "\n");
		}
		
		return sb.toString();
	}

	public void setProjects(List<Project> projects) {
		this.projects = projects;
	}

	public GetProjects_Result(List<Project> projects) {
		this.projects = projects;
	}

	public boolean isValid() {
		return valid;
	}

	public void setValid(boolean valid) {
		this.valid = valid;
	}
	public String getURL_PATH() {
		return URL_PATH;
	}

	public void setURL_PATH(String uRL_PATH) {
		URL_PATH = uRL_PATH;
	}
}
