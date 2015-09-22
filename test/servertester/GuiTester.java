package servertester;

import server.Server;
import servertester.controllers.Controller;
import servertester.views.IndexerServerTesterFrame;

import java.awt.EventQueue;

public class GuiTester {

	public static void main(String[] args) {
		EventQueue.invokeLater(
				new Runnable() {
					public void run() {
					//	Server server = new Server();
					//	server.run();
						IndexerServerTesterFrame frame = new IndexerServerTesterFrame();			
						Controller controller = new Controller();
						frame.setController(controller);			
						controller.setView(frame);
						controller.initialize();
						frame.setVisible(true);
					}
				}
		);

	}

}

