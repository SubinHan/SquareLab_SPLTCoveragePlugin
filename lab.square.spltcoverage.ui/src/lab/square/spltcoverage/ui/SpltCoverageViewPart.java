package lab.square.spltcoverage.ui;

import org.eclipse.jface.viewers.IBaseLabelProvider;
import org.eclipse.jface.viewers.IContentProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.ui.internal.dialogs.ViewContentProvider;
import org.eclipse.ui.internal.dialogs.ViewLabelProvider;
import org.eclipse.ui.part.ViewPart;

public class SpltCoverageViewPart extends ViewPart {
	
	private TableColumn productColumn;
	private TableColumn instructionColumn;
	private TableColumn lineColumn;
	private TableColumn branchColumn;
	private TableColumn methodColumn;
	private TableColumn featureColumn;
	private TableColumn problemColumn;
	
	private static int COUNTER_COLUMN_WIDTH = 50;

	public SpltCoverageViewPart() {
		super();
	}

	@Override
	public void createPartControl(Composite parent) {
		TableViewer viewer;
		
		viewer = new TableViewer(parent, SWT.H_SCROLL | SWT.V_SCROLL | SWT.MULTI | SWT.FULL_SELECTION);
		final Table table = viewer.getTable();
		
		productColumn = new TableColumn(table, SWT.LEFT);
		productColumn.setText("Products");
		productColumn.setWidth(30);
		
		instructionColumn = new TableColumn(table, SWT.LEFT);
		instructionColumn.setText("Instruction Ratio");
		instructionColumn.setWidth(COUNTER_COLUMN_WIDTH);

		lineColumn = new TableColumn(table, SWT.LEFT);
		lineColumn.setText("Line Ratio");
		lineColumn.setWidth(COUNTER_COLUMN_WIDTH);

		branchColumn = new TableColumn(table, SWT.LEFT);
		branchColumn.setText("Branch Ratio");
		branchColumn.setWidth(COUNTER_COLUMN_WIDTH);

		methodColumn = new TableColumn(table, SWT.LEFT);
		methodColumn.setText("Method Ratio");
		methodColumn.setWidth(COUNTER_COLUMN_WIDTH);
		
		featureColumn = new TableColumn(table, SWT.LEFT);
		featureColumn.setText("Features");
		featureColumn.setWidth(200);
		
		problemColumn = new TableColumn(table, SWT.LEFT);
		problemColumn.setText("isProblem");
		problemColumn.setWidth(10);
		
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		
		viewer.setContentProvider(new IContentProvider() {
		});
		
		viewer.setLabelProvider(new IBaseLabelProvider() {
			
			@Override
			public void removeListener(ILabelProviderListener listener) {
				
			}
			
			@Override
			public boolean isLabelProperty(Object element, String property) {
				return false;
			}
			
			@Override
			public void dispose() {
				
			}
			
			@Override
			public void addListener(ILabelProviderListener listener) {
				
			}
		});
		
		viewer.setInput(getViewSite());
	}

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub
		
	}

}
