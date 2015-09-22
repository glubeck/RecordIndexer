package dbaccess;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import model.Record;

public class DArecord {

	private static Logger logger;
	
	static {
		logger = Logger.getLogger("");
	}
	
	private Database db;
	
	//THIS SHOULD BE PRIVATE
	public DArecord(Database db) {
		this.db = db;
	}
	
	/**
	 * @return a list of all records in the database
	 * @throws SQLException 
	 * @throws DatabaseException 
	 */
	public List<Record> getAll() throws SQLException, DatabaseException {
		
		logger.entering("dbaccess.DArecord", "getAll");
		
		ArrayList<Record> result = new ArrayList<Record>();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			String query = "select id, value, associatedimage, fieldid, rownum from record";
			stmt = db.getConnection().prepareStatement(query);
			
			rs = stmt.executeQuery();
			while (rs.next()) {
				int id = rs.getInt(1);
				String value = rs.getString(2);
				int associatedimage = rs.getInt(3);
				int fieldid = rs.getInt(4);
				int rownum = rs.getInt(5);
				
				result.add(new Record(id, value, associatedimage, fieldid, rownum));
			}
		}
		catch (SQLException e) {
			DatabaseException serverEx = new DatabaseException(e.getMessage(), e);
			
			logger.throwing("dbaccess.DArecord", "getAll", serverEx);
			
			throw serverEx;
		}
		finally {
			Database.safeClose(rs);
			Database.safeClose(stmt);
		}
		logger.exiting("dbaccess.DArecord", "getAll");
		
		return result;
	}
	
	public void deleteAll() throws DatabaseException, SQLException {
		
		List<Record> all = getAll();
		
		for(Record r : all) {
			delete(r);
		}
	}
	
	/**
	 * adds new record to the database
	 * @param record
	 * @throws DatabaseException 
	 */
	public void add(Record record) throws DatabaseException {
		
		PreparedStatement stmt = null;
		ResultSet keyRS = null;		
		try {
			String query = "INSERT INTO record(value, associatedimage, fieldid, rownum) VALUES(?, ?, ?, ?)";
			stmt = db.getConnection().prepareStatement(query);
			stmt.setString(1, record.getValue());
			stmt.setInt(2, record.getAssociatedImage());
			stmt.setInt(3, record.getFieldID());
			stmt.setInt(4, record.getRowNum());
			if (stmt.executeUpdate() == 1) {
				Statement keyStmt = db.getConnection().createStatement();
				keyRS = keyStmt.executeQuery("select last_insert_rowid()");
				keyRS.next();
				int id = keyRS.getInt(1);
				record.setID(id);
			}
			else {
				throw new DatabaseException("Could not insert record");
			}
		}
		catch(SQLException e) {
			throw new DatabaseException("Could not insert record", e);
		}
		finally {
			Database.safeClose(stmt);
			Database.safeClose(keyRS);
		}
	}
	
	/**
	 * Updates a record in the database
	 * @param record
	 * @throws DatabaseException 
	 */
	public void update(Record record) throws DatabaseException {
		
		PreparedStatement stmt = null;
		try {
			String query = "update record set value = ?, associatedimage = ?, fieldid = ?, rownum = ? where id = ?";
			stmt = db.getConnection().prepareStatement(query);
			stmt.setString(1, record.getValue());
			stmt.setInt(2, record.getAssociatedImage());
			stmt.setInt(3, record.getFieldID());
			stmt.setInt(4, record.getRowNum());
			stmt.setInt(5, record.getID());
			if (stmt.executeUpdate() != 1) {
				throw new DatabaseException("Could not update record");
			}
		}
		catch (SQLException e) {
			throw new DatabaseException("Could not update record", e);
		}
		finally {
			Database.safeClose(stmt);
		}
	}
	
	public void delete(Record record) throws DatabaseException {
		
		PreparedStatement stmt = null;
		try {
			String query = "delete from record where id = ?";
			stmt = db.getConnection().prepareStatement(query);
			stmt.setInt(1, record.getID());
			if (stmt.executeUpdate() != 1) {
				throw new DatabaseException("Could not record contact");
			}
		}
		catch (SQLException e) {
			throw new DatabaseException("Could not record contact", e);
		}
		finally {
			Database.safeClose(stmt);
		}
	}
	
	
}
