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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/*******************************************************************************
 *
 * @author lukasz.bownik@gmail.com
 ******************************************************************************/
public abstract class Generator_UseCasesBase {

   /****************************************************************************
    * 
    ***************************************************************************/
   protected void assertIllegalArgumentException(final Runnable task)
           throws IOException {

      try {
         task.run();
         fail("IllegalArgumentException failed.");
      } catch (final IllegalArgumentException e) {
         assertTrue(true);
      }
   }
   /****************************************************************************
    * 
    ***************************************************************************/
   protected HashMap asMap(final String key, final Object value)
           throws Exception {

      final HashMap result = new HashMap();
      result.put(key, value);
      return result;
   }
   /****************************************************************************
    * 
    ***************************************************************************/
   protected HashMap asMap(final String key1, final Object value1,
           final String key2, final Object value2)
           throws Exception {

      final HashMap result = new HashMap();
      result.put(key1, value1);
      result.put(key2, value2);
      return result;
   }
   /****************************************************************************
    * 
    ***************************************************************************/
   protected void assertMapEquals(final String expected, final HashMap map)
           throws Exception {

      final Generator g = new Generator();
      assertEquals(expected, g.toString(map));
   }
   /****************************************************************************
    * 
    ***************************************************************************/
   protected void assertListEquals(final String expected, final List list)
           throws Exception {

      final Generator g = new Generator();
      assertEquals(expected, g.toString(list));
   }
}
