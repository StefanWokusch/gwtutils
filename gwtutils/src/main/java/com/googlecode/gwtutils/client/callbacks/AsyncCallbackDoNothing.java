package com.googlecode.gwtutils.client.callbacks;

/**
 * For empty AsyncCallbacks with default Error-Handling ignoring the Success-Value from the Server.
 * 
 * @param <T>
 */
public class AsyncCallbackDoNothing<T> extends AsyncCallbackSimple<T> {
	public void onSuccess(T result) {
	}
}
