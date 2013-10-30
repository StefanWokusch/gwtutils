package com.googlecode.gwtutils.client.console;

public class ChromeConsole extends Console {

  public native void log(String text) /*-{
		console.log(text);
  }-*/;

  @Override
  public native void group(String groupname) /*-{
		console.group(groupname);
  }-*/;

  @Override
  public native void groupEnd() /*-{
		console.groupEnd();
  }-*/;

  @Override
  public native void time(String name) /*-{
		console.time(name);
  }-*/;

  @Override
  public native void timeEnd(String name) /*-{
		console.timeEnd(name);
  }-*/;

  @Override
  public native void timeStamp(String markername) /*-{
		console.timeStamp(markername);
  }-*/;
}
