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

import java.util.ArrayList;
import java.util.HashMap;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import org.junit.Test;

/*******************************************************************************
 *
 * @author lukasz.bownik@gmail.com
 ******************************************************************************/
public class GeneratorGenerateUseCases extends GeneratorUseCasesBase {

   /****************************************************************************
    * 
    ***************************************************************************/
   @Test
   public void throwsNullPointer_ForNullArgument()
           throws Exception {

      try {
         new Generator().toString((HashMap) null);
         fail("NullPointertException failed.");
      } catch (final NullPointerException e) {
         assertTrue(true);
      }
      try {
         new Generator().toString((ArrayList) null);
         fail("NullPointertException failed.");
      } catch (final NullPointerException e) {
         assertTrue(true);
      }
      try {
         new Generator().toString((Object) null);
         fail("NullPointertException failed.");
      } catch (final NullPointerException e) {
         assertTrue(true);
      }
   }
   /****************************************************************************
    * 
    ***************************************************************************/
   @Test
   public void returnsProperValue_ForMap()
           throws Exception {

      assertMapEquals("{}", new HashMap());
      assertMapEquals("{\"a\":null}", asMap("a", null));
      assertMapEquals("{\"a\":true}", asMap("a", Boolean.TRUE));
      assertMapEquals("{\"a\":false}", asMap("a", Boolean.FALSE));
      assertMapEquals("{\"a\":\"b\"}", asMap("a", "b"));
      assertMapEquals("{\"a\":1}", asMap("a", 1));
      assertMapEquals("{\"a\":-123}", asMap("a", -123));
      assertMapEquals("{\"a\":0.0}", asMap("a", 0.0));
      assertMapEquals("{\"a\":1.0}", asMap("a", 1.0));
      assertMapEquals("{\"a\":-1.0}", asMap("a", -1.0));
      assertMapEquals("{\"a\":0}", asMap("a", 0));
      assertMapEquals("{\"a\":[]}", asMap("a", new ArrayList()));
      assertMapEquals("{\"a\":{}}", asMap("a", new HashMap()));
   }
}
