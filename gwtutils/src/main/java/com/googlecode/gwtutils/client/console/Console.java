package com.googlecode.gwtutils.client.console;

import com.google.gwt.core.shared.GWT;

/**
 * Use of the Browsers Console like in Chrome with support for group and time
 */
public class Console {
	public static final Console INSTANCE = GWT.create(Console.class);

	/**
	 * Simple Logging
	 * 
	 * @param text
	 *          text to print to the console
	 */
	public void log(String text) {
	}

	public void group(String groupname) {
	}

	public void groupEnd() {
	}

	public void time(String name) {
	}

	public void timeEnd(String name) {
	}

	/**
	 * Used for example to set markers in the timeline
	 * 
	 * @param markername
	 *          name of the Marker
	 */
	public void timeStamp(String markername) {
	}
}
