package io;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;

import org.junit.jupiter.api.Test;

import events.EventData;
import events.EventData.SyncState;
import util.SortedDateList;

/**
 * Test class for testing CalendarWriter
 * 
 * @author Caleb Kolb
 */
class CalendarWriterTest {

	/**
	 * Test method for
	 * {@link io.CalendarWriter#writeCalendar(java.io.File, util.SortedDateList)}.
	 */
	@Test
	void testWriteCalendar() {
		SortedDateList<EventData> s = new SortedDateList<EventData>(0, 0);
		EventData e1 = new EventData("Job", "12:30a", "12:35a", 12, 11, 2024, 100, 100, 100, SyncState.Synced, " ", " ");
		EventData e2 = new EventData("Job", "12:35a", "12:36a", 12, 11, 2024, 100, 100, 100, SyncState.Synced, " ", " ");
		EventData e3 = new EventData("Job", "12:29a", "12:30a", 12, 11, 2024, 100, 100, 100, SyncState.Synced, " ", " ");
		EventData e4 = new EventData("Job", "12:30a", "12:35a", 17, 11, 2024, 100, 100, 100, SyncState.Synced, " ", " ");
		EventData e5 = new EventData("Job", "1:00a", "12:35a", 18, 11, 2024, 100, 100, 100, SyncState.NotSynced, " ", " ");
		EventData e6 = new EventData("Job", "12:40a", "12:35a", 21, 11, 2024, 100, 100, 100, SyncState.Synced, " ", " ");
		s.add(e1, e1.getDate(), e1.getStartInt());
		s.add(e2, e2.getDate(), e2.getStartInt());
		s.add(e3, e3.getDate(), e3.getStartInt());
		s.add(e4, e4.getDate(), e4.getStartInt());
		s.add(e5, e5.getDate(), e5.getStartInt());
		s.add(e6, e6.getDate(), e6.getStartInt());

		CalendarWriter.writeCalendar(new File("testFiles/calendartest1.txt"), s, new File("testFiles/calendartest1_changes.txt"));
		SortedDateList<EventData> s2 = CalendarReader.readCalendar(new File("testFiles/calendartest1.txt"), new File("testFiles/calendartest1_changes.txt"));
		assertEquals(e1.getDate(), s2.getAtIndex(0).getDate());
		assertEquals(e2.getDate(), s2.getAtIndex(1).getDate());
		assertEquals(e3.getDate(), s2.getAtIndex(2).getDate());
		assertEquals(e4.getDate(), s2.getAtIndex(3).getDate());
		assertEquals(e5.getDate(), s2.getAtIndex(4).getDate());
		assertEquals(e6.getDate(), s2.getAtIndex(5).getDate());
		
		assertEquals(e3.getStartInt(), s2.getAtIndex(0).getStartInt());
		assertEquals(e1.getStartInt(), s2.getAtIndex(1).getStartInt());
		assertEquals(e2.getStartInt(), s2.getAtIndex(2).getStartInt());
		assertEquals(e4.getStartInt(), s2.getAtIndex(3).getStartInt());
		assertEquals(e5.getStartInt(), s2.getAtIndex(4).getStartInt());
		assertEquals(e6.getStartInt(), s2.getAtIndex(5).getStartInt());

		CalendarWriter.writeCalendar(new File("testFiles/calendartest1.txt"), s2, new File("testFiles/calendartest1_changes.txt"));
		s2 = CalendarReader.readCalendar(new File("testFiles/calendartest1.txt"), new File("testFiles/calendartest1_changes.txt"));
		assertEquals(e1.getDate(), s2.getAtIndex(0).getDate());
		assertEquals(e2.getDate(), s2.getAtIndex(1).getDate());
		assertEquals(e3.getDate(), s2.getAtIndex(2).getDate());
		assertEquals(e4.getDate(), s2.getAtIndex(3).getDate());
		assertEquals(e5.getDate(), s2.getAtIndex(4).getDate());
		assertEquals(e6.getDate(), s2.getAtIndex(5).getDate());
	}
	
	@Test
	void testWriteSyncedCalendar() {
		SortedDateList<EventData> s = new SortedDateList<EventData>(0, 0);
		s = CalendarReader.readCalendar(new File("testFiles/year1.txt"), new File("testFiles/year1_changes.txt"));
		CalendarWriter.writeSyncedCalendar(new File("testFiles/year2.txt"), s, new File("testFiles/year2_changes.txt"));
		assertEquals(151, s.size());
	}

}
