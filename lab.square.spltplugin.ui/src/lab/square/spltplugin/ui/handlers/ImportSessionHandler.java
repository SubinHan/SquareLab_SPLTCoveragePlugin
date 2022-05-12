package lab.square.spltplugin.ui.handlers;

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
import lab.square.spltplugin.ui.model.SpltCoverageSession;
import lab.square.spltplugin.ui.model.SpltCoverageSessionManager;
import lab.square.spltplugin.ui.views.SpltCoverageTable;
import lab.square.spltplugin.ui.views.SpltCoverageTree;

public class ImportSessionHandler extends AbstractHandler {

	public static final String PARM_VIEWID = "lab.square.spltplugin.ui.params.viewId";

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		Shell shell = new Shell();
		
		DirectoryDialog dialog = new DirectoryDialog(shell);

		shell.setText("*.exec file path");
		String execPath = dialog.open();

		shell.setText("bin file path");
		String binPath = dialog.open();

		ProductCoverageManager manager = new ProductCoverageManager("SPL");
		SpltCoverageReader reader = new SpltCoverageReader(manager, execPath, binPath);
		try {
			reader.read();
		} catch (IOException e) {
			e.printStackTrace();
		}

		SpltCoverageSession session = SpltCoverageSessionManager.getInstance().createSession(manager);

		IViewPart part = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage()
				.findView(event.getParameter(PARM_VIEWID));
		if (part instanceof SpltCoverageTable) {
			System.out.println("table found");
			SpltCoverageTable view = (SpltCoverageTable) part;
			view.setInput(session);
		}
		if (part instanceof SpltCoverageTree) {
			System.out.println("tree found");
			SpltCoverageTree view = (SpltCoverageTree) part;
			view.setInput(session);
		}

		return null;
	}

}
