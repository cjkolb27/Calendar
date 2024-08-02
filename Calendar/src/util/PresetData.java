package util;

import java.awt.Color;
import java.io.File;
import java.util.Scanner;

/**
 * 
 * 
 * @author Caleb Kolb
 */
public class PresetData {
	/** */
	private PresetItems[] presets;
	/** */
	private String presetFile;
	/** */
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
			if (presets[i].getName() == newPreset.getName() && presets[i].getStartInt() == newPreset.getStartInt()
					&& presets[i].getEnd() == newPreset.getEnd()) {
				throw new IllegalArgumentException("Event Already Exists");
			}
			if (!added) {
				if (presets[i].getStartInt() > newPreset.getStartInt()) {
					newItems[i] = newPreset;
					added = true;
				} else if (presets[i].getStartInt() == newPreset.getStartInt()
						&& presets[i].getName().charAt(0) > newPreset.getName().charAt(0)) {
					newItems[i] = newPreset;
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
	}

	public void removePreset(String name, String start, String end, Color color) {

	}

	private void savePreset() {

	}

	public PresetItems[] getPresets() {
		return presets;
	}

	public int getSize() {
		return size;
	}

	private class PresetItems {
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

		public PresetItems(String name, String start, String end, Color color) {
			setName(name);
			setStart(start);
			setEnd(end);
			setColor(color);
			setStartInt();
		}

		public void setName(String name) {
			if (name == null || "".equals(name)) {
				throw new IllegalArgumentException("Invalid Start Time");
			}
			this.name = name;
		}

		public void setStart(String start) {
			if (start == null || "".equals(start)) {
				throw new IllegalArgumentException("Invalid Start Time");
			}
			checkStartTime(start);
		}

		public void setEnd(String end) {
			if (end == null || "".equals(end)) {
				throw new IllegalArgumentException("Invalid Start Time");
			}
			checkEndTime(end);
			this.end = end;
		}

		public void setColor(Color color) {
			this.color = color;
		}

		public String getName() {
			return name;
		}

		public String getStart() {
			return start;
		}

		public String getEnd() {
			return end;
		}

		public Color getColor() {
			return color;
		}

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
