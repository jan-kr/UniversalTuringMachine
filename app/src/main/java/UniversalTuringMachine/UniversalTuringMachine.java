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

		public UniversalTuringMachine(
				int number1, int number2, HashSet transitionSet,
				boolean stepByStep, String startState, String endState)
		{
				if (number1 < 0 || number2 < 0) {
						throw new IllegalArgumentException("Illegal values for multiplication!");
				}

				tapeList = new ArrayList<>();
				this.number1 = number1;
				this.number2 = number2;
				this.transitionSet = transitionSet;
				this.stepByStep = stepByStep;
				this.state = startState;
				this.endState = endState;
				tapePointer += initialBlankCells;

				setupTape(number1, number2);
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

		/**
		 * This method provides a string containing the visualization of the tape.
		 * It shows the tape track with the pointer in the middle and 15 tape cells
		 * left and right from the pointer.
		 * @return string of visual tape
		 */
		public String getTape() {
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

		/**
		 * This method prints the current machine states and the visual tape
		 */
		public void getMachineOutput() {
				System.out.printf("\n%-30s%s\n%-30s%d%n%-30s%d%s%n",
						"Machine state:", state,
						"Number of steps:", stepCounter,
						"Pointer position on tape:" ,tapePointer ,
						getTape());
		}

		public void moveTapePointer(char direction) {
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

		public boolean run() {
				boolean isRunning = true;

				calcStep();
				if(stepByStep) {
						getMachineOutput();
				}

				if(endState.equals(state)) {
						getMachineOutput();
						isRunning = false;
				}

				return isRunning;
		}

		private void calcStep() {

				tapeStep();
				stepCounter++;

		}

		public void writeToTape(String value, char direction) {
				tapeList.set(tapePointer, value);
				try {
						moveTapePointer(direction);
				} catch (IllegalArgumentException e) {
						System.err.println(e.getMessage());
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

		public int getResult() {
				int result = 0;

				for (String tapeCell : tapeList) {
						if(tapeCell.equals(NUMBER_SYMBOL)) {
								result++;
						}
				}
				return result;
		}
}