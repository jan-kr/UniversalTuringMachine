package UniversalTuringMachine;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.IllegalFormatConversionException;

/**
 * TransitionImporter for turing machine.
 * This importer can import the transitions of a csv and give back a transition hashset
 * containing the transitions of the csv.
 *
 * @author kressjan
 * @version 1.0.0
 */
public class TransitionImporter {
    private static boolean hasStartEndState = false;

    /**
     * Static transition set importer. Imports transitions from file provided.
     * The states for the start and end state are defined as a row with two cells.
     * They must be provided! Else a {@link IllegalArgumentException} is thrown.
     * Returns the {@link HashSet<Transition>} containing all transitions available
     * for the given operation.
     *
     * @param file csv or text file containing comma separated values for the transitions
     * @return {@link HashSet<Transition>} containing all {@link Transition}s for the operation to run
     */
    public static HashSet<Transition> importSet(File file) {
        HashSet<Transition> transitionSet = new HashSet<>();
        String line;
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            while ((line = reader.readLine()) != null) {
                hasStartEndState = addTransitionToSet(line, transitionSet);
            }
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
        if (!hasStartEndState) {
            throw new IllegalArgumentException("No start- and endstate definition line found! in transition file" + file.getPath());
        }
        return transitionSet;
    }

    private static boolean addTransitionToSet(String line, HashSet<Transition> transitionSet) {
        String[] elements = line.split(",");
        if (elements.length == 5) {
            if (elements[3].charAt(0) == 'e') {
                hasStartEndState = true;
            }
            if (elements[1].isEmpty()) {
                elements[1] = " ";
            }
            if (elements[2].isEmpty()) {
                elements[2] = " ";
            }
            transitionSet.add(new Transition(elements[0], elements[1], elements[2], elements[3].charAt(0), elements[4]));
        }
        return hasStartEndState;
    }
}
