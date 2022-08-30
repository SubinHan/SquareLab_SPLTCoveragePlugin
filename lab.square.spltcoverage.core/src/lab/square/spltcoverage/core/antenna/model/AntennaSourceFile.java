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
	
}
