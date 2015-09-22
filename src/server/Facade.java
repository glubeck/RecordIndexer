package server;

import java.rmi.ServerException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

import client.*;
import dbaccess.*;
import model.*;

public class Facade {

	public Facade() {}
	
	public static void initialize() throws ServerException {		
		try {
			Database.initialize();		
		}
		catch (DatabaseException e) {
			throw new ServerException(e.getMessage(), e);
		}		
	}
	
	public static ValidateUser_Result validateUser(ValidateUser_Params params) throws ServerException, SQLException {	

		Database db = new Database();
		
		try {
			db.startTransaction();
			User user = db.getDAUser().checkUser(params.getUser());
			ValidateUser_Result result = new ValidateUser_Result(user);
			result.setValid(true);
			
			if(db.getDAUser().checkUser(params.getUser()) == null) {
				result.setValid(false);
				result.setInDatabase(false);
			}
			db.endTransaction(result.isValid());
			return result;
		}
		catch (DatabaseException e) {
			db.endTransaction(false);
			throw new ServerException(e.getMessage(), e);
		}
	}

	public static GetProjects_Result getProjects(GetProjects_Params params) throws ServerException, SQLException {
		
		Database db = new Database();
		
		try {
			db.startTransaction();			
			List<Project> projects = db.getDAProject().getAllProjects();
			GetProjects_Result result = new GetProjects_Result(projects);
			
			if(db.getDAUser().checkUser(params.getUser()) == null) {
				result.setValid(false);
			}
			
			db.endTransaction(result.isValid());
			return result;
		}
		catch (DatabaseException e) {
			db.endTransaction(false);
			throw new ServerException(e.getMessage(), e);
		}
	}
	
	public static GetSampleImage_Result getSampleImage(GetSampleImage_Params params) throws ServerException, SQLException {
		
		Database db = new Database();
		
		try {
			db.startTransaction();
			Image image = db.getDAImage().getSampleImage(params.getProject());
			GetSampleImage_Result result = new GetSampleImage_Result(image);
			
			if(db.getDAUser().checkUser(params.getUser()) == null) {
				result.setValid(false);
			}
			
			db.endTransaction(result.isValid());
			return result;
		}
		catch (DatabaseException e) {
			db.endTransaction(false);
			throw new ServerException(e.getMessage(), e);
		}
	}
	
	public static DownloadBatch_Result downloadBatch(DownloadBatch_Params params) throws ServerException, SQLException {
		
		Database db = new Database();
		
		try {
			db.startTransaction();
			Project project = params.getProject();
			Project realProject = db.getDAProject().getProject(project);
			User user = db.getDAUser().getUser(params.getUser().getUsername(), params.getUser().getPassword());
			
			
			if(user != null) {
				boolean temp = true;
			if(!user.getCurrentBatch().equals(""))
				temp = false;
			
			Image image = db.getDAImage().getImage(params.getProject(), user);
			List<Field> fields = db.getDAField().getAllFieldsForProject(params.getProject());
			
			DownloadBatch_Result result = new DownloadBatch_Result(user, realProject, image, fields);
			if(db.getDAUser().checkUser(params.getUser()) == null) {
				result.setValid(false);
			}
			if(!temp) {
				result.setValid(false);
			}
			db.endTransaction(result.isValid());
			
			return result;
			}
			else
				db.endTransaction(false);
				return null;
		}
		catch (DatabaseException e ) {
			db.endTransaction(false);
			throw new ServerException(e.getMessage(), e);
		}
	}
	
	public static SubmitBatch_Result submitBatch(SubmitBatch_Params params) throws ServerException, SQLException {
		
		Database db = new Database();
		
		try {
			db.startTransaction();
			
			Image image = db.getDAImage().getImage(params.getImage());
			User user = params.getUser();
			User realUser = db.getDAUser().getUser(user.getUsername(), user.getPassword());
			SubmitBatch_Result result = new SubmitBatch_Result(true);
			
			if(image.getStatus() == 2 || image.getStatus() == 0) {
				result.setSuccess(false);
				return result;
			}
				
			image.setStatus(2);
			db.getDAImage().update(image);
			
			Project project = new Project(image.getAssociatedProject());
			Project realProject = db.getDAProject().getProject(project);
			
			
			List<Field> fields = db.getDAField().getAllFieldsForProject(project);
			List<String[]> values = params.getRecordValues();
			
			for(int i = 0; i < values.size(); i++) {
				
				Record record = new Record(-1, image.getID(), i);
				db.getDARecord().add(record);
				
				String[] v = values.get(i);
				for(int j = 0; j < v.length; j++) {
					if(v.length != fields.size()) {
						result.setSuccess(false);
						return result;
					}
					Cell cell = new Cell(-1, i, record.getID(), fields.get(j).getID(), v[j]);
					db.getDACell().add(cell);
				}
			}
			/*Jones,Fred,13,White;Rogers,Susan,42,Black;Van,Bill,23,Asian;Van,Bill,23,Asian;Van,Bill,23,Asian;Van,Bill,23,Asian;Van,Bill,23,Asian*/
			
			
			if(db.getDAUser().checkUser(params.getUser()) == null) {
				result.setSuccess(false);
				return result;
			}
			
			if(!realUser.getCurrentBatch().equals(image.getFile())) {
				result.setSuccess(false);
				return result;
			}
			
			if(realProject.getRecordsPerImage() != values.size()) {
				result.setSuccess(false);
				return result;
			}
			
			if(realProject.getRecordsPerImage() != values.size()) {
				result.setSuccess(false);
				return result;	
			}
			else {
				realUser.setIndexedRecords(realUser.getIndexedRecords() + realProject.getRecordsPerImage());
				realUser.setCurrentBatch("");
				db.getDAUser().update(realUser);
			}
			
			
			//This is to ensure the correct number of values is put in
			for(String[] s : values) {
				if(s.length != fields.size())
					result.setSuccess(false);
			}
			
			db.endTransaction(result.isSuccess());
			return result;
		}
		catch (DatabaseException e) {
			db.endTransaction(false);
			throw new ServerException(e.getMessage(), e);
		}
	}
	
	public static GetFields_Result getFields(GetFields_Params params) throws ServerException, SQLException {
		
		Database db = new Database();
		
		try {
			db.startTransaction();
			Project project = params.getProject();
			List<Field> fields;
			if(project.getID() == -1)
				fields = db.getDAField().getAll();
			else
				fields = db.getDAField().getAllFieldsForProject(project);
			GetFields_Result result = new GetFields_Result(fields);
			if(result.getFields().size() == 0)
				result.setValid(false);
			
			if(db.getDAUser().checkUser(params.getUser()) == null) {
				result.setValid(false);
			}
			
			db.endTransaction(result.isValid());
			return result;
		}
		catch (DatabaseException e) {
			db.endTransaction(false);
			throw new ServerException(e.getMessage(), e);
		}
	}
	
	public static Search_Result search(Search_Params params) throws ServerException, DatabaseException, SQLException {
		
		Database db = new Database();
		db.startTransaction();
		
		ArrayList<Integer> batchIDs = new ArrayList<Integer>();
		ArrayList<String> ImageURLs = new ArrayList<String>();
		ArrayList<Integer> recordNumbers = new ArrayList<Integer>();
		ArrayList<Integer> FieldIDs = new ArrayList<Integer>();
		
		String[] values = params.getValues();
		int[] fields = params.getFields();
		
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			StringBuilder sb = new StringBuilder("SELECT image.id, image.file, "
					+ "record.rownum, field.id FROM image, record, field, cell WHERE "
					+ "cell.parentid = record.id AND field.id = cell.fieldid "
					+ "AND record.associatedimage = image.id AND (");
			
			for(int i = 0; i < values.length; i++) {
				if(i != values.length-1)
					sb.append("cell.value = ? OR ");
				else
					sb.append("cell.value = ?) AND (");
			}
			for(int i = 0; i < fields.length; i++) {
				if(i != fields.length-1)
					sb.append("field.id = ? OR ");
				else
					sb.append("field.id = ?)");
			}
			
			String query = sb.toString();
			stmt = db.getConnection().prepareStatement(query);
			
			int j = 0;
			for(int i = 0; i < values.length; i++) {
				String value = values[i];
				stmt.setString(i+1, values[i]);
				j++;
			}
			for(int i = 0; i < fields.length; i++) {
				int field = fields[i];
				stmt.setInt(j+1, fields[i]);
				j++;
			}
			
			rs = stmt.executeQuery();
			while (rs.next()) {
				batchIDs.add(rs.getInt(1));
				ImageURLs.add(rs.getString(2));
				recordNumbers.add(rs.getInt(3));
				FieldIDs.add(rs.getInt(4));
			}
		}
		catch (SQLException e) {
			DatabaseException serverEx = new DatabaseException(e.getMessage(), e);
			throw serverEx;
		}
		finally {
			Database.safeClose(rs);
			Database.safeClose(stmt);
		}
		Search_Result result = new Search_Result(batchIDs, ImageURLs, 
				recordNumbers, FieldIDs);
		
		if(db.getDAUser().checkUser(params.getUser()) == null) {
			result.setValid(false);
		}
		
		List<Field> all = db.getDAField().getAll();
		List<Integer> allInts = new ArrayList<Integer>();
		for(Field f : all) {
			allInts.add(f.getID());
		}
		for(int i = 0; i < fields.length; i++) {
			if(!allInts.contains(fields[i]))
				result.setValid(false);
		}
		
		db.endTransaction(result.isValid());
		return result;	
	}
}