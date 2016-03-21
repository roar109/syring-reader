package com.github.roar109.syring.reader.manifest.formatter.impl;

import java.util.List;
import java.util.Map.Entry;

import com.github.roar109.syring.reader.manifest.formatter.Formatter;
import com.github.roar109.syring.reader.manifest.model.FileProperties;
import com.github.roar109.syring.reader.manifest.model.JNDI;
import com.github.roar109.syring.reader.manifest.model.Manifest;

public class HtmlFormatter implements Formatter {

	private static final String TABLE_OPEN = "<TABLE border=1>";
	private static final String TABLE_CLOSE = "</table>";
	private static final String ROW_OPEN = "<TR>";
	private static final String ROW_CLOSE = "</TR>";
	private static final String DATA_OPEN = "<TD>";
	private static final String DATA_CLOSE = "</TD>";
	private static final String NEW_PARAGRAPH = "<P/>";

	/**
	 * 
	 * Overrides startContent
	 * 
	 * @return opening html and body tags
	 * @since Mar 29, 2010
	 * @see org.rage.manifest.Formatter#startContent()
	 */
	public String startContent() {
		return "<html><body>";
	}

	/**
	 * 
	 * Overrides endContent
	 * 
	 * @return closing html and body tags
	 * @since Mar 29, 2010
	 * @see org.rage.manifest.Formatter#endContent()
	 */
	public String endContent() {
		return "</body></html>";
	}

	/**
	 * Overrides format
	 * 
	 * Adds html paragraph tags around the string to ensure some white space
	 * around it in the html output
	 * 
	 * @param information
	 * @return the same string that was sent in with some html paragraph tags
	 *         around it
	 * @since Mar 29, 2010
	 * @see org.rage.manifest.Formatter#format(java.lang.String)
	 */
	public String format(final String information) {
		final StringBuilder html = new StringBuilder();
		html.append(NEW_PARAGRAPH);
		html.append(NEW_PARAGRAPH);
		html.append(information);
		html.append(NEW_PARAGRAPH);
		html.append(NEW_PARAGRAPH);
		return html.toString();
	}

	/**
	 * Overrides format
	 * 
	 * creates an html table with the Manifest information
	 * 
	 * @param manifest
	 * @return Manifest in html format
	 * @since Mar 26, 2010
	 * @see org.rage.manifest.Formatter#format(com.fitness.util.Manifest)
	 */
	public String format(final Manifest manifest) {
		final StringBuilder html = new StringBuilder();
		html.append(TABLE_OPEN);
		html.append(ROW_OPEN);
		html.append("<td colspan=2>");
		html.append(manifest.getName());
		html.append(DATA_CLOSE);
		html.append(ROW_CLOSE);
		
		if (null != manifest.getEntries()) {
			for (final Entry<String, String> entry : manifest.getEntries().entrySet()) {
				html.append(ROW_OPEN);
				html.append(DATA_OPEN);
				html.append(entry.getKey());
				html.append(DATA_CLOSE);
				html.append(DATA_OPEN);
				html.append(entry.getValue());
				html.append(DATA_CLOSE);
				html.append(ROW_CLOSE);
			}
		}
		html.append(TABLE_CLOSE);
		html.append(NEW_PARAGRAPH);
		html.append(NEW_PARAGRAPH);
		html.append(NEW_PARAGRAPH);
		return html.toString();
	}

	@Override
	public String formatJndi(final List<JNDI> jndis) {
		final StringBuilder html = new StringBuilder();
		html.append(TABLE_OPEN);
		html.append(ROW_OPEN);
		html.append("<td colspan=2>");
		html.append("JNDI");
		html.append(DATA_CLOSE);
		html.append(ROW_CLOSE);
		if (null != jndis) {
			for (final JNDI jndi : jndis) {
				html.append(ROW_OPEN);
				html.append(DATA_OPEN);
				html.append(jndi.getJndiName());
				html.append(DATA_CLOSE);
				html.append(DATA_OPEN);
				html.append(jndi.getJndiValue());
				html.append(DATA_CLOSE);
				html.append(ROW_CLOSE);
			}
		}
		html.append(TABLE_CLOSE);
		html.append(NEW_PARAGRAPH);
		html.append(NEW_PARAGRAPH);
		html.append(NEW_PARAGRAPH);
		return html.toString();
	}

	@Override
	public String formatFileProperties(final List<FileProperties> fileProperties) {
		final StringBuilder html = new StringBuilder();
		html.append(TABLE_OPEN);
		html.append(ROW_OPEN);
		html.append("<td colspan=2>");
		html.append("File Properties");
		html.append(DATA_CLOSE);
		html.append(ROW_CLOSE);
		
		if (null != fileProperties) {
			for (final FileProperties fProp : fileProperties) {
				html.append(ROW_OPEN);
				html.append(DATA_OPEN);
				html.append(fProp.getKey());
				html.append(DATA_CLOSE);
				html.append(DATA_OPEN);
				html.append(fProp.getValue());
				html.append(DATA_CLOSE);
				html.append(ROW_CLOSE);
			}
		}
		html.append(TABLE_CLOSE);
		html.append(NEW_PARAGRAPH);
		html.append(NEW_PARAGRAPH);
		html.append(NEW_PARAGRAPH);
		return html.toString();
	}

}