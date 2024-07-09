package events;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

/**
 * Class for testing EventData class
 * 
 * @author Caleb Kolb
 */
class EventDataTest {

	/**
	 * Test method for
	 * {@link events.EventData#EventData(java.lang.String, java.lang.String, java.lang.String, int, int, int)}.
	 */
	@Test
	void testEventData() {
		EventData e = new EventData("Job", "1215a", "415p", 12, 12, 30, 100, 100, 100);
		assertEquals("Job", e.getName());
		assertEquals("12:15am", e.getStartTime());
		assertEquals("4:15pm", e.getEndTime());
		assertEquals(12, e.getDay());
		assertEquals(12, e.getMonth());
		assertEquals(30, e.getYear());
	}

	/**
	 * Test method for {@link events.EventData#getName()}.
	 */
	@Test
	void testGetName() {
		EventData e = new EventData("Job", "1215a", "415p", 12, 12, 30, 100, 100, 100);
		assertEquals("Job", e.getName());
		assertThrows(IllegalArgumentException.class,
				() -> new EventData("", "1215am", "415pm", 12, 12, 30, 100, 100, 100));
		assertThrows(IllegalArgumentException.class,
				() -> new EventData(null, "1215a", "415p", 12, 12, 30, 100, 100, 100));
	}

	/**
	 * Test method for {@link events.EventData#getStartTime()}.
	 */
	@Test
	void testGetStartTime() {
		EventData e = new EventData("Job", "1215a", "415pm", 12, 12, 30, 100, 100, 100);
		assertEquals("12:15am", e.getStartTime());
		assertThrows(IllegalArgumentException.class, () -> new EventData("Job", "", "415p", 12, 12, 30, 100, 100, 100));
		assertThrows(IllegalArgumentException.class,
				() -> new EventData("Job", null, "415p", 12, 12, 30, 100, 100, 100));
		assertEquals(15, e.getStartInt());
	}

	/**
	 * Test method for {@link events.EventData#getEndTime()}.
	 */
	@Test
	void testGetEndTime() {
		EventData e = new EventData("Job", "1215am", "415pm", 12, 12, 30, 100, 100, 100);
		assertEquals("4:15pm", e.getEndTime());
		assertThrows(IllegalArgumentException.class, () -> new EventData("Job", "415p", "", 12, 12, 30, 100, 100, 100));
		assertThrows(IllegalArgumentException.class,
				() -> new EventData("Job", "415p", null, 12, 12, 30, 100, 100, 100));
		assertEquals(1615, e.getEndInt());
	}

	/**
	 * Test method for {@link events.EventData#getDay()}.
	 */
	@Test
	void testGetDay() {
		EventData e = new EventData("Job", "1215a", "415p", 12, 12, 30, 100, 100, 100);
		assertEquals(12, e.getDay());
		assertThrows(IllegalArgumentException.class,
				() -> new EventData("Job", "415p", "415p", 32, 12, 30, 100, 100, 100));
		assertThrows(IllegalArgumentException.class,
				() -> new EventData("Job", "415p", "415p", 0, 12, 30, 100, 100, 100));

		EventData e2 = new EventData("Job", "1215a", "415p", 12, 11, 30, 100, 100, 100);
		assertEquals(12, e2.getDay());

		EventData e3 = new EventData("Job", "1215a", "415p", 12, 10, 30, 100, 100, 100);
		assertEquals(12, e3.getDay());

		EventData e4 = new EventData("Job", "1215a", "415p", 12, 9, 30, 100, 100, 100);
		assertEquals(12, e4.getDay());

		EventData e5 = new EventData("Job", "1215a", "415p", 12, 8, 30, 100, 100, 100);
		assertEquals(12, e5.getDay());

		EventData e6 = new EventData("Job", "1215a", "415p", 12, 7, 30, 100, 100, 100);
		assertEquals(12, e6.getDay());

		EventData e7 = new EventData("Job", "1215a", "415p", 12, 6, 30, 100, 100, 100);
		assertEquals(12, e7.getDay());

		EventData e8 = new EventData("Job", "1215a", "415p", 12, 5, 30, 100, 100, 100);
		assertEquals(12, e8.getDay());

		EventData e9 = new EventData("Job", "1215a", "415p", 12, 4, 30, 100, 100, 100);
		assertEquals(12, e9.getDay());
		assertThrows(IllegalArgumentException.class,
				() -> new EventData("Job", "415p", "415p", 32, 4, 30, 100, 100, 100));

		EventData e10 = new EventData("Job", "1215a", "415p", 12, 3, 30, 100, 100, 100);
		assertEquals(12, e10.getDay());

		EventData e11 = new EventData("Job", "1215a", "415p", 12, 2, 30, 100, 100, 100);
		assertEquals(12, e11.getDay());
		assertThrows(IllegalArgumentException.class,
				() -> new EventData("Job", "415p", "415p", 32, 2, 30, 100, 100, 100));

		EventData e12 = new EventData("Job", "1215a", "415p", 12, 1, 30, 100, 100, 100);
		assertEquals(12, e12.getDay());
	}

	/**
	 * Test method for {@link events.EventData#getMonth()}.
	 */
	@Test
	void testGetMonth() {
		EventData e = new EventData("Job", "1215a", "415p", 12, 12, 30, 100, 100, 100);
		assertEquals(12, e.getMonth());
		assertThrows(IllegalArgumentException.class,
				() -> new EventData("Job", "415p", "415p", 12, 13, 30, 100, 100, 100));
		assertThrows(IllegalArgumentException.class,
				() -> new EventData("Job", "415p", "415p", 12, 0, 30, 100, 100, 100));
	}

	/**
	 * Test method for {@link events.EventData#getYear()}.
	 */
	@Test
	void testGetYear() {
		EventData e = new EventData("Job", "1215a", "415p", 12, 12, 30, 100, 100, 100);
		assertEquals(30, e.getYear());
	}

	/**
	 * Test method for {@link events.EventData#getDate()}.
	 */
	@Test
	void testGetDate() {
		EventData e1 = new EventData("Job", "1215a", "415p", 12, 12, 2023, 100, 100, 100);
		EventData e2 = new EventData("Job", "1215a", "415p", 13, 12, 2024, 100, 100, 100);
		EventData e3 = new EventData("Job", "1215a", "415p", 12, 11, 2023, 100, 100, 100);
		EventData e4 = new EventData("Job", "1215a", "415p", 11, 12, 2023, 100, 100, 100);
		assertEquals(2023.384, e1.getDate());
		assertTrue(e1.getDate() < e2.getDate());
		assertTrue(e1.getDate() > e3.getDate());
		assertTrue(e1.getDate() > e4.getDate());
	}

	/**
	 * Test method for
	 * {@link events.EventData#editData(java.lang.String, java.lang.String, java.lang.String, int, int, int)}.
	 */
	@Test
	void testEditData() {
		EventData e = new EventData("Job", "1215a", "415p", 12, 12, 30, 100, 100, 100);
		EventData e1 = new EventData("Job", "1215a", "415p", 12, 12, 2023, 100, 100, 100);
		assertTrue(e.getDate() < e1.getDate());
		e.editData("Job", "12:15a", "4:15p", 12, 12, 30, 100, 100, 100);
		assertEquals("Job", e.getName());
		assertEquals("12:15am", e.getStartTime());
		assertEquals("4:15pm", e.getEndTime());
		assertEquals(12, e.getDay());
		assertEquals(12, e.getMonth());
		assertEquals(30, e.getYear());
		e.editData("Job", "12:15a", "4:15p", 13, 12, 2023, 100, 100, 100);
		assertTrue(e.getDate() > e1.getDate());

	}
}
