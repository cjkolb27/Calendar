package util;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Iterator;

import org.junit.jupiter.api.Test;

import events.EventData;
import manager.CalendarManager;

/**
 * Test class for testing SortedDateList
 * 
 * @author Caleb Kolb
 */
class SortedDateListTest {

	/**
	 * Tests sortedDateList method
	 */
	@Test
	void testSortedDateList() {
		SortedDateList<EventData> s = new SortedDateList<EventData>();
		assertEquals(0, s.size());
		EventData e = new EventData("Job", "1215a", "415p", 12, 12, 2024);
		s.add(e, e.getDate(), e.getStartInt());
		assertEquals(1, s.size());
		assertEquals(e, s.get(2024.384, 15));
	}

	/**
	 * Tests add method
	 */
	@Test
	void testAdd() {
		SortedDateList<EventData> s = new SortedDateList<EventData>();
		assertThrows(NullPointerException.class, () -> s.add(null, 2.2, 0));
		EventData e = new EventData("Job", "1215a", "415p", 12, 12, 2024);
		EventData e2 = new EventData("Job", "1215a", "415p", 12, 12, 2024);
		s.add(e, e.getDate(), e.getStartInt());
		s.add(e2, e2.getDate(), e2.getStartInt());
		assertEquals(2, s.size());

		SortedDateList<EventData> s2 = new SortedDateList<EventData>();
		EventData e3 = new EventData("Job", "1215a", "415p", 13, 12, 2024);
		EventData e4 = new EventData("Job", "1215a", "415p", 12, 12, 2024);
		s2.add(e3, e3.getDate(), e3.getStartInt());
		s2.add(e4, e4.getDate(), e4.getStartInt());
		assertEquals(2, s2.size());
	}

	/**
	 * Tests get method
	 */
	@Test
	void testGet() {
		SortedDateList<EventData> s = new SortedDateList<EventData>();
		EventData e = new EventData("Job", "1215a", "415p", 12, 12, 2024);
		EventData e2 = new EventData("Job", "1215a", "415p", 13, 12, 2024);
		EventData e3 = new EventData("Job", "1215a", "415p", 14, 12, 2024);
		s.add(e, e.getDate(), e.getStartInt());
		assertEquals(e, s.get(e.getDate(), e.getStartInt()));

		s.add(e2, e2.getDate(), e2.getStartInt());
		assertEquals(e, s.get(e.getDate(), e.getStartInt()));
		assertEquals(e2, s.get(e2.getDate(), e2.getStartInt()));

		s.add(e3, e3.getDate(), e3.getStartInt());
		assertEquals(e, s.get(e.getDate(), e.getStartInt()));
		assertEquals(e2, s.get(e2.getDate(), e2.getStartInt()));
		assertEquals(e3, s.get(e3.getDate(), e3.getStartInt()));

		assertEquals(null, s.get(2.2, e.getStartInt()));
	}

	/**
	 * Tests getAtIndex method
	 */
	@Test
	void testGetAtIndex() {
		SortedDateList<EventData> s = new SortedDateList<EventData>();
		EventData e = new EventData("Job", "1215a", "415p", 12, 12, 2024);
		EventData e2 = new EventData("Job", "1215a", "415p", 13, 12, 2024);
		EventData e3 = new EventData("Job", "1215a", "415p", 14, 12, 2024);
		s.add(e, e.getDate(), e.getStartInt());
		assertEquals(e, s.getAtIndex(0));

		s.add(e2, e2.getDate(), e2.getStartInt());
		assertEquals(e, s.getAtIndex(0));
		assertEquals(e2, s.getAtIndex(1));

		s.add(e3, e3.getDate(), e3.getStartInt());
		assertEquals(e, s.getAtIndex(0));
		assertEquals(e2, s.getAtIndex(1));
		assertEquals(e3, s.getAtIndex(2));
	}

	/**
	 * Tests removeE method
	 */
	@Test
	void testRemoveE() {
		SortedDateList<EventData> s = new SortedDateList<EventData>();
		assertThrows(NullPointerException.class, () -> s.removeE(null, 0));

		EventData e = new EventData("Job", "1215a", "415p", 12, 12, 2024);
		assertThrows(IllegalArgumentException.class, () -> s.removeE(e, e.getStartInt()));
		assertEquals(0, s.size());

		s.add(e, e.getDate(), e.getStartInt());
		s.removeE(e, e.getStartInt());
		assertEquals(0, s.size());

		EventData e2 = new EventData("Job", "1215a", "415p", 13, 12, 2024);
		s.add(e, e.getDate(), e.getStartInt());
		s.add(e2, e2.getDate(), e2.getStartInt());
		s.removeE(e, e.getStartInt());
		assertEquals(1, s.size());

		SortedDateList<EventData> s2 = new SortedDateList<EventData>();
		EventData e3 = new EventData("Job", "1215a", "415p", 14, 12, 2024);
		s2.add(e, e.getDate(), e.getStartInt());
		s2.add(e2, e2.getDate(), e2.getStartInt());
		s2.add(e3, e3.getDate(), e3.getStartInt());
		s2.removeE(e, e.getStartInt());
		assertEquals(2, s2.size());

		SortedDateList<EventData> s3 = new SortedDateList<EventData>();
		EventData e4 = new EventData("Job", "1215a", "415p", 9, 12, 2024);
		s3.add(e, e.getDate(), e.getStartInt());
		s3.add(e2, e2.getDate(), e2.getStartInt());
		s3.add(e3, e3.getDate(), e3.getStartInt());
		s3.removeE(e2, e2.getStartInt());
		assertEquals(2, s3.size());
		assertEquals(e, s3.get(e.getDate(), e.getStartInt()));
		assertEquals(e3, s3.get(e3.getDate(), e3.getStartInt()));

		assertThrows(IllegalArgumentException.class, () -> s.removeE(e4, e4.getStartInt()));

		EventData e5 = new EventData("Job", "1215a", "415p", 8, 12, 2024);
		EventData e6 = new EventData("Job", "1215a", "415p", 15, 12, 2024);
		EventData e7 = new EventData("Job", "1215a", "415p", 19, 12, 2024);
		s3.add(e7, e7.getDate(), e7.getStartInt());
		s3.add(e5, e5.getDate(), e5.getStartInt());
		s3.add(e6, e6.getDate(), e6.getStartInt());
		assertEquals(e, s3.get(e.getDate(), e.getStartInt()));
		assertEquals(e3, s3.get(e3.getDate(), e3.getStartInt()));
		assertEquals(e5, s3.get(e5.getDate(), e5.getStartInt()));
		assertEquals(e6, s3.get(e6.getDate(), e6.getStartInt()));
		assertEquals(e7, s3.get(e7.getDate(), e7.getStartInt()));

		s3.removeE(e7, e7.getStartInt());
		assertThrows(IllegalArgumentException.class, () -> s.removeE(e7, e7.getStartInt()));
		assertEquals(null, s3.get(e7.getDate(), e7.getStartInt()));
	}

	/**
	 * Tests removeD method
	 */
	@Test
	void testRemoveD() {
		SortedDateList<EventData> s = new SortedDateList<EventData>();
		EventData e = new EventData("Job", "1215a", "415p", 12, 12, 2024);
		assertThrows(IllegalArgumentException.class, () -> s.removeD(2024.384, e.getStartInt()));
		assertEquals(0, s.size());

		s.add(e, e.getDate(), e.getStartInt());
		s.removeD(e.getDate(), e.getStartInt());
		assertEquals(0, s.size());

		EventData e2 = new EventData("Job", "1215a", "415p", 13, 12, 2024);
		s.add(e, e.getDate(), e.getStartInt());
		s.add(e2, e2.getDate(), e2.getStartInt());
		s.removeD(e.getDate(), e.getStartInt());
		assertEquals(1, s.size());

		SortedDateList<EventData> s2 = new SortedDateList<EventData>();
		EventData e3 = new EventData("Job", "1215a", "415p", 14, 12, 2024);
		s2.add(e, e.getDate(), e.getStartInt());
		s2.add(e2, e2.getDate(), e2.getStartInt());
		s2.add(e3, e3.getDate(), e3.getStartInt());
		s2.removeD(e.getDate(), e.getStartInt());
		assertEquals(2, s2.size());

		SortedDateList<EventData> s3 = new SortedDateList<EventData>();
		EventData e4 = new EventData("Job", "1215a", "415p", 9, 12, 2024);
		s3.add(e, e.getDate(), e.getStartInt());
		s3.add(e2, e2.getDate(), e2.getStartInt());
		s3.add(e3, e3.getDate(), e3.getStartInt());
		s3.removeD(e2.getDate(), e2.getStartInt());
		assertEquals(2, s3.size());
		assertEquals(e, s3.get(e.getDate(), e.getStartInt()));
		assertEquals(e3, s3.get(e3.getDate(), e3.getStartInt()));

		assertThrows(IllegalArgumentException.class, () -> s.removeD(e4.getDate(), e4.getStartInt()));

		EventData e5 = new EventData("Job", "1215a", "415p", 8, 12, 2024);
		EventData e6 = new EventData("Job", "1215a", "415p", 15, 12, 2024);
		EventData e7 = new EventData("Job", "1215a", "415p", 19, 12, 2024);
		s3.add(e7, e7.getDate(), e7.getStartInt());
		s3.add(e5, e5.getDate(), e5.getStartInt());
		s3.add(e6, e6.getDate(), e6.getStartInt());
		assertEquals(e, s3.get(e.getDate(), e.getStartInt()));
		assertEquals(e3, s3.get(e3.getDate(), e3.getStartInt()));
		assertEquals(e5, s3.get(e5.getDate(), e5.getStartInt()));
		assertEquals(e6, s3.get(e6.getDate(), e6.getStartInt()));
		assertEquals(e7, s3.get(e7.getDate(), e7.getStartInt()));

		s3.removeD(e7.getDate(), e7.getStartInt());
		assertThrows(IllegalArgumentException.class, () -> s.removeD(e7.getDate(), e7.getStartInt()));
		assertEquals(null, s3.get(e7.getDate(), e7.getStartInt()));
	}

	/**
	 * Tests size method
	 */
	@Test
	void testSize() {
		SortedDateList<EventData> s = new SortedDateList<EventData>();
		assertEquals(0, s.size());
	}
	
	/**
	 * Tests size method
	 */
	@Test
	void testIterator() {
		SortedDateList<EventData> sdl = new SortedDateList<EventData>();
		EventData e = new EventData("Job", "1215a", "415p", 12, 12, 2024);
		sdl.add(e, e.getDate(), e.getStartInt());
		Iterator<EventData> it = sdl.iterator();
		assertTrue(sdl.iterator().hasNext());
		assertEquals("Job", it.next().getName());
	}
}
