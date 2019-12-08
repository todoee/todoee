package io.meedo.finder.core.impl;

import io.meedo.finder.core.VolumeSecurity;

public class DefaultVolumeSecurity implements VolumeSecurity {

    private final String volumePattern;
    private final SecurityConstraint securityConstraint;

    public DefaultVolumeSecurity(String volumePattern, SecurityConstraint securityConstraint) {
        this.volumePattern = volumePattern;
        this.securityConstraint = securityConstraint;
    }

    @Override
    public String getVolumePattern() {
        return volumePattern;
    }

    @Override
    public SecurityConstraint getSecurityConstraint() {
        return securityConstraint;
    }

}
