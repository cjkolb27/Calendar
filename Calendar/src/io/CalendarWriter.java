package io;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;

import events.EventData;
import util.SortedDateList;

/**
 * Used for writing events to a file. Each file contains an entire year of
 * events.
 * 
 * @author Caleb Kolb
 */
public class CalendarWriter {

	/**
	 * Write the calendar to a file that is saved in the users computer. The file
	 * will be located at C:\Users\"user"\Documents
	 * 
	 * @param file        the file to write to
	 * @param list        the list of all events
	 * @param changedFile the change file to write to
	 */
	public static void writeCalendar(File file, SortedDateList<EventData> list, File changedFile) {
		int changeCount = 0;
		SortedDateList<EventData> changeList = new SortedDateList<EventData>(0);
		try {
			PrintStream fileWriter = new PrintStream(file);
			fileWriter.println(list.getVersion());
			for (int i = 0; i < list.size(); i++) {
				if (list.getAtIndex(i).getSynced()) {
					fileWriter.print(list.getAtIndex(i).getName() + "@@"
							+ list.getAtIndex(i).getStartTime().substring(0,
									list.getAtIndex(i).getStartTime().length() - 1)
							+ "@@"
							+ list.getAtIndex(i).getEndTime().substring(0, list.getAtIndex(i).getEndTime().length() - 1)
							+ "@@" + list.getAtIndex(i).getDay() + "@@" + list.getAtIndex(i).getMonth() + "@@"
							+ list.getAtIndex(i).getYear() + "@@" + list.getAtIndex(i).getColor().getRed() + "@@"
							+ list.getAtIndex(i).getColor().getGreen() + "@@" + list.getAtIndex(i).getColor().getBlue()
							+ "@@\n");
				} else {
					changeCount++;
					changeList.add(list.getAtIndex(i), list.getAtIndex(i).getDate(), list.getAtIndex(i).getStartInt());
				}
			}
			fileWriter.close();
			PrintStream fileWrite = new PrintStream(changedFile);
			fileWrite.println(changeCount);
			for (int i = 0; i < changeList.size(); i++) {
				fileWrite.print(changeList.getAtIndex(i).getName() + "@@" + changeList
						.getAtIndex(i).getStartTime().substring(0, changeList.getAtIndex(i).getStartTime().length() - 1)
						+ "@@"
						+ changeList.getAtIndex(i).getEndTime().substring(0,
								changeList.getAtIndex(i).getEndTime().length() - 1)
						+ "@@" + changeList.getAtIndex(i).getDay() + "@@" + changeList.getAtIndex(i).getMonth() + "@@"
						+ changeList.getAtIndex(i).getYear() + "@@" + changeList.getAtIndex(i).getColor().getRed()
						+ "@@" + changeList.getAtIndex(i).getColor().getGreen() + "@@"
						+ changeList.getAtIndex(i).getColor().getBlue() + "@@\n");
			}
			fileWrite.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	public static void writeSyncedCalendar(File file, SortedDateList<EventData> list, File changedFile) {
		try {
			PrintStream fileWriter = new PrintStream(file);
			fileWriter.println(list.getVersion());
			for (int i = 0; i < list.size(); i++) {
				fileWriter.print(list.getAtIndex(i).getName() + "@@"
						+ list.getAtIndex(i).getStartTime().substring(0, list.getAtIndex(i).getStartTime().length() - 1)
						+ "@@"
						+ list.getAtIndex(i).getEndTime().substring(0, list.getAtIndex(i).getEndTime().length() - 1)
						+ "@@" + list.getAtIndex(i).getDay() + "@@" + list.getAtIndex(i).getMonth() + "@@"
						+ list.getAtIndex(i).getYear() + "@@" + list.getAtIndex(i).getColor().getRed() + "@@"
						+ list.getAtIndex(i).getColor().getGreen() + "@@" + list.getAtIndex(i).getColor().getBlue()
						+ "@@\n");
			}
			fileWriter.close();
			PrintStream fileWrite = new PrintStream(changedFile);
			fileWrite.println("0");
			fileWrite.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	public static void writeNonSyncedCalendar(File changedFile, SortedDateList<EventData> list) {
		try {
			int changeCount = 0;
			StringBuffer buff = new StringBuffer();
			PrintStream fileWriter = new PrintStream(changedFile);
			fileWriter.println(changeCount);
			for (int i = 0; i < list.size(); i++) {
				if (!list.getAtIndex(i).getSynced()) {
					changeCount++;
					buff.append(list.getAtIndex(i).getName() + "@@"
							+ list.getAtIndex(i).getStartTime().substring(0,
									list.getAtIndex(i).getStartTime().length() - 1)
							+ "@@"
							+ list.getAtIndex(i).getEndTime().substring(0, list.getAtIndex(i).getEndTime().length() - 1)
							+ "@@" + list.getAtIndex(i).getDay() + "@@" + list.getAtIndex(i).getMonth() + "@@"
							+ list.getAtIndex(i).getYear() + "@@" + list.getAtIndex(i).getColor().getRed() + "@@"
							+ list.getAtIndex(i).getColor().getGreen() + "@@" + list.getAtIndex(i).getColor().getBlue()
							+ "@@\n");
				}
			}
			fileWriter.println(changeCount);
			fileWriter.print(buff.toString());
			fileWriter.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
}
