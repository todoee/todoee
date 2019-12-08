package io.meedo.finder.support.archiver;

/**
 * Archiver Supported Types.
 *
 * @author James.zhang
 */
public enum ArchiverType {

    TAR {
        @Override
        public Archiver getStrategy() {
            return new TarArchiver();
        }
    },
    ZIP {
        @Override
        public Archiver getStrategy() {
            return new ZipArchiver();
        }
    },
    GZIP {
        @Override
        public Archiver getStrategy() {
            return new GzipArchiver();
        }
    };

    // convenient

    public static ArchiverType of(String mimeType) {
        for (ArchiverType archiverType : values()) {
            if (archiverType.getStrategy().getMimeType().equalsIgnoreCase(mimeType)) {
                return archiverType;
            }
        }
        throw new RuntimeException(String.format("Archive type (%s) not supported", mimeType));
    }

    public static String[] SUPPORTED_MIME_TYPES = {
            ZIP.getMimeType(),
            TAR.getMimeType(),
            GZIP.getMimeType()
    };

    // shortcuts

    public String getMimeType() {
        return getStrategy().getMimeType();
    }

    public String getExtension() {
        return getStrategy().getExtension();
    }

    public String getArchiveName() {
        return getStrategy().getArchiveName();
    }

    public abstract Archiver getStrategy();

}
