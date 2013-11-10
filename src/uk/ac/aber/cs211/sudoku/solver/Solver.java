package uk.ac.aber.cs211.sudoku.solver;

import java.util.Arrays;

import uk.ac.aber.cs211.sudoku.model.CellValue;
import uk.ac.aber.cs211.sudoku.model.Grid;


/**
 * Contains an algorithm that attempts to solve sudoku puzzles using the
 * techniques called "naked singles", "hidden singles" and "naked pairs".
 * 
 * @author Josh Tumath (jmt14@aber.ac.uk)
 */
public class Solver
implements Runnable {
	// /////////////////// //
	// Instance variables. //
	// /////////////////// //
	private Grid grid;
	
	/** Stores a list of possible values that could go in each empty cell */
	private CandidateList[][] candidateLists = new CandidateList[9][9];
	
	/** Shows whether it's worth re-checking the grid to solve more squares */
	private boolean morePossibleSolutions;
	
	/** Set to false when the thread is asked to be stopped */
	private boolean keepGoing = true;
	
	// ///////////// //
	// Constructors. //
	// ///////////// //
	/**
	 * Creates a new instance of the grid solving system.
	 * @param grid the grid to be solved
	 */
	public Solver(Grid grid) {
		this.grid = grid;
	}
	
	// //////// //
	// Methods. //
	// //////// //
	/**
	 * Looks for solutions to any squares in the grid and updates cells in the
	 * grid if any are found.
	 */
	@Override
	public void run() {
		do {
			morePossibleSolutions = false;
			
			listCandidates();
			
			checkForNakedPairs();
			checkForNakedSingles();
			checkForHiddenSingles();
		} while (morePossibleSolutions && keepGoing);
	}
	
	/**
	 * Goes through each cell in the grid to find all possible candidates.
	 */
	private void listCandidates() {
		// Loop through every cell
		for (int x = 0; x < 9; x++) {
			for (int y = 0; y < 9; y++) {
				// We only want to add candidate lists to empty cells
				if (grid.getCell(x, y).getValue() == CellValue.EMPTY) {
					candidateLists[x][y] = new CandidateList();
					
					// Test if each cell value could be used in this empty cell
					for (CellValue cellValue : CellValue.values()) {
						if (isRowAvailable(x, y, cellValue)
								&& isColAvailable(x, y, cellValue)
								&& isSubGridAvailable(x, y, cellValue)) {
							candidateLists[x][y].add(cellValue);
						}
					}
				}
			}
		}
	}
	
	/**
	 * Checks if a number in a cell is equal to another cell in a given row.
	 * @param thisX the x coordinate of the cell to be checked
	 * @param y the y coordinate of the cell to be checked
	 * @param value the number that you want to see if the row could have
	 * @return true if the row doesn't have the number in it; otherwise false
	 */
	private boolean isRowAvailable(final int thisX, final int y, final CellValue value) {
		for (int x = 0; x < 9; x++) {
			// Test if any cell in this row (excluding the given cell) equals
			// the given cell value.
			if (x != thisX && grid.getCell(x, y).getValue() == value) {
				return false;
			}
		}
		
		return true;
	}
	
	/**
	 * Checks if a number in a cell is equal to another cell in a given column.
	 * @param x the x coordinate of the cell to be checked
	 * @param thisY the y coordinate of the cell to be checked
	 * @param value the number that you want to see if the column could have
	 * @return true if the column doesn't have the number in it; otherwise false
	 */
	private boolean isColAvailable(final int x, final int thisY, final CellValue value) {
		for (int y = 0; y < 9; y++) {
			// Test if any cell in this column (excluding the given cell) equals
			// the given cell value.
			if (y != thisY && grid.getCell(x, y).getValue() == value) {
				return false;
			}
		}
		
		return true;
	}

	/**
	 * Checks if a number in a cell is equal to another cell within its
	 * sub-grid.
	 * @param thisX the x coordinate of the cell to be checked
	 * @param thisY the y coordinate of the cell to be checked
	 * @param value the number that you want to see if the sub-grid could have
	 * @return true if the sub-grid doesn't have the number in it; otherwise
	 *         false
	 */
	private boolean isSubGridAvailable(final int thisX, final int thisY, final CellValue value) {
		final int subgridCoordinateX = calculateSubGridCoordinate(thisX);
		final int subgridCoordinateY = calculateSubGridCoordinate(thisY);
		
		for (int x = subgridCoordinateX; x < subgridCoordinateX + 3; x++) {
			for (int y = subgridCoordinateY; y < subgridCoordinateY + 3; y++) {
				// Test if any cell in this sub-grid (excluding the given cell)
				// equals the given cell value.
				if (x != thisX && y != thisY && grid.getCell(x, y).getValue() == value) {
					return false;
				}
			}
		}
		
		return true;
	}
	
	/**
	 * Checks for when a cell has two candidates and sees if any other cells in
	 * the same row, column
	 * 
	 * <p><strong>Note:</strong> Only use this after using lookForCandidates().
	 */
	private void checkForNakedPairs() {
		for (int x = 0; x < 9; x++) {
			for (int y = 0; y < 9; y++) {
				// Test if this cell is empty and has only two candidates
				if (grid.getCell(x, y).getValue() == CellValue.EMPTY
						&& candidateLists[x][y].size() == 2) {
					CellValue[] pair = candidateLists[x][y].getCandidates();
					
					if (isEqualCandidateListInRow(x, y, pair)) {
						// Loop through this row to find the matching
						// candidate lists.
						for (int x2 = 0; x2 < 9; x2++) {
							// Is this the cell that matched?
							if (grid.getCell(x2, y).getValue() == CellValue.EMPTY
									&& x != x2
									&& Arrays.equals(candidateLists[x2][y].getCandidates(), pair)) {
								// Loop through the row again to remove
								// candidates that contain any of the values in
								// the naked pairs
								for (int x3 = 0; x3 < 9; x3++) {
									// Check that this cell is not one of the
									// naked pairs
									if (grid.getCell(x3, y).getValue() == CellValue.EMPTY
											&& x3 != x && x3 != x2) {
										if (candidateLists[x3][y].isCandidateInList(pair[0])) {
											candidateLists[x3][y].remove(pair[0]);
										}
										
										if (candidateLists[x3][y].isCandidateInList(pair[1])) {
											candidateLists[x3][y].remove(pair[1]);
										}
									}
								}
								
								// There can only be one other pair, so there's
								// no need to continue the loop.
								break;
							}
						}
					} else if (isEqualCandidateListInCol(x, y, pair)) {
						// Loop through this column to find the matching
						// candidate lists.
						for (int y2 = 0; y2 < 9; y2++) {
							// Is this the cell that matched?
							if (grid.getCell(x, y2).getValue() == CellValue.EMPTY
									&& y != y2
									&& Arrays.equals(candidateLists[x][y2].getCandidates(), pair)) {
								// Loop through the column again to remove
								// candidates that contain any of the values in
								// the naked pairs
								for (int y3 = 0; y3 < 9; y3++) {
									// Check that this cell is not one of the
									// naked pairs
									if (grid.getCell(x, y3).getValue() == CellValue.EMPTY
											&& y3 != y && y3 != y2) {
										if (candidateLists[x][y3].isCandidateInList(pair[0])) {
											candidateLists[x][y3].remove(pair[0]);
										}
										
										if (candidateLists[x][y3].isCandidateInList(pair[1])) {
											candidateLists[x][y3].remove(pair[1]);
										}
									}
								}
								
								// There can only be one other pair, so there's
								// no need to continue the loop.
								break;
							}
						}
					} else if (isEqualCandidateListInSubGrid(x, y, pair)) {
						final int subgridCoordinateX = calculateSubGridCoordinate(x);
						final int subgridCoordinateY = calculateSubGridCoordinate(y);
						
						// Loop through this sub-grid to find the matching
						// candidate lists.
						for (int x2 = subgridCoordinateX; x2 < subgridCoordinateX + 3; x2++) {
							for (int y2 = subgridCoordinateY; y2 < subgridCoordinateY + 3; y2++) {
								// Is this the cell that matched?
								if (grid.getCell(x2, y2).getValue() == CellValue.EMPTY
										&& x2 != x
										&& y2 != y
										&& Arrays.equals(candidateLists[x2][y2].getCandidates(), pair)) {
									// Loop through the sub-grid again to remove
									// candidates that contain any of the values
									// in the naked pairs
									for (int x3 = subgridCoordinateX; x3 < subgridCoordinateX + 3; x3++) {
										for (int y3 = subgridCoordinateY; y3 < subgridCoordinateY + 3; y3++) {
											// Check that this cell is not one
											// of the naked pairs
											if (grid.getCell(x3, y3).getValue() == CellValue.EMPTY
													&& x3 != x && x3 != x2
													&& y3 != x && y3 != y2) {
												if (candidateLists[x3][y3].isCandidateInList(pair[0])) {
													candidateLists[x3][y3].remove(pair[0]);
												}
												
												if (candidateLists[x3][y3].isCandidateInList(pair[1])) {
													candidateLists[x3][y3].remove(pair[1]);
												}
											}
										}
									}
									
									// There can only be one other pair, so
									// there's no need to continue the loop.
									break;
								}
							}
						}
					}
				}
			}
		}
	}
	
	/**
	 * Checks for any empty cells that only have one possible candidate.
	 * 
	 * <p><strong>Note:</strong> Only use this after using lookForCandidates().
	 */
	private void checkForNakedSingles() {
		for (int x = 0; x < 9; x++) {
			for (int y = 0; y < 9; y++) {
				// Check if the cell is empty and it has only one possible
				// candidate. If that's true, we can be certain of the cell's
				// number and can safely put that number in the grid.
				if (grid.getCell(x, y).getValue() == CellValue.EMPTY
						&& candidateLists[x][y].size() == 1) {
					addSolutionToGrid(x, y, candidateLists[x][y].getFirstCandidate());
				}
			}
		}
	}
	
	/**
	 * Check for cells that may contain more than one candidate but don't have
	 * other cells of the same value in their row, column or sub-grid.
	 * 
	 * <p><strong>Note:</strong> Only use this after using lookForCandidates().
	 */
	private void checkForHiddenSingles() {
		for (int x = 0; x < 9; x++) {
			for (int y = 0; y < 9; y++) {
				if (grid.getCell(x, y).getValue() == CellValue.EMPTY) {
					// Check if each candidate appears only once in either a
					// row, column or sub-grid. If that's true, we can be
					// certain of the cell's number and can safely put that
					// number in the grid.
					for (CellValue candidate : candidateLists[x][y].getCandidates()) {
						if (countNumberInRow(x, candidate) == 1
								|| countNumberInCol(y, candidate) == 1
								/* buggy: || countNumberInSubGrid(x, y, candidate) == 1 */) {
							addSolutionToGrid(x, y, candidate);
							
							// We don't need to loop through the rest of the
							// candidates
							break;
						}
					}
				}
			}
		}
	}
	
	/**
	 * Count the amount of times a number occurs in a given row either as a Cell
	 * value or as a candidate.
	 * @param x the x coordinate of the row to be checked
	 * @param value the number that you want to see if the row contains
	 * @return the amount of times a number occurs in a given row
	 */
	private int countNumberInRow(final int x, final CellValue value) {
		int count = 0;
		
		for (int y = 0; y < 9; y++) {
			// Either the cell is empty and has the given cell in one of its
			// candidates, or the cell has a value equal the given cell
			if ((grid.getCell(x, y).getValue() == CellValue.EMPTY
					&& candidateLists[x][y].isCandidateInList(value))
					|| grid.getCell(x, y).getValue() == value) {
				count++;
			}
		}
		
		return count;
	}

	/**
	 * Count the amount of times a number occurs in a given column either as a
	 * Cell value or as a candidate.
	 * @param y the y coordinate of the column to be checked
	 * @param value the number that you want to see if the column contains
	 * @return the amount of times a number occurs in a given column
	 */
	private int countNumberInCol(final int y, final CellValue value) {
		int count = 0;
		
		for (int x = 0; x < 9; x++) {
			// Either the cell is empty and has the given cell in one of its
			// candidates, or the cell has a value equal the given cell
			if ((grid.getCell(x, y).getValue() == CellValue.EMPTY
					&& candidateLists[x][y].isCandidateInList(value))
					|| grid.getCell(x, y).getValue() == value) {
				count++;
			}
		}
		
		return count;
	}

	/**
	 * Count the amount of times a number occurs in a given sub-grid either as a
	 * Cell value or as a candidate.
	 * @param x the x coordinate of a cell in the sub-grid to be checked
	 * @param y the y coordinate of a cell in the sub-grid to be checked
	 * @param value the number that you want to see if the sub-grid contains
	 * @return the amount of times a number occurs in a given sub-grid
	 */
	private int countNumberInSubGrid(final int x, final int y, final CellValue value) {
		final int subgridCoordinateX = calculateSubGridCoordinate(x);
		final int subgridCoordinateY = calculateSubGridCoordinate(y);
		
		int count = 0;
		
		// FIXME: Doesn't seem to be working properly
		for (int subgridX = subgridCoordinateX; subgridX < subgridCoordinateX + 3; subgridX++) {
			for (int subgridY = subgridCoordinateY; subgridY < subgridCoordinateY + 3; subgridY++) {
				// Either the cell is empty and has the given cell in one of its
				// candidates, or the cell has a value equal the given cell
				if ((grid.getCell(subgridX, subgridY).getValue() == CellValue.EMPTY
						&& candidateLists[subgridX][subgridY].isCandidateInList(value))
						|| grid.getCell(subgridX, subgridX).getValue() == value) {
					count++;
				}
			}
		}
		
		return count;
	}
	
	/**
	 * Checks if there are two or more candidate lists with equal values on the
	 * same row.
	 * @param thisX the x coordinate of the cell to be compared
	 * @param thisY the y coordinate of the row to be checked
	 * @param an array of the candidate list to be compared
	 * @return true if there is a candidate list in the row with the same value;
	 *         otherwise false
	 */
	private boolean isEqualCandidateListInRow(final int thisX, final int thisY, final CellValue[] list) {
		for (int x = 0; x < 9; x++) {
			if (grid.getCell(x, thisY).getValue() == CellValue.EMPTY
					&& x != thisX
					&& Arrays.equals(candidateLists[x][thisY].getCandidates(), list)) {
				return true;
			}
		}
		
		return false;
	}
	
	/**
	 * Checks if there are two or more candidate lists with equal values on the
	 * same column.
	 * @param thisX the x coordinate of the column to be checked
	 * @param thisY the y coordinate of the cell to be compared
	 * @param an array of the candidate list to be compared
	 * @return true if there is a candidate list in the column with the same
	 *         value; otherwise false
	 */
	private boolean isEqualCandidateListInCol(final int thisX, final int thisY, final CellValue[] list) {
		for (int y = 0; y < 9; y++) {
			if (grid.getCell(thisX, y).getValue() == CellValue.EMPTY
					&& y != thisY
					&& Arrays.equals(candidateLists[thisX][y].getCandidates(), list)) {
				return true;
			}
		}
		
		return false;
	}
	
	private boolean isEqualCandidateListInSubGrid(final int thisX, final int thisY, final CellValue[] list) {
		final int subgridCoordinateX = calculateSubGridCoordinate(thisX);
		final int subgridCoordinateY = calculateSubGridCoordinate(thisY);
		
		for (int x = subgridCoordinateX; x < subgridCoordinateX + 3; x++) {
			for (int y = subgridCoordinateY; y < subgridCoordinateY + 3; y++) {
				if (grid.getCell(x, y).getValue() == CellValue.EMPTY
						&& x != thisX
						&& y != thisY
						&& Arrays.equals(candidateLists[x][y].getCandidates(), list)) {
					return true;
				}
			}
		}
		
		return false;
	}
	
	/**
	 * Calculate the top left coordinate of a sub-grid, given an x or y
	 * coordinate of a cell in it.
	 * @param c an x or y coordinate
	 * @return the top left coordinate of a sub-grid (either 0, 3 or 6)
	 */
	private int calculateSubGridCoordinate(int c) {
		// If we divide a coordinate between 0 and 8 by 3, it will get floored
		// to either 0, 1 or 2. Multiplying this by 3 will give us either 0, 3
		// or 6. This can be used to determine the position of each box in the
		// grid.
		return c / 3 * 3;
	}
	
	/**
	 * Sets a new value for a cell in the grid and allows the loop to continue.
	 * @param x the x coordinate of the cell in the grid
	 * @param y the y coordinate of the cell in the grid
	 * @param value the new value of the cell in the grid
	 */
	private void addSolutionToGrid(final int x, final int y, final CellValue value) {
		grid.setCell(x, y, value);
		
		// This cell doesn't need to be checked for candidates any more, so we
		// can remove the CandidateList from it to save memory.
		candidateLists[x][y] = null;
		
		// Now that the grid has changed, it's worth checking it
		// again to see if there are any new candidates.
		morePossibleSolutions = true;
		
		// Show the user the solution process step-by-step.
		try {
			Thread.sleep(50);
		} catch (InterruptedException e) {
			keepGoing = false;
		}
	}
}
