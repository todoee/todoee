package io.meedo.finder.support.content.detect;

import org.apache.tika.Tika;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * NIO file type detector implementation that uses Tika API.
 *
 * @author James.zhang
 */
public class NIO2FileTypeDetector extends java.nio.file.spi.FileTypeDetector implements Detector {

    private final Tika tika = new Tika();

    /**
     * Gets mime type from the given input stream.
     *
     * @return the mime type.
     * @throws IOException if the stream can not be read.
     */
    @Override
    public String detect(InputStream inputStream) throws IOException {
        return tika.detect(inputStream);
    }

    /**
     * Gets mime type from the given file path.
     *
     * @return the mime type.
     * @throws IOException if the file can not be read.
     */
    @Override
    public String detect(Path path) throws IOException {
        if (Files.isDirectory(path)) {
            return "directory";
        }
        return tika.detect(path);
    }

    /**
     * Gets mime type from the given file path.
     *
     * @return the mime type.
     * @throws IOException if the file can not be read.
     */
    @Override
    public String probeContentType(Path path) throws IOException {
        return detect(path);
    }

}
