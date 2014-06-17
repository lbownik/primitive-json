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

import java.io.IOException;
import java.io.Writer;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

/*******************************************************************************
 * JSON generator. This class is not thread safe but can be reused for generating 
 * consecutive JSON messages one by one.
 * @author lukasz.bownik@gmail.com
 ******************************************************************************/
public final class Generator {

   /****************************************************************************
    * 
    ***************************************************************************/
   public Generator() {
      
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
   public void write(final Object value, final Writer out)
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
    * Encode an Vector into JSON text and write it to out.
    * 
    * @param value vector.
    * @param out writer object.
    * @throws IOException if woutput error occurs.
    * @throws NullPointerException if value or out == null.
    ***************************************************************************/
   public void write(final Vector value, final Writer out)
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
    * Encode a Hashtable into JSON text and write it to out.
    * 
    * @param value hashtable.
    * @param out writer object.
    * @throws IOException if woutput error occurs.
    * @throws NullPointerException if value or out == null.
    ***************************************************************************/
   public void write(final Hashtable value, final Writer out)
           throws IOException {

      if (value == null) {
         throw new NullPointerException("value");
      }
      out.write('{');
      final Enumeration kenum = value.keys();
      if (kenum.hasMoreElements()) {
         final Object key = kenum.nextElement();
         out.write('\"');
         writeEscaped((String)key, out);
         out.write('\"');
         out.write(':');
         writeValue(value.get(key), out);
      }
      while (kenum.hasMoreElements()) {
         out.write(',');
         final Object key = kenum.nextElement();
         out.write('\"');
         writeEscaped((String)key, out);
         out.write('\"');
         out.write(':');
         writeValue(value.get(key), out);
      }
      out.write('}');
   }

   /****************************************************************************
    * 
    ***************************************************************************/
   private void writeValue(final Object value, final Writer out)
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

}
