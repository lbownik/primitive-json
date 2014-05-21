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

import java.io.EOFException;
import java.io.IOException;
import java.io.Reader;
import java.util.Hashtable;
import java.util.Vector;
import junit.framework.TestCase;

/*******************************************************************************
 * @author lukasz.bownik@gmail.com
 ******************************************************************************/
@SuppressWarnings("rawtypes")
public class ParserTest extends TestCase {

   /****************************************************************************
    * 
    ***************************************************************************/
   public void testProperPrimitiveValues() throws Exception {

      Vector v;
      //null
      v = (Vector) parse("[null]");
      assertTrue(v.elementAt(0) == Null.value);
      //true
      v = (Vector) parse("[true]");
      assertTrue(Boolean.TRUE == v.elementAt(0));
      // false
      v = (Vector) parse("[false]");
      assertTrue(Boolean.FALSE == v.elementAt(0));
      //string
      v = (Vector) parse("[\"\"]");
      assertEquals("", v.elementAt(0));
      v = (Vector) parse("[\" \\tt\\rr\\nn\\ff\"]");
      assertEquals(" \tt\rr\nn\ff", v.elementAt(0));
      v = (Vector) parse("[\"\\\"\\\"\"]");
      assertEquals("\"\"", v.elementAt(0));
      v = (Vector) parse("[\"ał \\t$-_\"]");
      assertEquals("ał \t$-_", v.elementAt(0));
      v = (Vector) parse("[\"a\\u0041b\\u0042\\u005A\"]");
      assertEquals("aAbBZ", v.elementAt(0));
      v = (Vector) parse("[\"{\\\"aaa\\\": true, \\\"b\\\" : [null, null]}\"]");
      assertEquals("{\"aaa\": true, \"b\" : [null, null]}", v.elementAt(0));
      v = (Vector) parse(
              "[\"http://feedburner.google.com/fb/a/mailverify?uri=JavaCodeGeeks&loc=en_US\"]");
      assertEquals("http://feedburner.google.com/fb/a/mailverify?uri=JavaCodeGeeks&loc=en_US",
              v.elementAt(0));
      //number
      v = (Vector) parse("[1]");
      assertEquals(new Long(1), v.elementAt(0));
      v = (Vector) parse("[123478900001]");
      assertEquals(new Long(123478900001L), v.elementAt(0));
      v = (Vector) parse("[-123478900001]");
      assertEquals(new Long(-123478900001L), v.elementAt(0));
      v = (Vector) parse("[0]");
      assertEquals(new Long(0), v.elementAt(0));
      v = (Vector) parse("[-0]");
      assertEquals(new Long(-0), v.elementAt(0));
      v = (Vector) parse("[1.0]");
      assertEquals(new Double(1.0), v.elementAt(0));
      v = (Vector) parse("[0.0]");
      assertEquals(new Double(0.0), v.elementAt(0));
      v = (Vector) parse("[-1.0]");
      assertEquals(new Double(-1.0), v.elementAt(0));
      v = (Vector) parse("[-0.0]");
      assertEquals(new Double(-0.0), v.elementAt(0));
      v = (Vector) parse("[123243324.43434]");
      assertEquals(new Double(123243324.43434), v.elementAt(0));
      v = (Vector) parse("[-123243324.43434]");
      assertEquals(new Double(-123243324.43434), v.elementAt(0));
      v = (Vector) parse("[1E1]");
      assertEquals(new Long(1 * 10), v.elementAt(0));
      v = (Vector) parse("[1e1]");
      assertEquals(new Long(1 * 10), v.elementAt(0));
      v = (Vector) parse("[1E0]");
      assertEquals(new Long(1), v.elementAt(0));
      v = (Vector) parse("[1E-0]");
      assertEquals(new Long(1), v.elementAt(0));
      v = (Vector) parse("[1e-0]");
      assertEquals(new Long(1), v.elementAt(0));
      v = (Vector) parse("[1E-1]");
      assertEquals(new Double(0.1), v.elementAt(0));
      v = (Vector) parse("[1e-1]");
      assertEquals(new Double(0.1), v.elementAt(0));
      v = (Vector) parse("[-1E-1]");
      assertEquals(new Double(-0.1), v.elementAt(0));
      v = (Vector) parse("[-1e-1]");
      assertEquals(new Double(-0.1), v.elementAt(0));
      v = (Vector) parse("[12E10]");
      assertEquals(new Long(120000000000L), v.elementAt(0));
      v = (Vector) parse("[12e10]");
      assertEquals(new Long(120000000000L), v.elementAt(0));
      v = (Vector) parse("[-12E10]");
      assertEquals(new Long(-120000000000L), v.elementAt(0));
      v = (Vector) parse("[-12e10]");
      assertEquals(new Long(-120000000000L), v.elementAt(0));
      v = (Vector) parse("[0E0]");
      assertEquals(new Long(0), v.elementAt(0));
      v = (Vector) parse("[0e0]");
      assertEquals(new Long(0), v.elementAt(0));
      v = (Vector) parse("[0e-0]");
      assertEquals(new Long(0), v.elementAt(0));
      v = (Vector) parse("[0E-0]");
      assertEquals(new Long(0), v.elementAt(0));
      v = (Vector) parse("[-0E0]");
      assertEquals(new Long(0), v.elementAt(0));
      v = (Vector) parse("[-0e0]");
      assertEquals(new Long(0), v.elementAt(0));
      v = (Vector) parse("[-0e-0]");
      assertEquals(new Long(0), v.elementAt(0));
      v = (Vector) parse("[-0E-0]");
      assertEquals(new Long(0), v.elementAt(0));
      v = (Vector) parse("[1.0e0]");
      assertEquals(new Double(1.0), v.elementAt(0));
      v = (Vector) parse("[1.0e-0]");
      assertEquals(new Double(1.0), v.elementAt(0));
      v = (Vector) parse("[1.0E0]");
      assertEquals(new Double(1.0), v.elementAt(0));
      v = (Vector) parse("[1.0E-0]");
      assertEquals(new Double(1.0), v.elementAt(0));
      v = (Vector) parse("[0.0e0]");
      assertEquals(new Double(0.0), v.elementAt(0));
      v = (Vector) parse("[0.0e-0]");
      assertEquals(new Double(0.0), v.elementAt(0));
      v = (Vector) parse("[0.0E0]");
      assertEquals(new Double(0.0), v.elementAt(0));
      v = (Vector) parse("[0.0E-0]");
      assertEquals(new Double(0.0), v.elementAt(0));
      v = (Vector) parse("[-1.0e0]");
      assertEquals(new Double(-1.0), v.elementAt(0));
      v = (Vector) parse("[-1.0e-0]");
      assertEquals(new Double(-1.0), v.elementAt(0));
      v = (Vector) parse("[-1.0E0]");
      assertEquals(new Double(-1.0), v.elementAt(0));
      v = (Vector) parse("[-1.0E-0]");
      assertEquals(new Double(-1.0), v.elementAt(0));
      v = (Vector) parse("[-0.0e0]");
      assertEquals(new Double(-0.0), v.elementAt(0));
      v = (Vector) parse("[-0.0e-0]");
      assertEquals(new Double(-0.0), v.elementAt(0));
      v = (Vector) parse("[-0.0E0]");
      assertEquals(new Double(-0.0), v.elementAt(0));
      v = (Vector) parse("[-0.0E-0]");
      assertEquals(new Double(-0.0), v.elementAt(0));
      v = (Vector) parse("[12.0e10]");
      assertEquals(new Double(120000000000.0), v.elementAt(0));
      v = (Vector) parse("[12.0e-10]");
      assertEquals(new Double(0.0000000012), v.elementAt(0));
      v = (Vector) parse("[12.0E10]");
      assertEquals(new Double(120000000000.0), v.elementAt(0));
      v = (Vector) parse("[12.0E-10]");
      assertEquals(new Double(0.0000000012), v.elementAt(0));
      v = (Vector) parse("[-12.0e10]");
      assertEquals(new Double(-120000000000.0), v.elementAt(0));
      v = (Vector) parse("[-12.0e-10]");
      assertEquals(new Double(-0.0000000012), v.elementAt(0));
      v = (Vector) parse("[-12.0E10]");
      assertEquals(new Double(-120000000000.0), v.elementAt(0));
      v = (Vector) parse("[-12.0E-10]");
      assertEquals(new Double(-0.0000000012), v.elementAt(0));
   }

   /****************************************************************************
    * 
    ***************************************************************************/
   public void testErronousPrimitiveValues() throws Exception {

      //nulll
      parseUnexpected("[nulL", 'L');
      parseUnexpected("[tru ", ' ');
      parseUnexpected("[falsee", 'e');
      //string
      parseUnexpected("[\"\"#", '#');
      // number
      parseUnexpected("[1x", 'x');
      parseUnexpected("[1. ", ' ');
      parseUnexpected("[1.0e ", ' ');
      parseUnexpected("[1.0d", 'd');
      parseUnexpected("[1.0e2f", 'f');
      parseUnexpected("[1e ", ' ');
      assertTrue(true);
   }

   /****************************************************************************
    * 
    ***************************************************************************/
   public void testEOFPrimitiveValues() throws Exception {

      //nulll
      parseEOF("[nul");
      parseEOF("[tru");
      parseEOF("[fals");
      //string
      parseEOF("[\"");
      // number
      parseEOF("[1.");
      parseEOF("[-");
      parseEOF("[1.0e");
      parseEOF("[1.0e-");
      parseEOF("[1e");
      assertTrue(true);
   }

   /****************************************************************************
    * 
    ***************************************************************************/
   public void testProperArrays() throws Exception {

      Vector result;
      // without whitespace
      assertEquals(new Vector(), parse("[]"));
      result = (Vector) parse("[12]");
      assertEquals(1, result.size());
      assertEquals(new Long(12), result.elementAt(0));
      result = (Vector) parse("[12,12.0E-10]");
      assertEquals(2, result.size());
      assertEquals(new Long(12), result.elementAt(0));
      assertEquals(new Double(0.0000000012), result.elementAt(1));
      result = (Vector) parse("[\"\"]");
      assertEquals(1, result.size());
      assertEquals("", result.elementAt(0));
      result = (Vector) parse("[\"\",\"12.0E-10\"]");
      assertEquals(2, result.size());
      assertEquals("", result.elementAt(0));
      assertEquals("12.0E-10", result.elementAt(1));
      result = (Vector) parse("[true]");
      assertEquals(1, result.size());
      assertEquals(Boolean.TRUE, result.elementAt(0));
      result = (Vector) parse("[true,true]");
      assertEquals(2, result.size());
      assertEquals(Boolean.TRUE, result.elementAt(0));
      assertEquals(Boolean.TRUE, result.elementAt(1));
      result = (Vector) parse("[false]");
      assertEquals(1, result.size());
      assertEquals(Boolean.FALSE, result.elementAt(0));
      result = (Vector) parse("[false,false]");
      assertEquals(2, result.size());
      assertEquals(Boolean.FALSE, result.elementAt(0));
      assertEquals(Boolean.FALSE, result.elementAt(1));
      result = (Vector) parse("[null]");
      assertEquals(1, result.size());
      assertEquals(Null.value, result.elementAt(0));
      result = (Vector) parse("[null,null]");
      assertEquals(2, result.size());
      assertEquals(Null.value, result.elementAt(0));
      assertEquals(Null.value, result.elementAt(1));
      result = (Vector) parse("[[]]");
      assertEquals(1, result.size());
      assertEquals(new Vector(), result.elementAt(0));
      result = (Vector) parse("[[],[]]");
      assertEquals(2, result.size());
      assertEquals(new Vector(), result.elementAt(0));
      assertEquals(new Vector(), result.elementAt(1));
      result = (Vector) parse("[{}]");
      assertEquals(1, result.size());
      assertEquals(new Hashtable(), result.elementAt(0));
      result = (Vector) parse("[{},{}]");
      assertEquals(2, result.size());
      assertEquals(new Hashtable(), result.elementAt(0));
      assertEquals(new Hashtable(), result.elementAt(1));
      result = (Vector) parse("[[[]]]");
      assertEquals(1, result.size());
      result = (Vector) result.elementAt(0);
      assertEquals(1, result.size());
      assertEquals(new Vector(), result.elementAt(0));

      // with whitespace
      assertEquals(new Vector(), parse("[ ]"));
      result = (Vector) parse("[ 12 ]");
      assertEquals(1, result.size());
      assertEquals(new Long(12), result.elementAt(0));
      result = (Vector) parse("[ 12 , 12.0E-10 ]");
      assertEquals(2, result.size());
      assertEquals(new Long(12), result.elementAt(0));
      assertEquals(new Double(0.0000000012), result.elementAt(1));
      result = (Vector) parse("[ \"\" ]");
      assertEquals(1, result.size());
      assertEquals("", result.elementAt(0));
      result = (Vector) parse("[ \"\" , \"12.0E-10\" ]");
      assertEquals(2, result.size());
      assertEquals("", result.elementAt(0));
      assertEquals("12.0E-10", result.elementAt(1));
      result = (Vector) parse("[ true ]");
      assertEquals(1, result.size());
      assertEquals(Boolean.TRUE, result.elementAt(0));
      result = (Vector) parse("[ true   , true ]");
      assertEquals(2, result.size());
      assertEquals(Boolean.TRUE, result.elementAt(0));
      assertEquals(Boolean.TRUE, result.elementAt(1));
      result = (Vector) parse("[ false   ]");
      assertEquals(1, result.size());
      assertEquals(Boolean.FALSE, result.elementAt(0));
      result = (Vector) parse("[ false  , false ]");
      assertEquals(2, result.size());
      assertEquals(Boolean.FALSE, result.elementAt(0));
      assertEquals(Boolean.FALSE, result.elementAt(1));
      result = (Vector) parse("[   null ]");
      assertEquals(1, result.size());
      assertEquals(Null.value, result.elementAt(0));
      result = (Vector) parse("[ null , null ]");
      assertEquals(2, result.size());
      assertEquals(Null.value, result.elementAt(0));
      assertEquals(Null.value, result.elementAt(1));
      result = (Vector) parse("[ [ ] ]");
      assertEquals(1, result.size());
      assertEquals(new Vector(), result.elementAt(0));
      result = (Vector) parse("[   [   ] , [ ] ]");
      assertEquals(2, result.size());
      assertEquals(new Vector(), result.elementAt(0));
      assertEquals(new Vector(), result.elementAt(1));
      result = (Vector) parse("[ { } ]");
      assertEquals(1, result.size());
      assertEquals(new Hashtable(), result.elementAt(0));
      result = (Vector) parse("[ {  } , {  } ]");
      assertEquals(2, result.size());
      assertEquals(new Hashtable(), result.elementAt(0));
      assertEquals(new Hashtable(), result.elementAt(1));
      result = (Vector) parse("[  [ [    ] ]   ]");
      assertEquals(1, result.size());
      result = (Vector) result.elementAt(0);
      assertEquals(1, result.size());
      assertEquals(new Vector(), result.elementAt(0));

   }

   /****************************************************************************
    * 
    ***************************************************************************/
   public void testErronousArrays() throws Exception {

      //nulll
      parseUnexpected("[,", ',');
      parseUnexpected("[ :", ':');
      parseUnexpected("[ }", '}');
      parseUnexpected("[ 1,]", ']');
   }

   /****************************************************************************
    * 
    ***************************************************************************/
   public void testEOFArrays() throws Exception {

      parseEOF("[");
      parseEOF("[2");
      parseEOF("[2,");
   }
   /****************************************************************************
    * 
    ***************************************************************************/
   public void testDuplicatedKey() throws Exception {

      try {
         parse("{\"a\":1,\"a\":2}");
         fail();
      } catch (final DuplicatedKeyException e) {
         assertEquals("a", e.key);
      }
   }

   /****************************************************************************
    * 
    ***************************************************************************/
   public void testProperObjects() throws Exception {

      Hashtable result;
      // without whitespace
      assertEquals(new Hashtable(), parse("{}"));
      result = (Hashtable) parse("{\"abc\":true}");
      assertEquals(1, result.size());
      assertEquals(Boolean.TRUE, result.get("abc"));
      result = (Hashtable) parse("{\"abc\":false}");
      assertEquals(1, result.size());
      assertEquals(Boolean.FALSE, result.get("abc"));
      result = (Hashtable) parse("{\"abc\":null}");
      assertEquals(1, result.size());
      assertEquals(Null.value, result.get("abc"));
      result = (Hashtable) parse("{\"abc\":12}");
      assertEquals(1, result.size());
      assertEquals(new Long(12), result.get("abc"));
      result = (Hashtable) parse("{\"abc\":-12.0e10}");
      assertEquals(1, result.size());
      assertEquals(new Double(-120000000000.0), result.get("abc"));
      result = (Hashtable) parse("{\"abc\":\"\"}");
      assertEquals(1, result.size());
      assertEquals("", result.get("abc"));
      result = (Hashtable) parse("{\"\":\"\"}");
      assertEquals(1, result.size());
      assertEquals("", result.get(""));
      result = (Hashtable) parse("{\"abc\":{}}");
      assertEquals(1, result.size());
      assertEquals(new Hashtable(), result.get("abc"));
      result = (Hashtable) parse("{\"abc\":[]}");
      assertEquals(1, result.size());
      assertEquals(new Vector(), result.get("abc"));
      result = (Hashtable) parse("{\"abc\":true,\"def\":true}");
      assertEquals(2, result.size());
      assertEquals(Boolean.TRUE, result.get("abc"));
      assertEquals(Boolean.TRUE, result.get("def"));
      result = (Hashtable) parse("{\"abc\":false,\"def\":false}");
      assertEquals(2, result.size());
      assertEquals(Boolean.FALSE, result.get("abc"));
      assertEquals(Boolean.FALSE, result.get("def"));
      result = (Hashtable) parse("{\"abc\":null,\"def\":null}");
      assertEquals(2, result.size());
      assertEquals(Null.value, result.get("abc"));
      assertEquals(Null.value, result.get("def"));
      result = (Hashtable) parse("{\"abc\":12,\"def\":12}");
      assertEquals(2, result.size());
      assertEquals(new Long(12), result.get("abc"));
      assertEquals(new Long(12), result.get("def"));
      result = (Hashtable) parse("{\"abc\":-12.0e10,\"def\":-12.0e10}");
      assertEquals(2, result.size());
      assertEquals(new Double(-120000000000.0), result.get("abc"));
      assertEquals(new Double(-120000000000.0), result.get("def"));
      result = (Hashtable) parse("{\"abc\":\"\",\"def\":\"\"}");
      assertEquals(2, result.size());
      assertEquals("", result.get("abc"));
      assertEquals("", result.get("def"));
      result = (Hashtable) parse("{\"abc\":{},\"def\":{}}");
      assertEquals(2, result.size());
      assertEquals(new Hashtable(), result.get("abc"));
      assertEquals(new Hashtable(), result.get("def"));
      result = (Hashtable) parse("{\"abc\":[],\"def\":[]}");
      assertEquals(2, result.size());
      assertEquals(new Vector(), result.get("abc"));
      assertEquals(new Vector(), result.get("def"));

      // with whitespace
      assertEquals(new Hashtable(), parse("{  }"));
      result = (Hashtable) parse("{ \"abc\" : true }");
      assertEquals(1, result.size());
      assertEquals(Boolean.TRUE, result.get("abc"));
      result = (Hashtable) parse("{ \"abc\" : false }");
      assertEquals(1, result.size());
      assertEquals(Boolean.FALSE, result.get("abc"));
      result = (Hashtable) parse("{  \"abc\"    :  null  }");
      assertEquals(1, result.size());
      assertEquals(Null.value, result.get("abc"));
      result = (Hashtable) parse("{ \"abc\"  :  12 }");
      assertEquals(1, result.size());
      assertEquals(new Long(12), result.get("abc"));
      result = (Hashtable) parse("{ \"abc\" : -12.0e10 }");
      assertEquals(1, result.size());
      assertEquals(new Double(-120000000000.0), result.get("abc"));
      result = (Hashtable) parse("{ \"abc\" : \"\" }");
      assertEquals(1, result.size());
      assertEquals("", result.get("abc"));
      result = (Hashtable) parse("{ \"\" :   \"\" }");
      assertEquals(1, result.size());
      assertEquals("", result.get(""));
      result = (Hashtable) parse("{ \"abc\" : {   } \n\t}");
      assertEquals(1, result.size());
      assertEquals(new Hashtable(), result.get("abc"));
      result = (Hashtable) parse("{ \"abc\": [ ] }");
      assertEquals(1, result.size());
      assertEquals(new Vector(), result.get("abc"));
      result = (Hashtable) parse("{  \"abc\"  : true ,\r\n\"def\" : true }");
      assertEquals(2, result.size());
      assertEquals(Boolean.TRUE, result.get("abc"));
      assertEquals(Boolean.TRUE, result.get("def"));
      result = (Hashtable) parse("{ \"abc\" : false , \"def\" : false }");
      assertEquals(2, result.size());
      assertEquals(Boolean.FALSE, result.get("abc"));
      assertEquals(Boolean.FALSE, result.get("def"));
      result = (Hashtable) parse("{ \"abc\" : null , \"def\": null }");
      assertEquals(2, result.size());
      assertEquals(Null.value, result.get("abc"));
      assertEquals(Null.value, result.get("def"));
      result = (Hashtable) parse("{ \"abc\" : 12 , \"def\"  : 12 }");
      assertEquals(2, result.size());
      assertEquals(new Long(12), result.get("abc"));
      assertEquals(new Long(12), result.get("def"));
      result = (Hashtable) parse("{  \"abc\"\r\n: -12.0e10 , \"def\" : -12.0e10  }");
      assertEquals(2, result.size());
      assertEquals(new Double(-120000000000.0), result.get("abc"));
      assertEquals(new Double(-120000000000.0), result.get("def"));
      result = (Hashtable) parse("{ \"abc\" : \"\",\r\n\t\"def\" : \"\" }");
      assertEquals(2, result.size());
      assertEquals("", result.get("abc"));
      assertEquals("", result.get("def"));
      result = (Hashtable) parse("{ \"abc\" : { } , \"def\" :  {  } }");
      assertEquals(2, result.size());
      assertEquals(new Hashtable(), result.get("abc"));
      assertEquals(new Hashtable(), result.get("def"));
      result = (Hashtable) parse("{ \"abc\"  : [\r\n], \"def\" : [\t] }");
      assertEquals(2, result.size());
      assertEquals(new Vector(), result.get("abc"));
      assertEquals(new Vector(), result.get("def"));
      parse(example); // parses properly
   }

   /****************************************************************************
    * 
    ***************************************************************************/
   public void testErronousObjects() throws Exception {

      //nulll
      parseUnexpected("{,", ',');
      parseUnexpected("{ :", ':');
      parseUnexpected("{ ]", ']');
      parseUnexpected("{ \"a\":}", '}');
      parseUnexpected("{ \"a\": \"b\",}", '}');
   }

   /****************************************************************************
    * 
    ***************************************************************************/
   public void testEOFObjects() throws Exception {

      //nulll
      parseEOF("{");
      parseEOF("");
      parseEOF("{ \"a\":");
      parseEOF("{ \"a\": \"b\",");
   }

   /****************************************************************************
    * 
    ***************************************************************************/
   public void testPparseNullPointer() throws IOException {

      try {
         p.parse((Reader)null);
         fail();
      } catch (final NullPointerException e) {
         assertTrue(true);
      }
      try {
         p.parse((String)null);
         fail();
      } catch (final NullPointerException e) {
         assertTrue(true);
      }
   }
   /****************************************************************************
    * 
    ***************************************************************************/
   public void testCreation() throws IOException {

      try {
         new Parser(0);
         fail();
      } catch (final IllegalArgumentException e) {
         assertTrue(true);
      }
      try {
         new Parser(-1);
         fail();
      } catch (final IllegalArgumentException e) {
         assertTrue(true);
      }
   }

   /****************************************************************************
    * 
    ***************************************************************************/
   private void parseUnexpected(final String str, final char unexpectedChar)
           throws IOException {

      try {
         parse(str);
         fail();
      } catch (final UnexpectedCharacterException e) {
         assertEquals(unexpectedChar, e.character);
      }
   }

   /****************************************************************************
    * 
    ***************************************************************************/
   private void parseEOF(final String str) throws IOException {

      try {
         parse(str);
         fail();
      } catch (final EOFException e) {
         assertTrue(true);
      }
   }

   /****************************************************************************
    * 
    ***************************************************************************/
   private Object parse(final String str) throws IOException {

      return this.p.parse(str);
   }
   /****************************************************************************
    * 
    ***************************************************************************/
   private final Parser p = new Parser(16);
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
