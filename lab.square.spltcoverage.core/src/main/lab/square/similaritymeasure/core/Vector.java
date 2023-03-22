package lab.square.similaritymeasure.core;

/**
 * The abstract vector class contains a logic of equals().
 * @author selab
 *
 */
public abstract class Vector implements IVector {	
	public abstract int getDimension();
	public abstract boolean getValue(int dimension);
	
	public boolean equals(IVector vector) {
		for(int i = 0; i < getDimension(); i++) {
			if(getValue(i) != vector.getValue(i))
				return false;
		}
		return true;
	}
}
