import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
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
	 * 
	 * @param states
	 *            an array of state names
	 * @return an array of Scanners of the appropriate state files
	 */
	protected static List<Scanner> getScanners(List<String> states)
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
	 * 
	 * @param arrays
	 *            the sorted array to merge
	 * @return a sorted array of the values from arrays
	 */
	static void mergeCities(List<Scanner> stateScanners, PrintWriter out)
			throws NoSuchElementException, IllegalStateException {
		Scanner scanner;
		PQItem item;

		// create the queue
		PriorityQueue queue = new PriorityQueue(stateScanners.size());

		// fill the queue with initial values
		for (int i = 0; i < stateScanners.size(); i++) {
			scanner = stateScanners.get(i);

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
			scanner = stateScanners.get(item.source);

			if (scanner.hasNextLine()) {
				queue.insert(new PQItem(scanner.nextLine(), item.source));
			} // if
		} // while

		return;
	} // merge(ArrayList<ArrayList<Integer>>)

	public static void main(String[] args) {
		PrintWriter out;
		List<Scanner> scanners = new ArrayList<>();
		List<String> states = new ArrayList<>(args.length);

		for (String arg : args) {
			if (VALID_STATES.contains(arg)) {
				states.add(arg);
			} else {
				System.err.println("Invalid state: " + arg
						+ ". Must choose from valid states:");
				System.err.println(VALID_STATES);
				return;
			} // if...else
		} // for

		try {
			
			out = new PrintWriter(OUT_FILE);
			scanners = getScanners(states);
			
			mergeCities(scanners, out);
			
			out.close();
			
		} catch (Exception e) {
			
			System.err.println(e);
			
		} finally {
			
			for (Scanner scanner : scanners) {
				scanner.close();
			} // for
		} // try...catch...finally
	} // main(String[])
} // class MergeUtils