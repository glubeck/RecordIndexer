package client;

import java.util.List;

import model.*;

/**
 * contains a list of fields for the result
 * of the GetFields method of the ClientCommunicator class
 * @author Grant
 *
 */
public class GetFields_Result {

	private List<Field> fields;
	private boolean valid = true;
	
	private String URL_PATH;
	
	public GetFields_Result(List<Field> fields) {
		this.fields = fields;
	}

	public String toString() {
		
		StringBuilder sb = new StringBuilder("");
		for(Field f : fields) {
			sb.append(f.getAssociatedProject() + "\n" + f.getID() + "\n" + f.getTitle() + "\n");
		}
		return sb.toString();
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
