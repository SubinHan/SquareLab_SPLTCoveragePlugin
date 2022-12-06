package lab.square.spltcoverage.test.antennatarget2;

public class ClassB {
	public int methodA(int a, int b) {
		//#ifdef A & B
//@			return a * b;
		//#else
		if(b != 0)
			return a / b;
		return 0;
		//#endif
	}
	
	public int methodB(int a, int b) {
		ClassA ca = new ClassA();
		if(ca.methodA())
			return ca.methodB(a, b + 1);
		return ca.methodB(a + 1, b);
	}
}
