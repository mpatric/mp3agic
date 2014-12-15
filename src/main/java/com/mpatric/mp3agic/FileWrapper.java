package com.mpatric.mp3agic;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class FileWrapper {
	
	protected File file;
	protected long length;
	protected long lastModified;
	
	protected FileWrapper() {
	}

	public FileWrapper(String filename) throws IOException {
		this.file = new File(filename);
		init();
	}
	
	public FileWrapper(File file) throws IOException {
		if (file == null) throw new NullPointerException();
		this.file = file;
		init();
	}
	
	private void init() throws IOException {
		if (!file.exists()) throw new FileNotFoundException("File not found " + file.getPath());
		if (!file.canRead()) throw new IOException("File not readable");
		length = file.length();
		lastModified = file.lastModified();
	}
	
	public String getFilename() {
		return file.getPath();
	}

	public long getLength() {
		return length;
	}

	public long getLastModified() {
		return lastModified;
	}
}
