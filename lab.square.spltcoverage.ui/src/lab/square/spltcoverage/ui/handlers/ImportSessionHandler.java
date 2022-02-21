package lab.square.spltcoverage.ui.handlers;

import java.io.IOException;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.PlatformUI;

import lab.square.spltcoverage.io.SpltCoverageReader;
import lab.square.spltcoverage.model.ProductCoverageManager;
import lab.square.spltcoverage.ui.views.SpltCoverageTable;
import lab.square.spltplugin.ui.model.SpltCoverageSession;
import lab.square.spltplugin.ui.model.SpltCoverageSessionManager;

public class ImportSessionHandler extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		DirectoryDialog dialog = new DirectoryDialog(new Shell());

		String execPath = dialog.open();
		String binPath = dialog.open();

		ProductCoverageManager manager = new ProductCoverageManager();
		SpltCoverageReader reader = new SpltCoverageReader(manager, execPath, binPath);
		try {
			reader.read();
		} catch (IOException e) {
			e.printStackTrace();
		}

		SpltCoverageSession session = SpltCoverageSessionManager.getInstance().createSession(manager);

		IViewPart part = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage()
				.findView(SpltCoverageTable.ID);
		if (part instanceof SpltCoverageTable) {
			System.out.println("table found");
			SpltCoverageTable view = (SpltCoverageTable) part;
			view.setInput(session);
		}

		return null;
	}

}
