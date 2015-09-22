package model;

import org.w3c.dom.Element;

import dataimporter.DataImporter;

/**
 * contains all of the information for a user including his KeyID, username,
 * password, firstname, lastname, email, the amount of records that he's
 * indexed, and the current batch that he is on.
 * @author Grant
 *
 */
public class User {

	private int id = -1;
	private String username;
	private String password;
	private String firstname;
	private String lastname;
	private String email;
	private int indexedRecords;
	private String currentBatch;
	
	public User(int id, String username, String password, String firstname,
			String lastname, String email, int indexedRecords, String currentBatch) {
		
		this.id = id;
		this.username = username;
		this.password = password;
		this.firstname = firstname;
		this.lastname = lastname;
		this.email = email;
		this.indexedRecords = indexedRecords;
		if(currentBatch == null)
			this.currentBatch = "";
		else
			this.currentBatch = currentBatch;
	}
	
	public User(String username, String password) {
		
		this.id = -1;
		this.username = username;
		this.password = password;
		this.firstname = "";
		this.lastname = "";
		this.email = "";
		this.indexedRecords = 0;
		this.currentBatch = "";
	}

	public User(Element userElement) {
		
		username = DataImporter.getValue((Element)userElement.getElementsByTagName("username").item(0));
		password = DataImporter.getValue((Element)userElement.getElementsByTagName("password").item(0));
		firstname = DataImporter.getValue((Element)userElement.getElementsByTagName("firstname").item(0));
		lastname = DataImporter.getValue((Element)userElement.getElementsByTagName("lastname").item(0));
		email = DataImporter.getValue((Element)userElement.getElementsByTagName("email").item(0));
		indexedRecords = Integer.parseInt(DataImporter.getValue((Element)userElement.getElementsByTagName("indexedrecords").item(0)));
	}

	public int getID() {
		return id;
	}

	public void setID(int id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public int getIndexedRecords() {
		return indexedRecords;
	}

	public void setIndexedRecords(int indexedRecords) {
		this.indexedRecords = indexedRecords;
	}

	public String getCurrentBatch() {
		return currentBatch;
	}

	public void setCurrentBatch(String currentBatch) {
		this.currentBatch = currentBatch;
	}
	
}
