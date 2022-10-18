package lab.square.spltcoverage.io;

import java.util.Collection;

import lab.square.spltcoverage.model.FeatureSet;

public interface IFeatureSetReader {
	public FeatureSet read();
	public Collection<FeatureSet> readAll();
}
