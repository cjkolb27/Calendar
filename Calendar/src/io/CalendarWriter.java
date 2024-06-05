package io;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;

import events.EventData;
import util.SortedDateList;

/**
 * 
 */
public class CalendarWriter {

	/**
	 * Write the calendar to a file that is saved in the users computer. The file
	 * will be located at C:\Users\"user"\Documents
	 * 
	 * @param file the file to write to
	 * @param list the list of all events
	 */
	public static void writeCalendar(File file, SortedDateList<EventData> list) {
		try {
			PrintStream fileWriter = new PrintStream(file);
			for (int i = 0; i < list.size(); i++) {
				fileWriter.print(list.getAtIndex(i).getName() + "@@"
						+ list.getAtIndex(i).getStartTime().substring(0, list.getAtIndex(i).getStartTime().length() - 1)
						+ "@@"
						+ list.getAtIndex(i).getEndTime().substring(0, list.getAtIndex(i).getEndTime().length() - 1)
						+ "@@" + list.getAtIndex(i).getDay() + "@@" + list.getAtIndex(i).getMonth() + "@@"
						+ list.getAtIndex(i).getYear() + "@@\n");
			}
			fileWriter.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
}
