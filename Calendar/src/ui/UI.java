// Test UI Window
package ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.io.File;
import java.util.Calendar;
import java.util.Iterator;
import java.util.Scanner;

import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SpringLayout;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;

import events.EventData;
import manager.CalendarManager;
import util.SortedDateList;

/**
 * GUI for displaying the Calendar to the user.
 * 
 * @author Caleb Kolb
 */
public class UI extends JFrame implements ActionListener, MouseWheelListener {

	/** Default Serial Version UID */
	private static final long serialVersionUID = 1L;
	/** Settings window state */
	private ScreenState windowState = null;
	/** Frame that holds all components on the screen */
	private static JFrame screen;
	/** Small calendar */
	private JPanel smallCalendar;
	/** Holds the panel for all button days */
	private JPanel panel;
	/** North Panel */
	private JPanel northPanel;
	/** West Panel */
	private JPanel westPanel;
	/** Srolling panel */
	private JPanel calendarScrollingPan;
	/** Environment Graphics */
	private static GraphicsEnvironment env;
	/** Default environment */
	private static GraphicsDevice ev;
	/** Calendar Manager */
	private static CalendarManager manager;
	/** Title GUI */
	private static final String PROGRAM_TITLE = "Calendar";
	/** All month names */
	private static final String[] ALLMONTHNAMES = { "January", "Febuary", "March", "April", "May", "June", "July",
			"August", "September", "October", "November", "December" };
	/** All week names */
	private static final String[] DAYSOFWEEKNAMES = { "Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday",
			"Saturday" };
	/** Stores all settings as an array of strings */
	private static String[] settings;
	/** Holds strings for all presetEvents */
	private static String[] presetEvents;
	/** Days per month */
	private static int[] daysPerMonth = { 31, 29, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 };
	/** Weeks per month */
	private static int[] startWeekPerMonth = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
	/** Position of week that changes month JLabel */
	private static int[] transitionWeek = { 31, 29, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 };
	/** Each start day of each month from 1 to 7 */
	private static int[] startDayPerMonth = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
	/** Small Month Buttons */
	private JButton[] smallMonthButtons;
	/** New event button */
	private JButton newEventBut;
	/** Presets button */
	private JButton presetsBut;
	/** Colors buttons */
	private JButton colorsBut;
	/** Buttons for every calendar day */
	private JButton[] buttons;
	/** Previous year button */
	private JButton lastYear;
	/** Next year button */
	private JButton nextYear;
	/** All presets of an event */
	private JComboBox<String> preset;
	/** Event text field */
	private JTextField eventTextField;
	/** Start text field */
	private JTextField startTextField;
	/** End text field */
	private JTextField endTextField;
	/** Panel for all events added to a day */
	private static DatePanel[] datePanel;
	/** Menu bar */
	private JMenuBar menuBar;
	/** Menu */
	private JMenu menu;
	/** Item for loading calendar date */
	private JMenuItem loadCalendar;
	/** Item for editing settings */
	private JMenuItem editSettings;
	/** Item for quitting the program */
	private JMenuItem quit;
	/** True when settings have changed */
	private Boolean settingsChanged;
	/** Scroll Frame for holding all map days */
	private JScrollPane scrollFrame;
	/** All color options */
	private static ColorData colorOptions;
	/** Scroll Frame color */
	private static Color panelColor = new Color(20, 20, 20);
	/** Odd month color */
	private static Color oddMonthColor = new Color(38, 38, 38);
	/** Even month color */
	private static Color evenMonthColor = new Color(26, 26, 26, 255);
	/** Event color */
	private static Color eventColor = new Color(255, 153, 161);
	/** File menu title */
	private static final String FILE_MENU_TITLE = "File";
	/** Load calendar title */
	private static final String LOADCAL = "Load Calendar";
	/** Edit Settings title */
	private static final String EDITSET = "Edit Settings";
	/** Label for holding the month name */
	private JLabel month;
	/** Label for holding the month name */
	private JLabel monthSmall;
	/** Year of calendar */
	private int yearOfCalendar;
	/** Month of calendar */
	private int monthOfCalendar;
	/** Days in the calendar */
	private int daysInCalendar;
	/** Current Month shown on the calendar */
	private int scrollMonth;
	/** Total number of weeks in the year */
	private int totalWeeks;
	/** Keeps track of scroll bar location */
	private double scrollLocation;
	/** Keeps track of scroll bar height */
	private double scrollHeight;
	/** Used for removing all events visually */
	private JMenuItem removeAll;
	/** Used for add all events visually */
	private JMenuItem addAll;

	/**
	 * Creates the GUI for the user to interact with the CalendarManager
	 */
	public UI() {
		super();
		colorOptions = new ColorData("mtf", System.getProperty("user.home") + File.separator + "Documents"
				+ File.separator + "CalendarData" + File.separator + "Colors.txt");
		presetEvents = new String[] { "Work 6:30am-2:00pm", "Work 2:00pm-8:30pm" };
		preset = new JComboBox<>(presetEvents);
		preset.addActionListener(this);
		preset.setFocusable(false);
		env = GraphicsEnvironment.getLocalGraphicsEnvironment();
		ev = env.getDefaultScreenDevice();
		settingsChanged = false;
		manager = new CalendarManager();
		screen = new JFrame();
		ImageIcon image = new ImageIcon(getClass().getClassLoader().getResource("CalendarPNG.png"));
		screen.setIconImage(image.getImage());
		screen.setSize(Toolkit.getDefaultToolkit().getScreenSize().width,
				Toolkit.getDefaultToolkit().getScreenSize().height);
		screen.setDefaultCloseOperation(EXIT_ON_CLOSE);
		screen.setLayout(new BorderLayout());
		screen.setTitle(PROGRAM_TITLE);
		screen.setBackground(panelColor);

		panel = new JPanel(new GridLayout(54, 7, 1, 1));
		panel.setBorder(BorderFactory.createEtchedBorder());

		setAllDates();

		buildButtonDays();

		createMenuBar();
		settings = manager.defaultSettings();
		ScreenState state = ScreenState.Windowed;
		if ("Borderless".equals(settings[1])) {
			state = ScreenState.Borderless;
		} else if ("Fullscreen".equals(settings[1])) {
			state = ScreenState.Fullscreen;
		}
		changeWindow(state);

		setUpScreen();

		screen.setVisible(true);
		scrollFrame.getVerticalScrollBar()
				.setValue((int) (((double) (scrollFrame.getVerticalScrollBar().getMaximum()) / (double) totalWeeks)
						* ((double) (startWeekPerMonth[monthOfCalendar] - 1) - .3)));
		screen.repaint();
		screen.validate();
	}

	/**
	 * IDK
	 * 
	 * @param args the amount of arguments
	 */
	public static void main(String[] args) {
		new UI();
	}

	/**
	 * Returns an array of all preset events
	 * 
	 * @return presetEvents an array of strings of all presets
	 */
	public static String[] getPresetEvents() {
		return presetEvents;
	}

	/**
	 * Changes the window type to windowed, fullscreen, or borderless.
	 * 
	 * @param applied the newly applied window type
	 */
	public void changeWindow(ScreenState applied) {
		screen.setVisible(false);
		if (applied == ScreenState.Fullscreen && windowState != ScreenState.Fullscreen) {
			if (settingsChanged) {
				setScreenWindow();
				if (ev.isFullScreenSupported()) {
					ev.setFullScreenWindow(screen);
				}

				screen.setVisible(true);
			} else {
				if (ev.isFullScreenSupported()) {
					ev.setFullScreenWindow(screen);
				}
			}
			windowState = ScreenState.Fullscreen;
			System.out.println("State Changed to Fullscreen");
		}
		if (applied == ScreenState.Borderless && windowState != ScreenState.Borderless) {
			if (settingsChanged) {
				setScreenWindow();
				screen.setUndecorated(true);
				screen.setExtendedState(Frame.MAXIMIZED_BOTH);
				screen.setVisible(true);
			} else {
				screen.setUndecorated(true);
				screen.setExtendedState(Frame.MAXIMIZED_BOTH);
			}
			windowState = ScreenState.Borderless;
			System.out.println("State Changed to Borderless");
		}
		if (applied == ScreenState.Windowed && windowState != ScreenState.Windowed) {
			if (settingsChanged) {
				setScreenWindow();
				screen.setUndecorated(false);
				screen.setExtendedState(Frame.MAXIMIZED_BOTH);
				screen.setVisible(true);
				System.out.println("IDK");
			} else {
				screen.setUndecorated(false);
				screen.setExtendedState(Frame.MAXIMIZED_BOTH);
			}
			windowState = ScreenState.Windowed;
			System.out.println("State Changed to Windowed");
			screen.setVisible(true);
		}
	}

	/**
	 * Sets up all panels on the screen. There is the West panel, North Panel, and
	 * scrollFrame scroll panel.
	 */
	private void setUpScreen() {
		scrollFrame = new JScrollPane(panel, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scrollFrame.setBackground(panelColor);
		scrollFrame.setBorder(BorderFactory.createEtchedBorder());
		scrollFrame.getVerticalScrollBar().setUnitIncrement(20);
		scrollFrame.getVerticalScrollBar().setPreferredSize(new Dimension(0, 0));
		scrollFrame.addMouseWheelListener(this);

		northPanel = new JPanel(new GridLayout(2, 1, 0, 0));
		northPanel.setLayout(new BoxLayout(northPanel, BoxLayout.PAGE_AXIS));
		westPanel = new JPanel();
		northPanel.setBackground(Color.DARK_GRAY);
		westPanel.setBackground(Color.DARK_GRAY);
		northPanel.setPreferredSize(new Dimension(1, 100));
		westPanel.setPreferredSize(new Dimension(265, 1));
		month = new JLabel("<html>" + ALLMONTHNAMES[monthOfCalendar] + " " + yearOfCalendar + "</html>");
		month.setFont(new Font("Comic Sans", Font.BOLD, 40));
		month.setForeground(Color.WHITE);
		JPanel yearSelecter = new JPanel(new FlowLayout());
		yearSelecter.setBackground(null);
		yearSelecter.setPreferredSize(new Dimension(50, 50));
		for (int i = 0; i < 7; i++) {
			JLabel space = new JLabel();
			space.setSize(new Dimension(0, 0));
			space.setVisible(true);
			if (i == 2) {
				lastYear = new JButton("<html>" + "\u2190" + "</html>");
				lastYear.addActionListener(this);
				lastYear.setFont(new Font("Comic Sans", Font.BOLD, 40));
				lastYear.setBackground(null);
				lastYear.setForeground(Color.WHITE);
				lastYear.setBorderPainted(false);
				lastYear.setFocusable(false);
				lastYear.setPreferredSize(new Dimension(40, 40));
				lastYear.setMargin(new Insets(0, 0, 0, 0));
				yearSelecter.add(lastYear);
				northPanel.add(space);
			} else if (i == 3) {
				yearSelecter.add(month);
				northPanel.add(yearSelecter);
			} else if (i == 4) {
				nextYear = new JButton("<html>" + "\u2192" + "</html>");
				nextYear.addActionListener(this);
				nextYear.setFont(new Font("Comic Sans", Font.BOLD, 40));
				nextYear.setBackground(null);
				nextYear.setForeground(Color.WHITE);
				nextYear.setBorderPainted(false);
				nextYear.setFocusable(false);
				nextYear.setPreferredSize(new Dimension(40, 40));
				nextYear.setMargin(new Insets(0, 0, 0, 0));
				yearSelecter.add(nextYear);
				northPanel.add(space);
			} else {
				northPanel.add(space);
			}
		}
		JPanel week = new JPanel(new GridLayout(1, 7, 0, 0));
		week.setBackground(null);
		for (int i = 0; i < 7; i++) {
			JLabel weekDay;
			if (i == 0) {
				weekDay = new JLabel(" " + DAYSOFWEEKNAMES[i]);
			} else
				weekDay = new JLabel(DAYSOFWEEKNAMES[i]);
			weekDay.setFont(new Font("Comic Sans", Font.BOLD, 20));
			weekDay.setForeground(Color.WHITE);
			week.add(weekDay);
		}
		northPanel.add(week);
		setWestPanel();
		screen.add(westPanel, BorderLayout.WEST);

		calendarScrollingPan = new JPanel(new BorderLayout());
		calendarScrollingPan.add(northPanel, BorderLayout.NORTH);
		calendarScrollingPan.add(scrollFrame, BorderLayout.CENTER);
		screen.add(calendarScrollingPan, BorderLayout.CENTER);
		scrollFrame.getVerticalScrollBar()
				.setValue((int) (((double) (scrollFrame.getVerticalScrollBar().getMaximum()) / (double) totalWeeks)
						* ((double) (startWeekPerMonth[monthOfCalendar] - 1) - .3)));
	}

	/**
	 * Sets up the west panel using a BoxLayout to evenly distribute the buttons
	 */
	public void setWestPanel() {
		westPanel.setLayout(new BoxLayout(westPanel, BoxLayout.PAGE_AXIS));
		newEventBut = new JButton("+ New Event");
		newEventBut.setAlignmentX(Component.CENTER_ALIGNMENT);
		newEventBut.addActionListener(this);
		newEventBut.setFont(new Font("Comic Sans", Font.BOLD, 25));
		newEventBut.setMaximumSize(new Dimension(220, 40));

		presetsBut = new JButton("+ Preset Event");
		presetsBut.setAlignmentX(Component.CENTER_ALIGNMENT);
		presetsBut.addActionListener(this);
		presetsBut.setFont(new Font("Comic Sans", Font.BOLD, 25));
		presetsBut.setMaximumSize(new Dimension(220, 40));

		colorsBut = new JButton("+ Colors");
		colorsBut.setAlignmentX(Component.CENTER_ALIGNMENT);
		colorsBut.addActionListener(this);
		colorsBut.setFont(new Font("Comic Sans", Font.BOLD, 25));
		colorsBut.setMaximumSize(new Dimension(220, 40));

		createSmallCalendar(monthOfCalendar);

		westPanel.add(new Box.Filler(new Dimension(0, 1), new Dimension(0, 60), new Dimension(0, 60)));
		westPanel.add(newEventBut);
		monthSmall = new JLabel(ALLMONTHNAMES[monthOfCalendar] + " " + yearOfCalendar);
		monthSmall.setFont(new Font("Comic Sans", Font.BOLD, 20));
		monthSmall.setAlignmentX(Component.CENTER_ALIGNMENT);
		monthSmall.setForeground(Color.WHITE);
		westPanel.add(monthSmall);
		westPanel.add(new Box.Filler(new Dimension(0, 1), new Dimension(0, 10), new Dimension(0, 10)));
		westPanel.add(smallCalendar);
		westPanel.add(new Box.Filler(new Dimension(0, 1), new Dimension(0, 1), new Dimension(0, 1000)));
		westPanel.add(presetsBut);
		westPanel.add(new Box.Filler(new Dimension(0, 1), new Dimension(0, 50), new Dimension(0, 100)));
		westPanel.add(colorsBut);
		westPanel.add(new Box.Filler(new Dimension(0, 1), new Dimension(0, 20), new Dimension(0, 20)));
	}

	/**
	 * Creates a small calendar of buttons on the westPanel. This allows for
	 * changing the small calendar to align with the current scroll month and start
	 * up month when needed.
	 * 
	 * @param month the month of the calendar
	 */
	public void createSmallCalendar(int month) {
		if (smallCalendar != null) {
			for (int i = 0; i < 42; i++) {
				smallCalendar.remove(smallMonthButtons[i]);
			}
		} else
			smallCalendar = new JPanel();
		smallCalendar.setLayout(new GridLayout(6, 7, 1, 1));
		smallCalendar.setMaximumSize(new Dimension(218, 191));
		smallMonthButtons = new JButton[42];
		boolean tracker = false;
		for (int i = 0; i < 42; i++) {
			if (i >= startDayPerMonth[month] - 1 && startDayPerMonth[month] - 1 + daysPerMonth[month] >= i) {
				tracker = true;

			} else
				tracker = false;
			if (!tracker) {
				smallMonthButtons[i] = new JButton();
				if (month % 2 == 0) {
					smallMonthButtons[i].setBackground(evenMonthColor);
				} else
					smallMonthButtons[i].setBackground(oddMonthColor);
				smallMonthButtons[i].setMargin(new Insets(0, 0, 0, 0));
				smallMonthButtons[i].setFocusable(false);
				smallMonthButtons[i].setPreferredSize(new Dimension(30, 30));
			} else {
				smallMonthButtons[i] = new JButton((i - startDayPerMonth[month] + 2) + "");
				if ((month + 1) % 2 == 0) {
					smallMonthButtons[i].setBackground(evenMonthColor);
				} else
					smallMonthButtons[i].setBackground(oddMonthColor);
				smallMonthButtons[i].setMargin(new Insets(0, 0, 0, 0));
				smallMonthButtons[i].setFocusable(false);
				smallMonthButtons[i].setFont(new Font("Comic Sans", Font.BOLD, 15));
				smallMonthButtons[i].setForeground(Color.WHITE);
				smallMonthButtons[i].setPreferredSize(new Dimension(30, 30));
			}
			smallCalendar.add(smallMonthButtons[i]);
		}
	}

	/**
	 * Resets the scroll panel when changing to a different year
	 */
	public void resetScrollPan() {
		calendarScrollingPan.remove(scrollFrame);
		screen.remove(calendarScrollingPan);
		scrollFrame.remove(panel);
		scrollFrame = new JScrollPane(panel, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scrollFrame.setBackground(panelColor);
		scrollFrame.setBorder(BorderFactory.createEtchedBorder());
		scrollFrame.getVerticalScrollBar().setUnitIncrement(20);
		scrollFrame.getVerticalScrollBar().setPreferredSize(new Dimension(0, 0));
		scrollFrame.addMouseWheelListener(this);

		calendarScrollingPan = new JPanel(new BorderLayout());
		calendarScrollingPan.add(northPanel, BorderLayout.NORTH);
		calendarScrollingPan.add(scrollFrame, BorderLayout.CENTER);
		screen.add(calendarScrollingPan, BorderLayout.CENTER);
		screen.setVisible(true);
	}

	/**
	 * Sets all constants for calendar information.
	 */
	private void setAllDates() {
		Calendar cal = Calendar.getInstance();
		yearOfCalendar = manager.getYear();
		monthOfCalendar = cal.get(Calendar.MONTH);
		scrollMonth = monthOfCalendar;
	}

	/**
	 * Builds all the buttons for each day in a year
	 */
	private void buildButtonDays() {
		Calendar cal = Calendar.getInstance();

		buttons = new JButton[366];

		datePanel = new DatePanel[366];

		SortedDateList<EventData> sdl = manager.getEvents();

		// System.out.println(sdl.size());

		Iterator<EventData> it = sdl.iterator();
		EventData currentData;
		if (it.hasNext()) {
			currentData = it.next();
			// System.out.println("Not Null");
			// System.out.println("Printed Data: " + currentData.getDay() + " " +
			// currentData.getMonth() + " "
			// + currentData.getYear());
		} else {
			currentData = null;
		}

		int currentDay = 0;
		int currentWeek = 0;
		for (int i = 0; i < 12; i++) {
			cal.set(yearOfCalendar, i, 1);
			startDayPerMonth[i] = cal.get(Calendar.DAY_OF_WEEK);
			int days = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
			// System.out.println(days);
			if (i == 0) {
				for (int j = 0; j < cal.get(Calendar.DAY_OF_WEEK) - 1; j++) {
					JButton b = new JButton();
					b.setVisible(false);
					b.setPreferredSize(new Dimension(75, 75));
					panel.add(b);
				}
			}
			if (i == 1 && days == 28) {
				daysPerMonth[1] = 28;
				buttons[currentDay] = new JButton();
				buttons[currentDay].addActionListener(this);
				currentDay++;
			} else
				daysPerMonth[1] = 29;
			int weekCount = 0;
			startWeekPerMonth[i] = 0;

			for (int j = 0; j < days; j++) {
				cal.set(yearOfCalendar, i, j + 1);
				if (cal.get(Calendar.DAY_OF_WEEK) == 7 && weekCount < 3) {
					if (weekCount == 2) {
						transitionWeek[i] = currentDay;
						// System.out.println(currentDay);
						weekCount++;
					} else {
						weekCount++;
					}
				}
				if (cal.get(Calendar.DAY_OF_WEEK) == 1 || (i == 0 && j == 0)) {
					currentWeek++;
				}
				if (j == 0) {
					startWeekPerMonth[i] = currentWeek;
				}
				buttons[currentDay] = new JButton();
				buttons[currentDay].setPreferredSize(new Dimension(80, GraphicsEnvironment.getLocalGraphicsEnvironment()
						.getDefaultScreenDevice().getDisplayMode().getWidth() / 12));
				// System.out.println(GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice()
				// .getDisplayMode().getWidth());
				buttons[currentDay].addActionListener(this);
				JLabel label;
				if (j == 0) {
					label = new JLabel((i + 1) + "/" + (j + 1));
				} else
					label = new JLabel("" + (j + 1));
				label.setHorizontalAlignment(SwingConstants.LEFT);
				label.setHorizontalTextPosition(SwingConstants.LEFT);
				label.setVerticalAlignment(SwingConstants.TOP);
				label.setVerticalTextPosition(SwingConstants.TOP);
				label.setFont(new Font("Comic Sans", Font.BOLD, 25));
				cal = Calendar.getInstance();
				if (cal.get(Calendar.DAY_OF_MONTH) == j + 1 && cal.get(Calendar.MONTH) == i
						&& cal.get(Calendar.YEAR) == yearOfCalendar) {
					label.setForeground(eventColor);
					buttons[currentDay].setBorder(new LineBorder(eventColor, 2));
				} else {
					label.setForeground(Color.WHITE);
					buttons[currentDay].setBorder(BorderFactory.createEtchedBorder());
				}
				buttons[currentDay].setLayout(new BorderLayout());
				buttons[currentDay].add(label, BorderLayout.NORTH);
				buttons[currentDay].setFocusable(false);
				// buttons[currentDay].setContentAreaFilled(false);
				if (i % 2 == 0) {
					buttons[currentDay].setBackground(evenMonthColor);
				} else
					buttons[currentDay].setBackground(oddMonthColor);
				datePanel[currentDay] = new DatePanel((j + 1), (i + 1), yearOfCalendar);
				while (currentData != null && currentData.getDay() == j + 1 && currentData.getMonth() == i + 1
						&& currentData.getYear() == yearOfCalendar) {
					datePanel[currentDay].addButton(currentData.getStartTime(), currentData.getStartInt(),
							currentData.getEndTime(), currentData.getDay(), currentData.getMonth(),
							currentData.getYear(), currentData.getName(), currentData.getColor().getRed(),
							currentData.getColor().getGreen(), currentData.getColor().getBlue());

					if (it.hasNext()) {
						currentData = it.next();
					} else {
						currentData = null;
					}
				}
				datePanel[currentDay].getPanel().setBackground(null);

				buttons[currentDay].add(datePanel[currentDay].getPanel(), BorderLayout.CENTER);
				panel.add(buttons[currentDay]);
				currentDay++;
			}
			daysInCalendar = currentDay;
		}
		totalWeeks = currentWeek;
		panel.setLayout(new GridLayout(totalWeeks, 7, 1, 1));
		panel.setAlignmentX(SwingConstants.CENTER);
		panel.setAlignmentY(SwingConstants.BOTTOM);
		panel.setBackground(panelColor);
		System.out.println(totalWeeks);
	}

	private void setScreenWindow() {
		System.out.println("Setting something");
		screen.dispose();
		screen.setVisible(false);
		env = GraphicsEnvironment.getLocalGraphicsEnvironment();
		ev = env.getDefaultScreenDevice();
		manager = new CalendarManager();
		screen = new JFrame();
		ImageIcon image = new ImageIcon(getClass().getClassLoader().getResource("CalendarPNG.png"));
		screen.setIconImage(image.getImage());
		screen.setSize(Toolkit.getDefaultToolkit().getScreenSize().width,
				Toolkit.getDefaultToolkit().getScreenSize().height);
		screen.setDefaultCloseOperation(EXIT_ON_CLOSE);
		screen.setLayout(new BorderLayout());

		panel = new JPanel(new GridLayout(totalWeeks, 7, 1, 1));

		setAllDates();

		buildButtonDays();

		createMenuBar();

		setUpScreen();

		scrollFrame.getVerticalScrollBar()
				.setValue((int) (((double) (scrollFrame.getVerticalScrollBar().getMaximum()) / (double) totalWeeks)
						* ((double) (startWeekPerMonth[monthOfCalendar] - 1) - .3)));

		screen.repaint();
		screen.validate();
		System.out.println("Setting everything");
	}

	/**
	 * All screen states
	 */
	public enum ScreenState {
		/** Fullscreen state */
		Fullscreen,
		/** Borderless state */
		Borderless,
		/** Windowed state */
		Windowed
	}

	private void createMenuBar() {
		menuBar = new JMenuBar();
		menu = new JMenu(FILE_MENU_TITLE);
		menu.setBackground(Color.DARK_GRAY);
		menu.setForeground(Color.WHITE);
		loadCalendar = new JMenuItem(LOADCAL);
		editSettings = new JMenuItem(EDITSET);
		quit = new JMenuItem("Quit");
		removeAll = new JMenuItem("Remove All");
		addAll = new JMenuItem("Add All");

		loadCalendar.addActionListener(this);
		loadCalendar.setBackground(Color.DARK_GRAY);
		loadCalendar.setForeground(Color.WHITE);
		editSettings.addActionListener(this);
		editSettings.setBackground(Color.DARK_GRAY);
		editSettings.setForeground(Color.WHITE);
		removeAll.addActionListener(this);
		removeAll.setBackground(Color.DARK_GRAY);
		removeAll.setForeground(Color.WHITE);
		addAll.addActionListener(this);
		addAll.setBackground(Color.DARK_GRAY);
		addAll.setForeground(Color.WHITE);
		quit.addActionListener(this);
		quit.setBackground(Color.DARK_GRAY);
		quit.setForeground(Color.WHITE);

		menu.add(loadCalendar);
		menu.add(editSettings);
		menu.add(removeAll);
		menu.add(addAll);
		menu.add(quit);
		menuBar.add(menu);
		menuBar.setBackground(Color.DARK_GRAY);
		menuBar.setForeground(Color.WHITE);
		menuBar.setBorder(BorderFactory.createBevelBorder(0));
		screen.setJMenuBar(menuBar);
	}

	@Override
	public void mouseWheelMoved(MouseWheelEvent arg0) {
		scrollLocation = scrollFrame.getVerticalScrollBar().getValue();
		scrollHeight = scrollFrame.getVerticalScrollBar().getMaximum() - scrollFrame.getVerticalScrollBar().getHeight();
		try {
			if (buttons[transitionWeek[scrollMonth]].getVisibleRect().height
					* buttons[transitionWeek[scrollMonth]].getVisibleRect().width == 0) {
				for (int i = 0; i < 12; i++) {
					if ((buttons[transitionWeek[i]].getVisibleRect().height
							* buttons[transitionWeek[i]].getVisibleRect().width) != 0) {
						scrollMonth = i;
						month.setText("<html>" + ALLMONTHNAMES[scrollMonth] + " " + yearOfCalendar + "</html>");
						monthSmall.setText(ALLMONTHNAMES[scrollMonth] + " " + yearOfCalendar);
						createSmallCalendar(scrollMonth);
						System.gc();
						screen.setVisible(true);
						i = 12;
					}
				}
			}
			if (scrollMonth - 1 >= 0 && buttons[transitionWeek[scrollMonth - 1]].getVisibleRect().height
					* buttons[transitionWeek[scrollMonth - 1]].getVisibleRect().width != 0) {
				for (int i = 0; i < 12; i++) {
					if ((buttons[transitionWeek[i]].getVisibleRect().height
							* buttons[transitionWeek[i]].getVisibleRect().width) != 0) {
						scrollMonth = i;
						month.setText("<html>" + ALLMONTHNAMES[scrollMonth] + " " + yearOfCalendar + "</html>");
						monthSmall.setText(ALLMONTHNAMES[scrollMonth] + " " + yearOfCalendar);
						createSmallCalendar(scrollMonth);
						System.gc();
						screen.setVisible(true);
						i = 12;
					}
				}
			}
			if (scrollMonth != 11 && scrollLocation == scrollHeight) {
				scrollMonth = 11;
				month.setText("<html>" + ALLMONTHNAMES[scrollMonth] + " " + yearOfCalendar + "</html>");
				monthSmall.setText(ALLMONTHNAMES[scrollMonth] + " " + yearOfCalendar);
				createSmallCalendar(scrollMonth);
				System.gc();
				screen.setVisible(true);
			}
		} catch (Exception e) {
			// Nothing
		}
	}

	/**
	 * Deletes All events from the datePanel by setting them to null. This is used
	 * to help garbage collection free up this memory quicker
	 */
	public void deleteAllEvents() {
		for (int i = 0; i < daysInCalendar; i++) {
			if (buttons[i] != null) {
				if (datePanel[i] != null && datePanel[i].size() > 0) {
					datePanel[i] = null;
				}
				buttons[i].removeAll();
				buttons[i] = null;
			}
		}
	}

	/**
	 * This method is used to perform actions when a button is pressed in the
	 * program. All major buttons on the panel use this method
	 * 
	 * @param e the action event
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		System.out.println("Action Listener");
		if (e.getSource() == preset) {
			if ("Work 6:30am-2:00pm".equals(preset.getSelectedItem())) {
				eventTextField.setText("Work");
				startTextField.setText("6:30am");
				endTextField.setText("2:00pm");
				preset.setSelectedIndex(0);
			}
			if ("Work 2:00pm-8:30pm".equals(preset.getSelectedItem())) {
				eventTextField.setText("Work");
				startTextField.setText("2:00pm");
				endTextField.setText("8:30pm");
				preset.setSelectedIndex(0);
			}
		} else if (e.getSource() == loadCalendar) {
			System.out.println("file tried");
			try {
				manager.loadCalendarByName(getFileName(true));
				System.out.println("file opened");
			} catch (IllegalArgumentException ia) {
				JOptionPane.showMessageDialog(screen, "Unable to load file.");
			} catch (IllegalStateException is) {
				// nothing
				System.out.println("state");
			}
		} else if (e.getSource() == editSettings) {
			try {
				ScreenState[] windowModes = { ScreenState.Windowed, ScreenState.Fullscreen, ScreenState.Borderless };
				JComboBox<ScreenState> combo = new JComboBox<>(windowModes);
				if (windowState.equals(ScreenState.Windowed)) {
					combo.setSelectedIndex(0);
				} else if (windowState.equals(ScreenState.Fullscreen)) {
					combo.setSelectedIndex(1);
				} else
					combo.setSelectedIndex(2);
				JPanel windowPanel = new JPanel(new GridLayout(0, 1));
				windowPanel.add(new JLabel("Window Mode"));
				windowPanel.add(combo);
				int test = JOptionPane.showConfirmDialog(screen, windowPanel, "Test", JOptionPane.OK_CANCEL_OPTION,
						JOptionPane.PLAIN_MESSAGE);
				System.out.println("Something");
				System.out.println(test);
				System.out.println(combo.getSelectedItem());
				if (test == -1 || test == 2) {
					System.out.println("Cancelled or Closed");
				} else {
					if (windowState != combo.getSelectedItem()) {
						settingsChanged = true;
						changeWindow((ScreenState) combo.getSelectedItem());
					}
					manager.saveSettings(manager.defaultSettings()[0], windowState);
					System.out.println(windowState);
				}
			} catch (IllegalArgumentException ia) {
				// Nothing
				System.out.println("IllegalArgument");
			} catch (IllegalStateException is) {
				// nothing
				System.out.println("IllegalState");
			}
			System.gc();
		} else if (e.getSource() == quit) {
			System.exit(0);
		} else if (e.getSource() == addAll) {
			for (int i = 0; i < 366; i++) {
				if (datePanel[i] != null && datePanel[i].size() > 0) {
					datePanel[i].addAllButtons();
				}
			}
			screen.setVisible(true);
		} else if (e.getSource() == removeAll) {
			// deleteAllEvents();
			for (int i = 0; i < 366; i++) {
				if (datePanel[i].size() > 0) {
					datePanel[i].removeAllButtons();
				}
			}
			screen.setVisible(true);
		} else if (e.getSource() == lastYear) {
			try {
				manager.loadCalendar(-1);
				yearOfCalendar--;
				panel = new JPanel(new GridLayout(54, 7, 1, 1));
				panel.setBorder(BorderFactory.createEtchedBorder());
				buildButtonDays();
				resetScrollPan();
				System.gc();
				monthOfCalendar = 11;
				if (Calendar.getInstance().get(Calendar.YEAR) == yearOfCalendar) {
					monthOfCalendar = Calendar.getInstance().get(Calendar.MONTH);
				}
				month.setText("<html>" + ALLMONTHNAMES[monthOfCalendar] + " " + yearOfCalendar + "</html>");
				screen.setVisible(true);
				scrollFrame.getVerticalScrollBar().setValue(scrollFrame.getVerticalScrollBar().getMaximum());
				if (Calendar.getInstance().get(Calendar.YEAR) == yearOfCalendar) {
					scrollFrame.getVerticalScrollBar().setValue(
							(int) (((double) (scrollFrame.getVerticalScrollBar().getMaximum()) / (double) totalWeeks)
									* ((double) (startWeekPerMonth[monthOfCalendar] - 1) - .3)));
				}
			} catch (Exception e2) {
				throw new IllegalArgumentException(e2.getMessage());
			}
		} else if (e.getSource() == nextYear) {
			try {
				manager.loadCalendar(1);
				yearOfCalendar++;
				deleteAllEvents();
				panel = new JPanel(new GridLayout(54, 7, 1, 1));
				panel.setBorder(BorderFactory.createEtchedBorder());
				buildButtonDays();
				resetScrollPan();
				System.gc();
				monthOfCalendar = 0;
				if (Calendar.getInstance().get(Calendar.YEAR) == yearOfCalendar) {
					monthOfCalendar = Calendar.getInstance().get(Calendar.MONTH);
				}
				month.setText("<html>" + ALLMONTHNAMES[monthOfCalendar] + " " + yearOfCalendar + "</html>");
				screen.setVisible(true);
				scrollFrame.getVerticalScrollBar().setValue(0);
				if (Calendar.getInstance().get(Calendar.YEAR) == yearOfCalendar) {
					scrollFrame.getVerticalScrollBar().setValue(
							(int) (((double) (scrollFrame.getVerticalScrollBar().getMaximum()) / (double) totalWeeks)
									* ((double) (startWeekPerMonth[monthOfCalendar] - 1) - .3)));
				}
			} catch (Exception e2) {
				throw new IllegalArgumentException(e2.getMessage());
			}
		} else {
			for (int i = 0; i < 366; i++) {
				if (e.getSource() == buttons[i]) {
					System.out.println("Button Listener");
					JLabel pre = new JLabel("Preset Event");
					JLabel lab1 = new JLabel("Event Name");
					eventTextField = new JTextField();
					JLabel lab2 = new JLabel("Start Time");
					startTextField = new JTextField();
					JLabel lab3 = new JLabel("End Time");
					endTextField = new JTextField();
					JPanel pan = new JPanel();
					// pan.setSize(new Dimension(500, 500));
					pan.setLayout(new GridLayout(4, 2, 0, 0));
					pan.add(pre);
					pan.add(preset);
					pan.add(lab1);
					pan.add(eventTextField);
					pan.add(lab2);
					pan.add(startTextField);
					pan.add(lab3);
					pan.add(endTextField);
					boolean tryEvent = true;
					while (tryEvent) {
						tryEvent = false;
						int optionSelected = JOptionPane.showConfirmDialog(screen, pan, "Add Event",
								JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
						System.out.println(optionSelected);
						if (optionSelected == 0) {
							try {
								System.out.println(yearOfCalendar);
								EventData newEvent = manager.createEvent((String) eventTextField.getText(),
										(String) startTextField.getText(), (String) endTextField.getText(),
										datePanel[i].getDay(), datePanel[i].getMonth(), datePanel[i].getYear(),
										eventColor.getRed(), eventColor.getGreen(), eventColor.getBlue());
								datePanel[i].addButton(newEvent.getStartTime(), newEvent.getStartInt(),
										newEvent.getEndTime(), datePanel[i].getDay(), datePanel[i].getMonth(),
										datePanel[i].getYear(), newEvent.getName(), newEvent.getColor().getRed(),
										newEvent.getColor().getGreen(), newEvent.getColor().getBlue());
								screen.setVisible(true);
								screen.repaint();
								screen.validate();
								return;
							} catch (Exception e1) {
								JOptionPane.showMessageDialog(screen, e1.getMessage());
								tryEvent = true;
							}
						}
					}
				}
			}

		}
		screen.repaint();
		screen.validate();
	}

	/**
	 * Panel for holding all Calendar events for an individual date. The panel
	 * allows for adding, editing, and removing DateButtons. DateButtons are stored
	 * as a LinkedList with a sentinel head pointer.
	 * 
	 * @author Caleb Kolb
	 */
	public static class DatePanel implements ActionListener {
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
		 * @param day   the day of the event
		 * @param month the moneth of the event
		 * @param year  the year of the event
		 */
		public DatePanel(int day, int month, int year) {
			this.day = day;
			this.month = month;
			this.year = year;
			head = new DateButton(null, 0, null, day, month, year, null, 0, 0, 0);
			panel = new JPanel();
			panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
			panel.setBackground(null);
			presets = new JComboBox<>(getPresetEvents());
			presets.setSelectedIndex(-1);
			presets.addActionListener(this);
			presets.setFocusable(false);
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
		public void addButton(String start, int startTime, String end, int day, int month, int year, String event,
				int red, int green, int blue) {
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
		public void editButton(String start, int originalStartTime, int startTime, String end, int day, int month,
				int year, String event, int red, int green, int blue) {
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
				System.out.println("This thing is working");
				if ("Work 6:30am-2:00pm".equals(presets.getSelectedItem())) {
					jtf1.setText("Work");
					jtf2.setText("6:30am");
					jtf3.setText("2:00pm");
					presets.setSelectedIndex(-1);
				}
				if ("Work 2:00pm-8:30pm".equals(presets.getSelectedItem())) {
					jtf1.setText("Work");
					jtf2.setText("2:00pm");
					jtf3.setText("8:30pm");
					presets.setSelectedIndex(-1);
				}
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

						JColorBox jcb = new JColorBox(colorOptions);
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
						// pan.add(colors);
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
							Color selectedColor = new Color(eventColor.getRed(), eventColor.getGreen(),
									eventColor.getBlue());
							int optionSelected = JOptionPane.showOptionDialog(getScreen(), pan, "Edit Event",
									JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null, choices,
									choices[0]);
							System.out.println(optionSelected);
							try {
								selectedColor = colorOptions.getColors()[jcb.getSelectedIndex()];
							} catch (Exception e2) {
								selectedColor = new Color(eventColor.getRed(), eventColor.getGreen(),
										eventColor.getBlue());
							}
							if (optionSelected == 0) {
								try {
									EventData newEvent = getManager().editEvent(
											(double) (year + (((month * 31) + (day)) * .001)), current.getStartTime(),
											jtf1.getText(), jtf2.getText(), jtf3.getText(), getDay(), getMonth(),
											getYear(), selectedColor.getRed(), selectedColor.getGreen(),
											selectedColor.getBlue());
									editButton(newEvent.getStartTime(), current.getStartTime(), newEvent.getStartInt(),
											newEvent.getEndTime(), getDay(), getMonth(), getYear(), newEvent.getName(),
											newEvent.getColor().getRed(), newEvent.getColor().getGreen(),
											newEvent.getColor().getBlue());
									screen.setVisible(true);
									getScreen().repaint();
									getScreen().validate();
									return;
								} catch (Exception e1) {
									jtf2.setText(current.start);
									jtf3.setText(current.end);
									JOptionPane.showMessageDialog(screen, e1.getMessage());
									tryEvent = true;
								}
							} else if (optionSelected == 1) {
								try {
									getManager().removeEvent((double) (year + (((month * 31) + (day)) * .001)),
											current.getStartTime());
									removeButton(current.getStartTime());
									screen.setVisible(true);
									screen.repaint();
									screen.validate();
									return;
								} catch (Exception e2) {
									JOptionPane.showMessageDialog(screen, e2.getMessage());
									tryEvent = true;
								}
							}
						}
					}
					current = current.next;
				}
			}
		}
	}

	/**
	 * Events are stored as button that can be clicked on to edit them. This class
	 * holds information about each event and is used by the DatePanel class
	 * 
	 * @author Caleb Kolb
	 */
	public static class DateButton {
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

	/**
	 * Returns the manager of the calendar
	 * 
	 * @return manager the manager of the calendar
	 */
	public static CalendarManager getManager() {
		return manager;
	}

	/**
	 * Returns the JFrame of the screen
	 * 
	 * @return screen the screen of the JFrame of the calendar
	 */
	public static JFrame getScreen() {
		return screen;
	}

	/**
	 * Returns an array of date panels
	 * 
	 * @return datePanel the date panel of the calendar
	 */
	public static DatePanel[] getDatePanel() {
		return datePanel;
	}

	/**
	 * Class used for sorting and geting color information presets from file
	 * 
	 * @author Caleb Kolb
	 */
	private class ColorData {
		/** Holds an array of colors */
		private Color[] colors;
		/** Sorting method to use */
		private String sortHeuristic;
		/** Size of the colors array */
		private int size;

		public ColorData(String sort, String file) {
			setHeuristic(sort);
			try {
				new File(file).createNewFile();
				Scanner scanner = new Scanner(new File(file));
				size = scanner.nextInt();
				System.out.println(size);
				colors = new Color[size];
				int counter = 0;
				scanner.nextLine();
				while (scanner.hasNextLine() && scanner.hasNext()) {
					scanner.useDelimiter("[\\n,]+");
					int red = scanner.nextInt();
					int green = scanner.nextInt();
					int blue = scanner.nextInt();
					System.out.println(red + " " + green + " " + blue);
					if (scanner.hasNextLine()) {
						scanner.nextLine();
					}
					colors[counter] = new Color(red, green, blue);
					counter++;
				}
				scanner.close();
			} catch (Exception e) {
				throw new IllegalArgumentException(e.getMessage());
			}
		}

		public void addColor(Color newColor) {
			size++;
			Color[] newColors = new Color[size];
			if ("mtf".equals(sortHeuristic)) {
				newColors[0] = newColor;
				for (int i = 0; i < size - 1; i++) {
					newColors[i + 1] = colors[i];
				}
			} else {
				newColors[size - 1] = newColor;
				for (int i = 0; i < size - 1; i++) {
					newColors[i] = colors[i];
				}
			}
			colors = newColors;
		}

		public void setHeuristic(String heur) {
			sortHeuristic = heur;
		}

		public Color[] getColors() {
			return colors;
		}

		public int getSize() {
			return size;
		}
	}

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
	private static class JColorBox extends JComboBox {

		/**
		 * Serial VersionUID
		 */
		private static final long serialVersionUID = -6382644066715104691L;

		@SuppressWarnings("unchecked")
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

	/**
	 * Code is used for getting a file using a JFileChooser
	 * 
	 * @param load the file being loaded or saved
	 * @return String the file name
	 */
	private String getFileName(boolean load) {
		JFileChooser fchooser = new JFileChooser("C:\\Users\\Caleb\\Documents\\CalendarData");
		fchooser.setBackground(Color.DARK_GRAY);
		fchooser.setForeground(Color.WHITE);
		int returnVal = Integer.MIN_VALUE;
		if (load) {
			returnVal = fchooser.showOpenDialog(screen);
		} else {
			returnVal = fchooser.showSaveDialog(screen);
		}
		if (returnVal != JFileChooser.APPROVE_OPTION) {
			throw new IllegalStateException();
		}
		File fileName = fchooser.getSelectedFile();
		return fileName.getAbsolutePath();
	}
}