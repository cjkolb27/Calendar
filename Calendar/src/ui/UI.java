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
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.io.File;
import java.util.Calendar;
import java.util.Iterator;

import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
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
import util.ColorData;
import util.DatePanel;
import util.JColorBox;
import util.PresetData;
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
	/** Buttons for every calendar day */
	private JButton[] buttons;
	/** Add button for west panel */
	private JButton add;
	/** Delete button for west panel */
	private JButton delete;
	/** New event button */
	private JButton newEventBut;
	/** Presets button */
	private JButton presetsBut;
	/** Colors buttons */
	private JButton colorsBut;
	/** Previous year button */
	private JButton lastYear;
	/** Next year button */
	private JButton nextYear;
	/** All presets of an event */
	private JComboBox<String> preset;
	/** Color combo box */
	private JColorBox jcb;
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
	/** All preset options */
	private static PresetData presetOptions;
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

	/**
	 * Creates the GUI for the user to interact with the CalendarManager
	 */
	public UI() {
		super();
		colorOptions = new ColorData("mtf", System.getProperty("user.home") + File.separator + "Documents"
				+ File.separator + "CalendarData" + File.separator + "Colors.txt");
		presetOptions = new PresetData(System.getProperty("user.home") + File.separator + "Documents" + File.separator
				+ "CalendarData" + File.separator + "Presets.txt");
		presetEvents = new String[] { "Work 6:30am-2:00pm", "Work 2:00pm-8:30pm" };
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
			boolean tracker = false;
			for (int i = 0; i < 42; i++) {
				if (i >= startDayPerMonth[month] - 1 && startDayPerMonth[month] - 2 + daysPerMonth[month] >= i) {
					tracker = true;
				} else
					tracker = false;
				if (!tracker) {
					if (month % 2 == 0) {
						smallMonthButtons[i].setBackground(evenMonthColor);
					} else
						smallMonthButtons[i].setBackground(oddMonthColor);
					smallMonthButtons[i].setText("");
					smallMonthButtons[i].setFont(new Font("Comic Sans", Font.BOLD, 15));
				} else {
					smallMonthButtons[i].setText((i - startDayPerMonth[month] + 2) + "");
					if ((month + 1) % 2 == 0) {
						smallMonthButtons[i].setBackground(evenMonthColor);
					} else
						smallMonthButtons[i].setBackground(oddMonthColor);
					smallMonthButtons[i].setFont(new Font("Comic Sans", Font.BOLD, 15));
					smallMonthButtons[i].setForeground(Color.WHITE);
				}
				smallCalendar.add(smallMonthButtons[i]);
			}
		} else {
			smallCalendar = new JPanel();
			smallCalendar.setLayout(new GridLayout(6, 7, 1, 1));
			smallCalendar.setMaximumSize(new Dimension(218, 191));
			smallMonthButtons = new JButton[42];
			boolean tracker = false;
			for (int i = 0; i < 42; i++) {
				if (i >= startDayPerMonth[month] - 1 && startDayPerMonth[month] - 2 + daysPerMonth[month] >= i) {
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

		Iterator<EventData> it = sdl.iterator();
		EventData currentData;
		if (it.hasNext()) {
			currentData = it.next();
		} else {
			currentData = null;
		}

		int currentDay = 0;
		int currentWeek = 0;
		for (int i = 0; i < 12; i++) {
			cal.set(yearOfCalendar, i, 1);
			startDayPerMonth[i] = cal.get(Calendar.DAY_OF_WEEK);
			int days = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
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
				System.out.println("Set Days in Feb to 28");
				buttons[currentDay] = new JButton();
				buttons[currentDay].addActionListener(this);
				currentDay++;
			} else if (i == 1) {
				daysPerMonth[1] = 29;
				System.out.println("Set Days in Feb to 29");
			}
			int weekCount = 0;
			startWeekPerMonth[i] = 0;

			for (int j = 0; j < days; j++) {
				cal.set(yearOfCalendar, i, j + 1);
				if (cal.get(Calendar.DAY_OF_WEEK) == 7 && weekCount < 3) {
					if (weekCount == 2) {
						transitionWeek[i] = currentDay;
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
				if ((i + 1) % 2 == 0) {
					buttons[currentDay].setBackground(evenMonthColor);
				} else
					buttons[currentDay].setBackground(oddMonthColor);
				datePanel[currentDay] = new DatePanel((j + 1), (i + 1), yearOfCalendar, this);
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
	}

	private void setScreenWindow() {
		// System.out.println("Setting something");
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

		loadCalendar.addActionListener(this);
		loadCalendar.setBackground(Color.DARK_GRAY);
		loadCalendar.setForeground(Color.WHITE);
		editSettings.addActionListener(this);
		editSettings.setBackground(Color.DARK_GRAY);
		editSettings.setForeground(Color.WHITE);
		quit.addActionListener(this);
		quit.setBackground(Color.DARK_GRAY);
		quit.setForeground(Color.WHITE);

		menu.add(loadCalendar);
		menu.add(editSettings);
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
		if (e.getSource() == preset) {
			System.out.println("Preset " + preset.getSelectedItem());
			eventTextField.setText(presetOptions.getPresets()[preset.getSelectedIndex()].getName());
			startTextField.setText(presetOptions.getPresets()[preset.getSelectedIndex()].getStart());
			endTextField.setText(presetOptions.getPresets()[preset.getSelectedIndex()].getEnd());
			preset.setSelectedIndex(-1);
		} else if (e.getSource() == jcb) {
			System.out.println("Changed Color");
			colorOptions.moveToFront((Color) jcb.getSelectedItem());
			jcb.updateBox(colorOptions);
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
		} else if (e.getSource() == newEventBut) {
			System.out.println("New Event Button");
			screen.setVisible(true);
		} else if (e.getSource() == presetsBut) {
			System.out.println("Presets Button");
			
			screen.setVisible(true);
		} else if (e.getSource() == colorsBut) {
			System.out.println("Color Button");
			JPanel pan = new JPanel();
			pan.setPreferredSize(new Dimension(150, 100));
			SpringLayout layout = new SpringLayout();
			pan.setLayout(layout);

			jcb = new JColorBox(getColorOptions());
			jcb.updateBox(colorOptions);
			jcb.setSelectedIndex(0);
			// jcb.setBounds(100, 20, 140, 30);
			jcb.setPreferredSize(new Dimension(70, 40));
			jcb.setFocusable(false);
			jcb.setBorder(BorderFactory.createLineBorder((Color) jcb.getSelectedItem(), 200));
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

			JLabel col = new JLabel("Event Color");
			JLabel lab1 = new JLabel("Add/Delete");
			add = new JButton("+");
			add.setPreferredSize(new Dimension(40, 40));
			add.setFont(new Font("Comic Sans", Font.BOLD, 25));
			add.addActionListener(this);
			add.setMargin(new Insets(0, 0, 0, 0));
			delete = new JButton("-");
			delete.setPreferredSize(new Dimension(40, 40));
			delete.setFont(new Font("Comic Sans", Font.BOLD, 25));
			delete.addActionListener(this);
			delete.setMargin(new Insets(0, 0, 0, 0));

			pan.add(col);
			pan.add(lab1);
			layout.putConstraint(SpringLayout.WEST, col, 5, SpringLayout.WEST, pan);
			layout.putConstraint(SpringLayout.NORTH, col, 5, SpringLayout.NORTH, pan);
			layout.putConstraint(SpringLayout.WEST, lab1, 14, SpringLayout.EAST, col);
			layout.putConstraint(SpringLayout.NORTH, lab1, 5, SpringLayout.NORTH, pan);

			pan.add(jcb);
			pan.add(add);
			pan.add(delete);
			layout.putConstraint(SpringLayout.WEST, jcb, 5, SpringLayout.WEST, pan);
			layout.putConstraint(SpringLayout.NORTH, jcb, 30, SpringLayout.NORTH, col);
			layout.putConstraint(SpringLayout.WEST, add, 5, SpringLayout.EAST, jcb);
			layout.putConstraint(SpringLayout.NORTH, add, 30, SpringLayout.NORTH, lab1);
			layout.putConstraint(SpringLayout.WEST, delete, 5, SpringLayout.EAST, add);
			layout.putConstraint(SpringLayout.NORTH, delete, 30, SpringLayout.NORTH, lab1);

			boolean tryEvent = true;
			while (tryEvent) {
				tryEvent = false;
				int optionSelected = JOptionPane.showConfirmDialog(screen, pan, "Add Event", JOptionPane.CLOSED_OPTION,
						JOptionPane.PLAIN_MESSAGE);
				System.out.println(optionSelected);
				if (optionSelected == 0) {
					try {
						screen.setVisible(true);
						screen.repaint();
						screen.validate();
						add.removeActionListener(this);
						delete.removeActionListener(this);
						return;
					} catch (Exception e1) {
						JOptionPane.showMessageDialog(screen, e1.getMessage());
						tryEvent = true;
					}
				}
				add.removeActionListener(this);
				delete.removeActionListener(this);
			}
			screen.setVisible(true);
		} else if (e.getSource() == add) {
			new JColorChooser();
			Color jcc = JColorChooser.showDialog(this, "Color Selector", panelColor);
			System.out.println(jcc);
			if (jcc != null) {
				colorOptions.addColor(jcc);
				jcb.updateBox(colorOptions);
				System.out.println("Try Adding");
			}
		} else if (e.getSource() == delete) {
			colorOptions.removeColor((Color) jcb.getSelectedItem());
			jcb.updateBox(colorOptions);
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
				createSmallCalendar(monthOfCalendar);
				if (Calendar.getInstance().get(Calendar.YEAR) == yearOfCalendar) {
					monthOfCalendar = Calendar.getInstance().get(Calendar.MONTH);
				}
				month.setText("<html>" + ALLMONTHNAMES[monthOfCalendar] + " " + yearOfCalendar + "</html>");
				monthSmall.setText(ALLMONTHNAMES[monthOfCalendar] + " " + yearOfCalendar);
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
				createSmallCalendar(monthOfCalendar);
				if (Calendar.getInstance().get(Calendar.YEAR) == yearOfCalendar) {
					monthOfCalendar = Calendar.getInstance().get(Calendar.MONTH);
				}
				month.setText("<html>" + ALLMONTHNAMES[monthOfCalendar] + " " + yearOfCalendar + "</html>");
				monthSmall.setText(ALLMONTHNAMES[monthOfCalendar] + " " + yearOfCalendar);
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
					System.out.println("Button Listener " + i);
					int textFieldSizeX = 151;
					int textFieldSizeZ = 20;

					JLabel col = new JLabel("Event Color");
					JLabel pre = new JLabel("Preset Event");
					JLabel lab1 = new JLabel("Event Name");
					eventTextField = new JTextField();
					eventTextField.setPreferredSize(new Dimension(textFieldSizeX, textFieldSizeZ));
					JLabel lab2 = new JLabel("Start Time");
					startTextField = new JTextField();
					startTextField.setPreferredSize(new Dimension(textFieldSizeX, textFieldSizeZ));
					JLabel lab3 = new JLabel("End Time");
					endTextField = new JTextField();
					endTextField.setPreferredSize(new Dimension(textFieldSizeX, textFieldSizeZ));
					JPanel pan = new JPanel();
					pan.setPreferredSize(new Dimension(100, 150));
					SpringLayout layout = new SpringLayout();
					pan.setLayout(layout);

					jcb = new JColorBox(getColorOptions());
					jcb.setSelectedIndex(0);
					jcb.setPreferredSize(new Dimension(30, 20));
					jcb.setFocusable(false);
					jcb.setBorder(BorderFactory.createLineBorder((Color) jcb.getSelectedItem(), 200));
					jcb.setForeground(null);
					jcb.addActionListener(this);

					Component[] c = jcb.getComponents();
					for (Component res : c) {
						System.out.println("Component: " + res);
						if (res instanceof AbstractButton && res.isVisible()) {
							res.setVisible(false);
						}
					}

					preset = new JComboBox<>(presetOptions.getStringPresets());
					preset.setSelectedIndex(-1);
					preset.setFocusable(false);
					preset.addActionListener(this);

					pan.add(col);
					pan.add(jcb);
					layout.putConstraint(SpringLayout.WEST, col, 5, SpringLayout.WEST, pan);
					layout.putConstraint(SpringLayout.NORTH, col, 5, SpringLayout.NORTH, pan);
					layout.putConstraint(SpringLayout.WEST, jcb, 14, SpringLayout.EAST, col);
					layout.putConstraint(SpringLayout.NORTH, jcb, 5, SpringLayout.NORTH, pan);

					pan.add(pre);
					pan.add(preset);
					layout.putConstraint(SpringLayout.WEST, pre, 5, SpringLayout.WEST, pan);
					layout.putConstraint(SpringLayout.NORTH, pre, 30, SpringLayout.NORTH, col);
					layout.putConstraint(SpringLayout.WEST, preset, 5, SpringLayout.EAST, pre);
					layout.putConstraint(SpringLayout.NORTH, preset, 30, SpringLayout.NORTH, jcb);

					pan.add(lab1);
					pan.add(eventTextField);
					layout.putConstraint(SpringLayout.WEST, lab1, 5, SpringLayout.WEST, pan);
					layout.putConstraint(SpringLayout.NORTH, lab1, 30, SpringLayout.NORTH, pre);
					layout.putConstraint(SpringLayout.WEST, eventTextField, 10, SpringLayout.EAST, lab1);
					layout.putConstraint(SpringLayout.NORTH, eventTextField, 30, SpringLayout.NORTH, preset);

					pan.add(lab2);
					pan.add(startTextField);
					layout.putConstraint(SpringLayout.WEST, lab2, 5, SpringLayout.WEST, pan);
					layout.putConstraint(SpringLayout.NORTH, lab2, 24, SpringLayout.NORTH, lab1);
					layout.putConstraint(SpringLayout.WEST, startTextField, 18, SpringLayout.EAST, lab2);
					layout.putConstraint(SpringLayout.NORTH, startTextField, 24, SpringLayout.NORTH, eventTextField);

					pan.add(lab3);
					pan.add(endTextField);
					layout.putConstraint(SpringLayout.WEST, lab3, 5, SpringLayout.WEST, pan);
					layout.putConstraint(SpringLayout.NORTH, lab3, 24, SpringLayout.NORTH, lab2);
					layout.putConstraint(SpringLayout.WEST, endTextField, 25, SpringLayout.EAST, lab3);
					layout.putConstraint(SpringLayout.NORTH, endTextField, 24, SpringLayout.NORTH, startTextField);

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
										((Color) jcb.getSelectedItem()).getRed(),
										((Color) jcb.getSelectedItem()).getGreen(),
										((Color) jcb.getSelectedItem()).getBlue());
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
	 * 
	 * @return
	 */
	public JColorBox getJCB() {
		jcb = new JColorBox(getColorOptions());
		jcb.addActionListener(this);
		return jcb;
	}

	/**
	 * Returns an array of all preset events
	 * 
	 * @return presetEvents an array of strings of all presets
	 */
	public String[] getPresetEvents() {
		return presetEvents;
	}

	/**
	 * Returns the Color data of preset color
	 * 
	 * @return colorOptions the color options
	 */
	public ColorData getColorOptions() {
		return colorOptions;
	}

	/**
	 * Returns the manager of the calendar
	 * 
	 * @return manager the manager of the calendar
	 */
	public CalendarManager getManager() {
		return manager;
	}

	/**
	 * Returns the JFrame of the screen
	 * 
	 * @return screen the screen of the JFrame of the calendar
	 */
	public JFrame getScreen() {
		return screen;
	}

	/**
	 * Returns an array of date panels
	 * 
	 * @return datePanel the date panel of the calendar
	 */
	public DatePanel[] getDatePanel() {
		return datePanel;
	}

	/**
	 * Returns presetOption
	 * 
	 * @return presetOption the options of preset events
	 */
	public PresetData getPresetOptions() {
		return presetOptions;
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