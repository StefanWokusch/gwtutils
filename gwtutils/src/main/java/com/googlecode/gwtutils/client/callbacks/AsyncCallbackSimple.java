package com.googlecode.gwtutils.client.callbacks;

import java.util.LinkedHashSet;
import java.util.Set;

import com.google.gwt.event.shared.UmbrellaException;
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * AsyncCallback with default ErrorHandling (use UncaughtExceptionHandler for example in AbstractEntryPoint)
 * 
 * @param <T>
 */
public abstract class AsyncCallbackSimple<T> implements AsyncCallback<T> {

	@Override
	public void onFailure(Throwable caught) {
		Set<Throwable> causes = new LinkedHashSet<Throwable>();
		causes.add(caught);
		throw new UmbrellaException(causes);
	}

}
