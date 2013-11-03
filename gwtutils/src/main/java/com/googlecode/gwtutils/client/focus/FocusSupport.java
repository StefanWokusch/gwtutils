package com.googlecode.gwtutils.client.focus;

import com.google.gwt.dom.client.Element;
import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.event.logical.shared.AttachEvent.Handler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Event.NativePreviewEvent;
import com.google.gwt.user.client.Event.NativePreviewHandler;
import com.google.gwt.user.client.ui.IsWidget;

/**
 * Use for manual focus for some individual Widgets.
 * 
 * It will set the focus by clicking on the Widget, and blur by clicking anywhere else
 */
public class FocusSupport {
	public static interface HasFocus extends IsWidget {
		void onFocusChanged(boolean focus);
	}

	private final HasFocus widget;
	private boolean currentFocus = false;

	public FocusSupport(HasFocus widget) {
		this.widget = widget;

		widget.asWidget().addAttachHandler(new Handler() {
			@Override
			public void onAttachOrDetach(AttachEvent event) {
				if (event.isAttached())
					onAttach();
				else
					onDetach();
			}
		});
		if (widget.asWidget().isAttached())
			onAttach();
	}

	private final NativePreviewHandler handler = new NativePreviewHandler() {
		@Override
		public void onPreviewNativeEvent(NativePreviewEvent event) {
			if (event.getTypeInt() == Event.ONCLICK) {
				Element rootElement = widget.asWidget().getElement();
				boolean isChildOfRoot = false;
				Element source = (Element) event.getNativeEvent().getEventTarget().cast();
				for (Element e = source; e != null; e = e.getParentElement()) {
					if (e == rootElement) {
						isChildOfRoot = true;
						break;
					}
				}

				setFocus(isChildOfRoot);
			}
		}
	};

	private HandlerRegistration reg;

	private void onAttach() {
		reg = Event.addNativePreviewHandler(handler);
	}

	private void onDetach() {
		reg.removeHandler();
	}

	public boolean isFocus() {
		return currentFocus;
	}

	public void setFocus(boolean isFocused) {
		if (isFocused != currentFocus) {
			currentFocus = isFocused;
			widget.onFocusChanged(currentFocus);
		}
	}
}
