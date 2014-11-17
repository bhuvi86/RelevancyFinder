package main.result.sort;

import java.util.Comparator;

import main.result.Result;

/**
 * Custom comparator to order {@link}Result in decreasing order of {@link}
 * Result.frequency. This is required to return search results in order of
 * relevance.
 */
public class ResultComparator implements Comparator<Result> {

	@Override
	public int compare(Result o1, Result o2) {
		return o2.getFrequency() - o1.getFrequency();
	}

}
