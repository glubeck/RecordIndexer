package model;

import org.w3c.dom.Element;


/**
 * contains all of the information for a cell which represents the 
 * actual value of a given field
 * @author Grant
 *
 */
public class Cell {

	private int id;
	private int rowNum;
	private int parentID;
	private int fieldID;
	private String value;
	
	public Cell(int id, int rowNum, int parentID, int fieldID, String value) {
		
		this.id = id;
		this.rowNum = rowNum;
		this.parentID = parentID;
		this.fieldID = fieldID;
		this.value = value;
	}


	public Cell(Element cellElement, int parentID, int fieldID, int rowNum) {

		value = cellElement.getTextContent();
		this.parentID = parentID;
		this.fieldID = fieldID;
		this.rowNum = rowNum;
	}


	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	public int getRowNum() {
		return rowNum;
	}

	public void setRowNum(int rowNum) {
		this.rowNum = rowNum;
	}

	public int getParentID() {
		return parentID;
	}

	public void setParentID(int parentID) {
		this.parentID = parentID;
	}

	public int getFieldID() {
		return fieldID;
	}

	public void setFieldID(int fieldID) {
		this.fieldID = fieldID;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
	
	
}
