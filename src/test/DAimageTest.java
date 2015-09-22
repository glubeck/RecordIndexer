package test;

import java.util.*;

import static org.junit.Assert.*;
import model.Image;
import model.Project;
import model.User;

import org.junit.*;

import dbaccess.*;

public class DAimageTest {

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
	private DAimage DAImage;
	
	@Before
	public void setUp() throws Exception {

		// Delete all contacts from the database	
		db = new Database();		
		db.startTransaction();
		
		List<Image> images = db.getDAImage().getAll();
		
		for (Image p : images) {
			db.getDAImage().delete(p);
		}
		
		db.endTransaction(true);

		// Prepare database for test case	
		db = new Database();
		db.startTransaction();
		DAImage = db.getDAImage();
	}

	@After
	public void tearDown() throws Exception {
		
		// Roll back transaction so changes to database are undone
		db.endTransaction(false);
		
		db = null;
		DAImage = null;
	}

	@Test
	public void testGetAllImages() throws DatabaseException {
		
		List<Image> all = DAImage.getAll();
		assertEquals(0, all.size());
	}

	@Test
	public void testAdd() throws DatabaseException {
		
		Image one = new Image(-1, "1900", 8, 2);
		Image two = new Image(-1, "1800", 4, 1);
		
		DAImage.add(one);
		DAImage.add(two);
		
		List<Image> all = DAImage.getAll();
		assertEquals(2, all.size());
		
		boolean foundOne = false;
		boolean foundTwo = false;
		
		for (Image p : all) {
			
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
	public void testGetSampleImage() throws DatabaseException {
		
		Image one = new Image(-1, "1900", 4, 2);
		Image two = new Image(-1, "1800", 4, 1);
		
		DAImage.add(one);
		DAImage.add(two);
		
		Project project = new Project(4, "1900", 7, 100, 200);
		
		Image other = DAImage.getSampleImage(project);
		
		assertTrue(project.getID() == other.getAssociatedProject());
	}
	
	@Test
	public void testGetImage() throws DatabaseException {
		
		Image one = new Image(-1, "1900", 4, 1);
		Image two = new Image(-1, "1800", 4, 1);
		Image three = new Image(-1, "2000", 4, 0);
		Image four = new Image(-1, "1850", 4, 1);
		
		DAImage.add(one);
		DAImage.add(two);
		DAImage.add(three);
		DAImage.add(four);
		
		Project project = new Project(4, "1900", 7, 100, 200);
		User john = new User(-1, "john", "password", "john", "doe", "jdoe@gmail.com", 8, "1800");
		DAuser DAUser = new DAuser(db);
		DAUser.add(john);
		
		Image other = DAImage.getImage(project, john);
		
		three.setStatus(1);
		assertTrue(areEqual(other, three, false));
		assertTrue(john.getCurrentBatch().equals(other.getFile()));
		
	}

	@Test
	public void testUpdate() throws DatabaseException {

		Image one = new Image(-1, "1900", 8, 2);
		Image two = new Image(-1, "1800", 4, 1);
		
		DAImage.add(one);
		DAImage.add(two);
		
		one.setFile("1950");
		one.setAssociatedProject(14);
		one.setStatus(0);
		
		two.setFile("2000");
		two.setAssociatedProject(3);
		two.setStatus(2);	
		
		DAImage.update(one);
		DAImage.update(two);
		
		List<Image> all = DAImage.getAll();
		assertEquals(2, all.size());
		
		boolean foundOne = false;
		boolean foundTwo = false;
		
		for (Image p : all) {
			
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
		
		Image one = new Image(-1, "1900", 8, 2);
		Image two = new Image(-1, "1800", 4, 1);
		
		DAImage.add(one);
		DAImage.add(two);
		
		List<Image> all = DAImage.getAll();
		assertEquals(2, all.size());
		
		DAImage.delete(one);
		DAImage.delete(two);
		
		all = DAImage.getAll();
		assertEquals(0, all.size());
	}

	private boolean areEqual(Image a, Image b, boolean compareIDs) {
		if (compareIDs) {
			if (a.getID() != b.getID()) {
				return false;
			}
		}	
		return (safeEquals(a.getFile(), b.getFile()) &&
				safeEquals(a.getAssociatedProject(), b.getAssociatedProject()) &&
				safeEquals(a.getStatus(), b.getStatus()));
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
