package uk.ac.aber.cs211.sudoku.solver;

import java.util.LinkedList;

import uk.ac.aber.cs211.sudoku.model.CellValue;


/**
 * A list suitable for storing candidates (possible values) that could go in a
 * cell in a sudoku board.
 * 
 * @author Josh Tumath (jmt14@aber.ac.uk)
 */
public class CandidateList {
	// /////////////////// //
	// Instance variables. //
	// /////////////////// //
	/** Stores a list of candidates (possible values) for the cell. */
	private LinkedList<CellValue> candidates = new LinkedList<CellValue>();
	
	// //////// //
	// Methods. //
	// //////// //
	/**
	 * Gets all the candidates in the list as an array
	 * @return an array of all the candidates in the list
	 */
	public CellValue[] getCandidates() {
		CellValue[] cells = new CellValue[candidates.size()];
		
		for (int i = 0; i < cells.length; i++) {
			cells[i] = candidates.get(i);
		}
		
		return cells;
	}
	
	/**
	 * Gets the first candidate in the list.
	 * @return the first candidate in the list
	 */
	public CellValue getFirstCandidate() {
		return candidates.getFirst();
	}
	
	/**
	 * Adds a candidate to the list.
	 * @param cell the number to be added to the list
	 */
	public void add(CellValue cell) {
		candidates.add(cell);
	}
	
	/**
	 * Removes a number from the list.
	 * @param cell the number to be removed from the list
	 */
	public void remove(CellValue cell) {
		candidates.remove(cell);
	}
	
	/**
	 * Gets the number of candidates in the list
	 * @return the number of candidates in the list
	 */
	public int size() {
		return candidates.size();
	}
	
	/**
	 * Check if a number is stored in the list
	 * @param cell the number to be checked
	 * @return true if the number is in the list; otherwise false
	 */
	public boolean isCandidateInList(CellValue cell) {
		return candidates.contains(cell);
	}
}
