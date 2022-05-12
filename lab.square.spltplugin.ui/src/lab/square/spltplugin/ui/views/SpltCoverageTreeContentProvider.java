package lab.square.spltplugin.ui.views;

import java.util.Collection;

import org.eclipse.jface.viewers.ITreeContentProvider;

import lab.square.spltcoverage.model.ICoverageModelComponent;
import lab.square.spltcoverage.model.ICoverageModelComposite;
import lab.square.spltcoverage.model.ProductCoverage;
import lab.square.spltplugin.ui.model.SpltCoverageSession;

public class SpltCoverageTreeContentProvider implements ITreeContentProvider {

	@Override
	public Object[] getElements(Object inputElement) {
		if (inputElement == null)
			return null;

		if (!(inputElement instanceof SpltCoverageSession))
			return null;

		SpltCoverageSession session;
		session = (SpltCoverageSession) inputElement;

		Collection<ProductCoverage> pcs = session.getManager().getProductCoverages();
		return new Object[] { session.getManager() };
	}

	@Override
	public Object[] getChildren(Object parentElement) {

		if (parentElement instanceof ICoverageModelComposite) {
			ICoverageModelComposite item = (ICoverageModelComposite) parentElement;
			
			if(item.getChildren() == null)
				return null;
			if(item.getChildren().isEmpty())
				return null;
			Collection<ICoverageModelComponent> components = item.getChildren();
			return components.toArray(new ICoverageModelComponent[components.size()]);
		}
		
		return null;
	}

	@Override
	public Object getParent(Object element) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean hasChildren(Object element) {
		if (element instanceof ICoverageModelComposite) {
			ICoverageModelComposite item = (ICoverageModelComposite) element;
			
			if(item.getChildren() == null)
				return false;
			if(item.getChildren().isEmpty())
				return false;
			
			return true;
		}
		return false;
	}

}
