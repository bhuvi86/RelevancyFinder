package test.search;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import main.result.Result;
import main.search.Searcher;
import main.search.SearcherFactory;
import main.search.Searcher.SearchMethod;

import org.junit.BeforeClass;
import org.junit.Test;

public class SearchTest {

	// word of length = 1
	private final static String TESTSTRING_1 = "france";
	// word of length = 2
	private final static String TESTSTRING_2 = "in the";
	// word of length = 3
	private final static String TESTSTRING_3 = "some of the";	
	// null string
	private final static String TESTSTRING_0 = null;
	
    // filenames
	private final static String FILE1= "french_armed_forces.txt";
	private final static String FILE2= "hitchhikers.txt";
	private final static String FILE3= "warp_drive.txt";

	// expected frequencies for given input strings in given files

    private static Map<String, HashMap<String,Integer>> expectedValues = null;

	private Searcher searcher = null;
	
	@BeforeClass
	public static void setUpBeforeClass(){
		expectedValues = new HashMap<String, HashMap<String,Integer>>();
		addValuesToMap(TESTSTRING_1, 18, 0, 0);
		addValuesToMap(TESTSTRING_2, 15, 2, 1);
		addValuesToMap(TESTSTRING_3, 0, 1, 1);
	}
	
	private static void addValuesToMap(String testString, int f1, int f2, int f3) {
		HashMap<String, Integer> childMap = new HashMap<String, Integer>();
		childMap.put(FILE1, f1);
		childMap.put(FILE2, f2);
		childMap.put(FILE3, f3);
		expectedValues.put(testString, childMap);
	}

	@Test
	public void testSearchByBruteForce() {
		searcher = SearcherFactory.getSearcher(SearchMethod.BRUTEFORCE.toString());
		try {
			runAllSearches();
		} catch (Exception e) {
			fail("testSearchByBruteForce failed");
		}
	}
	
	@Test
	public void testSearchByRegex() {
		searcher = SearcherFactory.getSearcher(SearchMethod.REGEX.toString());
		try {
			runAllSearches();
		} catch (Exception e) {
			fail("testSearchByRegex failed");
		}
	}
	
	@Test
	public void testSearchByInvertedIndex() {
		searcher = SearcherFactory.getSearcher(SearchMethod.DATASTRUCTURE.toString());
		try {
			runAllSearches();
		} catch (Exception e) {
			fail("testSearchByInvertedIndex failed");
		}
	}
	private void runAllSearches() throws Exception {
			List<Result> resultList = searcher.search(TESTSTRING_1);
			assertNotNull(resultList);
			verifyResult(resultList, TESTSTRING_1);
			
			resultList = searcher.search(TESTSTRING_2);
			assertNotNull(resultList);
			verifyResult(resultList, TESTSTRING_2);
			
			resultList = searcher.search(TESTSTRING_3);
			assertNotNull(resultList);
			verifyResult(resultList, TESTSTRING_3);
			
			assertNull(searcher.search(TESTSTRING_0));
		
	}

	private void verifyResult(List<Result> resultList, String testString) {
		for (Result rs : resultList){
				assertEquals((int)expectedValues.get(testString).get(rs.getFilename()),rs.getFrequency());
		}
	}

}
