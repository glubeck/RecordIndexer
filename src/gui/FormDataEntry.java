package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

import spell.ISpellCorrector;
import spell.ISpellCorrector.NoSimilarWordFoundException;
import spell.SpellCorrector;
import model.Field;

public class FormDataEntry extends JSplitPane implements ActionListener{

	JPanel fieldPanel;
	JList list;
	List<JTextField> textFields;
	BatchState batchState;
	private String inputword;
	private ISpellCorrector Corrector;
	private JPopupMenu popupMenu;
	private JMenuItem suggestionItem;
	private JTextField TextField;
	private int recordsPerImage;
	private Object[][] formData;
	private int index;
	private int column;
	private int fieldRow;
	private int fieldColumn;
	
	public FormDataEntry(int recordsPerImage, List<Field> fields, BatchState batchState) {
		
		batchState.setFormDataEntry(this);
		//index = 0;
		this.recordsPerImage = recordsPerImage;
		popupMenu = new JPopupMenu("See Suggestions");
		suggestionItem = new JMenuItem("See Suggestions");
		suggestionItem.addActionListener(this);
		popupMenu.add(suggestionItem);
		textFields = new ArrayList<JTextField>();
		this.batchState = batchState;
		Object[] rows = createList(recordsPerImage);
		

		list = new JList(rows); //data has type Object[]
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		list.setLayoutOrientation(JList.HORIZONTAL_WRAP);
		list.setSelectedIndex(index);
		
		ListSelectionModel listSelectionMode = list.getSelectionModel();
		listSelectionMode.addListSelectionListener(new SharedListSelectionHandler());
		
		JScrollPane listScroller = new JScrollPane(list);
		listScroller.setSize(new Dimension(80, 200));
		listScroller.setAlignmentX(LEFT_ALIGNMENT);
		Dimension minimumSize = new Dimension(100, 50);
		listScroller.setMinimumSize(minimumSize);
		
		this.setLeftComponent(listScroller);
		
		
		
		fieldPanel = new textFieldList(fields);
		
		formData = makeFormTable();
		batchState.setFormData(formData);
		
		fieldPanel.setAlignmentY(TOP_ALIGNMENT);
		
		JScrollPane fieldScroller = new JScrollPane(fieldPanel);
		fieldScroller.setSize(new Dimension(80, 200));
		fieldScroller.setAlignmentX(RIGHT_ALIGNMENT);
		fieldScroller.setAlignmentY(TOP_ALIGNMENT);
		this.setRightComponent(fieldScroller);
		


		this.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
	}
	
	public void setInvisible() {
		list.setVisible(false);
		fieldPanel.setVisible(false);
	}
	
	private Object[] createList(int recordsPerImage) {
		
		Object[] rows = new Object[recordsPerImage];
		
		for(int i = 0; i < recordsPerImage; i++)
			rows[i] = i+1;
		
		return rows;
	}
	
	private class SharedListSelectionHandler implements ListSelectionListener {
	    public void valueChanged(ListSelectionEvent e) {
	        ListSelectionModel lsm = (ListSelectionModel)e.getSource();

	        index = lsm.getMinSelectionIndex();
	        int Index = index;
	        batchState.setIndex(index);
	        
	        for(int i = 0; i < textFields.size(); i++) {
	        	
	        	if(String.valueOf(formData[index][i]).equals("null"))
	        		textFields.get(i).setText("");
	        	
	        	else
	        		textFields.get(i).setText(String.valueOf(formData[index][i]));
	        

	        	makeHighlights(i);
	        }
	    }
	}
	
	private class textFieldList extends JPanel implements FocusListener {
		
		private JTextField textField2;

		public textFieldList(List<Field> fields) {
			
			setLayout(new GridBagLayout());
			GridBagConstraints c = new GridBagConstraints();
			
			for( int i = 0; i < fields.size(); i++) {
			final int j = i;
			c.gridx = 0;
			c.gridy = i;
			c.gridwidth = 1;
			c.insets = new Insets(10, 10, 10, 10);
			this.add(new JLabel(fields.get(i).getTitle()), c);
			c.gridx = 1;
			c.gridy = i;
			c.gridwidth = 1;
			c.insets = new Insets(10, 10, 10, 10);
			final JTextField textField= new JTextField("", 8);
			textField.addFocusListener(this);
			fieldColumn = i;
			textField.addMouseListener(new MouseAdapter() {
			
				public void mouseClicked(MouseEvent e) {
					if(SwingUtilities.isRightMouseButton(e))
					{
						TextField = textField;
						inputword = textField.getText();
						ISpellCorrector corrector = new SpellCorrector();
						
						if(!batchState.getFields().get(j).getKnownData().equals("")) {
							column = j;
							
						Field field = batchState.getFields().get(j);
						StringBuilder sb = new StringBuilder(batchState.getURL_PATH() + 
								"/database/Records/" + field.getKnownData());
						String knownData = sb.toString();
						
						try {
							URL url = new URL(knownData);
							corrector.useDictionary(url);
							Corrector = corrector;
							String suggestion = corrector.suggestSimilarWord(inputword);
							
							if(!inputword.equals(suggestion) && !inputword.equals("null") && !inputword.equals("")) {

								popupMenu.show(e.getComponent(), e.getX(), e.getY());
							}
					} catch (NoSimilarWordFoundException | IOException e1) {
						e1.printStackTrace();
					}
						}
					}
				}
			});
			textFields.add(textField);
			this.add(textField, c);
			}

		}

		@Override
		public void focusGained(FocusEvent e) {
		
			for(int i = 0; i < textFields.size(); i++) {
				if(e.getSource() == textFields.get(i)) {
					fieldColumn = i;
				}
			}
		}

		@Override
		public void focusLost(FocusEvent e) {
			
			for(int i = 0; i < textFields.size(); i++) {
				if(e.getSource() == textFields.get(i)) {
	
					makeHighlights(i);
					
					
				}
			}
			
			batchState.setTable(formData);
		}		
	}

	
	public int makeHighlights(int i) {
		String inputword = textFields.get(i).getText().toLowerCase();
		formData[index][i] = inputword;
		
		Field field = batchState.getFields().get(i);
	
		StringBuilder sb = new StringBuilder(batchState.getURL_PATH() + 
				"/database/Records/" + field.getKnownData());
		String knownData = sb.toString();
		
		if(inputword.equals("null") || inputword.equals("")) {
			textFields.get(i).setBackground(Color.WHITE);
			return 0;
		}
		
		
		if(field.getKnownData().equals("") && (inputword.matches("\\d+") || inputword.equals(""))) {
			textFields.get(i).setBackground(Color.WHITE);
			return 0;
		}
		else if(field.getKnownData().equals("") && !inputword.matches("\\d+")) {
			textFields.get(i).setBackground(Color.RED);
			return 0;
		}
		
		else if(inputword.matches("\\d+")) {
			textFields.get(i).setBackground(Color.RED);
			return 0;
		}
		
		URL url;
		try {
			url = new URL(knownData);
		ISpellCorrector corrector = new SpellCorrector();
		corrector.useDictionary(url);

		String suggestion = corrector.suggestSimilarWord(inputword);
		
		if(inputword.equals("")) {
			textFields.get(i).setBackground(Color.WHITE);
			return 0;
		}
		else if(!inputword.equals(suggestion))
			textFields.get(i).setBackground(Color.RED);
		
		else
			textFields.get(i).setBackground(Color.WHITE);
		
		
		
		} catch (IOException | NoSimilarWordFoundException e1) {
			e1.printStackTrace();
		}
		return 0;
	}
	
	public void refreshTextFields() {
		
		for(int i = 0; i < textFields.size(); i++) {
        	
        	if(String.valueOf(formData[index][i]).equals("null"))
        		textFields.get(i).setText("");
        	
        	else
        		textFields.get(i).setText(String.valueOf(formData[index][i]));
        
        	
        	makeHighlights(i);
        }
	}
	
	public Object[][] makeFormTable() {
		
		Object[][] table = new Object[recordsPerImage][textFields.size()];
		
		Object[][] dataTable = getTableData(batchState.getTable());
		for(int i = 0; i < table.length; i++) {
			
			for(int j = 0; j < table[i].length; j++) {
				
				table[i][j] = dataTable[i][j+1];
			}
		}
		return table;
	}
	
	private Object[][] getTableData (JTable table) {
	    DefaultTableModel dtm = (DefaultTableModel) table.getModel();
	    int nRow = dtm.getRowCount(), nCol = dtm.getColumnCount();
	    Object[][] tableData = new Object[nRow][nCol];
	    for (int i = 0 ; i < nRow ; i++)
	        for (int j = 0 ; j < nCol ; j++)
	            tableData[i][j] = dtm.getValueAt(i,j);
	    return tableData;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == suggestionItem) {
			Set<String> suggestions = Corrector.getSimilarWords(inputword);
			Suggestions sugg = new Suggestions(suggestions, this);
		}
		
	}

	public void replaceValue(String value) {
		TextField.setText(value);
		TextField.setBackground(Color.white);
		formData[index][column] = value;
		
	}

	public void setFormData(Object[][] formData) {
		this.formData = formData;
		
	}

	public void forceLoseFocus() {
		
		
		int index = batchState.getIndex();
		int fieldcolumn = fieldColumn;
		batchState.setTableSelectedCell(batchState.getIndex(),fieldColumn);
		if(TextField!=null)
		TextField.setFocusable(false);
		
	}

	public void setHighlightedCell(int row, int column) {
		list.setSelectedIndex(row);

		if(column > 0) {
		textFields.get(fieldColumn).grabFocus();
		textFields.get(fieldColumn).requestFocus();
		}
	}
}
