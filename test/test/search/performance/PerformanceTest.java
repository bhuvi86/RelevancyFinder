package test.search.performance;

import java.util.Random;

import main.search.Searcher;
import main.search.Searcher.SearchMethod;
import main.search.SearcherFactory;

public class PerformanceTest {

	// word of length = 1
	private final static String TESTSTRING_0 = "the";
	// word of length = 2
	private final static String TESTSTRING_1 = "are";
	// word of length = 3
	private final static String TESTSTRING_2 = "france";

	private static String[] arrayString = { TESTSTRING_0, TESTSTRING_1,
		TESTSTRING_2 };

	private final static long number = 200000;

	public static void measureTime(String methodType) {
		System.out.println("Started measurement");
		Double sum = new Double(0);
		for (int i = 0; i < number; i++) {
			Random r = new Random();
			String inpuString = arrayString[Math.abs(r.nextInt() % 3)];
			long startTime = System.nanoTime();
			Searcher searcher = SearcherFactory.getSearcher(methodType);
			try {
				searcher.search(inpuString);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			// convert to milli-seconds
			double estimatedTime = (System.nanoTime() - startTime)/1000000;
			sum+=estimatedTime;
		}
		//writeToFile(methodType, values);
		// calculate average
		double avg =  (sum / number);
		System.out.println("Time taken for method type: " + methodType + " " + avg);
	}

	public static void main(String args[]) {
		measureTime(SearchMethod.BRUTEFORCE.toString());
		measureTime(SearchMethod.REGEX.toString());
		measureTime(SearchMethod.DATASTRUCTURE.toString());
	}

}
