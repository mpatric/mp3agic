package com.mpatric.mp3agic;

import java.io.IOException;
import java.nio.channels.SeekableByteChannel;
import java.nio.file.OpenOption;

public interface InputSource {

    /**
     * Retrieves a value that identifies this resource.
     * Can be a URL or a file name, for example.
     */
    String getResourceName();

    /**
     * Tells if the resource is valid and readable.
     */
    boolean isReadable();

    /**
     * Retrieves the last modification time of the resource.
     */
    long getLastModified() throws IOException;

    /**
     * Retrieves the length of the resource in bytes.
     */
    long getLength() throws IOException;

    /**
     * Opens a channel to read the resource.
     */
    SeekableByteChannel openChannel(OpenOption... options) throws IOException;

}
