package main.search;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.log4j.Logger;

import main.result.Result;

/**
 * create a hashmap<String, hashMap<String,Integer>> for every file. For every
 * clean word (filter out special characters): add to hashmap: add its follower
 * to childMap. If follower already in childMap: increment count of follower in
 * childmap. When no follower found: add null as follower in childMap.
 * To search: Split the input search string by space (filter out all special characters) and
 * create a array.
 * 
 * }
 * 
 * @author bhuvi
 * 
 */
public class SearcherByDataStructure implements Searcher {
	private static Logger logger = Logger.getLogger("SearcherByDataStructure");

	private Map<String, HashMap<String, Integer>> buildHashMap(File file) {
		BufferedReader br = null;
		Map<String, HashMap<String, Integer>> mainMap = null;

		try {
			mainMap = new HashMap<String, HashMap<String, Integer>>();
			br = new BufferedReader(new FileReader(file));
			String line = "";
			while ((line = br.readLine()) != null) {
				String words[] = line.toLowerCase().split("\\W+");
				int i;
				for (i = 0; i < words.length - 1; i++) {
					HashMap<String, Integer> childMap = mainMap.get(words[i]);
					if (childMap == null) {
						childMap = new HashMap<String, Integer>();
					} else if (childMap.containsKey(words[i + 1])) {
						int count = childMap.get(words[i + 1]);
						childMap.put(words[i + 1], count + 1);
						mainMap.put(words[i], childMap);
						continue;
					}
					childMap.put(words[i + 1], 1);
					mainMap.put(words[i], childMap);
				}
				// hit the last word in the line
				HashMap<String, Integer> childMap = mainMap.get(words[i]);
				if (childMap == null) {
					childMap = new HashMap<String, Integer>();
				}
				childMap.put(null, 1);
				mainMap.put(words[i], childMap);

			}
			logger.debug("Done with file: " + file.getName());
		} catch (Exception e) {
			logger.error("error", e);
		} finally {
			try {
				br.close();
			} catch (IOException e) {
				logger.error(e);
			}
		}
		return mainMap;
	}

	@Override
	public List<Result> search(String searchToken) {
		List<Result> list = new LinkedList<Result>();
		File resources = new File("resources");
		for (File textFile : resources.listFiles()) {
			logger.debug("File: " + textFile.getName());
			Map<String, HashMap<String, Integer>> mainMap;
			try {
				mainMap = buildHashMap(textFile);
				String[] searchTokens = searchToken.toLowerCase().split("\\W+");
				list.add(new Result(textFile.getName(), search(mainMap,
						searchTokens, 0, 0)));
				// prettyPrintHashMap(mainMap);
			} catch (Exception e) {
				logger.error(e);
			}
		}
		return list;
	}

	private int search(Map<String, HashMap<String, Integer>> map,
			String[] searchTokens, int index, int freq) {

		String word = searchTokens[index];
		HashMap<String, Integer> childMap = map.get(word);
		if (searchTokens.length == 1 && childMap != null) {
			logger.debug("Single word to search :" + word);
			freq = getFreq(childMap);
			logger.debug("Frequency:" + freq);
			return freq;
		}
		if (childMap == null) {
			logger.debug("word or phrase not found");
			logger.debug("Frequency:" + freq);
			return 0;
		}
		if (index >= searchTokens.length - 1) {
			logger.debug("Reached last word in search");
			logger.debug("Frequency:" + freq);
			return freq;
		}
		index = index + 1;
		String nextWord = searchTokens[index];
		if (childMap.containsKey(nextWord)) {
			freq = childMap.get(nextWord);
			return search(map, searchTokens, index, freq);
		}
		logger.debug("Frequency:" + freq);
		return freq;

	}

	private int getFreq(HashMap<String, Integer> map) {
		int sum = 0;
		for (int count : map.values()) {
			sum += count;
		}
		return sum;
	}

	private void prettyPrintHashMap(
			Map<String, HashMap<String, Integer>> mainMap) {
		for (Entry<String, HashMap<String, Integer>> entry : mainMap.entrySet()) {
			logger.debug(entry.getKey() + "- children are ");
			HashMap<String, Integer> childMap = entry.getValue();
			for (Entry<String, Integer> childEntry : childMap.entrySet()) {
				logger.debug(childEntry.getKey() + ": " + childEntry.getValue());
			}
		}
	}

}
