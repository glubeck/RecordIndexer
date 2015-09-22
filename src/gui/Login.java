package gui;

import java.awt.Dialog;
import java.awt.Event;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.io.IOException;

import javax.swing.*;

import client.ClientCommunicator;
import client.ClientException;
import client.ValidateUser_Params;
import client.ValidateUser_Result;
import model.User;

public class Login extends JDialog implements ActionListener {

	private JTextField username;
	private JPasswordField password;
	private JDialog loginFrame;
	private JButton loginButton;
	private JButton exitButton;
	
	Login () {}

	
	public void createLogin() {
		
		
		this.setModal(true);
		this.setSize(300, 150);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		//this.loginFrame = this;
		
		
		JPanel panel = new JPanel();
		this.add(panel);		
		placeComponents(panel);

		this.setResizable(false);
		this.setVisible(true);

	}
	
	public void placeComponents(JPanel panel) {

		panel.setLayout(null);

		JLabel userLabel = new JLabel("Name");
		userLabel.setBounds(10, 10, 80, 25);
		panel.add(userLabel);

		JTextField userText = new JTextField(20);
		userText.setBounds(100, 10, 160, 25);
		panel.add(userText);
		this.username = userText;

		JLabel passwordLabel = new JLabel("Password");
		passwordLabel.setBounds(10, 40, 80, 25);
		panel.add(passwordLabel);

		JPasswordField passwordText = new JPasswordField(20);
		passwordText.setBounds(100, 40, 160, 25);
		panel.add(passwordText);
		this.password = passwordText;

		JButton loginButton = new JButton("Login");
		loginButton.setBounds(10, 80, 80, 25);
		loginButton.addActionListener(this);
		panel.add(loginButton);
		this.loginButton = loginButton;
		
		JButton exitButton = new JButton("Exit");
		exitButton.setBounds(180, 80, 80, 25);
		exitButton.addActionListener(this);
		panel.add(exitButton);
		this.exitButton = exitButton;
		
	}

	public void displayLogin() {
		setVisible(true);
	}

	public void validateUser() throws IOException {
		
		ClientCommunicator c = new ClientCommunicator();
		String name = getUsername();
		String pass = getPassword();
		
		User user = new User(name, pass);
				
		ValidateUser_Params params = new ValidateUser_Params(user);
		ValidateUser_Result result;
		try {
			result = c.ValidateUser(params);
			if(result.isInDatabase()) {
				this.setVisible(false);
				WelcomeWindow welcome = new WelcomeWindow(this, result);
				welcome.createWelcomeWindow(true);

				IndexWindow index = new IndexWindow(this, result.getUser());
				index.createIndexWindow();
				
			}
			else {
				WelcomeWindow welcome = new WelcomeWindow(this, result);
				welcome.createWelcomeWindow(false);
			}
			
		} catch (ClientException e1) {
			e1.printStackTrace();
		}
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		
		if(e.getSource() == loginButton)
			try {
				validateUser();
			} catch (IOException e1) {
				e1.printStackTrace();
			}

		if(e.getSource() == exitButton) 
			System.exit(0);
			
	}
	
	public String getUsername() {
		return username.getText();
	}
	
	public String getPassword() {
		return new String(password.getPassword());
	}


	public JDialog getLoginFrame() {
		return loginFrame;
	}

	
}


