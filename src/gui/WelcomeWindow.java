package gui;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import client.ValidateUser_Result;

public class WelcomeWindow extends JOptionPane{

	JDialog welcomeFrame;
	JButton exitButton;
	Login login;
	ValidateUser_Result result;
	
	public WelcomeWindow (Login login, ValidateUser_Result result) {
		this.login = login;
		this.result = result;
	}
	
		public void createWelcomeWindow(boolean isValid) {
		
		
		this.setSize(200, 180);
		if(isValid) {
		String validMessage = "Welcome " + result.getUser().getFirstname() +
				". You have indexed " + result.getUser().getIndexedRecords() + " records.";
		showMessageDialog(null, validMessage, "Welcome", JOptionPane.INFORMATION_MESSAGE);
		}
		else {
			String errorMessage = "Invalid Username and/or Password";
			showMessageDialog(null, errorMessage, "Welcome", JOptionPane.ERROR_MESSAGE);
		}
	}
	
//	public void placeComponents(JPanel panel) {
//	
//		User user = result.getUser();
//		
//		JLabel welcomeLabel = new JLabel("Welcome " + user.getFirstname() + "! You have indexed " + user.getIndexedRecords() + " records");
//		welcomeLabel.setBounds(20, 10, 80, 25);
//		panel.add(welcomeLabel);
//		
//		JButton exitButton = new JButton("Continue");
//		exitButton.setBounds(180, 180, 80, 25);
//		exitButton.addActionListener(this);
//		panel.add(exitButton);
//		this.exitButton = exitButton;
//	
//	}
//
//	@Override
//	public void actionPerformed(ActionEvent e) {
//	
//		if(e.getSource() == exitButton) {
//		//	this.dispose();
//			IndexWindow indexer = new IndexWindow(getLogin());
//			indexer.createIndexWindow();
//		}
//		
//	
//	}
//
//	public Login getLogin() {
//		return login;
//	}

}
