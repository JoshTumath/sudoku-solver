package uk.ac.aber.cs211.sudoku.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JPanel;

import uk.ac.aber.cs211.sudoku.model.Cell;
import uk.ac.aber.cs211.sudoku.model.Grid;


/**
 * A JPanel representing a sudoku grid.
 * 
 * @author Josh Tumath (jmt14@aber.ac.uk)
 */
public class GridCanvas extends JPanel
implements Observer {
	// ////////// //
	// Constants. //
	// ////////// //
	private static final int CELL_MARGIN = 2;
	private static final int SUBCELL_MARGIN = 3;
	
	// /////////////////// //
	// Instance variables. //
	// /////////////////// //
	private Grid grid;
	
	/** A 2D array representing a grid of cells  */
	private CellCanvas[][] cellCanvases = new CellCanvas[9][9];
	
	// ///////////// //
	// Constructors. //
	// ///////////// //
	/**
	 * Set up the grid canvas with a suitable layout for rendering the sudoku
	 * grid.
	 */
	public GridCanvas() {
		setLayout(new GridLayout(3, 3, SUBCELL_MARGIN, SUBCELL_MARGIN));
		setBackground(Color.BLACK);
		this.setPreferredSize(new Dimension(400, 400));
		
		// Draw a grid of 3 x 3 JPanels with 3 x 3 JLabels in them
		for (int x = 0; x < 3; x++) {
			for (int y = 0; y < 3; y++) {
				add(generateSubGrid(x, y));
			}
		}
	}
	
	// ////////////////////// //
	// Read/Write properties. //
	// ////////////////////// //
	/**
	 * Set the grid to be rendered in the canvas.
	 * @param grid the grid to be rendered in the canvas
	 */
	public void setGrid(Grid grid) {
		this.grid = grid;
		
		for (int x = 0; x < 9; x++) {
			for (int y = 0; y < 9; y++) {
				cellCanvases[x][y].setGrid(grid, x, y);
			}
		}
		
		repaintGrid();
	}
	
	// ///////////////////// //
	// Read-only properties. //
	// ///////////////////// //
	/**
	 * Get the sudoku grid being rendered in the grid canvas.
	 * @return
	 */
	public Grid getGrid() {
		return grid;
	}
	
	// //////// //
	// Methods. //
	// //////// //
	/**
	 * Does the initial set up for the layout of all the items within the
	 * sub-grid of the canvas.
	 * @param x the x super-coordinate of the sub-grid
	 * @param y the y super-coordinate of the sub-grid
	 * @return a JPanel containing a sub-grid
	 */
	private JPanel generateSubGrid(int x, int y) {
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(3, 3, CELL_MARGIN, CELL_MARGIN));
		panel.setBackground(Color.LIGHT_GRAY);
		
		for (int innerX = 0; innerX < 3; innerX++) {
			for (int innerY = 0; innerY < 3; innerY++) {
				int currentX = x * 3 + innerX;
				int currentY = y * 3 + innerY;
				
				cellCanvases[currentX][currentY] = new CellCanvas();
				panel.add(cellCanvases[currentX][currentY]);
			}
		}
		
		return panel;
	}
	
	/**
	 * Repaint a cell in the grid if it has been updated.
	 */
	@Override
	public void update(Observable o, Object arg) {
		// Check that we are updating the same Grid object
		if (o == grid) {
			if (arg instanceof Cell) {
				Cell cell = (Cell) arg;
				
				repaintCell(cell.getX(), cell.getY());
			} else {
				repaintGrid();
			}
		}
	}
	
	/**
	 * Repaint all the cell canvases in the grid.
	 */
	public void repaintGrid() {
		for (int x = 0; x < 9; x++) {
			for (int y = 0; y < 9; y++) {
				cellCanvases[x][y].setColor(Color.BLACK);
				cellCanvases[x][y].repaint();
			}
		}
	}
	
	/**
	 * Repaint a single cell canvas in the grid.
	 * @param x the x coordinate of the cell
	 * @param y the y coordinate of the cell
	 */
	public void repaintCell(int x, int y) {
		cellCanvases[x][y].setColor(Color.GRAY);
		cellCanvases[x][y].repaint();
	}

	private static final long serialVersionUID = -2697667487944451854L;
}
