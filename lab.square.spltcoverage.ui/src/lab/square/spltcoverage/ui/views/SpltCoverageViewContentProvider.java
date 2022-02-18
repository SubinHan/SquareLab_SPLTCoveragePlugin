package lab.square.spltcoverage.ui.views;

import java.util.List;

import org.eclipse.jface.viewers.IStructuredContentProvider;

import lab.square.spltplugin.ui.model.SpltCoverageSession;
import lab.square.spltplugin.ui.model.SpltCoverageSessionManager;

public class SpltCoverageViewContentProvider implements IStructuredContentProvider {

	private SpltCoverageSession session;
	
	@Override
	public Object[] getElements(Object inputElement) {
		List<SpltCoverageSession> sessions = SpltCoverageSessionManager.getInstance().getSessions();
		
		Object[] sessionArray = sessions.toArray();
		
		SpltCoverageSession session = (SpltCoverageSession) sessionArray[0];
		
		return session.getItems();
		
		
	}

}
