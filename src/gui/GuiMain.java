package gui;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;

import client.ClientCommunicator;

import gui.Login;

public class GuiMain {

	public GuiMain() {
		
		Login login = new Login();		
		login.createLogin();
	}
	
	public static void main(String[] args) {
		
		String host = args[0];
		String port = args[1];
		//System.out.println(host);
		//System.out.println(port);
		ClientCommunicator c = new ClientCommunicator();
		c.setServerHost(host);
		c.setServerPort(port);
		//System.out.println(ClientCommunicator.getUrlPrefix());
		
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				new GuiMain();
				
			}
		});
		
		
		
	}
	
}
