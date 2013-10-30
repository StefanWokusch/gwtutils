package com.googlecode.gwtutils.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.GWT.UncaughtExceptionHandler;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.dom.client.Document;
import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.user.client.Window;

/**
 * Main Class with some Boilerbladecode to:
 * 
 * - Update the deviceType in the Body element (example: <body class="phone tablet not-desktop">)
 * 
 * - Sets an UncaughtExceptionHandler to handle these Exceptions well
 * 
 * - use UncaughtExceptionhandler on first call in DevMode
 */
public abstract class AbstractEntryPoint implements EntryPoint {

	@Override
	public final void onModuleLoad() {
		GWT.setUncaughtExceptionHandler(new UncaughtExceptionHandler() {
			@Override
			public void onUncaughtException(Throwable e) {
				// TODO Send via GWT-Logging
				e.printStackTrace();
				onUncaughtException(e);
			}
		});
		if (GWT.isProdMode())
			onLoad();
		else
			Scheduler.get().scheduleDeferred(new ScheduledCommand() {
				@Override
				public void execute() {
					onLoad();
				}
			});

		Window.addResizeHandler(new ResizeHandler() {
			@Override
			public void onResize(ResizeEvent event) {
				updateDevice();
			}
		});
		updateDevice();
	}

	private static final int CLIENTWIDTH_COMPUTER_LOWER = 980;
	private static final int CLIENTWIDTH_TABLET_LOWER = 480;
	private static final int CLIENTWIDTH_TABLET_UPPER = 1200;
	private static final int CLIENTWIDTH_PHONE_UPPER = 767;

	private void updateDevice() {
		int w = Window.getClientWidth();

		boolean desktop = w >= CLIENTWIDTH_COMPUTER_LOWER;
		boolean tablet = w > CLIENTWIDTH_TABLET_LOWER && w < CLIENTWIDTH_TABLET_UPPER;
		boolean phone = w <= CLIENTWIDTH_PHONE_UPPER;

		StringBuffer device = new StringBuffer();
		device.append(desktop ? "desktop" : "not-desktop");
		device.append(tablet ? " tablet" : " not-tablet");
		device.append(phone ? " phone" : " not-phone");

		Document.get().getBody().setClassName(device.toString());
	}

	protected abstract void onLoad();

	protected abstract void onUncaughtException(Throwable e);
}
