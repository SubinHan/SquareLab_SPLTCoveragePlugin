package lab.square.spltcoverage.ui.views;

import org.eclipse.jface.viewers.ITreeContentProvider;

import lab.square.spltplugin.ui.model.ISpltCoverageItem;
import lab.square.spltplugin.ui.model.SpltCoverageSession;

public class SpltCoverageTreeContentProvider implements ITreeContentProvider {

	@Override
	public Object[] getElements(Object inputElement) {
		if(inputElement == null)
			return null;
		
		if(!(inputElement instanceof SpltCoverageSession))
			return null;
		
		SpltCoverageSession session;
		session = (SpltCoverageSession) inputElement;
		
		return session.getItems();
	}

	@Override
	public Object[] getChildren(Object parentElement) {
		
		
		if(parentElement instanceof ISpltCoverageItem) {
			ISpltCoverageItem item = (ISpltCoverageItem) parentElement;
		}
		
		return null;
	}

	@Override
	public Object getParent(Object element) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean hasChildren(Object element) {
		// TODO Auto-generated method stub
		return false;
	}

}
