package gui;

import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import model.Field;
import model.Project;
import model.User;
import client.ClientCommunicator;
import client.ClientException;
import client.DownloadBatch_Params;
import client.DownloadBatch_Result;
import client.GetProjects_Params;
import client.GetProjects_Result;
import client.GetSampleImage_Params;
import client.GetSampleImage_Result;

public class DownloadBox extends JDialog implements ActionListener{

	private JButton viewSample;
	private JButton cancel;
	private JButton download;
	private JComboBox projectList;
	private String[] projectArray;
	private User user;
	private MidWin midwin;
	private IndexWindow indexwin;
	private BatchState batchState;
	
	public DownloadBox(User user, MidWin midwin, IndexWindow indexwin, BatchState batchState){
		this.user = user;
		this.midwin = midwin;
		this.indexwin = indexwin;
		this.batchState = batchState;
	}
	
	public void createDownloadBox() throws ClientException {
		
		this.setModal(true);
		this.setSize(300, 150);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	
		JPanel panel = new JPanel();
		this.add(panel);		
		placeComponents(panel);

		this.setResizable(false);
		this.setVisible(true);
	}
	
	public void placeComponents(JPanel panel) throws ClientException {
		
		JLabel userLabel = new JLabel("Project:");
		userLabel.setBounds(10, 10, 80, 25);
		panel.add(userLabel);
		
		String[] projects = getProjects();
		JComboBox projectList = new JComboBox(projects);
		projectList.setBounds(100, 10, 160, 25);
		panel.add(projectList);
		this.projectList = projectList;
		
		JButton downloadButton = new JButton("Download");
		downloadButton.setBounds(10, 80, 80, 25);
		downloadButton.addActionListener(this);
		panel.add(downloadButton);
		download = downloadButton;
		
		JButton cancelButton = new JButton("Cancel");
		cancelButton.setBounds(180, 80, 80, 25);
		cancelButton.addActionListener(this);
		panel.add(cancelButton);
		cancel = cancelButton;
		
		JButton getSample = new JButton("View Sample");
		getSample.setBounds(100, 80, 80, 25);
		getSample.addActionListener(this);
		panel.add(getSample);
		viewSample = getSample;
	}

	public String[] getProjects() throws ClientException {
		
		ClientCommunicator c = new ClientCommunicator();
		GetProjects_Params params = new GetProjects_Params(user);
		GetProjects_Result result = c.GetProjects(params);
		List<Project> projectsTemp = result.getProjects();
		
		String[] projects = new String[projectsTemp.size()];
		
		for(int i = 0; i < projectsTemp.size(); i++) {
			projects[i] = projectsTemp.get(i).getTitle();
		}
		projectArray = projects;
		
		
		return projects;
	}
	
	private int getProjectID(String title) {
		
		for(int i = 0; i < projectArray.length; i++) {
			if(projectArray[i].equals(title))
				return i + 1;
		}
		
		return 0;
	}
	
	
	private void download() {
		
		String title = (String)projectList.getSelectedItem();
		int projectID = getProjectID(title);
		
		ClientCommunicator c = new ClientCommunicator();
		Project project = new Project(projectID);
		if(batchState.getUser().getCurrentBatch().equals("")) {
			DownloadBatch_Params params = new DownloadBatch_Params(user, project);
		
			try {
				DownloadBatch_Result result = c.DownloadBatch(params);
				Project realProject = result.getProject();
				List<Field> fields = result.getFields();
				model.Image batch = result.getImage();
				result.setURL_PATH(ClientCommunicator.getUrlPrefix());
				String urlString = result.urlToString();
				URL url = new URL(urlString);
				batchState.setImage(batch);
				batchState.setUser(result.getUser());
				batchState.setFields(fields);
				batchState.setURL_PATH(result.getURL_PATH());
				
				
			
				Image image = ImageIO.read(url);
				midwin.addImage(image, realProject, fields);

				//DrawingComponent dc = new DrawingComponent(image);
								
				//midwin.add(dc);
				IndexButtons buttons = indexwin.getIndexButtons();
				
				buttons.setImage(image);
				
				SwingUtilities.updateComponentTreeUI(midwin);
				user.setCurrentBatch(result.getURL_PATH());
				JMenuItem downloaditem = indexwin.getDownloadItem();
				downloaditem.setEnabled(false);
				indexwin.setDownloadItem(downloaditem);
				
				indexwin.createTableEntry(realProject.getRecordsPerImage(), fields.size()+1, fields);
				indexwin.createFormEntry(realProject.getRecordsPerImage(), fields);
				this.dispose();
				
			} catch (ClientException | IOException e1) {
				e1.printStackTrace();
			}
		}
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		
		if(e.getSource() == download) {
			
			download();
		}
		
		if(e.getSource() == viewSample) {
			
			String title = (String)projectList.getSelectedItem();
			int projectID = getProjectID(title);
			
			ClientCommunicator c = new ClientCommunicator();
			Project project = new Project(projectID);
			GetSampleImage_Params params = new GetSampleImage_Params(user, project);
			try {
				GetSampleImage_Result result = c.GetSampleImage(params);
				result.setURL_PATH(ClientCommunicator.getUrlPrefix());
				String urlString = result.toString();
				URL url = new URL(urlString);
				
				Image image = ImageIO.read(url).getScaledInstance(640, 400, Image.SCALE_SMOOTH);
				
				JLabel picLabel = new JLabel(new ImageIcon(image));
				JOptionPane.showMessageDialog(null, picLabel, title, JOptionPane.PLAIN_MESSAGE, null);
				
			} catch (ClientException e1) {
				e1.printStackTrace();
			} catch (MalformedURLException e1) {
				e1.printStackTrace();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		
		if(e.getSource() == cancel) {
			this.dispose();
		}
		
	}
}
