package lab.square.spltcoverage.model;

public interface IProxy {
	String getVersion();
	String getSessionId();
	void setSessionId(String id);
	byte[] getExecutionData(boolean reset);
	void dump(boolean reset);
	void reset();
}
