package lab.square.spltplugin.ui.views;

import org.eclipse.jface.viewers.CellLabelProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.TreeViewerColumn;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.ui.part.ViewPart;

import lab.square.spltcoverage.model.ICoverageModelComponent;
import lab.square.spltplugin.ui.model.SpltCoverageSession;

public class SpltCoverageTree extends ViewPart {

	public static final Object LOADING_ELEMENT = new Object();
	
	private static final String HEADER_COLUMN0 = "Name";
	private static final String HEADER_COLUMN1 = "Instruction Ratio";
	private static final String HEADER_COLUMN2 = "Line Ratio";
	private static final String HEADER_COLUMN3 = "Branch Ratio";
	private static final String HEADER_COLUMN4 = "Method Ratio";
	private static final String HEADER_COLUMN5 = "Number of Features";
	private static final String HEADER_COLUMN6 = "Feature Set";
	
	private static final int WIDTH_RATIO = 100;
	

	private CellTextConverter cellTextConverter;
	private TreeViewer viewer;

	public SpltCoverageTree() {
		cellTextConverter = new CellTextConverter();
	}

	@Override
	public void createPartControl(Composite parent) {
		Tree tree = new Tree(parent, SWT.MULTI);
		tree.setHeaderVisible(true);
		tree.setLinesVisible(true);

		viewer = new TreeViewer(tree);
		
		final TreeViewerColumn column0 = new TreeViewerColumn(viewer, SWT.LEFT);
		column0.getColumn().setWidth(300);
		column0.getColumn().setText(HEADER_COLUMN0);
		column0.setLabelProvider(new CellLabelProvider() {
			@Override
			public void update(ViewerCell cell) {
				if (cell.getElement() == LOADING_ELEMENT) {
					cell.setText("");
					cell.setImage(null);
				} else {
					cell.setText(cellTextConverter.getElementName(cell.getElement()));
					cell.setImage(null);
				}
			}
		});
		
		final TreeViewerColumn column1 = new TreeViewerColumn(viewer, SWT.LEFT);
		column1.getColumn().setWidth(WIDTH_RATIO);
		column1.getColumn().setText(HEADER_COLUMN1);
		column1.setLabelProvider(new CellLabelProvider() {
			@Override
			public void update(ViewerCell cell) {
				if (cell.getElement() == LOADING_ELEMENT) {
					cell.setText("");
					cell.setImage(null);
				} else {
					cell.setText(cellTextConverter.getInstructionRatio(((ICoverageModelComponent)cell.getElement()).getClassCoverages()));
					cell.setImage(null);
				}
			}
		});
		
		final TreeViewerColumn column2 = new TreeViewerColumn(viewer, SWT.LEFT);
		column2.getColumn().setWidth(WIDTH_RATIO);
		column2.getColumn().setText(HEADER_COLUMN2);
		column2.setLabelProvider(new CellLabelProvider() {
			@Override
			public void update(ViewerCell cell) {
				if (cell.getElement() == LOADING_ELEMENT) {
					cell.setText("");
					cell.setImage(null);
				} else {
					cell.setText(cellTextConverter.getLineRatio(((ICoverageModelComponent)cell.getElement()).getClassCoverages()));
					cell.setImage(null);
				}
			}
		});
		
		final TreeViewerColumn column3 = new TreeViewerColumn(viewer, SWT.LEFT);
		column3.getColumn().setWidth(WIDTH_RATIO);
		column3.getColumn().setText(HEADER_COLUMN3);
		column3.setLabelProvider(new CellLabelProvider() {
			@Override
			public void update(ViewerCell cell) {
				if (cell.getElement() == LOADING_ELEMENT) {
					cell.setText("");
					cell.setImage(null);
				} else {
					cell.setText(cellTextConverter.getBranchRatio(((ICoverageModelComponent)cell.getElement()).getClassCoverages()));
					cell.setImage(null);
				}
			}
		});
		
		final TreeViewerColumn column4 = new TreeViewerColumn(viewer, SWT.LEFT);
		column4.getColumn().setWidth(WIDTH_RATIO);
		column4.getColumn().setText(HEADER_COLUMN4);
		column4.setLabelProvider(new CellLabelProvider() {
			@Override
			public void update(ViewerCell cell) {
				if (cell.getElement() == LOADING_ELEMENT) {
					cell.setText("");
					cell.setImage(null);
				} else {
					cell.setText(cellTextConverter.getMethodRatio(((ICoverageModelComponent)cell.getElement()).getClassCoverages()));
					cell.setImage(null);
				}
			}
		});
		
		final TreeViewerColumn column5 = new TreeViewerColumn(viewer, SWT.LEFT);
		column5.getColumn().setWidth(100);
		column5.getColumn().setText(HEADER_COLUMN5);
		column5.setLabelProvider(new CellLabelProvider() {
			@Override
			public void update(ViewerCell cell) {
				if (cell.getElement() == LOADING_ELEMENT) {
					cell.setText("");
					cell.setImage(null);
				} else {
					cell.setText(cellTextConverter.getNumOfFeatures(cell.getElement()));
					cell.setImage(null);
				}
			}
		});
		
		final TreeViewerColumn column6 = new TreeViewerColumn(viewer, SWT.LEFT);
		column6.getColumn().setWidth(300);
		column6.getColumn().setText(HEADER_COLUMN6);
		column6.setLabelProvider(new CellLabelProvider() {
			@Override
			public void update(ViewerCell cell) {
				if (cell.getElement() == LOADING_ELEMENT) {
					cell.setText("");
					cell.setImage(null);
				} else {
					cell.setText(cellTextConverter.getFeatureSet(cell.getElement()));
					cell.setImage(null);
				}
			}
		});
		
		viewer.setContentProvider(new SpltCoverageTreeContentProvider());
		
	}

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub

	}
	
	public void setInput(SpltCoverageSession session) {
		System.out.println("setInput");
		this.viewer.setInput(session);
		this.viewer.refresh();
	}

}
