package test;

import java.util.*;

import static org.junit.Assert.*;
import model.Field;
import model.Project;

import org.junit.*;

import dbaccess.*;

public class DAfieldTest {

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
	private DAfield DAField;
	
	@Before
	public void setUp() throws Exception {

		// Delete all contacts from the database	
		db = new Database();		
		db.startTransaction();
		
		List<Field> fields = db.getDAField().getAll();
		
		for (Field p : fields) {
			db.getDAField().delete(p);
		}
		
		db.endTransaction(true);

		// Prepare database for test case	
		db = new Database();
		db.startTransaction();
		DAField = db.getDAField();
	}

	@After
	public void tearDown() throws Exception {
		
		// Roll back transaction so changes to database are undone
		db.endTransaction(false);
		
		db = null;
		DAField = null;
	}

	@Test
	public void testGetAllFields() throws DatabaseException {
		
		List<Field> all = DAField.getAll();
		assertEquals(0, all.size());
	}
	
	@Test
	public void testGetAllFieldsForProject() throws DatabaseException {
		
		Field one = new Field(-1, "first", 20, 30, "help", "what", 1, 9);
		Field two = new Field(-1, "second", 20, 30, "no", "there", 1, 4);
		Field three = new Field(-1, "third", 20, 30, "no", "there", 3, 4);
		
		DAField.add(one);
		DAField.add(two);
		DAField.add(three);
		
		Project project = new Project(1, "1900", 7, 100, 200);
		
		List<Field> result = DAField.getAllFieldsForProject(project);
		
		assertTrue(areEqual(result.get(0), one, false));
		assertTrue(areEqual(result.get(1), two, false));
		assertTrue(result.size() == 2);
	}

	@Test
	public void testAdd() throws DatabaseException {
		
		Field one = new Field(-1, "first", 20, 30, "help", "what", 8, 9);
		Field two = new Field(-1, "second", 20, 30, "no", "there", 2, 4);
		
		DAField.add(one);
		DAField.add(two);
		
		List<Field> all = DAField.getAll();
		assertEquals(2, all.size());
		
		boolean foundOne = false;
		boolean foundTwo = false;
		
		for (Field p : all) {
			
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
	public void testUpdate() throws DatabaseException {

		Field one = new Field(-1, "first", 20, 30, "help", "what", 8, 9);
		Field two = new Field(-1, "second", 20, 30, "no", "there", 2, 4);
		
		DAField.add(one);
		DAField.add(two);
		
		one.setTitle("third");
		one.setXcoord(14);
		one.setWidth(150);
		one.setHelphtml("imnot");
		one.setKnownData("touche");
		one.setAssociatedProject(4);
		one.setNumber(9);
		
		two.setTitle("fifth");
		two.setXcoord(14);
		two.setWidth(150);
		two.setHelphtml("nothingmore");
		two.setKnownData("yay");
		two.setAssociatedProject(88);
		two.setNumber(90);
		
		DAField.update(one);
		DAField.update(two);
		
		List<Field> all = DAField.getAll();
		assertEquals(2, all.size());
		
		boolean foundOne = false;
		boolean foundTwo = false;
		
		for (Field p : all) {
			
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
	public void testDelete() throws DatabaseException {
		
		Field one = new Field(-1, "first", 20, 30, "help", "what", 8, 9);
		Field two = new Field(-11, "second", 20, 30, "no", "there", 2, 4);
		
		DAField.add(one);
		DAField.add(two);
		
		List<Field> all = DAField.getAll();
		assertEquals(2, all.size());
		
		DAField.delete(one);
		DAField.delete(two);
		
		all = DAField.getAll();
		assertEquals(0, all.size());
	}

	private boolean areEqual(Field a, Field b, boolean compareIDs) {
		if (compareIDs) {
			if (a.getID() != b.getID()) {
				return false;
			}
		}	
		return (safeEquals(a.getTitle(), b.getTitle()) &&
				safeEquals(a.getTitle(), b.getTitle()) &&
				safeEquals(a.getXcoord(), b.getXcoord()) &&
				safeEquals(a.getWidth(), b.getWidth()) &&
				safeEquals(a.getHelphtml(), b.getHelphtml()) &&
				safeEquals(a.getKnownData(), b.getKnownData()) &&
				safeEquals(a.getAssociatedProject(), b.getAssociatedProject()) &&
				safeEquals(a.getNumber(), b.getNumber()));
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
