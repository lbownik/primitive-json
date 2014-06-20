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

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.util.Hashtable;
import java.util.Vector;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;
import static junit.framework.Assert.fail;
import org.junit.Test;

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
      Vector array;
      Hashtable object;
      assertEquals("[]", g.toString(new Vector()));
      assertEquals("{}", g.toString(new Hashtable()));

      array = new Vector();
      array.addElement("ala");
      array.addElement(new Long(12));
      object = new Hashtable();
      object.put("b", new Double(1.0));
      array.addElement(object);

      assertEquals("[\"ala\",12,{\"b\":1.0}]", g.toString(array));

      array = new Vector();
      array.addElement("ala");
      array.addElement(new Long(12));
      object.put("c", array);
      String str = g.toString(object);
      assertTrue(str.equals("{\"b\":1.0,\"c\":[\"ala\",12]}")
              || str.equals("{\"c\":[\"ala\",12],\"b\":1.0}"));

      object = new Hashtable();
      object.put("a", Boolean.TRUE);
      assertEquals("{\"a\":true}", g.toString(object));
      object = new Hashtable();
      object.put("a", Boolean.TRUE);
      assertEquals("{\"a\":true}", g.toString(object));
      object = new Hashtable();
      object.put("a", Boolean.FALSE);
      assertEquals("{\"a\":false}", g.toString(object));
      object = new Hashtable();
      object.put("a", Null.value);
      assertEquals("{\"a\":null}", g.toString(object));
      object.put("a", "\r\n\t\b");
      assertEquals("{\"a\":\"\\r\\n\\t\\b\"}", g.toString(object));

      array = new Vector();
      array.addElement(null);
      array.addElement(Null.value);
      array.addElement(Boolean.TRUE);
      array.addElement(Boolean.FALSE);
      array.addElement(new Byte((byte) 1));
      array.addElement(new Short((short) 2));
      array.addElement(new Integer(3));
      array.addElement(new Long(4));
      array.addElement(new Float(1.0f));
      array.addElement(new Double(2.0));
      array.addElement(new StringBuilder("abc"));
      array.addElement(new Vector());
      array.addElement(new Hashtable());
      assertEquals("[null,null,true,false,1,2,3,4,1.0,2.0,\"abc\",[],{}]", g.toString(array));

      final Vector result = (Vector) new Parser().parse(g.toString(array));
      assertEquals(13, result.size());
      assertEquals(Null.value, result.elementAt(0));
      assertEquals(Null.value, result.elementAt(1));
      assertEquals(Boolean.TRUE, result.elementAt(2));
      assertEquals(Boolean.FALSE, result.elementAt(3));
      assertEquals(new Long(1), result.elementAt(4));
      assertEquals(new Long(2), result.elementAt(5));
      assertEquals(new Long(3), result.elementAt(6));
      assertEquals(new Long(4), result.elementAt(7));
      assertEquals(new Double(1.0), result.elementAt(8));
      assertEquals(new Double(2.0), result.elementAt(9));
      assertEquals("abc", result.elementAt(10));
      assertEquals(new Vector(), result.elementAt(11));
      assertEquals(new Hashtable(1), result.elementAt(12));
   }

}
