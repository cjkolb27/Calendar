package util;

import java.awt.Color;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.Scanner;

/**
 * Class for storing, loading, and saving preset data for the Calendar.
 * 
 * @author Caleb Kolb
 */
public class PresetData {
	/** List of PresetItems */
	private PresetItems[] presets;
	/** String versions of presets to view in JComboBoxes */
	private String[] stringPresets;
	/** File used for loading and saving presets */
	private String presetFile;
	/** Size of presets list */
	private int size;

	/**
	 * Creates all preset data
	 * 
	 * @param file the file of presets
	 */
	public PresetData(String file) {
		try {
			new File(file).createNewFile();
			Scanner scanner = new Scanner(new File(file));
			presetFile = file;
			size = scanner.nextInt();
			presets = new PresetItems[size];
			int counter = 0;
			scanner.nextLine();
			while (scanner.hasNextLine() && scanner.hasNext()) {
				scanner.useDelimiter("[\\n,]+");
				String name = scanner.next();
				String start = scanner.next();
				String end = scanner.next();
				int red = scanner.nextInt();
				int green = scanner.nextInt();
				int blue = scanner.nextInt();
				if (scanner.hasNextLine()) {
					scanner.nextLine();
				}
				presets[counter] = new PresetItems(name, start, end, new Color(red, green, blue));
				counter++;
			}
			scanner.close();
		} catch (Exception e) {
			throw new IllegalArgumentException(e.getMessage());
		}
		setStringPresets();
	}

	/**
	 * Adds a preset to the list sorting by start time and alphabetical name. If an
	 * added event already exists, an IAE is thrown.
	 * 
	 * @param name  the name of the event
	 * @param start the start time
	 * @param end   the end time
	 * @param color the color of the event
	 */
	public void addPreset(String name, String start, String end, Color color) {
		PresetItems newPreset = new PresetItems(name, start, end, color);
		PresetItems[] newItems = new PresetItems[size + 1];
		boolean added = false;
		for (int i = 0; i < size; i++) {
			if (presets[i].getName().equals(newPreset.getName()) && presets[i].getStartInt() == newPreset.getStartInt()
					&& presets[i].getEnd().equals(newPreset.getEnd())
					&& presets[i].getColor().equals(newPreset.getColor())) {
				throw new IllegalArgumentException("Event Already Exists");
			}
			if (!added) {
				if (presets[i].getStartInt() > newPreset.getStartInt()) {
					newItems[i] = newPreset;
					newItems[i + 1] = presets[i];
					added = true;
				} else if (presets[i].getStartInt() == newPreset.getStartInt()
						&& Character.toLowerCase(presets[i].getName().charAt(0)) > Character
								.toLowerCase(newPreset.getName().charAt(0))) {
					newItems[i] = newPreset;
					newItems[i + 1] = presets[i];
					added = true;
				} else
					newItems[i] = presets[i];
			} else
				newItems[i + 1] = presets[i];
		}
		if (!added) {
			newItems[size] = newPreset;
		}
		presets = newItems;
		size++;
		savePreset();
		setStringPresets();
	}

	/**
	 * Removes a given preset from the list if it exists
	 * 
	 * @param name  the name of the event
	 * @param start the start time of the event
	 * @param end   the end time of the event
	 * @param color the color of the event
	 */
	public void removePreset(String name, String start, String end, Color color) {
		if (size <= 1) {
			throw new IllegalArgumentException("Can't remove last preset");
		}
		boolean foundPreset = false;
		PresetItems[] newPresets = new PresetItems[size - 1];
		for (int i = 0; i < size; i++) {
			if (i < size - 1) {
				if (!foundPreset && presets[i].getName().equals(name) && presets[i].getStart().equals(start)
						&& presets[i].getEnd().equals(end) && presets[i].getColor().equals(color)) {
					foundPreset = true;
				} else if (foundPreset) {
					newPresets[i - 1] = presets[i];
				} else
					newPresets[i] = presets[i];
			} else {
				if (presets[i].getName().equals(name) && presets[i].getStart().equals(start)
						&& presets[i].getEnd().equals(end)) {
					presets = newPresets;
					size--;
					savePreset();
					setStringPresets();
					return;
				} else if (foundPreset) {
					newPresets[i - 1] = presets[i];
					presets = newPresets;
					size--;
					savePreset();
					setStringPresets();
					return;
				} else
					return;
			}
		}
	}

	/**
	 * Checks if a preset is a duplicate. Returns true if there isn't a duplicate,
	 * false if there is a duplicate.
	 * 
	 * @param name  the name of the event
	 * @param start the start time of the event
	 * @param end   the end time of the event
	 * @param color the color of the event
	 * @return boolean true if not duplicate, false if duplicate
	 */
	public boolean duplicatePreset(String name, String start, String end, Color color) {
		PresetItems newPreset = new PresetItems(name, start, end, color);
		for (int i = 0; i < size; i++) {
			if (presets[i].getName().equals(newPreset.getName()) && presets[i].getStartInt() == newPreset.getStartInt()
					&& presets[i].getEnd().equals(newPreset.getEnd())
					&& presets[i].getColor().equals(newPreset.getColor())) {
				System.out.println("Duplicate Preset Found");
				return false;
			}
		}
		System.out.println("No Duplicate");
		return true;
	}

	/**
	 * Sets the string of preset events
	 */
	private void setStringPresets() {
		stringPresets = new String[size];
		for (int i = 0; i < size; i++) {
			stringPresets[i] = presets[i].getName() + " " + presets[i].getStart() + "-" + presets[i].getEnd();
		}
	}

	/**
	 * Returns an array of all presets as strings
	 * 
	 * @return stringPresets an array of all presets as strings
	 */
	public String[] getStringPresets() {
		return stringPresets;
	}

	/**
	 * Save the presets to the original given file
	 */
	private void savePreset() {
		try {
			PrintStream fileWriter = new PrintStream(presetFile);
			fileWriter.print(size + "\n");
			for (int i = 0; i < size; i++) {
				fileWriter.print(presets[i].getName() + "," + presets[i].getStart() + "," + presets[i].getEnd() + ","
						+ presets[i].getColor().getRed() + "," + presets[i].getColor().getGreen() + ","
						+ presets[i].getColor().getBlue() + "\n");
			}
			fileWriter.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Returns a list of PresetItems
	 * 
	 * @return presets a list of PresetItems
	 */
	public PresetItems[] getPresets() {
		return presets;
	}

	/**
	 * Returns the size of the list
	 * 
	 * @return size the size of the list
	 */
	public int getSize() {
		return size;
	}

	/**
	 * Holds an events name, start time, end time, start time as an int, and the
	 * color.
	 * 
	 * @author Caleb Kolb
	 */
	public class PresetItems {
		/** Event name */
		private String name;
		/** Start time */
		private String start;
		/** End time */
		private String end;
		/** Color of event */
		private Color color;
		/** Start time as an int */
		private int startInt;

		/**
		 * Create a preset item used for staring a preset events information
		 * 
		 * @param name  the name of the event
		 * @param start the start time of the event
		 * @param end   the end time of the event
		 * @param color the color of the event
		 */
		public PresetItems(String name, String start, String end, Color color) {
			setName(name);
			setStart(start);
			setEnd(end);
			setColor(color);
			setStartInt();
		}

		/**
		 * Sets the name of the event and checks if it is validS
		 * 
		 * @param name the name of the event
		 */
		public void setName(String name) {
			if (name == null || "".equals(name)) {
				throw new IllegalArgumentException("Invalid Start Time");
			}
			this.name = name;
		}

		/**
		 * Sets the start time of the event and checks if the inputed time is valid
		 * 
		 * @param start the start time
		 */
		public void setStart(String start) {
			if (start == null || "".equals(start)) {
				throw new IllegalArgumentException("Invalid Start Time");
			}
			checkStartTime(start);
		}

		/**
		 * Sets the end time of the event and checks if the inputed time is valid
		 * 
		 * @param end the end time
		 */
		public void setEnd(String end) {
			if (end == null || "".equals(end)) {
				throw new IllegalArgumentException("Invalid Start Time");
			}
			checkEndTime(end);
			this.end = end;
		}

		/**
		 * Sets the color of the event
		 * 
		 * @param color the color of the event
		 */
		public void setColor(Color color) {
			this.color = color;
		}

		/**
		 * returns the name of the event
		 * 
		 * @return name the name of the event
		 */
		public String getName() {
			return name;
		}

		/**
		 * Returns the start time of the event
		 * 
		 * @return start the start time of the event
		 */
		public String getStart() {
			return start;
		}

		/**
		 * Returns the end time of the event
		 * 
		 * @return end the end time of the event
		 */
		public String getEnd() {
			return end;
		}

		/**
		 * Returns the color of the event
		 * 
		 * @return color the color of the event
		 */
		public Color getColor() {
			return color;
		}

		/**
		 * Returns the start time as an int
		 * 
		 * @return startInt the start time as an int
		 */
		public int getStartInt() {
			return startInt;
		}

		private void setStartInt() {
			if (start.length() == 6) {
				int time = Integer.parseInt("" + start.charAt(0) + start.charAt(2) + start.charAt(3));
				if (start.charAt(4) == 'p') {
					time = time + 1200;
				}
				startInt = time;
			} else {
				int time = Integer.parseInt("" + start.charAt(0) + start.charAt(1) + start.charAt(3) + start.charAt(4));
				if (start.charAt(5) == 'p') {
					time = time + 1200;
				} else if (start.charAt(5) == 'a' && start.charAt(1) == '2') {
					time = time - 1200;
				}
				startInt = time;
			}
		}

		/**
		 * Checks if a given start time is readable
		 * 
		 * @param check the start time to check
		 */
		public void checkStartTime(String check) {
			String startT = check;
			if (startT == null || "".equals(startT)) {
				throw new IllegalArgumentException("Invalid Start Time");
			}
			if (startT.charAt(startT.length() - 1) == 'm') {
				StringBuffer str = new StringBuffer();
				str.append(startT);
				str.deleteCharAt(startT.length() - 1);
				startT = str.toString();
			}
			if (startT.contains(":")) {
				if (startT.length() < 5 || startT.length() > 6) {
					throw new IllegalArgumentException("Invalid Start Time");
				}
				if (startT.length() == 5) {
					if (startT.charAt(4) == 'a') {
						this.start = "" + startT.charAt(0) + ":" + startT.charAt(2) + startT.charAt(3) + "am";
					} else if (startT.charAt(4) == 'p') {
						this.start = "" + startT.charAt(0) + ":" + startT.charAt(2) + startT.charAt(3) + "pm";
					} else
						throw new IllegalArgumentException("Invalid Start Time");
				} else if (startT.length() == 6) {
					if (startT.charAt(5) == 'a') {
						this.start = "" + startT.charAt(0) + startT.charAt(1) + ":" + startT.charAt(3)
								+ startT.charAt(4) + "am";
					} else if (startT.charAt(5) == 'p') {
						this.start = "" + startT.charAt(0) + startT.charAt(1) + ":" + startT.charAt(3)
								+ startT.charAt(4) + "pm";
					} else
						throw new IllegalArgumentException("Invalid Start Time");
				}
			} else if (startT.length() < 4 || startT.length() > 5) {
				throw new IllegalArgumentException("Invalid Start Time");
			} else if (startT.length() == 4) {
				if (startT.charAt(3) == 'a') {
					this.start = "" + startT.charAt(0) + ":" + startT.charAt(1) + startT.charAt(2) + "am";
				} else if (startT.charAt(3) == 'p') {
					this.start = "" + startT.charAt(0) + ":" + startT.charAt(1) + startT.charAt(2) + "pm";
				} else
					throw new IllegalArgumentException("Invalid Start Time");
			} else if (startT.length() == 5) {
				if (startT.charAt(4) == 'a') {
					this.start = "" + startT.charAt(0) + startT.charAt(1) + ":" + startT.charAt(2) + startT.charAt(3)
							+ "am";
				} else if (startT.charAt(4) == 'p') {
					this.start = "" + startT.charAt(0) + startT.charAt(1) + ":" + startT.charAt(2) + startT.charAt(3)
							+ "pm";
				} else
					throw new IllegalArgumentException("Invalid Start Time");
			} else
				throw new IllegalArgumentException("Invalid Start Time");
		}

		/**
		 * Checks if a given end time is readable
		 * 
		 * @param check the end time to check
		 */
		public void checkEndTime(String check) {
			String endT = check;
			if (endT == null || "".equals(endT)) {
				throw new IllegalArgumentException("Invalid Start Time");
			}
			if (endT.charAt(endT.length() - 1) == 'm') {
				StringBuffer str = new StringBuffer();
				str.append(endT);
				str.deleteCharAt(endT.length() - 1);
				endT = str.toString();
			}
			if (endT.contains(":")) {
				if (endT.length() < 5 || endT.length() > 6) {
					throw new IllegalArgumentException("Invalid Start Time");
				}
				if (endT.length() == 5) {
					if (endT.charAt(4) == 'a') {
						this.end = "" + endT.charAt(0) + ":" + endT.charAt(2) + endT.charAt(3) + "am";
					} else if (endT.charAt(4) == 'p') {
						this.end = "" + endT.charAt(0) + ":" + endT.charAt(2) + endT.charAt(3) + "pm";
					} else
						throw new IllegalArgumentException("Invalid Start Time");
				} else if (endT.length() == 6) {
					if (endT.charAt(5) == 'a') {
						this.end = "" + endT.charAt(0) + endT.charAt(1) + ":" + endT.charAt(3) + endT.charAt(4) + "am";
					} else if (endT.charAt(5) == 'p') {
						this.end = "" + endT.charAt(0) + endT.charAt(1) + ":" + endT.charAt(3) + endT.charAt(4) + "pm";
					} else
						throw new IllegalArgumentException("Invalid Start Time");
				}
			} else if (endT.length() < 4 || endT.length() > 5) {
				throw new IllegalArgumentException("Invalid Start Time");
			} else if (endT.length() == 4) {
				if (endT.charAt(3) == 'a') {
					this.end = "" + endT.charAt(0) + ":" + endT.charAt(1) + endT.charAt(2) + "am";
				} else if (endT.charAt(3) == 'p') {
					this.end = "" + endT.charAt(0) + ":" + endT.charAt(1) + endT.charAt(2) + "pm";
				} else
					throw new IllegalArgumentException("Invalid Start Time");
			} else if (endT.length() == 5) {
				if (endT.charAt(4) == 'a') {
					this.end = "" + endT.charAt(0) + endT.charAt(1) + ":" + endT.charAt(2) + endT.charAt(3) + "am";
				} else if (endT.charAt(4) == 'p') {
					this.end = "" + endT.charAt(0) + endT.charAt(1) + ":" + endT.charAt(2) + endT.charAt(3) + "pm";
				} else
					throw new IllegalArgumentException("Invalid Start Time");
			} else
				throw new IllegalArgumentException("Invalid Start Time");
		}
	}
}
