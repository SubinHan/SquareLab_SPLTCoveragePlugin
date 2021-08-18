package lab.square.spltcoverage.core.model;

import java.util.Collection;
import java.util.HashSet;

public class FeatureGraph {
	private FeatureGraph parent;
	private Collection<FeatureGraph> mandatory;
	private Collection<FeatureGraph> optional;
	private Collection<FeatureGraph> alter;
	private Collection<FeatureGraph> or;
	
	public FeatureGraph() {
		mandatory = new HashSet<FeatureGraph>();
		optional = new HashSet<FeatureGraph>();
		alter = new HashSet<FeatureGraph>();
		or = new HashSet<FeatureGraph>();
	}
	
	public FeatureGraph(FeatureGraph parent) {
		this();
		this.parent = parent;
	}
	
	public FeatureGraph getParent() {
		return parent;
	}
	
	public void setParent(FeatureGraph parent) {
		this.parent = parent;
	}
	
	public Collection<FeatureGraph> getMandatoryFeatures() {
		return mandatory;
	}
	
	public void addMandatoryFeatures(FeatureGraph mandatory) {
		this.mandatory.add(mandatory);
	}
	
	public Collection<FeatureGraph> getOptionalFeatures() {
		return optional;
	}
	
	public void setOptionalFeatures(FeatureGraph optional) {
		this.optional.add(optional);
	}
	
	public Collection<FeatureGraph> getAlterFeatures() {
		return alter;
	}
	
	public void setAlterFeatures(FeatureGraph alter) {
		this.alter.add(alter);
	}
	
	public Collection<FeatureGraph> getOrFeatures() {
		return or;
	}
	
	public void setOrFeatures(FeatureGraph or) {
		this.or.add(or);
	}
	
	
}
