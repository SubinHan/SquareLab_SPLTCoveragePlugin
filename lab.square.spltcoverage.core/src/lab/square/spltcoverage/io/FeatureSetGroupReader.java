package lab.square.spltcoverage.io;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

/**
 * 
 * @author selab
 *
 */
public class FeatureSetGroupReader implements IFeatureSetReader {

	private String directory;

	public FeatureSetGroupReader(String directory) {
		this.directory = directory;
	}

	@Override
	public Map<String, Boolean> read() {
		throw new UnsupportedOperationException();
	}

	@Override
	public Collection<Map<String, Boolean>> readAll() {
		File groupFolder = new File(directory);
		Collection<Map<String, Boolean>> products = new LinkedList<Map<String, Boolean>>();

		for (File productFolder : groupFolder.listFiles()) {
			Map<String, Boolean> featureSet = new HashMap<String, Boolean>();
			File featureSetFile = new File(productFolder.listFiles()[0].getAbsolutePath());
			BufferedReader reader = null;
			try {
				reader = new BufferedReader(new FileReader(featureSetFile));
				String line = null;
				while((line = reader.readLine()) != null){
					featureSet.put(line, true);
				}
				reader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			products.add(featureSet);
		}

		return products;
	}

}
