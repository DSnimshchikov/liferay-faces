/**
 * Copyright (c) 2000-2015 Liferay, Inc. All rights reserved.
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
package com.liferay.faces.alloy.component.nodemenunav.internal;

import java.io.IOException;
import java.util.List;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import com.liferay.faces.alloy.component.menu.Menu;
import com.liferay.faces.alloy.render.internal.DelegatingAlloyRendererBase;
import com.liferay.faces.util.component.Styleable;
import com.liferay.faces.util.lang.StringPool;
import com.liferay.faces.util.render.internal.DelegationResponseWriter;
import com.liferay.faces.util.render.internal.RendererUtil;
import java.util.Map;


/**
 * @author  Vernon Singleton
 */
public abstract class NodeMenuNavRendererBase extends DelegatingAlloyRendererBase {

	// Private constants
	private static final String ALLOY_MODULE_NAME = "node-menunav"; // Needed when yui="false"

	// Public constants
	public static final String COLON_OPTIONS = ":options";
	public static final String IMAGE = "image";

	// Needed when yui="false"
	// Modules
	protected static final String[] MODULES = { ALLOY_MODULE_NAME };

	// Needed when yui="false"
	@Override
	public void encodeAlloyAttributes(FacesContext facesContext, ResponseWriter respoonseWriter,
		UIComponent uiComponent) throws IOException {
		// no-op
	}

	@Override
	public void encodeChildren(FacesContext facesContext, UIComponent uiComponent) throws IOException {

		// Encode all children except for <alloy:menu> since menus get rendered after the anchor tag is closed in
		// encodeEnd.
		List<UIComponent> children = uiComponent.getChildren();

		for (UIComponent child : children) {

			if (!(child instanceof Menu)) {
				child.encodeAll(facesContext);
			}
		}
	}

	@Override
	public void encodeJavaScriptCustom(FacesContext facesContext, UIComponent uiComponent) throws IOException {

		ResponseWriter responseWriter = facesContext.getResponseWriter();

		Map<String, Object> attributes = uiComponent.getAttributes();
		boolean disabled = (Boolean) attributes.get("disabled");

		if (!disabled) {

			String escapedOptionsDivId = RendererUtil.escapeClientId(getDefaultOptionsId(facesContext, uiComponent) +
					StringPool.COLON + "0");

			// AlloyRendererUtil.LIFERAY_Z_INDEX_OVERLAY
			responseWriter.write("A.one('#");
			responseWriter.write(escapedOptionsDivId);
			responseWriter.write("')._node['style'].zIndex=" + LIFERAY_Z_INDEX_OVERLAY + ";");

			// The <div> containing menu items was initially styled with "display:none;" in order to prevent blinking
			// when JavaScript attempts to hide it. At this point in JavaScript execution, JavaScript is done
			// manipulating the DOM and it is necessary to set the style back to "display:block;" so that the menu items
			// will be visible when needed.
			responseWriter.write("A.one('#");
			responseWriter.write(escapedOptionsDivId);
			responseWriter.write("')._node['style'].display='block';");
		}
	}

	@Override
	public void encodeJavaScriptMain(FacesContext facesContext, UIComponent uiComponent) throws IOException {

		ResponseWriter responseWriter = facesContext.getResponseWriter();
		String clientId = uiComponent.getClientId(facesContext);
		String escapeClientId = RendererUtil.escapeClientId(clientId);

		responseWriter.write("A.one('#");
		responseWriter.write(escapeClientId);
		responseWriter.write("').plug(A.Plugin.NodeMenuNav,{autoSubmenuDisplay:false,mouseOutHideDelay:0});");
	}

	public void encodeLabel(UIComponent uiComponent, ResponseWriter responseWriter, FacesContext facesContext,
		int depth) throws IOException {

		UIComponent facet = uiComponent.getFacet(StringPool.LABEL);

		if (facet == null) {
			String label = (String) uiComponent.getAttributes().get(StringPool.LABEL);

			if (label == null) {

				if (depth == 0) {
					responseWriter.startElement(StringPool.SPAN, uiComponent);
					responseWriter.writeAttribute(StringPool.CLASS, "caret", StringPool.CLASS);
					responseWriter.endElement(StringPool.SPAN);
				}
				else {
					responseWriter.startElement(StringPool.SPAN, uiComponent);
					responseWriter.write(StringPool.NBSP);
					responseWriter.endElement(StringPool.SPAN);
				}
			}
			else {
				responseWriter.writeText(label, null);
			}
		}
		else {
			facet.encodeAll(facesContext);
		}
	}

	@Override
	public void encodeMarkupBegin(FacesContext facesContext, UIComponent uiComponent) throws IOException {

		ResponseWriter responseWriter = facesContext.getResponseWriter();

		String clientId = uiComponent.getClientId(facesContext);

		// start yui3-menu div
		responseWriter.startElement(StringPool.DIV, uiComponent);
		responseWriter.writeAttribute(StringPool.ID, clientId, StringPool.ID);
		responseWriter.writeAttribute(StringPool.CLASS, "yui3-menu yui3-menu-horizontal yui3-splitbuttonnav",
			StringPool.CLASS);

		// start yui3-menu-content div
		responseWriter.startElement(StringPool.DIV, uiComponent);
		responseWriter.writeAttribute(StringPool.CLASS, "yui3-menu-content", StringPool.CLASS);

		responseWriter.startElement(StringPool.UL, uiComponent);
		responseWriter.startElement(StringPool.LI, uiComponent);

		// Start the span containing the btn-group
		responseWriter.startElement(StringPool.SPAN, uiComponent);
		responseWriter.writeAttribute(StringPool.CLASS, "yui3-menu-label btn-group", StringPool.CLASS);

		// ResponseWriter blocks the text value and blocks writing of URIAttributes, if necessary
		Map<String, Object> attributes = uiComponent.getAttributes();
		boolean disabled = (Boolean) attributes.get("disabled");
		Styleable styleable = (Styleable) uiComponent;
		String styleClass = styleable.getStyleClass();
		DelegationResponseWriter delegationResponseWriter = new NodeMenuNavResponseWriter(responseWriter, disabled,
				uiComponent.getClientId(facesContext), styleClass);

		//J-
		// We have now written out something like this:
		//
		// <div id="menuId3" class="yui3-menu yui3-menu-horizontal yui3-splitbuttonnav">
		//	<div class="yui3-menu-content">
		//		<ul>
		//			<li>
		//				<span class="yui3-menu-label btn-group">
		//					... the main anchor tag for the splitButton goes here ...
		//J+

		// start the main anchor tag
		super.encodeMarkupBegin(facesContext, uiComponent, delegationResponseWriter);

	}

	@Override
	public void encodeMarkupEnd(FacesContext facesContext, UIComponent uiComponent) throws IOException {

		// close the main anchor tag
		super.encodeMarkupEnd(facesContext, uiComponent);

		ResponseWriter responseWriter = facesContext.getResponseWriter();
		Map<String, Object> attributes = uiComponent.getAttributes();
		boolean disabled = (Boolean) attributes.get("disabled");
		Styleable styleable = (Styleable) uiComponent;
		String styleClass = styleable.getStyleClass();
		String defaultOptionsDivId = getDefaultOptionsId(facesContext, uiComponent);

		responseWriter.startElement("a", uiComponent);
		responseWriter.writeAttribute(StringPool.CLASS, styleClass, StringPool.CLASS);

		int depth = 0;

		if (!disabled) {
			responseWriter.writeAttribute(StringPool.HREF,
				StringPool.POUND + defaultOptionsDivId + StringPool.COLON + depth, StringPool.HREF);
		}

		List<UIComponent> children = uiComponent.getChildren();

		// Find the menu among the children and render its label, if any, and then recurse over the menu's children
		for (UIComponent child : children) {

			if (child instanceof Menu) {

				encodeLabel(child, responseWriter, facesContext, depth);
				responseWriter.endElement("a");

				// End the span containing the btn-group
				responseWriter.endElement(StringPool.SPAN);

				// Recurse over (first and only expected) menu
				encodeMenuRecurse(child, responseWriter, disabled, styleClass, defaultOptionsDivId, depth,
					facesContext);

				break;
			}
		}

		responseWriter.endElement(StringPool.LI);
		responseWriter.endElement(StringPool.UL);

		// end yui3-menu-content div
		responseWriter.endElement(StringPool.DIV);

		// end yui3-menu div
		responseWriter.endElement(StringPool.DIV);

	}

	public void encodeMenuRecurse(UIComponent uiComponent, ResponseWriter responseWriter, boolean disabled,
		String styleClass, String optionsDivId, int depth, FacesContext facesContext) throws IOException {

		String menuId = optionsDivId + StringPool.COLON + depth;

		// Start a listItem tag for a sub-menu
		if (depth > 0) {
			responseWriter.startElement(StringPool.LI, uiComponent);
			responseWriter.startElement("a", uiComponent);
			responseWriter.writeAttribute(StringPool.CLASS, "yui3-menu-label", StringPool.CLASS);

			if (!disabled) {
				responseWriter.writeAttribute(StringPool.HREF, StringPool.POUND + menuId, StringPool.HREF);
			}

			encodeLabel(uiComponent, responseWriter, facesContext, depth);
			responseWriter.endElement("a");
		}

		// Start inner yui3-menu div
		responseWriter.startElement(StringPool.DIV, uiComponent);
		responseWriter.writeAttribute(StringPool.ID, menuId, StringPool.ID);
		responseWriter.writeAttribute(StringPool.CLASS, "yui3-menu", StringPool.CLASS);

		// Hide the main menu to prevent blinking
		if (depth == 0) {
			responseWriter.writeAttribute("style", "display: none;", "style");
		}

		// Start inner yui3-menu-content div
		responseWriter.startElement(StringPool.DIV, uiComponent);
		responseWriter.writeAttribute(StringPool.CLASS, "yui3-menu-content", StringPool.CLASS);

		responseWriter.startElement(StringPool.UL, uiComponent);

		// Encode the children of the menu
		List<UIComponent> children = uiComponent.getChildren();

		for (UIComponent child : children) {

			responseWriter.startElement(StringPool.LI, uiComponent);
			responseWriter.writeAttribute(StringPool.CLASS, "yui3-menuitem", StringPool.CLASS);

			ResponseWriter originalResponseWriter = facesContext.getResponseWriter();
			DelegationResponseWriter delegationResponseWriter = new NodeMenuNavMenuResponseWriter(
					originalResponseWriter);
			facesContext.setResponseWriter(delegationResponseWriter);
			child.encodeAll(facesContext);
			facesContext.setResponseWriter(originalResponseWriter);
			responseWriter.endElement(StringPool.LI);
		}

		responseWriter.endElement(StringPool.UL);

		// End inner yui3-menu-content div
		responseWriter.endElement(StringPool.DIV);

		// End inner yui3-menu div
		responseWriter.endElement(StringPool.DIV);

		// End the listItem tag for a sub-menu
		if (depth > 0) {
			responseWriter.endElement(StringPool.LI);
		}
	}

	@Override
	public String getAlloyClassName(FacesContext facesContext, UIComponent uiComponent) {
		return null;
	}

	protected String getDefaultOptionsId(FacesContext facesContext, UIComponent uiComponent) {
		return uiComponent.getClientId(facesContext) + COLON_OPTIONS;
	}

	@Override
	public String getDelegateComponentFamily() {
		return null;
	}

	@Override
	public String getDelegateRendererType() {
		return null;
	}

	// Needed when yui="false"
	@Override
	protected String[] getModules(FacesContext facesContext, UIComponent uiComponent) {
		return MODULES;
	}

	@Override
	public boolean getRendersChildren() {
		return true;
	}
}
