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
package com.liferay.faces.bridge.application.internal;

import javax.faces.application.Application;


/**
 * This class provides a compatibility layer that isolates differences between JSF1 and JSF2.
 *
 * @author  Neil Griffin
 */
public abstract class ApplicationCompatImpl extends ApplicationWrapper {

	// Private Data Members
	private Application wrappedApplication;

	public ApplicationCompatImpl(Application application) {
		this.wrappedApplication = application;
	}

	@SuppressWarnings("deprecation")
	protected void subscribeToJSF2SystemEvent(
		com.liferay.faces.bridge.config.ConfiguredSystemEventListener configuredSystemEventListener) {
		// This is a no-op for JSF 1.2
	}

	/**
	 * @see  ApplicationWrapper#getWrapped()
	 */
	@Override
	public Application getWrapped() {
		return wrappedApplication;
	}
}
