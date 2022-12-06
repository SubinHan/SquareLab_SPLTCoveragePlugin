package lab.square.spltcoverage.model;

import java.util.Collection;

import org.jacoco.core.analysis.IClassCoverage;

public interface ICoverageModelComponent {
	public Collection<IClassCoverage> getClassCoverages();
	public String getName();
	public void addClassCoverages(Collection<IClassCoverage> classCoverages);
	public Class[] getTargetClasses();
	public int getScore();
}
