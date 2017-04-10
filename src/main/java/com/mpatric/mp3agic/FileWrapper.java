package com.mpatric.mp3agic;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.TimeUnit;

public class FileWrapper {

	protected Path path;
	protected long length;
	protected long lastModified;

	protected FileWrapper() {
	}

	public FileWrapper(String filename) throws IOException {
		this.path = Paths.get(filename);
		init();
	}

	public FileWrapper(File file) throws IOException {
		if (file == null) throw new NullPointerException();
		this.path = Paths.get(file.getPath());
		init();
	}

	public FileWrapper(Path path) throws IOException {
		if (path == null) throw new NullPointerException();
		this.path = path;
		init();
	}

	private void init() throws IOException {
		if (!Files.exists(path)) throw new FileNotFoundException("File not found " + path);
		if (!Files.isReadable(path)) throw new IOException("File not readable");
		length = Files.size(path);
		lastModified = Files.getLastModifiedTime(path).to(TimeUnit.MILLISECONDS);
	}

	public String getFilename() {
		return path.toString();
	}

	public long getLength() {
		return length;
	}

	public long getLastModified() {
		return lastModified;
	}
}
