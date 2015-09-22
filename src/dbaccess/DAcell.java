package dbaccess;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import model.Cell;

public class DAcell {
	
	private static Logger logger;
	
	static {
		logger = Logger.getLogger("");
	}
	
	private Database db;
	//you should be private yo
	public DAcell(Database db) {
		this.db = db;
	}
	
	/**public void add(Cell cell) 
	 * adds new cell to the database
	 * @param cell
	 * @throws DatabaseException 
	 */
	public void add(Cell cell) throws DatabaseException {
		
		PreparedStatement stmt = null;
		ResultSet keyRS = null;		
		try {
			String query = "INSERT INTO cell(rownum, parentid, fieldid, value) VALUES(?, ?, ?, ?)";
			stmt = db.getConnection().prepareStatement(query);
			int temp = cell.getRowNum();
			stmt.setInt(1, cell.getRowNum());
			int temp2 = cell.getParentID();
			stmt.setInt(2, cell.getParentID());
			int temp3 = cell.getFieldID();
			stmt.setInt(3, cell.getFieldID());
			String temp4 = cell.getValue();
			stmt.setString(4, cell.getValue());
			if (stmt.executeUpdate() == 1) {
				Statement keyStmt = db.getConnection().createStatement();
				keyRS = keyStmt.executeQuery("select last_insert_rowid()");
				keyRS.next();
				int id = keyRS.getInt(1);
				cell.setId(id);
			}
			else {
				throw new DatabaseException("Could not insert cell");
			}
		}
		catch(SQLException e) {
			throw new DatabaseException("Could not insert cell", e);
		}
		finally {
			Database.safeClose(stmt);
			Database.safeClose(keyRS);
		}
	}
	
	/**
	 * Updates an existing cell in the database
	 * @param cell
	 * @throws DatabaseException 
	 */
	public void update(Cell cell) throws DatabaseException {
		
		PreparedStatement stmt = null;
		try {
			String query = "update cell set fieldid = ?, value = ? where parentid = ?";
			stmt = db.getConnection().prepareStatement(query);
			stmt.setInt(1, cell.getFieldID());
			stmt.setString(2, cell.getValue());
			stmt.setInt(3, cell.getParentID());
			if (stmt.executeUpdate() != 1) {
				throw new DatabaseException("Could not update cell");
			}
		}
		catch (SQLException e) {
			throw new DatabaseException("Could not update cell", e);
		}
		finally {
			Database.safeClose(stmt);
		}
	}

	/**
	 * Gets a list of cells in order to access data in the cell table
	 * @return
	 * @throws DatabaseException 
	 */
	public List<Cell> getAll() throws DatabaseException {
		
	logger.entering("dbaccess.DAcell", "getAll");
		
		ArrayList<Cell> result = new ArrayList<Cell>();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			String query = "select id, rownum, parentid, fieldid, value from cell";
			stmt = db.getConnection().prepareStatement(query);

			rs = stmt.executeQuery();
			while (rs.next()) {
				int id = rs.getInt(1);
				int rownum = rs.getInt(2);
				int parentid = rs.getInt(3);
				int fieldid = rs.getInt(4);
				String value = rs.getString(5);
				
				result.add(new Cell(id, rownum, parentid, fieldid, value));
			}
		}
		catch (SQLException e) {
			DatabaseException serverEx = new DatabaseException(e.getMessage(), e);
			
			logger.throwing("server.database.Cells", "getAll", serverEx);
			
			throw serverEx;
		}		
		finally {
			Database.safeClose(rs);
			Database.safeClose(stmt);
		}

		logger.exiting("server.database.Cells", "getAll");
		
		return result;	
	}
	
	public void deleteAll() throws DatabaseException, SQLException {
		
		List<Cell> all = getAll();
		
		for(Cell c : all) {
			delete(c);
		}
	}
	
	/**
	 * deletes a specified cell from the database
	 * @param cell
	 * @throws DatabaseException
	 */
	public void delete(Cell cell) throws DatabaseException {
		
		PreparedStatement stmt = null;
		try {
			String query = "delete from cell where id = ?";
			stmt = db.getConnection().prepareStatement(query);
			stmt.setInt(1, cell.getId());
			if (stmt.executeUpdate() != 1) {
				throw new DatabaseException("Could not delete cell");
			}
		}
		catch (SQLException e) {
			throw new DatabaseException("Could not delete cell", e);
		}
		finally {
			Database.safeClose(stmt);
		}
	}
}
