package test;

import java.sql.SQLException;
import java.util.*;

import static org.junit.Assert.*;
import model.Cell;

import org.junit.*;

import dbaccess.*;

public class DAcellTest {

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
	private DAcell DACell;
	
	@Before
	public void setUp() throws Exception {

		// Delete all contacts from the database	
		db = new Database();		
		db.startTransaction();
		
		List<Cell> cells = db.getDACell().getAll();
		
		for (Cell p : cells) {
			db.getDACell().delete(p);
		}
		
		db.endTransaction(true);

		// Prepare database for test case	
		db = new Database();
		db.startTransaction();
		DACell = db.getDACell();
	}

	@After
	public void tearDown() throws Exception {
		
		// Roll back transaction so changes to database are undone
		db.endTransaction(false);
		
		db = null;
		DACell = null;
	}

	@Test
	public void testGetAllCells() throws DatabaseException, SQLException {
		
		List<Cell> all = DACell.getAll();
		assertEquals(0, all.size());
	}

	@Test
	public void testAdd() throws DatabaseException, SQLException {
		
		Cell one = new Cell(-1, 1, 2, 3, "1800");
		Cell two = new Cell(-1, 1, 3, 6, "1900");
		
		DACell.add(one);
		DACell.add(two);
		
		List<Cell> all = DACell.getAll();
		assertEquals(2, all.size());
		
		boolean foundOne = false;
		boolean foundTwo = false;
		
		for (Cell p : all) {
			
			assertFalse(p.getId() == -1);
			
			if (!foundOne) {
				foundOne = areEqual(p, one, false);
			}
			if (!foundTwo) {
				foundTwo = areEqual(p, one, false);
			}
		}
		
		assertTrue(foundOne && foundTwo);
	}

	@Test
	public void testUpdate() throws DatabaseException, SQLException {

		Cell one = new Cell(-1, 1, 2, 3, "1800");
		Cell two = new Cell(-1, 1, 3, 6, "1900");
		
		DACell.add(one);
		DACell.add(two);
		
		one.setValue("1950");
		one.setFieldID(150);
		
		two.setValue("1850");
		two.setFieldID(250);
		
		DACell.update(one);
		DACell.update(two);
		
		List<Cell> all = DACell.getAll();
		assertEquals(2, all.size());
		
		boolean foundOne = false;
		boolean foundTwo = false;
		
		for (Cell p : all) {
			
			if(!foundOne) {
				foundOne = areEqual(p, one, false);
			}
			if (!foundTwo) {
				foundTwo = areEqual(p, two, false);
			}
		}
		assertTrue(foundOne && foundTwo);
	}
	
	@Test
	public void testDelete() throws DatabaseException, SQLException {
		
		Cell one = new Cell(-1, 1, 2, 3, "1800");
		Cell two = new Cell(-1, 1, 3, 6, "1900");
		
		DACell.add(one);
		DACell.add(two);
		
		List<Cell> all = DACell.getAll();
		assertEquals(2, all.size());
		
		DACell.delete(one);
		DACell.delete(two);
		
		all = DACell.getAll();
		assertEquals(0, all.size());
	}

	private boolean areEqual(Cell a, Cell b, boolean compareIDs) {
		if (compareIDs) {
			if (a.getId() != b.getId()) {
				return false;
			}
		}	
		return (safeEquals(a.getValue(), b.getValue()) &&
				safeEquals(a.getFieldID(), b.getFieldID()) &&
				safeEquals(a.getParentID(), b.getParentID()) &&
				safeEquals(a.getRowNum(), b.getRowNum()));
	}
	
	private boolean safeEquals(Object a, Object b) {
		if (a == null || b == null) {
			return (a == null && b == null);
		}
		else {
			return a.equals(b);
		}
	}
	
}
