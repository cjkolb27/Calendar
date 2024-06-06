// Test UI Window
package ui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.YearMonth;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
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
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;

import events.EventData;
import manager.CalendarManager;
import util.SortedDateList;

/**
 * 
 */
public class UI extends JFrame implements ActionListener, MouseWheelListener {

	/** Settings window state */
	private ScreenState windowState = null;
	/** Frame that holds all components on the screen */
	private static JFrame screen;
	/** Holds the panel for all button days */
	private JPanel panel;
	/** North Panel */
	private JPanel northPanel;
	/** West Panel */
	private JPanel westPanel;
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
	/** Stores all settings as an array of strings */
	private static String[] settings;
	/** Days per month */
	private static int[] daysPerMonth = { 31, 29, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 };
	/// ** Weeks per month */
	// private static int[] weeksPerMonth = { 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1 };
	/** Position of week that changes month JLabel */
	private static int[] transitionWeek = { 31, 29, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 };
	/** Buttons for every calendar day */
	private JButton[] buttons;
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
	/** Scroll Frame color */
	private static Color panelColor = new Color(20, 20, 20);
	/** Odd month color */
	private static Color oddMonthColor = new Color(38, 38, 38);
	/** Even month color */
	private static Color evenMonthColor = new Color(26, 26, 26, 255);
	/** Event color */
	private static Color eventColor = new Color(255, 153, 161, 255);
	/** File menu title */
	private static final String FILE_MENU_TITLE = "File";
	/** Load calendar title */
	private static final String LOADCAL = "Load Calendar";
	/** Edit Settings title */
	private static final String EDITSET = "Edit Settings";
	/** Label for holding the month name */
	private JLabel month;
	/** Year of calendar */
	private int yearOfCalendar;
	/** Month of calendar */
	private int monthOfCalendar;
	/** Days in the calendar */
	private int daysInCalendar;
	/** Current Month shown on the calendar */
	private int scrollMonth;
	/** Keeps track of scroll bar location */
	private double scrollLocation;
	/** Keeps track of scroll bar height */
	private double scrollHeight;

	private JMenuItem removeAll;
	private JMenuItem addAll;

	public UI() {
		super();
		env = GraphicsEnvironment.getLocalGraphicsEnvironment();
		ev = env.getDefaultScreenDevice();
		settingsChanged = false;
		manager = new CalendarManager();
		screen = new JFrame();
		screen.setSize(Toolkit.getDefaultToolkit().getScreenSize().width,
				Toolkit.getDefaultToolkit().getScreenSize().height);
		screen.setDefaultCloseOperation(EXIT_ON_CLOSE);
		screen.setLayout(new BorderLayout());

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
				// you can choose to make the screen fit or not
				screen.setExtendedState(Frame.MAXIMIZED_BOTH);
				screen.setVisible(true);
			} else {
				screen.setUndecorated(false);
				// you can choose to make the screen fit or not
				screen.setExtendedState(Frame.MAXIMIZED_BOTH);
			}
			windowState = ScreenState.Windowed;
			System.out.println("State Changed to Windowed");
		}
		/**
		 * if (!settingsChanged) { settingsChanged = true; } else if (stateChanged) {
		 * //Container c = getContentPane(); //c.setLayout(new GridBagLayout());
		 * //screen.setContentPane(c); //screen.setVisible(true);
		 * System.out.println("State Change"); }
		 */
	}

	private void setUpScreen() {
		scrollFrame = new JScrollPane(panel, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scrollFrame.setBackground(panelColor);
		scrollFrame.setBorder(BorderFactory.createEtchedBorder());
		scrollFrame.getVerticalScrollBar().setUnitIncrement(20);
		scrollFrame.addMouseWheelListener(this);

		northPanel = new JPanel(new FlowLayout());
		westPanel = new JPanel();
		northPanel.setBackground(Color.DARK_GRAY);
		westPanel.setBackground(Color.DARK_GRAY);
		northPanel.setPreferredSize(new Dimension(1, 100));
		westPanel.setPreferredSize(new Dimension(265, 1));
		month = new JLabel("January");
		month.setFont(new Font("Comic Sans", Font.BOLD, 40));
		month.setForeground(Color.WHITE);
		northPanel.add(month);
		screen.add(westPanel, BorderLayout.WEST);

		JPanel calendarScrollingPan = new JPanel(new BorderLayout());
		calendarScrollingPan.add(northPanel, BorderLayout.NORTH);
		calendarScrollingPan.add(scrollFrame, BorderLayout.CENTER);
		screen.add(calendarScrollingPan, BorderLayout.CENTER);
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

		System.out.println(sdl.size());

		Iterator<EventData> it = sdl.iterator();
		EventData currentData;
		if (it.hasNext()) {
			currentData = it.next();
			System.out.println("Not Null");
			System.out.println("Printed Data: " + currentData.getDay() + " " + currentData.getMonth() + " "
					+ currentData.getYear());
		} else {
			currentData = null;
		}

		int currentDay = 0;
		for (int i = 0; i < 12; i++) {
			cal.set(yearOfCalendar, i, 1);
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
				buttons[currentDay] = new JButton();
				buttons[currentDay].setPreferredSize(new Dimension(75, 75));
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
				if (cal.get(Calendar.DAY_OF_MONTH) == j + 1 && cal.get(Calendar.MONTH) == i + 1 && cal.get(Calendar.YEAR) == yearOfCalendar) {
					label.setForeground(eventColor);
					buttons[currentDay].setBorder(new LineBorder(eventColor));
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
							currentData.getYear(), currentData.getName());

					if (it.hasNext()) {
						currentData = it.next();
						System.out.println("Printed Data: " + currentData.getDay() + " " + currentData.getMonth() + " "
								+ currentData.getYear());
					} else {
						currentData = null;
					}
				}
				// datePanel[currentDay].addLabel(630, "Work");
				datePanel[currentDay].getPanel().setBackground(null);
				buttons[currentDay].add(datePanel[currentDay].getPanel(), BorderLayout.WEST);
				panel.add(buttons[currentDay]);
				// System.out.println("Day: " + currentDay + "printed");
				currentDay++;
			}
			daysInCalendar = currentDay;
		}
		panel.setLayout(
				new GridLayout((int) Math.ceil((cal.get(Calendar.DAY_OF_WEEK) + daysInCalendar) / 7.0), 7, 1, 1));
		panel.setAlignmentX(SwingConstants.RIGHT);
		panel.setAlignmentY(SwingConstants.BOTTOM);
		panel.setPreferredSize(new Dimension(500, 10000));
		panel.setBackground(panelColor);
	}

	private void setScreenWindow() {
		screen.dispose();
		screen.setVisible(false);
		env = GraphicsEnvironment.getLocalGraphicsEnvironment();
		ev = env.getDefaultScreenDevice();
		manager = new CalendarManager();
		screen = new JFrame();
		screen.setSize(Toolkit.getDefaultToolkit().getScreenSize().width,
				Toolkit.getDefaultToolkit().getScreenSize().height);
		screen.setDefaultCloseOperation(EXIT_ON_CLOSE);
		screen.setLayout(new BorderLayout());

		panel = new JPanel(new GridLayout(54, 7, 1, 1));

		setAllDates();

		buildButtonDays();

		createMenuBar();
		/**
		 * settings = manager.defaultSettings(); ScreenState state =
		 * ScreenState.Windowed; if ("Borderless".equals(settings[1])) { state =
		 * ScreenState.Borderless; } else if ("Fullscreen".equals(settings[1])) { state
		 * = ScreenState.Fullscreen; } changeWindow(state);
		 */

		setUpScreen();

		// screen.setVisible(true);
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
		loadCalendar = new JMenuItem(LOADCAL);
		editSettings = new JMenuItem(EDITSET);
		quit = new JMenuItem("Quit");
		removeAll = new JMenuItem("Remove All");
		addAll = new JMenuItem("Add All");

		loadCalendar.addActionListener(this);
		editSettings.addActionListener(this);
		removeAll.addActionListener(this);
		addAll.addActionListener(this);
		quit.addActionListener(this);

		menu.add(loadCalendar);
		menu.add(editSettings);
		menu.add(removeAll);
		menu.add(addAll);
		menu.add(quit);
		menuBar.add(menu);
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
						month.setText(ALLMONTHNAMES[scrollMonth]);
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
						month.setText(ALLMONTHNAMES[scrollMonth]);
						i = 12;
					}
				}
			}
			if (scrollLocation == scrollHeight) {
				scrollMonth = 11;
				month.setText(ALLMONTHNAMES[scrollMonth]);
			}
		} catch (Exception e) {
			// Nothing
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == loadCalendar) {
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
				System.out.println(test);
				System.out.println(combo.getSelectedItem());
				if (test == -1 || test == 2) {
					System.out.println("Cancelled or Closed");
				} else {
					settingsChanged = true;
					changeWindow((ScreenState) combo.getSelectedItem());
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
		} else if (e.getSource() == quit) {
			System.exit(0);
		}
		if (e.getSource() == addAll) {
			for (int i = 0; i < 366; i++) {
				if (datePanel[i].size() > 0) {
					datePanel[i].addAllButtons();
				}
			}
			screen.setVisible(true);
		} else if (e.getSource() == removeAll) {
			for (int i = 0; i < 366; i++) {
				if (datePanel[i].size() > 0) {
					datePanel[i].removeAllButtons();
				}
			}
			screen.setVisible(true);
		} else {
			for (int i = 0; i < 366; i++) {
				if (e.getSource() == buttons[i]) {
					System.out.println("Button Listener");
					JLabel lab1 = new JLabel("Event Name");
					JTextField jtf1 = new JTextField();
					JLabel lab2 = new JLabel("Start Time");
					JTextField jtf2 = new JTextField();
					JLabel lab3 = new JLabel("End Time");
					JTextField jtf3 = new JTextField();
					JPanel pan = new JPanel();
					pan.setSize(new Dimension(500, 500));
					pan.setLayout(new GridLayout(3, 2, 1, 1));
					pan.add(lab1);
					pan.add(jtf1);
					pan.add(lab2);
					pan.add(jtf2);
					pan.add(lab3);
					pan.add(jtf3);
					boolean tryEvent = true;
					while (tryEvent) {
						tryEvent = false;
						int optionSelected = JOptionPane.showConfirmDialog(screen, pan, "Test",
								JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
						System.out.println(optionSelected);
						if (optionSelected == 0) {
							try {
								EventData newEvent = manager.createEvent((String) jtf1.getText(),
										(String) jtf2.getText(), (String) jtf3.getText(), datePanel[i].getDay(),
										datePanel[i].getMonth(), datePanel[i].getYear());
								datePanel[i].addButton(newEvent.getStartTime(), newEvent.getStartInt(),
										newEvent.getEndTime(), datePanel[i].getDay(), datePanel[i].getMonth(),
										datePanel[i].getYear(), newEvent.getName());
								screen.setVisible(true);
								repaint();
								validate();
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
		repaint();
		validate();
	}

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
		/** Size of dateLabel */
		private int size;

		public DatePanel(int day, int month, int year) {
			this.day = day;
			this.month = month;
			this.year = year;
			head = new DateButton(null, 0, null, day, month, year, null);
			panel = new JPanel();
			panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
			size = 0;
		}

		public JPanel getPanel() {
			return panel;
		}

		public int getDay() {
			return day;
		}

		public int getMonth() {
			return month;
		}

		public int getYear() {
			return year;
		}

		public void addButton(String start, int startTime, String end, int day, int month, int year, String event) {
			DateButton newLabel = new DateButton(start, startTime, end, day, month, year, event);
			newLabel.getButton().addActionListener(this);
			newLabel.getButton().setPreferredSize(new Dimension(300, 0));
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

		public void editButton(String start, int originalStartTime, int startTime, String end, int day, int month,
				int year, String event) {
			if (size == 0) {
				return;
			}
			removeButton(originalStartTime);
			addButton(start, startTime, end, day, month, year, event);
		}

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

		public int size() {
			return size;
		}

		public DateButton getHead() {
			return head;
		}

		public void removeAllButtons() {
			DateButton current = head;
			while (current.next != null) {
				// System.out.println(current.startTime);
				getPanel().remove(current.next.getButton());
				current = current.next;
			}
		}

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

		@Override
		public void actionPerformed(ActionEvent e) {
			System.out.println("Action");
			DateButton current = head.next;
			while (current != null) {
				if (e.getSource() == current.getButton()) {
					System.out.println("Button pressed");
					JLabel lab1 = new JLabel("Event Name");
					JTextField jtf1 = new JTextField(current.getEvent());
					JLabel lab2 = new JLabel("Start Time");
					JTextField jtf2 = new JTextField(current.start);
					JLabel lab3 = new JLabel("End Time");
					JTextField jtf3 = new JTextField(current.end);
					JPanel pan = new JPanel();
					pan.setSize(new Dimension(500, 500));
					pan.setLayout(new GridLayout(3, 2, 1, 1));
					pan.add(lab1);
					pan.add(jtf1);
					pan.add(lab2);
					pan.add(jtf2);
					pan.add(lab3);
					pan.add(jtf3);
					boolean tryEvent = true;
					while (tryEvent) {
						tryEvent = false;
						String[] choices = { "Update", "Delete", "Cancel" };
						int optionSelected = JOptionPane.showOptionDialog(getScreen(), pan, "Test",
								JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null, choices, choices[0]);
						System.out.println(optionSelected);
						if (optionSelected == 0) {
							try {
								EventData newEvent = getManager().editEvent(
										(double) (year + (((month * 31) + (day)) * .001)), current.getStartTime(),
										jtf1.getText(), jtf2.getText(), jtf3.getText(), getDay(), getMonth(),
										getYear());
								editButton(newEvent.getStartTime(), current.getStartTime(), newEvent.getStartInt(),
										newEvent.getEndTime(), getDay(), getMonth(), getYear(), newEvent.getName());
								screen.setVisible(true);
								getScreen().repaint();
								getScreen().validate();
								return;
							} catch (Exception e1) {
								JOptionPane.showMessageDialog(screen, e1.getMessage());
								tryEvent = true;
							}
						} else if (optionSelected == 1) {
							try {
								getManager().removeEvent((double) (year + (((month * 31) + (day)) * .001)),
										current.getStartTime());
								removeButton(current.getStartTime());
								screen.setVisible(true);
								getScreen().repaint();
								getScreen().validate();
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

		public DateButton(String start, int startTime, String end, int day, int month, int year, String event) {
			setStart(start);
			setStartTime(startTime);
			setEnd(end);
			setDay(day);
			setMonth(month);
			setYear(year);
			this.event = event;
			button = new JButton(start + " " + event);
			button.setContentAreaFilled(false);
			button.setFocusable(false);
			button.setBorderPainted(false);
			button.setFont(new Font("Comic Sans", Font.BOLD, 15));
			button.setMargin(new Insets(0, 0, 0, 0));
			button.setIcon(null);
			button.setForeground(eventColor);
			next = null;
		}

		public void setDay(int day) {
			this.day = day;
		}

		public void setMonth(int month) {
			this.month = month;
		}

		public void setYear(int year) {
			this.year = year;
		}

		public int getDay() {
			return day;
		}

		public int getMonth() {
			return month;
		}

		public int getYear() {
			return year;
		}

		public void setStart(String start) {
			this.start = start;
		}

		public void setStartTime(int startTime) {
			this.startTime = startTime;
		}

		public void setEnd(String end) {
			this.end = end;
		}

		public int getStartTime() {
			return startTime;
		}

		public String getEvent() {
			return event;
		}

		public JButton getButton() {
			return button;
		}

		public void printToCMD() {
			System.out.println(start + " " + end + " " + day + " " + month + " " + year + " " + event);
		}

	}

	public static CalendarManager getManager() {
		return manager;
	}

	public static JFrame getScreen() {
		return screen;
	}

	public static DatePanel[] getDatePanel() {
		return datePanel;
	}

	private String getFileName(boolean load) {
		JFileChooser fchooser = new JFileChooser("C:\\Users\\Caleb\\Documents\\CalendarData");
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