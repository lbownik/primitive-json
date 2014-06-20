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
    * Create a generator
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

      final Class cls = value.getClass();
      if (cls == Hashtable.class) {
         write((Hashtable) value, out);
         return;
      }
      if (cls == Vector.class) {
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
   public String toString(final Object value) {

      try {
         final Writer wr = new StringWriter(32);
         write(value, wr);
         return wr.toString();
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
    * Encode an Vector into JSON text.
    * 
    * @param value vector.
    * @throws NullPointerException if value == null.
    ***************************************************************************/
   public String toString(final Vector value) {

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
         writeEscaped(key.toString(), out);
         out.write('\"');
         out.write(':');
         writeValue(value.get(key), out);
      }
      while (kenum.hasMoreElements()) {
         out.write(',');
         final Object key = kenum.nextElement();
         out.write('\"');
         writeEscaped(key.toString(), out);
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
   public String toString(final Hashtable value) {

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
   private void writeValue(final Object value, final Writer out)
           throws IOException {

      if (value == null) {
         out.write("null");
         return;
      }
      if (value == Null.value) {
         out.write("null");
         return;
      }
      final Class cls = value.getClass();
      if (cls == Double.class) {
         final Double d = (Double) value;
         if (d.isInfinite() | d.isNaN()) {
            out.write("null");
         } else {
            write(d.doubleValue(), out);
         }
         return;
      }
      if (cls == Float.class) {
         final Float f = (Float) value;
         if (f.isInfinite() | f.isNaN()) {
            out.write("null");
         } else {
            write(f.doubleValue(), out);
         }
         return;
      }
      if (cls == Byte.class) {
         write(((Byte) value).byteValue(), out);
         return;
      }
      if (cls == Short.class) {
         write(((Short) value).shortValue(), out);
         return;
      }
      if (cls == Integer.class) {
         write(((Integer) value).intValue(), out);
         return;
      }
      if (cls == Long.class) {
         write(((Long) value).longValue(), out);
         return;
      }
      if (cls == Boolean.class) {
         out.write(((Boolean) value).toString());
         return;
      }
      if (cls == Hashtable.class) {
         write((Hashtable) value, out);
         return;
      }
      if (cls == Vector.class) {
         write((Vector) value, out);
         return;
      }
      out.write('\"');
      writeEscaped(value.toString(), out);
      out.write('\"');
   }

   /****************************************************************************
    *
    ***************************************************************************/
   private static void writeEscaped(final String s, final Writer out)
           throws IOException {

      if (s == null) {
         out.write("null");
         return;
      }
      final int length = s.length();
      for (int i = 0; i < length; i++) {
         final char ch = s.charAt(i);
         switch (ch) {
            case '"':
               out.write('\\');
               out.write('"');
               break;
            case '\\':
               out.write('\\');
               out.write('\\');
               break;
            case '\b':
               out.write('\\');
               out.write('b');
               break;
            case '\f':
               out.write('\\');
               out.write('f');
               break;
            case '\n':
               out.write('\\');
               out.write('n');
               break;
            case '\r':
               out.write('\\');
               out.write('r');
               break;
            case '\t':
               out.write('\\');
               out.write('t');
               break;
            case '/':
               out.write('\\');
               out.write('/');
               break;
            default:
               //Reference: http://www.unicode.org/versions/Unicode5.1.0/
               if ((ch >= '\u0000' && ch <= '\u001F')
                       || (ch >= '\u007F' && ch <= '\u009F')
                       || (ch >= '\u2000' && ch <= '\u20FF')) {
                  final String ss = Integer.toHexString(ch);
                  out.write('\\');
                  out.write('u');
                  for (int k = 0; k < 4 - ss.length(); k++) {
                     out.write('0');
                  }
                  out.write(ss.toUpperCase());
               } else {
                  out.write(ch);
               }
         }
      }//for
   }

   /****************************************************************************
    *
    ***************************************************************************/
   private void write(long value, final Writer out)
           throws IOException {

      if (value == 0) {
         out.write('0');
         return;
      }
      if (value < 0) {
         out.write('-');
         value *= -1;
      }

      final int[] buf = this.buf;
      int index = 0;
      while (value > 0) {
         buf[index++] = '0' + (int) (value % 10);
         value /= 10;
      }
      while (index > 0) {
         out.write(buf[--index]);
      }
   }

   /****************************************************************************
    *
    ***************************************************************************/
   private void write(double value, final Writer out)
           throws IOException {

      write((long) value, out);
      if (value < 0) {
         value *= -1;
      }
      double decimal = value - (long) value;
      out.write('.');
      if (decimal == 0.0) {
         out.write('0');
         return;
      }
      for (int i = 0; i < 14 & decimal != 0.0; i++) {
         decimal *= 10.0;
         out.write('0' + (int) decimal);
         decimal -= (int) decimal;
      }
   }

   /****************************************************************************
    *
    ***************************************************************************/
   final int[] buf = new int[22];
}
