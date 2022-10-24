package test.spltcoverage.antennaproduct;

public class ClassA {
	
	public boolean methodA() {
		//#if A
		return true;
		//#else
//@		return false;
		//#endif
	}
	
	public int methodB(int a, int b) {
		//#if B
		return a + b;
		//#else
//@		return a - b;
		//#endif
	}
}
