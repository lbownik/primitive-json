//------------------------------------------------------------------------------
//Copyright 2014 Lukasz Bownik
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

/*******************************************************************************
 * A string writer (since CLDC 1.1 does not supply one).
 * @author lukasz.bownik@gmail.com
 ******************************************************************************/
public final class StringWriter extends Writer {

   /****************************************************************************
    * Constructs writer.
    * @param initialBufferSize initial interla buffer size.
    * @throws java.lang.IllegalArgumentException if initialBufferSize <= 0.
    ***************************************************************************/
   public StringWriter(final int initialBufferSize) {

      if (initialBufferSize <= 0) {
         throw new IllegalArgumentException("initialBufferSize <= 0");
      }
      this.buffer = new StringBuffer(initialBufferSize);
   }

   /****************************************************************************
    * @see java.io.Writer#write(char[], int, int) 
    ***************************************************************************/
   public void write(final char[] cbuf, final int off, final int len)
           throws IOException {

      this.buffer.append(cbuf, off, len);
   }

   /****************************************************************************
    * @see java.io.Writer#write(java.lang.String, int, int) 
    ***************************************************************************/
   public void write(final String str, final int off,
           final int len) throws IOException {

      this.buffer.append(str.substring(off, off + len));
   }

   /****************************************************************************
    * @see java.io.Writer#write(java.lang.String) 
    ***************************************************************************/
   public void write(final String str) throws IOException {

      this.buffer.append(str);
   }

   /****************************************************************************
    * @see java.io.Writer#write(int) 
    ***************************************************************************/
   public void write(final char c) throws IOException {

      this.buffer.append(c);
   }

   /****************************************************************************
    * No operation.
    ***************************************************************************/
   public void flush() throws IOException {

   }

   /****************************************************************************
    * No operation.
    ***************************************************************************/
   public void close() throws IOException {

   }

   /****************************************************************************
    * @return content as String.
    ***************************************************************************/
   public String toString() {

      return this.buffer.toString();
   }

   /****************************************************************************
    * Clears content.
    ***************************************************************************/
   public void clear() {

      this.buffer.setLength(0);
   }
   /****************************************************************************
    * 
    ***************************************************************************/
   private final StringBuffer buffer;
}
