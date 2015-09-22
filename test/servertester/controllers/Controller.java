package servertester.controllers;

import java.util.*;

import model.*;
import client.*;
import dbaccess.Database;
import servertester.views.*;

public class Controller implements IController {

	private IView _view;
	
	public Controller() {
		return;
	}
	
	public IView getView() {
		return _view;
	}
	
	public void setView(IView value) {
		_view = value;
	}
	
	// IController methods
	//
	
	@Override
	public void initialize() {
		getView().setHost("localhost");
		getView().setPort("8080");
		operationSelected();
	}

	@Override
	public void operationSelected() {
		ArrayList<String> paramNames = new ArrayList<String>();
		paramNames.add("User");
		paramNames.add("Password");
		
		switch (getView().getOperation()) {
		case VALIDATE_USER:
			break;
		case GET_PROJECTS:
			break;
		case GET_SAMPLE_IMAGE:
			paramNames.add("Project");
			break;
		case DOWNLOAD_BATCH:
			paramNames.add("Project");
			break;
		case GET_FIELDS:
			paramNames.add("Project");
			break;
		case SUBMIT_BATCH:
			paramNames.add("Batch");
			paramNames.add("Record Values");
			break;
		case SEARCH:
			paramNames.add("Fields");
			paramNames.add("Search Values");
			break;
		default:
			assert false;
			break;
		}
		
		getView().setRequest("");
		getView().setResponse("");
		getView().setParameterNames(paramNames.toArray(new String[paramNames.size()]));
	}

	@Override
	public void executeOperation() {
		switch (getView().getOperation()) {
		case VALIDATE_USER:
			try {
				validateUser();
			} catch (ClientException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
		case GET_PROJECTS:
			try {
				getProjects();
			} catch (ClientException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
		case GET_SAMPLE_IMAGE:
			try {
				getSampleImage();
			} catch (ClientException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
		case DOWNLOAD_BATCH:
			try {
				downloadBatch();
			} catch (ClientException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
		case GET_FIELDS:
			try {
				getFields();
			} catch (ClientException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
		case SUBMIT_BATCH:
			try {
				submitBatch();
			} catch (ClientException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			break;
		case SEARCH:
			try {
				search();
			} catch (ClientException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
		default:
			assert false;
			break;
		}
	}
	
	private void validateUser() throws ClientException {
		try {
		ClientCommunicator c = new ClientCommunicator();
		c.setServerHost(getView().getHost());
		c.setServerPort(getView().getPort());
		String[] paramValues = getView().getParameterValues();
		User user = new User(paramValues[0], paramValues[1]);
		ValidateUser_Params params = new ValidateUser_Params(user);
		ValidateUser_Result result = c.ValidateUser(params);
		if(!result.isInDatabase())
			getView().setResponse("FALSE");
		else
			getView().setResponse(result.toString());
		}
		catch (Exception e){
			getView().setResponse("FAILED");
		}
	}
	
	private void getProjects() throws ClientException {
		try {
			ClientCommunicator c = new ClientCommunicator();
			c.setServerHost(getView().getHost());
			c.setServerPort(getView().getPort());
			String[] paramValues = getView().getParameterValues();
			User user = new User(paramValues[0], paramValues[1]);

			GetProjects_Params params = new GetProjects_Params(user);
			GetProjects_Result result = c.GetProjects(params);
			
			if(!result.isValid())
				getView().setResponse("FAILED");
			else
				getView().setResponse(result.toString());
			
		}
		catch (Exception e) {
			getView().setResponse("FAILED");
		}
	}
	
	private void getSampleImage() throws ClientException {
		try {
		ClientCommunicator c = new ClientCommunicator();
		c.setServerHost(getView().getHost());
		c.setServerPort(getView().getPort());
		String[] paramValues = getView().getParameterValues();
		User user = new User(paramValues[0], paramValues[1]);
		int id = Integer.parseInt(paramValues[2]);
		Project project = new Project(id);
		GetSampleImage_Params params = new GetSampleImage_Params(user, project);
		GetSampleImage_Result result = c.GetSampleImage(params);
		
		result.setURL_PATH(ClientCommunicator.getUrlPrefix());
		
		if(!result.isValid())
			getView().setResponse("FAILED");
		else
			getView().setResponse(result.toString());
		}
		catch (Exception e) {
			getView().setResponse("FAILED");
		}
		
	}
	
	private void downloadBatch() throws ClientException {
		try {
		ClientCommunicator c = new ClientCommunicator();
		c.setServerHost(getView().getHost());
		c.setServerPort(getView().getPort());
		String[] paramValues = getView().getParameterValues();
		User user = new User(paramValues[0], paramValues[1]);
		int id = Integer.parseInt(paramValues[2]);
		Project project = new Project(id);
		DownloadBatch_Params params = new DownloadBatch_Params(user, project);
		DownloadBatch_Result result = c.DownloadBatch(params);

		result.setURL_PATH(ClientCommunicator.getUrlPrefix());
		
		if(!result.isValid() || result == null)
			getView().setResponse("FAILED");
		else
			getView().setResponse(result.toString());
		
		}
		catch (Exception e) {
			getView().setResponse("FAILED");
		}
	}
	
	private void getFields() throws ClientException {
		try {
		ClientCommunicator c = new ClientCommunicator();
		c.setServerHost(getView().getHost());
		c.setServerPort(getView().getPort());
		String[] paramValues = getView().getParameterValues();
		User user = new User(paramValues[0], paramValues[1]);
		int id;
		if(paramValues[2].isEmpty())
			id = -1;
		else
			id = Integer.parseInt(paramValues[2]);
		Project project = new Project(id);
		GetFields_Params params = new GetFields_Params(user, project);
		GetFields_Result result = c.GetFields(params);
		
		if(!result.isValid())
			getView().setResponse("FAILED");
		else
			getView().setResponse(result.toString());
		
		}
		catch (Exception e) {
			getView().setResponse("FAILED");
		}
	}
	
	private void submitBatch() throws ClientException {
		try {
		ClientCommunicator c = new ClientCommunicator();
		c.setServerHost(getView().getHost());
		c.setServerPort(getView().getPort());
		String[] paramValues = getView().getParameterValues();
		User user = new User(paramValues[0], paramValues[1]);
		int batchID = Integer.parseInt(paramValues[2]);
		Image image = new Image(batchID);
		String RecordValues = paramValues[3];
		SubmitBatch_Params params = new SubmitBatch_Params(user, image, RecordValues);
		SubmitBatch_Result result = c.SubmitBatch(params);
		
		if(!result.isSuccess())
			getView().setResponse("FAILED");
		else
			getView().setResponse(result.toString());
		
		
		}
		catch (Exception e) {
			getView().setResponse("FAILED");
		}
	}
	
	private void search() throws ClientException {
		try {
		ClientCommunicator c = new ClientCommunicator();
		c.setServerHost(getView().getHost());
		c.setServerPort(getView().getPort());
		String[] paramValues = getView().getParameterValues();
		User user = new User(paramValues[0], paramValues[1]);
		String fields = paramValues[2];
		String values = paramValues[3];
		if(values.equals(""))
			throw new Exception();
		
		Search_Params params = new Search_Params(user, fields, values);
		Search_Result result = c.Search(params);

		result.setURL_PATH(ClientCommunicator.getUrlPrefix());
		
		if(!result.isValid())
			getView().setResponse("FAILED");
		else
			getView().setResponse(result.toString());
		
		}
		catch (Exception e) {
			getView().setResponse("FAILED");
		}
	}

}

