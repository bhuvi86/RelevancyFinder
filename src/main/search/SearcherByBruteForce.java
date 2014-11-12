package main.search;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.LinkedList;
import java.util.List;

import main.result.Result;

import org.apache.log4j.Logger;

public class SearcherByBruteForce implements Searcher{

	private static Logger logger = Logger.getLogger("SearcherByBruteForce");

	public List<Result> search(String tokenString) throws Exception {
		// load file one by one
		// read every line 
		// try to find a match.
		List<Result>  list = new LinkedList<Result>();
		String strSearch = tokenString.toLowerCase();
		File resources = new File("resources");
		for (File textFile : resources.listFiles()){
			BufferedReader br = new BufferedReader(new FileReader(textFile));
			int frequency = 0;
			String line = "";
			while((line = br.readLine()) != null){
				int count = 0;
				int idx = 0;
				while ((idx = line.toLowerCase().indexOf(strSearch, idx)) != -1) {
					count++;
					idx += strSearch.length();
				}
				frequency += count;
				logger.debug("End of line frequency: " + frequency);

			}
			// TODO: add in sorted order by frequency
			list.add(new Result(textFile.getName(), frequency));
			br.close();
		}
		return list;
	}
}
