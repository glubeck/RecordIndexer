package gui;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

import model.User;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

public class UserState {

	private final String USER_STATE_PATH = "database" + File.separator + "users";
	
	private XStream xmlStream = new XStream(new DomDriver());
	
	public UserState() {}
	
	public void save(BatchState bs, String username) throws FileNotFoundException {
		
		String fileName = USER_STATE_PATH + File.separator + username + ".xml";
		File f = new File(fileName);
		PrintWriter writer;
		try {
			writer = new PrintWriter(f, "UTF-8");
			xmlStream.toXML(bs, writer);
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public BatchState getStateForUser(User user) throws IOException {
		
		String filename = USER_STATE_PATH + File.separator + user.getUsername() + ".xml";
		File f = new File(filename);
		
		if(f.exists() && !f.isDirectory()) {
		
		Path path = FileSystems.getDefault().getPath(USER_STATE_PATH, user.getUsername() + ".xml");
		BufferedReader reader;
		
		reader = Files.newBufferedReader(path, StandardCharsets.UTF_8);
		
		BatchState batchState = (BatchState)xmlStream.fromXML(reader);
		
		return batchState;
		}
		
		BatchState batchState = new BatchState(user);
		return batchState;
	}
}
