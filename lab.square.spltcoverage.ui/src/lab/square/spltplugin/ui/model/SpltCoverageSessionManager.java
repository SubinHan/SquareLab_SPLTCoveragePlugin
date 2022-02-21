package lab.square.spltplugin.ui.model;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import lab.square.spltcoverage.io.SpltCoverageReader;
import lab.square.spltcoverage.model.ProductCoverageManager;

public class SpltCoverageSessionManager {
	private static SpltCoverageSessionManager instance;
	
	private List<SpltCoverageSession> sessions;
	
	private SpltCoverageSessionManager() {
		sessions = new LinkedList<SpltCoverageSession>();
	}
	
	public static SpltCoverageSessionManager getInstance() {
		if(instance == null)
			instance = new SpltCoverageSessionManager();
		
		return instance;
	}
	
	public SpltCoverageSession createSession(ProductCoverageManager productCoverageManager) {
		SpltCoverageSession toAdd = new SpltCoverageSession(productCoverageManager);
		sessions.add(toAdd);
		
		return toAdd;
	}
	
	public List<SpltCoverageSession> getSessions() {
		return sessions;
	}
	
	public SpltCoverageSession getSession(String id) {
		for (SpltCoverageSession session : sessions) {
			if(session.getId().equals(id))
				return session;
		}
		
		return null;
	}
	
}
