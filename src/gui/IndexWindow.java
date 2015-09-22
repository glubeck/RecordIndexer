package gui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;

import client.ClientException;
import model.Field;
import model.User;

public class IndexWindow extends JFrame implements ActionListener, WindowListener{

	private JFrame indexWindowFrame;
	private Login login;
	private JButton exitButton;
	private JButton logoutButton;
	private JMenuBar menuBar = new JMenuBar(); // Window menu bar
	private JMenuItem downloadItem, logoutItem, exitItem;
	private User user;
	private MidWin midwin;
	private LeftPanel bottomPane;
	private IndexButtons indexButtons;
	private BatchState batchState;
	private UserState userState;

	IndexWindow(Login login, User user) throws IOException {
		this.addWindowListener(this);
		this.login = login;
		this.user = user;
		setJMenuBar(menuBar);
		userState = new UserState();
		batchState = new BatchState(user);
	}
	
public void createIndexWindow() {
		
		this.setSize(1000, 900);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JPanel Panel = new JPanel();
		this.add(Panel);		
		placeComponents(Panel);

		this.setResizable(true);
		this.setVisible(true);
		
	}
	

	public void placeComponents(JPanel Panel) {

		  JMenu fileMenu = new JMenu("File");
		//  downloadItem = new JMenuItem();
		  downloadItem = fileMenu.add("Download Batch");
		  downloadItem.addActionListener(this);
		  batchState.setDownloadItem(downloadItem);
		  if(!user.getCurrentBatch().equals(""))
			  batchState.disableDownload();
			  
		  
		  logoutItem = fileMenu.add("Logout");
		  logoutItem.addActionListener(this);
		  
		  exitItem = fileMenu.add("Exit");
		  exitItem.addActionListener(this);
		  
		  menuBar.add(fileMenu);
		
		  this.setLayout(new GridBagLayout());
		  GridBagConstraints c = new GridBagConstraints();
		  
		  JButton button;
		  

		  RootPane root = new RootPane(batchState);
		  root.createPanels();
		  c.fill = GridBagConstraints.BOTH;
		  c.ipady = 440;      //make this component tall
		  c.weightx = 0.0;
		  c.weighty = .5;
		  c.gridwidth = 3;
		  c.gridx = 0;
		  c.gridy = 1;
		  midwin = root.getTop();
		  bottomPane = root.getBottom();
		  this.add(root, c);
		  
		  IndexButtons buttonPanel = new IndexButtons(midwin, batchState);
		  buttonPanel.createButtons();
		  c.fill = GridBagConstraints.HORIZONTAL;
		  c.gridwidth = 2;
		  c.ipady = 10;
		  c.weightx = 0.5;
		  c.weighty = 0;
		  c.gridx = 0;
		  c.gridy = 0;
		  this.add(buttonPanel, c);
		  indexButtons = buttonPanel;
	   
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
	
		if(e.getSource() == downloadItem) {
			DownloadBox db = new DownloadBox(user, midwin, this, batchState);
			try {
				db.createDownloadBox();
			} catch (ClientException e1) {
				e1.printStackTrace();
			}
		}
		
		if(e.getSource() == logoutItem) {
			this.dispose();
			login.displayLogin();
		}
		
		if(e.getSource() == exitItem)
			System.exit(0);
	}

	public void createTableEntry(int rows, int columns, List<Field> fields) {
		
		TableEntryForm form = new TableEntryForm(rows, columns, fields, batchState);
		bottomPane.setTableEntryForm(form);
		bottomPane.createPanels();
	}
	
	public JMenuItem getDownloadItem() {
		return downloadItem;
	}

	public void setDownloadItem(JMenuItem downloadItem) {
		this.downloadItem = downloadItem;
	}
	
	public IndexButtons getIndexButtons() {
		return indexButtons; 
	}

	public void setIndexButtons(IndexButtons indexButtons) {
		this.indexButtons = indexButtons;
	}

	public void createFormEntry(int recordsPerImage, List<Field> fields) {
		FormDataEntry form = new FormDataEntry(recordsPerImage, fields, batchState);
		bottomPane.setFormDataEntry(form);
		bottomPane.createPanels();
	}

	@Override
	public void windowOpened(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowClosing(WindowEvent e) {
//		
//		try {
//			userState.save(batchState, user.getUsername());
//			e.getWindow().dispose();
//			
//		} catch (FileNotFoundException e1) {
//			e1.printStackTrace();
//		}
//		
	}

	@Override
	public void windowClosed(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowIconified(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowDeiconified(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowActivated(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowDeactivated(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}
}
