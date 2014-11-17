package main.search;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Queue;

import org.apache.log4j.Logger;

import main.result.Result;
import main.result.sort.ResultComparator;

/**
 * Builds an in-memory map of word - filename, positions.
 * A typical map might look below:
 * map : <is : <file1.txt: 10,17,18>
 *             <file2.txt : 18, 25>>
 *       <good: <file3.txt: 5,62,93>
 *             <file2.txt : 11>>
 *
 */
public class SearchByInvertedIndex implements Searcher {
	private static Logger logger = Logger.getLogger(SearchByInvertedIndex.class.toString());

	private static Map<String, HashMap<String, Queue<Integer>>> mainMap = null;

	// protected constructor so that only factory or subclass can call
	protected SearchByInvertedIndex() {
	}

	/**
	 * Use in memory hashmap to look for search token.
	 * call init, to build this map if it didnt exist.
	 * For search token with single word, relevancy = size of positions queue for given filename.
	 * For search token with two or more words , pick only common files containing all words in token.
	 * In those files, look if the words are in order (using positions Queue).
	 * Make multiple passes if necessary.
	 * Increment relevancy when found a match.
	 * @return List<Result> with filename and number of occurrences.
	 */
	@Override
	public List<Result> search(String searchToken) throws Exception {
		if (searchToken == null || searchToken.isEmpty()){
			return null;
		}
		init();
		List<Result> list = new LinkedList<Result>();
	    String[] searchTokens = searchToken.toLowerCase().split("\\W+");
	    // initialize to files of 1st string
		Set<String> matchingFiles = mainMap.get(searchTokens[0]).keySet();
		// populate matching files
		for (String token : searchTokens) {
			// retain all will get us the common files
			matchingFiles.retainAll(mainMap.get(token).keySet());
		}
		File[] inputFiles = new File("resources").listFiles();
		for (File file : inputFiles) {
			if (!matchingFiles.contains(file.getName())) {
				list.add(new Result(file.getName(), 0));
			}
		}
		logger.debug("Matching files:" + matchingFiles);
		String[] fileArray = matchingFiles.toArray((new String[matchingFiles.size()]));
		int relevancy = 0;
		for (int k = 0; k < fileArray.length; k++) {
			String token = null;
			String file = fileArray[k];
			boolean morePositionsFound = true;
			boolean matched = false;
			// if a single word
			if (searchTokens.length==1){
				list.add(new Result(file, mainMap.get(searchTokens[0]).get(file).size()));
				continue;
			}
			for (int i = 0; i < searchTokens.length - 1; i++) {
				token = searchTokens[i];
				Integer pos = 0;
				if (mainMap.get(token).get(file).size() != 0) {
					pos = mainMap.get(token).get(file).poll();
				} else {
					logger.debug("All positions for token is visited. Breaking");
					// no more positions left to explore.
					morePositionsFound = false;
					break;
				}
					
				String nextToken = searchTokens[i + 1];
				if (mainMap.get(nextToken).get(file).contains(pos + 1)) {
					matched = true;
					logger.debug("Found a trail.");
				} else {
					matched = false;
					// continue with current word
					i = i - 1;
					logger.debug("Trail broken. Continuing search with current token");
				}
			} if (matched){
				logger.debug("Found a match. Incrementing relevancy");
				relevancy++;
			}
			logger.debug("End of a pass for file: " + file + "Relevancy so far: " + relevancy);
			// check if there are more entries for token in the same file
			// there might be multiple matches
			if (morePositionsFound) {
				// continue with current file
				k = k - 1;
			} else {
				list.add(new Result(file, relevancy));
				// reset relevancy
				relevancy = 0;
			}
		}
		// return results in order of relevancy (most-relevant on top)
		Collections.sort(list, new ResultComparator());
		return list;
	}
	
	/**
	 * Build hashmap only if didn't exist.
	 */
	private void init() {
		if (mainMap == null) {
			mainMap = createInvertedIndex();
		} else{
			logger.info("inverted index exists. not building it");
		}
	}

	/**
	 * read file line by line and build hashmap.
	 * Add positions for every word in a given file.
	 */
	private synchronized Map<String, HashMap<String, Queue<Integer>>> createInvertedIndex() {
		Map<String,HashMap<String,Queue<Integer>>> myMap = new HashMap<String, HashMap<String, Queue<Integer>>>();
		File resources = new File("resources");
		BufferedReader br = null;
		for (File inputFile : resources.listFiles()) {
			String filename = inputFile.getName();
			int wordCount = 0;
			logger.debug("File: " + inputFile.getName());
			try {
				br = new BufferedReader(new FileReader(inputFile));
				String line = "";
				while ((line = br.readLine()) != null) {
					// get only words, remove all special characters
					String words[] = line.toLowerCase().split("\\W+");
					for (String word : words) {
						HashMap<String, Queue<Integer>> childMap;
						Queue<Integer> positions;
						wordCount++;
						// check if word exists
						if (myMap.containsKey(word)) {
							// get childmap
							childMap = myMap.get(word);
							// check if entry for file exists
							if (childMap.containsKey(filename)) {
								// add position for the word
								positions = childMap.get(filename);
							}else{
								positions = new LinkedList<Integer>();
							}
						} else {
							childMap = new HashMap<String, Queue<Integer>>();
							positions = new LinkedList<Integer>();
						}
						positions.add(wordCount);
						childMap.put(filename, positions);
						myMap.put(word, childMap);
					}
				}
			} catch (Exception e) {
				logger.error("error", e);
			} finally {
				try {
					br.close();
				} catch (IOException e) {
					logger.error(e);
				}
			}
		}
		logger.info("Inverted index built successfully");
		return myMap;

	}
}