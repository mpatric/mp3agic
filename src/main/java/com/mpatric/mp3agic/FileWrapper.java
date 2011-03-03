package com.mpatric.mp3agic;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class FileWrapper {
	
	protected File file;
	protected String filename;
	protected long length;
	protected long lastModified;
	
	protected FileWrapper() {
	}

	public FileWrapper(String filename) throws IOException {
		this.filename = filename;
		init();
		length = file.length();
		lastModified = file.lastModified();
	}

	private void init() throws IOException {
		file = new File(filename);
		if (!file.exists()) throw new FileNotFoundException("File not found " + filename);
		if (!file.canRead()) throw new IOException("File not readable");
	}
	
	public String getFilename() {
		return filename;
	}

	public long getLength() {
		return length;
	}

	public long getLastModified() {
		return lastModified;
	}
}
