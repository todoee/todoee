package io.meedo.finder.service;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import io.meedo.finder.core.Target;
import io.meedo.finder.core.Volume;
import io.meedo.finder.core.VolumeSecurity;

public interface ElfinderStorage {

    Target fromHash(String hash);

    String getHash(Target target) throws IOException;

    String getVolumeId(Volume volume);

    Locale getVolumeLocale(Volume volume);

    VolumeSecurity getVolumeSecurity(Target target);

    List<Volume> getVolumes();

    List<VolumeSecurity> getVolumeSecurities();

    ThumbnailWidth getThumbnailWidth();
}