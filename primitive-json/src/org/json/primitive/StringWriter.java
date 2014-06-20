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
    * @param initialBufferSize initial internal buffer size.
    * @throws java.lang.IllegalArgumentException if initialBufferSize <= 0.
    ***************************************************************************/
   public StringWriter(final int initialBufferSize) {

      if (initialBufferSize <= 0) {
         throw new IllegalArgumentException("initialBufferSize <= 0");
      }
      this.buffer = new char[initialBufferSize];
      this.bufferSize = initialBufferSize;
   }

   /****************************************************************************
    * @see java.io.Writer#write(char[], int, int) 
    ***************************************************************************/
   public void write(final char[] cbuf, int off, final int length)
           throws IOException {

      if ((off < 0) | (off > cbuf.length) | (length < 0)
              | ((off + length) > cbuf.length) | ((off + length) < 0)) {
         throw new IndexOutOfBoundsException();
      } else if (length == 0) {
         return;
      }
      if ((this.size + length) >= this.bufferSize) {
         realloc(this.size + length);
      }
      final int siz = off + length;
      for (; off < siz; ++off) {
         this.buffer[this.size++] = cbuf[off];
      }
   }

   /****************************************************************************
    * @see java.io.Writer#write(java.lang.String, int, int) 
    ***************************************************************************/
   public void write(String str, int off, final int length) throws IOException {

      if (str == null) {
         str = "null";
      }
      if ((off < 0) | (off > str.length()) | (length < 0)
              | ((off + length) > str.length()) | ((off + length) < 0)) {
         throw new IndexOutOfBoundsException();
      } else if (length == 0) {
         return;
      }

      if ((this.size + length) >= this.bufferSize) {
         realloc(this.size + length);
      }
      final int siz = off + length;
      for (; off < siz; ++off) {
         this.buffer[this.size++] = str.charAt(off);
      }
   }

   /****************************************************************************
    * @see java.io.Writer#write(java.lang.String) 
    ***************************************************************************/
   public void write(String str) throws IOException {

      if (str == null) {
         str = "null";
      }
      final int length = str.length();
      if ((this.size + length) >= this.bufferSize) {
         realloc(this.size + length);
      }
      for (int i = 0; i < length; ++i) {
         this.buffer[this.size++] = str.charAt(i);
      }
   }

   /****************************************************************************
    * @see java.io.Writer#write(int) 
    ***************************************************************************/
   public void write(final int c) throws IOException {

      if (this.size == this.bufferSize) {
         realloc(this.bufferSize*2);
      }
      this.buffer[this.size++] = (char) c;
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

      return new String(this.buffer, 0, this.size);
   }

   /****************************************************************************
    * Clears content.
    ***************************************************************************/
   public void clear() {

      this.size = 0;
   }

   /****************************************************************************
    * @return number o characters in this writer.
    ***************************************************************************/
   public int getSize() {

      return this.size;
   }

   /****************************************************************************
    * 
    ***************************************************************************/
   private void realloc(final int newSize) {

      this.bufferSize = newSize;
      final char[] newBuffer = new char[this.bufferSize];
      System.arraycopy(this.buffer, 0, newBuffer, 0, this.buffer.length);
      this.buffer = newBuffer;
   }
   /****************************************************************************
    * 
    ***************************************************************************/
   private char[] buffer;
   private int size = 0;
   private int bufferSize;
}
