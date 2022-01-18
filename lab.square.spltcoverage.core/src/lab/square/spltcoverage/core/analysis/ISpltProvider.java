package lab.square.spltcoverage.core.analysis;

import lab.square.spltcoverage.model.ProductSourceInfo;

public interface ISpltProvider {
	public int getNumProducts();
	public ProductSourceInfo getProductInfo(int i);
}
