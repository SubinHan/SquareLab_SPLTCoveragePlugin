package lab.square.spltcoverage.core.analysis;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jacoco.core.analysis.Analyzer;
import org.jacoco.core.analysis.CoverageBuilder;
import org.jacoco.core.tools.ExecFileLoader;
/**
 * The CoverageMerge class merges multiple *.exec files to one *.exec file.
 * @author SQUARELAB
 *
 */
public class CoverageMerger {

	public static void main(String[] args) throws IOException {

		String directory = "D:/directorypath/bankaccount/Product1/AccountTest";

		ExecFileLoader execFileLoader = new ExecFileLoader();

		File folder = new File(directory);
		File[] files = folder.listFiles();

		for (File file : files) {
			execFileLoader.load(file);
		}
		
		execFileLoader.save(new File(directory + "/aa"), false);
	}

	public void createCoverageList() throws IOException {

	}
	
	/**
	 * Merge multiple *.exec files to one output file.
	 * @param output		The File to output.
	 * @param execFiles		The Files to merge.
	 * @throws IOException
	 */
	public void mergeExecs(File output, File... execFiles) throws IOException {
		ExecFileLoader execFileLoader = new ExecFileLoader();
		for(File file : execFiles) {
			execFileLoader.load(file);
		}
		if(!output.exists())
			output.createNewFile();
		execFileLoader.save(output, false);
	}

}
