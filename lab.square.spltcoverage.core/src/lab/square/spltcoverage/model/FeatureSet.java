package lab.square.spltcoverage.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import lab.square.spltcoverage.utils.Tools;

public class FeatureSet {

	Map<String, Boolean> featureSetImpl;
	
	public FeatureSet(Map<String, Boolean> featureSet) {
		featureSetImpl = new HashMap<>(featureSet);
	}

	public FeatureSet() {
		this(Collections.emptyMap());
	}
	
	public static List<String> getAllFeatureList(Collection<FeatureSet> featureSets) {
		Set<String> result = new HashSet<>();
		
		for(FeatureSet featureSet : featureSets) {
			for(String feature : featureSet.getFeatures()) {
				result.add(feature);
			}
		}
		
		return result.stream().collect(Collectors.toCollection(ArrayList::new));
	}

	public void addFeature(String feature) {
		setFeature(feature, true);
	}


	public void setFeature(String featureName, boolean hasFeature) {
		featureSetImpl.put(featureName, hasFeature);		
	}
	
	public void removeFeature(String featureName) {
		featureSetImpl.put(featureName, false);
	}

	public boolean hasFeature(String feature) {
		if(!featureSetImpl.containsKey(feature))
			return false;
		
		return featureSetImpl.get(feature);
	}
	
	public Collection<String> getFeatures() {
		Set<String> features = new HashSet<>();
		
		for (Entry<String, Boolean> entry : featureSetImpl.entrySet()) {
			if(entry.getValue().booleanValue())
				features.add(entry.getKey());
		}
		
		return features;
	}
	
	public FeatureSet add(FeatureSet other) {
		FeatureSet result = new FeatureSet();
		
		for(Entry<String, Boolean> entry : featureSetImpl.entrySet()) {
			if(entry.getValue().booleanValue())
				result.addFeature(entry.getKey());
		}
		
		for(Entry<String, Boolean> entry : other.featureSetImpl.entrySet()) {
			if(entry.getValue().booleanValue())
				result.addFeature(entry.getKey());
		}
		
		return result;
	}
	
	public FeatureSet subtract(FeatureSet other) {
		FeatureSet result = new FeatureSet();
		
		for(Entry<String, Boolean> entry : featureSetImpl.entrySet()) {
			if(entry.getValue().booleanValue())
				result.addFeature(entry.getKey());
		}
		
		for(Entry<String, Boolean> entry : other.featureSetImpl.entrySet()) {
			if(entry.getValue().booleanValue())
				result.removeFeature(entry.getKey());
		}
		
		return result;
	}
	
	public FeatureSet intersect(FeatureSet other) {
		FeatureSet added = this.add(other);
		FeatureSet leftSide = this.subtract(other);
		FeatureSet rightSide = other.subtract(this);
		
		return added.subtract(leftSide).subtract(rightSide);
	}

	public int getNumFeatures() {
		return getFeatures().size();
	}

	public int getHowManyDifferences(FeatureSet other) {
		return this.subtract(other).getNumFeatures() + other.subtract(this).getNumFeatures();
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
	
	@Override
	public String toString() {
		return featureSetImpl.toString();
	}


}
