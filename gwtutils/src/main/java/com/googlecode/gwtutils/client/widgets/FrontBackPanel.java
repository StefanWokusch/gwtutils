package com.googlecode.gwtutils.client.widgets;

import com.google.gwt.dom.client.Style;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiChild;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.IsWidget;

public class FrontBackPanel extends Composite {

	private FlowPanel panel = new FlowPanel();
	private IsWidget front;
	private IsWidget back;

	private boolean isFront = true;

	public boolean isBackVisible() {
		return !isFront;
	}

	public boolean isFrontVisible() {
		return isFront;
	}

	public FrontBackPanel() {
		initWidget(panel);
	}

	private boolean toggleOnClick = false;

	public void enableToggleOnClick() {
		if (toggleOnClick)
			return;
		toggleOnClick = true;
		panel.addDomHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				toggle();
			}
		}, ClickEvent.getType());
	}

	public void toggle() {
		isFront = !isFront;

		refresh();
	}

	public static enum Side {
		FRONT, BACK
	}

	public void setSide(Side visible) {
		isFront = visible == Side.FRONT;
		refresh();
	}

	private void refresh() {
		String transformFront = "rotateY(" + (isFront ? "0" : "180deg") + ")";
		String transformBack = "rotateY(" + (!isFront ? "0" : "180deg") + ")";
		front.asWidget().getElement().getStyle().setProperty("transform", transformFront);
		front.asWidget().getElement().getStyle().setProperty("WebkitTransform", transformFront);
		back.asWidget().getElement().getStyle().setProperty("transform", transformBack);
		back.asWidget().getElement().getStyle().setProperty("WebkitTransform", transformBack);
	}

	@UiChild
	public void addFront(IsWidget w) {
		panel.add(w);
		front = w;

		setup(w.asWidget().getElement().getStyle(), true);
	}

	@UiChild
	public void addBack(IsWidget w) {
		panel.add(w);
		back = w;

		setup(w.asWidget().getElement().getStyle(), false);
	}

	private void setup(Style s, boolean front) {
		s.setPosition(Position.ABSOLUTE);
		s.setLeft(0, Unit.PX);
		s.setRight(0, Unit.PX);

		String transform = "rotateY(" + (isFront == front ? "0" : "180deg") + ")";
		s.setProperty("transform", transform);
		s.setProperty("WebkitTransform", transform);
		s.setProperty("transition", "-webkit-transform 500ms ease-in-out");
		s.setProperty("transition", "transform 500ms ease-in-out");
		s.setProperty("backfaceVisibility", "hidden");
		s.setProperty("WebkitBackfaceVisibility", "hidden");
	}

}
