package com.mpatric.mp3agic;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.channels.SeekableByteChannel;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.TimeUnit;

public class FileWrapper {

	protected InputSource inputSource;
	protected long lastModified;

	protected FileWrapper() {
	}

	public FileWrapper(String filename) throws IOException {
		if (filename == null) throw new IllegalArgumentException("filename must not be null");
		this.inputSource = new FileInputSource(Paths.get(filename));
		init();
	}

	public FileWrapper(File file) throws IOException {
		if (file == null) throw new IllegalArgumentException("file must not be null");
		this.inputSource = new FileInputSource(Paths.get(file.getPath()));
		init();
	}

	public FileWrapper(Path path) throws IOException {
		if (path == null) throw new IllegalArgumentException("path must not be null");
		this.inputSource = new FileInputSource(path);
		init();
	}

	public FileWrapper(InputSource inputSource) throws IOException {
		if (inputSource == null) throw new IllegalArgumentException("inputSource must not be null");
		this.inputSource = inputSource;
		init();
	}

	private void init() throws IOException {
		if (!inputSource.isReadable()) throw new FileNotFoundException("File could not be found or is not readable " +
				inputSource.getResourceName());
		lastModified = inputSource.getLastModified();
	}

	public String getFilename() {
		return inputSource.getResourceName();
	}

	public long getLastModified() {
		return lastModified;
	}

}
