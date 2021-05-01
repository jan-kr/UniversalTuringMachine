package UniversalTuringMachine;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;

public class TransitionImporter {

		public static HashSet<Transition> importSet(File file) {
				HashSet<Transition> transitionSet = new HashSet<>();
				String line;
				try {
						BufferedReader reader = new BufferedReader(new FileReader(file));
						while((line = reader.readLine()) != null) {
								String[] elements = line.split(",");
								if(elements.length == 2) {
										transitionSet.add(new Transition(elements[0], null, null, 'e', elements[1]));
								} else {
										if(elements[1].isEmpty()) {
												elements[1] = " ";
										}
										if(elements[2].isEmpty()) {
												elements[2] = " ";
										}
										transitionSet.add(new Transition(elements[0], elements[1], elements[2], elements[3].charAt(0), elements[4]));
								}
						}
				} catch (IOException e) {
						System.err.println(e.getMessage());
				}
				return transitionSet;
		}
}
