package gui;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import model.Field;
import model.Project;

public class MidWin extends JPanel {

	private DrawingComponent drawingComponent;
	private BatchState batchState;
	
	public void createMidWin(BatchState batchState) {
			
	setBackground(Color.gray);
	this.batchState = batchState;
	
	}
	
	
	
	public void addImage(Image image, Project project, List<Field> fields) {
		
		if(drawingComponent == null)  {
			drawingComponent = new DrawingComponent(image, project, fields);
			add(drawingComponent);
		}
		else {
			drawingComponent.updateImageProjectFields(image,  project,  fields);
		}
		batchState.setDrawingComponent(drawingComponent);
		
	}
	
	public void updateImage(Image image) {
		
		drawingComponent.updateImage(image);
		repaint();
		SwingUtilities.updateComponentTreeUI(this);
		batchState.setDrawingComponent(drawingComponent);
	}
	
	public void zoomOut() {
		if(drawingComponent.getScale() > .3) {
			drawingComponent.setScale(drawingComponent.getScale()*.9);
			batchState.setDrawingComponent(drawingComponent);
		}
	}	
	public void zoomIn() {
		if(drawingComponent.getScale() < 2.5) {
			drawingComponent.setScale(drawingComponent.getScale()*1.1);
			batchState.setDrawingComponent(drawingComponent);
		}
	}
}
