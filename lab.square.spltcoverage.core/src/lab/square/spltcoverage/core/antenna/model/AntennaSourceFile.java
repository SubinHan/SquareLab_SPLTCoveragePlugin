package lab.square.spltcoverage.core.antenna.model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

import lab.square.spltcoverage.core.antenna.FeatureLocator;

public class AntennaSourceFile {
	
	Logger logger = Logger.getLogger(AntennaSourceFile.class.getName());
	
	AntennaLineType[] sourceLines;

	public AntennaSourceFile(String filePath) {
		List<String> lines = getLines(filePath);
		
		int numberOfLines = lines.size();
		this.sourceLines = new AntennaLineType[numberOfLines];
		
		for(int i = 0; i < numberOfLines; i++) {
			this.sourceLines[i] = FeatureLocator.calculateLineType(lines.get(i));
		}		
	}
	
	public AntennaSourceFile(AntennaSourceFile clone) {
		this.sourceLines = new AntennaLineType[clone.getNumberOfLine()];
		
		for(int i = 0; i < clone.getNumberOfLine(); i++) {
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
			while((line = reader.readLine()) != null) {
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
		return sourceLines[lineNumber - 1] == AntennaLineType.ACTIVATED;
	}

	private void logError(Exception e) {
		logger.severe(e.getMessage());
	}
	
	public AntennaSourceFile add(AntennaSourceFile other) {
		checkMatchAndThrowIfNot(other);
		
		AntennaLineType[] added = this.sourceLines.clone();
		
		for(int i = 1; i <= getNumberOfLine(); i++) {
			if(other.isActivatedAt(i))
				added[i-1] = AntennaLineType.ACTIVATED;
		}		
		
		return new AntennaSourceFile(added);
	}

	public AntennaSourceFile subtract(AntennaSourceFile other) {
		checkMatchAndThrowIfNot(other);
		
		AntennaLineType[] subtracted = this.sourceLines.clone();
		
		for(int i = 1; i <= getNumberOfLine(); i++) {
			if(other.isActivatedAt(i))
				subtracted[i-1] = AntennaLineType.DEACTIVATED;
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
		if(other.getNumberOfLine() != this.getNumberOfLine())
			throw new AntennaSourceModelException("Given sourcefiles are not match");
	}
	
	public class AntennaSourceModelException extends RuntimeException {
		public AntennaSourceModelException(String message) {
			super(message);
		}
	}
}
