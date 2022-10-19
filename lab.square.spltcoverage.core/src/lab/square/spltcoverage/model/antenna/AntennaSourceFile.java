package lab.square.spltcoverage.model.antenna;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

import lab.square.spltcoverage.core.antenna.FeatureLocation;
import lab.square.spltcoverage.core.antenna.FeatureLocator;
import lab.square.spltcoverage.model.FeatureSet;

public class AntennaSourceFile {

	Logger logger = Logger.getLogger(AntennaSourceFile.class.getName());

	String fileName;
	AntennaLineType[] sourceLines;
	Collection<FeatureLocation> featureLocations;

	public AntennaSourceFile(String filePath) {
		initFileName(filePath);
		initSourceLines(filePath);
		initFeatureLocations(filePath);
	}

	private void initFeatureLocations(String filePath) {
		featureLocations = FeatureLocator.analyze(filePath);
	}

	private void initSourceLines(String filePath) {
		List<String> lines = getLines(filePath);

		int numberOfLines = lines.size();
		this.sourceLines = new AntennaLineType[numberOfLines];

		for (int i = 0; i < numberOfLines; i++) {
			this.sourceLines[i] = FeatureLocator.calculateLineType(lines.get(i));
		}
	}

	private void initFileName(String filePath) {
		this.fileName = findFileName(filePath);
	}

	private String findFileName(String filePath) {
		Path p = Paths.get(filePath);
		return p.getFileName().toString();
	}

	public String getFileName() {
		return this.fileName;
	}

	public AntennaSourceFile(AntennaSourceFile clone) {
		this.sourceLines = new AntennaLineType[clone.getNumberOfLine()];

		for (int i = 0; i < clone.getNumberOfLine(); i++) {
			this.sourceLines[i] = clone.sourceLines[i];
		}
	}

	private AntennaSourceFile(AntennaLineType[] sourceLines) {
		this.sourceLines = sourceLines.clone();
	}

	private List<String> getLines(String filePath) {
		List<String> lines = new ArrayList<>();

		try (BufferedReader reader = new BufferedReader(new FileReader(new File(filePath)))) {
			String line;
			while ((line = reader.readLine()) != null) {
				lines.add(line);
			}
		} catch (IOException e) {
			logError(e);
			return Collections.emptyList();
		}

		return lines;
	}

	public int getNumberOfLine() {
		return sourceLines.length;
	}

	public boolean isActivatedAt(int lineNumber) {
		try {
			return sourceLines[lineNumber - 1] == AntennaLineType.ACTIVATED;
		} catch (ArrayIndexOutOfBoundsException e) {
			return false;
		}
	}

	private void logError(Exception e) {
		logger.severe(e.getMessage());
	}

	public AntennaSourceFile add(AntennaSourceFile other) {
		checkMatchAndThrowIfNot(other);

		AntennaLineType[] added = this.sourceLines.clone();

		for (int i = 1; i <= getNumberOfLine(); i++) {
			if (other.isActivatedAt(i))
				added[i - 1] = AntennaLineType.ACTIVATED;
		}

		return new AntennaSourceFile(added);
	}

	public AntennaSourceFile subtract(AntennaSourceFile other) {
		checkMatchAndThrowIfNot(other);

		AntennaLineType[] subtracted = this.sourceLines.clone();

		for (int i = 1; i <= getNumberOfLine(); i++) {
			if (other.isActivatedAt(i))
				subtracted[i - 1] = AntennaLineType.DEACTIVATED;
		}

		return new AntennaSourceFile(subtracted);
	}

	public AntennaSourceFile intersect(AntennaSourceFile other) {
		checkMatchAndThrowIfNot(other);

		AntennaSourceFile whole = this.add(other);
		AntennaSourceFile leftSide = this.subtract(other);
		AntennaSourceFile rightSide = other.subtract(this);

		return whole.subtract(leftSide).subtract(rightSide);
	}

	private void checkMatchAndThrowIfNot(AntennaSourceFile other) {
		if (!hasSameStructure(other))
			throw new AntennaSourceModelException("Given sourcefiles are not match");
	}

	public class AntennaSourceModelException extends RuntimeException {
		public AntennaSourceModelException(String message) {
			super(message);
		}
	}
	
	private boolean hasSameNumberOfLine(AntennaSourceFile other) {
		return this.getNumberOfLine() == other.getNumberOfLine();
	}

	public boolean hasSameStructure(AntennaSourceFile other) {
		if(!hasSameNumberOfLine(other))
			return false;
	
		for(int i = 0; i < getNumberOfLine(); i++) {
			if(this.sourceLines[i] == other.sourceLines[i])
				continue;
			if(bothAreNotAntennaDirectives(this.sourceLines[i], other.sourceLines[i]))
				continue;
			return false;
		}
		
		return true;
	}

	private boolean bothAreNotAntennaDirectives(AntennaLineType a, AntennaLineType b) {
		return !(isAntennaDirectives(a) && isAntennaDirectives(b));
	}
	

	private boolean isAntennaDirectives(AntennaLineType antennaLineType) {
		return antennaLineType != AntennaLineType.ACTIVATED && antennaLineType != AntennaLineType.DEACTIVATED;
	}

	public boolean isLineFeatureLocationOf(int i, String feature) {
		FeatureSet featureSet = new FeatureSet();
		featureSet.addFeature(feature);
		return isLineFeatureLocationOf(i, featureSet);
	}

	public boolean isLineFeatureLocationOf(int i, Collection<String> features) {
		FeatureSet featureSet = new FeatureSet();
		for(String feature : features) {
			featureSet.addFeature(feature);
		}
		return isLineFeatureLocationOf(i, featureSet);
	}
	
	public boolean isLineFeatureLocationOf(int i, FeatureSet featureSet) {
		for (FeatureLocation each : featureLocations) {
			if(!each.containsLine(i))
				continue;
			
			if(!each.isFeatureLocationOf(featureSet))
				return false;
		}
		
		return true;
	}
}
