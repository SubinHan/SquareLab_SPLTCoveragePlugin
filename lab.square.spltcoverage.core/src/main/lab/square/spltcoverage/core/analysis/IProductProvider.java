package lab.square.spltcoverage.core.analysis;

import java.util.Collection;

/**
 * 
 * @author selab
 *
 */
public interface IProductProvider {
	public Collection<String> getTestClassPaths();
	public String getClasspath();	
	public String getOutputPath();
}
