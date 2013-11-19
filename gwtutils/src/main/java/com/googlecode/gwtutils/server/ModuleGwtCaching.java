package com.googlecode.gwtutils.server;

import com.google.inject.servlet.ServletModule;
import com.googlecode.gwtutils.server.filter.CacheFilter;
import com.googlecode.gwtutils.server.filter.NoCacheFilter;

/**
 * Adds support for *.nocache.* and *.cache.* files generated via gwt
 */
public class ModuleGwtCaching extends ServletModule {
	@Override
	protected void configureServlets() {
		filter("*.nocache.js", "*.nocache.jpg", "*.nocache.png", "*.nocache.gif", "*.nocache.css", "*.nocache.html")//
				.through(NoCacheFilter.class);
		filter("*.cache.js", "*.cache.jpg", "*.cache.png", "*.cache.gif", "*.cache.css", "*.cache.html")//
				.through(CacheFilter.class);
	}
}
