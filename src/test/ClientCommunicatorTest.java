package test;

import java.sql.SQLException;
import java.util.*;

import static org.junit.Assert.*;
import model.Cell;
import model.Image;
import model.Project;
import model.User;
import server.Server;

import org.junit.*;

import client.*;
import dataimporter.DataImporter;
import dbaccess.*;

public class ClientCommunicatorTest {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {	
		// Load database driver	
		String[] args = new String[1];
		args[0] = "8083";
		Server.main(args);
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		return;
	}
	
	private ClientCommunicator c;
	
	@Before
	public void setUp() throws Exception {

		// Delete all contacts from the database	
		c = new ClientCommunicator();
		String[] args = new String[1];
		args[0] = "ImportFrom/Records.xml";
		
		DataImporter.main(args);
	
	}

	@After
	public void tearDown() throws Exception {
		
	}
	
//	@Test
//	public void testValidateUser() throws ClientException {
//		
//		User user1 = new User("Sheila", "Parker");
//		ValidateUser_Params params1 = new ValidateUser_Params(user1);
//		ValidateUser_Result result1 = c.ValidateUser(params1);
//		
//		assertTrue(result1.getUser() != null);
//		
//		User user2 = new User("Sheil", "Parke");
//		ValidateUser_Params params2 = new ValidateUser_Params(user2);
//		ValidateUser_Result result2 = c.ValidateUser(params2);
//		
//		assertTrue(result2.getUser() == null);
//	}
	
	@Test
	public void testGetProjects() throws ClientException {
		

		User user1 = new User("Sheila", "Parker");
		GetProjects_Params params1 = new GetProjects_Params(user1);
		GetProjects_Result result1 = c.GetProjects(params1);
		
		assertTrue(result1.isValid());
		
		User user2 = new User("Sheil", "Parke");
		GetProjects_Params params2 = new GetProjects_Params(user2);
		GetProjects_Result result2 = c.GetProjects(params2);
		
		assertFalse(result2.isValid());
	}
	
	@Test
	public void getSampleImage() throws ClientException {
		
		User user1 = new User("Sheila", "Parker");
		int id = 1;
		Project project1 = new Project(id);
		GetSampleImage_Params params1 = new GetSampleImage_Params(user1, project1);
		GetSampleImage_Result result1 = c.GetSampleImage(params1);
		
		assertTrue(result1.isValid());
		
		User user2 = new User("Sheil", "Parke");
		Project project2 = new Project(id);
		GetSampleImage_Params params2 = new GetSampleImage_Params(user2, project2);
		GetSampleImage_Result result2 = c.GetSampleImage(params2);
		
		assertFalse(result2.isValid());
		

		int id3 = 9;
		Project project3 = new Project(id3);
		GetSampleImage_Params params3 = new GetSampleImage_Params(user1, project3);
		GetSampleImage_Result result3 = c.GetSampleImage(params3);
		
		assertTrue(result3.getImage() == null);
		
	}
	
	@Test
	public void DownloadBatchTest() throws ClientException {
		
		User user1 = new User("Sheila", "Parker");
		int id = 1;
		Project project1 = new Project(id);
		DownloadBatch_Params params1 = new DownloadBatch_Params(user1, project1);
		DownloadBatch_Result result1 = c.DownloadBatch(params1);
		
		assertTrue(result1.getFields() != null);
		assertTrue(result1.getImage() != null);
		assertTrue(result1.getProject() != null);
		
		User user2 = new User("Sheil", "Parke");
		int id2 = 2;
		Project project2 = new Project(id2);
		DownloadBatch_Params params2 = new DownloadBatch_Params(user2, project2);
		DownloadBatch_Result result2 = c.DownloadBatch(params2);
		
		assertTrue(result2 == null);
		
		User user3 = new User("Sheila", "Parker");
		int id3 = 10;
		Project project3 = new Project(id3);
		DownloadBatch_Params params3 = new DownloadBatch_Params(user3, project3);
		DownloadBatch_Result result3 = c.DownloadBatch(params3);
		
		assertFalse(result3.isValid());
				
	}
	
	@Test
	public void getFieldsTest() throws ClientException {
		
		User user1 = new User("Sheila", "Parker");
		int id = 1;
		Project project1 = new Project(id);
		GetFields_Params params1 = new GetFields_Params(user1, project1);
		GetFields_Result result1 = c.GetFields(params1);
		
		assertTrue(result1.getFields() != null);
		
		User user2 = new User("Sheil", "Parke");
		int id2 = 2;
		Project project2 = new Project(id2);
		GetFields_Params params2 = new GetFields_Params(user2, project2);
		GetFields_Result result2 = c.GetFields(params2);
		
		assertFalse(result2.isValid());
		
		User user3 = new User("Sheila", "Parker");
		int id3 = 10;
		Project project3 = new Project(id3);
		GetFields_Params params3 = new GetFields_Params(user3, project3);
		GetFields_Result result3 = c.GetFields(params3);
		
		assertFalse(result3.isValid());
	}
	
	@Test
	public void submitBatchTest() throws ClientException {
		
		User user = new User("Sheila", "Parker");
		int batchID = 21;
		Image image = new Image(batchID);
		String RecordValues = "John,Baily,19;Jane,Doe,22;Gordon,Doe,28";
		SubmitBatch_Params params = new SubmitBatch_Params(user, image, RecordValues);
		SubmitBatch_Result result1 = c.SubmitBatch(params);
		
		assertFalse(result1.isSuccess());
		
		User user2 = new User("Sheila", "Parker");
		int batchID2 = 21;
		Image image2 = new Image(batchID2);
		String RecordValues2 = "John,Baily,19;Jane,Doe,22;Gordon,Doe";
		SubmitBatch_Params params2 = new SubmitBatch_Params(user2, image2, RecordValues2);
		SubmitBatch_Result result2 = c.SubmitBatch(params2);
		
		assertFalse(result2.isSuccess());
	}
	
	@Test
	public void searchTest() throws ClientException {
		
		User user = new User("Sheila", "Parker");
		String fields = "12";
		String values = "19";
		
		Search_Params params = new Search_Params(user, fields, values);
		Search_Result result = c.Search(params);
		
		assertTrue(result.isValid());
		
		User user2 = new User("Sheil", "Parker");
		String fields2 = "12";
		String values2 = "19";
		
		Search_Params params2 = new Search_Params(user2, fields2, values2);
		Search_Result result2 = c.Search(params2);
		
		assertFalse(result2.isValid());
	}
	
}
