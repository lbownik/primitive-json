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

import java.io.EOFException;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/*******************************************************************************
 *
 * @author lukasz.bownik@gmail.com
 ******************************************************************************/
public abstract class Parser_UseCasesBase {
   /****************************************************************************
    * 
    ***************************************************************************/
   protected void assertUnexpected(final String str, final char unexpectedChar)
           throws IOException {

      try {
         Object result = parse(str);
         fail("Unexpected character failed. Result: ".concat(result.toString()));
      } catch (final Parser.UnexpectedCharacterException e) {
         assertEquals(unexpectedChar, e.character);
      }
   }
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
   protected void assertLongEquals(final long expected, final String str)
           throws Exception {

      assertEquals(new Long(expected), ((List) parse(str)).get(0));
   }
   /****************************************************************************
    * 
    ***************************************************************************/
   protected void assertDoubleEquals(final double expected, final String str)
           throws Exception {

      assertEquals(new Double(expected), ((List) parse(str)).get(0));
   }
   /****************************************************************************
    * 
    ***************************************************************************/
   protected void assertStringEquals(final String expected, final String str)
           throws Exception {

      assertEquals(expected, ((List) parse(str)).get(0));
   }
   /****************************************************************************
    * 
    ***************************************************************************/
   protected void assertListquals(final List expected, final String str)
           throws Exception {

      assertEquals(expected, parse(str));
   }
   /****************************************************************************
    * 
    ***************************************************************************/
   protected void assertMapEquals(final Map expected, final String str)
           throws Exception {

      assertEquals(expected, parse(str));
   }
   /****************************************************************************
    * 
    ***************************************************************************/
   protected Map asMap(final String key, final Object value)
           throws Exception {

      final HashMap result = new HashMap();
      result.put(key, value);
      return result;
   }
   /****************************************************************************
    * 
    ***************************************************************************/
   protected Map asMap(final String key1, final Object value1,
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
   protected void assertEmptyList(final String str)
           throws Exception {

      assertTrue(((List) parse(str)).isEmpty());
   }
   /****************************************************************************
    * 
    ***************************************************************************/
   protected void assertEmptyMap(final String str)
           throws Exception {

      assertTrue(((Map) parse(str)).isEmpty());
   }
   /****************************************************************************
    * 
    ***************************************************************************/
   protected void assertEOF(final String str) throws IOException {

      try {
         Object result = parse(str);
         fail("EOF failed. Result: ".concat(result.toString()));
      } catch (final EOFException e) {
         assertTrue(true);
      }
   }

   /****************************************************************************
    * 
    ***************************************************************************/
   protected Object parse(final String str) throws IOException {

      return new Parser().parse(str);
   }
}
