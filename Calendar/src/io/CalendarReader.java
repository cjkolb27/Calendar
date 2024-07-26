package io;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import events.EventData;
import util.SortedDateList;

/**
 * Allows for loading all events in a year from a file. Each file contains rows
 * of event names, start time, end time, day, month, year, and RGB coloring for
 * each event.
 * 
 * @author Caleb Kolb
 */
public class CalendarReader {

	/**
	 * Reads a given file and outputs data into a Sorted list
	 * 
	 * @param file the file to read
	 * @return list the list of sorted dates
	 */
	public static SortedDateList<EventData> readCalendar(File file) {
		SortedDateList<EventData> list = new SortedDateList<EventData>();
		try {
			Scanner scanner = new Scanner(file);
			while (scanner.hasNextLine() && scanner.hasNext()) {
				scanner.useDelimiter("@@");
				try {
					String name = scanner.next();
					String startTime = scanner.next();
					String endTime = scanner.next();
					int day = scanner.nextInt();
					int month = scanner.nextInt();
					int year = scanner.nextInt();
					int red = scanner.nextInt();
					int green = scanner.nextInt();
					int blue = scanner.nextInt();
					if (scanner.hasNextLine()) {
						scanner.nextLine();
					}
					EventData eventData = new EventData(name, startTime, endTime, day, month, year, red, green, blue);
					list.add(eventData, eventData.getDate(), eventData.getStartInt());
				} catch (Exception e) {
					// Nothing
				}
			}
			scanner.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return list;
	}
}
