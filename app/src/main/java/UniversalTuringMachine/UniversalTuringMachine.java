package UniversalTuringMachine;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

/**
 * This is the implementation of a universal turing machine.
 * For a mathematic operation, the transitions can be given
 * as a transition hashset, or imported from a csv file.
 * When providing a csv, start and end states must be provided
 * on first cell of the file.
 * Supports a stepper mode. If enabled, each calculation step
 * will be printed visually on the console. If disabled, only
 * the result will be shown on console.
 * @author kressjan
 * @version 0.2.1
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
		 */
		public UniversalTuringMachine(
				int number1, int number2, HashSet transitionSet, boolean stepByStep)
		{
				tapeList = new ArrayList<>();
				setCalculationNumbers(number1, number2);
				setTransitions(transitionSet);
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

		/**
		 * This method runs the turing machine and returns a true,
		 * as long as the calculation is still running.
		 * If the stepper mode is enabled, each step will be printed
		 * to the console. Else the output is only printed on completion.
		 * @return true, if still calculating
		 */
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
		public void setTransitions(HashSet<Transition> transitionSet) {
				HashSet<Transition> copy = (HashSet) transitionSet.clone();
				setRelevantStates(copy);
				this.transitionSet = copy;
		}

		private void calculateStep() {
				tapeStep();
				stepCounter++;
		}

		/**
		 * This method prints the current machine states and the visual tape
		 */
		private void getMachineOutput() {
				System.out.printf("\n%-30s%s\n%-30s%s\n%-30s%d\n%-30s%d%s\n",
						"Machine state:", state,
						"Accepting end state:", endState,
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

		private void setRelevantStates(HashSet<Transition> transitionSet) {
				Iterator<Transition> iterator = transitionSet.iterator();
				while(iterator.hasNext()) {
						Transition transition = iterator.next();
						if(transition.getDirection() == 'e') {
								this.state = transition.getState();
								this.endState = transition.getNewState();
								iterator.remove();
						}
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

		private void writeToTape(String value, char direction) {
				tapeList.set(tapePointer, value);
				try {
						moveTapePointer(direction);
				} catch (IllegalArgumentException e) {
						System.err.println(e.getMessage());
				}
		}



}