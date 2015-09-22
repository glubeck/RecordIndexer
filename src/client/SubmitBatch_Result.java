package client;

/**
 * contains the boolean information for the result of the SubmitBatch
 * method of the ClientCommunicator class
 * @author Grant
 *
 */
public class SubmitBatch_Result {

	private boolean success;
	
	private String URL_PATH;
	
	public SubmitBatch_Result(boolean success) {
		this.success = success;
	}
	
	public String toString() {
		
		return "TRUE";
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}
	public String getURL_PATH() {
		return URL_PATH;
	}

	public void setURL_PATH(String uRL_PATH) {
		URL_PATH = uRL_PATH;
	}


	
	
	
}
