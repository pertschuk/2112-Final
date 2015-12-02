package simulator;

/**
 * Types of hexes
 * @author jack
 *
 */
public abstract class Hex {
	protected int r, c;
	
	/**
	 * Obvious method
	 * 
	 * @param r  the row
	 * @param c  the column
	 */
	public void setLocation(int c, int r) {
		this.r = r;
		this.c = c;
	}
	
	public int getRow() { return r; }
	public int getCol() { return c; }
	
	/**
	 * Return int array representation of coordinates
	 * @return an int array of form {row,col}
	 */
	public int[] getCoords() { 
		int[] coords = {c,r};
		return coords;
	}
	
	/**
	 * Implemented in subclasses
	 * @return
	 */
	public int sense(){
		throw new UnsupportedOperationException();
	}
	
	/**
	 * Equals method for Hex
	 * @param other  object to be compared
	 * @return  true if row and columns are equal, false otherwise
	 */
	public boolean equals(Object other) {
		if (other instanceof Hex) {
			Hex h = (Hex) other;
			return (h.getRow()==r && h.getCol()==c);
		}
		return false;
	}
}
