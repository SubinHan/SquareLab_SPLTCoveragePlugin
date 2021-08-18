package lab.square.spltcoverage.ui;

import org.eclipse.core.resources.IFile;
import org.eclipse.jdt.core.ElementChangedEvent;
import org.eclipse.jdt.core.IElementChangedListener;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.ui.IPartListener;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.internal.Workbench;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

import lab.square.spltcoverage.ui.handlers.SampleHandler;

/**
 * The activator class controls the plug-in life cycle
 */
public class Activator extends AbstractUIPlugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "lab.square.spltcoverage.ui"; //$NON-NLS-1$

	// The shared instance
	private static Activator plugin;
	
	/**
	 * The constructor
	 */
	public Activator() {
	}

	@Override
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
		
		JavaCore.addElementChangedListener(new IElementChangedListener() {
			@Override
			public void elementChanged(ElementChangedEvent event) {
				System.out.println("element Changed");
				
			}
		});
		
		Workbench.getInstance().getActiveWorkbenchWindow().getPartService().addPartListener(new IPartListener() {
			@Override
			public void partActivated(IWorkbenchPart part) {
				System.out.println("part activated");
				SampleHandler.testPrintCvoerage(part.getSite().getPage().getActiveEditor().getEditorInput().getAdapter(IFile.class));
			}
			@Override
			public void partBroughtToTop(IWorkbenchPart part) {
				System.out.println("part brought to top");
			}

			@Override
			public void partClosed(IWorkbenchPart part) {
				System.out.println("part closed");
			}

			@Override
			public void partDeactivated(IWorkbenchPart part) {
				System.out.println("part deactivated");
			}
			@Override
			public void partOpened(IWorkbenchPart part) {
				System.out.println("part opened");
				System.out.println(part.getTitle());
			}
		});
	}

	@Override
	public void stop(BundleContext context) throws Exception {
		plugin = null;
		super.stop(context);
	}

	/**
	 * Returns the shared instance
	 *
	 * @return the shared instance
	 */
	public static Activator getDefault() {
		return plugin;
	}

}
