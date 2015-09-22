package gui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;

public class LeftPanel extends JSplitPane {
	
	TableEntryForm tableEntryForm;
	FormDataEntry formDataEntry;
	JScrollPane helpPane;
	BatchState batchState;
	JComponent panel3;
	
	public LeftPanel(BatchState batchState) {
		this.batchState = batchState;
	}
	
	
	public void createPanels() {
		
		JTabbedPane tab1 = new JTabbedPane();
		
		

		
		
		tab1.addTab("Table Entry", tableEntryForm);
		
		tab1.addTab("Form Entry", formDataEntry);
		tab1.addMouseListener(new MouseAdapter() {
			
			@Override
			public void mouseClicked(MouseEvent e) {
				if(SwingUtilities.isLeftMouseButton(e)) {
					batchState.refreshTextFields();
					batchState.forceLoseFocus();
					batchState.setHighlightedCell();
				}
			}
		});
		
		this.setLeftComponent(tab1);
		
		JTabbedPane tab2 = new JTabbedPane();
		
		panel3 = makeTextPanel("Field Help");
		
		helpPane = new JScrollPane(panel3);
		tab2.addTab("Field Help", helpPane);
		
		batchState.setHelpPane(helpPane);
		
		JComponent panel4 = makeTextPanel("Image Navigation");
		tab2.addTab("Image Navigation", panel4);
		
		this.setRightComponent(tab2);
		this.setOneTouchExpandable(true);
		this.setDividerLocation(470);
		this.setResizeWeight(0.5);
		this.revalidate();
		this.repaint();
	}
	
	
	protected JComponent makeTextPanel(String text) {
	    JPanel panel = new JPanel(false);
	    JLabel filler = new JLabel(text);
	    filler.setHorizontalAlignment(JLabel.CENTER);
	    panel.setLayout(new GridLayout(1, 1));
	    panel.add(filler);
	    return panel;
	}

	public void setInvisible() {
		formDataEntry.setInvisible();
		tableEntryForm.setInvisible();
		helpPane.setVisible(false);
		panel3.setVisible(false);
		batchState.setHtmlPaneInvisible();
	}
	
	public TableEntryForm getTableEntryForm() {
		return tableEntryForm;
	}

	public void setTableEntryForm(TableEntryForm tableEntryForm) {
		this.tableEntryForm = tableEntryForm;
	}

	public FormDataEntry getDataEntryFrom() {
		return formDataEntry;
	}
	
	public void setFormDataEntry(FormDataEntry formDataEntry) {
		this.formDataEntry = formDataEntry;
	}
	
	
	
}

