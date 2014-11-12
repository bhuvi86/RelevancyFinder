package main.search;

import java.util.List;
import main.result.Result;

public interface Searcher {
	public List<Result> search (String searchToken) throws Exception;
}
