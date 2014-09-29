package org.rage.manifest.formatter.impl;

import java.text.MessageFormat;
import java.util.Map.Entry;

import org.rage.manifest.formatter.Formatter;
import org.rage.manifest.model.Manifest;

public class XmlFormatter implements Formatter
{

   /**
    * Overrides endContent
    * 
    * closes the main XML tag
    * 
    * @return closing tag
    * @since Mar 29, 2010
    * @see org.rage.manifest.Formatter#endContent()
    */
   public String endContent ()
   {
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
    * @see org.rage.manifest.Formatter#format(com.fitness.util.Manifest)
    */
   public String format (final Manifest manifest)
   {
      final StringBuilder _xml = new StringBuilder ();
      _xml.append ("<manifest name=\"");
      _xml.append (manifest.getName ());
      _xml.append ("\">");
      for (final Entry <String, String> _entry : manifest.getEntries ().entrySet ())
      {
         _xml.append (format (_entry.getKey (), _entry.getValue ()));
      }
      _xml.append ("</manifest>");
      return _xml.toString ();
   }


   /**
    * used to format the various entry's in the manifest
    * 
    * @param manifestName
    * @param manifestValue
    * @return xml representation of a single manifest entry
    * @since Mar 29, 2010
    */
   private String format (final String manifestName, final String manifestValue)
   {
      final StringBuilder _xml = new StringBuilder ();
      _xml.append ("<attribute name=\"");
      _xml.append (manifestName);
      _xml.append ("\">");
      _xml.append (manifestValue);
      _xml.append ("</attribute>");
      return _xml.toString ();
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
    * @see org.rage.manifest.Formatter#format(java.lang.String)
    */
   public String format (final String information)
   {
      return MessageFormat.format ("<information>{0}</information>", information);
   }


   /**
    * Overrides startContent
    * 
    * opens the main XML tag
    * 
    * @return main xml tag
    * @since Mar 29, 2010
    * @see org.rage.manifest.Formatter#startContent()
    */
   public String startContent ()
   {
      return "<VersionInfo>";
   }
}
