package io.meedo.finder.core;

import io.meedo.finder.core.impl.SecurityConstraint;

public interface VolumeSecurity {

    String getVolumePattern();

    SecurityConstraint getSecurityConstraint();

}