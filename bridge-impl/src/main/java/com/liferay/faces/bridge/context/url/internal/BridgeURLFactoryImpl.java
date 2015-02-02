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
package com.liferay.faces.bridge.context.url.internal;

import java.util.List;
import java.util.Map;

import com.liferay.faces.bridge.container.internal.PortletContainerDetector;
import com.liferay.faces.bridge.context.BridgeContext;
import com.liferay.faces.bridge.context.url.BridgeActionURL;
import com.liferay.faces.bridge.context.url.BridgeRedirectURL;
import com.liferay.faces.bridge.context.url.BridgeResourceURL;
import com.liferay.faces.bridge.context.url.BridgeURLFactory;
import com.liferay.faces.bridge.context.url.liferay.internal.BridgeResourceURLLiferayImpl;


/**
 * @author  Neil Griffin
 */
public class BridgeURLFactoryImpl extends BridgeURLFactory {

	@Override
	public BridgeActionURL getBridgeActionURL(String url, String currentFacesViewId, BridgeContext bridgeContext) {
		return new BridgeActionURLImpl(url, currentFacesViewId, bridgeContext);
	}

	@Override
	public BridgeRedirectURL getBridgeRedirectURL(String url, Map<String, List<String>> parameters,
		String currentFacesViewId, BridgeContext bridgeContext) {
		return new BridgeRedirectURLImpl(url, parameters, currentFacesViewId, bridgeContext);
	}

	@Override
	public BridgeResourceURL getBridgeResourceURL(String url, String currentFacesViewId, BridgeContext bridgeContext) {

		if (PortletContainerDetector.isLiferayPortletRequest(bridgeContext.getPortletRequest())) {
			return new BridgeResourceURLLiferayImpl(url, currentFacesViewId, bridgeContext);
		}
		else {
			return new BridgeResourceURLImpl(url, currentFacesViewId, bridgeContext);
		}
	}

	public BridgeURLFactory getWrapped() {

		// Since this is the factory instance provided by the bridge, it will never wrap another factory.
		return null;
	}

}
