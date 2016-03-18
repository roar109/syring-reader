package com.github.roar109.syring.reader.manifest.model;

import java.util.Map;

public class Manifest
{

   private String               m_name;
   private Map <String, String> m_entries;


   /**
    * @return the name
    */
   public String getName ()
   {
      return m_name;
   }


   /**
    * @param name the name to set
    */
   public void setName (final String name)
   {
      m_name = name;
   }


   /**
    * @return the entries
    */
   public Map <String, String> getEntries ()
   {
      return m_entries;
   }


   /**
    * @param entries the entries to set
    */
   public void setEntries (final Map <String, String> entries)
   {
      m_entries = entries;
   }
}