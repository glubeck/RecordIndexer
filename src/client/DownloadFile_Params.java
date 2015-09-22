package client;

/**
 * contains the information for the file path for the parameters
 * of the downloadFile method of the ClientCommunicatorClass
 * @author Grant
 *
 */
public class DownloadFile_Params {

	private String path;

	public DownloadFile_Params(String path) {
		this.path = path;
	}
	
	public String toString() {
		return null;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}
	
	
}
