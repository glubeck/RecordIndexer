package gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import model.User;
import client.ValidateUser_Result;

public class ErrorWindow extends JDialog implements ActionListener{

	JDialog errorFrame;
	JButton exitButton;
	Login login;
	
	public ErrorWindow (Login login) {
		this.login = login;
	}
	
		public void createErrorWindow() {
		
		//JFrame parentFrame = new JFrame("Error");
		
		//JDialog frame = new JDialog(parentFrame, "Error", true);
		this.setModal(true);
		this.setSize(500, 80);
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		//errorFrame = frame;
		
		JPanel panel = new JPanel();
		this.add(panel);		
		placeComponents(panel);

		this.setResizable(false);
		this.setVisible(true);

	}
	
	public void placeComponents(JPanel panel) {
		
		JLabel welcomeLabel = new JLabel("Invalid Username and/or Password");
		welcomeLabel.setBounds(20, 10, 80, 25);
		panel.add(welcomeLabel);
		
		JButton exitButton = new JButton("OK");
		exitButton.setBounds(180, 180, 80, 25);
		exitButton.addActionListener(this);
		panel.add(exitButton);
		this.exitButton = exitButton;
	
	}

	@Override
	public void actionPerformed(ActionEvent e) {
	
		if(e.getSource() == exitButton) {
			errorFrame.dispose();
		
			login.displayLogin();
		}
	}
}
