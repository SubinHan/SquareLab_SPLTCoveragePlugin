package lab.square.spltplugin.ui.model;

import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.LinkedList;

import lab.square.spltcoverage.model.ProductCoverage;
import lab.square.spltcoverage.model.ProductCoverageManager;

public class SpltCoverageSession {
	
	private ProductCoverageManager manager;
	private String id;
	
	
	public SpltCoverageSession(ProductCoverageManager manager) {
		this.manager = manager;
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm:ss");
		
		id = simpleDateFormat.format(System.currentTimeMillis());
	}
	
	public String getId() {
		return id;
	}
	
	public ISpltCoverageItem[] getItems() {
		Collection<ISpltCoverageItem> items = new LinkedList<ISpltCoverageItem>();
		
		for(ProductCoverage pc : manager.getProductCoverages()) {
			items.add(new SpltCoverageProduct(pc));
		}
		
		return (ISpltCoverageItem[]) items.toArray(new ISpltCoverageItem[items.size()]);
	}
	
	public ProductCoverageManager getManager() {
		return this.manager;
	}
}
