package test;

import java.sql.SQLException;
import java.util.*;

import static org.junit.Assert.*;
import model.User;

import org.junit.*;

import dbaccess.*;

public class DAuserTest {

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
	private DAuser DAUser;
	
	@Before
	public void setUp() throws Exception {

		// Delete all contacts from the database	
		db = new Database();		
		db.startTransaction();
		
		List<User> users = db.getDAUser().getAll();
		
		for (User p : users) {
			db.getDAUser().delete(p);
		}
		
		db.endTransaction(true);

		// Prepare database for test case	
		db = new Database();
		db.startTransaction();
		DAUser = db.getDAUser();
	}

	@After
	public void tearDown() throws Exception {
		
		// Roll back transaction so changes to database are undone
		db.endTransaction(false);
		
		db = null;
		DAUser = null;
	}

	@Test
	public void testGetAllUsers() throws DatabaseException {
		
		List<User> all = DAUser.getAll();
		assertEquals(0, all.size());
	}

	@Test
	public void testAdd() throws DatabaseException {
		
		User one = new User(-1, "john", "password", "john", "doe", "jdoe@gmail.com", 8, "1800");
		User two = new User(-1, "jane", "password", "jane", "doe", "jdoe@gmail.com", 200, "1900");
		
		DAUser.add(one);
		DAUser.add(two);
		
		List<User> all = DAUser.getAll();
		assertEquals(2, all.size());
		
		boolean foundOne = false;
		boolean foundTwo = false;
		
		for (User p : all) {
			
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
	public void testCheckUser() throws DatabaseException, SQLException {
		
		User one = new User(-1, "john", "password", "john", "doe", "jdoe@gmail.com", 8, "1800");
		User two = new User(-1, "jane", "password", "jane", "doe", "jdoe@gmail.com", 200, "1900");
		
		DAUser.add(one);
		DAUser.add(two);
		
		User other = DAUser.checkUser(one);
		
		assertTrue(areEqual(one, other, true));
	}
	
	@Test
	public void testUpdate() throws DatabaseException {

		User one = new User(-1, "john", "password", "john", "doe", "jdoe@gmail.com", 8, "1800");
		User two = new User(-1, "jane", "password", "jane", "doe", "jdoe@gmail.com", 200, "1900");
		
		DAUser.add(one);
		DAUser.add(two);
		
		one.setUsername("1950");
		one.setPassword("argh");
		one.setFirstname("blah");
		one.setLastname("foo");
		one.setEmail("noway");
		one.setIndexedRecords(1);
		one.setCurrentBatch("2000");
		
		two.setUsername("2950");
		two.setPassword("nuts");
		two.setFirstname("wat");
		two.setLastname("uh");
		two.setEmail("mmmmm");
		two.setIndexedRecords(1000);
		two.setCurrentBatch("2222");
		
		DAUser.update(one);
		DAUser.update(two);
		
		List<User> all = DAUser.getAll();
		assertEquals(2, all.size());
		
		boolean foundOne = false;
		boolean foundTwo = false;
		
		for (User p : all) {
			
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
		
		User one = new User(-1, "john", "password", "john", "doe", "jdoe@gmail.com", 8, "1800");
		User two = new User(-1, "jane", "password", "jane", "doe", "jdoe@gmail.com", 200, "1900");
		
		DAUser.add(one);
		DAUser.add(two);
		
		List<User> all = DAUser.getAll();
		assertEquals(2, all.size());
		
		DAUser.delete(one);
		DAUser.delete(two);
		
		all = DAUser.getAll();
		assertEquals(0, all.size());
	}

	private boolean areEqual(User a, User b, boolean compareIDs) {
		if (compareIDs) {
			if (a.getID() != b.getID()) {
				return false;
			}
		}	
		return (safeEquals(a.getUsername(), b.getUsername()) &&
				safeEquals(a.getPassword(), b.getPassword()) &&
				safeEquals(a.getFirstname(), b.getFirstname()) &&
				safeEquals(a.getLastname(), b.getLastname()) &&
				safeEquals(a.getEmail(), b.getEmail()) &&
				safeEquals(a.getIndexedRecords(), b.getIndexedRecords()) &&
				safeEquals(a.getCurrentBatch(), b.getCurrentBatch()));
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
