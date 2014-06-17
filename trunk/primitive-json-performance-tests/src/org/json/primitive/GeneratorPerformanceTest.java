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

import java.util.Hashtable;
import java.util.Vector;

/*******************************************************************************
 * @author lukasz.bownik@gmail.com
 ******************************************************************************/
public class GeneratorPerformanceTest {

   /****************************************************************************
    * 
    ***************************************************************************/
   public static void main(String[] args) throws Exception {
      
      final long n = 50000;
      final Hashtable object = createObject();
      
//      final Generator g = new Generator();
      final long begin = System.currentTimeMillis();
      for (int i = 0; i < n; ++i) {
         JSON.write(object, new StringWriter(example.length()));
      }
      final long duration = System.currentTimeMillis() - begin;
      final long speed = 1000 * n / duration;
      System.out.println(speed);
   }
   /****************************************************************************
    * 
    ***************************************************************************/
   private static Hashtable createObject() {
      
      final Hashtable result = new Hashtable();
      
      final Hashtable o1 = new Hashtable();
      o1.put("interval", new Long(5));
      o1.put("config", Boolean.TRUE);
      o1.put("treshold", "LOW");
      result.put("abcde", o1);
 
      final Vector v1 = new Vector();
      result.put("jhjhjhj", v1);
      final Hashtable o2 = new Hashtable();
      o2.put("module", "main");
      o2.put("type", "active");
      o2.put("version", "2.2");
      v1.add(o2);
      
      final Vector v2 = new Vector();
      result.put("array", v2);
      final Hashtable o3 = new Hashtable();
      o3.put("id", "kldsjfkldejgfklejgfklrejtljrktljlrk");
      o3.put("value", new Long(13));
      v2.add(o3);
      final Hashtable o4 = new Hashtable();
      o4.put("id", "lk;lk;lk;lklk;lk");
      o4.put("value", new Long(13));
      v2.add(o4);
      
      final Vector v3 = new Vector();
      result.put("alarms", v3);
      final Hashtable o5 = new Hashtable();
      o5.put("id", "kjkljkljklj");
      o5.put("state", "SET");
      o5.put("value", new Long(16));
      o5.put("message", "something");
      v3.add(o5);
      
      final Vector v4 = new Vector();
      result.put("array2", v4);
      v4.add(new Double(-12.0e10));
      v4.add(new Double(-120000000000.0)); 
      v4.add(new Double(123243324.43434)); 
      v4.add(new Integer(13));
      v4.add(new Double(-12.0e10));
      v4.add(new Double(-120000000000.0)); 
      v4.add(new Double(123243324.43434));
      v4.add(new Long(13));
      v4.add(new Double(-12.0e10)); 
      v4.add(new Double(-120000000000.0));
      v4.add(new Double(123243324.43434));
      v4.add(new Integer(13));
      v4.add(new Double(-12.0e10)); 
      v4.add(new Double(-120000000000.0));
      v4.add(new Double(123243324.43434));
      v4.add(new Long(13));
      
      return result;
   }
   /****************************************************************************
    * 
    ***************************************************************************/
   final static String example = "{"
           + "	\"abcde\":{"
           + "	\"interval\":5,"
           + "	\"config\":true,"
           + "	\"treshold\":\"LOW\"},"
           + "	\"jhjhjhj\":[{\"module\":\"main\",\"type\":\"ACTIVE\",\"version\":2.2}],"
           + "	\"array\":[{"
           + "	\"id\":\"kldsjfkldejgfklejgfklrejtljrktljlrk\",\"value\":13},{"
           + "	\"id\":\"lk;lk;lk;lklk;lk\",\"value\":13}],\"alarms\":[{"
           + "	\"id\":\"kjkljkljklj\",\"state\":\"SET\",\"level\":\"16\",\"message\":\"Overheat\"}],"
           + "	\"array2\": [-12.0e10, -120000000000.0, 123243324.43434, 13, -12.0e10, -120000000000.0,"
           + " 123243324.43434, 13,-12.0e10, -120000000000.0, 123243324.43434, 13,-12.0e10,"
           + " -120000000000.0, 123243324.43434, 13]}";

}
