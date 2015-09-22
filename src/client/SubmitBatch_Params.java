package client;

import java.awt.Image;
import java.util.ArrayList;

import model.*;

/**
 * contains the user and image information for the parameters of the
 * submitBatch method of the ClientCommunicator class
 * @author Grant
 *
 */
public class SubmitBatch_Params {

	private User user;
	private model.Image image;
	private ArrayList<String[]> recordValues;
	
	public SubmitBatch_Params(User user, model.Image image,
			String recordValues) {
		this.user = user;
		this.image = image;
		this.recordValues = createRecords(recordValues);
	}
	public SubmitBatch_Params(User user, model.Image image,
			ArrayList<String[]> recordValues) {
		this.user = user;
		this.image = image;
		this.recordValues = recordValues;
	}


	public ArrayList<String[]> createRecords(String input) {
		
		ArrayList<String[]> result = new ArrayList<String[]>();
		
		String[] records = input.split(";");
		
		for(int i = 0; i < records.length; i++) {
			String[] values = records[i].split(",");
			result.add(values);
		}
		
		return result;
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

	public model.Image getImage() {
		return image;
	}

	public void setImage(model.Image image) {
		this.image = image;
	}

	public ArrayList<String[]> getRecordValues() {
		return recordValues;
	}

	public void setRecordValues(ArrayList<String[]> recordValues) {
		this.recordValues = recordValues;
	}
	



	
	
}
