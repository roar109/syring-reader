package com.github.roar109.syring.reader.manifest.reader;

import com.github.roar109.syring.reader.manifest.formatter.Formatter;
import com.github.roar109.syring.reader.manifest.formatter.impl.HtmlFormatter;
import com.github.roar109.syring.reader.manifest.formatter.impl.XmlFormatter;

import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.jar.Attributes;
import java.util.jar.JarFile;
import java.util.jar.Manifest;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * AppVersion
 *
 * @since Aug 14, 2015
 *
 */
public class AppVersion extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final transient Set<String> m_propertyList;
	private static final String SHOW_JAR_FILES_PARAM = "showJarFiles";
	private static final String SHOW_JNDI_PARAM = "showJndi";
	private static final String SHOW_FILE_PROPERTY_PARAM = "showFileProps";
	private static final String VIEW_XML_PARAM = "viewXml";
	private static final String PATH_SEPARATOR = System.getProperty("file.separator");

	static {
		// static block initializing the list of items to be displayed from the
		// manifest file
		m_propertyList = new HashSet<String>();
		m_propertyList.add(Attributes.Name.IMPLEMENTATION_VERSION.toString());
		m_propertyList.add(Attributes.Name.IMPLEMENTATION_VENDOR.toString());
		m_propertyList.add(Attributes.Name.IMPLEMENTATION_TITLE.toString());
		m_propertyList.add("Build-Jdk");
		m_propertyList.add("Build-Operating-System");
		m_propertyList.add("Built-By");
		m_propertyList.add("Build-Directory");
		m_propertyList.add("Build-Time");
	}

	/**
	 * @inheritDoc
	 * @see javax.servlet.http.HttpServlet#doGet(javax.servlet.http.HttpServletRequest,
	 *      javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected final void doGet(final HttpServletRequest request, final HttpServletResponse response) {
		execute(request, response);
	}

	/**
	 * @inheritDoc
	 * @see javax.servlet.http.HttpServlet#doPost(javax.servlet.http.HttpServletRequest,
	 *      javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected final void doPost(final HttpServletRequest request, final HttpServletResponse response) {
		execute(request, response);
	}

	/**
	 * main method for printing out the manifest information
	 *
	 * @param request
	 *            - servlet request
	 * @param response
	 *            - servlet response
	 */
	protected final void execute(final HttpServletRequest request, final HttpServletResponse response) {
		PrintWriter _out = null;
		final Formatter _formatter = getFormatter(request, response);
		try {
			_out = response.getWriter();
			_out.write(_formatter.startContent());

			final String _basePath = request.getSession().getServletContext().getRealPath(PATH_SEPARATOR);
			final String _warManifest = _basePath + "META-INF" + PATH_SEPARATOR + "MANIFEST.MF";
			// m_log.debug("URL to manifest is: " + _warManifest);
			final InputStream is = new FileInputStream(new File(_warManifest));
			final Manifest _manifest = new Manifest(is);
			// m_log.debug("mainfest " + _manifest);

			printManifestInfo(_manifest, "WAR manifest", _out, _formatter);

			final String showJarFiles = request.getParameter(SHOW_JAR_FILES_PARAM);
			if ((null != showJarFiles) && "y".equalsIgnoreCase(showJarFiles)) {
				showJarFileManifestInfo(_basePath, _out, _formatter);
			} else {
				_out.print(_formatter
						.format("To view the manifest information for the jar files add the following parameter to the URL: "
								+ SHOW_JAR_FILES_PARAM + "=Y"));
			}

			final String showJndiParam = request.getParameter(SHOW_JNDI_PARAM);
			if ((null != showJndiParam) && "y".equalsIgnoreCase(showJndiParam)) {
				// TODO Add jndi
			} else {
				_out.print(_formatter.format("To view the JNDI information add the following parameter to the URL: "
						+ SHOW_JNDI_PARAM + "=Y"));
			}

			final String showFileProps = request.getParameter(SHOW_FILE_PROPERTY_PARAM);
			if ((null != showFileProps) && "y".equalsIgnoreCase(showFileProps)) {
				// TODO Add file props
			} else {
				_out.print(_formatter
						.format("To view the file properties available add the following parameter to the URL: "
								+ SHOW_FILE_PROPERTY_PARAM + "=Y"));
			}

			_out.write(_formatter.endContent());
		} catch (final Exception e) {
			// m_log.error("Unable to get all manifest information", e);
			System.err.println(e.getMessage());
			e.printStackTrace();
			_out.print(_formatter.format(
					"There was an error getting the manifest information.  Please review the logs for details."));
		} finally {
			close(_out);
		}
	}

	/**
	 * Using the base directory this method will find and loop through all Jar's
	 * associated with the application and print out the manifest information
	 * for each one
	 *
	 * @param baseDir
	 *            - root directory of the web application
	 * @param printWriter
	 *            - output stream to print the manifest information out to
	 * @throws IOException
	 */
	private void showJarFileManifestInfo(final String baseDir, final PrintWriter printWriter, final Formatter formatter)
			throws IOException {
		final File _file = new File(baseDir + PATH_SEPARATOR + "WEB-INF" + PATH_SEPARATOR + "lib");
		if (_file.isDirectory()) {
			JarFile _jar;
			for (final File _jarFile : _file.listFiles()) {
				// adding no pmd here as moving the creation of the JarFile
				// object into its own method is fairly pointless
				// for this
				_jar = new JarFile(_jarFile); // NOPMD for creating items in a
				// loop
				printManifestInfo(_jar.getManifest(), _jar.getName(), printWriter, formatter);
			}
		}
	}

	/**
	 * Prints all manifest information to the given PrintWriter
	 *
	 * @param manifest
	 *            - manifest file to show the information from
	 * @param displayName
	 *            - Title to be shown for the manifest information
	 * @param printWriter
	 *            - outputstream of the information
	 * @param formatter
	 *            - used to format the output
	 */
	private void printManifestInfo(final Manifest manifest, final String displayName, final PrintWriter printWriter,
			final Formatter formatter) {
		final com.github.roar109.syring.reader.manifest.model.Manifest _customManifest = new com.github.roar109.syring.reader.manifest.model.Manifest();
		_customManifest.setName(displayName);
		final Map<String, String> _manifestEntries = new HashMap<String, String>();
		_customManifest.setEntries(_manifestEntries);
		if (null != manifest) {
			final Attributes _attributes = manifest.getMainAttributes();
			for (final String _item : m_propertyList) {
				_manifestEntries.put(_item, _attributes.getValue(_item));
			}
		} else {
			_manifestEntries.put("", "There is no manifest for this artifact");
		}
		printWriter.print(formatter.format(_customManifest));
	}

	/**
	 * determines which formatter to use and initializes / returns the correct
	 * one.
	 *
	 * The default formatter is HTML
	 *
	 * @param request
	 * @return output formatter. default is HTML
	 * @since Mar 29, 2010
	 */
	private Formatter getFormatter(final HttpServletRequest request, final HttpServletResponse response) {
		final String _showXml = request.getParameter(VIEW_XML_PARAM);
		Formatter _formatter = null;
		if ((null != _showXml) && _showXml.equalsIgnoreCase("y")) {
			_formatter = new XmlFormatter();
		} else {
			_formatter = new HtmlFormatter();
			response.setContentType("text/html");
		}
		return _formatter;
	}

	/**
	 * closes any Closeable item and eats any exceptions that might be thrown
	 * from the close call
	 *
	 * @param closeableStream
	 */
	private void close(final Closeable closeableStream) {
		try {
			if (null != closeableStream) {
				closeableStream.close();
			}
		} catch (final Exception e) {
			// dont care to report this issue
			/*
			 * if (m_log.isDebugEnabled()) { m_log.debug(
			 * "Got exception closing the stream...", e); }
			 */
		}
	}

	/**
	 * When extending this class use this method to add items to be searched for
	 * and printed from the manifest file. Some items already included are
	 * Build_Jdk, Created-by
	 *
	 * NOTE: Case is important
	 *
	 * @param manifestItem
	 *            - item to be added to the printout of manifest information
	 */
	protected final void addItemToShowFromManifest(final String manifestItem) {
		if ((null != manifestItem) && (manifestItem.length() > 0)) {
			m_propertyList.add(manifestItem);
		}
	}
}
