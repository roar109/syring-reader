package com.github.roar109.syring.reader.manifest.formatter.impl;

import java.text.MessageFormat;
import java.util.List;
import java.util.Map.Entry;

import com.github.roar109.syring.reader.manifest.formatter.Formatter;
import com.github.roar109.syring.reader.manifest.model.FileProperties;
import com.github.roar109.syring.reader.manifest.model.JNDI;
import com.github.roar109.syring.reader.manifest.model.Manifest;

public class XmlFormatter implements Formatter {

	/**
	 * Overrides endContent
	 * 
	 * closes the main XML tag
	 * 
	 * @return closing tag
	 * @since Mar 29, 2010
	 */
	public String endContent() {
		return "</VersionInfo>";
	}

	/**
	 * Overrides format
	 * 
	 * formats the Manifest in xml for simliar to the following
	 * 
	 * <pre>
	 * &lt;manifest name=&quot;{manifest Name}&quot;&gt;
	 * &lt;attribute name=&quot;{manifest entry name}&quot;&gt;{manifest entry value}&lt;/attribute&gt;
	 * &lt;/manifest&gt;
	 * </pre>
	 * 
	 * @param manifest
	 * @return String representation of the Manifest
	 * @since Mar 29, 2010
	 */
	public String format(final Manifest manifest) {
		final StringBuilder _xml = new StringBuilder();
		_xml.append("<manifest name=\"");
		_xml.append(manifest.getName());
		_xml.append("\">");
		for (final Entry<String, String> _entry : manifest.getEntries().entrySet()) {
			_xml.append(format(_entry.getKey(), _entry.getValue()));
		}
		_xml.append("</manifest>");
		return _xml.toString();
	}

	/**
	 * used to format the various entry's in the manifest
	 * 
	 * @param manifestName
	 * @param manifestValue
	 * @return xml representation of a single manifest entry
	 * @since Mar 29, 2010
	 */
	private String format(final String manifestName, final String manifestValue) {
		final StringBuilder _xml = new StringBuilder();
		_xml.append("<attribute name=\"");
		_xml.append(manifestName);
		_xml.append("\">");
		_xml.append(manifestValue);
		_xml.append("</attribute>");
		return _xml.toString();
	}

	private String format(final String elementName, final String manifestName, final String manifestValue) {
		final StringBuilder _xml = new StringBuilder();
		_xml.append("<" + elementName + " name=\"");
		_xml.append(manifestName);
		_xml.append("\">");
		_xml.append(manifestValue);
		_xml.append("</" + elementName + ">");
		return _xml.toString();
	}

	/**
	 * Overrides format
	 * 
	 * formats a single string into xml simliar to the following
	 * 
	 * <pre>
	 * &lt;information&gt;{string provided}&lt;/information&gt;
	 * </pre>
	 * 
	 * @param information
	 * @return xml representation of the string provided
	 * @since Mar 29, 2010
	 */
	public String format(final String information) {
		return MessageFormat.format("<information>{0}</information>", information);
	}

	/**
	 * Overrides startContent
	 * 
	 * opens the main XML tag
	 * 
	 * @return main xml tag
	 * @since Mar 29, 2010
	 */
	public String startContent() {
		return "<VersionInfo>";
	}

	@Override
	public String formatJndi(List<JNDI> jndis) {
		final StringBuilder _xml = new StringBuilder();
		_xml.append("<jndis>");
		for (final JNDI jndi : jndis) {
			_xml.append(format("jndi", jndi.getJndiName(), jndi.getJndiValue()));
		}
		_xml.append("</jndis>");
		return _xml.toString();
	}

	@Override
	public String formatFileProperties(List<FileProperties> fileProperties) {
		final StringBuilder _xml = new StringBuilder();
		_xml.append("<fileProperties>");
		for (final FileProperties fProp : fileProperties) {
			_xml.append(format("fileProperty", fProp.getKey(), fProp.getValue()));
		}
		_xml.append("</fileProperties>");
		return _xml.toString();
	}
}
