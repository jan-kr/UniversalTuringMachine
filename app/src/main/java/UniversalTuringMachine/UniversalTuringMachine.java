package UniversalTuringMachine;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * @author kressjan
 * @version 0.0.1
 */
public class UniversalTuringMachine {

		private boolean             stepByStep;
		private int                 initialBlankCells = 30;
		private int                 viewSpan          = 15;
		private int                 stepCounter       = 0;
		private int                 number1           = 0;
		private int                 number2           = 0;
		private int                 tapePointer       = 1;
		private HashSet<Transition> transitionSet;
		private List<String>        tapeList;
		private String              state, endState;

		private static final String BLANK_SYMBOL = " ";
		private static final String NUMBER_SYMBOL = "0";
		private static final String SEPARATOR_SYMBOL = "1";

		/**
		 * Initialize a turing machine without parameters.
		 * Settings can be changed with setter methods.
		 */
		public UniversalTuringMachine() {
				tapeList = new ArrayList<>();
				tapePointer += initialBlankCells;
		}

		/**
		 * Initialize a turing machine with the given parameters.
		 * With this constructor, the initialized machine is ready to run.
		 * @param number1 number 1 of the calculation
		 * @param number2 number 2 of the calculation
 		 * @param transitionSet HashSet containing the {@link Transition}s
		 * @param stepByStep boolean that controlls the stepper mode
		 * @param startState start state of the calculation
		 * @param endState accepted end state of the calculation
		 */
		public UniversalTuringMachine(
				int number1, int number2, HashSet transitionSet,
				boolean stepByStep, String startState, String endState)
		{
				tapeList = new ArrayList<>();
				setCalculationNumbers(number1, number2);
				setTransitions(transitionSet, startState, endState);
				setStepperMode(stepByStep);
				tapePointer += initialBlankCells;
		}

		/**
		 * Returns the result of the calculation of the turing machine.
		 * Counts the number of NUMBER_SYMBOL elements on the tape.
		 * If called during operation, it delivers a wrong result!
		 * @return result of the operation
		 */
		public int getResult() {
				int result = 0;

				for (String tapeCell : tapeList) {
						if(tapeCell.equals(NUMBER_SYMBOL)) {
								result++;
						}
				}
				return result;
		}

		public boolean run() {
				boolean isRunning = true;

				calculateStep();
				if(stepByStep) {
						getMachineOutput();
				}

				if(endState.equals(state)) {
						getMachineOutput();
						isRunning = false;
				}

				return isRunning;
		}

		/**
		 * Set the numbers to calculate
		 * @param number1 first number of calculation
		 * @param number2 second number of calculation
		 */
		public void setCalculationNumbers(int number1, int number2) {
				if (number1 < 0 || number2 < 0) {
						throw new IllegalArgumentException("Illegal values for multiplication!");
				}
				this.number1 = number1;
				this.number2 = number2;
				setupTape(number1, number2);
		}

		/**
		 * This method toggles the stepper mode of the turing machine
		 * @param stepByStep true, if stepper mode enabled
		 */
		public void setStepperMode(boolean stepByStep) {
				this.stepByStep = stepByStep;
		}

		/**
		 * Set the transition set for the calculation
		 * @param transitionSet HashSet containing the available transitions
		 */
		public void setTransitions(HashSet<Transition> transitionSet, String startState, String endState) {
				this.transitionSet = transitionSet;
				this.state = startState;
				this.endState = endState;
		}

		private void calculateStep() {
				tapeStep();
				stepCounter++;
		}

		/**
		 * This method prints the current machine states and the visual tape
		 */
		private void getMachineOutput() {
				System.out.printf("\n%-30s%s\n%-30s%d%n%-30s%d%s%n",
						"Machine state:", state,
						"Number of steps:", stepCounter,
						"Pointer position on tape:" ,tapePointer ,
						getTape());
		}

		/**
		 * This method provides a string containing the visualization of the tape.
		 * It shows the tape track with the pointer in the middle and 15 tape cells
		 * left and right from the pointer.
		 * @return string of visual tape
		 */
		private String getTape() {
				StringBuilder pointerString = new StringBuilder("\nTape:\n");
				StringBuilder tapeString = new StringBuilder();

				for (int i = tapePointer - viewSpan; i <= tapePointer + viewSpan; i++) {
						tapeString.append(" | " + tapeList.get(i));
						if(i == tapePointer) {
								pointerString.append(" | v");
						} else {
								pointerString.append(" | -");
						}
				}
				pointerString.append(" |");
				tapeString.append(" |");

				pointerString.append("\n" + tapeString);
				return pointerString.toString();
		}

		private void moveTapePointer(char direction) {
				switch (direction) {
				case 'r':
						tapePointer++;
						break;
				case 'l':
						if(tapePointer - viewSpan > 0) {
								tapePointer--;
						}
						break;
				default:
						throw new IllegalArgumentException("Illegal direction provided!");
				}
		}

		private void setupTape(int number1, int number2) {
				for (int i = 0; i <= initialBlankCells; i++) {
						tapeList.add(BLANK_SYMBOL);
				}
				for (int i = 0; i < number1; i++) {
						tapeList.add(NUMBER_SYMBOL);
				}
				tapeList.add(SEPARATOR_SYMBOL);
				for (int i = 0; i < number2; i++) {
						tapeList.add(NUMBER_SYMBOL);
				}
				for (int i = 0; i < number1 * number2 + initialBlankCells; i++) {
						tapeList.add(BLANK_SYMBOL);
				}
		}

		private void tapeStep() {
				boolean transitionFound = false;
				for (Transition transition : transitionSet) {
						if(transition.getState().equals(state) && transition.getTapeValue().equals(tapeList.get(tapePointer)) && !transitionFound) {
								writeToTape(transition.getTapeValueNew(), transition.getDirection());
								state = transition.getNewState();
								transitionFound = true;
						}
				}

				if (!transitionFound) {
						throw new IllegalArgumentException("No possible transition found!");
				}
		}

		public void writeToTape(String value, char direction) {
				tapeList.set(tapePointer, value);
				try {
						moveTapePointer(direction);
				} catch (IllegalArgumentException e) {
						System.err.println(e.getMessage());
				}
		}



}