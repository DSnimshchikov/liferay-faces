/**
 * Copyright (c) 2000-2014 Liferay, Inc. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */
package com.liferay.faces.alloy.component.inputtextarea;

import javax.faces.component.FacesComponent;
import javax.faces.component.html.HtmlInputTextarea;

import com.liferay.faces.alloy.component.AlloyComponentUtil;
import com.liferay.faces.util.component.ComponentUtil;
import com.liferay.faces.util.component.Styleable;
import com.liferay.faces.util.lang.StringPool;


/**
 * @author  Neil Griffin
 */
@FacesComponent(value = InputTextarea.COMPONENT_TYPE)
public class InputTextarea extends HtmlInputTextarea implements Styleable {

	// Public Constants
	public static final String COMPONENT_TYPE = "com.liferay.faces.alloy.component.inputtextarea.InputTextarea";
	public static final String DELEGATE_COMPONENT_FAMILY = COMPONENT_FAMILY;
	public static final String DELEGATE_RENDERER_TYPE = "javax.faces.Textarea";
	public static final String RENDERER_TYPE = "com.liferay.faces.alloy.component.inputtextarea.InputTextareaRenderer";
	public static final String STYLE_CLASS_NAME = "alloy-input-textarea";

	public InputTextarea() {
		super();
		setRendererType(RENDERER_TYPE);
	}

	@Override
	public String getLabel() {

		String label = (String) getStateHelper().get(StringPool.LABEL);

		if (label == null) {
			label = AlloyComponentUtil.getComponentLabel(this);
		}

		return label;
	}

	@Override
	public void setLabel(String label) {
		getStateHelper().put(StringPool.LABEL, label);
	}

	@Override
	public String getStyleClass() {

		String styleClass = (String) getStateHelper().eval(STYLE_CLASS, null);

		return ComponentUtil.concatCssClasses(styleClass, STYLE_CLASS_NAME);
	}

	@Override
	public void setStyleClass(String styleClass) {
		getStateHelper().put(STYLE_CLASS, styleClass);
	}
}
