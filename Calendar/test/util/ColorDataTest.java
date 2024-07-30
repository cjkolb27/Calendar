/**
 * 
 */
package util;

import static org.junit.jupiter.api.Assertions.*;

import java.awt.Color;

import org.junit.jupiter.api.Test;

/**
 * 
 */
class ColorDataTest {

	/**
	 * Test method for
	 * {@link util.ColorData#ColorData(java.lang.String, java.lang.String)}.
	 */
	@Test
	void testColorData() {
		ColorData cd = new ColorData("mtf", "testFiles/colors.txt");
		assertNotNull(cd);
		cd.printColors();
		cd.moveToFront(new Color(3, 4, 6));
		cd.printColors();

		cd.moveToFront(new Color(0, 0, 0));
		cd.printColors();

		cd.moveToFront(new Color(255, 255, 255));
		cd.printColors();
		
		cd.moveToFront(new Color(255, 255, 255));
		cd.printColors();
		
		cd.moveToFront(new Color(255, 255, 255));
		cd.moveToFront(new Color(0, 0, 0));
		cd.moveToFront(new Color(230, 100, 140));
		cd.moveToFront(new Color(255, 153, 161));
		cd.printColors();
	}

	/**
	 * Test method for {@link util.ColorData#addColor(java.awt.Color)}.
	 */
	@Test
	void testAddColor() {
		ColorData cd = new ColorData("mtf", "testFiles/colors.txt");
		cd.addColor(new Color(1, 1, 1));
		assertEquals(5, cd.getSize());
		assertEquals(new Color(1, 1, 1), cd.getColors()[0]);
		cd.removeColor(new Color(1, 1, 1));
		cd.addColor(new Color(1, 1, 1));
		cd.removeColor(new Color(1, 1, 1));
		assertEquals(new Color(255, 153, 161), cd.getColors()[0]);
	}

	/**
	 * Test method for {@link util.ColorData#removeColor(java.awt.Color)}.
	 */
	@Test
	void testRemoveColor() {
		ColorData cd = new ColorData("mtf", "testFiles/colors.txt");
		cd.removeColor(new Color(255, 153, 161));
		assertEquals(3, cd.getSize());
		assertEquals(new Color(230, 100, 140), cd.getColors()[0]);
		cd.addColor(new Color(255, 153, 161));
	}
}
