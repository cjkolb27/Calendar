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
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.io.File;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Iterator;

import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFormattedTextField;
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
import javax.swing.UIManager;
import javax.swing.border.LineBorder;
import javax.swing.plaf.ColorUIResource;
import javax.swing.text.NumberFormatter;

import events.EventData;
import manager.CalendarManager;
import util.ColorData;
import util.DatePanel;
import util.JColorBox;
import util.PresetData;
import util.PresetStateMachine;
import util.PresetStateMachine.PresetState;
import util.SortedDateList;

/**
 * GUI for displaying the Calendar to the user.
 * 
 * @author Caleb Kolb
 */
public class UI extends JFrame implements ActionListener, MouseWheelListener, ItemListener {

	/** Default Serial Version UID */
	private static final long serialVersionUID = 1L;
	/** Settings window state */
	private ScreenState windowState = null;
	/** Used for determining which state the presets are in */
	private PresetStateMachine presetState;
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
	/** Panel for holding days on new event panel */
	private JPanel dayPanel;
	/** Scrolling panel */
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
	/** Holds the first days of each week for each month */
	private static String[] monthAndDay;
	/** Font for all JTextFields, JComboBoxes, and JLabels */
	private static final Font TEXTFIELDFONT = new Font("Comic Sans", Font.PLAIN, 18);
	/** Days per month */
	private int[] daysPerMonth = { 31, 29, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 };
	/** Weeks per month */
	private int[] startWeekPerMonth = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
	/** Position of week that changes month JLabel */
	private int[] transitionWeek = { 31, 29, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 };
	/** Each start day of each month from 1 to 7 */
	private int[] startDayPerMonth = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
	/** Small Month Buttons */
	private JButton[] smallMonthButtons;
	/** Buttons for every calendar day */
	private JButton[] buttons;
	/** Add button for west panel */
	private JButton addColorBut;
	/** Delete button for west panel */
	private JButton deleteColorBut;
	/** Add button for adding presets */
	private JButton addPresetBut;
	/** Delete button for deleting presets */
	private JButton deletePresetBut;
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
	/** Used for saving and updating presets */
	private JButton savePreset;
	/** Used for canceling preset options */
	private JButton cancelPreset;
	/** Check box for no repeats */
	private JCheckBox noRepCB;
	/** Check box for Sunday repeats */
	private JCheckBox sunCB;
	/** Check box for Monday repeats */
	private JCheckBox monCB;
	/** Check box for Tuesday repeats */
	private JCheckBox tueCB;
	/** Check box for Wednesday repeats */
	private JCheckBox wedCB;
	/** Check box for Thursday repeats */
	private JCheckBox thuCB;
	/** Check box for Friday repeats */
	private JCheckBox friCB;
	/** Check box for Saturday repeats */
	private JCheckBox satCB;
	/** Check box for specific repeats */
	private JCheckBox speCB;
	/** All presets of an event */
	private JComboBox<String> preset;
	/** No Answer Day date */
	private JTextField naDay;
	/** No Answer Month date */
	private JComboBox<String> naMonth;
	/** Start Week */
	private JComboBox<String> startWeek;
	/** End Week */
	private JComboBox<String> endWeek;
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
	private static Color scrollPanelColor = new Color(20, 20, 20);
	/** Odd month color */
	private static Color oddMonthColor = new Color(38, 38, 38);
	/** Even month color */
	private static Color evenMonthColor = new Color(26, 26, 26, 255);
	/** Event color */
	private static Color eventColor = new Color(255, 153, 161);
	/** All other panels in calendar color */
	private static Color panelColor = Color.DARK_GRAY;
	/** Color of text */
	private static Color textColor = Color.WHITE;
	/** Option Panel Color */
	private static Color optionPaneColor = new Color(30, 30, 30);
	/** File menu title */
	private static final String FILE_MENU_TITLE = "File";
	/** Load calendar title */
	private static final String LOADCAL = "Load Calendar";
	/** Edit Settings title */
	private static final String EDITSET = "Edit Settings";
	/** Label for holding the month name */
	private JLabel monthLabel;
	/** Label for holding the month name */
	private JLabel monthSmallLabel;
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
	/** Determining if the preset is coming from the preset event menu */
	private boolean presetEventMenu;

	/**
	 * Creates the GUI for the user to interact with the CalendarManager
	 */
	public UI() {
		super();
		// UIManager UI = new UIManager();
		UIManager.put("OptionPane.background", new ColorUIResource(optionPaneColor));
		UIManager.put("Panel.background", optionPaneColor);
		presetEventMenu = false;
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
		screen.setBackground(scrollPanelColor);

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
		scrollFrame.setBackground(scrollPanelColor);
		scrollFrame.setBorder(BorderFactory.createEtchedBorder());
		scrollFrame.getVerticalScrollBar().setUnitIncrement(20);
		scrollFrame.getVerticalScrollBar().setPreferredSize(new Dimension(0, 0));
		scrollFrame.addMouseWheelListener(this);

		northPanel = new JPanel(new GridLayout(2, 1, 0, 0));
		northPanel.setLayout(new BoxLayout(northPanel, BoxLayout.PAGE_AXIS));
		westPanel = new JPanel();
		northPanel.setBackground(panelColor);
		westPanel.setBackground(panelColor);
		northPanel.setPreferredSize(new Dimension(1, 100));
		westPanel.setPreferredSize(new Dimension(265, 1));
		monthLabel = new JLabel("<html>" + ALLMONTHNAMES[monthOfCalendar] + " " + yearOfCalendar + "</html>");
		monthLabel.setFont(new Font("Comic Sans", Font.BOLD, 40));
		monthLabel.setForeground(textColor);
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
				lastYear.setForeground(textColor);
				lastYear.setBorderPainted(false);
				lastYear.setFocusable(false);
				lastYear.setPreferredSize(new Dimension(40, 40));
				lastYear.setMargin(new Insets(0, 0, 0, 0));
				yearSelecter.add(lastYear);
				northPanel.add(space);
			} else if (i == 3) {
				yearSelecter.add(monthLabel);
				northPanel.add(yearSelecter);
			} else if (i == 4) {
				nextYear = new JButton("<html>" + "\u2192" + "</html>");
				nextYear.addActionListener(this);
				nextYear.setFont(new Font("Comic Sans", Font.BOLD, 40));
				nextYear.setBackground(null);
				nextYear.setForeground(textColor);
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
			weekDay.setForeground(textColor);
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
		monthSmallLabel = new JLabel(ALLMONTHNAMES[monthOfCalendar] + " " + yearOfCalendar);
		monthSmallLabel.setFont(new Font("Comic Sans", Font.BOLD, 20));
		monthSmallLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		monthSmallLabel.setForeground(textColor);
		westPanel.add(monthSmallLabel);
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
					smallMonthButtons[i].setEnabled(false);
				} else {
					smallMonthButtons[i].setText((i - startDayPerMonth[month] + 2) + "");
					if ((month + 1) % 2 == 0) {
						smallMonthButtons[i].setBackground(evenMonthColor);
					} else
						smallMonthButtons[i].setBackground(oddMonthColor);
					smallMonthButtons[i].setFont(new Font("Comic Sans", Font.BOLD, 15));
					smallMonthButtons[i].setForeground(textColor);
					smallMonthButtons[i].setEnabled(true);
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
					smallMonthButtons[i].setEnabled(false);
					smallMonthButtons[i].addActionListener(this);
				} else {
					smallMonthButtons[i] = new JButton((i - startDayPerMonth[month] + 2) + "");
					if ((month + 1) % 2 == 0) {
						smallMonthButtons[i].setBackground(evenMonthColor);
					} else
						smallMonthButtons[i].setBackground(oddMonthColor);
					smallMonthButtons[i].setMargin(new Insets(0, 0, 0, 0));
					smallMonthButtons[i].setFocusable(false);
					smallMonthButtons[i].setFont(new Font("Comic Sans", Font.BOLD, 15));
					smallMonthButtons[i].setForeground(textColor);
					smallMonthButtons[i].setPreferredSize(new Dimension(30, 30));
					smallMonthButtons[i].addActionListener(this);
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
		scrollFrame.setBackground(scrollPanelColor);
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
			System.out.println(startDayPerMonth[i]);
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
					label.setForeground(textColor);
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
		panel.setBackground(scrollPanelColor);

		monthAndDay = new String[totalWeeks + 1];
		Calendar cal2 = Calendar.getInstance();
		cal2.set(yearOfCalendar, 11, 31);
		if (cal2.get(Calendar.DAY_OF_WEEK) == 1) {
			monthAndDay = new String[totalWeeks];
		} else {
			monthAndDay[totalWeeks] = "Dec/31";
		}
		monthAndDay[0] = "<html>" + "Jan/1" + "</html>";
		cal2.set(yearOfCalendar, 0, 1);
		System.out.println(cal2.get(Calendar.DAY_OF_WEEK));
		int offset = cal2.get(Calendar.DAY_OF_WEEK);
		int daysCounted = 0 + (offset * -1) + 9;
		int month = 0;
		offset = 1;
		while (month < 12) {
			if (daysPerMonth[month] < daysCounted) {
				daysCounted -= daysPerMonth[month];
				month++;
			} else {
				monthAndDay[offset] = ALLMONTHNAMES[month].substring(0, 3) + "/" + daysCounted;
				System.out.println(monthAndDay[offset]);
				daysCounted += 7;
				offset++;
			}
		}
	}

	private void setScreenWindow() {
		// System.out.println("Setting something");
		presetEventMenu = false;
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
		menu.setBackground(panelColor);
		menu.setForeground(textColor);
		loadCalendar = new JMenuItem(LOADCAL);
		editSettings = new JMenuItem(EDITSET);
		quit = new JMenuItem("Quit");

		loadCalendar.addActionListener(this);
		loadCalendar.setBackground(panelColor);
		loadCalendar.setForeground(textColor);
		editSettings.addActionListener(this);
		editSettings.setBackground(panelColor);
		editSettings.setForeground(textColor);
		quit.addActionListener(this);
		quit.setBackground(panelColor);
		quit.setForeground(textColor);

		menu.add(loadCalendar);
		menu.add(editSettings);
		menu.add(quit);
		menuBar.add(menu);
		menuBar.setBackground(panelColor);
		menuBar.setForeground(textColor);
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
						monthLabel.setText("<html>" + ALLMONTHNAMES[scrollMonth] + " " + yearOfCalendar + "</html>");
						monthSmallLabel.setText(ALLMONTHNAMES[scrollMonth] + " " + yearOfCalendar);
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
						monthLabel.setText("<html>" + ALLMONTHNAMES[scrollMonth] + " " + yearOfCalendar + "</html>");
						monthSmallLabel.setText(ALLMONTHNAMES[scrollMonth] + " " + yearOfCalendar);
						createSmallCalendar(scrollMonth);
						System.gc();
						screen.setVisible(true);
						i = 12;
					}
				}
			}
			if (scrollMonth != 11 && scrollLocation == scrollHeight) {
				scrollMonth = 11;
				monthLabel.setText("<html>" + ALLMONTHNAMES[scrollMonth] + " " + yearOfCalendar + "</html>");
				monthSmallLabel.setText(ALLMONTHNAMES[scrollMonth] + " " + yearOfCalendar);
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

	@Override
	public void itemStateChanged(ItemEvent e) {
		if (e.getSource() == noRepCB) {
			System.out.println("No");
			if (noRepCB.getSelectedObjects() != null) {
				newEventNA();
				noRepCB.setEnabled(false);
				sunCB.setSelected(false);
				monCB.setSelected(false);
				tueCB.setSelected(false);
				wedCB.setSelected(false);
				thuCB.setSelected(false);
				friCB.setSelected(false);
				satCB.setSelected(false);
				speCB.setSelected(false);
			} else {
				newEventWeekDays();
			}
		} else if (e.getSource() == sunCB) {
			System.out.println("Sun");
			if (sunCB.getSelectedObjects() != null) {
				speCB.setSelected(false);
				noRepCB.setEnabled(true);
				noRepCB.setSelected(false);
			} else if (monCB.getSelectedObjects() == null && tueCB.getSelectedObjects() == null
					&& wedCB.getSelectedObjects() == null && thuCB.getSelectedObjects() == null
					&& friCB.getSelectedObjects() == null && satCB.getSelectedObjects() == null) {
				noRepCB.setSelected(true);
			}
		} else if (e.getSource() == monCB) {
			System.out.println("Mon");
			if (monCB.getSelectedObjects() != null) {
				speCB.setSelected(false);
				noRepCB.setEnabled(true);
				noRepCB.setSelected(false);
			} else if (sunCB.getSelectedObjects() == null && tueCB.getSelectedObjects() == null
					&& wedCB.getSelectedObjects() == null && thuCB.getSelectedObjects() == null
					&& friCB.getSelectedObjects() == null && satCB.getSelectedObjects() == null) {
				noRepCB.setSelected(true);
			}
		} else if (e.getSource() == tueCB) {
			System.out.println("Tue");
			if (tueCB.getSelectedObjects() != null) {
				speCB.setSelected(false);
				noRepCB.setEnabled(true);
				noRepCB.setSelected(false);
			} else if (sunCB.getSelectedObjects() == null && monCB.getSelectedObjects() == null
					&& wedCB.getSelectedObjects() == null && thuCB.getSelectedObjects() == null
					&& friCB.getSelectedObjects() == null && satCB.getSelectedObjects() == null) {
				noRepCB.setSelected(true);
			}
		} else if (e.getSource() == wedCB) {
			System.out.println("Wed");
			if (wedCB.getSelectedObjects() != null) {
				speCB.setSelected(false);
				noRepCB.setEnabled(true);
				noRepCB.setSelected(false);
			} else if (sunCB.getSelectedObjects() == null && monCB.getSelectedObjects() == null
					&& tueCB.getSelectedObjects() == null && thuCB.getSelectedObjects() == null
					&& friCB.getSelectedObjects() == null && satCB.getSelectedObjects() == null) {
				noRepCB.setSelected(true);
			}
		} else if (e.getSource() == thuCB) {
			System.out.println("Thu");
			if (thuCB.getSelectedObjects() != null) {
				speCB.setSelected(false);
				noRepCB.setEnabled(true);
				noRepCB.setSelected(false);
			} else if (sunCB.getSelectedObjects() == null && monCB.getSelectedObjects() == null
					&& tueCB.getSelectedObjects() == null && wedCB.getSelectedObjects() == null
					&& friCB.getSelectedObjects() == null && satCB.getSelectedObjects() == null) {
				noRepCB.setSelected(true);
			}
		} else if (e.getSource() == friCB) {
			System.out.println("Fri");
			if (friCB.getSelectedObjects() != null) {
				speCB.setSelected(false);
				noRepCB.setEnabled(true);
				noRepCB.setSelected(false);
			} else if (sunCB.getSelectedObjects() == null && monCB.getSelectedObjects() == null
					&& tueCB.getSelectedObjects() == null && wedCB.getSelectedObjects() == null
					&& thuCB.getSelectedObjects() == null && satCB.getSelectedObjects() == null) {
				noRepCB.setSelected(true);
			}
		} else if (e.getSource() == satCB) {
			System.out.println("Sat");
			if (satCB.getSelectedObjects() != null) {
				speCB.setSelected(false);
				noRepCB.setEnabled(true);
				noRepCB.setSelected(false);
			} else if (sunCB.getSelectedObjects() == null && monCB.getSelectedObjects() == null
					&& tueCB.getSelectedObjects() == null && wedCB.getSelectedObjects() == null
					&& thuCB.getSelectedObjects() == null && friCB.getSelectedObjects() == null) {
				noRepCB.setSelected(true);
			}
		} else if (e.getSource() == speCB) {
			System.out.println("Spe");
			System.out.println("Soemthing: " + speCB.getSelectedObjects());
			if (speCB.getSelectedObjects() != null) {
				noRepCB.setEnabled(true);
				noRepCB.removeItemListener(this);

				sunCB.setSelected(false);
				monCB.setSelected(false);
				tueCB.setSelected(false);
				wedCB.setSelected(false);
				thuCB.setSelected(false);
				friCB.setSelected(false);
				satCB.setSelected(false);

				noRepCB.addItemListener(this);
				noRepCB.setSelected(false);
				newEventCustome();
			} else {
				System.out.println("extra");
				noRepCB.removeItemListener(this);
				noRepCB.setSelected(true);
				noRepCB.setEnabled(false);
				noRepCB.addItemListener(this);
				newEventNA();
			}
		}
	}

	/**
	 * Used to set panel for no specific days selected
	 */
	public void newEventNA() {
		dayPanel.removeAll();
		dayPanel.revalidate();
		dayPanel.repaint();
		SpringLayout layout = new SpringLayout();
		dayPanel.setLayout(layout);
		dayPanel.setPreferredSize(new Dimension(479, 40));

		JLabel lab = new JLabel("Event Date");
		lab.setPreferredSize(new Dimension(100, 30));
		lab.setForeground(textColor);
		lab.setFont(TEXTFIELDFONT);
		dayPanel.add(lab);

		String[] allMonths = new String[12];
		for (int i = 0; i < 12; i++) {
			allMonths[i] = ALLMONTHNAMES[i].substring(0, 3);
		}
		naMonth = new JComboBox<>(allMonths);
		naMonth.setFont(TEXTFIELDFONT);
		naMonth.setPreferredSize(new Dimension(70, 30));
		naMonth.setSelectedIndex(scrollMonth);

		NumberFormatter formatter = new NumberFormatter();
		formatter.setValueClass(Integer.class);
		naDay = new JFormattedTextField(formatter);
		naDay.setFont(TEXTFIELDFONT);
		naDay.setPreferredSize(new Dimension(79, 30));
		naDay.setFont(TEXTFIELDFONT);
		Calendar cal = Calendar.getInstance();
		naDay.setText(cal.get(Calendar.DAY_OF_MONTH) + "");
		dayPanel.add(naMonth);
		dayPanel.add(naDay);

		layout.putConstraint(SpringLayout.WEST, lab, 115, SpringLayout.WEST, dayPanel);
		layout.putConstraint(SpringLayout.NORTH, lab, 5, SpringLayout.NORTH, dayPanel);
		layout.putConstraint(SpringLayout.WEST, naMonth, 14, SpringLayout.EAST, lab);
		layout.putConstraint(SpringLayout.NORTH, naMonth, 0, SpringLayout.NORTH, lab);
		layout.putConstraint(SpringLayout.WEST, naDay, 5, SpringLayout.EAST, naMonth);
		layout.putConstraint(SpringLayout.NORTH, naDay, 0, SpringLayout.NORTH, naMonth);
	}

	/**
	 * Used to set panel for specific days selected
	 */
	public void newEventWeekDays() {
		dayPanel.removeAll();
		dayPanel.revalidate();
		dayPanel.repaint();
		SpringLayout layout = new SpringLayout();
		dayPanel.setLayout(layout);
		dayPanel.setPreferredSize(new Dimension(479, 40));
		startWeek = new JComboBox<>(monthAndDay);
		startWeek.setFont(TEXTFIELDFONT);
		startWeek.setPreferredSize(new Dimension(90, 30));

		Calendar cal = Calendar.getInstance();
		cal.setMinimalDaysInFirstWeek(4);
		System.out.println("Week in year: " + cal.get(Calendar.WEEK_OF_YEAR));
		startWeek.setSelectedIndex(cal.get(Calendar.WEEK_OF_YEAR) - 1);

		endWeek = new JComboBox<>(Arrays.asList(monthAndDay).subList(cal.get(Calendar.WEEK_OF_YEAR), monthAndDay.length)
				.toArray(new String[0]));
		endWeek.setFont(TEXTFIELDFONT);
		endWeek.setPreferredSize(new Dimension(90, 30));

		JLabel lab = new JLabel("Event Date");
		lab.setPreferredSize(new Dimension(100, 30));
		lab.setForeground(textColor);
		lab.setFont(TEXTFIELDFONT);
		dayPanel.add(lab);
		dayPanel.add(startWeek);
		dayPanel.add(endWeek);

		layout.putConstraint(SpringLayout.WEST, lab, 115, SpringLayout.WEST, dayPanel);
		layout.putConstraint(SpringLayout.NORTH, lab, 5, SpringLayout.NORTH, dayPanel);
		layout.putConstraint(SpringLayout.WEST, startWeek, 14, SpringLayout.EAST, lab);
		layout.putConstraint(SpringLayout.NORTH, startWeek, 0, SpringLayout.NORTH, lab);
		layout.putConstraint(SpringLayout.WEST, endWeek, 0, SpringLayout.EAST, startWeek);
		layout.putConstraint(SpringLayout.NORTH, endWeek, 0, SpringLayout.NORTH, startWeek);
	}

	/**
	 * Used to set panel for custom days selected
	 */
	public void newEventCustome() {
		dayPanel.removeAll();
		dayPanel.revalidate();
		dayPanel.repaint();
		JLabel col = new JLabel("Custome thing");
		col.setPreferredSize(new Dimension(79, 20));
		col.setForeground(textColor);
		dayPanel.add(col);
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
			if (presetEventMenu) {
				System.out.println("Preset Thingy Is being Called");
				preset.removeActionListener(this);
				if (preset.getSelectedIndex() == -1) {
					preset.addActionListener(this);
					return;
				}
				if (presetState.changeState("selected")) {
					addPresetBut.setEnabled(true);
					deletePresetBut.setEnabled(true);
					eventTextField.setEnabled(true);
					startTextField.setEnabled(true);
					endTextField.setEnabled(true);
					jcb.setEnabled(true);
					savePreset.setEnabled(true);
					cancelPreset.setEnabled(true);

					System.out.println("Current State4: " + presetState.getState());
					savePreset.setText("Update");
					eventTextField.setText(presetOptions.getPresets()[preset.getSelectedIndex()].getName());
					startTextField.setText(presetOptions.getPresets()[preset.getSelectedIndex()].getStart());
					endTextField.setText(presetOptions.getPresets()[preset.getSelectedIndex()].getEnd());
					jcb.setSelectedItem(presetOptions.getPresets()[preset.getSelectedIndex()].getColor());
					preset.addActionListener(this);
				}
			} else {
				System.out.println("Preset " + preset.getSelectedItem());
				eventTextField.setText(presetOptions.getPresets()[preset.getSelectedIndex()].getName());
				startTextField.setText(presetOptions.getPresets()[preset.getSelectedIndex()].getStart());
				endTextField.setText(presetOptions.getPresets()[preset.getSelectedIndex()].getEnd());
				jcb.setSelectedItem(presetOptions.getPresets()[preset.getSelectedIndex()].getColor());
				preset.setSelectedIndex(-1);
			}
		} else if (e.getSource() == jcb) {
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
			Dimension textFieldSize = new Dimension(151, 30);
			Dimension labelSize = new Dimension(110, 30);
			Dimension checkSize = new Dimension(50, 30);

			noRepCB = new JCheckBox("<html>" + "N/A" + "</html>");
			noRepCB.setPreferredSize(checkSize);
			noRepCB.setSelected(true);
			noRepCB.setEnabled(false);
			noRepCB.addItemListener(this);
			noRepCB.setBackground(optionPaneColor);
			noRepCB.setForeground(textColor);
			sunCB = new JCheckBox("<html>" + "Sun" + "</html>");
			sunCB.setPreferredSize(checkSize);
			sunCB.addItemListener(this);
			sunCB.setBackground(optionPaneColor);
			sunCB.setForeground(textColor);
			monCB = new JCheckBox("<html>" + "Mon" + "</html>");
			monCB.setPreferredSize(checkSize);
			monCB.addItemListener(this);
			monCB.setBackground(optionPaneColor);
			monCB.setForeground(textColor);
			tueCB = new JCheckBox("<html>" + "Tue" + "</html>");
			tueCB.setPreferredSize(checkSize);
			tueCB.addItemListener(this);
			tueCB.setBackground(optionPaneColor);
			tueCB.setForeground(textColor);
			wedCB = new JCheckBox("<html>" + "Wed" + "</html>");
			wedCB.setPreferredSize(checkSize);
			wedCB.addItemListener(this);
			wedCB.setBackground(optionPaneColor);
			wedCB.setForeground(textColor);
			thuCB = new JCheckBox("<html>" + "Thu" + "</html>");
			thuCB.setPreferredSize(checkSize);
			thuCB.addItemListener(this);
			thuCB.setBackground(optionPaneColor);
			thuCB.setForeground(textColor);
			friCB = new JCheckBox("<html>" + "Fri" + "</html>");
			friCB.setPreferredSize(checkSize);
			friCB.addItemListener(this);
			friCB.setBackground(optionPaneColor);
			friCB.setForeground(textColor);
			satCB = new JCheckBox("<html>" + "Sat" + "</html>");
			satCB.setPreferredSize(checkSize);
			satCB.addItemListener(this);
			satCB.setBackground(optionPaneColor);
			satCB.setForeground(textColor);
			speCB = new JCheckBox("<html>" + "Custome" + "</html>");
			speCB.setPreferredSize(new Dimension(90, 30));
			speCB.addItemListener(this);
			speCB.setBackground(optionPaneColor);
			speCB.setForeground(textColor);

			dayPanel = new JPanel();
			newEventNA();

			JLabel col = new JLabel("Event Color");
			col.setPreferredSize(labelSize);
			col.setForeground(textColor);
			col.setFont(TEXTFIELDFONT);
			JLabel pre = new JLabel("Preset Event");
			pre.setPreferredSize(labelSize);
			pre.setForeground(textColor);
			pre.setFont(TEXTFIELDFONT);
			JLabel lab1 = new JLabel("Event Name");
			lab1.setPreferredSize(labelSize);
			lab1.setForeground(textColor);
			lab1.setFont(TEXTFIELDFONT);
			eventTextField = new JTextField();
			eventTextField.setPreferredSize(textFieldSize);
			eventTextField.setFont(TEXTFIELDFONT);
			JLabel lab2 = new JLabel("Start Time");
			lab2.setPreferredSize(labelSize);
			lab2.setForeground(textColor);
			lab2.setFont(TEXTFIELDFONT);
			startTextField = new JTextField();
			startTextField.setPreferredSize(textFieldSize);
			startTextField.setFont(TEXTFIELDFONT);
			JLabel lab3 = new JLabel("End Time");
			lab3.setPreferredSize(labelSize);
			lab3.setForeground(textColor);
			lab3.setFont(TEXTFIELDFONT);
			endTextField = new JTextField();
			endTextField.setPreferredSize(textFieldSize);
			endTextField.setFont(TEXTFIELDFONT);
			JPanel pan = new JPanel();
			pan.setPreferredSize(new Dimension(479, 300));
			SpringLayout layout = new SpringLayout();
			pan.setLayout(layout);
			pan.setBackground(optionPaneColor);

			jcb = new JColorBox(getColorOptions());
			jcb.setFont(TEXTFIELDFONT);
			jcb.setSelectedIndex(0);
			jcb.setPreferredSize(new Dimension(30, 30));
			jcb.setFocusable(false);
			jcb.setBorder(BorderFactory.createLineBorder((Color) jcb.getSelectedItem(), 200));
			jcb.setForeground(null);
			jcb.addActionListener(this);

			Component[] c = jcb.getComponents();
			for (Component res : c) {
				if (res instanceof AbstractButton && res.isVisible()) {
					res.setVisible(false);
				}
			}

			preset = new JComboBox<>(presetOptions.getStringPresets());
			preset.setFont(TEXTFIELDFONT);
			preset.setPreferredSize(textFieldSize);
			preset.setSelectedIndex(-1);
			preset.setFocusable(false);
			preset.addActionListener(this);

			pan.add(noRepCB);
			layout.putConstraint(SpringLayout.WEST, noRepCB, 5, SpringLayout.WEST, pan);
			layout.putConstraint(SpringLayout.NORTH, noRepCB, 5, SpringLayout.NORTH, pan);
			pan.add(sunCB);
			layout.putConstraint(SpringLayout.WEST, sunCB, 0, SpringLayout.EAST, noRepCB);
			layout.putConstraint(SpringLayout.NORTH, sunCB, 5, SpringLayout.NORTH, pan);
			pan.add(monCB);
			layout.putConstraint(SpringLayout.WEST, monCB, 0, SpringLayout.EAST, sunCB);
			layout.putConstraint(SpringLayout.NORTH, monCB, 5, SpringLayout.NORTH, pan);
			pan.add(tueCB);
			layout.putConstraint(SpringLayout.WEST, tueCB, 0, SpringLayout.EAST, monCB);
			layout.putConstraint(SpringLayout.NORTH, tueCB, 5, SpringLayout.NORTH, pan);
			pan.add(wedCB);
			layout.putConstraint(SpringLayout.WEST, wedCB, 0, SpringLayout.EAST, tueCB);
			layout.putConstraint(SpringLayout.NORTH, wedCB, 5, SpringLayout.NORTH, pan);
			pan.add(thuCB);
			layout.putConstraint(SpringLayout.WEST, thuCB, 0, SpringLayout.EAST, wedCB);
			layout.putConstraint(SpringLayout.NORTH, thuCB, 5, SpringLayout.NORTH, pan);
			pan.add(friCB);
			layout.putConstraint(SpringLayout.WEST, friCB, 0, SpringLayout.EAST, thuCB);
			layout.putConstraint(SpringLayout.NORTH, friCB, 5, SpringLayout.NORTH, pan);
			pan.add(satCB);
			layout.putConstraint(SpringLayout.WEST, satCB, 0, SpringLayout.EAST, friCB);
			layout.putConstraint(SpringLayout.NORTH, satCB, 5, SpringLayout.NORTH, pan);
			pan.add(speCB);
			layout.putConstraint(SpringLayout.WEST, speCB, 0, SpringLayout.EAST, satCB);
			layout.putConstraint(SpringLayout.NORTH, speCB, 5, SpringLayout.NORTH, pan);

			pan.add(dayPanel);
			layout.putConstraint(SpringLayout.WEST, dayPanel, 0, SpringLayout.WEST, pan);
			layout.putConstraint(SpringLayout.NORTH, dayPanel, 5, SpringLayout.SOUTH, noRepCB);

			pan.add(col);
			pan.add(jcb);
			int height = 35;
			layout.putConstraint(SpringLayout.WEST, col, 115, SpringLayout.WEST, pan);
			layout.putConstraint(SpringLayout.NORTH, col, 5, SpringLayout.SOUTH, dayPanel);
			layout.putConstraint(SpringLayout.WEST, jcb, 5, SpringLayout.EAST, col);
			layout.putConstraint(SpringLayout.NORTH, jcb, 5, SpringLayout.SOUTH, dayPanel);

			pan.add(pre);
			pan.add(preset);
			layout.putConstraint(SpringLayout.WEST, pre, 115, SpringLayout.WEST, pan);
			layout.putConstraint(SpringLayout.NORTH, pre, height, SpringLayout.NORTH, col);
			layout.putConstraint(SpringLayout.WEST, preset, 5, SpringLayout.EAST, pre);
			layout.putConstraint(SpringLayout.NORTH, preset, height, SpringLayout.NORTH, jcb);

			pan.add(lab1);
			pan.add(eventTextField);
			layout.putConstraint(SpringLayout.WEST, lab1, 115, SpringLayout.WEST, pan);
			layout.putConstraint(SpringLayout.NORTH, lab1, height, SpringLayout.NORTH, pre);
			layout.putConstraint(SpringLayout.WEST, eventTextField, 5, SpringLayout.EAST, lab1);
			layout.putConstraint(SpringLayout.NORTH, eventTextField, height, SpringLayout.NORTH, preset);

			pan.add(lab2);
			pan.add(startTextField);
			layout.putConstraint(SpringLayout.WEST, lab2, 115, SpringLayout.WEST, pan);
			layout.putConstraint(SpringLayout.NORTH, lab2, height, SpringLayout.NORTH, lab1);
			layout.putConstraint(SpringLayout.WEST, startTextField, 5, SpringLayout.EAST, lab2);
			layout.putConstraint(SpringLayout.NORTH, startTextField, height, SpringLayout.NORTH, eventTextField);

			pan.add(lab3);
			pan.add(endTextField);
			layout.putConstraint(SpringLayout.WEST, lab3, 115, SpringLayout.WEST, pan);
			layout.putConstraint(SpringLayout.NORTH, lab3, height, SpringLayout.NORTH, lab2);
			layout.putConstraint(SpringLayout.WEST, endTextField, 5, SpringLayout.EAST, lab3);
			layout.putConstraint(SpringLayout.NORTH, endTextField, height, SpringLayout.NORTH, startTextField);

			boolean tryEvent = true;
			while (tryEvent) {
				tryEvent = false;
				String[] options = { "Add", "Cancel" };
				JOptionPane pane = new JOptionPane(pan, JOptionPane.PLAIN_MESSAGE, JOptionPane.OK_CANCEL_OPTION, null,
						options, options[0]);
				pane.setBackground(optionPaneColor);
				JDialog dialog = pane.createDialog(screen, "Advanced Event Adder");
				// dialog.setBackground(scrollPanelColor);
				dialog.setLocation(westPanel.getLocationOnScreen().x + 233, westPanel.getLocationOnScreen().y + 30);
				dialog.setVisible(true);
				dialog.dispose();
				System.out.println("Dialog:2 " + pane.getValue());
				if (pane.getValue() != null && pane.getValue().equals(options[0])) {
					try {
						int daysCount = 0;
						int buttonIndex = 0;
						if (noRepCB != null && noRepCB.getSelectedObjects() != null) {
							System.out.println("NA was selected.");
							try {
								if (daysPerMonth[naMonth.getSelectedIndex()] < Integer.parseInt(naDay.getText())
										|| Integer.parseInt(naDay.getText()) < 1) {
									throw new IllegalArgumentException();
								}
							} catch (Exception e2) {
								throw new IllegalArgumentException("Invalid Date.");
							}
							for (int j = 0; j < naMonth.getSelectedIndex(); j++) {
								daysCount = daysCount + daysPerMonth[j];
							}
							buttonIndex = daysCount + Integer.parseInt(naDay.getText()) - 1;
							EventData newEvent = manager.createEvent((String) eventTextField.getText(),
									(String) startTextField.getText(), (String) endTextField.getText(),
									datePanel[buttonIndex].getDay(), datePanel[buttonIndex].getMonth(),
									datePanel[buttonIndex].getYear(), ((Color) jcb.getSelectedItem()).getRed(),
									((Color) jcb.getSelectedItem()).getGreen(),
									((Color) jcb.getSelectedItem()).getBlue());
							datePanel[buttonIndex].addButton(newEvent.getStartTime(), newEvent.getStartInt(),
									newEvent.getEndTime(), datePanel[buttonIndex].getDay(),
									datePanel[buttonIndex].getMonth(), datePanel[buttonIndex].getYear(),
									newEvent.getName(), newEvent.getColor().getRed(), newEvent.getColor().getGreen(),
									newEvent.getColor().getBlue());
						} else if (noRepCB.getSelectedObjects() == null && speCB.getSelectedObjects() == null) {
							System.out.println("Weeks was selected.");
						} else {
							System.out.println("Custome was selected.");
						}
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
			screen.setVisible(true);
			return;
		} else if (e.getSource() == presetsBut) {
			System.out.println("Presets Button");
			presetState = new PresetStateMachine();
			presetEventMenu = true;
			addPresetBut = new JButton("+");
			deletePresetBut = new JButton("-");
			deletePresetBut.setEnabled(false);

			addPresetBut.setPreferredSize(new Dimension(20, 20));
			addPresetBut.setMargin(new Insets(0, 0, 0, 0));
			addPresetBut.setFocusable(false);
			addPresetBut.setFont(new Font("Comic Sans", Font.BOLD, 15));
			addPresetBut.addActionListener(this);

			deletePresetBut.setPreferredSize(new Dimension(20, 20));
			deletePresetBut.setMargin(new Insets(0, 0, 0, 0));
			deletePresetBut.setFocusable(false);
			deletePresetBut.setFont(new Font("Comic Sans", Font.BOLD, 15));
			deletePresetBut.addActionListener(this);

			JPanel pan = new JPanel();
			pan.setPreferredSize(new Dimension(400, 150));
			SpringLayout layout = new SpringLayout();
			pan.setLayout(layout);

			JLabel top = new JLabel("Events");
			top.setPreferredSize(new Dimension(40, 20));
			top.setForeground(textColor);
			preset = new JComboBox<>(presetOptions.getStringPresets());
			preset.setPreferredSize(new Dimension(160, 30));
			preset.setSelectedIndex(-1);
			preset.setFocusable(false);
			preset.addActionListener(this);

			pan.add(top);
			pan.add(preset);
			layout.putConstraint(SpringLayout.WEST, top, 5, SpringLayout.WEST, pan);
			layout.putConstraint(SpringLayout.NORTH, top, 5, SpringLayout.NORTH, pan);
			layout.putConstraint(SpringLayout.WEST, preset, 5, SpringLayout.WEST, pan);
			layout.putConstraint(SpringLayout.NORTH, preset, 25, SpringLayout.NORTH, top);

			pan.add(addPresetBut);
			pan.add(deletePresetBut);
			layout.putConstraint(SpringLayout.WEST, addPresetBut, 5, SpringLayout.EAST, top);
			layout.putConstraint(SpringLayout.NORTH, addPresetBut, 5, SpringLayout.NORTH, pan);
			layout.putConstraint(SpringLayout.WEST, deletePresetBut, 5, SpringLayout.EAST, addPresetBut);
			layout.putConstraint(SpringLayout.NORTH, deletePresetBut, 5, SpringLayout.NORTH, pan);

			Dimension textFieldSize = new Dimension(151, 20);
			Dimension labelSize = new Dimension(75, 20);
			JLabel col = new JLabel("Event Color");
			col.setPreferredSize(labelSize);
			col.setForeground(textColor);
			JLabel lab1 = new JLabel("Event Name");
			lab1.setPreferredSize(labelSize);
			lab1.setForeground(textColor);
			eventTextField = new JTextField();
			eventTextField.setPreferredSize(textFieldSize);
			eventTextField.setEnabled(false);
			JLabel lab2 = new JLabel("Start Time");
			lab2.setPreferredSize(labelSize);
			lab2.setForeground(textColor);
			startTextField = new JTextField();
			startTextField.setPreferredSize(textFieldSize);
			startTextField.setEnabled(false);
			JLabel lab3 = new JLabel("End Time");
			lab3.setPreferredSize(labelSize);
			lab3.setForeground(textColor);
			endTextField = new JTextField();
			endTextField.setPreferredSize(textFieldSize);
			endTextField.setEnabled(false);

			savePreset = new JButton("Save");
			savePreset.setEnabled(false);
			savePreset.addActionListener(this);
			savePreset.setPreferredSize(new Dimension(90, 30));

			cancelPreset = new JButton("Cancel");
			cancelPreset.setEnabled(false);
			cancelPreset.addActionListener(this);
			cancelPreset.setPreferredSize(new Dimension(80, 30));

			JPanel pan2 = new JPanel();
			pan2.setPreferredSize(new Dimension(250, 190));
			SpringLayout layout2 = new SpringLayout();
			pan2.setLayout(layout2);

			jcb = new JColorBox(getColorOptions());
			jcb.setEnabled(false);
			jcb.setSelectedIndex(0);
			jcb.setPreferredSize(new Dimension(30, 20));
			jcb.setFocusable(false);
			jcb.setBorder(BorderFactory.createLineBorder((Color) jcb.getSelectedItem(), 200));
			jcb.setForeground(null);
			jcb.addActionListener(this);

			Component[] c = jcb.getComponents();
			for (Component res : c) {
				if (res instanceof AbstractButton && res.isVisible()) {
					res.setVisible(false);
				}
			}

			pan2.add(col);
			pan2.add(jcb);
			layout2.putConstraint(SpringLayout.WEST, col, 5, SpringLayout.WEST, pan2);
			layout2.putConstraint(SpringLayout.NORTH, col, 5, SpringLayout.NORTH, pan2);
			layout2.putConstraint(SpringLayout.WEST, jcb, 5, SpringLayout.EAST, col);
			layout2.putConstraint(SpringLayout.NORTH, jcb, 5, SpringLayout.NORTH, pan2);

			pan2.add(lab1);
			pan2.add(eventTextField);
			layout2.putConstraint(SpringLayout.WEST, lab1, 5, SpringLayout.WEST, pan2);
			layout2.putConstraint(SpringLayout.NORTH, lab1, 30, SpringLayout.NORTH, col);
			layout2.putConstraint(SpringLayout.WEST, eventTextField, 5, SpringLayout.EAST, lab1);
			layout2.putConstraint(SpringLayout.NORTH, eventTextField, 30, SpringLayout.NORTH, jcb);

			pan2.add(lab2);
			pan2.add(startTextField);
			layout2.putConstraint(SpringLayout.WEST, lab2, 5, SpringLayout.WEST, pan2);
			layout2.putConstraint(SpringLayout.NORTH, lab2, 24, SpringLayout.NORTH, lab1);
			layout2.putConstraint(SpringLayout.WEST, startTextField, 5, SpringLayout.EAST, lab2);
			layout2.putConstraint(SpringLayout.NORTH, startTextField, 24, SpringLayout.NORTH, eventTextField);

			pan2.add(lab3);
			pan2.add(endTextField);
			layout2.putConstraint(SpringLayout.WEST, lab3, 5, SpringLayout.WEST, pan2);
			layout2.putConstraint(SpringLayout.NORTH, lab3, 24, SpringLayout.NORTH, lab2);
			layout2.putConstraint(SpringLayout.WEST, endTextField, 5, SpringLayout.EAST, lab3);
			layout2.putConstraint(SpringLayout.NORTH, endTextField, 24, SpringLayout.NORTH, startTextField);

			pan2.add(savePreset);
			pan2.add(cancelPreset);
			layout2.putConstraint(SpringLayout.EAST, cancelPreset, -50, SpringLayout.EAST, pan2);
			layout2.putConstraint(SpringLayout.NORTH, cancelPreset, 24, SpringLayout.NORTH, endTextField);
			layout2.putConstraint(SpringLayout.EAST, savePreset, -5, SpringLayout.WEST, cancelPreset);
			layout2.putConstraint(SpringLayout.NORTH, savePreset, 24, SpringLayout.NORTH, endTextField);

			pan.add(pan2);
			layout.putConstraint(SpringLayout.WEST, pan2, 120, SpringLayout.EAST, top);
			layout.putConstraint(SpringLayout.NORTH, pan2, 0, SpringLayout.NORTH, pan);

			boolean tryEvent = true;
			while (tryEvent) {
				tryEvent = false;
				try {
					String[] something = { "Close" };
					JOptionPane pane = new JOptionPane(pan, JOptionPane.PLAIN_MESSAGE, JOptionPane.CLOSED_OPTION, null,
							something, something[0]);
					JDialog dialog = pane.createDialog(screen, "Add/Remove Preset");
					dialog.setLocation(westPanel.getLocationOnScreen().x + 233,
							presetsBut.getLocationOnScreen().y - 190);
					dialog.setVisible(true);
					dialog.dispose();
					screen.setVisible(true);
					screen.repaint();
					screen.validate();
					presetEventMenu = false;
					return;
				} catch (Exception e1) {
					JOptionPane.showMessageDialog(screen, e1.getMessage());
					tryEvent = true;
				}
			}
			presetEventMenu = false;
			screen.setVisible(true);
		} else if (e.getSource() == savePreset) {
			PresetState current = presetState.getState();
			if (presetState.changeState("save")) {
				if (current == PresetState.adding) {
					try {
						System.out.println("Current State: " + current);
						EventData newEvent = new EventData(eventTextField.getText(), startTextField.getText(),
								endTextField.getText(), 1, 1, 1, 2, 2, 2);
						if (!presetOptions.duplicatePreset(newEvent.getName(), newEvent.getStartTime(),
								newEvent.getEndTime(), (Color) jcb.getSelectedItem())) {
							System.out.println(":[");
							return;
						}
						presetOptions.addPreset(newEvent.getName(), newEvent.getStartTime(), newEvent.getEndTime(),
								(Color) jcb.getSelectedItem());
						int index = -1;
						String newPresetString = newEvent.getName() + " " + newEvent.getStartTime() + "-"
								+ newEvent.getEndTime();
						for (int i = 0; i < presetOptions.getSize(); i++) {
							if (presetOptions.getStringPresets()[i].equals(newPresetString)) {
								index = i;
								i = presetOptions.getSize();
							}
						}
						preset.removeActionListener(this);
						preset.removeAllItems();
						for (int i = 0; i < presetOptions.getSize(); i++) {
							preset.addItem(presetOptions.getStringPresets()[i]);
						}
						addPresetBut.setEnabled(true);
						deletePresetBut.setEnabled(true);
						eventTextField.setEnabled(true);
						startTextField.setEnabled(true);
						endTextField.setEnabled(true);
						jcb.setEnabled(true);
						savePreset.setEnabled(true);
						cancelPreset.setEnabled(true);

						startTextField.setText(newEvent.getStartTime());
						endTextField.setText(newEvent.getEndTime());
						preset.setSelectedIndex(index);
						preset.setFocusable(false);
						preset.addActionListener(this);
						savePreset.setText("Update");
					} catch (Exception e1) {
						JOptionPane.showMessageDialog(screen, e1.getMessage());
						presetState.setState(current);
					}
				} else if (current == PresetState.selected) {
					try {
						System.out.println("Current State5: " + presetState.getState());
						int presetIndex = preset.getSelectedIndex();
						EventData newEvent = new EventData(eventTextField.getText(), startTextField.getText(),
								endTextField.getText(), 1, 1, 1, 2, 2, 2);
						if (presetOptions.duplicatePreset(newEvent.getName(), newEvent.getStartTime(),
								newEvent.getEndTime(), (Color) jcb.getSelectedItem())) {
							presetOptions.removePreset(presetOptions.getPresets()[presetIndex].getName(),
									presetOptions.getPresets()[presetIndex].getStart(),
									presetOptions.getPresets()[presetIndex].getEnd(),
									presetOptions.getPresets()[presetIndex].getColor());
							presetOptions.addPreset(newEvent.getName(), newEvent.getStartTime(), newEvent.getEndTime(),
									(Color) jcb.getSelectedItem());
							int index = -1;
							String newPresetString = newEvent.getName() + " " + newEvent.getStartTime() + "-"
									+ newEvent.getEndTime();
							for (int i = 0; i < presetOptions.getSize(); i++) {
								System.out.println(presetOptions.getStringPresets()[i]);
								if (presetOptions.getStringPresets()[i].equals(newPresetString)) {
									index = i;
									i = presetOptions.getSize();
								}
							}
							System.out.println(index);
							preset.removeActionListener(this);
							preset.removeAllItems();
							for (int i = 0; i < presetOptions.getSize(); i++) {
								preset.addItem(presetOptions.getStringPresets()[i]);
							}
							addPresetBut.setEnabled(true);
							deletePresetBut.setEnabled(true);
							eventTextField.setEnabled(true);
							startTextField.setEnabled(true);
							endTextField.setEnabled(true);
							jcb.setEnabled(true);
							savePreset.setEnabled(true);
							cancelPreset.setEnabled(true);

							preset.setSelectedIndex(index);
							preset.setFocusable(false);
							preset.addActionListener(this);
						}
						// System.out.println("Duplicate Preset Found");
					} catch (Exception e2) {
						JOptionPane.showMessageDialog(screen, e2.getMessage());
						presetState.setState(current);
					}
				}
			}
		} else if (e.getSource() == cancelPreset) {
			if (presetState.changeState("cancel")) {
				if (presetState.getState() == PresetState.selected) {
					int index = preset.getSelectedIndex();
					addPresetBut.setEnabled(true);
					deletePresetBut.setEnabled(true);
					eventTextField.setEnabled(true);
					startTextField.setEnabled(true);
					endTextField.setEnabled(true);
					jcb.setEnabled(true);
					savePreset.setEnabled(true);
					cancelPreset.setEnabled(true);

					eventTextField.setText(presetOptions.getPresets()[index].getName());
					startTextField.setText(presetOptions.getPresets()[index].getStart());
					endTextField.setText(presetOptions.getPresets()[index].getEnd());
					jcb.setSelectedItem(presetOptions.getPresets()[index].getColor());
				} else {
					addPresetBut.setEnabled(true);
					deletePresetBut.setEnabled(false);
					eventTextField.setEnabled(false);
					startTextField.setEnabled(false);
					endTextField.setEnabled(false);
					jcb.setEnabled(false);
					savePreset.setEnabled(false);
					cancelPreset.setEnabled(false);

					eventTextField.setText("");
					startTextField.setText("");
					endTextField.setText("");
					preset.removeActionListener(this);
					preset.setSelectedIndex(-1);
					preset.addActionListener(this);
					jcb.setSelectedIndex(0);
				}
			}
		} else if (e.getSource() == addPresetBut) {
			if (presetState.changeState("add")) {
				addPresetBut.setEnabled(false);
				deletePresetBut.setEnabled(false);
				eventTextField.setEnabled(true);
				startTextField.setEnabled(true);
				endTextField.setEnabled(true);
				jcb.setEnabled(true);
				savePreset.setEnabled(true);
				cancelPreset.setEnabled(true);

				preset.setEditable(true);
				preset.setSelectedItem("Adding Event");
				savePreset.setText("Add");
				preset.setEditable(false);
				eventTextField.setText("");
				startTextField.setText("");
				endTextField.setText("");
			}
		} else if (e.getSource() == deletePresetBut) {
			if (preset.getSelectedIndex() == -1) {
				return;
			} else if (presetState.changeState("remove")) {
				presetOptions.removePreset(presetOptions.getPresets()[preset.getSelectedIndex()].getName(),
						presetOptions.getPresets()[preset.getSelectedIndex()].getStart(),
						presetOptions.getPresets()[preset.getSelectedIndex()].getEnd(),
						presetOptions.getPresets()[preset.getSelectedIndex()].getColor());
				eventTextField.setText("");
				startTextField.setText("");
				endTextField.setText("");
				jcb.setSelectedIndex(0);

				addPresetBut.setEnabled(true);
				deletePresetBut.setEnabled(false);
				eventTextField.setEnabled(false);
				startTextField.setEnabled(false);
				endTextField.setEnabled(false);
				jcb.setEnabled(false);
				savePreset.setEnabled(false);
				cancelPreset.setEnabled(false);

				preset.removeActionListener(this);
				preset.removeAllItems();
				for (int i = 0; i < presetOptions.getSize(); i++) {
					preset.addItem(presetOptions.getStringPresets()[i]);
				}
				preset.setSelectedIndex(-1);
				preset.setFocusable(false);
				preset.addActionListener(this);
			}
		} else if (e.getSource() == colorsBut) {
			System.out.println("Color Button");
			JPanel pan = new JPanel();
			pan.setPreferredSize(new Dimension(50, 65));
			SpringLayout layout = new SpringLayout();
			pan.setLayout(layout);

			jcb = new JColorBox(getColorOptions());
			jcb.updateBox(colorOptions);
			jcb.setSelectedIndex(0);
			// jcb.setBounds(100, 20, 140, 30);
			jcb.setPreferredSize(new Dimension(100, 40));
			jcb.setFocusable(false);
			jcb.setBorder(BorderFactory.createLineBorder((Color) jcb.getSelectedItem(), 200));
			jcb.setForeground(null);

			Component[] c = jcb.getComponents();
			for (Component res : c) {
				if (res instanceof AbstractButton) {
					if (res.isVisible()) {
						res.setVisible(false);
					}
				}
			}

			Dimension labelSize = new Dimension(75, 20);
			JLabel col = new JLabel("Event Color");
			col.setPreferredSize(labelSize);
			col.setForeground(textColor);
			JLabel lab1 = new JLabel("Add/Delete");
			lab1.setPreferredSize(labelSize);
			lab1.setForeground(textColor);
			addColorBut = new JButton("+");
			addColorBut.setPreferredSize(new Dimension(40, 40));
			addColorBut.setFont(new Font("Comic Sans", Font.BOLD, 25));
			addColorBut.addActionListener(this);
			addColorBut.setMargin(new Insets(0, 0, 0, 0));
			deleteColorBut = new JButton("-");
			deleteColorBut.setPreferredSize(new Dimension(40, 40));
			deleteColorBut.setFont(new Font("Comic Sans", Font.BOLD, 25));
			deleteColorBut.addActionListener(this);
			deleteColorBut.setMargin(new Insets(0, 0, 0, 0));

			pan.add(col);
			pan.add(lab1);
			layout.putConstraint(SpringLayout.EAST, lab1, -15, SpringLayout.EAST, pan);
			layout.putConstraint(SpringLayout.NORTH, lab1, 5, SpringLayout.NORTH, pan);
			layout.putConstraint(SpringLayout.WEST, col, 15, SpringLayout.WEST, pan);
			layout.putConstraint(SpringLayout.NORTH, col, 5, SpringLayout.NORTH, pan);

			pan.add(jcb);
			pan.add(addColorBut);
			pan.add(deleteColorBut);
			layout.putConstraint(SpringLayout.EAST, deleteColorBut, -15, SpringLayout.EAST, pan);
			layout.putConstraint(SpringLayout.NORTH, deleteColorBut, 20, SpringLayout.NORTH, lab1);
			layout.putConstraint(SpringLayout.EAST, addColorBut, -10, SpringLayout.WEST, deleteColorBut);
			layout.putConstraint(SpringLayout.NORTH, addColorBut, 20, SpringLayout.NORTH, lab1);
			layout.putConstraint(SpringLayout.WEST, jcb, 15, SpringLayout.WEST, pan);
			layout.putConstraint(SpringLayout.NORTH, jcb, 20, SpringLayout.NORTH, col);

			boolean tryEvent = true;
			while (tryEvent) {
				tryEvent = false;
				try {
					JOptionPane pane = new JOptionPane(pan, JOptionPane.PLAIN_MESSAGE, JOptionPane.CLOSED_OPTION);
					JDialog dialog = pane.createDialog(screen, "Add/Remove Color");
					dialog.setLocation(westPanel.getLocationOnScreen().x + 233, colorsBut.getLocationOnScreen().y - 95);
					dialog.setVisible(true);
					dialog.dispose();
					screen.setVisible(true);
					screen.repaint();
					screen.validate();
					addColorBut.removeActionListener(this);
					deleteColorBut.removeActionListener(this);
					return;
				} catch (Exception e1) {
					JOptionPane.showMessageDialog(screen, e1.getMessage());
					tryEvent = true;
				}
			}
		} else if (e.getSource() == addColorBut) {
			new JColorChooser();
			JColorChooser jcc = new JColorChooser();
			JOptionPane pane = new JOptionPane(jcc, JOptionPane.PLAIN_MESSAGE, JOptionPane.CLOSED_OPTION);
			JDialog dialog = pane.createDialog(screen, "Add Color");
			dialog.setLocation(westPanel.getLocationOnScreen().x + 233, colorsBut.getLocationOnScreen().y - 343);
			dialog.setVisible(true);
			dialog.dispose();
			System.out.println(jcc.getColor() + " " + pane.getValue());
			if (jcc != null && pane.getValue() != null) {
				colorOptions.addColor(jcc.getColor());
				jcb.updateBox(colorOptions);
			}
		} else if (e.getSource() == deleteColorBut) {
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
				scrollMonth = monthOfCalendar;
				createSmallCalendar(monthOfCalendar);
				if (Calendar.getInstance().get(Calendar.YEAR) == yearOfCalendar) {
					monthOfCalendar = Calendar.getInstance().get(Calendar.MONTH);
					scrollMonth = monthOfCalendar;
				}
				monthLabel.setText("<html>" + ALLMONTHNAMES[monthOfCalendar] + " " + yearOfCalendar + "</html>");
				monthSmallLabel.setText(ALLMONTHNAMES[monthOfCalendar] + " " + yearOfCalendar);
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
				scrollMonth = monthOfCalendar;
				createSmallCalendar(monthOfCalendar);
				if (Calendar.getInstance().get(Calendar.YEAR) == yearOfCalendar) {
					monthOfCalendar = Calendar.getInstance().get(Calendar.MONTH);
					scrollMonth = monthOfCalendar;
				}
				monthLabel.setText("<html>" + ALLMONTHNAMES[monthOfCalendar] + " " + yearOfCalendar + "</html>");
				monthSmallLabel.setText(ALLMONTHNAMES[monthOfCalendar] + " " + yearOfCalendar);
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
					System.out.println("Button: " + i);
					Dimension textFieldSize = new Dimension(151, 20);
					Dimension labelSize = new Dimension(75, 20);

					JLabel col = new JLabel("Event Color");
					col.setPreferredSize(labelSize);
					col.setForeground(textColor);
					JLabel pre = new JLabel("Preset Event");
					pre.setPreferredSize(labelSize);
					pre.setForeground(textColor);
					JLabel lab1 = new JLabel("Event Name");
					lab1.setPreferredSize(labelSize);
					lab1.setForeground(textColor);
					eventTextField = new JTextField();
					eventTextField.setPreferredSize(textFieldSize);
					JLabel lab2 = new JLabel("Start Time");
					lab2.setPreferredSize(labelSize);
					lab2.setForeground(textColor);
					startTextField = new JTextField();
					startTextField.setPreferredSize(textFieldSize);
					JLabel lab3 = new JLabel("End Time");
					lab3.setPreferredSize(labelSize);
					lab3.setForeground(textColor);
					endTextField = new JTextField();
					endTextField.setPreferredSize(textFieldSize);
					JPanel pan = new JPanel();
					pan.setPreferredSize(new Dimension(100, 135));
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
						if (res instanceof AbstractButton && res.isVisible()) {
							res.setVisible(false);
						}
					}

					preset = new JComboBox<>(presetOptions.getStringPresets());
					preset.setPreferredSize(textFieldSize);
					preset.setSelectedIndex(-1);
					preset.setFocusable(false);
					preset.addActionListener(this);

					pan.add(col);
					pan.add(jcb);
					layout.putConstraint(SpringLayout.WEST, col, 5, SpringLayout.WEST, pan);
					layout.putConstraint(SpringLayout.NORTH, col, 5, SpringLayout.NORTH, pan);
					layout.putConstraint(SpringLayout.WEST, jcb, 5, SpringLayout.EAST, col);
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
					layout.putConstraint(SpringLayout.WEST, eventTextField, 5, SpringLayout.EAST, lab1);
					layout.putConstraint(SpringLayout.NORTH, eventTextField, 30, SpringLayout.NORTH, preset);

					pan.add(lab2);
					pan.add(startTextField);
					layout.putConstraint(SpringLayout.WEST, lab2, 5, SpringLayout.WEST, pan);
					layout.putConstraint(SpringLayout.NORTH, lab2, 24, SpringLayout.NORTH, lab1);
					layout.putConstraint(SpringLayout.WEST, startTextField, 5, SpringLayout.EAST, lab2);
					layout.putConstraint(SpringLayout.NORTH, startTextField, 24, SpringLayout.NORTH, eventTextField);

					pan.add(lab3);
					pan.add(endTextField);
					layout.putConstraint(SpringLayout.WEST, lab3, 5, SpringLayout.WEST, pan);
					layout.putConstraint(SpringLayout.NORTH, lab3, 24, SpringLayout.NORTH, lab2);
					layout.putConstraint(SpringLayout.WEST, endTextField, 5, SpringLayout.EAST, lab3);
					layout.putConstraint(SpringLayout.NORTH, endTextField, 24, SpringLayout.NORTH, startTextField);

					boolean tryEvent = true;
					while (tryEvent) {
						tryEvent = false;
						String[] options = { "Add", "Cancel" };
						int optionSelected = JOptionPane.showOptionDialog(screen, pan,
								"Add Event (" + datePanel[i].getDateString() + ")", JOptionPane.OK_CANCEL_OPTION,
								JOptionPane.PLAIN_MESSAGE, null, options, options[0]);
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
			int buttonCount = 0;
			for (int i = 0; i < 42; i++) {
				if (smallMonthButtons[i].isEnabled()) {
					buttonCount++;
				}
				if (e.getSource() == smallMonthButtons[i]) {
					// System.out.println("Button Found: " + i);
					// System.out.println("Buttons: " + buttonCount);
					Dimension textFieldSize = new Dimension(151, 20);
					Dimension labelSize = new Dimension(75, 20);

					int buttonIndex = 0;
					int daysCount = 0;
					for (int j = 0; j < scrollMonth; j++) {
						daysCount = daysCount + daysPerMonth[j];
					}
					buttonIndex = daysCount + buttonCount - 1;
					if (scrollMonth >= 1 && daysPerMonth[1] == 28) {
						buttonIndex++;
					}
					System.out.println("\nButton Count: " + buttonCount);
					System.out.println("Days Count: " + daysCount);
					System.out.println("Button: " + buttonIndex);

					JLabel col = new JLabel("Event Color");
					col.setPreferredSize(labelSize);
					col.setForeground(textColor);
					JLabel pre = new JLabel("Preset Event");
					pre.setPreferredSize(labelSize);
					pre.setForeground(textColor);
					JLabel lab1 = new JLabel("Event Name");
					lab1.setPreferredSize(labelSize);
					lab1.setForeground(textColor);
					eventTextField = new JTextField();
					eventTextField.setPreferredSize(textFieldSize);
					JLabel lab2 = new JLabel("Start Time");
					lab2.setPreferredSize(labelSize);
					lab2.setForeground(textColor);
					startTextField = new JTextField();
					startTextField.setPreferredSize(textFieldSize);
					JLabel lab3 = new JLabel("End Time");
					lab3.setPreferredSize(labelSize);
					lab3.setForeground(textColor);
					endTextField = new JTextField();
					endTextField.setPreferredSize(textFieldSize);
					JPanel pan = new JPanel();
					pan.setPreferredSize(new Dimension(100, 135));
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
						if (res instanceof AbstractButton && res.isVisible()) {
							res.setVisible(false);
						}
					}

					preset = new JComboBox<>(presetOptions.getStringPresets());
					preset.setPreferredSize(textFieldSize);
					preset.setSelectedIndex(-1);
					preset.setFocusable(false);
					preset.addActionListener(this);

					pan.add(col);
					pan.add(jcb);
					layout.putConstraint(SpringLayout.WEST, col, 5, SpringLayout.WEST, pan);
					layout.putConstraint(SpringLayout.NORTH, col, 5, SpringLayout.NORTH, pan);
					layout.putConstraint(SpringLayout.WEST, jcb, 5, SpringLayout.EAST, col);
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
					layout.putConstraint(SpringLayout.WEST, eventTextField, 5, SpringLayout.EAST, lab1);
					layout.putConstraint(SpringLayout.NORTH, eventTextField, 30, SpringLayout.NORTH, preset);

					pan.add(lab2);
					pan.add(startTextField);
					layout.putConstraint(SpringLayout.WEST, lab2, 5, SpringLayout.WEST, pan);
					layout.putConstraint(SpringLayout.NORTH, lab2, 24, SpringLayout.NORTH, lab1);
					layout.putConstraint(SpringLayout.WEST, startTextField, 5, SpringLayout.EAST, lab2);
					layout.putConstraint(SpringLayout.NORTH, startTextField, 24, SpringLayout.NORTH, eventTextField);

					pan.add(lab3);
					pan.add(endTextField);
					layout.putConstraint(SpringLayout.WEST, lab3, 5, SpringLayout.WEST, pan);
					layout.putConstraint(SpringLayout.NORTH, lab3, 24, SpringLayout.NORTH, lab2);
					layout.putConstraint(SpringLayout.WEST, endTextField, 5, SpringLayout.EAST, lab3);
					layout.putConstraint(SpringLayout.NORTH, endTextField, 24, SpringLayout.NORTH, startTextField);

					boolean tryEvent = true;
					while (tryEvent) {
						tryEvent = false;
						String[] options = { "Add", "Cancel" };
						JOptionPane pane = new JOptionPane(pan, JOptionPane.PLAIN_MESSAGE, JOptionPane.OK_CANCEL_OPTION,
								null, options, options[0]);
						JDialog dialog = pane.createDialog(screen,
								"Add Event (" + datePanel[buttonIndex].getDateString() + ")");
						dialog.setLocation(westPanel.getLocationOnScreen().x + 233,
								westPanel.getLocationOnScreen().y + 107);
						dialog.setVisible(true);
						dialog.dispose();
						System.out.println("Dialog:1 " + pane.getValue());
						if (pane.getValue() != null && pane.getValue().equals(options[0])) {
							try {
								System.out.println(yearOfCalendar);
								EventData newEvent = manager.createEvent((String) eventTextField.getText(),
										(String) startTextField.getText(), (String) endTextField.getText(),
										datePanel[buttonIndex].getDay(), datePanel[buttonIndex].getMonth(),
										datePanel[buttonIndex].getYear(), ((Color) jcb.getSelectedItem()).getRed(),
										((Color) jcb.getSelectedItem()).getGreen(),
										((Color) jcb.getSelectedItem()).getBlue());
								datePanel[buttonIndex].addButton(newEvent.getStartTime(), newEvent.getStartInt(),
										newEvent.getEndTime(), datePanel[buttonIndex].getDay(),
										datePanel[buttonIndex].getMonth(), datePanel[buttonIndex].getYear(),
										newEvent.getName(), newEvent.getColor().getRed(),
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
					return;
				}
			}
		}
	}

	/**
	 * Return the JColorBox of the ui
	 * 
	 * @return jcb the JColorBox
	 */
	public JColorBox getJCB() {
		jcb = new JColorBox(getColorOptions());
		jcb.addActionListener(this);
		return jcb;
	}

	/**
	 * Retuns the color of text
	 * 
	 * @return textColor the color of the text
	 */
	public Color getTextColor() {
		return textColor;
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
		fchooser.setBackground(panelColor);
		fchooser.setForeground(textColor);
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