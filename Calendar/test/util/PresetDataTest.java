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
		pd.addPreset("Gym", "1:00am", "5:30am", new Color(200, 200, 40));
		assertEquals(4, pd.getSize());
		pd.addPreset("Date<3", "7:00pm", "8:30pm", new Color(200, 200, 40));
		assertEquals(5, pd.getSize());
		pd.addPreset("eDate<3", "7:00pm", "8:30pm", new Color(200, 200, 40));
		pd.addPreset("cDate<3", "7:00pm", "8:30pm", new Color(200, 200, 40));
		assertEquals(7, pd.getSize());
		pd.addPreset("bGym", "1:00am", "5:31am", new Color(200, 200, 40));
		pd.addPreset("Gym", "1:00am", "5:31am", new Color(200, 200, 40));
		assertEquals(9, pd.getSize());

		assertThrows(IllegalArgumentException.class,
				() -> pd.addPreset("Gym", "1:00am", "5:31am", new Color(200, 200, 40)));

		pd.removePreset("School", "10:15am", "5:30pm");
		pd.removePreset("Gym", "1:00am", "5:30am");
		pd.removePreset("Date<3", "7:00pm", "8:30pm");
		pd.removePreset("eDate<3", "7:00pm", "8:30pm");
		pd.removePreset("cDate<3", "7:00pm", "8:30pm");
		pd.removePreset("bGym", "1:00am", "5:31am");
		pd.removePreset("Gym", "1:00am", "5:31am");
		assertEquals(2, pd.getSize());
	}

	/**
	 * Test method for
	 * {@link util.PresetData#removePreset(java.lang.String, java.lang.String, java.lang.String, java.awt.Color)}.
	 */
	@Test
	void testRemovePreset() {
		PresetData pd = new PresetData("testFiles/Presets.txt");
		pd.addPreset("School", "1:00pm", "2:00pm", new Color(100, 100, 100));
		pd.addPreset("School", "1:01pm", "2:00pm", new Color(100, 100, 100));
		pd.addPreset("School", "1:02pm", "2:00pm", new Color(100, 100, 100));
		pd.addPreset("School", "1:03pm", "2:00pm", new Color(100, 100, 100));
		pd.addPreset("School", "1:04pm", "2:00pm", new Color(100, 100, 100));
		assertEquals(7, pd.getSize());

		pd.removePreset("Work", "6:30am", "1:00pm");
		assertEquals(6, pd.getSize());

		pd.removePreset("School", "1:04pm", "2:00pm");
		assertEquals(5, pd.getSize());

		pd.removePreset("School", "1:00pm", "2:00pm");
		assertEquals(4, pd.getSize());

		pd.removePreset("School", "1:03pm", "2:00pm");
		assertEquals(3, pd.getSize());

		pd.removePreset("School", "1:01pm", "2:00pm");
		assertEquals(2, pd.getSize());

		pd.removePreset("School", "1:02pm", "2:00pm");
		assertEquals(1, pd.getSize());

		pd.addPreset("Work", "6:30am", "1:00pm", new Color(255, 153, 161));
		assertEquals(2, pd.getSize());
	}

	/**
	 * Test method for {@link util.PresetData#getPresets()}.
	 */
	@Test
	void testGetPresets() {
		PresetData pd = new PresetData("testFiles/Presets.txt");
		assertEquals("Work", pd.getPresets()[0].getName());
		assertEquals("6:30am", pd.getPresets()[0].getStart());
		assertEquals("1:00pm", pd.getPresets()[0].getEnd());

		assertEquals("Work", pd.getPresets()[1].getName());
		assertEquals("1:00pm", pd.getPresets()[1].getStart());
		assertEquals("8:00pm", pd.getPresets()[1].getEnd());
	}
}
