package util;

/**
 * Used for determining which actions a user can do in the preset event button
 * JOptionPane
 * 
 * @author Caleb Kolb
 */
public class PresetStateMachine {
	/** Current state in the automaton */
	PresetState currentState;

	/**
	 * Sets the current state to the start
	 */
	public PresetStateMachine() {
		currentState = PresetState.start;
	}

	/**
	 * The state is change if the given input action is allowed for the current
	 * state. True or false is returned if the state has change or not
	 * 
	 * @param input a string of an action performed
	 * @return boolean, true if state changed, false if not
	 */
	public boolean changeState(String input) {
		if (currentState == PresetState.start) {
			if ("add".equals(input)) {
				currentState = PresetState.adding;
				return true;
			} else if ("selected".equals(input)) {
				currentState = PresetState.selected;
				return true;
			} else
				return false;
		} else if (currentState == PresetState.adding) {
			if ("selected".equals(input)) {
				currentState = PresetState.selected;
				return true;
			} else if ("save".equals(input)) {
				currentState = PresetState.selected;
				return true;
			} else if ("cancel".equals(input)) {
				currentState = PresetState.start;
				return true;
			} else
				return false;
		} else if (currentState == PresetState.selected) {
			if ("add".equals(input)) {
				currentState = PresetState.adding;
				return true;
			} else if ("selected".equals(input)) {
				currentState = PresetState.selected;
				return true;
			} else if ("remove".equals(input)) {
				currentState = PresetState.start;
				return true;
			} else if ("save".equals(input)) {
				currentState = PresetState.selected;
				return true;
			} else if ("cancel".equals(input)) {
				currentState = PresetState.selected;
				return true;
			} else
				return false;
		} else
			return false;
	}

	/**
	 * Returns the current state
	 * 
	 * @return currentState the PresetState of the machine
	 */
	public PresetState getState() {
		return currentState;
	}

	/**
	 * All state of PresetState
	 */
	public enum PresetState {
		/** Start state */
		start,
		/** Adding a preset state */
		adding,
		/** Preset selected state */
		selected
	}
}
