package client;

import java.util.List;

/**
 * contains the cell array for the result of the
 * search method of the ClientCommunicator class
 * @author Grant
 *
 */
public class Search_Result {

	private List<Integer> batchIDs;
	private List<String> ImageURLs;
	private List<Integer> recordNumbers;
	private List<Integer> FieldIDs;
	private boolean valid = true;
	
	private String URL_PATH;
	
	public Search_Result(List<Integer> batchIDs, List<String> imageURLs,
			List<Integer> recordNumbers, List<Integer> fieldIDs) {
		this.batchIDs = batchIDs;
		ImageURLs = imageURLs;
		this.recordNumbers = recordNumbers;
		FieldIDs = fieldIDs;
	}

	public String toString() {
		
		StringBuilder sb = new StringBuilder("");
		
		for(int i = 0; i < batchIDs.size(); i++) {
			sb.append(batchIDs.get(i) + "\n");
			sb.append(URL_PATH + "/database/Records/" + ImageURLs.get(i) + "\n");
			sb.append(recordNumbers.get(i) + "\n");
			sb.append(FieldIDs.get(i) + "\n");
		}
		
		return sb.toString();
	}

	public List<Integer> getBatchIDs() {
		return batchIDs;
	}

	public void setBatchIDs(List<Integer> batchIDs) {
		this.batchIDs = batchIDs;
	}

	public List<String> getImageURLs() {
		return ImageURLs;
	}

	public void setImageURLs(List<String> imageURLs) {
		ImageURLs = imageURLs;
	}

	public List<Integer> getRecordNumbers() {
		return recordNumbers;
	}

	public void setRecordNumbers(List<Integer> recordNumbers) {
		this.recordNumbers = recordNumbers;
	}

	public List<Integer> getFieldIDs() {
		return FieldIDs;
	}

	public void setFieldIDs(List<Integer> fieldIDs) {
		FieldIDs = fieldIDs;
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
