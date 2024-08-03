package util;

import java.awt.Color;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.Scanner;

/**
 * Class used for sorting and getting color information presets from file
 * 
 * @author Caleb Kolb
 */
public class ColorData {
	/** Holds an array of colors */
	private Color[] colors;
	/** Sorting method to use */
	private String sortHeuristic;
	/** Color settings file */
	private String colorFile;
	/** Size of the colors array */
	private int size;

	/**
	 * Creates a ColorData list from a file and sorts it off of the sort value
	 * giver.
	 * 
	 * @param sort the way to sort the list
	 * @param file the file to extract colors from
	 */
	public ColorData(String sort, String file) {
		setHeuristic(sort);
		try {
			new File(file).createNewFile();
			Scanner scanner = new Scanner(new File(file));
			colorFile = file;
			size = scanner.nextInt();
			colors = new Color[size];
			int counter = 0;
			scanner.nextLine();
			while (scanner.hasNextLine() && scanner.hasNext()) {
				scanner.useDelimiter("[\\n,]+");
				int red = scanner.nextInt();
				int green = scanner.nextInt();
				int blue = scanner.nextInt();
				if (scanner.hasNextLine()) {
					scanner.nextLine();
				}
				colors[counter] = new Color(red, green, blue);
				counter++;
			}
			scanner.close();
		} catch (Exception e) {
			throw new IllegalArgumentException(e.getMessage());
		}
	}

	/**
	 * Adds a color to the list of color
	 * 
	 * @param newColor the new color to add
	 */
	public void addColor(Color newColor) {
		size++;
		Color[] newColors = new Color[size];
		if ("mtf".equals(sortHeuristic)) {
			newColors[0] = newColor;
			for (int i = 0; i < size - 1; i++) {
				newColors[i + 1] = colors[i];
			}
		} else {
			newColors[size - 1] = newColor;
			for (int i = 0; i < size - 1; i++) {
				newColors[i] = colors[i];
			}
		}
		colors = newColors;
		saveColor();
	}

	/**
	 * Moves a color to the front of the list if it exist
	 * 
	 * @param color the color to mtf
	 */
	public void moveToFront(Color color) {
		Color[] oldColors = new Color[size];
		for (int i = 0; i < size; i++) {
			oldColors[i] = colors[i];
		}
		Color temp = new Color(0, 0, 0);
		for (int i = 0; i < size; i++) {
			if (i == 0) {
				if (colors[0].equals(color)) {
					return;
				}
				temp = colors[i];
				colors[i] = color;
			} else {
				if (colors[i].equals(color)) {
					colors[i] = temp;
					saveColor();
					return;
				}
				Color temp2 = colors[i];
				colors[i] = temp;
				temp = temp2;
			}
		}
		for (int i = 0; i < size; i++) {
			colors[i] = oldColors[i];
		}
	}

	/**
	 * Removes a color to the list of color
	 * 
	 * @param color the color to remove
	 */
	public void removeColor(Color color) {
		if (size <= 1) {
			throw new IllegalArgumentException("Can't remove last color");
		}
		boolean foundColor = false;
		for (int i = 0; i < size - 1; i++) {
			if (foundColor || colors[i].equals(color)) {
				foundColor = true;
				colors[i] = colors[i + 1];
			}
			if (i == size - 2 && colors[size - 1].equals(color)) {
				foundColor = true;
			}
		}
		if (foundColor) {
			colors[size - 1] = null;
			size--;
			saveColor();
		}
	}

	private void saveColor() {
		try {
			PrintStream fileWriter = new PrintStream(colorFile);
			fileWriter.print(size + "\n");
			for (int i = 0; i < size; i++) {
				fileWriter.print(colors[i].getRed() + "," + colors[i].getGreen() + "," + colors[i].getBlue() + "\n");
			}
			fileWriter.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Sets the heuristic for sorting
	 * 
	 * @param heuristic the heuristic to sort
	 */
	public void setHeuristic(String heuristic) {
		sortHeuristic = heuristic;
	}

	/**
	 * Returns an array of colors
	 * 
	 * @return colors an array of colors
	 */
	public Color[] getColors() {
		return colors;
	}

	/**
	 * Prints out all colors to cmd
	 */
	public void printColors() {
		for (int i = 0; i < size; i++) {
			System.out.println("Color " + (i + 1) + ": " + colors[i].getRed() + " " + colors[i].getGreen() + " "
					+ colors[i].getBlue());
		}
	}

	/**
	 * Returns the size of the list
	 * 
	 * @return size the size of the list
	 */
	public int getSize() {
		return size;
	}
}