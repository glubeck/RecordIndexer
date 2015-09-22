package dbaccess;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import model.Project;

public class DAproject {

	private static Logger logger;
	
	static {
		logger = Logger.getLogger("");
	}
	
	private Database db;
	
	//THIS SHOULD BE PRIVATE
	public DAproject(Database db) {
		this.db = db;
	}
	
	/**
	 * @return a list of all projects in the database
	 */
	public List<Project> getAllProjects() throws DatabaseException {
		
		logger.entering("dbaccess.DAproject", "getAll");
		
		ArrayList<Project> result = new ArrayList<Project>();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			String query = "select id, title, recordsperimage, firstycoord, "
					+ "recordheight from project";
			stmt = db.getConnection().prepareStatement(query);
			
			rs = stmt.executeQuery();
			while (rs.next()) {
				int id = rs.getInt(1);
				String title = rs.getString(2);
				int recordsperimage = rs.getInt(3);
				int firstycoord = rs.getInt(4);
				int recordheight = rs.getInt(5);
				
				result.add(new Project(id, title, recordsperimage, firstycoord, 
						recordheight));
			}
		}
		catch (SQLException e) {
			DatabaseException serverEx = new DatabaseException(e.getMessage(), e);
			
			logger.throwing("dbaccess.DAproject", "getAll", serverEx);
			
			throw serverEx;
		}
		finally {
			Database.safeClose(rs);
			Database.safeClose(stmt);
		}
		logger.exiting("dbaccess.DAproject", "getAll");
		
		return result;	
	}
	
	public Project getProject(Project project) throws DatabaseException {
		
		List<Project> projects = getAllProjects();
		
		for(Project p : projects) {
			
			if(project.getID() == p.getID())
				return p;
		}
		
		return null;
	}
	
public Project getProjectbyTitle(Project project) throws DatabaseException {
		
		List<Project> projects = getAllProjects();
		
		for(Project p : projects) {
			
			if(project.getTitle().equals(p.getTitle()))
				return p;
		}
		
		return null;
	}
	
	public void deleteAll() throws DatabaseException, SQLException {
		
		List<Project> all = getAllProjects();
		
		for(Project p : all) {
			delete(p);
		}
	}
	
	/** 
	 * adds new project to the database
	 * @param project
	 */
	public void add(Project project) throws DatabaseException {
		
		PreparedStatement stmt = null;
		ResultSet keyRS = null;		
		try {
			String query = "INSERT INTO project(title, recordsperimage, firstycoord, recordheight) VALUES(?, ?, ?, ?)";
			stmt = db.getConnection().prepareStatement(query);
			stmt.setString(1, project.getTitle());
			stmt.setInt(2, project.getRecordsPerImage());
			stmt.setInt(3, project.getFirstYCoord());
			stmt.setInt(4, project.getRecordHeight());
			if (stmt.executeUpdate() == 1) {
				Statement keyStmt = db.getConnection().createStatement();
				keyRS = keyStmt.executeQuery("select last_insert_rowid()");
				keyRS.next();
				int id = keyRS.getInt(1);
				project.setID(id);
			}
			else {
				throw new DatabaseException("Could not insert project");
			}
		}
		catch(SQLException e) {
			throw new DatabaseException("Could not insert project", e);
		}
		finally {
			Database.safeClose(stmt);
			Database.safeClose(keyRS);
		}
	}
	
	/**
	 * updates the information of a project in the database
	 * @param project
	 */
	public void update(Project project) throws DatabaseException {
		
		PreparedStatement stmt = null;
		try {
			String query = "update project set title = ?, recordsperimage = ?, firstycoord = ?, recordheight = ? where id = ?";
			stmt = db.getConnection().prepareStatement(query);
			stmt.setString(1, project.getTitle());
			stmt.setInt(2, project.getRecordsPerImage());
			stmt.setInt(3, project.getFirstYCoord());
			stmt.setInt(4, project.getRecordHeight());
			stmt.setInt(5, project.getID());
			if (stmt.executeUpdate() != 1) {
				throw new DatabaseException("Could not update project");
			}
		}
		catch (SQLException e) {
			throw new DatabaseException("Could not update project", e);
		}
		finally {
			Database.safeClose(stmt);
		}
	}
	
	/**
	 * Deletes a project from the database
	 */
	public void delete(Project project) throws DatabaseException {
		PreparedStatement stmt = null;
		try {
			String query = "delete from project where id = ?";
			stmt = db.getConnection().prepareStatement(query);
			stmt.setInt(1,  project.getID());
			if (stmt.executeUpdate() != 1) {
				throw new DatabaseException("Could not delete project");
			}
		}
		catch (SQLException e) {
			throw new DatabaseException("Could not delete project", e);
		}
		finally {
			Database.safeClose(stmt);
		}
	}
	
}
