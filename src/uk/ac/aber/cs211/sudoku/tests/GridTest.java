package uk.ac.aber.cs211.sudoku.tests;

import static org.junit.Assert.*;

import org.junit.Test;

import uk.ac.aber.cs211.sudoku.model.CellValue;
import uk.ac.aber.cs211.sudoku.model.Grid;

public class GridTest {
	@Test
	public void testNewGrid() {
		Grid grid = new Grid();
		
		for (int x = 0; x < 9; x++) {
			for (int y = 0; y < 9; y++) {
				assertEquals("Cell at coordinates (" + x + "," + y + ") is not empty",
						CellValue.EMPTY, grid.getCell(x, y).getValue());
			}
		}
	}
	
	@Test
	public void testSetCell() {
		Grid grid = new Grid();
		
		grid.setCell(0, 0, CellValue.ONE);
		assertEquals("Cell at coordinates (0,0) does not have set value",
				CellValue.ONE, grid.getCell(0, 0).getValue());
		
		grid.setCell(8, 0, CellValue.ONE);
		assertEquals("Cell at coordinates (8,0) does not have set value",
				CellValue.ONE, grid.getCell(8, 0).getValue());
		
		try {
			grid.setCell(9, 0, CellValue.ONE);
			fail("ArrayIndexOutOfBoundsException not caught");
		} catch (ArrayIndexOutOfBoundsException e) {
			// Expected behaviour
		}
	}
}
