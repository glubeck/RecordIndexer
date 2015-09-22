package dataimporter;

import java.io.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import model.*;

import org.w3c.dom.*;
import org.xml.sax.SAXException;

import dbaccess.*;


public class IndexerData {
	

	public void deleteAll(DAuser a, DAproject b, DAfield c, DAimage d, DArecord e, DAcell f) throws DatabaseException, SQLException {
		
		a.deleteAll();
		b.deleteAll();
		c.deleteAll();
		d.deleteAll();
		e.deleteAll();
		f.deleteAll();
	}
	
	public IndexerData(Element root, Database db) throws DatabaseException, SQLException {
		
//		ArrayList<Element> rootElements = DataImporter.getChildElements(root);
//		
		DAuser DAUser = new DAuser(db);
		DAproject DAProject = new DAproject(db);
		DAfield DAField = new DAfield(db);
		DAimage DAImage = new DAimage(db);
		DArecord DARecord = new DArecord(db);
		DAcell DACell = new DAcell(db);
		
		deleteAll(DAUser, DAProject, DAField, DAImage, DARecord, DACell);
		
		NodeList userElements = root.getElementsByTagName("user");
		for(int i = 0; i < userElements.getLength(); i++) {
			Element userElement = (Element) userElements.item(i);
			User user = new User(userElement);
			DAUser.add(user);
		}
		
		NodeList projectElements = root.getElementsByTagName("project");
		for(int i = 0; i < projectElements.getLength(); i++) {
			Element projectElement = (Element) projectElements.item(i);
			Project proj = new Project(projectElement);
			DAProject.add(proj);
			
			NodeList fieldElements = projectElement.getElementsByTagName("field");
			List<Integer> fieldIDs = new ArrayList<Integer>();
			for(int j = 0; j < fieldElements.getLength(); j++) {
				Element fieldElement = (Element) fieldElements.item(j);
				Field field = new Field(fieldElement, proj.getID(), j+1);
				DAField.add(field);
				fieldIDs.add(field.getID());
			}
			
			NodeList imageElements = projectElement.getElementsByTagName("image");
			for(int j = 0; j < imageElements.getLength(); j++) {
				Element imageElement = (Element) imageElements.item(j);
				Image image = new Image(imageElement, proj.getID());
				DAImage.add(image);
				
				NodeList recordElements = imageElement.getElementsByTagName("record");
				for(int k = 0; k < recordElements.getLength(); k++) {
					Element recordElement = (Element) recordElements.item(k);
					Record record = new Record(recordElement, image.getID(), k);
					DARecord.add(record);
					
					NodeList valueElements = recordElement.getElementsByTagName("values");
					for(int l = 0; l < valueElements.getLength(); l++) {
						Element valueElement = (Element) valueElements.item(l);
						
						NodeList cellElements = valueElement.getElementsByTagName("value");
						for(int m = 0; m < cellElements.getLength(); m++) {
							Element cellElement = (Element) cellElements.item(m);
							Cell cell = new Cell(cellElement, record.getID(), fieldIDs.get(m), k);
							DACell.add(cell);
						}
					}
				}
			}
		}
		
		

	}
}