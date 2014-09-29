package org.rage.manifest.formatter;

import org.rage.manifest.model.Manifest;


/**
 * Formatter represents the standard interface for formatting output
 * 
 * @author <a href="mailto:mfoley@24hourfit.com">mfoley</a>
 * @version $Id: Formatter.java 30559 2010-03-30 16:54:05Z mfoley $
 * @since Mar 26, 2010
 */
public interface Formatter
{



   /**
    * formats the Manifest into a String suitable for output
    * 
    * @param manifest
    * @return String representation of the Manifest
    * @since Mar 29, 2010
    */
   String format (Manifest manifest);


   /**
    * Adds any special header content needed for the output type. If none is needed this will return an empty string
    * 
    * @return standard header information needed for the formatter type
    * @since Mar 29, 2010
    */
   String startContent ();


   /**
    * Adds any special footer content needed for the output type. If none is needed this will return an empty string
    * 
    * @return standard footer information needed fo the formatter type
    * @since Mar 29, 2010
    */
   String endContent ();


   /**
    * formats a simple string into the correct output style
    * 
    * @param information
    * @return formatted string
    * @since Mar 29, 2010
    */
   String format (String information);
}

