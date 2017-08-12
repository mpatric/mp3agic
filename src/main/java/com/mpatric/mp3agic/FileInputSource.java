package com.mpatric.mp3agic;

import java.io.IOException;
import java.nio.channels.SeekableByteChannel;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.util.concurrent.TimeUnit;

public class FileInputSource implements InputSource {

    private final Path path;

    public FileInputSource(Path path) {
        this.path = path;
    }

    @Override
    public String getResourceName() {
        return path.toString();
    }

    @Override
    public boolean isReadable() {
        return Files.exists(path) && Files.isReadable(path);
    }

    @Override
    public long getLastModified() throws IOException {
        return Files.getLastModifiedTime(path).to(TimeUnit.MILLISECONDS);
    }

    @Override
    public long getLength() throws IOException {
        return Files.size(path);
    }

    @Override
    public SeekableByteChannel openChannel(OpenOption... options) throws IOException {
        return Files.newByteChannel(path, options);
    }

}
