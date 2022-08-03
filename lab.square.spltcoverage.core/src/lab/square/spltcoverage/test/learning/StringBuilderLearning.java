package lab.square.spltcoverage.test.learning;

import java.util.Arrays;
import java.util.Collection;

import org.junit.Test;

public class StringBuilderLearning {
	
	private static String convertToSingleLine(Collection<String> paths) {
		StringBuilder builder = new StringBuilder();
		
		for (String path : paths) {
			builder.append(path);
			builder.append(";");
		}

		return builder.substring(0, builder.length()-1);
	}
	
	@Test
	public void learnSubstring() {
		System.out.println(convertToSingleLine(Arrays.asList(new String[] { "a", "b", "c" })));
	}
}
