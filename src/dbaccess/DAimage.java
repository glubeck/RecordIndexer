package dbaccess;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import model.Image;
import model.Project;
import model.User;

public class DAimage {

	private static Logger logger;
	
	static {
		logger = Logger.getLogger("");
	}
	
	private Database db;
	
	//YOU SHOULD BE PRIVATE
	public DAimage(Database db) {
		this.db = db;
	}
	
	/**
	 * Adds an Image to the database
	 * @param image
	 * @throws DatabaseException 
	 */
	public void add(Image image) throws DatabaseException {
		
		PreparedStatement stmt = null;
		ResultSet keyRS = null;		
		try {
			String query = "insert into image (file, associatedproject, status) values (?, ?, ?)";
			stmt = db.getConnection().prepareStatement(query);
			stmt.setString(1, image.getFile());
			stmt.setInt(2, image.getAssociatedProject());
			stmt.setInt(3, image.getStatus());
			if (stmt.executeUpdate() == 1) {
				Statement keyStmt = db.getConnection().createStatement();
				keyRS = keyStmt.executeQuery("select last_insert_rowid()");
				keyRS.next();
				int id = keyRS.getInt(1);
				image.setID(id);
			}
			else {
				throw new DatabaseException("Could not insert image");
			}
		}
		catch (SQLException e) {
			throw new DatabaseException("Could not insert image", e);
		}
		finally {
			Database.safeClose(stmt);
			Database.safeClose(keyRS);
		}
	}
	
	public void deleteAll() throws DatabaseException, SQLException {
		
		List<Image> all = getAll();
		
		for(Image i : all) {
			delete(i);
		}
	}
	
	public List<Image> getAll() throws DatabaseException {
		
		logger.entering("dbaccess.DAimage", "getAll");
		
		ArrayList<Image> result = new ArrayList<Image>();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			String query = "select id, file, associatedproject, status from image";
			stmt = db.getConnection().prepareStatement(query);

			rs = stmt.executeQuery();
			while (rs.next()) {
				int id = rs.getInt(1);
				String file = rs.getString(2);
				int associatedproject = rs.getInt(3);
				int status = rs.getInt(4);

				result.add(new Image(id, file, associatedproject, status));
			}
		}
		catch (SQLException e) {
			DatabaseException serverEx = new DatabaseException(e.getMessage(), e);
			
			logger.throwing("dbaccess.DAimage", "getAll", serverEx);
			
			throw serverEx;
		}		
		finally {
			Database.safeClose(rs);
			Database.safeClose(stmt);
		}

		logger.exiting("dbaccess.DAimage", "getAll");
		
		return result;	
	}
	
	public Image getImage(model.Image image) throws DatabaseException {
		
		List<Image> all = getAll();
		
		for(Image i : all) {
			
			if(image.getID() == i.getID()) {
				return i; 
			}
		}
		
		return null;
	}
	

	/**public List<Image> getSampleImage()
	 * @return get first image in a project
	 * @throws DatabaseException 
	 */
	public Image getSampleImage(Project project) throws DatabaseException {
		
		List<Image> images = getAll();
		
		for(Image i : images) {
			if(i.getAssociatedProject() == project.getID())
				return i;
		}
		return null;
	}
	
	/**public void getImage()
	 * @return first available image
	 * @throws DatabaseException 
	 */
	public Image getImage(Project project, User user) throws DatabaseException {
		
		List<Image> images = getAll();
		
		for(Image i : images) {
			if(i.getAssociatedProject() == project.getID() && i.getStatus() == 0) {
				i.setStatus(1);
				update(i);
				DAuser DAUser = new DAuser(db);
				user.setCurrentBatch(i.getFile());
				DAUser.update(user);
				return i;
			}
		}
		return null;
	}
	
	/**public void update(Image image)
	 * updates the information of a image in the database
	 * @param image
	 * @throws DatabaseException 
	 */
	public void update(Image image) throws DatabaseException {
		
		PreparedStatement stmt = null;
		try {
			String query = "update image set file = ?, associatedproject = ?, status = ? where id = ?";
			stmt = db.getConnection().prepareStatement(query);
			stmt.setString(1, image.getFile());
			stmt.setInt(2, image.getAssociatedProject());
			stmt.setInt(3, image.getStatus());
			stmt.setInt(4, image.getID());
			if (stmt.executeUpdate() != 1) {
				throw new DatabaseException("Could not update image");
			}
		}
		catch (SQLException e) {
			throw new DatabaseException("Could not update image", e);
		}
		finally {
			Database.safeClose(stmt);
		}
	}
	
	/**
	 * deletes a specifed image from the database
	 * @param image
	 * @throws DatabaseException
	 */
	public void delete(Image image) throws DatabaseException {
		
		PreparedStatement stmt = null;
		try {
			String query = "delete from image where id = ?";
			stmt = db.getConnection().prepareStatement(query);
			stmt.setInt(1, image.getID());
			if (stmt.executeUpdate() != 1) {
				throw new DatabaseException("Could not delete image");
			}
		}
		catch (SQLException e) {
			throw new DatabaseException("Could not delete image", e);
		}
		finally {
			Database.safeClose(stmt);
		}
	}

}
