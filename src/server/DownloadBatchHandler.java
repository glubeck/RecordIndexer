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

public class DownloadBatchHandler implements HttpHandler {

	private Logger logger = Logger.getLogger("contactmanager"); 
	
	private XStream xmlStream = new XStream(new DomDriver());	

	@Override
	public void handle(HttpExchange exchange) throws IOException {
		
		DownloadBatch_Params params = (DownloadBatch_Params)xmlStream.fromXML(exchange.getRequestBody());
		
		try {
			DownloadBatch_Result result = Facade.downloadBatch(params);
			exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
			xmlStream.toXML(result, exchange.getResponseBody());
		}
		catch (ServerException | SQLException e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
			exchange.sendResponseHeaders(HttpURLConnection.HTTP_INTERNAL_ERROR, 0);
			return;
		}
		exchange.getResponseBody().close();
	}
}