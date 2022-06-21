package lab.square.spltcoverage.model;

public interface ISplCoverageVisitor {
	public void visit(SplCoverage pcm);
	public void visit(ProductCoverage pc);
	public void visit(TestCaseCoverage tcc);
	public void visit(TestMethodCoverage tmc);
}
