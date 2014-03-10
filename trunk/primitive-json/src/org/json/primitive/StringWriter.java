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
 * @author lukasz.bownik@gmail.com
 ******************************************************************************/
public final class StringWriter extends Writer {

   /****************************************************************************
    * 
    ***************************************************************************/
   public StringWriter(final int initialBufferSize) {
      
      this.buffer = new StringBuffer(initialBufferSize);
   }
   /****************************************************************************
    * 
    ***************************************************************************/
   public void write(char[] cbuf, int off, int len) throws IOException {

      this.buffer.append(cbuf, off, len);
   }
   /****************************************************************************
    * 
    ***************************************************************************/
   public void write(String str, int off, int len) throws IOException {
      
      this.buffer.append(str.substring(off, off + len));
   }
   /****************************************************************************
    * 
    ***************************************************************************/
   public void write(final String str) throws IOException {
      
      this.buffer.append(str);
   }
   /****************************************************************************
    * 
    ***************************************************************************/
   public void write(final char c) throws IOException {
      
      this.buffer.append(c);
   }
   

   /****************************************************************************
    * 
    ***************************************************************************/
   public void flush() throws IOException {

   }

   /****************************************************************************
    * 
    ***************************************************************************/
   public void close() throws IOException {
      
   }
   /****************************************************************************
    * 
    ***************************************************************************/
   public String toString() {
      
      return this.buffer.toString();
   }
   /****************************************************************************
    * 
    ***************************************************************************/
   private final StringBuffer buffer;
}
