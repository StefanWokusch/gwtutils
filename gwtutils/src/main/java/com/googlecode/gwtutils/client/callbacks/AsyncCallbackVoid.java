package com.googlecode.gwtutils.client.callbacks;

import java.util.LinkedHashSet;
import java.util.Set;

import com.google.gwt.event.shared.UmbrellaException;
import com.google.gwt.user.client.rpc.AsyncCallback;

public abstract class AsyncCallbackVoid implements AsyncCallback<Void> {

	@Override
	public void onSuccess(Void result) {
		onFinish();
	}

	/**
	 * Called onSuccess AND onFailure
	 * 
	 * @example use this when you want to deselect a button
	 */
	protected abstract void onFinish();

	@Override
	public void onFailure(Throwable caught) {
		onFinish();
		Set<Throwable> causes = new LinkedHashSet<Throwable>();
		causes.add(caught);
		throw new UmbrellaException(causes);
	}

}
