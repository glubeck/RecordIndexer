package dataimporter;

import java.io.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.logging.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.*;
import org.xml.sax.SAXException;

import dbaccess.Database;
import dbaccess.DatabaseException;


public class DataImporter {

	static Database db;
	
	public static void main(String[] args) throws Exception {
		
		Database.initialize();
		Database db = new Database();
		db.startTransaction();
		String fileName = args[0];
		
		File xmlFile = new File(fileName);
		
		ApacheImporter importer = new ApacheImporter();
		importer.importData(xmlFile.getParentFile());
		
		DataImporter DI = new DataImporter();
		
		
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		Document doc = dBuilder.parse(xmlFile);
		
		doc.getDocumentElement().normalize();
		
		Element root = doc.getDocumentElement();
		
		IndexerData indexerdata = new IndexerData(root, db);
	

		
		db.endTransaction(true);
}
	
	public static ArrayList<Element> getChildElements(Node  node) {
		ArrayList<Element> result = new ArrayList<Element>();
		
		NodeList children = node.getChildNodes();
		for(int i = 0; i < children.getLength(); i++) {
			Node child = children.item(i);
			if(child.getNodeType() == Node.ELEMENT_NODE){
				result.add((Element)child);
			}
		}
		return result;
	}
	
	public static String getValue(Element element) {
		String result = "";
		if(element != null) {
		Node child = element.getFirstChild();
		result = child.getNodeValue();
		return result;
	}
	else return null;
	}
}


