package lab.square.similaritymeasure.core;

/**
 * The vector that contains the value only true or false.
 * It is used to measure similarity of the products.
 * @author selab
 *
 */
public interface IVector {
	public int getDimension();
	public boolean getValue(int dimension);
	public boolean equals(IVector vector);
}
