package model;

import org.w3c.dom.Element;

import dataimporter.DataImporter;

/**
 * contains all of the information for an image, which represents
 * a batch which a person can index
 * @author Grant
 *
 */
public class Image {

	private int id;
	private String file;
	private int associatedProject;
	private int status;
	
	public Image(int id, String file, int associatedProject, int status) {
		
		this.id = id;
		this.file = file;
		this.associatedProject = associatedProject;
		this.status = status;
	}
	
	public Image(int id) {
		
		this.id = id;
		this.file = "";
		this.associatedProject = 0;
		this.status = 0;
	}

	public Image(Element imageElement, int associatedProject) {
		
		file = DataImporter.getValue((Element)imageElement.getElementsByTagName("file").item(0));
		this.associatedProject = associatedProject;
	}

	public int getID() {
		return id;
	}

	public void setID(int id) {
		this.id = id;
	}

	public String getFile() {
		return file;
	}

	public void setFile(String file) {
		this.file = file;
	}

	public int getAssociatedProject() {
		return associatedProject;
	}

	public void setAssociatedProject(int associatedProject) {
		this.associatedProject = associatedProject;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}
	
	
	
}
