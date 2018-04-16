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

      System.out.println("PERFORMANCE TESTS");
      System.out.println("Number of iterations per test:" + iterations/1000000 + "M");
      System.out.println("-----------------------------------------------");
//      test("Mixed", defaultJson);
//      test("Array of \"\"", emptyStringsJson);
//      test("Array of strings", arrayOfStrings);
//      test("Array of numbers", arrayOfNumbers);
//      test("Array of nulls", arrayOfNumbers);
      test("Array of nulls", arrayOfNulls);
   }

   /****************************************************************************
    * 
    ***************************************************************************/
   static void test(final String name, final String json) throws Exception {

      final long n = iterations;
      final Parser p = new Parser(15, 10, 10);
      final long begin = System.currentTimeMillis();
      for (int i = 0; i < n; ++i) {
         p.parse(json);
      }
      final long duration = System.currentTimeMillis() - begin;
      final long speed = 1000 * n / duration/1000;
      System.out.print(name);
      System.out.print(" (iter./s): ");
      System.out.print(speed);
      System.out.println("K");
   }
   /****************************************************************************
    * 
    ***************************************************************************/
   final static long iterations = 100000000;
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
   final static String arrayOfStrings = "[\"abctą\", \"dfterf\\r\\n\", "
           + "\"tksmsd\t\", \"    \", \"12344556gf\", \"kfjgkfj\","
           + " \"\u12AA\u1234\", \"#$%$^%^%%#\", \"???????\", \"\\\\\\\\S\"]";
   final static String arrayOfNumbers = "[1234, 57563, 2324.45454, 10e-12, 34354e+10, 123243.54534]";
   final static String arrayOfNulls = "[null, null, null, null, null, null, null, null, null, null]";
}
