package gui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;

public class Suggestions extends JDialog implements ActionListener {

	private JButton cancel;
	private JButton use;
	private Set<String> suggestions;
	private JList list;
	private TableEntryForm tableEntryForm;
	private FormDataEntry formDataEntry;

	public Suggestions(Set<String> suggestions, TableEntryForm tableEntryForm) {
		
		this.tableEntryForm = tableEntryForm;
		this.suggestions = suggestions;
		this.setModal(true);
		this.setSize(250, 300);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
		JPanel panel = new JPanel();
		this.add(panel);		
		placeComponents(panel);

		this.setResizable(false);
		this.setVisible(true);

	}
	
	public Suggestions(Set<String> suggestions, FormDataEntry formDataEntry) {
		this.formDataEntry = formDataEntry;
		this.suggestions = suggestions;
		this.setModal(true);
		this.setSize(250, 300);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
		JPanel panel = new JPanel();
		this.add(panel);		
		placeComponents(panel);

		this.setResizable(false);
		this.setVisible(true);
	}

	public void placeComponents(JPanel panel) {
		
		  panel.setLayout(new GridBagLayout());
		  GridBagConstraints c = new GridBagConstraints();
		  
		  
		  Object[] listValues = makeList(suggestions);
		  list = new JList(listValues);
		  JScrollPane scroller = new JScrollPane(list,
				  JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				  JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		  list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		  list.setLayoutOrientation(JList.HORIZONTAL_WRAP);
		  c.fill = GridBagConstraints.BOTH;
		  c.ipady = 200;      //make this component tall
		  c.weightx = 0.0;
		  c.weighty = .7;
		  c.gridwidth = 2;
		  c.gridx = 0;
		  c.gridy = 0;
		  
		  panel.add(scroller, c);
		  
		  cancel = new JButton("Cancel");
		  cancel.addActionListener(this);
		  c.fill = GridBagConstraints.BOTH;
		  c.ipady = 25;      //make this component tall
		  c.weightx = 0.0;
		  c.weighty = .4;
		  c.gridwidth = 1;
		  c.gridx = 0;
		  c.gridy = 1;
		  
		  panel.add(cancel, c);
		  
		  use = new JButton("Use Suggestion");
		  use.addActionListener(this);
		  c.fill = GridBagConstraints.BOTH;
		  c.ipady = 25;      //make this component tall
		  c.weightx = 0.0;
		  c.weighty = .4;
		  c.gridwidth = 1;
		  c.gridx = 1;
		  c.gridy = 1;
		  
		  panel.add(use, c);
	}
	
	private Object[] makeList(Set<String> suggestions) {
		
		Object[] listValues = new Object[suggestions.size()];
		
		List sortedList = new ArrayList(suggestions);
		Collections.sort(sortedList);
		
		for(int i = 0; i < sortedList.size(); i++) {
			listValues[i] = sortedList.get(i);
		}
		return listValues;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == cancel)
			this.dispose();
		if(e.getSource() == use) {
			if(list.isSelectionEmpty()) {
				String errorMessage = "No Value Selected";
				JOptionPane.showMessageDialog(null, errorMessage, "Error", JOptionPane.ERROR_MESSAGE);
			}
			else {
				String value = list.getSelectedValue().toString();
				if(tableEntryForm != null)
					tableEntryForm.replaceValue(value);
				else
					formDataEntry.replaceValue(value);
				this.dispose();
			}
		}
	}

}
