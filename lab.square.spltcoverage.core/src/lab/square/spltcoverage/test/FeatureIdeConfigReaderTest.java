package lab.square.spltcoverage.test;

import static org.junit.Assert.assertTrue;

import java.util.Map;

import org.junit.Test;

import lab.square.spltcoverage.io.FeatureIdeConfigReader;
import lab.square.spltcoverage.utils.Tools;

public class FeatureIdeConfigReaderTest {

	private static final String TARGET_PATH = "testResources/FeatureIdeConfigFile/16.xml";
	
	@Test
	public void testReadFeatureSet() {
		Map<String, Boolean> featureSet = FeatureIdeConfigReader.readFeatureSet(TARGET_PATH);
		
		assertTrue(isCorrect(featureSet));
	}
	
	private boolean isCorrect(Map<String, Boolean> featureSet) {
		if(!hasFeature(featureSet, "Elevator"))
			return false;
		if(!hasFeature(featureSet, "Behavior"))
			return false;
		if(!hasFeature(featureSet, "Modes"))
			return false;
		if(hasFeature(featureSet, "Sabbath"))
			return false;
		if(hasFeature(featureSet, "FIFO"))
			return false;
		if(!hasFeature(featureSet, "ShortestPaht"))
			return false;
		if(!hasFeature(featureSet, "Service"))
			return false;
		if(hasFeature(featureSet, "Priorities"))
			return false;
		if(hasFeature(featureSet, "RushHour"))
			return false;
		if(hasFeature(featureSet, "FloorPriority"))
			return false;
		if(hasFeature(featureSet, "PersonPriority"))
			return false;
		if(hasFeature(featureSet, "VoiceOutput"))
			return false;
		if(!hasFeature(featureSet, "CallButtons"))
			return false;
		if(hasFeature(featureSet, "UndirectedCall"))
			return false;
		if(!hasFeature(featureSet, "DirectedCall"))
			return false;
		if(!hasFeature(featureSet, "Security"))
			return false;
		if(!hasFeature(featureSet, "Permission"))
			return false;
		if(!hasFeature(featureSet, "FloorPermission"))
			return false;
		if(hasFeature(featureSet, "PermissionControl"))
			return false;
		if(hasFeature(featureSet, "Safety"))
			return false;
		if(hasFeature(featureSet, "Overloaded"))
			return false;
		return true;
	}

	private boolean hasFeature(Map<String, Boolean> featureSet, String feature) {
		return Tools.getBooleanValue(featureSet.get(feature));
	}
}
