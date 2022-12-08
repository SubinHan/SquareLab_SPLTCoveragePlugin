package lab.square.spltclient.learningtest;

public class Classpath {
	
	public static void main(String[] args) {
		ClassLoader loader = ClassLoader.getSystemClassLoader();
		Class klass = null;
		
		try {
			klass = Class.forName("de.ovgu.featureide.examples.elevator.test.TestElevator");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		
		// It doesn't works. 
		
		assert klass != null;
		
	}

}
