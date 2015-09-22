package spell;

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
				"spell.SpellTest",
		};

		org.junit.runner.JUnitCore.main(testClasses);
		

	}
}

