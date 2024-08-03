package util;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SpringLayout;
import javax.swing.SwingConstants;

import events.EventData;
import ui.UI;

/**
 * Panel for holding all Calendar events for an individual date. The panel
 * allows for adding, editing, and removing DateButtons. DateButtons are stored
 * as a LinkedList with a sentinel head pointer.
 * 
 * @author Caleb Kolb
 */
public class DatePanel implements ActionListener {
	/** Current UI */
	private UI currentUI;
	/** Day of event */
	private int day;
	/** Month of event */
	private int month;
	/** Year of event */
	private int year;
	/** Panels for each day in the year */
	private JPanel panel;
	/** First dateLabel */
	private DateButton head;
	/** Combo box of preset events */
	private JComboBox<String> presets;
	/** Event text field */
	private JTextField jtf1;
	/** Start time text field */
	private JTextField jtf2;
	/** End time text field */
	private JTextField jtf3;
	/** Size of dateLabel */
	private int size;

	/**
	 * Date panel holds the current day, month, and year
	 * 
	 * @param day       the day of the event
	 * @param month     the month of the event
	 * @param year      the year of the event
	 * @param currentUI the current UI attached
	 */
	public DatePanel(int day, int month, int year, UI currentUI) {
		this.day = day;
		this.month = month;
		this.year = year;
		this.currentUI = currentUI;
		head = new DateButton(null, 0, null, day, month, year, null, 0, 0, 0);
		panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
		panel.setBackground(null);
	}

	/**
	 * Returns the JPanel
	 * 
	 * @return panel the panel of DatePanel
	 */
	public JPanel getPanel() {
		return panel;
	}

	/**
	 * Returns the panels day
	 * 
	 * @return day the day of the event
	 */
	public int getDay() {
		return day;
	}

	/**
	 * Returns the panels month
	 * 
	 * @return month the month of the event
	 */
	public int getMonth() {
		return month;
	}

	/**
	 * Returns the panels year
	 * 
	 * @return year the year of the event
	 */
	public int getYear() {
		return year;
	}

	/**
	 * Adding a button to the linked list of buttons requires a start string, start
	 * time, end string, day, month, year, and event name.
	 * 
	 * @param start     the start time string
	 * @param startTime the start time as an int
	 * @param end       the end time string
	 * @param day       the day of the event
	 * @param month     the month of the event
	 * @param year      the year of the event
	 * @param event     the event name
	 * @param red       the red color
	 * @param green     the green color
	 * @param blue      the blue color
	 */
	public void addButton(String start, int startTime, String end, int day, int month, int year, String event, int red,
			int green, int blue) {
		DateButton newLabel = new DateButton(start, startTime, end, day, month, year, event, red, green, blue);
		newLabel.getButton().addActionListener(this);
		// newLabel.getButton().setPreferredSize(new Dimension(3, 0));
		newLabel.getButton().setHorizontalAlignment(SwingConstants.LEFT);
		if (size == 0) {
			head.next = newLabel;
			getPanel().add(newLabel.getButton());
			size++;
			return;
		}
		DateButton current = head;
		while (current.next != null) {
			if (newLabel.getStartTime() == current.next.getStartTime()) {
				throw new IllegalArgumentException("Events Start at the Same Time");
			}
			if (newLabel.getStartTime() < current.next.getStartTime()) {
				removeAllButtons();
				DateButton temp = current.next;
				current.next = newLabel;
				newLabel.next = temp;
				addAllButtons();
				// getPanel().add(newLabel.getLabel());

				size++;
				return;
			}
			current = current.next;
		}
		current.next = newLabel;
		getPanel().add(newLabel.getButton());
		size++;
	}

	/**
	 * Editing a button in the list requires start string, original start time,
	 * start time, end string, day, month, year, event name, read, blue, green.
	 * 
	 * @param start             the start time string
	 * @param originalStartTime the original start time being edited
	 * @param startTime         the start time as an int
	 * @param end               the end time string
	 * @param day               the day of the event
	 * @param month             the month of the event
	 * @param year              the year of the event
	 * @param event             the event name
	 * @param red               the red color
	 * @param green             the green color
	 * @param blue              the blue color
	 */
	public void editButton(String start, int originalStartTime, int startTime, String end, int day, int month, int year,
			String event, int red, int green, int blue) {
		if (size == 0) {
			return;
		}
		removeButton(originalStartTime);
		addButton(start, startTime, end, day, month, year, event, red, green, blue);
	}

	/**
	 * Removes a given event based on start time. No two events can have the same
	 * start time
	 * 
	 * @param startTime the start time of the event
	 */
	public void removeButton(int startTime) {
		if (size == 0) {
			return;
		}
		DateButton current = head;
		while (current.next != null) {
			if (current.next.getStartTime() == startTime) {
				System.out.println("Removed");
				getPanel().remove(current.next.getButton());
				current.next = current.next.next;
				size--;
				removeAllButtons();
				addAllButtons();
				current = head;
				while (current.next != null) {
					System.out.println(current.next.startTime);
					current = current.next;
				}
				return;
			}
			current = current.next;
		}
	}

	/**
	 * Returns the size of the list of buttons
	 * 
	 * @return size the size of the list
	 */
	public int size() {
		return size;
	}

	/**
	 * Returns the sentinel head pointer
	 * 
	 * @return head the sentinel head pointer
	 */
	public DateButton getHead() {
		return head;
	}

	/**
	 * Deletes all values by seating the head pointer to null
	 */
	public void deleteAll() {
		removeAllButtons();
		head.next = null;
		size = 0;
	}

	/**
	 * Visually removes all buttons from the panel view. The data is still stored in
	 * the list
	 */
	public void removeAllButtons() {
		DateButton current = head;
		while (current.next != null) {
			// System.out.println(current.startTime);
			getPanel().remove(current.next.getButton());
			current = current.next;
		}
	}

	/**
	 * Adds all buttons to the panel from the list
	 */
	public void addAllButtons() {
		DateButton current = head.next;
		System.out.println();
		while (current != null) {
			System.out.println(current.startTime);
			getPanel().add(current.getButton());
			// current.getButton().addActionListener(this);
			current = current.next;
		}
		panel.repaint();
	}

	/**
	 * Used for checking when an event button has been pressed. This allows for
	 * editing and deleting the event
	 * 
	 * @param e the action event
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == presets) {
			System.out.println("Preset " + presets.getSelectedItem());
			jtf1.setText(currentUI.getPresetOptions().getPresets()[presets.getSelectedIndex()].getName());
			jtf2.setText(currentUI.getPresetOptions().getPresets()[presets.getSelectedIndex()].getStart());
			jtf3.setText(currentUI.getPresetOptions().getPresets()[presets.getSelectedIndex()].getEnd());
			presets.setSelectedIndex(-1);
		} else {
			System.out.println("Action");
			DateButton current = head.next;
			while (current != null) {
				if (e.getSource() == current.getButton()) {
					int textFieldSizeX = 151;
					int textFieldSizeZ = 20;

					System.out.println("Button pressed");
					JLabel col = new JLabel("Event Color");
					JLabel pre = new JLabel("Preset Event");
					JLabel lab1 = new JLabel("Event Name");
					jtf1 = new JTextField(current.getEvent());
					jtf1.setPreferredSize(new Dimension(textFieldSizeX, textFieldSizeZ));
					JLabel lab2 = new JLabel("Start Time");
					jtf2 = new JTextField(current.start);
					jtf2.setPreferredSize(new Dimension(textFieldSizeX, textFieldSizeZ));
					JLabel lab3 = new JLabel("End Time");
					jtf3 = new JTextField(current.end);
					jtf3.setPreferredSize(new Dimension(textFieldSizeX, textFieldSizeZ));
					JPanel pan = new JPanel();
					pan.setPreferredSize(new Dimension(100, 150));
					SpringLayout layout = new SpringLayout();
					pan.setLayout(layout);

					JColorBox jcb = currentUI.getJCB();
					jcb.setSelectedItem(current.getColor());
					// jcb.setBounds(100, 20, 140, 30);
					jcb.setPreferredSize(new Dimension(30, 20));
					jcb.setFocusable(false);
					jcb.setBorder(BorderFactory.createLineBorder(current.getColor(), 200));
					jcb.setForeground(null);

					Component[] c = jcb.getComponents();
					for (Component res : c) {
						System.out.println("Component: " + res);
						if (res instanceof AbstractButton) {
							if (res.isVisible()) {
								res.setVisible(false);
							}
						}
					}

					presets = new JComboBox<>(currentUI.getPresetOptions().getStringPresets());
					presets.setSelectedIndex(-1);
					presets.setFocusable(false);
					presets.addActionListener(this);

					pan.add(col);
					pan.add(jcb);
					layout.putConstraint(SpringLayout.WEST, col, 5, SpringLayout.WEST, pan);
					layout.putConstraint(SpringLayout.NORTH, col, 5, SpringLayout.NORTH, pan);
					layout.putConstraint(SpringLayout.WEST, jcb, 14, SpringLayout.EAST, col);
					layout.putConstraint(SpringLayout.NORTH, jcb, 5, SpringLayout.NORTH, pan);

					pan.add(pre);
					pan.add(presets);
					layout.putConstraint(SpringLayout.WEST, pre, 5, SpringLayout.WEST, pan);
					layout.putConstraint(SpringLayout.NORTH, pre, 30, SpringLayout.NORTH, col);
					layout.putConstraint(SpringLayout.WEST, presets, 5, SpringLayout.EAST, pre);
					layout.putConstraint(SpringLayout.NORTH, presets, 30, SpringLayout.NORTH, jcb);

					pan.add(lab1);
					pan.add(jtf1);
					layout.putConstraint(SpringLayout.WEST, lab1, 5, SpringLayout.WEST, pan);
					layout.putConstraint(SpringLayout.NORTH, lab1, 30, SpringLayout.NORTH, pre);
					layout.putConstraint(SpringLayout.WEST, jtf1, 10, SpringLayout.EAST, lab1);
					layout.putConstraint(SpringLayout.NORTH, jtf1, 30, SpringLayout.NORTH, presets);

					pan.add(lab2);
					pan.add(jtf2);
					layout.putConstraint(SpringLayout.WEST, lab2, 5, SpringLayout.WEST, pan);
					layout.putConstraint(SpringLayout.NORTH, lab2, 24, SpringLayout.NORTH, lab1);
					layout.putConstraint(SpringLayout.WEST, jtf2, 18, SpringLayout.EAST, lab2);
					layout.putConstraint(SpringLayout.NORTH, jtf2, 24, SpringLayout.NORTH, jtf1);

					pan.add(lab3);
					pan.add(jtf3);
					layout.putConstraint(SpringLayout.WEST, lab3, 5, SpringLayout.WEST, pan);
					layout.putConstraint(SpringLayout.NORTH, lab3, 24, SpringLayout.NORTH, lab2);
					layout.putConstraint(SpringLayout.WEST, jtf3, 25, SpringLayout.EAST, lab3);
					layout.putConstraint(SpringLayout.NORTH, jtf3, 24, SpringLayout.NORTH, jtf2);

					boolean tryEvent = true;
					while (tryEvent) {
						tryEvent = false;
						String[] choices = { "Update", "Delete", "Cancel" };
						Color selectedColor = new Color(255, 153, 161);
						int optionSelected = JOptionPane.showOptionDialog(currentUI.getScreen(), pan, "Edit Event",
								JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null, choices, choices[0]);
						System.out.println(optionSelected);
						try {
							selectedColor = currentUI.getColorOptions().getColors()[jcb.getSelectedIndex()];
						} catch (Exception e2) {
							selectedColor = new Color(255, 153, 161);
						}
						if (optionSelected == 0) {
							try {
								EventData newEvent = currentUI.getManager().editEvent(
										(double) (year + (((month * 31) + (day)) * .001)), current.getStartTime(),
										jtf1.getText(), jtf2.getText(), jtf3.getText(), getDay(), getMonth(), getYear(),
										selectedColor.getRed(), selectedColor.getGreen(), selectedColor.getBlue());
								editButton(newEvent.getStartTime(), current.getStartTime(), newEvent.getStartInt(),
										newEvent.getEndTime(), getDay(), getMonth(), getYear(), newEvent.getName(),
										newEvent.getColor().getRed(), newEvent.getColor().getGreen(),
										newEvent.getColor().getBlue());
								currentUI.getScreen().setVisible(true);
								currentUI.getScreen().repaint();
								currentUI.getScreen().validate();
								return;
							} catch (Exception e1) {
								jtf2.setText(current.start);
								jtf3.setText(current.end);
								JOptionPane.showMessageDialog(currentUI.getScreen(), e1.getMessage());
								tryEvent = true;
							}
						} else if (optionSelected == 1) {
							try {
								currentUI.getManager().removeEvent((double) (year + (((month * 31) + (day)) * .001)),
										current.getStartTime());
								removeButton(current.getStartTime());
								currentUI.getScreen().setVisible(true);
								currentUI.getScreen().repaint();
								currentUI.getScreen().validate();
								return;
							} catch (Exception e2) {
								JOptionPane.showMessageDialog(currentUI.getScreen(), e2.getMessage());
								tryEvent = true;
							}
						}
					}
				}
				current = current.next;
			}
		}
	}

	/**
	 * Events are stored as button that can be clicked on to edit them. This class
	 * holds information about each event and is used by the DatePanel class
	 * 
	 * @author Caleb Kolb
	 */
	public class DateButton {
		/** Start time of an event */
		private int startTime;
		/** End time of an event */
		private int endTime;
		/** Day of event */
		private int day;
		/** Month of event */
		private int month;
		/** Year of event */
		private int year;
		/** Start time of an event as a string */
		private String start;
		/** End time of an event as a string */
		private String end;
		/** Name of event */
		private String event;
		/** JLabel for adding to panel */
		private JButton button;
		/** Next dateLabel */
		private DateButton next;
		/** Color of the event */
		private Color dateColor;

		/**
		 * Creates a button with information about start time, end time, day, month,
		 * year, event name, and color.
		 * 
		 * @param start     the start time string
		 * @param startTime the start time as an int
		 * @param end       the end time string
		 * @param day       the day of the event
		 * @param month     the month of the event
		 * @param year      the year of the event
		 * @param event     the event name
		 * @param red       the red color
		 * @param green     the green color
		 * @param blue      the blue color
		 */
		public DateButton(String start, int startTime, String end, int day, int month, int year, String event, int red,
				int green, int blue) {
			setStart(start);
			setStartTime(startTime);
			setEnd(end);
			setDay(day);
			setMonth(month);
			setYear(year);
			this.event = event;
			// button = new JButton("<html>" + start + " " + event + "</html>");
			button = new JButton(start + " " + event);
			button.setContentAreaFilled(false);
			button.setFocusable(false);
			// button.setBorderPainted(false);
			button.setFont(new Font("Comic Sans", Font.BOLD, 15));
			button.setMargin(new Insets(0, 0, 0, 0));
			button.setIcon(null);
			dateColor = new Color(red, green, blue);
			button.setForeground(dateColor);
			// button.setSize(new Dimension(2, 2));
			next = null;
		}

		/**
		 * Sets the day
		 * 
		 * @param day the day of the event
		 */
		public void setDay(int day) {
			this.day = day;
		}

		/**
		 * Sets the month
		 * 
		 * @param month the month of the event
		 */
		public void setMonth(int month) {
			this.month = month;
		}

		/**
		 * Sets the year
		 * 
		 * @param year the year of the event
		 */
		public void setYear(int year) {
			this.year = year;
		}

		/**
		 * Returns the day
		 * 
		 * @return day the day of the event
		 */
		public int getDay() {
			return day;
		}

		/**
		 * Returns the month
		 * 
		 * @return month the month of the event
		 */
		public int getMonth() {
			return month;
		}

		/**
		 * Returns the year
		 * 
		 * @return year the year of the event
		 */
		public int getYear() {
			return year;
		}

		/**
		 * Sets the start time string
		 * 
		 * @param start the start time string
		 */
		public void setStart(String start) {
			this.start = start;
		}

		/**
		 * Sets the start time int
		 * 
		 * @param startTime the start time as an int
		 */
		public void setStartTime(int startTime) {
			this.startTime = startTime;
		}

		/**
		 * Sets the end string
		 * 
		 * @param end the end time as a string
		 */
		public void setEnd(String end) {
			this.end = end;
		}

		/**
		 * Gets the start time
		 * 
		 * @return startTime the start time of the event
		 */
		public int getStartTime() {
			return startTime;
		}

		/**
		 * Gets the event name
		 * 
		 * @return event the event name
		 */
		public String getEvent() {
			return event;
		}

		/**
		 * Returns the button of the event
		 * 
		 * @return button the button
		 */
		public JButton getButton() {
			return button;
		}

		/**
		 * Gets the color of the event
		 * 
		 * @return dateColor the color of the event
		 */
		public Color getColor() {
			return dateColor;
		}

		/**
		 * Prints all values of the event into a string to the cmd
		 */
		public void printToCMD() {
			System.out.println(start + " " + end + " " + day + " " + month + " " + year + " " + event);
		}

	}
}