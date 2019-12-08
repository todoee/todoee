package io.todoee.faces.core;

import java.io.Serializable;

import io.todoee.core.context.IocContext;

/**
 * 
 * @author James.zhang
 *
 */
@SuppressWarnings("serial")
public abstract class AbstractView implements Serializable {

	public AbstractView() {
		IocContext.inject(this);
	}

}