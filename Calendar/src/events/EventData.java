package events;

import java.awt.Color;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

/**
 * Abstract Class for setting all event data
 * 
 * @author Caleb Kolb
 */
public class EventData {
	/** Event name */
	private String name;
	/** Event start time */
	private String startTime;
	/** Event end time */
	private String endTime;
	/** Previous event details */
	private String previous;
	/** Timestamp of when event was created */
	private String timestamp;
	/** Synce state of the event */
	private SyncState syncState;
	/** Start time int */
	private int startInt;
	/** End time int */
	private int endInt;
	/** Event day */
	private int day;
	/** Event month */
	private int month;
	/** Event year */
	private int year;
	/** Date factor */
	private double date;
	/** Color of an event */
	private Color eventColor;

	/**
	 * Sets all parameters in an Event
	 * 
	 * @param name      the name of the event
	 * @param startTime the start time of the event
	 * @param endTime   the end time of the event
	 * @param day       the day of the event
	 * @param month     the month of the event
	 * @param year      the year of the event
	 * @param red       the red color
	 * @param blue      the blue color
	 * @param green     the green color
	 * @param synced    the synced boolean if synced to the server
	 * @param timestamp the timestamp of when the event was created
	 */
	public EventData(String name, String startTime, String endTime, int day, int month, int year, int red, int green,
			int blue, SyncState syncState, String previous, String timestamp) {
		setColor(red, green, blue);
		setName(name);
		setStartTime(startTime);
		setEndTime(endTime);
		setStartInt();
		setEndInt();
		setYear(year);
		setMonth(month);
		setDay(day);
		setSyncState(syncState);
		setPrevious(previous);
		if (previous == null) {
			setPrevious(" ");
		}
		setDate();
		if (" ".equals(timestamp)) {
			String formatted = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").withZone(ZoneId.systemDefault())
					.format(Instant.now());
			setTimestamp(formatted);
		} else {
			setTimestamp(timestamp);
		}
	}

	private void setColor(int red, int green, int blue) {
		if (red < 0 || red > 255 || green < 0 || green > 255 || blue < 0 || blue > 255) {
			eventColor = new Color(255, 255, 255);
		} else {
			eventColor = new Color(red, green, blue);
		}

	}

	private void setName(String name) {
		if (name == null || "".equals(name)) {
			throw new IllegalArgumentException("Invalid Event");
		}
		this.name = name;
	}

	private void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

	public String getTimestamp() {
		return timestamp;
	}

	private void setStartTime(String startTime) {
		String start = startTime;
		if (start == null || "".equals(start)) {
			throw new IllegalArgumentException("Invalid Start Time");
		}
		if (start.charAt(start.length() - 1) == 'm') {
			StringBuffer str = new StringBuffer();
			str.append(start);
			str.deleteCharAt(start.length() - 1);
			start = str.toString();
		}
		if (start.contains(":")) {
			if (start.length() < 5 || start.length() > 6) {
				throw new IllegalArgumentException("Invalid Start Time");
			}
			if (start.length() == 5) {
				if (start.charAt(4) == 'a') {
					this.startTime = "" + start.charAt(0) + ":" + start.charAt(2) + start.charAt(3) + "am";
				} else if (start.charAt(4) == 'p') {
					this.startTime = "" + start.charAt(0) + ":" + start.charAt(2) + start.charAt(3) + "pm";
				} else
					throw new IllegalArgumentException("Invalid Start Time");
			} else if (start.length() == 6) {
				if (start.charAt(5) == 'a') {
					this.startTime = "" + start.charAt(0) + start.charAt(1) + ":" + start.charAt(3) + start.charAt(4)
							+ "am";
				} else if (start.charAt(5) == 'p') {
					this.startTime = "" + start.charAt(0) + start.charAt(1) + ":" + start.charAt(3) + start.charAt(4)
							+ "pm";
				} else
					throw new IllegalArgumentException("Invalid Start Time");
			}
		} else if (start.length() < 4 || start.length() > 5) {
			throw new IllegalArgumentException("Invalid Start Time");
		} else if (start.length() == 4) {
			if (start.charAt(3) == 'a') {
				this.startTime = "" + start.charAt(0) + ":" + start.charAt(1) + start.charAt(2) + "am";
			} else if (start.charAt(3) == 'p') {
				this.startTime = "" + start.charAt(0) + ":" + start.charAt(1) + start.charAt(2) + "pm";
			} else
				throw new IllegalArgumentException("Invalid Start Time");
		} else if (start.length() == 5) {
			if (start.charAt(4) == 'a') {
				this.startTime = "" + start.charAt(0) + start.charAt(1) + ":" + start.charAt(2) + start.charAt(3)
						+ "am";
			} else if (start.charAt(4) == 'p') {
				this.startTime = "" + start.charAt(0) + start.charAt(1) + ":" + start.charAt(2) + start.charAt(3)
						+ "pm";
			} else
				throw new IllegalArgumentException("Invalid Start Time");
		} else
			throw new IllegalArgumentException("Invalid Start Time");
	}

	private void setEndTime(String endTime) {
		String end = endTime;
		if (end == null || "".equals(end)) {
			throw new IllegalArgumentException("Invalid End Time");
		}
		if (end.charAt(end.length() - 1) == 'm') {
			StringBuffer str = new StringBuffer();
			str.append(end);
			str.deleteCharAt(end.length() - 1);
			end = str.toString();
		}
		if (end.contains(":")) {
			if (end.length() < 5 || end.length() > 6) {
				throw new IllegalArgumentException("Invalid End Time");
			}
			if (end.length() == 5) {
				if (end.charAt(4) == 'a') {
					this.endTime = "" + end.charAt(0) + ":" + end.charAt(2) + end.charAt(3) + "am";
				} else if (end.charAt(4) == 'p') {
					this.endTime = "" + end.charAt(0) + ":" + end.charAt(2) + end.charAt(3) + "pm";
				} else
					throw new IllegalArgumentException("Invalid End Time");
			} else if (end.length() == 6) {
				if (end.charAt(5) == 'a') {
					this.endTime = "" + end.charAt(0) + end.charAt(1) + ":" + end.charAt(3) + end.charAt(4) + "am";
				} else if (end.charAt(5) == 'p') {
					this.endTime = "" + end.charAt(0) + end.charAt(1) + ":" + end.charAt(3) + end.charAt(4) + "pm";
				} else
					throw new IllegalArgumentException("Invalid End Time");
			}
		} else if (end.length() < 4 || end.length() > 5) {
			throw new IllegalArgumentException("Invalid End Time");
		} else if (end.length() == 4) {
			if (end.charAt(3) == 'a') {
				this.endTime = "" + end.charAt(0) + ":" + end.charAt(1) + end.charAt(2) + "am";
			} else if (end.charAt(3) == 'p') {
				this.endTime = "" + end.charAt(0) + ":" + end.charAt(1) + end.charAt(2) + "pm";
			} else
				throw new IllegalArgumentException("Invalid End Time");
		} else if (end.length() == 5) {
			if (end.charAt(4) == 'a') {
				this.endTime = "" + end.charAt(0) + end.charAt(1) + ":" + end.charAt(2) + end.charAt(3) + "am";
			} else if (end.charAt(4) == 'p') {
				this.endTime = "" + end.charAt(0) + end.charAt(1) + ":" + end.charAt(2) + end.charAt(3) + "pm";
			} else
				throw new IllegalArgumentException("Invalid End Time");
		} else
			throw new IllegalArgumentException("Invalid End Time");
	}

	private void setStartInt() {
		if (startTime.length() == 6) {
			int time = Integer.parseInt("" + startTime.charAt(0) + startTime.charAt(2) + startTime.charAt(3));
			if (startTime.charAt(4) == 'p') {
				time = time + 1200;
			}
			startInt = time;
		} else {
			int time = Integer.parseInt(
					"" + startTime.charAt(0) + startTime.charAt(1) + startTime.charAt(3) + startTime.charAt(4));
			if (startTime.charAt(5) == 'p') {
				time = time + 1200;
			} else if (startTime.charAt(5) == 'a' && startTime.charAt(1) == '2') {
				time = time - 1200;
			}
			startInt = time;
		}
	}

	private void setEndInt() {
		if (endTime.length() == 6) {
			int time = Integer.parseInt("" + endTime.charAt(0) + endTime.charAt(2) + endTime.charAt(3));
			if (endTime.charAt(4) == 'p') {
				time = time + 1200;
			}
			endInt = time;
		} else {
			int time = Integer
					.parseInt("" + endTime.charAt(0) + endTime.charAt(1) + endTime.charAt(3) + endTime.charAt(4));
			if (endTime.charAt(5) == 'p') {
				time = time + 1200;
			} else if (startTime.charAt(5) == 'a' && startTime.charAt(1) == '2') {
				time = time - 1200;
			}
			endInt = time;
		}
	}

	private void setDay(int day) {
		if (day < 1) {
			throw new IllegalArgumentException("Invalid Day");
		}
		if (month == 1 || month == 3 || month == 5 || month == 7 || month == 8 || month == 10 || month == 12) {
			if (day > 31) {
				throw new IllegalArgumentException("Invalid Day");
			} else {
				this.day = day;
			}
		} else if (month == 4 || month == 6 || month == 9 || month == 11) {
			if (day > 30) {
				throw new IllegalArgumentException("Invalid Day");
			} else {
				this.day = day;
			}
		} else if (month == 2 && day > 29) {
			throw new IllegalArgumentException("Invalid Day");
		} else {
			this.day = day;
		}
	}

	private void setMonth(int month) {
		if (month > 12 || month < 1) {
			throw new IllegalArgumentException("Invalid Month");
		}
		this.month = month;
	}

	private void setYear(int year) {
		this.year = year;
	}

	private void setDate() {
		this.date = year + (month * 31 + (double) day) * .001;
	}

	public void setSyncState(SyncState state) {
		syncState = state;
	}

	public SyncState getSyncState() {
		return syncState;
	}

	public void setPrevious(String previous) {
		this.previous = previous;
	}

	public String getPrevious() {
		return previous;
	}

	/**
	 * Returns the name
	 * 
	 * @return the name of the event
	 */
	public String getName() {
		return name;
	}

	/**
	 * Returns the start time
	 * 
	 * @return the start time of the event
	 */
	public String getStartTime() {
		return startTime;
	}

	/**
	 * Returns the end time
	 * 
	 * @return the end time of the event
	 */
	public String getEndTime() {
		return endTime;
	}

	/**
	 * Returns the start time as an int
	 * 
	 * @return startInt the start time
	 */
	public int getStartInt() {
		return startInt;
	}

	/**
	 * Returns the end time as an int
	 * 
	 * @return endInt the end time
	 */
	public int getEndInt() {
		return endInt;
	}

	/**
	 * Returns the day
	 * 
	 * @return the day of the event
	 */
	public int getDay() {
		return day;
	}

	/**
	 * Returns the month
	 * 
	 * @return the month of the event
	 */
	public int getMonth() {
		return month;
	}

	/**
	 * Returns the year
	 * 
	 * @return year the year of the event
	 */
	public int getYear() {
		return year;
	}

	/**
	 * Returns the date
	 * 
	 * @return date the date factor
	 */
	public double getDate() {
		return date;
	}

	/**
	 * Returns the color of an event
	 * 
	 * @return eventColor the color of the event
	 */
	public Color getColor() {
		return eventColor;
	}

	public enum SyncState {
		/** Synced state */
		Synced,
		/** Not Synced state */
		NotSynced,
		/** Deleted state */
		Deleted,
		/** Edited state */
		Edited
	}

	@Override
	public String toString() {
		return getSyncState().toString() + "@@" + getPrevious() + "@@" + getName() + "@@"
				+ getStartTime().substring(0, getStartTime().length() - 1) + "@@"
				+ getEndTime().substring(0, getEndTime().length() - 1) + "@@" + getDay() + "@@" + getMonth() + "@@"
				+ getYear() + "@@" + getColor().getRed() + "@@" + getColor().getGreen() + "@@" + getColor().getBlue()
				+ "@@" + getTimestamp() + "@@";
	}

	public String toStringSynced() {
		return getName() + "@@" + getStartTime().substring(0, getStartTime().length() - 1) + "@@"
				+ getEndTime().substring(0, getEndTime().length() - 1) + "@@" + getDay() + "@@" + getMonth() + "@@"
				+ getYear() + "@@" + getColor().getRed() + "@@" + getColor().getGreen() + "@@" + getColor().getBlue()
				+ "@@" + getTimestamp() + "@@";
	}

	/**
	 * Updates all data when editing an event
	 * 
	 * @param name      the name of the event
	 * @param startTime the start time of the event
	 * @param endTime   the end time of the event
	 * @param day       the day of the event
	 * @param month     the month of the event
	 * @param year      the year of the event
	 * @param red       the red color
	 * @param green     the green color
	 * @param blue      the blue color
	 */
	public void editData(String name, String startTime, String endTime, int day, int month, int year, int red,
			int green, int blue, SyncState syncState, String previous) {
		setColor(red, green, blue);
		setName(name);
		setStartTime(startTime);
		setEndTime(endTime);
		setYear(year);
		setMonth(month);
		setDay(day);
		setSyncState(syncState);
		setPrevious(previous);
		setDate();
	}
}
