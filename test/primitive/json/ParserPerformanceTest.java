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
import org.junit.Ignore;

/*******************************************************************************
 * @author lukasz.bownik@gmail.com
 ******************************************************************************/
@Ignore
public class ParserPerformanceTest {

   
   /****************************************************************************
    * 
    ***************************************************************************/
   public static void main(String[] args) throws Exception {

      //test(defaultJson);
      test(emptyStringsJson);
   }
   
   /****************************************************************************
    * 
    ***************************************************************************/
   static void test(final String json) throws Exception {

      final long n = iterations;
      final Parser p = new Parser(15, 10, 10);
      final long begin = System.currentTimeMillis();
      for (int i = 0; i < n; ++i) {
         p.parse(json);
      }
      final long duration = System.currentTimeMillis() - begin;
      final long speed = 1000 * n / duration;
      System.out.println(speed);
   }
   /****************************************************************************
    * 
    ***************************************************************************/
   final static long iterations = 10000000;
   final static String defaultJson = "{"
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
   final static String emptyStringsJson = "[\"\", \"\", \"\", \"\", \"\", \"\","
           + " \"\", \"\", \"\", \"\", \"\","
           + " \"\", \"\", \"\"]";

}
