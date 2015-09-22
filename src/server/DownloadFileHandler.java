package server;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.Path;
import com.sun.net.httpserver.*;

public class DownloadFileHandler implements HttpHandler {


	@Override
	public void handle(HttpExchange exchange) throws IOException {
		
		String s = "." +exchange.getRequestURI().toString();
		Path p = Paths.get(s);
		byte[] b = Files.readAllBytes(p);
		exchange.sendResponseHeaders(200, b.length);
		exchange.getResponseBody().write(b);
		exchange.close();
	}
}