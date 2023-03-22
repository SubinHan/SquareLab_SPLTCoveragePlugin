package lab.square.similaritymeasure.core;

import java.util.Collection;
import java.util.LinkedList;
//import java.util.Collection;
import java.util.List;

/**
 * The similarity measurer using Jaccard Similarity.
 * @author selab
 *
 */
public class Jaccard implements ISimilarityMeasurer {
	
	
	public Jaccard() {
		
	}
	
	@Override
	public double compare(IVector v1, IVector v2) throws RuntimeException {
		if(v1.getDimension() != v2.getDimension()) {
			throw new RuntimeException("The dimensions are not the same");
		}
		
		int dimension = v1.getDimension();
		
		int common = 0;
		
		for(int i = 0; i < dimension; i++) {
			if(v1.getValue(i) == v2.getValue(i) && v1.getValue(i)) {
				common++;
			}
		}
		
		int numV1 = 0, numV2 = 0;
		
		for(int i = 0; i < dimension; i++) {
			if(v1.getValue(i))
				numV1++;
			if(v2.getValue(i))
				numV2++;
		}
		
		double result = (double)common / (double)(numV1 + numV2 - common);
		
		return result;
	}

	
	@Override
	public MostSimilarVector calculateMostSimilar(List<IVector> others, int target) throws RuntimeException {
		double max = 0; // 최종 유사도, 최종 인덱스 
		Collection<IVector> vectors = new LinkedList<IVector>();
		
		for(int i=0; i<others.size(); i++) {
			if(i == target) continue;
			IVector it = others.get(i);
			if(others.get(target).getDimension() != it.getDimension()) {
				throw new RuntimeException("The dimensions are not the same");
			} 
			double res = compare(others.get(target), it);
			if(max < res) {
				max = res;
				vectors.clear();
				vectors.add(others.get(i));
			} else if(max == res) {
				vectors.add(others.get(i));
			}
			
		}
		return new MostSimilarVector(max, vectors);
	}
	
	
}
