package lab.square.spltcoverage.core.analysis;

import java.util.Collection;
import java.util.Map;

public interface IFeatureSetReader {
	public Map<String, Boolean> read();
	public Collection<Map<String, Boolean>> readAll();
}
