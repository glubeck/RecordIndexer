package gui;

import javax.swing.JComponent;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;

public class RootPane extends JSplitPane {

	private MidWin top;
	private LeftPanel bottom;
	private BatchState batchState;
	
public RootPane(BatchState batchState) {
	this.batchState = batchState;
}
	
	public void createPanels() {
		
		this.setOrientation(VERTICAL_SPLIT);
		
		this.setDividerLocation(570);
		
		MidWin midwin = new MidWin();
		midwin.createMidWin(batchState);
		top = midwin;
		
		this.setTopComponent(midwin);
		
		LeftPanel leftpanel = new LeftPanel(batchState);
		leftpanel.createPanels();
		bottom = leftpanel;
		batchState.setBottom(bottom);
		
		
		this.setBottomComponent(leftpanel);
		
		this.setResizeWeight(0.7);
	}

	public MidWin getTop() {
		return top;
	}

	public void setTop(MidWin top) {
		this.top = top;
	}

	public LeftPanel getBottom() {
		return bottom;
	}

	public void setBottom(LeftPanel bottom) {
		this.bottom = bottom;
	}
	
}
