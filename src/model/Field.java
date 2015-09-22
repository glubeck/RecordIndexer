package model;

import org.w3c.dom.Element;

import dataimporter.DataImporter;

/**
 * represents one field on a table which can store information
 * @author Grant
 *
 */
public class Field {

	private int id;
	private String title;
	private int xcoord;
	private int width;
	private String helphtml;
	private String knownData = "";
	private int associatedProject;
	private int number;
	
	public Field( int id, String title, int xcoord, int width, String helphtml,
			String knownData, int associatedProject, int number) {
		
		this.id = id;
		this.title = title;
		this.xcoord = xcoord;
		this.width = width;
		this.helphtml = helphtml;
		this.knownData = knownData;
		this.associatedProject = associatedProject;
		this.number = number;
	}
	
	public Field(Element fieldElement, int associatedProject, int number) {

		title = DataImporter.getValue((Element)fieldElement.getElementsByTagName("title").item(0));
		xcoord = Integer.parseInt(DataImporter.getValue((Element)fieldElement.getElementsByTagName("xcoord").item(0)));
		width = Integer.parseInt(DataImporter.getValue((Element)fieldElement.getElementsByTagName("width").item(0)));
		helphtml = DataImporter.getValue((Element)fieldElement.getElementsByTagName("helphtml").item(0));
		if(DataImporter.getValue((Element)fieldElement.getElementsByTagName("knowndata").item(0)) != null)
		knownData = DataImporter.getValue((Element)fieldElement.getElementsByTagName("knowndata").item(0));
		this.associatedProject = associatedProject;
		this.number = number;
	}

	public int getID() {
		return id;
	}

	public void setID(int id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public int getXcoord() {
		return xcoord;
	}

	public void setXcoord(int xcoord) {
		this.xcoord = xcoord;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public String getHelphtml() {
		return helphtml;
	}

	public void setHelphtml(String helphtml) {
		this.helphtml = helphtml;
	}

	public String getKnownData() {
		return knownData;
	}

	public void setKnownData(String knownData) {
		this.knownData = knownData;
	}

	public int getAssociatedProject() {
		return associatedProject;
	}

	public void setAssociatedProject(int associatedProject) {
		this.associatedProject = associatedProject;
	}

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	
}
