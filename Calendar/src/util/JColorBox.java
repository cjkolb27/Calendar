package util;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;

/**
 * 
 * 
 * Code was created with the help of JColorComboBox: JComboBox as Color Chooser,
 * Friday 22 July 2011, by All About Java. Source:
 * https://www.allabtjava.com/2011/07/jcolorcombobox-jcombobox-as-color.html
 * 
 * @author Caleb Kolb
 */
@SuppressWarnings("rawtypes")
public class JColorBox extends JComboBox {

	/**
	 * Serial VersionUID
	 */
	private static final long serialVersionUID = -6382644066715104691L;

	/**
	 * Uses the colors from the color presets and adds them to the JColorBox
	 * 
	 * @param allColors all the colors
	 */
	public JColorBox(ColorData allColors) {
		super();
		DefaultComboBoxModel dcbm = new DefaultComboBoxModel();
		for (int i = 0; i < allColors.getSize(); i++) {
			dcbm.addElement(allColors.getColors()[i]);
		}
		setFocusable(false);
		setModel(dcbm);
		setRenderer(new ColorRenderer());
		this.setOpaque(true);
		this.setSelectedIndex(0);
	}

	/**
	 * 
	 * @param allColors
	 */
	public void updateBox(ColorData allColors) {
		DefaultComboBoxModel dcbm = new DefaultComboBoxModel();
		for (int i = 0; i < allColors.getSize(); i++) {
			dcbm.addElement(allColors.getColors()[i]);
		}
		setModel(dcbm);
		setRenderer(new ColorRenderer());
		this.setSelectedIndex(0);
	}

	@Override
	public void setSelectedItem(Object anObject) {
		super.setSelectedItem(anObject);
		System.out.println((Color) anObject);
		setBorder(BorderFactory.createLineBorder((Color) anObject, 200));
		setBackground((Color) anObject);
	}

	/**
	 * Custom renderer used for defining how the component should be rendered
	 * 
	 * Code was created with the help of JColorComboBox: JComboBox as Color Chooser,
	 * Friday 22 July 2011, by All About Java. Source:
	 * https://www.allabtjava.com/2011/07/jcolorcombobox-jcombobox-as-color.html
	 * 
	 * @author Calen Kolb
	 */
	@SuppressWarnings("serial")
	private class ColorRenderer extends JLabel implements javax.swing.ListCellRenderer {

		public ColorRenderer() {
			this.setOpaque(true);
		}

		public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected,
				boolean cellHasFocus) {
			Color color = (Color) value;
			// System.out.println(color);

			list.setSelectionBackground(null);
			list.setSelectionForeground(null);

			setBorder(null);

			setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 15));
			setBackground(color);
			setText(" ");
			setForeground(Color.black);

			return this;
		}

	}
}