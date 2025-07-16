/**
 * 
 */
package manager;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import events.EventData.SyncState;

/**
 * CalendarManager test class
 */
class CalendarManagerTest {

	/**
	 * Test method for {@link manager.CalendarManager#loadCalendar(int)}.
	 */
	@Test
	void testCalendarManager() {
		CalendarManager c = new CalendarManager(1800);
		c.deleteCalendar();
		c.createEvent("Job", "12:20p", "1:20p", 3, 5, 1800, 100, 100, 100, SyncState.Synced, " ");
		c.createEvent("Job", "12:20p", "1:20p", 2, 5, 1800, 100, 100, 100, SyncState.Synced, " ");
		c.createEvent("Job", "12:20p", "1:20p", 1, 5, 1800, 100, 100, 100, SyncState.Synced, " ");
		c.createEvent("Job", "12:20p", "1:20p", 3, 6, 1800, 100, 100, 100, SyncState.Synced, " ");

		c.clearCalendar();
		c.loadCalendar(0);
		c.saveCalendar();

		assertEquals(1, c.getEvents().getAtIndex(0).getDay());
		assertEquals(2, c.getEvents().getAtIndex(1).getDay());
		assertEquals(3, c.getEvents().getAtIndex(2).getDay());
		assertEquals(3, c.getEvents().getAtIndex(3).getDay());

		c.editEvent(c.getEvents().getAtIndex(3).getDate(), c.getEvents().getAtIndex(3).getStartInt(), "Job", "12:20p",
				"1:25p", 4, 6, 1800, 100, 100, 100, SyncState.Synced, " ");
		assertEquals(4, c.getEvents().getAtIndex(3).getDay());

		c.createEvent("Job", "12:20p", "1:20p", 5, 5, 1800, 100, 100, 100, SyncState.Synced, " ");
		c.createEvent("Job", "12:20p", "1:20p", 6, 5, 1800, 100, 100, 100, SyncState.Synced, " ");
		assertEquals(1, c.getEvents().getAtIndex(0).getDay());
		assertEquals(2, c.getEvents().getAtIndex(1).getDay());
		assertEquals(3, c.getEvents().getAtIndex(2).getDay());
		assertEquals(5, c.getEvents().getAtIndex(3).getDay());
		assertEquals(6, c.getEvents().getAtIndex(4).getDay());
		assertEquals(4, c.getEvents().getAtIndex(5).getDay());

		// c = new CalendarManager();
		// c.saveSettings(c.defaultSettings()[0], c.defaultSettings()[1]);

		// CalendarManager c1 = new CalendarManager();
		// assertEquals(2024, c1.getYear());
		// c1.getEvents().iterator();
	}
}
