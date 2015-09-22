package client;

import java.util.List;

import model.*;

/**
 * contains user, project, image, and fields information for the result of the
 * DonwloadBatch_Result method of the ClientCommunicator class
 * @author Grant
 *
 */
public class DownloadBatch_Result {

	private User user;
	private Project project;
	private Image image;
	private List<Field> fields;
	private boolean valid = true;
	
	private String URL_PATH;
	
	public DownloadBatch_Result(User user, Project project, Image image,
			List<Field> fields) {
		this.user = user;
		this.project = project;
		this.image = image;
		this.fields = fields;
	}

	public String urlToString() {
		
		StringBuilder sb = new StringBuilder(URL_PATH + "/database/Records/" + image.getFile());
		return sb.toString();
	}
	
	public String toString() {

		StringBuilder sb = new StringBuilder(image.getID() + "\n" + project.getID() + "\n" + URL_PATH + "/database/Records/" + image.getFile() + 
				"\n" + project.getFirstYCoord() + "\n" + project.getRecordHeight() + "\n" + 
				project.getRecordsPerImage() + "\n" + fields.size() + "\n");
		
		
		for (Field f : fields) {
			
			
			if(!f.getKnownData().equals("")) {
			sb.append(f.getID() + "\n" + f.getNumber() + "\n" + f.getTitle() + "\n" + URL_PATH + "/database/Records/" + f.getHelphtml() + "\n" +
			f.getXcoord() + "\n" + f.getWidth() + "\n" + URL_PATH + "/database/Records/" + f.getKnownData() + "\n");
			}
			else if(f.getKnownData() == null || f.getKnownData().equals("")) {
				sb.append(f.getID() + "\n" + f.getNumber() + "\n" + f.getTitle() + "\n" + URL_PATH + "/database/Records/" + f.getHelphtml() + "\n" +
						f.getXcoord() + "\n" + f.getWidth() + "\n");
			}
		}	
		
		return sb.toString();
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

	public Image getImage() {
		return image;
	}

	public void setImage(Image image) {
		this.image = image;
	}
	
	public List<Field> getFields() {
		return fields;
	}

	public void setFields(List<Field> fields) {
		this.fields = fields;
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
