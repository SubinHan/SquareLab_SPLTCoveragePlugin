package lab.square.spltcoverage.ui.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.PlatformUI;

import lab.square.spltcoverage.ui.views.SpltCoverageTable;
import lab.square.spltplugin.ui.model.SpltCoverageSessionManager;

public class OpenSessionHandler extends AbstractHandler {

	public static final String PARM_SESSIONID = "lab.square.spltplugin.ui.params.sessionId";
	
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		IViewPart part = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage()
	            .findView(SpltCoverageTable.ID);
	        if (part instanceof SpltCoverageTable) {
	        	SpltCoverageTable view = (SpltCoverageTable) part;
	        	view.setInput(SpltCoverageSessionManager.getInstance().getSession(event.getParameter(PARM_SESSIONID)));
	        }
	   
		return null;
	}

}
