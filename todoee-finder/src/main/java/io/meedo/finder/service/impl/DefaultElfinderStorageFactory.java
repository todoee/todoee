package io.meedo.finder.service.impl;

import io.meedo.finder.service.ElfinderStorage;
import io.meedo.finder.service.ElfinderStorageFactory;

public class DefaultElfinderStorageFactory implements ElfinderStorageFactory {

    private ElfinderStorage elfinderStorage;

    @Override
    public ElfinderStorage getVolumeSource() {
        return elfinderStorage;
    }

    public void setElfinderStorage(ElfinderStorage elfinderStorage) {
        this.elfinderStorage = elfinderStorage;
    }
}
