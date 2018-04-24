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
import java.util.List;
import java.util.Map;
import org.junit.Ignore;

/*******************************************************************************
 * @author lukasz.bownik@gmail.com
 ******************************************************************************/
@Ignore
public class Generator_PerformanceTest {

   /****************************************************************************
    * 
    ***************************************************************************/
   public static void main(String[] args) throws Exception {
      
      final long n =10000000;
      final Map object = createObject();
      final StringBuilder writer = new StringBuilder(example.length()+ 20);
      final Generator g = new Generator();
      g.write(object, writer);
      final long begin = System.currentTimeMillis();
      for (int i = 0; i < n; ++i) {
         g.write(object, writer);
         writer.delete(0, writer.length());
      }
      final long duration = System.currentTimeMillis() - begin;
      final long speed = 1000 * n / duration;
      System.out.println(speed);
   }

   /****************************************************************************
    * 
    ***************************************************************************/
   private static Map createObject() {
      
      final Map result = new HashMap();
      
      final Map o1 = new HashMap();
      o1.put("interval", new Long(5));
      o1.put("config", Boolean.TRUE);
      o1.put("treshold", "LOW");
      result.put("abcde", o1);
 
      final List v1 = new ArrayList();
      result.put("jhjhjhj", v1);
      final Map o2 = new HashMap();
      o2.put("module", "main");
      o2.put("type", "active");
      o2.put("version", "2.2");
      v1.add(o2);
      
      final List v2 = new ArrayList();
      result.put("array", v2);
      final Map o3 = new HashMap();
      o3.put("id", "kldsjfkldejgfklejgfklrejtljrktljlrk");
      o3.put("value", new Long(13));
      v2.add(o3);
      final Map o4 = new HashMap();
      o4.put("id", "lk;lk;lk;lklk;lk");
      o4.put("value", new Long(13));
      v2.add(o4);
      
      final List v3 = new ArrayList();
      result.put("alarms", v3);
      final Map o5 = new HashMap();
      o5.put("id", "kjkljkljklj");
      o5.put("state", "SET");
      o5.put("value", new Long(16));
      o5.put("message", "something");
      v3.add(o5);
      
      final List v4 = new ArrayList();
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
