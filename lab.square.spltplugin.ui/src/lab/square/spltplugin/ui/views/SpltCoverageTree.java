package lab.square.spltplugin.ui.views;

import org.eclipse.jface.viewers.CellLabelProvider;
import org.eclipse.jface.viewers.IBaseLabelProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.TreeViewerColumn;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.ui.part.ViewPart;

public class SpltCoverageTree extends ViewPart {

	public static final Object LOADING_ELEMENT = new Object();

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
		
//		final TreeViewerColumn column0 = new TreeViewerColumn(viewer, SWT.LEFT);
//		column0.setLabelProvider(new CellLabelProvider() {
//			@Override
//			public void update(ViewerCell cell) {
//				if (cell.getElement() == LOADING_ELEMENT) {
//					cell.setText("");
//					cell.setImage(null);
//				} else {
//					cell.setText(cellTextConverter.getElementName(cell.getElement()));
//					cell.setImage(null);
//				}
//			}
//		});

	}

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub

	}

}
