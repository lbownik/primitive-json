//------------------------------------------------------------------------------
//Copyright 2014 Lukasz Bownik, Yidong Fang, Chris Nokleberg, Dave Hughes
//
//Licensed under the Apache License, Version 2.0 (the "License");
//you may not use this file except in compliance with the License.
//You may obtain a copy of the License at
//
//    http://www.apache.org/licenses/LICENSE-2.0
//
//Unless required by applicable law or agreed to in writing, software
//distributed under the License is distributed on an "AS IS" BASIS,
//WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
//See the License for the specific language governing permissions and
//limitations under the License.
//------------------------------------------------------------------------------
package org.json.primitive;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

/*******************************************************************************
 * Static methods for parsing and generating JSON.
 * @author lukasz.bownik@gmail.com
 ******************************************************************************/
public class JSON {

   /****************************************************************************
    * Parse JSON text into java object from the input source
    * (invokes new Parser(16).parse(in)).
    * @see Parser
    * 
    * @param in reader
    * @return Instance of the following:
    * 	java.util.Hashtable,
    * 	java.util.Vector,
    * 	java.lang.String,
    * 	java.lang.Long,
    * 	java.lang.Double,   
    * 	java.lang.Boolean,
    * 	org.json.primitive.Null.value (caouse Hashtable does not support null values).
    * 
    * @throws IOException
    * @throws UnexpectedCharacterException
    ***************************************************************************/
   public static Object parse(final Reader in) throws IOException {

      return new Parser(16).parse(in);
   }

   /****************************************************************************
    * 
    ***************************************************************************/
   public static Object parse(final String s) throws IOException {

      return new Parser(16).parse(s);
   }
   /****************************************************************************
    * Encode an object into JSON text and write it to out.
    ***************************************************************************/
   public static void write(final Object value, final Writer out) 
           throws IOException {
      
      if (value instanceof Hashtable) {
         write((Hashtable) value, out);
         return;
      }
      if (value instanceof Vector) {
         write((Vector) value, out);
         return;
      }
      throw new IllegalArgumentException("Only HashTable or Vactor accepted");
   }

   /****************************************************************************
    * 
    ***************************************************************************/
   private static void writeValue(final Object value, final Writer out)
           throws IOException {

      if (value == null) {
         out.write("null");
         return;
      }
      if (value instanceof String) {
         out.write('\"');
         writeEscaped((String) value, out);
         out.write('\"');
         return;
      }
      if (value instanceof Double) {
         final Double d = (Double) value;
         if (d.isInfinite() | d.isNaN()) {
            out.write("null");
         } else {
            out.write(value.toString());
         }
         return;
      }
      if (value instanceof Float) {
         final Float f = (Float) value;
         if (f.isInfinite() | f.isNaN()) {
            out.write("null");
         } else {
            out.write(value.toString());
         }
         return;
      }
      if (value instanceof Hashtable) {
         write((Hashtable) value, out);
         return;
      }
      if (value instanceof Vector) {
         write((Vector) value, out);
         return;
      }
      out.write(value.toString());
   }

   /****************************************************************************
    * Convert an object to JSON text.
    ***************************************************************************/
   public static String toString(final Object value) {

      try {
         final Writer wr = new StringWriter(16);
         writeValue(value, wr);
         return wr.toString();
      } catch (final IOException e) {
         // never happens
         throw new RuntimeException(e.getMessage());
      }
   }

   /****************************************************************************
    *
    ***************************************************************************/
   public static byte[] toByteArray(final Object value, final String encoding)
           throws IOException {

      final ByteArrayOutputStream out = new ByteArrayOutputStream(1000);
      JSON.writeValue(value, new OutputStreamWriter(out, encoding));
      return out.toByteArray();
   }

   /****************************************************************************
    *
    ***************************************************************************/
   private static void writeEscaped(final String s, final Writer out)
           throws IOException {

      if (s != null) {
         for (int i = 0; i < s.length(); i++) {
            char ch = s.charAt(i);
            switch (ch) {
               case '"':
                  out.write("\\\"");
                  break;
               case '\\':
                  out.write("\\\\");
                  break;
               case '\b':
                  out.write("\\b");
                  break;
               case '\f':
                  out.write("\\f");
                  break;
               case '\n':
                  out.write("\\n");
                  break;
               case '\r':
                  out.write("\\r");
                  break;
               case '\t':
                  out.write("\\t");
                  break;
               case '/':
                  out.write("\\/");
                  break;
               default:
                  //Reference: http://www.unicode.org/versions/Unicode5.1.0/
                  if ((ch >= '\u0000' && ch <= '\u001F')
                          || (ch >= '\u007F' && ch <= '\u009F')
                          || (ch >= '\u2000' && ch <= '\u20FF')) {
                     final String ss = Integer.toHexString(ch);
                     out.write("\\u");
                     for (int k = 0; k < 4 - ss.length(); k++) {
                        out.write('0');
                     }
                     out.write(ss.toUpperCase());
                  } else {
                     out.write(ch);
                  }
            }
         }//for
      } else {
         out.write("null");
      }
   }

   /****************************************************************************
    * Encode a list into JSON text and write it to out. 
    ***************************************************************************/
   public static void write(final Vector v, final Writer out)
           throws IOException {

      if (v != null) {
         out.write('[');
         final int lastIndex = v.size() - 1;
         if (lastIndex > -1) {
            for (int i = 0; i < lastIndex; ++i) {
               writeValue(v.elementAt(i), out);
               out.write(',');
            }
            writeValue(v.elementAt(lastIndex), out);
         }
         out.write(']');
      } else {
         out.write("null");
      }
   }

   /****************************************************************************
    * Convert a list to JSON text. The result is a JSON array. 
    ***************************************************************************/
   public static String toString(final Vector v) {

      try {
         final Writer wr = new StringWriter(16);
         write(v, wr);
         return wr.toString();
      } catch (final IOException e) {
         // never happens
         throw new RuntimeException(e.getMessage());
      }
   }

   /****************************************************************************
    * Encode a map into JSON text and write it to out.
    ***************************************************************************/
   public static void write(final Hashtable map, final Writer out)
           throws IOException {

      if (map != null) {
         out.write('{');
         final Enumeration kenum = map.keys();
         if (kenum.hasMoreElements()) {
            final Object key = kenum.nextElement();
            out.write('\"');
            writeEscaped(String.valueOf(key), out);
            out.write('\"');
            out.write(':');
            writeValue(map.get(key), out);
         }
         while (kenum.hasMoreElements()) {
            out.write(',');
            final Object key = kenum.nextElement();
            out.write('\"');
            writeEscaped(String.valueOf(key), out);
            out.write('\"');
            out.write(':');
            writeValue(map.get(key), out);
         }
         out.write('}');
      } else {
         out.write("null");
      }
   }

   /****************************************************************************
    * Convert a map to JSON text. The result is a JSON object. 
    ***************************************************************************/
   public static String toString(final Hashtable map) {

      try {
         final Writer wr = new StringWriter(16);
         write(map, wr);
         return wr.toString();
      } catch (final IOException e) {
         // never happens
         throw new RuntimeException(e.getMessage());
      }
   }

   /****************************************************************************
    * 
    ***************************************************************************/
}
