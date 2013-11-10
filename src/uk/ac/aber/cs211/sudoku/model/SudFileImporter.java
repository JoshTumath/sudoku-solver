package uk.ac.aber.cs211.sudoku.model;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

/**
 * A class for reading in and parsing a SUD file (a format for storing
 * uncompleted sudoku puzzles) and outputting them in a new Grid object.
 * 
 * @author Josh Tumath (jmt14@aber.ac.uk)
 */
public class SudFileImporter
implements Closeable {
	// /////////////////// //
	// Instance variables. //
	// /////////////////// //
	/** The scanner used to read a sudoku grid file. */
	private Scanner sudFile;
	
	// ///////////// //
	// Constructors. //
	// ///////////// //
	/**
	 * Constructs an object for importing an SUD file and converting it into a
	 * Grid object.
	 * @param file the file containing the data to be imported
	 * @throws FileNotFoundException if the file cannot be found in the file
	 *                               system.
	 */
	public SudFileImporter(File file) throws FileNotFoundException {
		sudFile =
			new Scanner(new BufferedReader(new FileReader(file.getPath())));
	}
	
	// //////// //
	// Methods. //
	// //////// //
	/**
	 * Imports the grid data from a SUD file. The location of the SUD file is
	 * passed in via the constructor.
	 * @return a new Grid object representing the data in the SUD file
	 * @throws InvalidSudFileException if the SUD file stores the data in an
	 *                                 invalid format.
	 */
	public Grid importGrid() throws InvalidSudFileException {
		Grid grid = new Grid();
		String row;
		
		// Loop through each line in the file
		for (int y = 0; y < 9; y++) {
			// Throw if the next line doesn't exist
			if(!sudFile.hasNextLine()) {
				throw new InvalidSudFileException();
			}
			
			// Store a line of blocks in the file
			row = sudFile.nextLine();
			
			// Throw if the line is not nine characters
			if(row.length() < 9) {
				throw new InvalidSudFileException();
			}
			
			// Go through each character in the row and store as an enum
			for (int x = 0; x < 9; x++) {
				grid.setCell(x, y, charToCell(row.charAt(x)));
			}
		}
		
		return grid;
	}
	
	/**
	 * Converts a digit represented in unicode into an enum representing the
	 * possible values in the cell of a Sudoku grid.
	 * @param value a character ranging from 1 to 9 or a space
	 * @return an enum representing the possible values in a Sudoku grid
	 * @throws InvalidSudFileException if the SUD file contains a character that
	 *                                 is not either a digit or a space
	 */
	private CellValue charToCell(char value) throws InvalidSudFileException {
		switch (value) {
		case ' ':
			return CellValue.EMPTY;
		case '1':
			return CellValue.ONE;
		case '2':
			return CellValue.TWO;
		case '3':
			return CellValue.THREE;
		case '4':
			return CellValue.FOUR;
		case '5':
			return CellValue.FIVE;
		case '6':
			return CellValue.SIX;
		case '7':
			return CellValue.SEVEN;
		case '8':
			return CellValue.EIGHT;
		case '9':
			return CellValue.NINE;
		default:
			throw new InvalidSudFileException();
		}
	}

	@Override
	public void close() throws IOException {
		sudFile.close();
	}
}
