package lab.square.spltcoverage.core.model;

public interface IProxy {
	String getVersion();
	String getSessionId();
	void setSessionId(String id);
	byte[] getExecutionData(boolean reset);
	void dump(boolean reset);
	void reset();
}
