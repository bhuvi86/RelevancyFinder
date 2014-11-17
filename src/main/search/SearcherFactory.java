package main.search;

import main.search.Searcher.SearchMethod;

/**
 * factory for creating the Searcher object.
 * Takes methodType as input and creates an instance based this value.
 *
 */
public class SearcherFactory {

	public static Searcher getSearcher(String methodType){
		if (methodType.equals(SearchMethod.REGEX.toString())) {
			return new SearcherByRegex();
		}if (methodType.equals(SearchMethod.DATASTRUCTURE.toString())) {
			return new SearchByInvertedIndex();
		}else{
			return new SearcherByBruteForce();
		}
	}
}
