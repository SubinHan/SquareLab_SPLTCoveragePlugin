package lab.square.similaritymeasure.core;

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
