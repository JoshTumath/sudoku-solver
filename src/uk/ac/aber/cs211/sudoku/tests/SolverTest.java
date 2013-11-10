package uk.ac.aber.cs211.sudoku.tests;

import static org.junit.Assert.*;

import org.junit.Test;

import uk.ac.aber.cs211.sudoku.model.CellValue;
import uk.ac.aber.cs211.sudoku.model.Grid;
import uk.ac.aber.cs211.sudoku.solver.Solver;

public class SolverTest {
	@Test
	public void test() {
		Grid grid = new Grid(new CellValue[][] {
			{/* CellValue.TWO */ CellValue.EMPTY, CellValue.FIVE, CellValue.THREE, CellValue.EIGHT, CellValue.FOUR, CellValue.SIX, CellValue.ONE, CellValue.SEVEN, CellValue.NINE},
			{CellValue.SIX, CellValue.EIGHT, CellValue.ONE, CellValue.SEVEN, CellValue.NINE, CellValue.TWO, CellValue.FOUR, CellValue.THREE, CellValue.FIVE},
			{CellValue.SEVEN, CellValue.NINE, CellValue.FOUR, CellValue.FIVE, CellValue.ONE, CellValue.THREE, CellValue.SIX, CellValue.TWO, CellValue.EIGHT},
			{CellValue.NINE, CellValue.SEVEN, CellValue.EIGHT, CellValue.FOUR, CellValue.SIX, CellValue.ONE, CellValue.THREE, CellValue.FIVE, CellValue.TWO},
			{CellValue.THREE, CellValue.ONE, CellValue.TWO, CellValue.NINE, CellValue.SEVEN, CellValue.FIVE, CellValue.EIGHT, CellValue.FOUR, CellValue.SIX},
			{CellValue.FIVE, CellValue.FOUR, CellValue.SIX, CellValue.TWO, CellValue.THREE, CellValue.EIGHT, CellValue.NINE, CellValue.ONE, CellValue.SEVEN},
			{CellValue.ONE, CellValue.TWO, CellValue.SEVEN, CellValue.THREE, CellValue.EIGHT, CellValue.NINE, CellValue.FIVE, CellValue.SIX, CellValue.FOUR},
			{CellValue.EIGHT, CellValue.SIX, CellValue.FIVE, CellValue.ONE, CellValue.TWO, CellValue.FOUR, CellValue.SEVEN, CellValue.NINE, CellValue.THREE},
			{CellValue.FOUR, CellValue.THREE, CellValue.NINE, CellValue.SIX, CellValue.FIVE, CellValue.SEVEN, CellValue.TWO, CellValue.EIGHT, /* CellValue.ONE */ CellValue.EMPTY},
		});
		
		Thread solver = new Thread(new Solver(grid));
		solver.start();
		
		// Wait for thread to finish
		try {
			solver.join(10000);
			
			assertEquals("Cell at (0,0) was not solved.",
					CellValue.TWO, grid.getCell(0, 0).getValue());
			assertEquals("Cell at (8,8) was not solved.",
					CellValue.ONE, grid.getCell(8, 8).getValue());
		} catch (InterruptedException e) {
			fail("Solver thread did not finish.");
		}
	}

}
