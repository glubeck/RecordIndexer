package model;

import org.w3c.dom.Element;

/**
 * contains all of the information for a record, which represents an image
 * which has already been indexed
 * @author Grant
 *
 */
public class Record {

	private int id;
	private String value = "";
	private int associatedImage;
	private int fieldID;
	private int rowNum;
	
	public Record(int id, String value, int associatedImage, int fieldID, int rowNum) {
		
		this.id = id;
		this.value = value;
		this.associatedImage = associatedImage;
		this.fieldID = fieldID;
		this.rowNum = rowNum;
	}

	public Record(int id, int associatedImage, int rowNum) {
		
		this.id = id;
		this.associatedImage = associatedImage;
		this.rowNum = rowNum;
	}
	
	public Record(Element recordElement, int associatedImage, int rowNum) {
		
		this.associatedImage = associatedImage;
		this.rowNum = rowNum;
	}

	public int getID() {
		return id;
	}

	public void setID(int id) {
		this.id = id;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public int getFieldID() {
		return fieldID;
	}

	public void setFieldID(int fieldID) {
		this.fieldID = fieldID;
	}

	public int getRowNum() {
		return rowNum;
	}

	public void setRowNum(int rowNum) {
		this.rowNum = rowNum;
	}

	public int getAssociatedImage() {
		return associatedImage;
	}

	public void setAssociatedImage(int associatedImage) {
		this.associatedImage = associatedImage;
	}
	
}
