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

	/**
	 * Create a FeatureSet with given hash map.
	 * 
	 * @param featureSet
	 */
	public FeatureSet(Map<String, Boolean> featureSet) {
		featureSetImpl = new HashMap<>(featureSet);
	}

	/**
	 * Create an empty FeatureSet.
	 */
	public FeatureSet() {
		this(Collections.emptyMap());
	}

	public static List<String> getAllFeatureList(Collection<FeatureSet> featureSets) {
		Set<String> result = new HashSet<>();

		for (FeatureSet featureSet : featureSets) {
			for (String feature : featureSet.getFeatures()) {
				result.add(feature);
			}
		}

		return result.stream().collect(Collectors.toCollection(ArrayList::new));
	}

	/**
	 * Add the given feature into FeatureSet.
	 * 
	 * @param feature
	 */
	public void addFeature(String feature) {
		setFeature(feature, true);
	}

	/**
	 * Set the feature of FeatureSet true or not.
	 * 
	 * @param feature
	 * @param hasFeature
	 */
	public void setFeature(String feature, boolean hasFeature) {
		featureSetImpl.put(feature, hasFeature);
	}

	/**
	 * Remove the given feature.
	 * 
	 * @param feature
	 */
	public void removeFeature(String feature) {
		featureSetImpl.put(feature, false);
	}

	/**
	 * Returns true if the FeatureSet has the given feature.
	 * 
	 * @param feature
	 * @return
	 */
	public boolean hasFeature(String feature) {
		if (!featureSetImpl.containsKey(feature))
			return false;

		return featureSetImpl.get(feature);
	}

	/**
	 * Returns all features.
	 * 
	 * @return
	 */
	public Collection<String> getFeatures() {
		Set<String> features = new HashSet<>();

		for (Entry<String, Boolean> entry : featureSetImpl.entrySet()) {
			if (entry.getValue().booleanValue())
				features.add(entry.getKey());
		}

		return features;
	}

	/**
	 * Add given FeatureSet and return new FeatureSet object as a result. It behaves
	 * as a Set Theorem. Notice that this object doesn¡¯t change.
	 * 
	 * @param other
	 * @return
	 */
	public FeatureSet add(FeatureSet other) {
		FeatureSet result = new FeatureSet();

		for (Entry<String, Boolean> entry : featureSetImpl.entrySet()) {
			if (entry.getValue().booleanValue())
				result.addFeature(entry.getKey());
		}

		for (Entry<String, Boolean> entry : other.featureSetImpl.entrySet()) {
			if (entry.getValue().booleanValue())
				result.addFeature(entry.getKey());
		}

		return result;
	}

	/**
	 * Subtract given FeatureSet and return new FeatureSet object as a result. It
	 * behaves as a Set Theorem. Notice that this object doesn¡¯t change.
	 * 
	 * @param other
	 * @return
	 */
	public FeatureSet subtract(FeatureSet other) {
		FeatureSet result = new FeatureSet();

		for (Entry<String, Boolean> entry : featureSetImpl.entrySet()) {
			if (entry.getValue().booleanValue())
				result.addFeature(entry.getKey());
		}

		for (Entry<String, Boolean> entry : other.featureSetImpl.entrySet()) {
			if (entry.getValue().booleanValue())
				result.removeFeature(entry.getKey());
		}

		return result;
	}

	/**
	 * Intersect given FeatureSet and return new FeatureSet object as a result. It
	 * behaves as a Set Theorem. Notice that this object doesn¡¯t change.
	 * 
	 * @param other
	 * @return
	 */
	public FeatureSet intersect(FeatureSet other) {
		FeatureSet added = this.add(other);
		FeatureSet leftSide = this.subtract(other);
		FeatureSet rightSide = other.subtract(this);

		return added.subtract(leftSide).subtract(rightSide);
	}

	/**
	 * Returns number of features.
	 * 
	 * @return
	 */
	public int getNumFeatures() {
		return getFeatures().size();
	}

	/**
	 * Returns the differences with the given FeatureSet.
	 * 
	 * @param other
	 * @return
	 */
	public int getHowManyDifferences(FeatureSet other) {
		return this.subtract(other).getNumFeatures() + other.subtract(this).getNumFeatures();
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof FeatureSet))
			return false;

		FeatureSet other = (FeatureSet) obj;

		return Tools.featureSetEquals(this.featureSetImpl, other.featureSetImpl);
	}

	@Override
	public int hashCode() {
		int hashCode = 0;
		for (Entry<String, Boolean> each : featureSetImpl.entrySet()) {
			if (each.getValue().booleanValue())
				hashCode += Objects.hash(each.getKey());
		}

		return hashCode;
	}

	@Override
	public String toString() {
		return featureSetImpl.toString();
	}

}
