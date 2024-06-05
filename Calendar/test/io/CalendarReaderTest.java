/**
 * 
 */
package io;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;

import org.junit.jupiter.api.Test;

import events.EventData;
import util.SortedDateList;

/**
 * 
 */
class CalendarReaderTest {

	/**
	 * Test method for {@link io.CalendarReader#readCalendar(java.io.File)}.
	 */
	@Test
	void testReadCalendar() {
		SortedDateList<EventData> s = new SortedDateList<EventData>();
		s = CalendarReader.readCalendar(new File("testFiles/calendar1.txt"));
		assertEquals(12, s.size());
		assertEquals("Job", s.getAtIndex(0).getName());
		assertEquals(12, s.getAtIndex(0).getDay());
		assertEquals(13, s.getAtIndex(1).getDay());
		assertEquals(14, s.getAtIndex(2).getDay());
		assertEquals(15, s.getAtIndex(3).getDay());
		assertEquals(17, s.getAtIndex(4).getDay());
		assertEquals(18, s.getAtIndex(5).getDay());
		assertEquals(19, s.getAtIndex(6).getDay());
		assertEquals(20, s.getAtIndex(7).getDay());
		assertEquals(21, s.getAtIndex(8).getDay());
		assertEquals(22, s.getAtIndex(9).getDay());
		assertEquals(23, s.getAtIndex(10).getDay());
		assertEquals(30, s.getAtIndex(11).getDay());
	}

}
