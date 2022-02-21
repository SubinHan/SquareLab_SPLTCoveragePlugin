package lab.square.spltcoverage.ui.views;

import java.util.List;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;

import lab.square.spltplugin.ui.model.SpltCoverageSession;
import lab.square.spltplugin.ui.model.SpltCoverageSessionManager;

public class SpltCoverageTableContentProvider implements IStructuredContentProvider {

	private SpltCoverageSession session;
	
	@Override
	public Object[] getElements(Object inputElement) {
		if(inputElement == null)
			return null;
		
		SpltCoverageSession session;
		
		session = (SpltCoverageSession) inputElement;
		
		System.out.println(session.getId());
		return session.getItems();
	}

}
