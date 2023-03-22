package lab.square.similaritymeasure.core;

import java.util.Collection;

/**
 * It represents how was similar to the given vector, and the list of the most
 * similar vectors.
 * 
 * @author selab
 *
 */
public class MostSimilarVector {
	final public double similarity;
	final public Collection<IVector> vectors;

	public MostSimilarVector(double similarity, Collection<IVector> vectors) {
		this.similarity = similarity;
		this.vectors = vectors;
	}

}