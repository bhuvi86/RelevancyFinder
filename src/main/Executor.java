package main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

import org.apache.log4j.Logger;

import main.result.Result;
import main.search.Searcher;
import main.search.SearcherByBruteForce;
import main.search.SearcherByDataStructure;
import main.search.SearcherByRegex;

public class Executor {

	public static void main(String args[]) {
		System.out.println("Enter the search term:");
		// open up standard input
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		final Logger logger = Logger.getLogger(Executor.class.toString());
		logger.info("In executor's main");

		String tokenString = null;
		String methodType = null;

		// read the token string from the command-line;
		try {
			tokenString = br.readLine();
			while (tokenString == null || tokenString.isEmpty()) {
				System.out.println(" Token string cannot be empty. Enter a non-empty string:");
				tokenString = br.readLine();
			}
			logger.info("User entered search token: " + tokenString);
			System.out.println("Enter 1 for regex search or 2 for data-structure based search."
							+ "\nEnter for default brute force search");
			methodType = br.readLine();
			Searcher searcher = null;
			if (methodType.equals("1")) {
				logger.info("User wants to search using regex");
				searcher = new SearcherByRegex();
			} else if (methodType.equals("2")) {
				logger.info("User wants to search using datastructure");
				searcher = new SearcherByDataStructure();
			} else {
				// user wants to perform default brute force based search
				logger.info("User wants to search using brute force");
				searcher = new SearcherByBruteForce();
			}
			List<Result> resultSet = searcher.search(tokenString);
			for (Result rs : resultSet) {
				System.out.println("For file: " + rs.getFilename()
						+ " relevancy: " + rs.getFrequency());
			}

		} catch (Exception e) {
			System.out.println("Error occured . Exiting");
			logger.error("Error while searching.", e);
		} finally {
			try {
				br.close();
				System.out.println("Done. thank you");
			} catch (IOException e) {
				logger.error("Failed to close bufferedReader");
			}
		}

	}

}
