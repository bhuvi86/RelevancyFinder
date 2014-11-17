package main.search;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

import main.result.Result;
import main.result.sort.ResultComparator;

/**
 * Search for token string using Java REGEX API.
 */
public class SearcherByRegex implements Searcher {

	//protected constructor so that only factory or subclass can call
	protected SearcherByRegex(){
	}

	private static Logger logger = Logger.getLogger("SearcherByRegex");
	
	/**
	 * Search for string using REGEX API.
	 * @return List<Result> with filename and number of occurrences.
	 */
	@Override
	public List<Result> search(String searchToken) throws Exception {
		if (searchToken == null || searchToken.isEmpty()){
			return null;
		}
		String searchString = searchToken.toLowerCase();
		//using word boundary so that "there" does not match for "the"
		Pattern pattern =  Pattern.compile("\\b" + searchString +"\\b");
		List<Result>  list = new LinkedList<Result>();
		File resources = new File("resources");
		for (File textFile : resources.listFiles()){
			BufferedReader br = new BufferedReader(new FileReader(textFile));
			Matcher matcher = null;
			int frequency = 0;
			String line = "";
			while((line = br.readLine()) != null){
				line = line.toLowerCase();
				matcher = pattern.matcher(line);
				while(matcher.find()){
					frequency++;
				}
			}
			list.add(new Result(textFile.getName(), frequency));
			br.close();
		}
		// return results in order of relevancy (most-relevant on top)
		Collections.sort(list, new ResultComparator());
		logger.info("Final result: " + list);
		return list;
	}

}
