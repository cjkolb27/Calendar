package util;

import static org.junit.jupiter.api.Assertions.*;

import java.awt.Color;

import org.junit.jupiter.api.Test;

/**
 * Test class for testing PresetData
 * 
 * @author Caleb Kolb
 */
class PresetDataTest {

	/**
	 * Test method for {@link util.PresetData#PresetData(java.lang.String)}.
	 */
	@Test
	void testPresetData() {
		PresetData pd = new PresetData("testFiles/Presets.txt");
		assertNotNull(pd);
		assertEquals(2, pd.getSize());
	}

	/**
	 * Test method for
	 * {@link util.PresetData#addPreset(java.lang.String, java.lang.String, java.lang.String, java.awt.Color)}.
	 */
	@Test
	void testAddPreset() {
		PresetData pd = new PresetData("testFiles/Presets.txt");
		pd.addPreset("School", "10:15am", "5:30pm", new Color(200, 200, 40));
		assertEquals(3, pd.getSize());
	}

	/**
	 * Test method for
	 * {@link util.PresetData#removePreset(java.lang.String, java.lang.String, java.lang.String, java.awt.Color)}.
	 */
	@Test
	void testRemovePreset() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link util.PresetData#getPresets()}.
	 */
	@Test
	void testGetPresets() {
		fail("Not yet implemented");
	}
}
