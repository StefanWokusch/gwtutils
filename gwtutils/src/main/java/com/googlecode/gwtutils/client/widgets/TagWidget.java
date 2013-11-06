package com.googlecode.gwtutils.client.widgets;

import java.util.Collection;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.FocusEvent;
import com.google.gwt.event.dom.client.FocusHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.MultiWordSuggestOracle;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.SuggestOracle;
import com.google.gwt.user.client.ui.SuggestOracle.Suggestion;

public class TagWidget extends Composite implements HasValue<String> {

	private final FlowPanel panel = new FlowPanel();

	private final HTML addText;
	private final MultiWordSuggestOracle oracle;
	private final SuggestBox addTextBox;
	private final SimplePanel addBox = new SimplePanel();

	public static interface Style extends CssResource {
		String container();

		String addText();

		String tag();

		String tagName();

		String tagDeleteButton();
	}

	public static interface Bundle extends ClientBundle {
		Style tagWidget();
	}

	public final Style style;

	public TagWidget() {
		this(GWT.<Bundle> create(Bundle.class).tagWidget(), new MultiWordSuggestOracle());
	}

	public TagWidget(Style style, SuggestOracle oracle) {
		this.style = style;
		this.oracle = oracle instanceof MultiWordSuggestOracle ? (MultiWordSuggestOracle) oracle : null;

		this.style.ensureInjected();
		panel.setStyleName(style.container());
		initWidget(new SimplePanel(panel));

		panel.getElement().setTabIndex(0);
		panel.addDomHandler(new FocusHandler() {
			@Override
			public void onFocus(FocusEvent event) {
				focus();
			}
		}, FocusEvent.getType());

		addTextBox = new SuggestBox(oracle);
		addTextBox.addDomHandler(new FocusHandler() {
			@Override
			public void onFocus(FocusEvent event) {
				panel.getElement().setTabIndex(-1);
			}
		}, FocusEvent.getType());
		addTextBox.addDomHandler(new BlurHandler() {
			@Override
			public void onBlur(BlurEvent event) {
				panel.getElement().setTabIndex(0);
				blur();
			}
		}, BlurEvent.getType());
		addTextBox.addKeyPressHandler(new KeyPressHandler() {
			@Override
			public void onKeyPress(KeyPressEvent event) {
				if (event.getCharCode() == ' ') {
					event.preventDefault();
					event.stopPropagation();
					addTag(addTextBox.getValue());
					addTextBox.setValue("");
				}
			}
		});
		addTextBox.addSelectionHandler(new SelectionHandler<SuggestOracle.Suggestion>() {
			@Override
			public void onSelection(SelectionEvent<Suggestion> event) {
				addTag(addTextBox.getValue());
				addTextBox.setValue("");
			}
		});

		addTextBox.addKeyDownHandler(new KeyDownHandler() {
			private long last = System.currentTimeMillis();

			@Override
			public void onKeyDown(KeyDownEvent event) {
				if (System.currentTimeMillis() - last > 100 && event.getNativeKeyCode() == KeyCodes.KEY_BACKSPACE
						&& addTextBox.getValue().isEmpty()) {
					last = System.currentTimeMillis();
					event.preventDefault();
					event.stopPropagation();
					if (value.contains(" ")) {
						int lastIndexOf = value.lastIndexOf(" ");
						value = value.substring(0, lastIndexOf);
					} else
						value = "";
					ValueChangeEvent.fire(TagWidget.this, value);
					refersh();
				}
			}
		});

		addText = new HTML("+ Add Tag");
		addText.setStyleName(style.addText());
		addText.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				focus();
			}
		});

		addBox.setWidget(addText);

		panel.add(addBox);
	}

	private void addTag(String newTag) {
		if (!newTag.isEmpty() && !contains(newTag)) {

			if (value.length() > 0)
				value += " ";
			value += newTag;
			ValueChangeEvent.fire(this, value);

			refersh();
		}
	}

	private boolean contains(String tag) {
		for (String t : value.split(" "))
			if (t.equals(tag))
				return true;
		return false;
	}

	@Override
	public HandlerRegistration addValueChangeHandler(ValueChangeHandler<String> handler) {
		return addHandler(handler, ValueChangeEvent.getType());
	}

	@Override
	public void setValue(String value) {
		setValue(value, false);
	}

	private String value = "";

	@Override
	public void setValue(String value, boolean fireEvents) {
		this.value = value;
		refersh();
		if (fireEvents)
			ValueChangeEvent.fire(this, value);
	}

	private void assertOracle() {
		assert (oracle != null) : "Suggestions needs the default-constructor. Otherwise build your own Oracle";
	}

	public void clearSuggestions() {
		assertOracle();
		oracle.clear();
	}

	public void addSuggestion(String suggestion) {
		assertOracle();
		oracle.add(suggestion);
	}

	public void addAllSuggestion(Collection<String> suggestions) {
		assertOracle();
		oracle.addAll(suggestions);
	}

	@Override
	public String getValue() {
		return value;
	}

	private void refersh() {
		// Clear old stuff
		while (panel.getWidgetCount() > 1) {
			panel.remove(0);
		}

		// Add the Tags
		String[] split = value.split(" ");
		for (final String tag : split)
			if (!tag.isEmpty()) {
				FlowPanel tagWidget = new FlowPanel();
				tagWidget.setStyleName(style.tag());

				HTML name = new HTML();
				name.setText(tag);
				name.setStyleName(style.tagName());
				tagWidget.add(name);

				HTML tagDeleteButton = new HTML();
				tagDeleteButton.setStyleName(style.tagDeleteButton());
				tagWidget.add(tagDeleteButton);
				tagWidget.addDomHandler(new ClickHandler() {
					@Override
					public void onClick(ClickEvent event) {
						int start = value.indexOf(tag);
						int end = start + tag.length();
						int maxLen = value.length();
						value = value.substring(0, start) + (end == maxLen ? "" : value.substring(end + 1, maxLen));
						refersh();
					}
				}, ClickEvent.getType());

				panel.insert(tagWidget, panel.getWidgetCount() - 1);
			}
	}

	private void blur() {
		addBox.setWidget(addText);
		addTag(addTextBox.getValue());
		addTextBox.setValue("");
	}

	private void focus() {
		addBox.setWidget(addTextBox);
		addTextBox.setFocus(true);
	}

}
