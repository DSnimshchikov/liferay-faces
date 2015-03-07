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
package com.liferay.faces.bridge.container.liferay.internal;

import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;

import com.liferay.faces.bridge.container.internal.PortletContainerImpl;


/**
 * This class provides a compatibility layer that isolates differences between JSF1 and JSF2.
 *
 * @author  Neil Griffin
 */
public class PortletContainerLiferayCompatImpl extends PortletContainerImpl implements PhaseListener {

	// serialVersionUID
	private static final long serialVersionUID = 8713570232856573935L;

	@Override
	public void afterPhase(PhaseEvent phaseEvent) {
		// no-op for JSF 1.2
	}

	/**
	 * This method is called prior to the {@link PhaseId#RENDER_RESPONSE} phase of the JSF lifecycle. It's purpose is to
	 * determine if there are any resources in the LIFERAY_SHARED_PAGE_TOP request attribute, so that execution of the
	 * {@link #afterPhase(PhaseEvent)} can be optimized.
	 */
	@Override
	public void beforePhase(PhaseEvent phaseEvent) {
		// no-op for JSF 1.2
	}

	@Override
	public PhaseId getPhaseId() {
		return PhaseId.RENDER_RESPONSE;
	}
}
