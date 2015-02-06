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

import javax.faces.application.ViewHandler;

import com.liferay.faces.util.helper.Wrapper;


/**
 * This class defines a factory for getting an instance of the default {@link ViewHandler} provided by the
 * Mojarra/MyFaces implementation.
 *
 * @author  Neil Griffin
 */
public abstract class ViewHandlerFactory implements Wrapper<ViewHandlerFactory> {

	public abstract ViewHandler getViewHandler();
}
