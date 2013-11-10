package uk.ac.aber.cs211.sudoku.model;

/**
 * A part of the model representing a single cell in the grid.
 * 
 * @author Josh Tumath (jmt14@aber.ac.uk)
 */
public class Cell {
	// /////////////////// //
	// Instance variables. //
	// /////////////////// //
	/** The x coordinate of the cell */
	private final int x;

	/** The y coordinate of the cell */
	private final int y;
	
	/** The contents of the cell */
	private CellValue value;
	
	// ///////////// //
	// Constructors. //
	// ///////////// //
	/**
	 * Creates a new cell with an empty value.
	 * @param x the x coordinate of the cell
	 * @param y the y coordinate of the cell
	 */
	public Cell(int x, int y) {
		this(x, y, CellValue.EMPTY);
	}
	
	/**
	 * Creates a new cell with all values set initially.
	 * @param x the x coordinate of the cell
	 * @param y the y coordinate of the cell
	 * @param value the contents of the cell
	 */
	public Cell(int x, int y, CellValue value) {
		this.x = x;
		this.y = y;
		this.value = value;
	}
	
	// ////////////////////// //
	// Read/Write properties. //
	// ////////////////////// //
	/**
	 * Sets a new value for the cell.
	 * @param value the new value
	 */
	public void setValue(CellValue value) {
		this.value = value;
	}
	
	// ///////////////////// //
	// Read-only properties. //
	// ///////////////////// //
	/**
	 * Gets the x coordinate of the cell
	 * @return the x coordinate of the cell
	 */
	public int getX() {
		return x;
	}
	
	/**
	 * Gets the y coordinate of the cell
	 * @return the y coordinate of the cell
	 */
	public int getY() {
		return y;
	}
	
	/**
	 * Gets the value of the cell
	 * @return the value of the cell
	 */
	public CellValue getValue() {
		return value;
	}
}
