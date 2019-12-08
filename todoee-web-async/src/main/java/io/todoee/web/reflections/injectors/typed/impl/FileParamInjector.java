package io.todoee.web.reflections.injectors.typed.impl;

import java.util.Set;

import io.todoee.web.reflections.injectors.typed.ParamInjector;
import io.vertx.ext.web.FileUpload;
import io.vertx.ext.web.RoutingContext;

public class FileParamInjector implements ParamInjector<FileUpload[]> {

	@Override
	public FileUpload[] resolve(RoutingContext context) {
		Set<FileUpload> uploads = context.fileUploads();
		return uploads.toArray(new FileUpload[0]);
	}

}
