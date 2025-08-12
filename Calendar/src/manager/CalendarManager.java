package manager;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.time.Year;
import java.util.Scanner;

import events.EventData;
import events.EventData.SyncState;
import util.SortedDateList;
import io.CalendarReader;
import io.CalendarWriter;
import ui.UI.ScreenState;

/**
 * Manager for interacting with the UI
 * 
 * @author Caleb Kolb
 */
public class CalendarManager {

	/** Current year */
	private int year = 0;
	/** Path to calendar data */
	private String path = null;
	/** Path change log */
	private String pathChangeLog = null;
	/** Sorted list for holding events */
	private SortedDateList<EventData> eventYearList;
	/** Array of all settings */
	private String[] allSettings;
	/** File Settings Location */
	private static final String SETTINGS = System.getProperty("user.home") + File.separator + "Documents"
			+ File.separator + "CalendarData" + File.separator + "Settings.txt";

	/**
	 * Creates a event year list of dates. This is used for setting up manager.
	 */
	public CalendarManager() {
		allSettings = new String[2];
		eventYearList = new SortedDateList<EventData>(0, 0);
		year = Year.now().getValue();
		loadSettings();
		path = System.getProperty("user.home") + File.separator + "Documents" + File.separator + "CalendarData"
				+ File.separator + year + ".txt";
		pathChangeLog = System.getProperty("user.home") + File.separator + "Documents" + File.separator + "CalendarData"
				+ File.separator + year + "_changes.txt";
		System.out.println(path);
		try {
			if (!new File(path).exists()) {
				new File(path).createNewFile();
				PrintStream fileWriter = new PrintStream(new File(path));
				fileWriter.print("1\n");
				fileWriter.close();
			}
			if (!new File(pathChangeLog).exists()) {
				new File(pathChangeLog).createNewFile();
				PrintStream fileWriter = new PrintStream(new File(pathChangeLog));
				fileWriter.print("0\n");
				fileWriter.close();
			}
		} catch (IOException e) {
			throw new IllegalArgumentException("No Calendar Files Exist\n");
		}
		loadEvents();
	}

	/**
	 * Used for debugging
	 * 
	 * @param debugYear the year set for debugging
	 */
	public CalendarManager(int debugYear) {
		eventYearList = new SortedDateList<EventData>(0, 0);
		year = debugYear;
		path = System.getProperty("user.home") + File.separator + "Documents" + File.separator + "CalendarData"
				+ File.separator + year + ".txt";
		pathChangeLog = System.getProperty("user.home") + File.separator + "Documents" + File.separator + "CalendarData"
				+ File.separator + year + "_changes.txt";
		try {
			new File(path).createNewFile();
			eventYearList = CalendarReader.readCalendar(new File(path), new File(pathChangeLog));
		} catch (IOException e) {
			e.printStackTrace();
		}
		// System.out.println(path);
	}

	/**
	 * Loads all settings in the settings.txt file
	 */
	private void loadSettings() {
		try {
			new File(SETTINGS).createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			Scanner scanner = new Scanner(new File(SETTINGS));
			try {
				path = scanner.nextLine();
				scanner.useDelimiter("window:");
				String windowMode = scanner.next();
				windowMode = windowMode.substring(0, windowMode.length() - 1);
				scanner.useDelimiter("");
				allSettings[0] = path;
				if ("Borderless".equals(windowMode) || "Windowed".equals(windowMode)
						|| "Fullscreen".equals(windowMode)) {
					allSettings[1] = windowMode;
				} else
					allSettings[1] = "Windowed";
			} catch (Exception e) {
				path = System.getProperty("user.home") + File.separator + "Documents" + File.separator + "CalendarData"
						+ File.separator + year + ".txt";
			}
			scanner.close();
		} catch (Exception e) {
			path = System.getProperty("user.home") + File.separator + "Documents" + File.separator + "CalendarData"
					+ File.separator + year + ".txt";
			allSettings[0] = path;
			allSettings[1] = "Windowed";
		}
	}

	/**
	 * Saves the settings to the settings.txt file
	 * 
	 * @param path   the calendar files
	 * @param window the window type
	 */
	public void saveSettings(String path, ScreenState window) {
		try {
			PrintStream fileWriter = new PrintStream(SETTINGS);
			fileWriter.print(path + '\n' + "window:" + window + '\n');
			fileWriter.close();
		} catch (Exception e) {
			System.out.println("Settings Failed");
			throw new IllegalArgumentException("Error when saving");
		}
		System.out.println("settings saved");
		loadSettings();
	}

	/**
	 * Takes in a year and opens the txt file with that years data.
	 * 
	 * @param yearAdd the year to load calendar
	 */
	public void loadCalendar(int yearAdd) {
		eventYearList = new SortedDateList<EventData>(0, 0);
		year += yearAdd;
		System.out.println("Something" + year);
		path = System.getProperty("user.home") + File.separator + "Documents" + File.separator + "CalendarData"
				+ File.separator + year + ".txt";
		pathChangeLog = System.getProperty("user.home") + File.separator + "Documents" + File.separator + "CalendarData"
				+ File.separator + year + "_changes.txt";
		try {
			if (!new File(path).exists()) {
				new File(path).createNewFile();
				PrintStream fileWriter = new PrintStream(new File(path));
				fileWriter.print("1\n");
				fileWriter.close();
			}
			if (!new File(pathChangeLog).exists()) {
				new File(pathChangeLog).createNewFile();
				PrintStream fileWriter = new PrintStream(new File(pathChangeLog));
				fileWriter.print("0\n");
				fileWriter.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		eventYearList = CalendarReader.readCalendar(new File(path), new File(pathChangeLog));
	}

	/**
	 * Takes in a year and opens the txt file with that years data.
	 * 
	 * @param name the name of the file
	 */
	public void loadCalendarByName(String name) {
		eventYearList = new SortedDateList<EventData>(0, 0);
		path = name;
		try {
			new File(path).createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
		eventYearList = CalendarReader.readCalendar(new File(path), new File(pathChangeLog));
	}
	
	public void loadEvents() {
		eventYearList = CalendarReader.readCalendar(new File(path), new File(pathChangeLog));
	}

	/**
	 * Saves the contents of the calendar
	 */
	public void saveCalendar() {
		try {
			//CalendarWriter.writeCalendar(new File(path), eventYearList, new File(pathChangeLog));
			CalendarWriter.writeNonSyncedCalendar(new File(pathChangeLog), eventYearList);
		} catch (Exception e) {
			throw new IllegalArgumentException(e.getMessage());
		}
	}
	
	public void approveChanges() {
		try {
			CalendarWriter.writeSyncedCalendar(new File(path), eventYearList, new File(pathChangeLog));
		} catch (Exception e) {
			throw new IllegalArgumentException(e.getMessage());
		}
	}
	
	public void patch(String patches, int version) {
		try {
			Scanner scanner = new Scanner(patches);
			scanner.useDelimiter("@@");
			while (scanner.hasNextLine() && scanner.hasNext()) {
				try {
					SyncState syncState = SyncState.valueOf(scanner.next());
					System.out.println("ALKJSFLKJIWJWMNWMWMWKMWIXIXIXIIXX" + syncState);
					if (syncState == null) {
						break;
					}
					String previous = scanner.next();
					String name = scanner.next();
					String startTime = scanner.next();
					String endTime = scanner.next();
					int day = scanner.nextInt();
					int month = scanner.nextInt();
					int years = scanner.nextInt();
					int red = scanner.nextInt();
					int green = scanner.nextInt();
					int blue = scanner.nextInt();
					String timestamp = scanner.next();
					if (scanner.hasNextLine()) {
						scanner.nextLine();
					}
					EventData eventData = new EventData(name, startTime, endTime, day, month, years, red, green, blue, syncState, previous, timestamp);
					System.out.println("SKLJDFLKSJDFLKSJDFLKSDJFSLKDLAPAPPAPAPPAS");
					eventYearList.updateChanges(eventData);
				} catch (Exception e) {
					// Nothing
					System.out.println(e.getMessage());
				}
			}
			scanner.close();
			CalendarWriter.writeCalendar(new File(path), eventYearList, new File(pathChangeLog));
			loadCalendar(0);
		} catch (Exception e) {
			throw new IllegalArgumentException(e.getMessage());
		}
	}

	/**
	 * Returns the year of the loaded calendar
	 * 
	 * @return year the year of calendar
	 */
	public int getYear() {
		return year;
	}

	/**
	 * Creates a brand new event. If an event already exists, throw an exception.
	 * 
	 * @param name      the name of the event
	 * @param startTime the start time
	 * @param endTime   the end time
	 * @param day       the day of the event
	 * @param month     the month of the event
	 * @param year      the year of the event
	 * @param red       the red color
	 * @param blue      the blue color
	 * @param green     the green color
	 * @return newEvent the newly created event
	 */
	public EventData createEvent(String name, String startTime, String endTime, int day, int month, int year, int red,
			int green, int blue, SyncState syncState, String previous) {
		EventData newEvent = new EventData(name, startTime, endTime, day, month, year, red, green, blue, syncState, previous, " ");
		if (year != this.year) {
			throw new IllegalArgumentException("Year does not match current year");
		}
		try {
			eventYearList.add(newEvent, newEvent.getDate(), newEvent.getStartInt());
			saveCalendar();
			return newEvent;
		} catch (Exception e) {
			throw new IllegalArgumentException(e.getMessage());
		}
	}

	/**
	 * If the edited event has the same date, change the event details. If the
	 * edited event date changes, delete the old date and add the new one.
	 * 
	 * @param originalDate  the edited event
	 * @param originalStart the start time
	 * @param name          the name of the event
	 * @param startTime     the start time
	 * @param endTime       the end time
	 * @param day           the day of the event
	 * @param month         the month of the event
	 * @param year          the year of the event
	 * @param red           the red color
	 * @param blue          the blue color
	 * @param green         the green color
	 * @param synced        the synced boolean
	 * @return EventData the event edited
	 */
	public EventData editEvent(double originalDate, int originalStart, String name, String startTime, String endTime,
			int day, int month, int year, int red, int green, int blue, SyncState syncState, String previous) {
		try {
			EventData newEvent = new EventData(name, startTime, endTime, day, month, year, red, green, blue, syncState, previous.replaceAll("@@", "/"), " ");
			if (!eventYearList.checkValue(originalDate, newEvent.getStartInt())
					&& newEvent.getStartInt() != originalStart) {
				throw new IllegalArgumentException("Duplicate Dates when Editing");
			}
			eventYearList.removeD(originalDate, originalStart);
			eventYearList.add(newEvent, newEvent.getDate(), newEvent.getStartInt());
			saveCalendar();
			return newEvent;
		} catch (Exception e) {
			throw new IllegalArgumentException(e.getMessage());
		}
	}

	/**
	 * Removes an event from the list using the original date and starting time
	 * 
	 * @param originalDate  the date of the event
	 * @param originalStart the
	 * @return EventData the event removed
	 */
	public EventData removeEvent(double originalDate, int originalStart) {
		try {
			EventData newEvent = eventYearList.get(originalDate, originalStart);
			eventYearList.removeD(originalDate, originalStart);
			saveCalendar();
			return newEvent;
		} catch (Exception e) {
			throw new IllegalArgumentException(e.getMessage());
		}
	}

	/**
	 * Saves and clears that eventYearList
	 */
	public void clearCalendar() {
		saveCalendar();
		eventYearList = new SortedDateList<EventData>(0, 0);
	}

	/**
	 * Returns the list of all events
	 * 
	 * @return eventYearList the list of all dates
	 */
	public SortedDateList<EventData> getEvents() {
		return eventYearList;
	}

	/**
	 * Saves and clears that eventYearList
	 */
	public void deleteCalendar() {
		try {
			PrintStream fileWriter = new PrintStream(new File(path));
			fileWriter.print("1\n");
			fileWriter.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		loadCalendar(0);
	}

	/**
	 * Returns an array of the settings file information which is used in the UI to
	 * get all settings at runtime
	 * 
	 * @return allSettings an array that contains all settings
	 */
	public String[] defaultSettings() {
		return allSettings;
	}

	/**
	 * Returns the path to the current year
	 * 
	 * @return path the path to the calendar data
	 */
	public String getPath() {
		return path;
	}
}
