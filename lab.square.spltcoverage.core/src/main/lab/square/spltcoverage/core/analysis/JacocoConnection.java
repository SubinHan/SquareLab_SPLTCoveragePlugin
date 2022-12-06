package lab.square.spltcoverage.core.analysis;

import java.io.IOException;

import javax.management.MBeanServerConnection;
import javax.management.MBeanServerInvocationHandler;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;

import org.jacoco.core.instr.Instrumenter;
import org.jacoco.core.runtime.IRuntime;
import org.jacoco.core.runtime.LoggerRuntime;
import org.jacoco.core.runtime.RuntimeData;

import lab.square.spltcoverage.model.IProxy;

public final class JacocoConnection {
	private static final String SERVICE_URL = "service:jmx:rmi:///jndi/rmi://localhost:7777/jmxrmi";

	private static JacocoConnection instance;
	private final IProxy proxy;

	final IRuntime runtime;
	final Instrumenter instr;
	final RuntimeData data;

	private JacocoConnection() throws IOException, MalformedObjectNameException {
		runtime = new LoggerRuntime();
		instr = new Instrumenter(runtime);
		data = new RuntimeData();
		
		// Open connection to the coverage agent:
		final JMXServiceURL url = new JMXServiceURL(SERVICE_URL);
		final JMXConnector jmxc = JMXConnectorFactory.connect(url, null);
		final MBeanServerConnection connection = jmxc.getMBeanServerConnection();

		proxy = MBeanServerInvocationHandler.newProxyInstance(connection, new ObjectName("org.jacoco:type=Runtime"),
				IProxy.class, false);

	}

	public static JacocoConnection getInstance() {
		if (instance == null)
			try {
				instance = new JacocoConnection();
			} catch (MalformedObjectNameException | IOException e) {
				e.printStackTrace();
			}

		return instance;
	}
	
	public String getVersion() {
		return proxy.getVersion();
	}
	
	public String getSessionId() {
		return proxy.getSessionId();
	}
	
	public byte[] getExecutionData(boolean reset) {
		return proxy.getExecutionData(reset);
	}
	
	public void resetData() {
		proxy.reset();
	}
}
