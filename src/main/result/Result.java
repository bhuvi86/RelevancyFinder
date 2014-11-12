package main.result;

public class Result {
	private String filename;
	private int frequency;

	public Result(String fnmae, int f){
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
