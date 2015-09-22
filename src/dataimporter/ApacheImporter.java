package dataimporter;

import java.io.*;
import java.util.*;
import java.util.logging.*;

import org.apache.commons.io.*;

import model.*;
import dbaccess.*;
import server.*;

import org.w3c.dom.*;

import javax.xml.parsers.*;

public class ApacheImporter
{
	
private static Logger logger;
	
	static {
		logger = Logger.getLogger("");
	}
	
	public ApacheImporter() {}
	
	public void importData(File parentDir) throws Exception
	{
		
		DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
		//File xmlFile = new File(parentDir);
		File dest = new File("database" + File.separator + "Records");
		
		File emptydb = new File("database" + File.separator + "empty" + File.separator + "recordindexer.sqlite");
		File currentdb = new File("database" + File.separator + "recordindexer.sqlite");
		//File parentpath = xmlFile.getAbsoluteFile().getParentFile();
		
		/*
		 * (**APACHE**)
		 */
		try
		{
			//	We make sure that the directory we are copying is not the the destination
			//	directory.  Otherwise, we delete the directories we are about to copy.
			if(!parentDir.getCanonicalPath().equals(dest.getCanonicalPath()))
				FileUtils.deleteDirectory(dest);
				
			//	Copy the directories (recursively) from our source to our destination.
			FileUtils.copyDirectory(parentDir.getAbsoluteFile(), dest);
			
			//	Overwrite my existing *.sqlite database with an empty one.  Now, my
			//	database is completely empty and ready to load with data.
			FileUtils.copyFile(emptydb, currentdb);
		}
		catch (IOException e)
		{
			e.printStackTrace();
			logger.log(Level.SEVERE, "Unable to deal with the IOException thrown", e);
		}
		/*
		 * (**APACHE**)
		 */
		
	}
}