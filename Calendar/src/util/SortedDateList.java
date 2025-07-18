package util;

import java.util.Iterator;

import events.EventData;

/**
 * List for sorting events by their dates
 * 
 * @author Caleb Kolb
 * @param <E> param of the list
 */
public class SortedDateList<E> {
	/** First ListNode */
	ListNode front;
	/** Last ListNode */
	ListNode back;
	/** Size of list */
	private int size;
	/** Version history of list */
	private int version;

	/**
	 * Sets the size of the list to 0, and sets front/back to null
	 */
	public SortedDateList(int version) {
		this.version = version;
		size = 0;
		front = null;
		back = null;
	}

	/**
	 * Checks if there is a date with the same startTime
	 * 
	 * @param date      the date of the event
	 * @param startTime the start time of the event
	 * @return boolean true if no duplicate and false if duplicate
	 */
	public boolean checkValue(double date, int startTime) {
		ListNode temp = front;
		while (temp != null) {
			if (temp.date == date && temp.startTime == startTime) {
				return false;
			}
			temp = temp.next;
		}
		return true;
	}

	/**
	 * Adds an event in order of the date
	 * 
	 * @param data      the data of the event
	 * @param date      the date of the event
	 * @param startTime the start time of an event
	 */
	public void add(EventData data, double date, int startTime) {
		if (data == null) {
			throw new NullPointerException();
		}
		if (!checkValue(date, startTime)) {
			return;
			// throw new IllegalArgumentException("Duplicate Dates");
		}
		if (size > 1) {
			if (front.date > date || (front.date == date && front.startTime >= startTime)) {
				ListNode temp = new ListNode(data, date, startTime, front, null);
				front.prev = temp;
				temp.next = front;
				front = temp;
				size++;
				return;
			}
			if (back != null && (back.date < date || (back.date == date && back.startTime <= startTime))) {
				ListNode temp = new ListNode(data, date, startTime, null, back);
				back.next = temp;
				temp.prev = back;
				back = temp;
				size++;
				return;
			}
			ListNode current = front;
			while (current.next != null) {
				if (current.next.date > date || (current.next.date == date && current.next.startTime >= startTime)) {
					ListNode temp = new ListNode(data, date, startTime, null, null);
					ListNode temp2 = current.next;
					current.next = temp;
					temp.prev = current;
					temp.next = temp2;
					temp2.prev = temp;
					size++;
					return;
				}
				current = current.next;
			}
		} else if (size == 1 && (front.date > date || (front.date == date && front.startTime >= startTime))) {
			back = new ListNode((EventData) null, 0, 0, null, null);
			back = front;
			front = new ListNode(data, date, startTime, back, null);
			back.prev = front;
			size++;
			return;
		} else if (size == 1) {
			back = new ListNode(data, date, startTime, null, front);
			front.next = back;
			back.prev = front;
			size++;
			return;
		} else if (size == 0) {
			front = new ListNode(data, date, startTime, null, null);
			size++;
			return;
		}
		throw new IllegalArgumentException("Date not found");
	}

	/**
	 * Gets a E at a double in the list
	 * 
	 * @param date      the date of the event
	 * @param startTime the start time of an event
	 * @return current the current event
	 */
	public EventData get(double date, int startTime) {
		ListNode current = front;
		if (size == 0) {
			return null;
		}
		if (current.date == date && current.startTime == startTime) {
			return current.data;
		}
		while (current.next != null) {
			if (current.next.date == date && current.next.startTime == startTime) {
				return current.next.data;
			}
			current = current.next;
		}
		return null;
	}

	/**
	 * Returns the value of an index
	 * 
	 * @param index the index in the list
	 * @return E the E at index
	 */
	public EventData getAtIndex(int index) {
		ListNode current = front;
		int in = index;
		if (in < 0 || in > size - 1) {
			throw new IllegalArgumentException("Cannot get an out of bounds index");
		}
		while (current.next != null && in > 0) {
			current = current.next;
			in--;
		}

		return current.data;
	}

	/**
	 * Removes a given event from a list
	 * 
	 * @param data      the event
	 * @param startTime as an integer
	 */
	public void removeE(EventData data, int startTime) {
		if (data == null) {
			throw new NullPointerException();
		}
		if (size == 0) {
			throw new IllegalArgumentException("Cannot remove from an empty list");
		}
		ListNode current = front;
		if ((current.data == data || current.next.data.getTimestamp().equals(data.getTimestamp())) && current.startTime == startTime) {
			if (size == 1) {
				front = null;
				size--;
				return;
			}
			if (size == 2) {
				front = front.next;
				front.prev = null;
				back = null;
				size--;
				return;
			}
			front = front.next;
			front.prev = null;
			size--;
			return;
		}
		while (current.next != null) {
			if ((current.next.data == data || current.next.data.getTimestamp().equals(data.getTimestamp())) && current.next.startTime == startTime) {
				if (current.next.next == null) {
					current.next = current.next.next;
					back = current.next;
					size--;
					return;
				} else {
					current.next.next.prev = current;
					current.next = current.next.next;
					size--;
					return;
				}
			}
			current = current.next;
		}
		throw new IllegalArgumentException("Event not in list");
	}

	/**
	 * Removes an event with the given date
	 * 
	 * @param date      the date of the event
	 * @param startTime as an integer
	 */
	public void removeD(double date, int startTime) {
		if (size == 0) {
			throw new IllegalArgumentException("Cannot remove from an empty list");
		}
		ListNode current = front;
		if (current.date == date && current.startTime == startTime) {
			if (size == 1) {
				front = null;
				size--;
				return;
			}
			if (size == 2) {
				front = front.next;
				front.prev = null;
				back = null;
				size--;
				return;
			}
			front = front.next;
			front.prev = null;
			size--;
			return;
		}
		while (current.next != null) {
			if (current.next.date == date && current.next.startTime == startTime) {
				if (current.next.next == null) {
					current.next = current.next.next;
					back = current;
					size--;
					return;
				} else {
					current.next.next.prev = current;
					current.next = current.next.next;
					size--;
					return;
				}
			}
			current = current.next;
		}
		throw new IllegalArgumentException("Event not in list");
	}

	/**
	 * Gets and returns the size of the list
	 * 
	 * @return size the size of the list
	 */
	public int size() {
		return size;
	}
	
	/**
	 * Returns the version history of the calendar
	 * 
	 * @return version the version of the calendar
	 */
	public int getVersion() {
		return version;
	}
	
	/**
	 * Sets the version history
	 * 
	 * @param version the version of the calendar
	 */
	public void setVersion(int version) {
		this.version = version;
	}
	
	public void printToCMD() {
		Iterator<EventData> it = iterator();
		while(it.hasNext()) {
			EventData event = it.next();
			System.out.println(event.toString());
		}
	}

	/**
	 * Creates an iterable list of of events in order
	 * 
	 * @return DateIterator the iterator of all events
	 */
	public Iterator<EventData> iterator() {
		return new DateIterator();
	}

	/**
	 * Used for defining what a DateIterator is allowed to do.
	 * 
	 * @author Caleb Kolb
	 */
	private class DateIterator implements Iterator<EventData> {
		/** current ListNode */
		private ListNode current;

		public DateIterator() {
			current = new ListNode(null, 0, 0, null, null);
			current.next = front;
		}

		@Override
		public boolean hasNext() {
			return current.next != null;
		}

		@Override
		public EventData next() {
			if (hasNext()) {
				current = current.next;
				return current.data;
			} else {
				throw new IllegalArgumentException("No more elements left");
			}
		}

	}

	/**
	 * Private class for structuring the ListNode
	 * 
	 * @author Caleb Kolb
	 */
	private class ListNode {
		/** Data of the list node */
		private EventData data;
		/** Date of event */
		private double date;
		/** Start Time of event */
		private int startTime;
		/** The next list node */
		private ListNode next;
		/** The previous list node */
		@SuppressWarnings("unused")
		private ListNode prev;

		/**
		 * ListNode structure and holds data for data, date, next and prev.
		 * 
		 * @param data      the data of the event
		 * @param date      the date of the event
		 * @param startTime as an integer
		 * @param next      the next event
		 * @param prev      the previous event
		 */
		public ListNode(EventData data, double date, int startTime, ListNode next, ListNode prev) {
			setData(data);
			setDate(date);
			setStartTime(startTime);
			setNext(next);
			setPrev(prev);
		}

		private void setData(EventData data) {
			this.data = data;
		}

		private void setDate(double date) {
			this.date = date;
		}

		private void setStartTime(int startTime) {
			this.startTime = startTime;
		}

		private void setNext(ListNode next) {
			this.next = next;
		}

		private void setPrev(ListNode prev) {
			this.prev = prev;
		}

		public EventData getData() {
			return data;
		}
	}
}
