package uk.ac.aber.cs211.sudoku.ui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JToolBar;
import javax.swing.filechooser.FileNameExtensionFilter;

import uk.ac.aber.cs211.sudoku.model.Grid;
import uk.ac.aber.cs211.sudoku.model.InvalidSudFileException;
import uk.ac.aber.cs211.sudoku.model.SudFileImporter;
import uk.ac.aber.cs211.sudoku.solver.Solver;


/**
 * Creates a window for rendering the UI for the application.
 * 
 * @author Josh Tumath (jmt14@aber.ac.uk)
 */
public class MainWindow extends JFrame
implements ActionListener {
	// ////////// //
	// Constants. //
	// ////////// //
	private static final String CMD_OPEN = "open";
	private static final String CMD_SOLVE = "solve";
	private static final String CMD_STOP = "stop";
	
	// /////////////////// //
	// Instance variables. //
	// /////////////////// //
	/** A panel that rendered to show the sudoku grid. */
	private GridCanvas gridCanvas = new GridCanvas();
	
	/** Contains the thread that solves the sudoku puzzles. */
	private Thread solverThread;
	
	// ///////////// //
	// Constructors. //
	// ///////////// //
	/**
	 * Creates a new JFrame and sets up the initial content.
	 */
	public MainWindow() {
		setTitle("Sudoku Solver");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		// Create toolbar
		getContentPane().add(createToolBar(), BorderLayout.NORTH);
		
		// Draw the empty grid
		getContentPane().add(gridCanvas, BorderLayout.CENTER);
		
		pack();
		setResizable(false);
		setLocationRelativeTo(null); // Centre window in middle of screen
		setVisible(true);
	}
	
	// //////// //
	// Methods. //
	// //////// //
	/**
	 * Creates a toolbar with buttons for the user interaction with the
	 * application.
	 * @return the toolbar
	 */
	private JToolBar createToolBar() {
		JToolBar toolBar = new JToolBar();
		toolBar.setFloatable(false);
		
		JButton btn;
		
		btn = new JButton("Open...");
		btn.setToolTipText("Open a Sudoku puzzle...");
		btn.setActionCommand(CMD_OPEN);
		btn.addActionListener(this);
		toolBar.add(btn);
		
		btn = new JButton("Solve");
		btn.setToolTipText("Watch the puzzle be solved");
		btn.setActionCommand(CMD_SOLVE);
		btn.addActionListener(this);
		toolBar.add(btn);
		
		btn = new JButton("Stop");
		btn.setToolTipText("Stop the puzzle while it's being solved");
		btn.setActionCommand(CMD_STOP);
		btn.addActionListener(this);
		toolBar.add(btn);
		
		return toolBar;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String cmd = e.getActionCommand();
		
		switch (cmd) {
		case CMD_OPEN:
			openSudFile();
			break;
		case CMD_SOLVE:
			solvePuzzle();
			break;
		case CMD_STOP:
			stopSolvingPuzzle();
			break;
		}
	}
	
	/**
	 * Open a file chooser to load in a SUD file.
	 */
	private void openSudFile() {
		JFileChooser fileChooser = new JFileChooser();
		
		fileChooser.setFileFilter(
			new FileNameExtensionFilter("Sudoku puzzle file (*.sud)", "sud"));
		
		// Show the open dialog and - if the user selects a file - load the file
		if(fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
			File sudFile = fileChooser.getSelectedFile();
			
			// Open the file and close it when done
			try (SudFileImporter sudFileImporter = new SudFileImporter(sudFile)) {
				Grid grid = sudFileImporter.importGrid();
				
				gridCanvas.setGrid(grid);
				grid.addObserver(gridCanvas);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
				JOptionPane.showMessageDialog(
					this,
					"The file could not be found.",
					"File not found",
					JOptionPane.ERROR_MESSAGE);
			} catch (IOException e) {
				e.printStackTrace();
			} catch (InvalidSudFileException e) {
				e.printStackTrace();
				JOptionPane.showMessageDialog(
					this,
					"The file you have selected is broken and cannot be used " +
					"to create a Sudoku puzzle.",
					"Invalid SUD file",
					JOptionPane.ERROR_MESSAGE);
			}
		}
	}
	
	/**
	 * Solve the puzzle that has been loaded. 
	 */
	private void solvePuzzle() {
		if (gridCanvas.getGrid() != null) {
			solverThread = new Thread(new Solver(gridCanvas.getGrid()));
			
			solverThread.start();
		} else {
			JOptionPane.showMessageDialog(
					this,
					"You have not opened a sudoku puzzle to solve yet.",
					"No sudoku puzzle to solve",
					JOptionPane.ERROR_MESSAGE);
		}
	}
	
	private void stopSolvingPuzzle() {
		if (solverThread == null) {
			JOptionPane.showMessageDialog(
					this,
					"You have not opened a sudoku puzzle to solve yet.",
					"No sudoku puzzle to solve",
					JOptionPane.ERROR_MESSAGE);
		} else if (solverThread.isAlive()) {
			solverThread.interrupt();
		}
	}
	
	private static final long serialVersionUID = -7584219545108315719L;
}
