package model;

import org.w3c.dom.Element;

import dataimporter.DataImporter;

/**
 * contains all of the information for the project which you are currently
 * working on, which contains multiple images
 * @author Grant
 *
 */
public class Project {

	private int id;
	private String title;
	private int recordsPerImage;
	private int firstYCoord;
	private int recordHeight;
	
	public Project(int id, String title, int recordsPerImage, int firstYCoord,
			int recordHeight) {
		
		this.id = id;
		this.title = title;
		this.recordsPerImage = recordsPerImage;
		this.firstYCoord = firstYCoord;
		this.recordHeight = recordHeight;
	}
	
	public Project(int id) {
		
		this.id = id;
		this.title = "";
		this.recordsPerImage = 0;
		this.firstYCoord = 0;
		this.recordHeight = 0;
	}

	public Project(Element projectElement) {
		
		title = DataImporter.getValue((Element)projectElement.getElementsByTagName("title").item(0));
		recordsPerImage = Integer.parseInt(DataImporter.getValue((Element)projectElement.getElementsByTagName("recordsperimage").item(0)));
		firstYCoord = Integer.parseInt(DataImporter.getValue((Element)projectElement.getElementsByTagName("firstycoord").item(0)));
		recordHeight = Integer.parseInt(DataImporter.getValue((Element)projectElement.getElementsByTagName("recordheight").item(0)));
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

	public int getRecordsPerImage() {
		return recordsPerImage;
	}

	public void setRecordsPerImage(int recordsPerImage) {
		this.recordsPerImage = recordsPerImage;
	}

	public int getFirstYCoord() {
		return firstYCoord;
	}

	public void setFirstYCoord(int firstYCoord) {
		this.firstYCoord = firstYCoord;
	}

	public int getRecordHeight() {
		return recordHeight;
	}

	public void setRecordHeight(int recordHeight) {
		this.recordHeight = recordHeight;
	}
	
}
