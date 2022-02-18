package lab.square.spltcoverage.ui.views;

import java.io.IOException;

import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.ui.part.ViewPart;

import lab.square.spltcoverage.io.SpltCoverageReader;
import lab.square.spltcoverage.model.ProductCoverageManager;
import lab.square.spltplugin.ui.model.SpltCoverageSessionManager;

public class SpltCoverageView extends ViewPart {
	
	private TableColumn productColumn;
	private TableColumn instructionColumn;
	private TableColumn lineColumn;
	private TableColumn branchColumn;
	private TableColumn methodColumn;
	private TableColumn featureColumn;
	private TableColumn problemColumn;
	
	private static int COUNTER_COLUMN_WIDTH = 80;

	public SpltCoverageView() {
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
		
		instructionColumn = new TableColumn(table, SWT.CENTER);
		instructionColumn.setText("Instruction Ratio");
		instructionColumn.setWidth(COUNTER_COLUMN_WIDTH);

		lineColumn = new TableColumn(table, SWT.CENTER);
		lineColumn.setText("Line Ratio");
		lineColumn.setWidth(COUNTER_COLUMN_WIDTH);

		branchColumn = new TableColumn(table, SWT.CENTER);
		branchColumn.setText("Branch Ratio");
		branchColumn.setWidth(COUNTER_COLUMN_WIDTH);

		methodColumn = new TableColumn(table, SWT.CENTER);
		methodColumn.setText("Method Ratio");
		methodColumn.setWidth(COUNTER_COLUMN_WIDTH);
		
		featureColumn = new TableColumn(table, SWT.LEFT);
		featureColumn.setText("Features");
		featureColumn.setWidth(700);
		
		problemColumn = new TableColumn(table, SWT.CENTER);
		problemColumn.setText("isProblem");
		problemColumn.setWidth(30);
		
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		
		viewer.setContentProvider(new SpltCoverageViewContentProvider());
		viewer.setLabelProvider(new SpltCoverageViewLabelProvider());
		viewer.setInput(SpltCoverageSessionManager.getInstance());
	}

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub
		
	}

}
