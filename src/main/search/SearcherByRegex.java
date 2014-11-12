package main.search;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

import main.result.Result;

public class SearcherByRegex implements Searcher {
	
	private static Logger logger = Logger.getLogger("SearcherByRegex");
	@Override
	public List<Result> search(String searchToken) throws Exception {
		String searchString = searchToken.toLowerCase();
		Pattern pattern =  Pattern.compile(searchString);
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
				logger.debug("End of line frequency: " + frequency);
			}
			list.add(new Result(textFile.getName(), frequency));
			br.close();
		}
		return list;
	}

}
