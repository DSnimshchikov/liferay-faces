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
package com.liferay.faces.bridge.context.internal;

import java.io.IOException;
import java.io.Writer;
import java.util.EmptyStackException;

import javax.faces.component.UIComponent;
import javax.faces.context.ResponseWriter;

import com.liferay.faces.bridge.context.HeadResponseWriter;
import com.liferay.faces.bridge.renderkit.html_basic.internal.ElementWriterStack;
import org.w3c.dom.Element;

import com.liferay.faces.util.lang.StringPool;


/**
 * @author  Neil Griffin
 */
public abstract class HeadResponseWriterBase extends HeadResponseWriter {

	// Private Data Members
	ResponseWriter wrappedResponseWriter;
	ElementWriterStack elementWriterStack;

	public HeadResponseWriterBase(ResponseWriter wrappedResponseWriter) {
		this.wrappedResponseWriter = wrappedResponseWriter;
		this.elementWriterStack = new ElementWriterStack();
	}

	public abstract Element createElement(String name);

	@Override
	public Writer append(CharSequence csq) throws IOException {

		try {
			elementWriterStack.safePeek().append(csq);
		}
		catch (EmptyStackException e) {
			throw new IOException(EmptyStackException.class.getSimpleName());
		}

		return this;
	}

	@Override
	public Writer append(char c) throws IOException {

		try {
			elementWriterStack.safePeek().append(c);
		}
		catch (EmptyStackException e) {
			throw new IOException(EmptyStackException.class.getSimpleName());
		}

		return this;
	}

	@Override
	public Writer append(CharSequence csq, int start, int end) throws IOException {

		try {
			elementWriterStack.safePeek().append(csq, start, end);
		}
		catch (EmptyStackException e) {
			throw new IOException(EmptyStackException.class.getSimpleName());
		}

		return this;
	}

	@Override
	public void close() throws IOException {

		try {
			elementWriterStack.safePeek().close();
		}
		catch (EmptyStackException e) {
			throw new IOException(EmptyStackException.class.getSimpleName());
		}
	}

	@Override
	public void endCDATA() throws IOException {

		try {
			elementWriterStack.safePeek().write(StringPool.CDATA_CLOSE);
		}
		catch (EmptyStackException e) {
			throw new IOException(EmptyStackException.class.getSimpleName());
		}
	}

	@Override
	public void endDocument() throws IOException {
		// no-op
	}

	@Override
	public void flush() throws IOException {

		try {
			elementWriterStack.safePeek().flush();
		}
		catch (EmptyStackException e) {
			throw new IOException(EmptyStackException.class.getSimpleName());
		}
	}

	@Override
	public void startCDATA() throws IOException {

		try {
			elementWriterStack.safePeek().write(StringPool.CDATA_OPEN);
		}
		catch (EmptyStackException e) {
			throw new IOException(EmptyStackException.class.getSimpleName());
		}
	}

	@Override
	public void startDocument() throws IOException {
		// no-op
	}

	@Override
	public void write(int c) throws IOException {

		try {
			elementWriterStack.safePeek().write(c);
		}
		catch (EmptyStackException e) {
			throw new IOException(EmptyStackException.class.getSimpleName());
		}
	}

	@Override
	public void write(char[] cbuf) throws IOException {

		if (cbuf != null) {

			try {
				elementWriterStack.safePeek().write(cbuf);
			}
			catch (EmptyStackException e) {
				throw new IOException(EmptyStackException.class.getSimpleName());
			}
		}
	}

	@Override
	public void write(String str) throws IOException {

		if (str != null) {

			try {
				elementWriterStack.safePeek().write(str);
			}
			catch (EmptyStackException e) {
				throw new IOException(EmptyStackException.class.getSimpleName());
			}
		}
	}

	@Override
	public void write(char[] cbuf, int off, int len) throws IOException {

		if (cbuf != null) {

			try {
				elementWriterStack.safePeek().write(cbuf, off, len);
			}
			catch (EmptyStackException e) {
				throw new IOException(EmptyStackException.class.getSimpleName());
			}
		}
	}

	@Override
	public void write(String str, int off, int len) throws IOException {

		if (str != null) {

			try {
				elementWriterStack.safePeek().write(str, off, len);
			}
			catch (EmptyStackException e) {
				throw new IOException(EmptyStackException.class.getSimpleName());
			}
		}
	}

	@Override
	public void writeAttribute(String name, Object value, String property) throws IOException {

		try {

			if (value == null) {
				elementWriterStack.safePeek().getElement().setAttribute(name, null);
			}
			else {
				elementWriterStack.safePeek().getElement().setAttribute(name, value.toString());
			}
		}
		catch (EmptyStackException e) {
			throw new IOException(EmptyStackException.class.getSimpleName());
		}
	}

	@Override
	public void writeComment(Object comment) throws IOException {

		if (comment != null) {

			try {
				elementWriterStack.safePeek().write(comment.toString());
			}
			catch (EmptyStackException e) {
				throw new IOException(EmptyStackException.class.getSimpleName());
			}
		}
	}

	@Override
	public void writeText(Object text, String property) throws IOException {

		if (text != null) {

			try {
				elementWriterStack.safePeek().write(text.toString());
			}
			catch (EmptyStackException e) {
				throw new IOException(EmptyStackException.class.getSimpleName());
			}
		}
	}

	@Override
	public void writeText(Object text, UIComponent component, String property) throws IOException {

		if (text != null) {

			try {
				elementWriterStack.safePeek().write(text.toString());
			}
			catch (EmptyStackException e) {
				throw new IOException(EmptyStackException.class.getSimpleName());
			}
		}
	}

	@Override
	public void writeText(char[] text, int off, int len) throws IOException {

		if (text != null) {

			try {
				elementWriterStack.safePeek().write(text, off, len);
			}
			catch (EmptyStackException e) {
				throw new IOException(EmptyStackException.class.getSimpleName());
			}
		}
	}

	@Override
	public void writeURIAttribute(String name, Object value, String property) throws IOException {

		if (value != null) {
			value = escapeURI(value.toString());
		}

		writeAttribute(name, value, property);
	}

	protected String escapeURI(String uri) {

		if (uri.length() == 0) {
			return StringPool.BLANK;
		}

		// Escape using XSS recommendations from
		// http://www.owasp.org/index.php/Cross_Site_Scripting#How_to_Protect_Yourself
		StringBuilder sb = null;

		int lastReplacementIndex = 0;

		for (int i = 0; i < uri.length(); i++) {

			char c = uri.charAt(i);
			String replacement = null;

			switch (c) {

			case '<': {
				replacement = "&lt;";

				break;
			}

			case '>': {
				replacement = "&gt;";

				break;
			}

			case '&': {
				replacement = "&amp;";

				break;
			}

			case '"': {
				replacement = "&#034;";

				break;
			}

			case '\'': {
				replacement = "&#039;";

				break;
			}

			case '\u00bb': {
				replacement = "&#187;";

				break;
			}

			case '\u2013': {
				replacement = "&#x2013;";

				break;
			}

			case '\u2014': {
				replacement = "&#x2014;";

				break;
			}
			}

			if (replacement != null) {

				if (sb == null) {
					sb = new StringBuilder();
				}

				if (i > lastReplacementIndex) {
					sb.append(uri.substring(lastReplacementIndex, i));
				}

				sb.append(replacement);

				lastReplacementIndex = i + 1;
			}
		}

		if (sb == null) {
			return uri;
		}

		if (lastReplacementIndex < uri.length()) {
			sb.append(uri.substring(lastReplacementIndex));
		}

		return sb.toString();
	}

	@Override
	public ResponseWriter getWrapped() {
		return wrappedResponseWriter;
	}
}
