package lab.square.spltcoverage.test.target;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class Configuration {
	private static Map<String, Boolean> configuration = new HashMap<>();
	public static boolean configA;
	public static boolean configB;
	
	public static final String CONFIG_A = "configa";
	public static final String CONFIG_B = "configb";
	
	public static boolean getConfiguration(String key) {
		if(!configuration.containsKey(key))
			return false;
		return configuration.get(key);
	}
	
	public static void setConfiguration(Map<String, Boolean> configuration) {
		Configuration.configuration = configuration;
		
		Configuration.configA = getConfiguration(CONFIG_A);
		Configuration.configB = getConfiguration(CONFIG_B);
	}
	
	public static class ValidConfigs {
		public final static ArrayList<Map<String, Boolean>> validConfigs;
		
		static {
			validConfigs = new ArrayList<>();
			Map<String, Boolean> config = new HashMap<>();
			config.put(CONFIG_A, false);
			config.put(CONFIG_B, false);
			validConfigs.add(new HashMap<>(config));
			config.put(CONFIG_A, false);
			config.put(CONFIG_B, true);
			validConfigs.add(new HashMap<>(config));
			config.put(CONFIG_A, true);
			config.put(CONFIG_B, false);
			validConfigs.add(new HashMap<>(config));
			config.put(CONFIG_A, true);
			config.put(CONFIG_B, true);
			validConfigs.add(new HashMap<>(config));
		}
	}

	public static Map<String, Boolean> getConfigurations() {
		return Configuration.configuration;
	}
}
