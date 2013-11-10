package uk.ac.aber.cs211.sudoku.ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

import javax.swing.JPanel;

import uk.ac.aber.cs211.sudoku.model.CellValue;
import uk.ac.aber.cs211.sudoku.model.Grid;


/**
 * A JPanel that represents a single cell in a sudoku grid.
 * 
 * @author Josh Tumath (jmt14@aber.ac.uk)
 */
public class CellCanvas extends JPanel {
	// ////////// //
	// Constants. //
	// ////////// //
	private static final String TEXT_FONT = "Serif";
	private static final int TEXT_SIZE = 24;
	private static final int TEXT_POS_X = 16;
	private static final int TEXT_POS_Y = 28;
	
	// /////////////////// //
	// Instance variables. //
	// /////////////////// //
	private Grid grid;
	
	/** The x coordinate of this cell. */
	private int x;

	/** The y coordinate of this cell. */
	private int y;
	
	/** The color of the text in this cell. */
	private Color textColor = Color.BLACK;
	
	// ///////////// //
	// Constructors. //
	// ///////////// //
	/**
	 * Creates the cell with no corresponding cell from the currently grid set.
	 */
	public CellCanvas() {
		setBackground(Color.WHITE);
	}
	
	// ////////////////////// //
	// Read/Write properties. //
	// ////////////////////// //
	/**
	 * Set the grid and the position of this cell in the grid to be rendered.
	 * @param grid the grid to be rendered
	 * @param x the x coordinate of the cell in the grid to be rendered
	 * @param y the y coordinate of the cell in the grid to be rendered
	 */
	public void setGrid(Grid grid, int x, int y) {
		this.grid = grid;
		this.x = x;
		this.y = y;
		
		repaint();
	}
	
	/**
	 * Set the color of the text in the grid
	 * @param textColor the text color
	 */
	public void setColor(Color textColor) {
		this.textColor = textColor;
	}
	
	// //////// //
	// Methods. //
	// //////// //
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		// If a grid has not yet been set in the cell, we don't want to try
		// drawing the cell at the moment.
		if (grid != null) {
			g.setColor(textColor);
			g.setFont(new Font(TEXT_FONT, Font.BOLD, TEXT_SIZE));
			g.drawString(cellToString(grid.getCell(x, y).getValue()), TEXT_POS_X, TEXT_POS_Y);
		}
	}
	
	/**
	 * Converts a Cell enum to a string for rendering.
	 * @param cell the Cell value
	 * @return the Cell value as a string
	 */
	private String cellToString(CellValue cell) {
		switch(cell) {
		case ONE:
			return "1";
		case TWO:
			return "2";
		case THREE:
			return "3";
		case FOUR:
			return "4";
		case FIVE:
			return "5";
		case SIX:
			return "6";
		case SEVEN:
			return "7";
		case EIGHT:
			return "8";
		case NINE:
			return "9";
		default:
			return " ";
		}
	}
	
	private static final long serialVersionUID = 4357421883593741211L;
}