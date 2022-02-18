package lab.square.spltplugin.ui.dynamic;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.jface.action.IContributionItem;
import org.eclipse.ui.actions.CompoundContributionItem;
import org.eclipse.ui.menus.CommandContributionItem;
import org.eclipse.ui.menus.CommandContributionItemParameter;
import org.eclipse.ui.menus.IWorkbenchContribution;
import org.eclipse.ui.services.IServiceLocator;

import lab.square.spltcoverage.ui.handlers.OpenSessionHandler;
import lab.square.spltplugin.ui.model.SpltCoverageSession;
import lab.square.spltplugin.ui.model.SpltCoverageSessionManager;

public class SessionListDynamic extends CompoundContributionItem implements IWorkbenchContribution {

	IServiceLocator serviceLocator;

	public SessionListDynamic() {
		// TODO Auto-generated constructor stub
	}

	public SessionListDynamic(String id) {
		super(id);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected IContributionItem[] getContributionItems() {

		SpltCoverageSessionManager manager = SpltCoverageSessionManager.getInstance();

		List<SpltCoverageSession> sessions = manager.getSessions();

		IContributionItem[] items = new IContributionItem[sessions.size()];

		int i = 0;
		for (SpltCoverageSession session : sessions) {
			Map<String, String> commandParams = new HashMap();
			commandParams.put(OpenSessionHandler.PARM_SESSIONID, session.getName());

			final CommandContributionItemParameter contributionParameter = new CommandContributionItemParameter(
					serviceLocator, null, "lab.square.spltplugin.ui.commands.openSessionCommand",
					CommandContributionItem.STYLE_PUSH);
			contributionParameter.label = session.getName();
			contributionParameter.visibleEnabled = true;
			contributionParameter.parameters = commandParams;

			items[i++] = new CommandContributionItem(contributionParameter);
		}

		return items;
	}

	@Override
	public void initialize(final IServiceLocator serviceLocator) {
		this.serviceLocator = serviceLocator;
	}

}
