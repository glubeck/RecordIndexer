package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Set;
import java.util.Vector;
import java.util.regex.Pattern;

import javax.swing.DefaultCellEditor;
import javax.swing.JComponent;
import javax.swing.JEditorPane;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

import spell.ISpellCorrector;
import spell.ISpellCorrector.NoSimilarWordFoundException;
import spell.SpellCorrector;
import dbaccess.DAimage;
import model.Field;

public class TableEntryForm extends JPanel implements ActionListener{

	private JTable table;
	private BatchState batchState;
	private JScrollPane scrollPane;
	private JPopupMenu popupMenu;
	private JMenuItem suggestionItem;
	private String inputword;
	private ISpellCorrector Corrector;
	private Object[][] data;
	private int cellRow;
	private int cellColumn;

	
	public TableEntryForm(int rows, int columns, List<Field> fields, BatchState batchState) {
		
		this.batchState = batchState;
		batchState.setTableEntryForm(this);
		if(batchState.getData() == null) 
			data = createRowNumbers(rows, columns);
		else 
			data = batchState.getData();
			
		batchState.setData(data);
		String[] columnNames = createColumnNames(fields);
		MyTableModel model = new MyTableModel(data, columnNames);
		final JTable table = new JTable(model);
		this.table = table;
		
		final JPopupMenu popupMenu = new JPopupMenu();
		suggestionItem = new JMenuItem("See Suggestions");
		suggestionItem.addActionListener(this);
		popupMenu.add(suggestionItem);
		
		this.popupMenu = popupMenu;
		

		
		table.setDefaultEditor(Object.class,  new DefaultCellEditor(new JTextField()) {});
		table.setDefaultRenderer(Object.class,  new DefaultTableCellRenderer() 
		{
			public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column)
			{
				
				Component c = super.getTableCellRendererComponent(table,  value,  isSelected,  hasFocus,  row,  column);
				
				data = getTableData(table);
				getBatchState().setFormData(data);
				getBatchState().setData(data);
				
				if(isSelected) {
					c.setBackground(new Color(0,0,182,70));
					cellRow = row;
					cellColumn = column;
					getBatchState().setCellRowAndColumn(row, column);
					getBatchState().setHighlightedCell();
					return c;
				}
				
				String cv = String.valueOf(value).toLowerCase();
				if(cv.equals("null") || cv.equals("")) {
					c.setBackground(Color.WHITE);
					return c;
				}
				
				@SuppressWarnings("unused")
				int love = column;
				if (column > 0) {
				ISpellCorrector corrector = new SpellCorrector();
				
				try {
					Field field = getBatchState().getFields().get(column-1);
					
					if(field.getKnownData().equals("") && !cv.matches("\\d+")) {
						c.setBackground(Color.RED);
						return c;
					}
					else if(field.getKnownData().equals("") && cv.matches("\\d+")) {
						c.setBackground(Color.WHITE);
						return c;
					}
					else if(cv.matches("\\d+")) {
						c.setBackground(Color.RED);
						return c;
					}
					
					StringBuilder sb = new StringBuilder(getBatchState().getURL_PATH() + 
							"/database/Records/" + field.getKnownData());
					String knownData = sb.toString();
					
					URL url = new URL(knownData);
					corrector.useDictionary(url);
					

				String suggestion = corrector.suggestSimilarWord(cv);
				
				if(!cv.equals(suggestion))
					c.setBackground(Color.RED);
				else
					c.setBackground(Color.WHITE);
				return c;
				
				} catch (IOException | NoSimilarWordFoundException e) {
					e.printStackTrace();
				}
				}
				c.setBackground(Color.WHITE);
				return c;
			}
		});
		
		table.addMouseListener(new MouseAdapter() {
			
			

			public void mouseClicked(MouseEvent e) {
				if(SwingUtilities.isRightMouseButton(e))
				{
					int row = table.rowAtPoint(e.getPoint());
					int column = table.columnAtPoint(e.getPoint());
					table.changeSelection(row,column,false,false);
					
					TableCellRenderer cellRenderer = table.getCellRenderer(row, column);
					Component rendererComponent = cellRenderer.getTableCellRendererComponent(table, null, false, true, 0, 0);
					Color cellColor = rendererComponent.getBackground();
					
					String value = String.valueOf(table.getModel().getValueAt(row, column));
					inputword = value;
					ISpellCorrector corrector = new SpellCorrector();
					
					if(!getBatchState().getFields().get(column-1).getKnownData().equals("")) {

					Field field = getBatchState().getFields().get(column-1);
					StringBuilder sb = new StringBuilder(getBatchState().getURL_PATH() + 
							"/database/Records/" + field.getKnownData());
					String knownData = sb.toString();
					
					try {
						URL url = new URL(knownData);
						corrector.useDictionary(url);
						Corrector = corrector;
						String suggestion = corrector.suggestSimilarWord(value);
						
						if(!value.equals(suggestion) && !value.equals("null") && !value.equals("")) {

							popupMenu.show(e.getComponent(), e.getX(), e.getY());
						}
				} catch (NoSimilarWordFoundException | IOException e1) {
					e1.printStackTrace();
				}
					}
				}
			}
		});
		ListSelectionModel cellSelectionModel = this.table.getSelectionModel();
		cellSelectionModel.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		
		cellSelectionModel.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				String selectedData = null;
				
				int selectedRow = table.getSelectionModel().getLeadSelectionIndex();
				int selectedColumn = table.getColumnModel().getSelectionModel().getLeadSelectionIndex();
				
				data = getTableData(table);
				getBatchState().setFormData(data);
				getBatchState().setData(data);
		
			}
		});
		
		this.table.getColumnModel().getSelectionModel().addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				String selectedData = null;
				
				int selectedRow = table.getSelectionModel().getLeadSelectionIndex();
				int selectedColumn = table.getColumnModel().getSelectionModel().getLeadSelectionIndex()-1;
				
				if(selectedColumn >= 0) {
				StringBuilder sb = new StringBuilder(getBatchState().getURL_PATH() + "/database/Records/" + 
						getBatchState().getFields().get(selectedColumn).getHelphtml());
				String mainURL = sb.toString();
				try {
					JEditorPane p = new JEditorPane();
					p.setPage(mainURL);
					JEditorPane p2 = new JEditorPane("text/html", p.getText());
					getBatchState().setHtmlPane(p2);
					getBatchState().getHelpPane().setViewportView(p2);
					
					getBatchState().getHelpPane().repaint();
					data = getTableData(table);
					getBatchState().setFormData(data);
					getBatchState().setData(data);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				}
			}
		});
		
		
		
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.setCellSelectionEnabled(true);
		this.table = table;
		batchState.setTable(table);
		
		JScrollPane pane = new JScrollPane(table, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, 
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		pane.setPreferredSize(new Dimension(280, 250));
		
	
		this.setLayout(new BorderLayout());
		this.add(pane, BorderLayout.CENTER);
		this.scrollPane = pane;
		
	}
	
	
	protected BatchState getBatchState() {
		return batchState;
		
	}


	public Object[][] getTableData (JTable table) {
	    DefaultTableModel dtm = (DefaultTableModel) table.getModel();
	    int nRow = dtm.getRowCount(), nCol = dtm.getColumnCount()-1;
	    Object[][] tableData = new Object[nRow][nCol];
	    for (int i = 0 ; i < nRow ; i++)
	        for (int j = 0 ; j < nCol ; j++)
	            tableData[i][j] = dtm.getValueAt(i,j+1);
	    batchState.setData(tableData);
	    return tableData;
	}
	
	private String[] createColumnNames(List<Field> fields) {
		
		String[] titles = new String[fields.size()+1];
		
		for(int i = 0; i < fields.size()+1; i++) {
			
			if(i==0)
				titles[0] = "Record Number";
			else {
				titles[i] = fields.get(i-1).getTitle();
			}
		}
		
		return titles;
	}
	
	private Object[][] createRowNumbers(int rows, int columns) {
		
		Object[][] data = new Object[rows][columns];
		
		for(int i = 0; i < rows; i++) {
			
				
				data[i][0] = i+1;
		}
		batchState.setData(data);
		
		return data;
	}
	
	public class MyTableModel extends DefaultTableModel {

		   public MyTableModel(Object[][] tableData, Object[] colNames) {
		      super(tableData, colNames);
		   }

		   @Override
		   public boolean isCellEditable(int row, int column) {
			   switch(column) {
			   case 0:
				   return false;
		       default:
		    	   return true;
			   }
		   }


		   


	public JTable getTable() {
		return table;
	}
	
	public JPopupMenu getPopupMenu() {
		return popupMenu;
	}
	

	}

	public void setInvisible() {
		table.setVisible(false);
		scrollPane.setVisible(false);
	}

	public void replaceValue(String value) {
		
		int row = table.getSelectionModel().getLeadSelectionIndex();
		int column = table.getColumnModel().getSelectionModel().getLeadSelectionIndex();

		table.setValueAt(value, row, column);
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		
		if(e.getSource() == suggestionItem) {
			Set<String> suggestions = Corrector.getSimilarWords(inputword);
			Suggestions sugg = new Suggestions(suggestions, this);
			
		}
		
	}


	public void setSelectedCell(int row, int column) {
		table.changeSelection(row, column+1, false, false);
		table.requestFocus();
		
	}
}
