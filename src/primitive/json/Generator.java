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
package primitive.json;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/*******************************************************************************
 * JSON generator. This class is not thread safe but can be reused for generating 
 * consecutive JSON messages one by one.
 * This generator accepts object of type:
 *   <ul>
 *   <li>null,</li>
 *   <li>java.util.HashMap,</li>
 *   <li>java.util.ArrayList,</li>
 *   <li>java.lang.String,</li>
 *   <li>java.lang.Byte,</li>
 *   <li>java.lang.Short,</li>
 *   <li>java.lang.Integer,</li>
 *   <li>java.lang.Long,</li>
 *   <li>java.lang.Float,</li>
 *   <li>java.lang.Double,</li>
 *   <li>java.lang.Boolean,</li>
 *   <li>org.json.primitive.Null,</li>
 *   <li>objects of any other class (converted to string using toString method)</li>
 *   </ul>
 * @see https://github.com/lbownik/primitive-json
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
    * @param out appendable object.
    * @throws IOException if woutput error occurs.
    * @throws NullPointerException if value or out == null.
    * @throws IllegalArgumentException if value is not Map or List.
    ***************************************************************************/
   public void write(final Object value, final Appendable out)
           throws IOException {

      if (value == null) {
         throw new NullPointerException("value");
      }

      if (Map.class.isInstance(value)) {
         write((Map) value, out);
         return;
      }
      if (List.class.isInstance(value)) {
         write((List) value, out);
         return;
      }
      throw new IllegalArgumentException("Only Map or List accepted");
   }

   /****************************************************************************
    * Encode an object into JSON text.
    * 
    * @param value JSON object.
    * @return JSON string.
    * @throws NullPointerException if value == null.
    * @throws IllegalArgumentException if value is not Hashtable or ArrayList.
    ***************************************************************************/
   public String toString(final Object value) {

      try {
         final Appendable wr = new StringBuilder(32);
         write(value, wr);
         return wr.toString();
      } catch (final IOException e) {
         throw new RuntimeException(e.getMessage()); // never happens
      }
   }

   /****************************************************************************
    * Encode an ArrayList into JSON text and write it to out.
    * 
    * @param value list.
    * @param out appendable object.
    * @throws IOException if output error occurs.
    * @throws NullPointerException if value or out == null.
    ***************************************************************************/
   public void write(final List<?> value, final Appendable out)
           throws IOException {

      final int lastIndex = value.size() - 1;
      
      out.append('[');
      if (lastIndex > -1) {
         for (int i = 0; i < lastIndex; ++i) {
            writeValue(value.get(i), out);
            out.append(',');
         }
         writeValue(value.get(lastIndex), out);
      }
      out.append(']');
   }

   /****************************************************************************
    * Encode an List into JSON text.
    * 
    * @param value list.
    * @return JSON string.
    * @throws NullPointerException if value == null.
    ***************************************************************************/
   public String toString(final List<?> value) {

      try {
         final Appendable wr = new StringBuilder(16);
         write(value, wr);
         return wr.toString();
      } catch (final IOException e) {
         throw new RuntimeException(e.getMessage()); // never happens
      }
   }

   /****************************************************************************
    * Encode a HashMap into JSON text and write it to out.
    * 
    * @param value hashtable.
    * @param out appendable object.
    * @throws IOException if woutput error occurs.
    * @throws NullPointerException if value or out == null.
    ***************************************************************************/
   public void write(final Map value, final Appendable out)
           throws IOException {

      final Iterator<Map.Entry> entries = value.entrySet().iterator();
      
      out.append('{');
      if (entries.hasNext()) {
         final Map.Entry entry = entries.next();
         out.append('\"');
         writeEscaped(entry.getKey().toString(), out);
         out.append('\"');
         out.append(':');
         writeValue(entry.getValue(), out);
      }
      while (entries.hasNext()) {
         out.append(',');
         final Map.Entry entry = entries.next();
         out.append('\"');
         writeEscaped(entry.getKey().toString(), out);
         out.append('\"');
         out.append(':');
         writeValue(entry.getValue(), out);
      }
      out.append('}');
   }

   /****************************************************************************
    * Encode a HashMap into JSON text.
    * 
    * @param value map.
    * @return JSON string.
    * @throws NullPointerException if value == null.
    ***************************************************************************/
   public String toString(final Map<?,?> value) {

      try {
         final Appendable wr = new StringBuilder(16);
         write(value, wr);
         return wr.toString();
      } catch (final IOException e) {
         throw new RuntimeException(e.getMessage()); // never happens
      }
   }

   /****************************************************************************
    * 
    ***************************************************************************/
   private void writeValue(final Object value, final Appendable out)
           throws IOException {

      if (value == null) {
         out.append("null");
         return;
      }
      final Class<?> cls = value.getClass();
      if (cls == Double.class) {
         final Double d = (Double) value;
         if (d.isInfinite() | d.isNaN()) {
            out.append("null");
         } else {
            write(d.doubleValue(), out);
         }
         return;
      }
      if (cls == Float.class) {
         final Float f = (Float) value;
         if (f.isInfinite() | f.isNaN()) {
            out.append("null");
         } else {
            write(f.doubleValue(), out);
         }
         return;
      }
      if (Number.class.isInstance(value)) {
         write(((Number) value).longValue(), out);
         return;
      }
      if (cls == Boolean.class) {
         out.append(value.toString());
         return;
      }
      if (Map.class.isInstance(value)) {
         write((Map) value, out);
         return;
      }
      if (List.class.isInstance(value)) {
         write((List) value, out);
         return;
      }
      out.append('\"');
      writeEscaped(value.toString(), out);
      out.append('\"');
   }

   /****************************************************************************
    *
    ***************************************************************************/
   private static void writeEscaped(final String s, final Appendable out)
           throws IOException {

      final int length = s.length();
      for (int i = 0; i < length; i++) {
         final char ch = s.charAt(i);
         switch (ch) {
            case '"':
               out.append('\\');
               out.append('"');
               break;
            case '\\':
               out.append('\\');
               out.append('\\');
               break;
            case '\b':
               out.append('\\');
               out.append('b');
               break;
            case '\f':
               out.append('\\');
               out.append('f');
               break;
            case '\n':
               out.append('\\');
               out.append('n');
               break;
            case '\r':
               out.append('\\');
               out.append('r');
               break;
            case '\t':
               out.append('\\');
               out.append('t');
               break;
            case '/':
               out.append('\\');
               out.append('/');
               break;
            default:
               out.append(ch);
         }
      }//for
   }

   /****************************************************************************
    *
    ***************************************************************************/
   private void write(long value, final Appendable out)
           throws IOException {

      if (value == 0) {
         out.append('0');
         return;
      }
      if (value < 0) {
         out.append('-');
         value *= -1;
      }

      final int[] buf = this.buf;
      int index = 0;
      while (value > 0) {
         buf[index++] = '0' + (int) (value % 10);
         value /= 10;
      }
      while (index > 0) {
         out.append((char) buf[--index]);
      }
   }

   /****************************************************************************
    *
    ***************************************************************************/
   private void write(double value, final Appendable out)
           throws IOException {

      write((long) value, out);
      if (value < 0) {
         value *= -1;
      }
      double decimal = value - (long) value;
      out.append('.');
      if (decimal == 0.0) {
         out.append('0');
         return;
      }
      for (int i = 0; i < 14; i++) {
         decimal *= 10.0;
         out.append((char) ('0' + (int) decimal));
         decimal -= (int) decimal;
      }
   }

   /****************************************************************************
    *
    ***************************************************************************/
   final int[] buf = new int[22];
}
