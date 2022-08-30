package lab.square.spltcoverage.core.antenna;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Stack;
import java.util.logging.Logger;

import lab.square.spltcoverage.core.antenna.model.AntennaLineType;

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
				switch (calculateLineType(line)) {
				case IF: {
					int startLine = lineReader.getLineNumber();
					String featureExpression;
					featureExpression = line.substring(5);

					LocationInfo locationInfo = new LocationInfo(featureExpression, startLine);
					infoStack.add(locationInfo);
					stackedExpressions.push(featureExpression);
					break;
				}
				case ENDIF: {
					int endLine = lineReader.getLineNumber();
					if (infoStack.isEmpty())
						logger.fine(() -> "" + endLine);
					LocationInfo popped = infoStack.pop();
					featureLocations.add(
							new FeatureLocation(javaSourceFile, stackedExpressions, popped.startLine + 1, endLine - 1));
					stackedExpressions.pop();
					break;
				}
				case ELIF: {
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
					break;
				}
				case ELSE: {
					int endLine = lineReader.getLineNumber();
					LocationInfo popped = infoStack.pop();
					featureLocations.add(
							new FeatureLocation(javaSourceFile, stackedExpressions, popped.startLine + 1, endLine - 1));
					stackedExpressions.pop();

					String elseExpression = "!(" + popped.featureExpression + ")";
					LocationInfo locationInfo = new LocationInfo(elseExpression, endLine);
					infoStack.add(locationInfo);
					stackedExpressions.push(elseExpression);
					break;
				}
				default:
					break;
				}
			}
		} catch (IOException e) {
			logger.severe(e.getMessage());
			return Collections.emptyList();
		}

		return featureLocations;
	}

	public static AntennaLineType calculateLineType(String line) {
		if (line.contains("//#if"))
			return AntennaLineType.IF;
		if (line.contains("//#endif"))
			return AntennaLineType.ENDIF;
		if (line.contains("//#elif"))
			return AntennaLineType.ELIF;
		if (line.contains("//#else"))
			return AntennaLineType.ELSE;
		if (line.contains("//@"))
			return AntennaLineType.DEACTIVATED;
		return AntennaLineType.ACTIVATED;
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
