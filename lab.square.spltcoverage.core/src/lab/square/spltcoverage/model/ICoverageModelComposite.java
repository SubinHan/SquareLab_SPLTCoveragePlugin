package lab.square.spltcoverage.model;

import java.util.Collection;

public interface ICoverageModelComposite extends ICoverageModelComponent {
	public Collection<ICoverageModelComponent> getChildren();
	public void addChild(ICoverageModelComponent component);
	
}
