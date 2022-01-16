package lab.square.spltcoverage.core.analysis;

import java.io.File;

public interface ICoverageRunner {
	public Class[] getTestClasses();
	public String getClassPath();
	public File getBaseDir();
}
