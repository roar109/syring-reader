package com.github.roar109.syring.reader.manifest.formatter;


import java.util.List;

import com.github.roar109.syring.reader.manifest.model.FileProperties;
import com.github.roar109.syring.reader.manifest.model.JNDI;
import com.github.roar109.syring.reader.manifest.model.Manifest;


/**
 * Formatter represents the standard interface for formatting output
 *
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
   
   String formatJndi(List<JNDI> jndis);
   
   String formatFileProperties(List<FileProperties> fileProperties);


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
