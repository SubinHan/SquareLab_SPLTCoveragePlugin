package lab.square.spltplugin.ui.handlers;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceVisitor;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.ISourceRange;
import org.eclipse.jdt.core.IType;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.text.IDocument;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.handlers.HandlerUtil;
import org.jacoco.core.analysis.Analyzer;
import org.jacoco.core.analysis.CoverageBuilder;
import org.jacoco.core.analysis.IClassCoverage;
import org.jacoco.core.analysis.ICounter;
import org.jacoco.core.data.ExecutionDataStore;
import org.jacoco.core.data.SessionInfoStore;
import org.jacoco.core.tools.ExecFileLoader;

import lab.square.spltcoverage.io.SpltCoverageReader;
import lab.square.spltcoverage.model.ProductCoverageManager;

public class SampleHandler extends AbstractHandler {
	HashSet<IFile> javaFiles = new HashSet<IFile>();
	static HashSet<IFile> classFiles = new HashSet<IFile>();
	
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindowChecked(event);
		MessageDialog.openInformation(window.getShell(), "SPLTCoverage", "Hello, Eclipse world");
		
		System.out.println("sample handler executed");

		IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();				
		
		////////////////////////////////
		
		try {
			root.accept(new IResourceVisitor() {
				@Override
				public boolean visit(IResource resource) throws CoreException {
					if (resource instanceof IProject) {
						System.out.println("IProject: " + resource.getName());
						return true;
					}
					if (resource instanceof IFile) {
						System.out.println("IFile: " + resource.getName());
						
						if(isJavaFile(resource))
							javaFiles.add((IFile)resource);
						else if(isClassFile(resource))
							classFiles.add((IFile)resource);
						
						
						ProductCoverageManager manager = new ProductCoverageManager();
						SpltCoverageReader reader = new SpltCoverageReader(manager, resource.getLocation().toString());
						
						testPrintCvoerage(resource);
						
						return false;
					}
					if (resource instanceof IFolder) {
						System.out.println("IFolder: " + resource.getName());
						return true;
					}
					if (resource instanceof IWorkspaceRoot) {
						System.out.println("IWorkspaceRoot: " + resource.getName());
						return true;
					}
					return false;
				}
			});
		} catch (CoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		for(IFile file : javaFiles) {
			System.out.println(file.getName());
			ISourceRange range;
			try {
				IMarker marker = file.createMarker("lab.square.spltcoverage.markers.covered");
				marker.setAttribute(IMarker.CHAR_START, 50);
				marker.setAttribute(IMarker.CHAR_END, 100);
				marker.setAttribute(IMarker.SEVERITY, IMarker.SEVERITY_INFO);
				marker.setAttribute(IMarker.MESSAGE, "hello");
			} catch (CoreException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println(file.getAdapter(IType.class));
			System.out.println(file.getAdapter(IDocument.class));
		}
		
		for(IFile file : classFiles) {
			System.out.println(file.getName());
		}

		//////////////////////
		
//		String directory = "D:/directorypath/bankaccount/";
//		String classDirectory = "D:/workspacechallenege/challenge-master/workspace_IncLing/bankaccount/bin/bankaccount";
//		
//		ProductCoverageManager manager = new ProductCoverageManager();
//		CoverageReader reader = new CoverageReader(manager, directory, classDirectory);
//		try {
//			reader.read();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		
//		Collection<ProductGraph> heads = ProductLinker.link(manager);
//		if(heads.isEmpty() || heads.size() >= 3)
//			System.out.println("My Programming has failed.." + heads.isEmpty());
//		else
//			System.out.println("Successed!");
		
		return null;
	}
	
	public static void testPrintCvoerage(IResource resource) {
		if(resource.getName().endsWith(".java")) {
			testPrintCoverageJava(resource);
			return;
		}
		
		ExecFileLoader execFileLoader = new ExecFileLoader();
		
		try {
			execFileLoader.load(new File("D:/directorypath/bankaccount/Product1/ApplicationTest/ApplicationTestMerged.exec"));
		} catch (IOException e) {
			e.printStackTrace();
		}

		final ExecutionDataStore execStore = execFileLoader.getExecutionDataStore();
		final SessionInfoStore sessionStore = execFileLoader.getSessionInfoStore();

		final CoverageBuilder coverageBuilder = new CoverageBuilder();
		final Analyzer analyzer = new Analyzer(execStore, coverageBuilder);

		try {
			analyzer.analyzeAll(new File(resource.getLocation().toString()));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		Collection<IClassCoverage> ccs = coverageBuilder.getClasses();
		for(IClassCoverage cc : ccs) {
			System.out.println(cc.getId());
			System.out.println(cc.getBranchCounter());
			
			if(cc.getBranchCounter().getCoveredRatio() > 0.f) {
				for(int i = cc.getFirstLine(); i <= cc.getLastLine(); i++) {
					System.out.println(cc.getLine(i).getStatus() == ICounter.FULLY_COVERED ? "Line " + i + ": Covered" : "Line " + i + ": not Covered");
				}
			}
		}
	}
	
	public static void testPrintCoverageJava(IResource resource) {
		if(resource instanceof IFile) {
			IFile file = (IFile)resource;
			
			for(IFile iter : classFiles) {
				if(iter.getName().replace(".class", "").equals(file.getName().replace(".java", ""))) {
					testPrintCvoerage(iter);
					return;
				}
			}
		}
	}
	
	private boolean isClassFile(IResource resource) {
		return resource.getName().endsWith(".class");
	}

	private boolean isJavaFile(IResource resource) {
		return resource.getName().endsWith(".java");
	}
	
	private String getClassName(final IResource classFile) {
		String toReturn = classFile.getName().replaceFirst("[.][^.]+$", "");
		IResource parentFile = classFile.getParent();
		String name = parentFile.getName().replaceFirst("[.][^.]+$", "");
		
		while(!name.equalsIgnoreCase("bin")) {
			toReturn = name.replaceFirst("[.][^.]+$", "").concat("." + toReturn);
			parentFile = parentFile.getParent();
			name = parentFile.getName();
		}

		return toReturn;
	}
	
	private IResource getBinFolder(final IResource resource) {
		IResource toReturn = resource;
		while(!toReturn.getName().equalsIgnoreCase("bin"))
			toReturn = toReturn.getParent();
		return toReturn;
	}
}
