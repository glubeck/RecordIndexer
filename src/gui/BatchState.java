package gui;

import java.awt.Image;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JEditorPane;
import javax.swing.JMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import model.Field;
import model.User;

public class BatchState {

	private transient List<BatchStateListener> listeners;
	private DrawingComponent drawingComponent;
	private Object[][] data;
	private JTable table;
	private User user;
	private model.Image image;
	private JMenuItem downloadItem;
	private LeftPanel bottom;
	private List<Field> fields;
	private JScrollPane helpPane;
	private String URL_PATH;
	private JEditorPane htmlPane;
	private Object[][] formData;
	private FormDataEntry formDataEntry;
	private int CellRow;
	private int CellColumn;
	private TableEntryForm tableEntryForm;
	private int index;
	private int column;
	
	public BatchState (User user) {
		this.user = user;
		user.setCurrentBatch("");
	}
	
	public DrawingComponent getDrawingComponent() { 		
		return this.drawingComponent;
	}
	
	public void setDrawingComponent(DrawingComponent drawingComponent) {
		this.drawingComponent = drawingComponent;
	}
	
	public void setData(Object[][] data) {
		this.data = data;
	}
	
	public Object[][] getData() {
		return this.data;
	}
	
	public JTable getTable() {
		return table;
	}
	
	public void setTable(JTable table) {
		this.table = table;
	}

	public void setImage(model.Image image) {
		this.image = image;
		
	}

	public User getUser() {
		return user;
	}

	public model.Image getImage() {
		return image;
	}

	public boolean hasCurrentBatch() {
		if(user.getCurrentBatch().equals("")) 
			return false;
		else {
			user.setCurrentBatch("");
			return true;
		}
	}

	public void setUser(User user) {
		this.user = user;		
	}
	


	public void clearDrawingComponent() {
		drawingComponent.clear();
		
	}

	public void disableDownload() {
		downloadItem.setEnabled(false);
		
	}
	public void enableDownload() {
		downloadItem.setEnabled(true);
		
	}

	public void setDownloadItem(JMenuItem downloadItem) {
		this.downloadItem = downloadItem;
		
	}

	public void setBottom(LeftPanel bottom) {
		this.bottom = bottom;
		
	}

	public void clearBottom() {
		bottom.setInvisible();
	}

	public void toggleHighlights() {
		drawingComponent.toggleHighlights();
		
	}

	public void setFields(List<Field> fields) {
		this.fields = fields;
		
	}

	public List<Field> getFields() {
		return fields;
	}

	public void setHelpPane(JScrollPane helpPane) {
		this.helpPane = helpPane;
	}
	
	public JScrollPane getHelpPane() {
		return helpPane;
	}

	public void setURL_PATH(String url_PATH) {
		this.URL_PATH = url_PATH;	
	}
	public String getURL_PATH() {
		return URL_PATH;
	}

	public void setHtmlPaneInvisible() {
		htmlPane.setVisible(false);
	}
	
	public void setHtmlPane(JEditorPane p2) {
		this.htmlPane = p2;
		
	}
	
	public void setTable(Object[][] formData) {
		
		for(int i = 0; i < formData.length; i++) {
			
			for(int j = 0; j < formData[i].length; j++) {
				table.setValueAt(formData[i][j], i, j+1);
			}
		}
		data = formData;
	}

	public void setFormData(Object[][] formData) {
		this.formDataEntry.setFormData(formData);
		
	}


	public void setFormDataEntry(FormDataEntry formDataEntry) {
		this.formDataEntry = formDataEntry;
		
	}

	public void refreshTextFields() {
		formDataEntry.refreshTextFields();
		
	}

	public void forceLoseFocus() {
		formDataEntry.forceLoseFocus();
		
	}

	public void setHighlightedCell() {
		formDataEntry.setHighlightedCell(CellRow,CellColumn);
		
	}

	public void setCellRowAndColumn(int row, int column) {
		CellRow = row;
		CellColumn = column;
		
	}

	public void setTableSelectedCell(int row, int column) {
		
		tableEntryForm.setSelectedCell(row, column);
		
	}

	public void setTableEntryForm(TableEntryForm tableEntryForm) {
		this.tableEntryForm = tableEntryForm;
		
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public int getIndex() {
		return index;
	}
	
	public void setColumn(int column) {
		this.column = column;
	}
	public int getColumn() {
		return column;
	}
	
	public void addListener(BatchStateListener bsl) {
		
		listeners.add(bsl);
	}
	
	public interface BatchStateListener {
		
		public void cellChanged(int row, int column); 
		
		public void valueChanged(int row, int column, String value);
	}
}
