package com.mpatric.mp3agic;

import java.io.*;
import java.nio.channels.FileChannel;
import java.nio.channels.SeekableByteChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.concurrent.TimeUnit;

public class FileWrapper {

	private interface SeekableByteChannelFactory {
		SeekableByteChannel createByteChannel() throws IOException;
	}

	private SeekableByteChannelFactory byteChannelFactory;
	private String filename;
	private Path absolutePath;
	private long length;
	private long lastModified;

	protected FileWrapper() {
	}

	public FileWrapper(FileDescriptor fileDescriptor) throws IOException {
		if (fileDescriptor == null) throw new NullPointerException();
		if (!fileDescriptor.valid()) throw new IOException("FileDescriptor not valid");
		length = -1;
		lastModified = -1;
		byteChannelFactory = () -> {
			FileChannel channel = new FileInputStream(fileDescriptor).getChannel();
			length = channel.size();
			return channel;
		};
	}

	public FileWrapper(String filename) throws IOException {
		this(Paths.get(filename));
	}

	public FileWrapper(File file) throws IOException {
		this(Paths.get(file.getPath()));
	}

	public FileWrapper(Path path) throws IOException {
		if (path == null) throw new NullPointerException();
		init(path);
	}

	private void init(Path path) throws IOException {
		if (!Files.exists(path)) throw new FileNotFoundException("File not found " + path);
		if (!Files.isReadable(path)) throw new IOException("File not readable");
		absolutePath = path.toAbsolutePath();
		byteChannelFactory = () -> Files.newByteChannel(path, StandardOpenOption.READ);
		filename = path.toString();
		length = Files.size(path);
		lastModified = Files.getLastModifiedTime(path).to(TimeUnit.MILLISECONDS);
	}

	public String getFilename() {
		if (filename == null) {
			throw new IllegalArgumentException("Cannot get filename from a FileDescriptor");
		}
		return filename;
	}

	public long getLength() {
		return length;
	}

	public long getLastModified() {
		if (lastModified < 0) {
			throw new IllegalArgumentException("Cannot get last modified from a FileDescriptor");
		}
		return lastModified;
	}

	protected SeekableByteChannel getByteChannel() throws IOException {
		return byteChannelFactory.createByteChannel();
	}

	protected Path getAbsolutePath() {
		return absolutePath;
	}
}
