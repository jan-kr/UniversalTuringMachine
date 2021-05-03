package UniversalTuringMachine;

/**
 * Transition of {@link UniversalTuringMachine}. A transition is describing,
 * what operations can be made from each step. It is used to change states
 * according to the operation logic needed for the calculation.
 *
 * @author kressjan
 * @version 1.0.0
 */
public class Transition {

    private String state;
    private String tapeValue, tapeValueNew;
    private char direction;
    private String newState;

    /**
     * Instantiates a new transition with the given source state, tape value, value to write
     * to tape, direction of the r/w head and the next state of the tm.
     *
     * @param state        current state of machine
     * @param tapeValue    current tape value
     * @param tapeValueNew tape value to write
     * @param direction    direction to move r/w head
     * @param newState     next state of machine
     */
    public Transition(String state, String tapeValue, String tapeValueNew, char direction, String newState) {
        if (direction != 'r' && direction != 'l' && direction != 'e') {
            throw new IllegalArgumentException("Illegal direction given!");
        }
        this.state = state;
        this.tapeValue = tapeValue;
        this.tapeValueNew = tapeValueNew;
        this.direction = direction;
        this.newState = newState;
    }

    /**
     * Get the current state of the turing machine
     *
     * @return current state of turing machine
     */
    public String getState() {
        return state;
    }

    /**
     * Get the tape value of the cell where the r/w head is
     *
     * @return tape value at current r/w head position
     */
    public String getTapeValue() {
        return tapeValue;
    }

    /**
     * Get the value, which should be written to the tape
     *
     * @return value to write to tape
     */
    public String getTapeValueNew() {
        return tapeValueNew;
    }

    /**
     * Get the direction the r/w head is moving with this transition
     *
     * @return transition r/w head movement direction
     */
    public char getDirection() {
        return direction;
    }

    /**
     * Get the next state of the turing machine
     *
     * @return next state of turing machine
     */
    public String getNewState() {
        return newState;
    }
}
