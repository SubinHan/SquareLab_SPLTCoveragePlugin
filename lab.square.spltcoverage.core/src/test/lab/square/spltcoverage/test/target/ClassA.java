package lab.square.spltcoverage.test.target;

public class ClassA {
	public boolean methodA() {
		if(Configuration.configA)
			return true;
		else
			return false;
	}
	
	public int methodB(int a, int b) {
		if(Configuration.configB)
			return a + b;
		else
			return a - b;
	}
}
