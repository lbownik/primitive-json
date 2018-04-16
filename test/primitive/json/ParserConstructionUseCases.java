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

import org.junit.Test;

/*******************************************************************************
 *
 * @author lukasz.bownik@gmail.com
 ******************************************************************************/
public class ParserConstructionUseCases extends ParserUseCasesBase {

   /****************************************************************************
    * 
    ***************************************************************************/
   @Test
   public void throwsException_ForNevativeArgumentValues()
           throws Exception {
      
      assertIllegalArgumentException(() -> new Parser(0, 10, 10));
      assertIllegalArgumentException(() -> new Parser(-1, 10, 10));
      
      assertIllegalArgumentException(() -> new Parser(10, 0, 10));
      assertIllegalArgumentException(() -> new Parser(10, -1, 10));
      
      assertIllegalArgumentException(() -> new Parser(10, 10, 0));
      assertIllegalArgumentException(() -> new Parser(10, 10, -1));
   }
   /****************************************************************************
    * 
    ***************************************************************************/
   @Test
   public void doesNotThrowException_ForProperInvocation()
           throws Exception {
      
      new Parser(10, 10, 10);
   }
}
