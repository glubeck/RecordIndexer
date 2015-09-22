package dbaccess;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import model.Field;
import model.Project;

public class DAfield {
	private static Logger logger;
	
	static {
		logger = Logger.getLogger("");
	}
	
	private Database db;
	
	public DAfield(Database db) {
		this.db = db;
	}
	
	public List<Field> getAll() throws DatabaseException {
		
		logger.entering("dbaccess.DAfield", "getAll");
		
		ArrayList<Field> result = new ArrayList<Field>();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			String query = "select id, title, xcoord, width, helphtml, "
					+ "knowndata, associatedproject, number from field";
			stmt = db.getConnection().prepareStatement(query);
			
			rs = stmt.executeQuery();
			while (rs.next()) {
				int id = rs.getInt(1);
				String title = rs.getString(2);
				int xcoord = rs.getInt(3);
				int width = rs.getInt(4);
				String helphtml = rs.getString(5);
				String knowndata = rs.getString(6);
				int associatedfield = rs.getInt(7);
				int number = rs.getInt(8);
				
				result.add(new Field(id, title, xcoord, width, helphtml, 
						knowndata, associatedfield, number));
			}
		}
		catch (SQLException e) {
			DatabaseException serverEx = new DatabaseException(e.getMessage(), e);
			
			logger.throwing("dbaccess.DAfield", "getAll", serverEx);
			
			throw serverEx;
		}
		finally {
			Database.safeClose(rs);
			Database.safeClose(stmt);
		}
		logger.exiting("dbaccess.DAfield", "getAll");
		
		return result;	
	}
	
	/**
	 * Gets all the fields in the database for a given field
	 * @param field who's fields we want
	 * @return List<Field> of the field
	 * @throws DatabaseException 
	 */
	public List<Field> getAllFieldsForProject(Project project) throws DatabaseException {
		
		List<Field> fields = getAll();
		List<Field> result = new ArrayList<Field>();
		for(Field f : fields) {	
			if(f.getAssociatedProject() == project.getID())
				result.add(f);
		}	
		return result;
	}
	
	public void deleteAll() throws DatabaseException, SQLException {
		
		List<Field> all = getAll();
		
		for(Field f : all) {
			delete(f);
		}
	}
	
	/**
	 * Adds a field to the database
	 * @throws DatabaseException 
	 */
	public void add(Field field) throws DatabaseException {
		
		PreparedStatement stmt = null;
		ResultSet keyRS = null;		
		try {
			String query = "INSERT INTO field(title, xcoord, width, helphtml, "
					+ "knowndata, associatedproject, number) VALUES(?, ?, ?, ?, ?, ?, ?)";
			stmt = db.getConnection().prepareStatement(query);
			stmt.setString(1, field.getTitle());
			stmt.setInt(2, field.getXcoord());
			stmt.setInt(3, field.getWidth());
			stmt.setString(4, field.getHelphtml());
			stmt.setString(5, field.getKnownData());
			stmt.setInt(6, field.getAssociatedProject());
			stmt.setInt(7, field.getNumber());
			if (stmt.executeUpdate() == 1) {
				Statement keyStmt = db.getConnection().createStatement();
				keyRS = keyStmt.executeQuery("select last_insert_rowid()");
				keyRS.next();
				int id = keyRS.getInt(1);
				field.setID(id);
			}
			else {
				throw new DatabaseException("Could not insert field");
			}
		}
		catch(SQLException e) {
			throw new DatabaseException("Could not insert field", e);
		}
		finally {
			Database.safeClose(stmt);
			Database.safeClose(keyRS);
		}
	}
	
	/**
	 * Updates an existing field in the database
	 * @throws DatabaseException 
	 */
	public void update(Field field) throws DatabaseException {
		
		PreparedStatement stmt = null;
		try {
			String query = "update field set title = ?, xcoord = ?, width = ?, "
					+ "helphtml = ?, knowndata = ?, associatedproject = ?, number = ? "
					+ "where id = ?";
			stmt = db.getConnection().prepareStatement(query);
			stmt.setString(1, field.getTitle());
			stmt.setInt(2, field.getXcoord());
			stmt.setInt(3, field.getWidth());
			stmt.setString(4, field.getHelphtml());
			stmt.setString(5, field.getKnownData());
			stmt.setInt(6, field.getAssociatedProject());
			stmt.setInt(7, field.getNumber());
			stmt.setInt(8, field.getID());
			if (stmt.executeUpdate() != 1) {
				throw new DatabaseException("Could not update field");
			}
		}
		catch (SQLException e) {
			throw new DatabaseException("Could not update field", e);
		}
		finally {
			Database.safeClose(stmt);
		}
	}

	public void delete(Field field) throws DatabaseException {
		PreparedStatement stmt = null;
		try {
			String query = "delete from field where id = ?";
			stmt = db.getConnection().prepareStatement(query);
			stmt.setInt(1,  field.getID());
			if (stmt.executeUpdate() != 1) {
				throw new DatabaseException("Could not delete field");
			}
		}
		catch (SQLException e) {
			throw new DatabaseException("Could not delete field", e);
		}
		finally {
			Database.safeClose(stmt);
		}
	}
}
