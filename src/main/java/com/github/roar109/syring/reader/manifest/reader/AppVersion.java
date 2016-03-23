package com.github.roar109.syring.reader.manifest.reader;

import com.github.roar109.syring.reader.manifest.formatter.Formatter;
import com.github.roar109.syring.reader.manifest.formatter.impl.HtmlFormatter;
import com.github.roar109.syring.reader.manifest.formatter.impl.XmlFormatter;
import com.github.roar109.syring.reader.manifest.model.FileProperties;
import com.github.roar109.syring.reader.manifest.model.JNDI;
import com.github.roar109.syring.resolver.JNDIPropertyResolver;
import com.github.roar109.syring.resolver.PropertyFileResolver;
import com.github.roar109.syring.resolver.PropertyResolver;
import com.github.roar109.syring.resolver.factory.PropertyResolverFactory;

import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
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
	private static final transient Set<String> propertyList;
	private static final String SHOW_JAR_FILES_PARAM = "showJarFiles";
	private static final String SHOW_JNDI_PARAM = "showJndi";
	private static final String SHOW_FILE_PROPERTY_PARAM = "showFileProps";
	private static final String VIEW_XML_PARAM = "viewXml";
	private static final String PATH_SEPARATOR = System.getProperty("file.separator");

	static {
		// static block initializing the list of items to be displayed from the
		// manifest file
		propertyList = new HashSet<String>();
		propertyList.add(Attributes.Name.IMPLEMENTATION_VERSION.toString());
		propertyList.add(Attributes.Name.IMPLEMENTATION_VENDOR.toString());
		propertyList.add(Attributes.Name.IMPLEMENTATION_TITLE.toString());
		propertyList.add("Build-Jdk");
		propertyList.add("Build-Operating-System");
		propertyList.add("Built-By");
		propertyList.add("Build-Directory");
		propertyList.add("Build-Time");
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
		PrintWriter out = null;
		final Formatter formatter = getFormatter(request, response);
		
		try {
			out = response.getWriter();
			out.write(formatter.startContent());

			final String basePath = request.getSession().getServletContext().getRealPath(PATH_SEPARATOR);
			final String warManifest = basePath + PATH_SEPARATOR + "META-INF" + PATH_SEPARATOR + "MANIFEST.MF";
			final InputStream warManifestStream = new FileInputStream(new File(warManifest));
			final Manifest manifest = new Manifest(warManifestStream);

			printManifestInfo(manifest, "WAR manifest", out, formatter);

			showJndiInfo(request, out, formatter);
			showFilePropertiesInfo(request, out, formatter);
			
			final String showJarFiles = request.getParameter(SHOW_JAR_FILES_PARAM);
			if ((null != showJarFiles) && "y".equalsIgnoreCase(showJarFiles)) {
				showJarFileManifestInfo(basePath, out, formatter);
			} else {
				out.print(formatter
						.format("To view the manifest information for the jar files add the following parameter to the URL: "
								+ SHOW_JAR_FILES_PARAM + "=Y"));
			}

			out.write(formatter.endContent());
		} catch (final Exception e) {
			System.err.println(e.getMessage());
			e.printStackTrace();
			out.print(formatter.format(
					"There was an error getting the manifest information.  Please review the logs for details."));
		} finally {
			close(out);
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
		final File libFile = new File(baseDir + PATH_SEPARATOR + "WEB-INF" + PATH_SEPARATOR + "lib");
		
		if (libFile != null && libFile.isDirectory()) {
			for (final File file : libFile.listFiles()) {
				try(final JarFile jarFile = new JarFile(file)){
					printManifestInfo(jarFile.getManifest(), jarFile.getName(), printWriter, formatter);	
				}
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
		final com.github.roar109.syring.reader.manifest.model.Manifest customManifest = new com.github.roar109.syring.reader.manifest.model.Manifest();
		customManifest.setName(displayName);
		final Map<String, String> manifestEntries = new HashMap<String, String>();
		customManifest.setEntries(manifestEntries);
		
		if (null != manifest) {
			final Attributes attributes = manifest.getMainAttributes();
			for (final String item : propertyList) {
				manifestEntries.put(item, attributes.getValue(item));
			}
		} else {
			manifestEntries.put("", "There is no manifest for this artifact");
		}
		printWriter.print(formatter.format(customManifest));
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
		final String showXml = request.getParameter(VIEW_XML_PARAM);
		Formatter formatter = null;
		
		if ((null != showXml) && showXml.equalsIgnoreCase("y")) {
			formatter = new XmlFormatter();
		} else {
			formatter = new HtmlFormatter();
			response.setContentType("text/html");
		}
		return formatter;
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
			propertyList.add(manifestItem);
		}
	}

	private void showJndiInfo(final HttpServletRequest request, final PrintWriter printWriter, final Formatter formatter) {
		final String showJndiParam = request.getParameter(SHOW_JNDI_PARAM);
		if ((null != showJndiParam) && "y".equalsIgnoreCase(showJndiParam)) {
			final List<JNDI> jndis = new ArrayList<JNDI>();
			
			final PropertyResolver propertyResolver = PropertyResolverFactory.instance().instanciateResolver(JNDIPropertyResolver.class);
			if(propertyResolver != null && propertyResolver instanceof JNDIPropertyResolver){
				final JNDIPropertyResolver jndiPropRsolver = (JNDIPropertyResolver)propertyResolver;
				
				for(final String key : jndiPropRsolver.getJndiValuesCache().keySet()){
					jndis.add(new JNDI(key, jndiPropRsolver.getJndiValuesCache().get(key)));
				}
			}
			printWriter.print(formatter.formatJndi(jndis));
		} else {
			printWriter.print(formatter.format("To view the JNDI information add the following parameter to the URL: "
					+ SHOW_JNDI_PARAM + "=Y"));
		}
	}

	private void showFilePropertiesInfo(final HttpServletRequest request, final PrintWriter printWriter, final Formatter formatter) {
		final String showFileProps = request.getParameter(SHOW_FILE_PROPERTY_PARAM);
		if ((null != showFileProps) && "y".equalsIgnoreCase(showFileProps)) {
			final List<FileProperties> fileProperties = new ArrayList<>();
			
			final PropertyResolver propertyResolver = PropertyResolverFactory.instance().instanciateResolver(PropertyFileResolver.class);
			if(propertyResolver != null && propertyResolver instanceof PropertyFileResolver){
				final PropertyFileResolver filePropertyResolver = (PropertyFileResolver)propertyResolver;
				
				for(final String key : filePropertyResolver.getPropertiesMap().keySet()){
					fileProperties.add(new FileProperties(key, filePropertyResolver.getPropertiesMap().get(key)));
				}
			}
			printWriter.print(formatter.formatFileProperties(fileProperties));
		} else {
			printWriter.print(formatter
					.format("To view the file properties available add the following parameter to the URL: "
							+ SHOW_FILE_PROPERTY_PARAM + "=Y"));
		}
	}
}
