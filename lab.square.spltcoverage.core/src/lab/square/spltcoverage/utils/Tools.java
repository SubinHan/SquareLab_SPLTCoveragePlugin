package lab.square.spltcoverage.utils;

import java.util.Collection;
import java.util.Map;

public class Tools {
	
	public static boolean featureSetEquals(Map<String, Boolean> f1, Map<String, Boolean> f2) {
		for(String key : f1.keySet()) {
			if(!equals(f1.get(key), f2.get(key)))
				return false;
		}
		for(String key : f2.keySet()) {
			if(!equals(f1.get(key), f2.get(key)))
				return false;
		}
		return true;
	}
	
	public static boolean equals(Boolean boolean1, Boolean boolean2) {
		return Tools.getBooleanValue(boolean1) == Tools.getBooleanValue(boolean2);
	}
	
	public static boolean getBooleanValue(Boolean value) {
		if(value == null)
			return false;
		return value;
	}
	
	public static boolean contains(Collection<Map<String, Boolean>> featureSets, Map<String, Boolean> target) {
		for(Map<String, Boolean> featureSet : featureSets) {
			if(featureSetEquals(featureSet, target))
				return true;
		}
		
		return false;
	}
}
