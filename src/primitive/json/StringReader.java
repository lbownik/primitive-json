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
package primitive.json;

import java.io.IOException;
import java.io.Reader;

/*******************************************************************************
 * A character stream whose source is a string. Not suitable for general usage.
 * 
 * @author lukasz.bownik@gmail.com
 ******************************************************************************/
final class StringReader extends Reader {

   /****************************************************************************
    * 
    ***************************************************************************/
   StringReader(final String s) {

      this.s = s;
      this.length = s.length();
   }

   /****************************************************************************
    * 
    ***************************************************************************/
   public int read() throws IOException {

      return this.pos < this.length ? this.s.charAt(this.pos++) : -1;
   }

   /****************************************************************************
    * 
    ***************************************************************************/
   public int read(final char[] cbuf, int off, final int len)
           throws IOException {

      return -1; // not implemented
   }

   /****************************************************************************
    * 
    ***************************************************************************/
   public void close() throws IOException {
   }

   /****************************************************************************
    * 
    ***************************************************************************/
   private final String s;
   private int pos = 0;
   private final int length;
}
