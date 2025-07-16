package io;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import events.EventData;
import events.EventData.SyncState;
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
	 * @param changeFile the change log file to read
	 * @return list the list of sorted dates
	 */
	public static SortedDateList<EventData> readCalendar(File file, File changeFile) {
		SortedDateList<EventData> list = new SortedDateList<EventData>(0);
		try {
			Scanner scanner = new Scanner(file);
			list.setVersion(scanner.nextInt());
			scanner.nextLine();
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
					String timestamp = scanner.next();
					if (scanner.hasNextLine()) {
						scanner.nextLine();
					}
					EventData eventData = new EventData(name, startTime, endTime, day, month, year, red, green, blue, SyncState.Synced, " ", timestamp);
					list.add(eventData, eventData.getDate(), eventData.getStartInt());
				} catch (Exception e) {
					// Nothing
					System.out.println(e.getMessage());
				}
			}
			scanner.close();
			Scanner scanner2 = new Scanner(changeFile);
			scanner2.nextLine();
			while (scanner2.hasNextLine() && scanner2.hasNext()) {
				scanner2.useDelimiter("@@");
				try {
					SyncState syncState = SyncState.valueOf(scanner2.next());
					String previous = scanner2.next();
					String name = scanner2.next();
					String startTime = scanner2.next();
					String endTime = scanner2.next();
					int day = scanner2.nextInt();
					int month = scanner2.nextInt();
					int year = scanner2.nextInt();
					int red = scanner2.nextInt();
					int green = scanner2.nextInt();
					int blue = scanner2.nextInt();
					String timestamp = scanner2.next();
					if (scanner2.hasNextLine()) {
						scanner2.nextLine();
					}
					EventData eventData = new EventData(name, startTime, endTime, day, month, year, red, green, blue, syncState, previous, timestamp);
					if (syncState == SyncState.Deleted || syncState == SyncState.Edited) {
						Scanner scanner3 = new Scanner(previous);
						scanner3.useDelimiter("/");
						SyncState syncState2 = SyncState.valueOf(scanner3.next());
						String previous2 = scanner3.next();
						String name2 = scanner3.next();
						String startTime2 = scanner3.next();
						String endTime2 = scanner3.next();
						int theDay2 = scanner3.nextInt();
						int theMonth2 = scanner3.nextInt();
						int theYear2 = scanner3.nextInt();
						int red2 = scanner3.nextInt();
						int green2 = scanner3.nextInt();
						int blue2 = scanner3.nextInt();
						String timestamp2 = scanner3.next();
						scanner3.close();
						EventData eventData2 = new EventData(name2, startTime2, endTime2, theDay2, theMonth2, theYear2, red2, green2, blue2, syncState2, previous2, timestamp2);
						list.removeD(eventData2.getDate(), eventData2.getStartInt());
					}
					list.add(eventData, eventData.getDate(), eventData.getStartInt());
				} catch (Exception e) {
					// Nothing
					System.out.println(e.getMessage());
				}
			}
			scanner2.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		list.printToCMD();
		return list;
	}
}
