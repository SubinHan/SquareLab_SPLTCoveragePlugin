package lab.square.similaritymeasure.core;

//import java.util.Collection;
import java.util.List;

/**
 * The interface for measuring similarities between given vectors. We provides
 * the concrete class named Jaccard using Jaccard Similarity to measure
 * similarity.
 * 
 * @author selab
 *
 */
public interface ISimilarityMeasurer {
	/**
	 * Returns similarity of the two vectors in range [0, 1).
	 * 
	 * @param v1
	 * @param v2
	 * @return
	 * @throws RuntimeException if the vectors' dimension are not the same.
	 */
	public double compare(IVector v1, IVector v2);

	/**
	 * Returns the most similar vector with the target index vector of the given
	 * vectors.
	 * 
	 * @param others
	 * @param target
	 * @return
	 * @throws RuntimeException if the vectors' dimension are not the same.
	 */
	public MostSimilarVector calculateMostSimilar(List<IVector> others, int target);
}
