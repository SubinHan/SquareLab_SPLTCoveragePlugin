package lab.square.spltcoverage.test.antennatarget;

import lab.square.spltcoverage.test.target.Configuration;

public class ClassA {
	
	public boolean methodA() {
		int a = 0;
		//#if A
		return true;
		//#else
		//#if C
//@		return true;
		//#endif
//@		return false;
		//#endif
	}
	
	public int methodB(int a, int b) {
		int b = 0;
		//#if B
		return a + b;
		//#else
//@		return a - b;
		//#endif
	}
}
