package io.meedo.finder.core;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import io.meedo.finder.service.ElfinderStorageFactory;

public interface ElfinderContext {

    ElfinderStorageFactory getVolumeSourceFactory();

    HttpServletRequest getRequest();

    HttpServletResponse getResponse();

}
