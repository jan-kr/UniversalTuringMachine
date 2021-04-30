package UniversalTuringMachine;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class UniversalTuringMachine {

		private boolean stepByStep;
		private int stepCounter = 0;
		private int multiplier = 0;
		private int multiplicant = 0;
		private int tapePointer = 16;
		private HashSet<Transition> transitionSet;
		private List<String>    tapeList;
		private String state, endState;

		private static final String BLANK_SYMBOL = " ";
		private static final String NUMBER_SYMBOL = "0";
		private static final String SEPARATOR_SYMBOL = "1";

		public UniversalTuringMachine(
				int multiplier, int multiplicant, HashSet transitionSet,
				boolean stepByStep, String startState, String endState)
		{
				/*if (multiplier < 0 || multiplicant < 0) {
						throw new IllegalArgumentException("Illegal values for multiplication!");
				}*/

				tapeList = new ArrayList<>();
				this.multiplier = multiplier;
				this.multiplicant = multiplicant;
				//this.transitionSet = transitionSet;
				this.stepByStep = stepByStep;
				this.state = startState;
				this.endState = endState;
				tapePointer = tapePointer + multiplier;

				setupTape(multiplier, multiplicant);
		}

		private void setupTape(int multiplier, int multiplicant) {
				for (int i = 0; i <= 15; i++) {
						tapeList.add(BLANK_SYMBOL);
				}
				for (int i = 0; i < multiplier; i++) {
						tapeList.add(NUMBER_SYMBOL);
				}
				tapeList.add(SEPARATOR_SYMBOL);
				for (int i = 0; i < multiplicant; i++) {
						tapeList.add(NUMBER_SYMBOL);
				}
				for (int i = 0; i < multiplier * multiplicant + 16; i++) {
						tapeList.add(BLANK_SYMBOL);
				}
		}

		public String getTape() {
				StringBuilder pointerString = new StringBuilder();
				StringBuilder tapeString = new StringBuilder();

				for (int i = tapePointer - 15; i <= tapePointer + 15; i++) {
						tapeString.append(" | " + tapeList.get(i));
						if(i == tapePointer) {
								pointerString.append(" | v");
						} else {
								pointerString.append(" |  ");
						}
				}
				pointerString.append(" |");
				tapeString.append(" |");

				pointerString.append("\n" + tapeString);
				return pointerString.toString();
		}

		public void getStepOutput() {
				System.out.printf("\n%-20s%s\n%-20s%d\nTape:%n%s", "Machine state:", state, "Number of steps:", stepCounter, getTape());
		}

		public void moveTapePointer(char direction) {
				switch (direction) {
				case 'r':
						tapePointer++;
						break;
				case 'l':
						tapePointer--;
						break;
				default:
						throw new IllegalArgumentException("Illegal direction provided!");
				}
		}
}