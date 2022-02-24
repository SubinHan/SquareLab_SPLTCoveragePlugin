package lab.square.spltplugin.ui.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.PlatformUI;

import lab.square.spltplugin.ui.model.SpltCoverageSessionManager;
import lab.square.spltplugin.ui.views.SpltCoverageTable;
import lab.square.spltplugin.ui.views.SpltCoverageTree;

public class OpenSessionHandler extends AbstractHandler {

	public static final String PARM_SESSIONID = "lab.square.spltplugin.ui.params.sessionId";
	public static final String PARM_VIEWID = "lab.square.spltplugin.ui.params.viewId";

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		IViewPart part = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage()
				.findView(event.getParameter(PARM_VIEWID));
		if (part instanceof SpltCoverageTable) {
			SpltCoverageTable view = (SpltCoverageTable) part;
			view.setInput(SpltCoverageSessionManager.getInstance().getSession(event.getParameter(PARM_SESSIONID)));
		}
		
		if (part instanceof SpltCoverageTree) {
        	SpltCoverageTree view = (SpltCoverageTree) part;
        	view.setInput(SpltCoverageSessionManager.getInstance().getSession(event.getParameter(PARM_SESSIONID)));
        }

		return null;
	}

}
