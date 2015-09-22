package test;

import java.util.*;

import static org.junit.Assert.*;
import model.Project;

import org.junit.*;

import dbaccess.*;

public class DAprojectTest {

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
	private DAproject DAProject;
	
	@Before
	public void setUp() throws Exception {

		// Delete all contacts from the database	
		db = new Database();		
		db.startTransaction();
		
		List<Project> projects = db.getDAProject().getAllProjects();
		
		for (Project p : projects) {
			db.getDAProject().delete(p);
		}
		
		db.endTransaction(true);

		// Prepare database for test case	
		db = new Database();
		db.startTransaction();
		DAProject = db.getDAProject();
	}

	@After
	public void tearDown() throws Exception {
		
		// Roll back transaction so changes to database are undone
		db.endTransaction(false);
		
		db = null;
		DAProject = null;
	}

	@Test
	public void testGetAllProjects() throws DatabaseException {
		
		List<Project> all = DAProject.getAllProjects();
		assertEquals(0, all.size());
	}

	@Test
	public void testAdd() throws DatabaseException {
		
		Project one = new Project(-1, "1900", 7, 100, 200);
		Project two = new Project(-1, "1800", 8, 200, 300);
		
		DAProject.add(one);
		DAProject.add(two);
		
		List<Project> all = DAProject.getAllProjects();
		assertEquals(2, all.size());
		
		boolean foundOne = false;
		boolean foundTwo = false;
		
		for (Project p : all) {
			
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

		Project one = new Project(-1, "1900", 7, 100, 200);
		Project two = new Project(-1, "1800", 8, 200, 300);
		
		DAProject.add(one);
		DAProject.add(two);
		
		one.setTitle("1950");
		one.setRecordsPerImage(14);
		one.setFirstYCoord(150);
		one.setRecordHeight(250);
		
		two.setTitle("1850");
		two.setRecordsPerImage(16);
		two.setFirstYCoord(250);
		two.setRecordHeight(350);
		
		DAProject.update(one);
		DAProject.update(two);
		
		List<Project> all = DAProject.getAllProjects();
		assertEquals(2, all.size());
		
		boolean foundOne = false;
		boolean foundTwo = false;
		
		for (Project p : all) {
			
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
		
		Project one = new Project(-1, "1900", 7, 100, 200);
		Project two = new Project(-1, "1800", 8, 200, 300);
		
		DAProject.add(one);
		DAProject.add(two);
		
		List<Project> all = DAProject.getAllProjects();
		assertEquals(2, all.size());
		
		DAProject.delete(one);
		DAProject.delete(two);
		
		all = DAProject.getAllProjects();
		assertEquals(0, all.size());
	}

	private boolean areEqual(Project a, Project b, boolean compareIDs) {
		if (compareIDs) {
			if (a.getID() != b.getID()) {
				return false;
			}
		}	
		return (safeEquals(a.getTitle(), b.getTitle()) &&
				safeEquals(a.getRecordsPerImage(), b.getRecordsPerImage()) &&
				safeEquals(a.getFirstYCoord(), b.getFirstYCoord()) &&
				safeEquals(a.getRecordHeight(), b.getRecordHeight()));
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
