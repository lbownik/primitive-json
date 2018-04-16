package primitive.json;

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



import java.util.HashMap;
import java.util.ArrayList;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNull;

/*******************************************************************************
 * @author lukasz.bownik@gmail.com
 ******************************************************************************/
public class GeneratorTest {

   /****************************************************************************
    * 
    ***************************************************************************/
   @Test
   public void testToString() throws Exception {

      final Generator g = new Generator();
      ArrayList array;
      HashMap object;
      assertEquals("[]", g.toString(new ArrayList()));
      assertEquals("{}", g.toString(new HashMap()));

      array = new ArrayList();
      array.add("ala");
      array.add(new Long(12));
      object = new HashMap();
      object.put("b", new Double(1.0));
      array.add(object);

      assertEquals("[\"ala\",12,{\"b\":1.0}]", g.toString(array));

      array = new ArrayList();
      array.add("ala");
      array.add(new Long(12));
      object.put("c", array);
      String str = g.toString(object);
      assertTrue(str.equals("{\"b\":1.0,\"c\":[\"ala\",12]}")
              || str.equals("{\"c\":[\"ala\",12],\"b\":1.0}"));

      object = new HashMap();
      object.put("a", Boolean.TRUE);
      assertEquals("{\"a\":true}", g.toString(object));
      object = new HashMap();
      object.put("a", Boolean.TRUE);
      assertEquals("{\"a\":true}", g.toString(object));
      object = new HashMap();
      object.put("a", Boolean.FALSE);
      assertEquals("{\"a\":false}", g.toString(object));
      object = new HashMap();
      object.put("a", null);
      assertEquals("{\"a\":null}", g.toString(object));
      object.put("a", "\r\n\t\b");
      assertEquals("{\"a\":\"\\r\\n\\t\\b\"}", g.toString(object));

      array = new ArrayList();
      array.add(null);
      array.add(Boolean.TRUE);
      array.add(Boolean.FALSE);
      array.add(new Byte((byte) 1));
      array.add(new Short((short) 2));
      array.add(new Integer(3));
      array.add(new Long(4));
      array.add(new Float(1.0f));
      array.add(new Double(2.0));
      array.add(new StringBuilder("abc"));
      array.add(new ArrayList());
      array.add(new HashMap());
      assertEquals("[null,true,false,1,2,3,4,1.0,2.0,\"abc\",[],{}]", g.toString(array));

      final ArrayList result = (ArrayList) new Parser().parse(g.toString(array));
      assertEquals(12, result.size());
      assertNull(result.get(0));
      assertEquals(Boolean.TRUE, result.get(1));
      assertEquals(Boolean.FALSE, result.get(2));
      assertEquals(new Long(1), result.get(3));
      assertEquals(new Long(2), result.get(4));
      assertEquals(new Long(3), result.get(5));
      assertEquals(new Long(4), result.get(6));
      assertEquals(new Double(1.0), result.get(7));
      assertEquals(new Double(2.0), result.get(8));
      assertEquals("abc", result.get(9));
      assertEquals(new ArrayList(), result.get(10));
      assertEquals(new HashMap(1), result.get(11));
   }

}
