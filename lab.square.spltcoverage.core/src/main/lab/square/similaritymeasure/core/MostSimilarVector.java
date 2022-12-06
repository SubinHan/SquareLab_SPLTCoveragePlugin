package lab.square.similaritymeasure.core;

import java.util.Collection;

public class MostSimilarVector {
	final public double similarity;
	final public Collection<IVector> vectors;
	
	public MostSimilarVector(double similarity, Collection<IVector> vectors) {
		this.similarity = similarity;
		this.vectors = vectors;
	}
	
}