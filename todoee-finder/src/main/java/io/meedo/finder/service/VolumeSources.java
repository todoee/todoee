package io.meedo.finder.service;

import java.nio.file.Paths;
import java.text.Normalizer;
import java.util.Arrays;

import io.meedo.finder.configuration.ElfinderConfigurationUtils;
import io.meedo.finder.core.Volume;
import io.meedo.finder.core.VolumeBuilder;
import io.meedo.finder.core.impl.NIO2FileSystemVolume;
import io.meedo.finder.exception.VolumeSourceException;

/**
 * Volume Sources supported.
 *
 * @author James.zhang
 */
public enum VolumeSources {

    FILESYSTEM {
        @Override
        public VolumeBuilder<?> getVolumeBuilder(String alias, String path) {
            return NIO2FileSystemVolume.builder(alias, Paths.get(ElfinderConfigurationUtils.toURI(path)));
        }
    }
//    ,DROPBOX, GOOGLEDRIVE, ONEDRIVE, ICLOUD
    ;

    public static VolumeSources of(String source) {
        if (source != null) {

            final String notLetterRegex = "[^\\p{L}]";
            final String whitespaceRegex = "[\\p{Z}]";
            final String notAsciiCharactersRegex = "[^\\p{ASCII}]";
            final String emptyString = "";

            source = Normalizer.normalize(source, Normalizer.Form.NFD);
            source = source.replaceAll(notLetterRegex, emptyString);
            source = source.replaceAll(whitespaceRegex, emptyString);
            source = source.replaceAll(notAsciiCharactersRegex, emptyString);
            source = source.trim().toUpperCase();

            for (VolumeSources volumesource : values()) {
                if (volumesource.name().equalsIgnoreCase(source)) {
                    return volumesource;
                }
                throw new VolumeSourceException("Volume source not supported! The supported volumes sources are: " + Arrays.deepToString(values()).toLowerCase());
            }
        }
        throw new VolumeSourceException("Volume source not informed in elfinder configuration xml!");
    }

    public Volume newInstance(String alias, String path) {
        if (path == null || path.trim().isEmpty())
            throw new VolumeSourceException("Volume source path not informed");
        return getVolumeBuilder(alias, path).build();
    }

    public abstract VolumeBuilder<?> getVolumeBuilder(String alias, String path);
}
