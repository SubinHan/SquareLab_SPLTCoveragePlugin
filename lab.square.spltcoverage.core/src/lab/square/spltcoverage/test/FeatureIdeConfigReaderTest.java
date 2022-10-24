package lab.square.spltcoverage.test;

import static org.junit.Assert.assertTrue;

import java.util.Map;

import org.junit.Test;

import lab.square.spltcoverage.io.FeatureIdeConfigReader;
import lab.square.spltcoverage.model.FeatureSet;
import lab.square.spltcoverage.utils.Tools;

public class FeatureIdeConfigReaderTest {

	private static final String TARGET_PATH = "testResources/testInput/FeatureIdeConfigFile/16.xml";
	
	@Test
	public void testReadFeatureSet() {
		FeatureSet featureSet = FeatureIdeConfigReader.readFeatureSet(TARGET_PATH);
		
		assertTrue(isCorrect(featureSet));
	}
	
	private boolean isCorrect(FeatureSet featureSet) {
		if(!featureSet.hasFeature("Elevator"))
			return false;
		if(!featureSet.hasFeature("Behavior"))
			return false;
		if(!featureSet.hasFeature("Modes"))
			return false;
		if(featureSet.hasFeature("Sabbath"))
			return false;
		if(featureSet.hasFeature("FIFO"))
			return false;
		if(!featureSet.hasFeature("ShortestPaht"))
			return false;
		if(!featureSet.hasFeature("Service"))
			return false;
		if(featureSet.hasFeature("Priorities"))
			return false;
		if(featureSet.hasFeature("RushHour"))
			return false;
		if(featureSet.hasFeature("FloorPriority"))
			return false;
		if(featureSet.hasFeature("PersonPriority"))
			return false;
		if(featureSet.hasFeature("VoiceOutput"))
			return false;
		if(!featureSet.hasFeature("CallButtons"))
			return false;
		if(featureSet.hasFeature("UndirectedCall"))
			return false;
		if(!featureSet.hasFeature("DirectedCall"))
			return false;
		if(!featureSet.hasFeature("Security"))
			return false;
		if(!featureSet.hasFeature("Permission"))
			return false;
		if(!featureSet.hasFeature("FloorPermission"))
			return false;
		if(featureSet.hasFeature("PermissionControl"))
			return false;
		if(featureSet.hasFeature("Safety"))
			return false;
		if(featureSet.hasFeature("Overloaded"))
			return false;
		return true;
	}
}
