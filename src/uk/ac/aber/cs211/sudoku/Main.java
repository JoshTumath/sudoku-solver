package uk.ac.aber.cs211.sudoku;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import uk.ac.aber.cs211.sudoku.ui.MainWindow;


/**
 * Hours of work so far: 40
 * 
* @author Josh Tumath (jmt14@aber.ac.uk)
* @since 1.0
* @version 1.0
*/
public class Main {
	public static void main(String[] args) {
		// Set the look and feel to a native system UI.
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (UnsupportedLookAndFeelException
				|ClassNotFoundException
				|InstantiationException
				|IllegalAccessException e) {
			System.err.println(e);
		}
		
		// Open the main window.
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				new MainWindow();
			}
		});
	}
}
