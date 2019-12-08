package io.meedo.finder.support.archiver;

import org.json.JSONObject;

/**
 * Archiver Options.
 *
 * @author James.zhang
 */
public enum ArchiverOption {

    INSTANCE;

    public static final JSONObject JSON_INSTANCE = new JSONObject(INSTANCE);

    public String[] getCreate() {
        return ArchiverType.SUPPORTED_MIME_TYPES;
    }

    public String[] getExtract() {
        return ArchiverType.SUPPORTED_MIME_TYPES;
    }

}