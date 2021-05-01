package UniversalTuringMachine;

public class Transition {

		private String state;
		private String tapeValue, tapeValueNew;
		private char direction;
		private String newState;


		public Transition(String state, String tapeValue, String tapeValueNew, char direction, String newState) {
				if(direction != 'r' && direction != 'l' && direction != 'e') {
						throw new IllegalArgumentException("Illegal direction given!");
				}
				this.state = state;
				this.tapeValue = tapeValue;
				this.tapeValueNew = tapeValueNew;
				this.direction = direction;
				this.newState = newState;
		}

		public String getState() {
				return state;
		}

		public String getTapeValue() {
				return tapeValue;
		}

		public String getTapeValueNew() {
				return tapeValueNew;
		}

		public char getDirection() {
				return direction;
		}

		public String getNewState() {
				return newState;
		}
}
