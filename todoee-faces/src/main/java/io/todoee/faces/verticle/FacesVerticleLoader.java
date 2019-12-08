package io.todoee.faces.verticle;

import io.todoee.core.loader.VerticleLoader;
import io.todoee.core.verticle.ExternalAbstractVerticle;

public class FacesVerticleLoader extends VerticleLoader<ExternalAbstractVerticle>{

	@Override
	protected boolean worker() {
		return true;
	}

	@Override
	protected int instances() {
		return 1;
	}

}
