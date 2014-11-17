package main.result;

/**
 * Result for the search query with filename and number of occurrences for the
 * input string in the file.
 */
public class Result {
	private String filename;
	private int frequency;

	public Result(String fnmae, int f) {
		this.filename = fnmae;
		this.frequency = f;
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public int getFrequency() {
		return frequency;
	}

	public void setFrequency(int frequency) {
		this.frequency = frequency;
	}
}
