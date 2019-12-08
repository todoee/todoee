package io.meedo.finder.service.impl;

import io.meedo.finder.service.ThumbnailWidth;

public class DefaultThumbnailWidth implements ThumbnailWidth {

    private int thumbnailWidth;

    @Override
    public int getThumbnailWidth() {
        return thumbnailWidth;
    }

    public void setThumbnailWidth(int thumbnailWidth) {
        this.thumbnailWidth = thumbnailWidth;
    }
}
