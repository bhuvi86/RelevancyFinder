package main.search;

import java.util.List;
import main.result.Result;

/**
 * Interface for the search implementations. Exposes search method which is
 * invoked by client program (Executor.java).
 * 
 */
public interface Searcher {

	public enum SearchMethod {
		BRUTEFORCE, REGEX, DATASTRUCTURE
	}

	public List<Result> search(String searchToken) throws Exception;
}
