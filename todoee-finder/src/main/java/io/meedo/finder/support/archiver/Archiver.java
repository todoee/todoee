package io.meedo.finder.support.archiver;

import java.io.IOException;

import io.meedo.finder.core.Target;

/**
 * Archiver interface.
 *
 * @author James.zhang
 */
public interface Archiver {

    /**
     * Gets the archive name.
     *
     * @return the archive name
     */
    String getArchiveName();

    /**
     * Gets the archive mimetype.
     *
     * @return the archive mimetype
     */
    String getMimeType();

    /**
     * Gets the archive extension.
     *
     * @return the archive extension
     */
    String getExtension();

    /**
     * Creates the compress archive for the given targets.
     *
     * @param targets the targets to compress.
     * @return the target of the compress archive.
     * @throws IOException if something goes wrong.
     */
    Target compress(Target... targets) throws IOException;

    /**
     * Decompress the archive for the given target
     *
     * @param target the compress archive to decompress.
     * @return the target folder of the decompressed targets.
     * @throws IOException if something goes wrong.
     */
    Target decompress(Target target) throws IOException;
}
