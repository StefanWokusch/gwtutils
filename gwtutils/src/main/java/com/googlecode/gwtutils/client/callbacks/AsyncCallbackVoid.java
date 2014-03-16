package com.googlecode.gwtutils.client.callbacks;

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
		throw new RuntimeException("Request failed for " + getClass().getName(), caught);
	}

}
