package com.mpatric.mp3agic;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

public class FileMediaSource implements MediaSource {
	
	protected File file;
	protected String filename;
	protected long length;
	protected long lastModified;

	protected FileMediaSource() {
	}

	public FileMediaSource(String filename) throws IOException {
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

    @Override
    public RandomAccessMediaSource open() throws FileNotFoundException {
        return new RandomAccessMediaFile(filename, "r");
    }
}
