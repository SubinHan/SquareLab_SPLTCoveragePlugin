package lab.square.spltcoverage.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import lab.square.spltcoverage.core.analysis.SplCoverageGenerator;

public final class Tools {
	
	private Tools() {
	}

	public static boolean featureSetEquals(Map<String, Boolean> f1, Map<String, Boolean> f2) {
		for (Entry<String, Boolean> entry : f1.entrySet()) {
			if (!equals(entry.getValue(), f2.get(entry.getKey())))
				return false;
		}
		for (Entry<String, Boolean> entry : f2.entrySet()) {
			if (!equals(f1.get(entry.getKey()), entry.getValue()))
				return false;
		}
		return true;
	}

	public static boolean equals(Boolean boolean1, Boolean boolean2) {
		return Tools.getBooleanValue(boolean1) == Tools.getBooleanValue(boolean2);
	}

	public static boolean getBooleanValue(Boolean value) {
		if (value == null)
			return false;
		return value;
	}

	public static boolean contains(Collection<Map<String, Boolean>> featureSets, Map<String, Boolean> target) {
		for (Map<String, Boolean> featureSet : featureSets) {
			if (featureSetEquals(featureSet, target))
				return true;
		}

		return false;
	}

	public static List<String> getSourceByLine(File file) {
		List<String> result = new ArrayList<>();

		try (BufferedReader br = new BufferedReader(new FileReader(file))) {
			String line = "";
			while ((line = br.readLine()) != null) {
				result.add(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return result;
	}

	public static String convertClassNameByConvention(String className) {
		String converted = className.replace('/', '.');
		if (className.contains("$"))
			converted = converted.substring(0, className.lastIndexOf('$'));

		return converted;
	}

	public static void deleteDirectoryRecursively(File f) throws FileNotFoundException {
		if (f.isDirectory()) {
			for (File c : f.listFiles())
				deleteDirectoryRecursively(c);
		}
		
		if (!f.delete())
			throw new FileNotFoundException("Failed to delete file: " + f);
	}
	
	public static boolean isMergedCoverage(String fileNameWithExtension) {
		return fileNameWithExtension.endsWith("Merged.exec") || fileNameWithExtension.endsWith(SplCoverageGenerator.SUFFIX_MERGED);
	}
}
