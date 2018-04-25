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
import static org.junit.Assert.assertEquals;
import org.junit.Test;
import static java.lang.Boolean.*;
import static java.util.Arrays.asList;
import java.util.Map;

/*******************************************************************************
 *
 * @author lukasz.bownik@gmail.com
 ******************************************************************************/
public class Generator_Generate_UseCases extends Generator_UseCasesBase {

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
   public void throwsIllegalArgument_ForIllegalArgument()
           throws Exception {

      try {
         new Generator().toString("abc");
         fail("AllegalArgumentException failed.");
      } catch (final IllegalArgumentException e) {
         assertEquals("Only Map or List accepted", e.getMessage());
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
      assertMapEquals("{\"a\":true}", asMap("a", TRUE));
      assertMapEquals("{\"a\":false}", asMap("a", FALSE));
      assertMapEquals("{\"a\":\"b\"}", asMap("a", "b"));
      assertMapEquals("{\"a\":1}", asMap("a", 1));
      assertMapEquals("{\"a\":-123}", asMap("a", -123));
      assertMapEquals("{\"a\":0.0}", asMap("a", 0.0));
      assertMapEquals("{\"a\":1.0}", asMap("a", 1.0));
      assertMapEquals("{\"a\":-1.0}", asMap("a", -1.0));
      assertMapEquals("{\"a\":-123.45600000000000}", asMap("a", -123.456));
      assertMapEquals("{\"a\":456.12345678901232}", asMap("a", 456.123456789012345)); //the last digit does not fit - ok
      assertMapEquals("{\"a\":0}", asMap("a", 0));
      assertMapEquals("{\"a\":[]}", asMap("a", new ArrayList()));
      assertMapEquals("{\"a\":{}}", asMap("a", new HashMap()));
      assertMapEquals("{\"a\":{\"b\":true}}", asMap("a", asMap("b", TRUE)));
      assertMapEquals("{\"a\":[null,\"c\"]}", asMap("a", asList(null, "c")));

      final String json = new Generator().toString(asMap("a", TRUE, "b", FALSE));
      assertTrue("{\"a\":true,\"b\":false}".equals(json)
              | "{\"b\":false,\"a\":true}".equals(json));
   }
   /****************************************************************************
    * 
    ***************************************************************************/
   @Test
   public void returnsProperValue_ForArray()
           throws Exception {

      assertListEquals("[]", new ArrayList());
      assertListEquals("[null]", asList((Object) null));
      assertListEquals("[null]", asList(Double.NaN));
      assertListEquals("[null]", asList(Double.POSITIVE_INFINITY));
      assertListEquals("[null]", asList(Float.NaN));
      assertListEquals("[null]", asList(Float.POSITIVE_INFINITY));
      assertListEquals("[true]", asList(TRUE));
      assertListEquals("[false]", asList(FALSE));
      assertListEquals("[\"b\"]", asList("b"));
      assertListEquals("[\"ab\\\"c\"]", asList("ab\"c"));
      assertListEquals("[\"\\\\\"]", asList("\\"));
      assertListEquals("[\"\\b\\f\\n\\r\\t\\/\"]", asList("\b\f\n\r\t/"));
      assertListEquals("[\"ąćśźżł\"]", asList("ąćśźżł"));
      assertListEquals("[\"\u0000\u0002\"]", asList("\u0000\u0002"));
      assertListEquals("[1]", asList(1));
      assertListEquals("[1]", asList((long)1));
      assertListEquals("[1]", asList((short)1));
      assertListEquals("[1]", asList((byte)1));
      assertListEquals("[-123]", asList(-123));
      assertListEquals("[0.0]", asList(0.0));
      assertListEquals("[1.0]", asList(1.0));
      assertListEquals("[-1.0]", asList(-1.0));
      assertListEquals("[-1.0]", asList((float)-1.0));
      assertListEquals("[0]", asList(0));
      assertListEquals("[[]]", asList(new ArrayList()));
      assertListEquals("[{}]", asList(new HashMap()));
      assertListEquals("[{\"b\":true}]", asList(asMap("b", TRUE)));
      assertListEquals("[[null,\"c\"]]", asList(asList(null, "c")));
      assertListEquals("[null,true,\"b\",1,0.0,[333,22],{\"b\":true}]",
              asList(null, TRUE, "b", 1, 0.0, asList(333, 22), asMap("b", TRUE)));
      assertEquals("[0]", new Generator().toString((Object)asList(0)));
      assertEquals("{\"b\":true}", new Generator().toString((Object)asMap("b", TRUE)));
   }
}
