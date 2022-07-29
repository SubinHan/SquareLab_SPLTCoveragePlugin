package lab.square.spltcoverage.core.antenna;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Stack;
import java.util.logging.Logger;

public class FeatureLocator {

	static final Logger logger = Logger.getLogger(FeatureLocator.class.getName());

	public Collection<FeatureLocation> analyze(String javaSourceFilePath) {

		Collection<FeatureLocation> featureLocations = new ArrayList<>();
		Stack<LocationInfo> infoStack = new Stack<>();

		File javaSourceFile = new File(javaSourceFilePath);
		try (FileReader fileReader = new FileReader(javaSourceFile);
				LineNumberReader lineReader = new LineNumberReader(fileReader)) {
			String line;
			Stack<String> stackedExpressions = new Stack<>();
			while ((line = lineReader.readLine()) != null) {
				line = removeSpace(line);
				if (line.contains("//#if")) {
					int startLine = lineReader.getLineNumber();
					String featureExpression;
					featureExpression = line.substring(5);

					LocationInfo locationInfo = new LocationInfo(featureExpression, startLine);
					infoStack.add(locationInfo);
					stackedExpressions.push(featureExpression);
				} else if (line.contains("//#endif")) {
					int endLine = lineReader.getLineNumber();
					if (infoStack.isEmpty())
						logger.fine(() -> "" + endLine);
					LocationInfo popped = infoStack.pop();
					featureLocations.add(
							new FeatureLocation(javaSourceFile, stackedExpressions, popped.startLine + 1, endLine - 1));
					stackedExpressions.pop();
				} else if (line.contains("//#elif")) {

					int endLine = lineReader.getLineNumber();
					LocationInfo popped = infoStack.pop();
					featureLocations.add(
							new FeatureLocation(javaSourceFile, stackedExpressions, popped.startLine + 1, endLine - 1));
					stackedExpressions.pop();

					String featureExpression;
					featureExpression = line.substring(7);

					LocationInfo locationInfo = new LocationInfo(featureExpression, endLine);
					infoStack.add(locationInfo);
					stackedExpressions.push(featureExpression);
				} else if (line.contains("//#else")) {
					int endLine = lineReader.getLineNumber();
					LocationInfo popped = infoStack.pop();
					featureLocations.add(
							new FeatureLocation(javaSourceFile, stackedExpressions, popped.startLine + 1, endLine - 1));
					stackedExpressions.pop();

					String elseExpression = "!(" + popped.featureExpression + ")";
					LocationInfo locationInfo = new LocationInfo(elseExpression, endLine);
					infoStack.add(locationInfo);
					stackedExpressions.push(elseExpression);
				} else if (line.contains("//@")) {
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
			return Collections.emptyList();
		}

		return featureLocations;
	}

	private String removeSpace(String line) {
		line = line.replaceAll("\\s+", "");
		return line;
	}

	protected class LocationInfo {
		protected final int startLine;
		protected final String featureExpression;

		private LocationInfo(String featureExpression, int startLine) {
			this.featureExpression = featureExpression;
			this.startLine = startLine;
		}
	}

}
