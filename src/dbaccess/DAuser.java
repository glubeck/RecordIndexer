package dbaccess;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import model.User;

public class DAuser {

	private static Logger logger;
	
	static {
		logger = Logger.getLogger("");
	}
	
	private Database db;
	//PRIVAAAAATTTTEEEE
	public DAuser(Database db) {
		this.db = db;
	}
	
	/**
	 * Returns a list of all users in the database
	 */
	public List<User> getAll() throws DatabaseException{
		
		logger.entering("dbaccess.DAuser", "getAll");
		
		ArrayList<User> result = new ArrayList<User>();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			String query = "select id, username, password, firstname, "
					+ "lastname, email, indexedrecords, currentbatch from user";
			stmt = db.getConnection().prepareStatement(query);
			
			rs = stmt.executeQuery();
			while (rs.next()) {
				int id = rs.getInt(1);
				String username = rs.getString(2);
				String password = rs.getString(3);
				String firstname = rs.getString(4);
				String lastname = rs.getString(5);
				String email = rs.getString(6);
				int indexedrecords = rs.getInt(7);
				String currentbatch = rs.getString(8);
				
				result.add(new User(id, username, password, firstname, lastname, 
						email, indexedrecords, currentbatch));
			}
			
		}
		catch(SQLException e) {
			DatabaseException serverEx = new DatabaseException(e.getMessage(), e);
			
			logger.throwing("dbaccess.DAuser", "getAll", serverEx);
			
			throw serverEx;
			
		}
		finally {
			Database.safeClose(rs);
			Database.safeClose(stmt);
		}
		logger.exiting("dbaccess.DAuser", "getAll");
		
		return result;
	}
	
	public User getUser(String username, String password) throws DatabaseException {
		
		List<User> all = getAll();
		
		for(User u : all) {
			if(u.getUsername().toLowerCase().equals(username.toLowerCase()) &&
					u.getPassword().toLowerCase().equals(password.toLowerCase())) {
				return u;
			}
		}
		return null;
	}
	public void deleteAll() throws DatabaseException {
	
		List<User> all = getAll();
		
		for(User u : all) {
			delete(u);
		}
	}
	
	/**
	 * 
	 * @param userName
	 * @param password
	 * @return boolean indicating whether the username and password 
	 * are valid
	 * @throws SQLException 
	 * @throws DatabaseException 
	 */
	public User checkUser(User user) throws SQLException, DatabaseException {
		
			List<User> users = getAll();
			String username = user.getUsername();
			String password = user.getPassword();
			
			for(User u : users) {
				if(u.getUsername().toLowerCase().equals(username.toLowerCase())
						&& u.getPassword().toLowerCase().equals(password.toLowerCase()))
					return u;
			}	
		return null;
	}
	
	/**
	 * updates the status of the user
	 * this status changes depending if he's currently working on a batch
	 * or not
	 * @param image ???
	 * @throws DatabaseException 
	 */
	public void update(User user) throws DatabaseException {
		
		PreparedStatement stmt = null;
		try {
			String query = "update user set username = ?, password = ?, firstname = ?, "
					+ "lastname = ?, email = ?, indexedrecords = ?, currentbatch = ? "
					+ "where id = ?";
			stmt = db.getConnection().prepareStatement(query);
			stmt.setString(1, user.getUsername());
			stmt.setString(2, user.getPassword());
			stmt.setString(3, user.getFirstname());
			stmt.setString(4, user.getLastname());
			stmt.setString(5, user.getEmail());
			stmt.setInt(6, user.getIndexedRecords());
			stmt.setString(7, user.getCurrentBatch());
			stmt.setInt(8, user.getID());
			if (stmt.executeUpdate() != 1) {
				throw new DatabaseException("Could not update user");
			}
		}
		catch (SQLException e) {
			throw new DatabaseException("Could not update user", e);
		}
		finally {
			Database.safeClose(stmt);
		}
	}
	
	/**
	 * deletes a user who's in the database
	 * @param user
	 * @throws DatabaseException 
	 */
	public void delete(User user) throws DatabaseException {
		
		PreparedStatement stmt = null;
		try {
			String query = "delete from user where id = ?";
			stmt = db.getConnection().prepareStatement(query);
			stmt.setInt(1,  user.getID());
			if (stmt.executeUpdate() != 1) {
				throw new DatabaseException("Could not delete user");
			}
		}
		catch (SQLException e) {
			throw new DatabaseException("Could not delete user", e);
		}
		finally {
			Database.safeClose(stmt);
		}
	}
	
	/**
	 * add's a user into the database
	 * @throws DatabaseException 
	 */
	public void add(User user) throws DatabaseException {
		
		PreparedStatement stmt = null;
		ResultSet keyRS = null;		
		try {
			String query = "INSERT INTO user(username, password, firstname, lastname, email, indexedrecords, currentbatch) VALUES(?, ?, ?, ?, ?, ?, ?)";
			stmt = db.getConnection().prepareStatement(query);
			stmt.setString(1, user.getUsername());
			stmt.setString(2, user.getPassword());
			stmt.setString(3, user.getFirstname());
			stmt.setString(4, user.getLastname());
			stmt.setString(5, user.getEmail());
			stmt.setInt(6, user.getIndexedRecords());
			stmt.setString(7, user.getCurrentBatch());
			
			if (stmt.executeUpdate() == 1) {
				Statement keyStmt = db.getConnection().createStatement();
				keyRS = keyStmt.executeQuery("select last_insert_rowid()");
				keyRS.next();
				int id = keyRS.getInt(1);
				user.setID(id);
			}
			else {
				throw new DatabaseException("Could not insert user");
			}
		}
		catch(SQLException e) {
			throw new DatabaseException("Could not insert user", e);
		}
		finally {
			Database.safeClose(stmt);
			Database.safeClose(keyRS);
		}
	}
}
