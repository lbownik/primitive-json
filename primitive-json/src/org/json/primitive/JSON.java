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
    * Parse JSON object (invokes new Parser(16,10,10).parse(reader)).
    * @param reader a reader object.
    * @return java.util.Hashtable if the reader contained JSON object or 
    *    java.util.Vector if the reader contained JSON array.
    * @throws IOException if input error occurs.
    * @throws org.json.primitive.UnexpectedCharacterException if malformed JSON is
    * encountered.
    * @throws org.json.primitive.DuplicatedKeyException if JSON object with two 
    * same kays is encountered
    * @throws NullPointerException if reader is null.
    ***************************************************************************/
   public static Object parse(final Reader reader) throws IOException {

      return new Parser(16, 10, 10).parse(reader);
   }

   /****************************************************************************
    * Parse JSON object (invokes new Parser(16,10,10).parse(s)).
    * @param s a JSON string.
    * @return java.util.Hashtable if the string containes JSON object or 
    *    java.util.Vector if the string containes JSON array.
    * @throws IOException if input error occurs.
    * @throws org.json.primitive.UnexpectedCharacterException if malformed JSON is
    * encountered.
    * @throws org.json.primitive.DuplicatedKeyException if JSON object with two 
    * same kays is encountered
    * @throws NullPointerException if reader is null.
    ***************************************************************************/
   public static Object parse(final String s) throws IOException {

      return new Parser(16, 10, 10).parse(s);
   }

   /****************************************************************************
    * Encode an object into JSON text and write it to out.
    * 
    * @param value JSON object.
    * @param out writer object.
    * @throws IOException if woutput error occurs.
    * @throws NullPointerException if value or out == null.
    * @throws IllegalArgumentException if value is not Hashtable or Vector.
    ***************************************************************************/
   public static void write(final Object value, final Writer out)
           throws IOException {

      if (value == null) {
         throw new NullPointerException("value");
      }

      if (value instanceof Hashtable) {
         write((Hashtable) value, out);
         return;
      }
      if (value instanceof Vector) {
         write((Vector) value, out);
         return;
      }
      throw new IllegalArgumentException("Only Hashtable or Vector accepted");
   }

   /****************************************************************************
    * Encode an object into JSON text.
    * 
    * @param value JSON object.
    * @return JSON string.
    * @throws NullPointerException if value == null.
    * @throws IllegalArgumentException if value is not Hashtable or Vector.
    ***************************************************************************/
   public static String toString(final Object value) {

      try {
         final Writer wr = new StringWriter(16);
         write(value, wr);
         return wr.toString();
      } catch (final IOException e) {
         throw new RuntimeException(e.getMessage()); // never happens
      }
   }

   /****************************************************************************
    * Encode an object into JSON text and return as array of bytes.
    * 
    * @param value JSON object.
    * @param charsetName character set name (eg. "UTF-8").
    * @return array of bytes containing JSON text.
    * @throws NullPointerException if value or charsetName == null.
    * @throws IllegalArgumentException if value is not Hashtable or Vector.
    * @throws RuntimeException if charsetName does not identify character set.
    ***************************************************************************/
   public static byte[] toByteArray(final Object value, final String charsetName) {
      try {
         final ByteArrayOutputStream out = new ByteArrayOutputStream(1000);
         write(value, new OutputStreamWriter(out, charsetName));
         return out.toByteArray();
      } catch (final IOException e) {
         throw new RuntimeException(e.getMessage()); // never happens
      }
   }

   /****************************************************************************
    * Encode an Vector into JSON text and write it to out.
    * 
    * @param value vector.
    * @param out writer object.
    * @throws IOException if woutput error occurs.
    * @throws NullPointerException if value or out == null.
    ***************************************************************************/
   public static void write(final Vector value, final Writer out)
           throws IOException {

      if (value == null) {
         throw new NullPointerException("value");
      }
      out.write('[');
      final int lastIndex = value.size() - 1;
      if (lastIndex > -1) {
         for (int i = 0; i < lastIndex; ++i) {
            writeValue(value.elementAt(i), out);
            out.write(',');
         }
         writeValue(value.elementAt(lastIndex), out);
      }
      out.write(']');
   }

   /****************************************************************************
    * Encode an Vector into JSON text.
    * 
    * @param value vector.
    * @throws NullPointerException if value == null.
    ***************************************************************************/
   public static String toString(final Vector value) {

      try {
         final Writer wr = new StringWriter(16);
         write(value, wr);
         return wr.toString();
      } catch (final IOException e) {
         throw new RuntimeException(e.getMessage()); // never happens
      }
   }

   /****************************************************************************
    * Encode a Hashtable into JSON text and write it to out.
    * 
    * @param value hashtable.
    * @param out writer object.
    * @throws IOException if woutput error occurs.
    * @throws NullPointerException if value or out == null.
    ***************************************************************************/
   public static void write(final Hashtable value, final Writer out)
           throws IOException {

      if (value == null) {
         throw new NullPointerException("value");
      }
      out.write('{');
      final Enumeration kenum = value.keys();
      if (kenum.hasMoreElements()) {
         final Object key = kenum.nextElement();
         out.write('\"');
         writeEscaped(String.valueOf(key), out);
         out.write('\"');
         out.write(':');
         writeValue(value.get(key), out);
      }
      while (kenum.hasMoreElements()) {
         out.write(',');
         final Object key = kenum.nextElement();
         out.write('\"');
         writeEscaped(String.valueOf(key), out);
         out.write('\"');
         out.write(':');
         writeValue(value.get(key), out);
      }
      out.write('}');
   }

   /****************************************************************************
    * Encode a Hashtable into JSON text.
    * 
    * @param value hashtable.
    * @throws NullPointerException if value == null.
    ***************************************************************************/
   public static String toString(final Hashtable value) {

      try {
         final Writer wr = new StringWriter(16);
         write(value, wr);
         return wr.toString();
      } catch (final IOException e) {
         throw new RuntimeException(e.getMessage()); // never happens
      }
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
    * 
    ***************************************************************************/
//   private static void write(long value, final Writer out)
//           throws IOException {
//
//      final char[] buf = new char[20];
//      int bufIndex = 0;
//      final boolean negative = value < 0;
//      if (negative) {
//         out.write('-');
//         value *= -1;
//         ++bufIndex;
//      }
//      
//      
//         final long reminder = value % 10;
//         if (reminder > 0) {
//            value /= 10;
//         }
//      }
//   }
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
    * 
    ***************************************************************************/
   private JSON() {

   }
}
