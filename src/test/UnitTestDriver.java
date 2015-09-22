package test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.rmi.ServerException;
import java.sql.SQLException;
import java.util.List;

import server.Facade;
import client.*;
import model.*;
import dbaccess.*;

public class UnitTestDriver {

	public static void main(String[] args) throws DatabaseException, SQLException, ServerException, ClientException {

		
		
		
		String[] testClasses = new String[] {
				"test.DAprojectTest",
				"test.DArecordTest",
				"test.DAcellTest",
				"test.DAfieldTest",
				"test.DAimageTest",
				//"test.DASearchTest",
				"test.DAuserTest",
				"test.ClientCommunicatorTest"
		};

		org.junit.runner.JUnitCore.main(testClasses);
		
//		ClientCommunicator c = new ClientCommunicator();
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
		
	}
}

