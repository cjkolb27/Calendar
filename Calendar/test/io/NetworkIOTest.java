/**
 * 
 */
package io;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;

import org.junit.jupiter.api.Test;

/**
 * Test class for testing NetworkIO
 */
class NetworkIOTest {

	@Test
	void testReadNetworkIO() {
		String[] hostport = NetworkIO.readNetworkIO(new File(System.getProperty("user.home") + File.separator + "Documents"
				+ File.separator + "CalendarData" + File.separator + "NetworkIO.txt"));
		assertEquals(hostport[0].charAt(0), 'c');
		assertEquals(hostport[1], "2727");
	}

}
