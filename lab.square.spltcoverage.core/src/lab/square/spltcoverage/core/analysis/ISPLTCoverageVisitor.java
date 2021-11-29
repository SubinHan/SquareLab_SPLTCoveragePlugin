package lab.square.spltcoverage.core.analysis;

import lab.square.spltcoverage.core.model.ProductCoverage;
import lab.square.spltcoverage.core.model.ProductCoverageManager;
import lab.square.spltcoverage.core.model.TestCaseCoverage;
import lab.square.spltcoverage.core.model.TestMethodCoverage;

public interface ISpltCoverageVisitor {
	public void visit(ProductCoverageManager pcm);
	public void visit(ProductCoverage pc);
	public void visit(TestCaseCoverage tcc);
	public void visit(TestMethodCoverage tmc);
}
