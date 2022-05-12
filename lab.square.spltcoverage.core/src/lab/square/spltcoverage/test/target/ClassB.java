package lab.square.spltcoverage.test.target;

public class ClassB {
	public int methodA(int a, int b) {
		if(Configuration.configA && Configuration.configB)
			return a * b;
		if(b != 0)
			return a / b;
		return 0;
	}
	
	public int methodB(int a, int b) {
		ClassA ca = new ClassA();
		if(ca.methodA())
			return ca.methodB(a, b + 1);
		return ca.methodB(a + 1, b);
	}
}
