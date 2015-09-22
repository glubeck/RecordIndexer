package client;

import model.*;

/**
 * contains image information for the result of the
 * GetSampleImage method of the Client Communicator class
 * @author Grant
 *
 */
public class GetSampleImage_Result {

	private Image image;
	private boolean valid = true;

	private String URL_PATH;
	
	public GetSampleImage_Result(Image image) {
		this.image = image;
	}
	
	public String toString() {

		StringBuilder sb = new StringBuilder(URL_PATH + "/database/Records/" + image.getFile());
		
		return sb.toString();
	}

	public Image getImage() {
		return image;
	}

	public void setImage(Image image) {
		this.image = image;
	}
	
	public boolean isValid() {
		return valid;
	}

	public void setValid(boolean valid) {
		this.valid = valid;
	}
	
	public String getURL_PATH() {
		return URL_PATH;
	}

	public void setURL_PATH(String uRL_PATH) {
		URL_PATH = uRL_PATH;
	}


}
