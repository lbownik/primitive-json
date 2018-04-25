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

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

/*******************************************************************************
 *
 * @author lukasz.bownik@gmail.com
 ******************************************************************************/
public class Generator_Reuse_UseCases extends Generator_UseCasesBase{

   /****************************************************************************
    * 
    ***************************************************************************/
   @Test
   public void worksProperly_WhenReused() throws Exception {

      final Generator g = new Generator();

      assertEquals("[\"a\",true,null]", g.toString(asList("a", Boolean.TRUE, null)));
      assertEquals("{\"b\":false}", g.toString(asMap("b", Boolean.FALSE)));
      assertEquals("[\"a\",true,null]", g.toString(asList("a", Boolean.TRUE, null)));
      assertEquals("{\"b\":false}", g.toString(asMap("b", Boolean.FALSE)));
   }
}
