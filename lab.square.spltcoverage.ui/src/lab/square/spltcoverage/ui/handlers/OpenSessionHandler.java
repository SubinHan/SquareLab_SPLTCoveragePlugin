package lab.square.spltcoverage.ui.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.common.NotDefinedException;

import lab.square.spltplugin.ui.model.SpltCoverageSession;

public class OpenSessionHandler extends AbstractHandler {

	public static final String PARM_SESSIONID = "lab.square.spltplugin.ui.params.sessionId";
	
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		System.out.println(event.getParameter(PARM_SESSIONID));
		return null;
	}

}
