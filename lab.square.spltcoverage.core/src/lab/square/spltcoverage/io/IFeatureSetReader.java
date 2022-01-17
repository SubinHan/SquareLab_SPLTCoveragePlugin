package lab.square.spltcoverage.io;

import java.util.Collection;
import java.util.Map;

public interface IFeatureSetReader {
	public Map<String, Boolean> read();
	public Collection<Map<String, Boolean>> readAll();
}
