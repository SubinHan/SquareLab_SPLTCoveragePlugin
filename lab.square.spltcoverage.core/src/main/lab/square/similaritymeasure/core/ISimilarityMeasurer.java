package lab.square.similaritymeasure.core;

//import java.util.Collection;
import java.util.List;

public interface ISimilarityMeasurer {
	public double compare(IVector v1, IVector v2) throws Exception;
	public MostSimilarVector calculateMostSimilar(List<IVector> others, int target) throws Exception;
}
