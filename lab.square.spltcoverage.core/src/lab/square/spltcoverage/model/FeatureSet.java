package lab.square.spltcoverage.model;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;

import lab.square.spltcoverage.utils.Tools;

public class FeatureSet {

	Map<String, Boolean> featureSetImpl;
	
	public FeatureSet(Map<String, Boolean> featureSet) {
		featureSetImpl = new HashMap<>(featureSet);
	}

	public FeatureSet() {
		this(Collections.emptyMap());
	}

	public void addFeature(String feature) {
		addFeature(feature, true);
	}

	public void addFeature(String featureName, boolean hasFeature) {
		featureSetImpl.put(featureName, hasFeature);		
	}

	public boolean hasFeature(String feature) {
		if(!featureSetImpl.containsKey(feature))
			return false;
		
		return featureSetImpl.get(feature);
	}
	
	@Override
	public boolean equals(Object obj) {
		if(!(obj instanceof FeatureSet))
			return false;
		
		FeatureSet other = (FeatureSet)obj;
		
		return Tools.featureSetEquals(this.featureSetImpl, other.featureSetImpl);
	}
	
	@Override
	public int hashCode() {
		int hashCode = 0;
		for(Entry<String, Boolean> each : featureSetImpl.entrySet()) {
			if(each.getValue().booleanValue())
				hashCode += Objects.hash(each.getKey());
		}
		
		return hashCode;
	}

}
