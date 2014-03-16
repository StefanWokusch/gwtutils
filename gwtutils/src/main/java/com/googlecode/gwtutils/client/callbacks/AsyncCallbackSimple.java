package com.googlecode.gwtutils.client.callbacks;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * AsyncCallback with default ErrorHandling (use UncaughtExceptionHandler for example in AbstractEntryPoint)
 * 
 * @param <T>
 */
public abstract class AsyncCallbackSimple<T> implements AsyncCallback<T> {

	@Override
	public void onFailure(Throwable caught) {
		throw new RuntimeException("Request failed for " + getClass().getName(), caught);
	}

}
