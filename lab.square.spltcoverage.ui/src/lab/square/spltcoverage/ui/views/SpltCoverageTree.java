package lab.square.spltcoverage.ui.views;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.ui.part.ViewPart;

public class SpltCoverageTree extends ViewPart {

	public SpltCoverageTree() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void createPartControl(Composite parent) {
		Tree tree = new Tree(parent, SWT.MULTI);
		tree.setHeaderVisible(true);
		tree.setLinesVisible(true);
		
		
	}

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub

	}

}
