package client;

import java.io.*;
import java.net.*;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

public class ClientCommunicator {

	
	private static String SERVER_HOST = "localhost";
	private static String SERVER_PORT = "8083";
	private static String URL_PREFIX = "http://" + SERVER_HOST + ":" + SERVER_PORT;
	private static String HTTP_GET = "GET";
	private static final String HTTP_POST = "POST";

	private XStream xmlStream;

	public static void setHostandPort(String host, String port) {
	
		SERVER_HOST = host;
		SERVER_PORT = port;
	}
	
	public ClientCommunicator() {
		xmlStream = new XStream(new DomDriver());
	}
	/**
	 * validates user credentials
	 * @param params
	 * @return ValidateUser_Result
	 * @throws ClientException 
	 */
	public ValidateUser_Result ValidateUser(ValidateUser_Params params) throws ClientException {
		
		return (ValidateUser_Result)doPost("/ValidateUser", params);
	}
	
	/**
	 * returns information about all of the available projects
	 * @param params
	 * @return GetProjects_Result
	 * @throws ClientException 
	 */
	public GetProjects_Result GetProjects(GetProjects_Params params) throws ClientException {
		return (GetProjects_Result)doPost("/GetProjects", params);
	}
	
	/**
	 * returns a sample image for the specified project
	 * @param params
	 * @return GetSampleImage_Result
	 * @throws ClientException 
	 */
	public GetSampleImage_Result GetSampleImage(GetSampleImage_Params params) throws ClientException {
		return (GetSampleImage_Result)doPost("/GetSampleImage", params);
	}
	
	/**
	 * downloads a batch for the user to index
	 * @param params
	 * @return DownloadBatch_Result
	 * @throws ClientException 
	 */
	public DownloadBatch_Result DownloadBatch(DownloadBatch_Params params) throws ClientException {
		return (DownloadBatch_Result)doPost("/DownloadBatch", params);
	}
	
	/**
	 * Submits the indexed record field values for a batch to the Server
	 * @param params
	 * @return SubmitBatch_Result
	 * @throws ClientException 
	 */
	public SubmitBatch_Result SubmitBatch(SubmitBatch_Params params) throws ClientException {
		return (SubmitBatch_Result)doPost("/SubmitBatch", params);
	}
	
	/**
	 * returns information about all of the fields for the specified project
	 * @param params
	 * @return getFields_Result
	 * @throws ClientException 
	 */
	public GetFields_Result GetFields(GetFields_Params params) throws ClientException {
		return (GetFields_Result)doPost("/GetFields", params);
	}
	
	/**
	 * Searches the indexed records for the specified strings
	 * @param params
	 * @return Search_Result
	 * @throws ClientException 
	 */
	public Search_Result Search(Search_Params params) throws ClientException {
		return (Search_Result)doPost("/Search", params);
	}
	
	/**
	 * Downloads a specified file
	 * @param params
	 * @return DownloadFile_Result
	 */
	public DownloadFile_Result DownloadFile(DownloadFile_Params params) {
		
		return null;
	}
	
	public void setServerHost(String serverHost) {
		SERVER_HOST = serverHost;
		URL_PREFIX = "http://" + SERVER_HOST + ":" + SERVER_PORT;
	}
	public void setServerPort(String string) {
		SERVER_PORT = string;
		URL_PREFIX = "http://" + SERVER_HOST + ":" + SERVER_PORT;
	}
	public static void setUrlPrefix(String urlPrefix) {
		URL_PREFIX = urlPrefix;
	}
	public static void setHttpGet(String httpGet) {
		HTTP_GET = httpGet;
	}
	public XStream getXmlStream() {
		return xmlStream;
	}
	public void setXmlStream(XStream xmlStream) {
		this.xmlStream = xmlStream;
	}
	public static String getServerHost() {
		return SERVER_HOST;
	}
	public static String getServerPort() {
		return SERVER_PORT;
	}
	public static String getUrlPrefix() {
		return URL_PREFIX;
	}
	public static String getHttpGet() {
		return HTTP_GET;
	}
	
	private Object doGet(String urlPath) throws ClientException {
		try {
			URL url = new URL(URL_PREFIX + urlPath);
			HttpURLConnection connection = (HttpURLConnection)url.openConnection();
			connection.setRequestMethod(HTTP_GET);
			connection.connect();
			if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
				Object result = xmlStream.fromXML(connection.getInputStream());
				return result;
			}
			else {
				throw new ClientException(String.format("doGet failed: %s (http code %d)",
											urlPath, connection.getResponseCode()));
			}
		}
		catch (IOException e) {
			throw new ClientException(String.format("doGet failed: %s", e.getMessage()), e);
		}
	}
	
	private Object doPost(String urlPath, Object postData) throws ClientException {
		try {
			URL url = new URL(URL_PREFIX + urlPath);
			HttpURLConnection connection = (HttpURLConnection)url.openConnection();
			connection.setRequestMethod(HTTP_POST);
			connection.setDoOutput(true);
			connection.connect();
			xmlStream.toXML(postData, connection.getOutputStream());
			connection.getOutputStream();
			if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
				throw new ClientException(String.format("doPost failed: %s (http code %d)",
						urlPath, connection.getResponseCode()));
			}
			if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
				Object result = xmlStream.fromXML(connection.getInputStream());
				return result;
			}
		}
		catch (IOException e) {
			throw new ClientException(String.format("doPost failed: %s", e.getMessage()), e);
		}
		return null;
	}
}
