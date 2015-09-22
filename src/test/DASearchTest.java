package test;

import java.rmi.ServerException;
import java.sql.SQLException;
import java.util.*;

import static org.junit.Assert.*;
import model.Cell;
import model.User;

import org.junit.*;

import server.Facade;
import client.Search_Params;
import client.Search_Result;
import dbaccess.*;

public class DASearchTest {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {	
		// Load database driver	
		Database.initialize();
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		return;
	}
	
	private Database db;
	
	@Before
	public void setUp() throws Exception {

		// Delete all contacts from the database	
		db = new Database();		
		db.startTransaction();
		

		
		db.endTransaction(false);

		// Prepare database for test case	
		db = new Database();
		db.startTransaction();
	}

	@After
	public void tearDown() throws Exception {
		
		// Roll back transaction so changes to database are undone
		db.endTransaction(false);
		
		db = null;
	}

	@Test
	public void testSearch() throws DatabaseException, ServerException, SQLException {
		Database.initialize();
		Database db = new Database();
		db.startTransaction();
		
		User john = new User(-1, "john", "password", "john", "doe", "jdoe@gmail.com", 8, "1800");

		int[] fields = new int[5];
		for(int i = 0; i < fields.length; i++)
			fields[i] = i;
		String[] values = new String[5];
		for(int i = 0; i < values.length; i++)
			values[i] = "fine";
		
		Search_Params params = new Search_Params(john, fields, values);
		Facade facade = new Facade();
		Search_Result result = facade.search(params);
	}
	
}
