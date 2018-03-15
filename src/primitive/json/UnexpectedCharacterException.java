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

/*******************************************************************************
 * An exception thrown when parser encounters malformed JSON syntax.
 * @author lukasz.bownik@gmail.com
 ******************************************************************************/
public final class UnexpectedCharacterException extends IOException {

   /****************************************************************************
    * 
    * @param position posision of unexpected character.
    * @param character unexpected character value (zero based).
    ***************************************************************************/
   UnexpectedCharacterException(final int position, final char character) {

      super("Unexpected character '" + character + "' at position " + position + ".");
      this.position = position;
      this.character = character;
   }
   /****************************************************************************
    * 
    ***************************************************************************/
   /** Unexpected character value.*/
   public final char character;
   /** Position of unexpected character (zero based).*/
   public final int position;
}