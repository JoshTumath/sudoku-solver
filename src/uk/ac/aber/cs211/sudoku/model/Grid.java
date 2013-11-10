package uk.ac.aber.cs211.sudoku.model;

import java.util.Observable;

/**
 * A model storing the entire grid of a sudoku board. The board's cells are
 * represented as a simple 9 x 9 2D array that can be accessed through getters
 * and setters.
 * 
 * <p>This model is only concerned with the current values of the grid. Any way
 * of storing the candidates (possible values) of the cells in the grid must be
 * implemented externally.
 * 
 * @author Josh Tumath (jmt14@aber.ac.uk)
 */
public class Grid extends Observable {
	// /////////////////// //
	// Instance variables. //
	// /////////////////// //
	/** An array of all the cells in the sudoku grid */
	private Cell[][] cells = new Cell[9][9];
	
	// ///////////// //
	// Constructors. //
	// ///////////// //
	/**
	 * Creates a grid with empty cells.
	 */
	public Grid() {
		for (int x = 0; x < 9; x++) {
			for (int y = 0; y < 9; y++) {
				cells[x][y] = new Cell(x, y);
			}
		}
	}
	
	/**
	 * Creates a grid containing a specified set of values.
	 * @param values a 2D array of CellValue that must be 9 x 9.
	 */
	public Grid(CellValue[][] values) {
		this();
		
		// Verify the array is the correct length
		if (cells.length == 9 && cells[0].length == 9) {
			for (int x = 0; x < 9; x++) {
				for (int y = 0; y < 9; y++) {
					cells[x][y].setValue(values[x][y]);
				}
			}
		}
	}
	
	// ////////////////////// //
	// Read/Write properties. //
	// ////////////////////// //
	/**
	 * Sets a new value for a cell in the grid.
	 * @param x the x coordinate of the cell in the grid
	 * @param y the y coordinate of the cell in the grid
	 * @param value the new value of the cell in the grid
	 */
	public void setCell(int x, int y, CellValue value) {
		cells[x][y].setValue(value);
		
		// Tell the observer (i.e. the GridCanvas) that a cell has changed
		setChanged();
		notifyObservers(cells[x][y]);
	}
	
	// ///////////////////// //
	// Read-only properties. //
	// ///////////////////// //
	/**
	 * Returns the value of a cell in the grid.
	 * @param x the x coordinate of the cell in the grid
	 * @param y the y coordinate of the cell in the grid
	 * @return the value of a cell in the grid
	 */
	public Cell getCell(int x, int y) {
		return cells[x][y];
	}
}