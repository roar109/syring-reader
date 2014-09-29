package org.rage.manifest.formatter.impl;

import java.util.Map.Entry;

import org.rage.manifest.formatter.Formatter;
import org.rage.manifest.model.Manifest;

public class HtmlFormatter implements Formatter
{
   private static final String TABLE_OPEN    = "<TABLE border=1>";
   private static final String TABLE_CLOSE   = "</table>";
   private static final String ROW_OPEN      = "<TR>";
   private static final String ROW_CLOSE     = "</TR>";
   private static final String DATA_OPEN     = "<TD>";
   private static final String DATA_CLOSE    = "</TD>";
   private static final String NEW_PARAGRAPH = "<P/>";


   /**
    * 
    * Overrides startContent
    * 
    * @return opening html and body tags
    * @since Mar 29, 2010
    * @see org.rage.manifest.Formatter#startContent()
    */
   public String startContent ()
   {
      final StringBuilder _html = new StringBuilder ();
      _html.append ("<html><body>");
      return _html.toString ();
   }


   /**
    * 
    * Overrides endContent
    * 
    * @return closing html and body tags
    * @since Mar 29, 2010
    * @see org.rage.manifest.Formatter#endContent()
    */
   public String endContent ()
   {
      return "</body></html>";
   }


   /**
    * Overrides format
    * 
    * Adds html paragraph tags around the string to ensure some white space around it in the html output
    * 
    * @param information
    * @return the same string that was sent in with some html paragraph tags around it
    * @since Mar 29, 2010
    * @see org.rage.manifest.Formatter#format(java.lang.String)
    */
   public String format (final String information)
   {
      final StringBuilder _html = new StringBuilder ();
      _html.append (NEW_PARAGRAPH);
      _html.append (NEW_PARAGRAPH);
      _html.append (information);
      _html.append (NEW_PARAGRAPH);
      _html.append (NEW_PARAGRAPH);
      return _html.toString ();
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
   public String format (final Manifest manifest)
   {
      final StringBuilder _html = new StringBuilder ();
      _html.append (TABLE_OPEN);
      _html.append (ROW_OPEN);
      _html.append ("<td colspan=2>");
      _html.append (manifest.getName ());
      _html.append (DATA_CLOSE);
      _html.append (ROW_CLOSE);
      if (null != manifest.getEntries ())
      {
         for (final Entry <String, String> _entry : manifest.getEntries ().entrySet ())
         {
            _html.append (ROW_OPEN);
            _html.append (DATA_OPEN);
            _html.append (_entry.getKey ());
            _html.append (DATA_CLOSE);
            _html.append (DATA_OPEN);
            _html.append (_entry.getValue ());
            _html.append (DATA_CLOSE);
            _html.append (ROW_CLOSE);
         }
      }
      _html.append (TABLE_CLOSE);
      _html.append (NEW_PARAGRAPH);
      _html.append (NEW_PARAGRAPH);
      _html.append (NEW_PARAGRAPH);
      return _html.toString ();
   }

}