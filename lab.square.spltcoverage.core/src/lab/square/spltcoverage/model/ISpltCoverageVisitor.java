package lab.square.spltcoverage.model;

public interface ISpltCoverageVisitor {
	public void visit(ProductCoverageManager pcm);
	public void visit(ProductCoverage pc);
	public void visit(TestCaseCoverage tcc);
	public void visit(TestMethodCoverage tmc);
}
