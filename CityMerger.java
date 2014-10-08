import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class CityMerger {

	protected static final String OUT_FILE = "merged-states.cities";
	protected static final String CITIES_PATH = "states-cities/";
	protected static final String CITIES_EXT = ".cities";
	protected static final List<String> VALID_STATES = Arrays.asList(
			"illinois", "iowa", "kansas", "minnesota", "missouri", "nebraska",
			"north-dakota", "south-dakota", "wisconsin");

	/**
	 * Retrieves a list of scanners for a given set of states' .cities files
	 * 
	 * @param states
	 *            an array of valid state names
	 * @return an array of Scanners of the appropriate state files
	 */
	protected static List<Scanner> scannersForStates(List<String> states)
			throws FileNotFoundException {
		String path;
		FileReader file;
		Scanner stateScanner;
		List<Scanner> scanners = new ArrayList<>(states.size());

		for (String state : states) {
			path = CITIES_PATH + state + CITIES_EXT;
			file = new FileReader(path);
			stateScanner = new Scanner(file);

			scanners.add(stateScanner);
		} // for

		return scanners;
	} // getScanners(List<String>)

	/**
	 * merge sorted file input into a given output stream
	 * 
	 * @param scanners
	 *            the scanners that point to the sorted files
	 * @param out
	 *            the PrintWriter with which output will be written
	 * @throws NoSuchElementException
	 * @throws IllegalStateException
	 */
	static void sortedFileMerge(List<Scanner> scanners, PrintWriter out)
			throws NoSuchElementException, IllegalStateException {
		Scanner scanner;
		PQItem item;

		// create the queue
		PriorityQueue queue = new PriorityQueue(scanners.size());

		// fill the queue with initial values
		for (int i = 0; i < scanners.size(); i++) {
			scanner = scanners.get(i);

			if (scanner.hasNextLine()) {
				queue.insert(new PQItem(scanner.nextLine(), i));
			} // while
		} // for

		// begin merging!
		while (!queue.isEmpty()) {
			// get the smallest item
			item = queue.remove();

			// write item to outfile
			out.println(item.value);

			// get another item from item's source array
			scanner = scanners.get(item.source);

			if (scanner.hasNextLine()) {
				queue.insert(new PQItem(scanner.nextLine(), item.source));
			} // if
		} // while

		return;
	} // merge(ArrayList<ArrayList<Integer>>)

	/**
	 * Merge the cities from given states into one alphabetically ascending file
	 * 
	 * @param args
	 *            the names of valid states
	 */
	public static void main(String[] args) {
		PrintWriter out;
		List<Scanner> scanners = new ArrayList<>();
		List<String> states = new ArrayList<>(args.length);

		// ensure all arguments are valid
		for (String arg : args) {
			if (VALID_STATES.contains(arg)) {
				states.add(arg);
			} else {
				// argument was not valid state
				System.err.println("Invalid state: " + arg
						+ ". Must choose from valid states:");
				System.err.println(VALID_STATES);

				return;
			} // if...else
		} // for

		try {
			out = new PrintWriter(OUT_FILE);
			scanners = scannersForStates(states);

			sortedFileMerge(scanners, out);

			out.close();

		} catch (Exception e) {
			// catch any file exceptions
			System.err.println(e);

		} finally {
			// always close all the scanners
			for (Scanner scanner : scanners) {
				scanner.close();
			} // for
		} // try...catch...finally
	} // main(String[])
} // class MergeUtils