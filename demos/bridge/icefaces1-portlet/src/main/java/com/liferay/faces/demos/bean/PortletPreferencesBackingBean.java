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
package com.liferay.faces.demos.bean;

import java.io.Serializable;
import java.util.Enumeration;
import java.util.Map;

import javax.el.ELResolver;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.portlet.PortletPreferences;
import javax.portlet.PortletRequest;
import javax.portlet.faces.preference.Preference;

import com.liferay.faces.demos.util.FacesMessageUtil;
import com.liferay.faces.util.logging.Logger;
import com.liferay.faces.util.logging.LoggerFactory;


/**
 * @author  Neil Griffin
 */
public class PortletPreferencesBackingBean implements Serializable {

	// serialVersionUID
	private static final long serialVersionUID = 8416378305977838647L;

	// Logger
	Logger logger = LoggerFactory.getLogger(PortletPreferencesBackingBean.class);

	/**
	 * Resets/restores the values in the portletPreferences.xhtml Facelet composition with portlet preference default
	 * values.
	 */
	public void reset(ActionEvent actionEvent) {

		FacesContext facesContext = FacesContext.getCurrentInstance();
		ExternalContext externalContext = facesContext.getExternalContext();
		PortletRequest portletRequest = (PortletRequest) externalContext.getRequest();
		PortletPreferences portletPreferences = portletRequest.getPreferences();

		try {
			Enumeration<String> preferenceNames = portletPreferences.getNames();

			while (preferenceNames.hasMoreElements()) {
				String preferenceName = preferenceNames.nextElement();
				portletPreferences.reset(preferenceName);
			}

			portletPreferences.store();

			FacesMessageUtil.addGlobalSuccessInfoMessage(facesContext);
		}
		catch (Exception e) {
			FacesMessageUtil.addGlobalUnexpectedErrorMessage(facesContext);
		}

	}

	/**
	 * Saves the values in the portletPreferences.xhtml Facelet composition as portlet preferences.
	 */
	public void submit(ActionEvent actionEvent) {

		// The JSR 329 specification defines an EL variable named mutablePortletPreferencesValues that is being used in
		// the portletPreferences.xhtml Facelet composition. This object is of type Map<String, Preference> and is
		// designed to be a model managed-bean (in a sense) that contain preference values. However the only way to
		// access this from a Java class is to evaluate an EL expression (effectively self-injecting) the map into
		// this backing bean.
		FacesContext facesContext = FacesContext.getCurrentInstance();
		ExternalContext externalContext = facesContext.getExternalContext();
		String elExpression = "mutablePortletPreferencesValues";
		ELResolver elResolver = facesContext.getApplication().getELResolver();
		@SuppressWarnings("unchecked")
		Map<String, Preference> mutablePreferenceMap = (Map<String, Preference>) elResolver.getValue(
				facesContext.getELContext(), null, elExpression);

		// Get a list of portlet preference names.
		PortletRequest portletRequest = (PortletRequest) externalContext.getRequest();
		PortletPreferences portletPreferences = portletRequest.getPreferences();
		Enumeration<String> preferenceNames = portletPreferences.getNames();

		try {

			// For each portlet preference name:
			while (preferenceNames.hasMoreElements()) {

				// Get the value specified by the user.
				String preferenceName = preferenceNames.nextElement();
				String preferenceValue = mutablePreferenceMap.get(preferenceName).getValue();

				// Prepare to save the value.
				if (!portletPreferences.isReadOnly(preferenceName)) {
					portletPreferences.setValue(preferenceName, preferenceValue);
				}
			}

			// Save the preference values.
			portletPreferences.store();

			// Report a successful message back to the user as feedback.
			FacesMessageUtil.addGlobalSuccessInfoMessage(facesContext);
		}
		catch (Exception e) {
			logger.error(e);
			FacesMessageUtil.addGlobalUnexpectedErrorMessage(facesContext);
		}
	}
}
