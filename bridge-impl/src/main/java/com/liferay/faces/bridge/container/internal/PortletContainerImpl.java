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
package com.liferay.faces.bridge.container.internal;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.portlet.BaseURL;
import javax.portlet.MimeResponse;
import javax.portlet.PortletRequest;
import javax.portlet.PortletURL;
import javax.portlet.ResourceURL;
import javax.portlet.faces.Bridge;
import javax.portlet.faces.Bridge.PortletPhase;

import com.liferay.faces.bridge.context.BridgeContext;
import com.liferay.faces.bridge.internal.BridgeConstants;
import com.liferay.faces.bridge.util.internal.RequestParameter;
import com.liferay.faces.util.application.ResourceConstants;
import com.liferay.faces.util.logging.Logger;
import com.liferay.faces.util.logging.LoggerFactory;


/**
 * @author  Neil Griffin
 */
public class PortletContainerImpl extends PortletContainerCompatImpl {

	// Logger
	private static final Logger logger = LoggerFactory.getLogger(PortletContainerImpl.class);

	public PortletURL createActionURL(String fromURL) throws MalformedURLException {

		try {
			logger.debug("createActionURL fromURL=[" + fromURL + "]");

			BridgeContext bridgeContext = BridgeContext.getCurrentInstance();
			MimeResponse mimeResponse = (MimeResponse) bridgeContext.getPortletResponse();
			PortletURL actionURL = createActionURL(mimeResponse);
			copyRequestParameters(fromURL, actionURL);

			return actionURL;
		}
		catch (ClassCastException e) {
			throw new MalformedURLException(e.getMessage());
		}
	}

	public ResourceURL createPartialActionURL(String fromURL) throws MalformedURLException {
		logger.debug("createPartialActionURL fromURL=[" + fromURL + "]");

		return createResourceURL(fromURL);
	}

	/**
	 * Note that this default method implementation doesn't help when a <redirect /> is present in the navigation-rule.
	 * That's because the JSF implementation will end up calling this method during the Portlet 2.0 ACTION_PHASE, and
	 * it's impossible for us to get a redirect URL (really, a render URL) from an ActionResponse. This method will need
	 * to be overridden for each portlet container and handled in a container-dependent way.
	 */
	public PortletURL createRedirectURL(String fromURL, Map<String, List<String>> parameters)
		throws MalformedURLException {

		PortletURL redirectURL;

		BridgeContext bridgeContext = BridgeContext.getCurrentInstance();
		PortletPhase portletRequestPhase = bridgeContext.getPortletRequestPhase();

		if ((portletRequestPhase == Bridge.PortletPhase.RENDER_PHASE) ||
				(portletRequestPhase == Bridge.PortletPhase.RESOURCE_PHASE)) {

			try {
				logger.debug("createRedirectURL fromURL=[" + fromURL + "]");

				MimeResponse mimeResponse = (MimeResponse) bridgeContext.getPortletResponse();
				redirectURL = mimeResponse.createRenderURL();
				copyRequestParameters(fromURL, redirectURL);

				if (parameters != null) {
					Set<String> parameterNames = parameters.keySet();

					for (String parameterName : parameterNames) {
						List<String> parameterValues = parameters.get(parameterName);
						String[] parameterValuesArray = parameterValues.toArray(new String[parameterValues.size()]);
						redirectURL.setParameter(parameterName, parameterValuesArray);
					}
				}
			}
			catch (ClassCastException e) {
				throw new MalformedURLException(e.getMessage());
			}
		}
		else {
			throw new UnsupportedOperationException("Unable to create a redirectURL (renderURL) during " +
				portletRequestPhase + " from URL=[" + fromURL + "]");
		}

		return redirectURL;
	}

	public PortletURL createRenderURL(String fromURL) throws MalformedURLException {
		BridgeContext bridgeContext = BridgeContext.getCurrentInstance();
		PortletPhase portletRequestPhase = bridgeContext.getPortletRequestPhase();

		if ((portletRequestPhase == Bridge.PortletPhase.RENDER_PHASE) ||
				(portletRequestPhase == Bridge.PortletPhase.RESOURCE_PHASE)) {

			try {
				logger.debug("createRenderURL fromURL=[" + fromURL + "]");

				MimeResponse mimeResponse = (MimeResponse) bridgeContext.getPortletResponse();
				PortletURL renderURL = createRenderURL(mimeResponse);
				copyRequestParameters(fromURL, renderURL);

				return renderURL;
			}
			catch (ClassCastException e) {
				throw new MalformedURLException(e.getMessage());
			}
		}
		else {
			throw new MalformedURLException("Unable to create a RenderURL during " + portletRequestPhase.toString());
		}

	}

	public ResourceURL createResourceURL(String fromURL) throws MalformedURLException {

		try {
			logger.debug("createResourceURL fromURL=[" + fromURL + "]");

			// Ask the portlet container to create a portlet resource URL.
			BridgeContext bridgeContext = BridgeContext.getCurrentInstance();
			MimeResponse mimeResponse = (MimeResponse) bridgeContext.getPortletResponse();
			ResourceURL resourceURL = createResourceURL(mimeResponse);

			// If the "javax.faces.resource" token is found in the URL, then
			int tokenPos = fromURL.indexOf(ResourceConstants.JAVAX_FACES_RESOURCE);

			if (tokenPos >= 0) {

				// Parse-out the resourceId
				String resourceId = fromURL.substring(tokenPos);

				// Parse-out the resourceName and convert it to a URL parameter on the portlet resource URL.
				int queryStringPos = resourceId.indexOf('?');

				String resourceName = resourceId;

				if (queryStringPos > 0) {
					resourceName = resourceName.substring(0, queryStringPos);
				}

				int slashPos = resourceName.indexOf('/');

				if (slashPos > 0) {
					resourceName = resourceName.substring(slashPos + 1);
				}
				else {
					logger.error("There is no slash after the [{0}] token in resourceURL=[{1}]",
						ResourceConstants.JAVAX_FACES_RESOURCE, fromURL);
				}

				resourceURL.setParameter(ResourceConstants.JAVAX_FACES_RESOURCE, resourceName);
				logger.debug("Added parameter to portletURL name=[{0}] value=[{1}]",
					ResourceConstants.JAVAX_FACES_RESOURCE, resourceName);
			}

			// Copy the request parameters to the portlet resource URL.
			copyRequestParameters(fromURL, resourceURL);

			return resourceURL;
		}
		catch (ClassCastException e) {
			throw new MalformedURLException(e.getMessage());
		}
	}

	/**
	 * Copies any query paramters present in the specified "from" URL to the specified "to" URL.
	 *
	 * @param   fromURL  The String-based URL to copy query parameters from.
	 * @param   toURL    The portlet-based URL to copy query parameters to.
	 *
	 * @throws  MalformedURLException
	 */
	protected void copyRequestParameters(String fromURL, BaseURL toURL) throws MalformedURLException {
		List<RequestParameter> requestParameters = parseRequestParameters(fromURL);

		if (requestParameters != null) {

			for (RequestParameter requestParameter : requestParameters) {
				String name = requestParameter.getName();
				String value = requestParameter.getValue();
				toURL.setParameter(name, value);
				logger.debug("Copied parameter to portletURL name=[{0}] value=[{1}]", name, value);
			}
		}
	}

	protected PortletURL createActionURL(MimeResponse mimeResponse) {
		return mimeResponse.createActionURL();
	}

	protected PortletURL createRenderURL(MimeResponse mimeResponse) {
		return mimeResponse.createRenderURL();
	}

	protected ResourceURL createResourceURL(MimeResponse mimeResponse) {
		return mimeResponse.createResourceURL();
	}

	/**
	 * Parses the specified URL and returns a list of query parameters that are found.
	 *
	 * @param   url  The URL to parse.
	 *
	 * @return  The list of query parameters found.
	 *
	 * @throws  MalformedURLException
	 */
	protected List<RequestParameter> parseRequestParameters(String url) throws MalformedURLException {

		List<RequestParameter> requestParameters = null;

		if (url != null) {
			int pos = url.indexOf("?");

			if (pos >= 0) {
				String queryString = url.substring(pos + 1);

				if (queryString.length() > 0) {
					requestParameters = new ArrayList<RequestParameter>();

					String[] queryParameters = queryString.split(BridgeConstants.REGEX_AMPERSAND_DELIMITER);

					for (String queryParameter : queryParameters) {
						String[] nameValueArray = queryParameter.split("[=]");

						if (nameValueArray.length == 2) {
							String name = nameValueArray[0];
							String value = nameValueArray[1];
							requestParameters.add(new RequestParameter(name, value));
						}
						else {
							throw new MalformedURLException("invalid name/value pair: " + queryParameter);
						}
					}
				}
			}
		}

		return requestParameters;
	}

	public String[] getHeader(String name) {
		BridgeContext bridgeContext = BridgeContext.getCurrentInstance();
		PortletRequest portletRequest = bridgeContext.getPortletRequest();
		Enumeration<String> properties = portletRequest.getProperties(name);
		List<String> propertyList = new ArrayList<String>();

		while (properties.hasMoreElements()) {
			propertyList.add(properties.nextElement());
		}

		return propertyList.toArray(new String[propertyList.size()]);
	}

	public long getHttpServletRequestDateHeader(String name) {

		// Unsupported by default implementation.
		return -1L;
	}
}
