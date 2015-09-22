package server;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.rmi.ServerException;
import java.sql.SQLException;
import java.util.logging.*;

import com.sun.net.httpserver.*;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

import client.*;
import dbaccess.DatabaseException;

public class SearchHandler implements HttpHandler {

	private Logger logger = Logger.getLogger("contactmanager"); 
	
	private XStream xmlStream = new XStream(new DomDriver());	

	@Override
	public void handle(HttpExchange exchange) throws IOException {
		
		Search_Params params = (Search_Params)xmlStream.fromXML(exchange.getRequestBody());
		
		try {
			Search_Result result = Facade.search(params);
			exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
			xmlStream.toXML(result, exchange.getResponseBody());
		}
		catch (ServerException | DatabaseException | SQLException e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
			exchange.sendResponseHeaders(HttpURLConnection.HTTP_INTERNAL_ERROR, 0);
			return;
		}
		exchange.getResponseBody().close();
	}
}