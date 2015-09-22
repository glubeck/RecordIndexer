package test;

import java.sql.SQLException;
import java.util.*;

import static org.junit.Assert.*;
import model.Record;

import org.junit.*;

import dbaccess.*;

public class DArecordTest {

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
	private DArecord DARecord;
	
	@Before
	public void setUp() throws Exception {

		// Delete all contacts from the database	
		db = new Database();		
		db.startTransaction();
		
		List<Record> records = db.getDARecord().getAll();
		
		for (Record p : records) {
			db.getDARecord().delete(p);
		}
		
		db.endTransaction(true);

		// Prepare database for test case	
		db = new Database();
		db.startTransaction();
		DARecord = db.getDARecord();
	}

	@After
	public void tearDown() throws Exception {
		
		// Roll back transaction so changes to database are undone
		db.endTransaction(false);
		
		db = null;
		DARecord = null;
	}

	@Test
	public void testGetAllRecords() throws DatabaseException, SQLException {
		
		List<Record> all = DARecord.getAll();
		assertEquals(0, all.size());
	}

	@Test
	public void testAdd() throws DatabaseException, SQLException {
		
		Record one = new Record(-1, "1900", 7, 100, 200);
		Record two = new Record(-1, "1800", 8, 200, 300);
		
		DARecord.add(one);
		DARecord.add(two);
		
		List<Record> all = DARecord.getAll();
		assertEquals(2, all.size());
		
		boolean foundOne = false;
		boolean foundTwo = false;
		
		for (Record p : all) {
			
			assertFalse(p.getID() == -1);
			
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

		Record one = new Record(-1, "1900", 7, 100, 200);
		Record two = new Record(-1, "1800", 8, 200, 300);
		
		DARecord.add(one);
		DARecord.add(two);
		
		one.setValue("1950");
		one.setAssociatedImage(14);
		one.setFieldID(150);
		one.setRowNum(250);
		
		two.setValue("1850");
		two.setAssociatedImage(16);
		two.setFieldID(250);
		two.setRowNum(350);
		
		DARecord.update(one);
		DARecord.update(two);
		
		List<Record> all = DARecord.getAll();
		assertEquals(2, all.size());
		
		boolean foundOne = false;
		boolean foundTwo = false;
		
		for (Record p : all) {
			
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
		
		Record one = new Record(-1, "1900", 7, 100, 200);
		Record two = new Record(-1, "1800", 8, 200, 300);
		
		DARecord.add(one);
		DARecord.add(two);
		
		List<Record> all = DARecord.getAll();
		assertEquals(2, all.size());
		
		DARecord.delete(one);
		DARecord.delete(two);
		
		all = DARecord.getAll();
		assertEquals(0, all.size());
	}

	private boolean areEqual(Record a, Record b, boolean compareIDs) {
		if (compareIDs) {
			if (a.getID() != b.getID()) {
				return false;
			}
		}	
		return (safeEquals(a.getValue(), b.getValue()) &&
				safeEquals(a.getAssociatedImage(), b.getAssociatedImage())) &&
				safeEquals(a.getFieldID(), b.getFieldID()) &&
				safeEquals(a.getRowNum(), b.getRowNum());
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
