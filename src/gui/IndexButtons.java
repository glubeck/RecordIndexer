package gui;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import client.ClientCommunicator;
import client.ClientException;
import client.SubmitBatch_Params;
import client.SubmitBatch_Result;


public class IndexButtons extends JPanel implements ActionListener{

	JButton zoomIn;
	JButton zoomOut;
	JButton invertImage;
	JButton toggleHighlights;
	JButton save;
	JButton submit;
	java.awt.Image image;
	MidWin midwin;
	BatchState batchState;
	
	public IndexButtons(MidWin midwin, BatchState batchState) {
		this.midwin = midwin;
		this.batchState = batchState;
	}
	
	
	public void createButtons() {
		
		this.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		
		c.anchor = GridBagConstraints.LINE_START;
		
		JButton button;
		
		JButton zoomIn = new JButton("Zoom In");
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridwidth = 1;
		c.ipady = 20;
		c.weightx = 0;
		c.weighty = 0;
		c.gridx = 0;
		c.gridy = 0;
		this.add(zoomIn, c);
		zoomIn.addActionListener(this);
		this.zoomIn = zoomIn;
		
		JButton zoomOut = new JButton("Zoom Out");
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridwidth = 1;
		c.ipady = 20;
		c.weightx = 0;
		c.weighty = 0;
		c.gridx = 1;
		c.gridy = 0;
		this.add(zoomOut, c);
		zoomOut.addActionListener(this);
		this.zoomOut = zoomOut;
		
		JButton invert = new JButton("Invert Image");
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridwidth = 1;
		c.ipady = 20;
		c.weightx = 0;
		c.weighty = 0;
		c.gridx = 2;
		c.gridy = 0;
		this.add(invert, c);
		invert.addActionListener(this);
		invertImage = invert;
		
		JButton toggle = new JButton("Toggle Highlights");
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridwidth = 1;
		c.ipady = 20;
		c.weightx = 0;
		c.weighty = 0;
		c.gridx = 3;
		c.gridy = 0;
		toggle.addActionListener(this);
		this.add(toggle, c);
		toggleHighlights = toggle;
		
		button = new JButton("Save");
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridwidth = 1;
		c.ipady = 20;
		c.weightx = 0;
		c.weighty = 0;
		c.gridx = 4;
		c.gridy = 0;
		this.add(button, c);
		
		JButton submit = new JButton("Submit");
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridwidth = 1;
		c.ipady = 20;
		c.weightx = 0;
		c.weighty = 0;
		c.gridx = 5;
		c.gridy = 0;
		submit.addActionListener(this);
		this.add(submit, c);
		this.submit = submit;
	}
	
	private java.awt.Image invertImage(java.awt.Image image) {
        BufferedImage inputFile = null;
        inputFile = toBufferedImage(image);

        for (int x = 0; x < inputFile.getWidth(); x++) {
            for (int y = 0; y < inputFile.getHeight(); y++) {
                int rgba = inputFile.getRGB(x, y);
                Color col = new Color(rgba, true);
                col = new Color(255 - col.getRed(),
                                255 - col.getGreen(),
                                255 - col.getBlue());
                inputFile.setRGB(x, y, col.getRGB());
            }
        }


		return (java.awt.Image)inputFile;
    }
	
	/**
	 * Converts a given Image into a BufferedImage
	 *
	 * @param img The Image to be converted
	 * @return The converted BufferedImage
	 */
	private BufferedImage toBufferedImage(java.awt.Image img)
	{
	    if (img instanceof BufferedImage)
	    {
	        return (BufferedImage) img;
	    }

	    // Create a buffered image with transparency
	    BufferedImage bimage = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_ARGB);

	    // Draw the image on to the buffered image
	    Graphics2D bGr = bimage.createGraphics();
	    bGr.drawImage(img, 0, 0, null);
	    bGr.dispose();

	    // Return the buffered image
	    return bimage;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
	
		if(e.getSource() == invertImage) {
			
			image = invertImage(image);
			midwin.updateImage(image);
		}
			
		if(e.getSource() == zoomOut)
			midwin.zoomOut();
		if(e.getSource() == zoomIn)
			midwin.zoomIn();
		
		if(e.getSource() == toggleHighlights)
			batchState.toggleHighlights();
		
		if(e.getSource() == submit) {
			if(!batchState.hasCurrentBatch()) {
				displayError();
			}
			else {
			JTable table = batchState.getTable();
			Object[][] data = getTableData(table);
			model.Image image = batchState.getImage();
			String recordValues = transferData(data);
			SubmitBatch_Params params = new SubmitBatch_Params(
					batchState.getUser(), batchState.getImage(),
					recordValues);
			ClientCommunicator c = new ClientCommunicator();
			try {
				SubmitBatch_Result result = c.SubmitBatch(params);
				if(result.isSuccess()) {
					batchState.clearDrawingComponent();
					batchState.enableDownload();
					batchState.clearBottom();
				}
			} catch (ClientException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			}
		}
	}
	
	private void displayError() {
		String errorMessage = "No Current Batch";
		JOptionPane.showMessageDialog(null, errorMessage, "Error", JOptionPane.ERROR_MESSAGE);
		
	}


	private String transferData(Object[][] data) {
		
		//ArrayList<String[]> result = new ArrayList<String[]>();
		StringBuilder sb = new StringBuilder("");
		for(int i = 0; i < data.length; i++) {
			//String[] temp = new String[data[i].length];
			for(int j = 1; j < data[i].length; j++) {
				//temp[j] = (String) data[i][j];
				if(data[i][j]!=null)
					sb.append(data[i][j]);
				else
					sb.append(" ");
				if(j < data[i].length-1)
					sb.append(",");
			}
			sb.append(";");
		}
		
		return sb.toString();
	}
	
	public Object[][] getTableData (JTable table) {
	    DefaultTableModel dtm = (DefaultTableModel) table.getModel();
	    int nRow = dtm.getRowCount(), nCol = dtm.getColumnCount();
	    Object[][] tableData = new Object[nRow][nCol];
	    for (int i = 0 ; i < nRow ; i++)
	        for (int j = 0 ; j < nCol ; j++) {
	        	if(dtm.getValueAt(i,  j) == null)
	        		tableData[i][j] = " ";
	        	else
	        		tableData[i][j] = dtm.getValueAt(i,j);
	        }
	    batchState.setData(tableData);
	    return tableData;
	}
	
	
	public Image getImage() {
		return this.image;
	}
	
	public void setImage(Image image) {
		this.image = image;
	}
}
