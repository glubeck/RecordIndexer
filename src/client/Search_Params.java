package client;

import model.*;

/**
 * contains the user, list of fields, and search values that are to be the 
 * parameters for the Search method of the ClientCommunicator class
 * @author Grant
 *
 */
public class Search_Params {

	private User user;
	private int[] fields;
	private String[] values;
	
	public Search_Params(User user, int[] fields, String[] values) {
		this.user = user;
		this.fields = fields;
		this.values = values;
	}
	
	public Search_Params(User user, String fields, String values) {
		this.user = user;
		this.fields = createFields(fields);
		this.values = createValues(values);
	}

	public int[] createFields(String input) {

	String[] items = input.split(",");
	int[] results = new int[items.length];
	
	for(int i = 0; i < items.length; i++) {
		try{
			results[i] = Integer.parseInt(items[i]);
		} catch (NumberFormatException nfe) {};
	}
	return results;
	}
	
	public String[] createValues(String input) {
		
		String[] items = input.split(",");
		
		return items;
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

	public int[] getFields() {
		return fields;
	}

	public void setFields(int[] fields) {
		this.fields = fields;
	}

	public String[] getValues() {
		return values;
	}

	public void setValues(String[] values) {
		this.values = values;
	}
	
	
	
}
