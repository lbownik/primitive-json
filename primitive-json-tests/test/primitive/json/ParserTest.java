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
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.ArrayList;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/*******************************************************************************
 * @author lukasz.bownik@gmail.com
 ******************************************************************************/
@SuppressWarnings("rawtypes")
public class ParserTest{

   /****************************************************************************
    * 
    ***************************************************************************/
   @Test
   public void testProperPrimitiveValues() throws Exception {

      ArrayList v;
      //null
      v = (ArrayList) parse("[null]");
      assertTrue(v.get(0) == null);
      //true
      v = (ArrayList) parse("[true]");
      assertTrue(Boolean.TRUE == v.get(0));
      // false
      v = (ArrayList) parse("[false]");
      assertTrue(Boolean.FALSE == v.get(0));
      //string
      v = (ArrayList) parse("[\"\"]");
      assertEquals("", v.get(0));
      v = (ArrayList) parse("[\" \\tt\\rr\\nn\\ff\"]");
      assertEquals(" \tt\rr\nn\ff", v.get(0));
      v = (ArrayList) parse("[\"\\\"\\\"\"]");
      assertEquals("\"\"", v.get(0));
      v = (ArrayList) parse("[\"ał \\t$-_\"]");
      assertEquals("ał \t$-_", v.get(0));
      v = (ArrayList) parse("[\"a\\u0041b\\u0042\\u005A\"]");
      assertEquals("aAbBZ", v.get(0));
      v = (ArrayList) parse("[\"{\\\"aaa\\\": true, \\\"b\\\" : [null, null]}\"]");
      assertEquals("{\"aaa\": true, \"b\" : [null, null]}", v.get(0));
      v = (ArrayList) parse(
              "[\"http://feedburner.google.com/fb/a/mailverify?uri=JavaCodeGeeks&loc=en_US\"]");
      assertEquals("http://feedburner.google.com/fb/a/mailverify?uri=JavaCodeGeeks&loc=en_US",
              v.get(0));
      //number
      v = (ArrayList) parse("[1]");
      assertEquals(new Long(1), v.get(0));
      v = (ArrayList) parse("[123478900001]");
      assertEquals(new Long(123478900001L), v.get(0));
      v = (ArrayList) parse("[-123478900001]");
      assertEquals(new Long(-123478900001L), v.get(0));
      v = (ArrayList) parse("[0]");
      assertEquals(new Long(0), v.get(0));
      v = (ArrayList) parse("[-0]");
      assertEquals(new Long(-0), v.get(0));
      v = (ArrayList) parse("[1.0]");
      assertEquals(new Double(1.0), v.get(0));
      v = (ArrayList) parse("[0.0]");
      assertEquals(new Double(0.0), v.get(0));
      v = (ArrayList) parse("[-1.0]");
      assertEquals(new Double(-1.0), v.get(0));
      v = (ArrayList) parse("[-0.0]");
      assertEquals(new Double(-0.0), v.get(0));
      v = (ArrayList) parse("[123243324.43434]");
      assertEquals(new Double(123243324.43434), v.get(0));
      v = (ArrayList) parse("[-123243324.43434]");
      assertEquals(new Double(-123243324.43434), v.get(0));
      v = (ArrayList) parse("[1E1]");
      assertEquals(new Long(1 * 10), v.get(0));
      v = (ArrayList) parse("[1e1]");
      assertEquals(new Long(1 * 10), v.get(0));
      v = (ArrayList) parse("[1E0]");
      assertEquals(new Long(1), v.get(0));
      v = (ArrayList) parse("[1E-0]");
      assertEquals(new Long(1), v.get(0));
      v = (ArrayList) parse("[1e-0]");
      assertEquals(new Long(1), v.get(0));
      v = (ArrayList) parse("[1E-1]");
      assertEquals(new Double(0.1), v.get(0));
      v = (ArrayList) parse("[1e-1]");
      assertEquals(new Double(0.1), v.get(0));
      v = (ArrayList) parse("[-1E-1]");
      assertEquals(new Double(-0.1), v.get(0));
      v = (ArrayList) parse("[-1e-1]");
      assertEquals(new Double(-0.1), v.get(0));
      v = (ArrayList) parse("[12E10]");
      assertEquals(new Long(120000000000L), v.get(0));
      v = (ArrayList) parse("[12e10]");
      assertEquals(new Long(120000000000L), v.get(0));
      v = (ArrayList) parse("[-12E10]");
      assertEquals(new Long(-120000000000L), v.get(0));
      v = (ArrayList) parse("[-12e10]");
      assertEquals(new Long(-120000000000L), v.get(0));
      v = (ArrayList) parse("[0E0]");
      assertEquals(new Long(0), v.get(0));
      v = (ArrayList) parse("[0e0]");
      assertEquals(new Long(0), v.get(0));
      v = (ArrayList) parse("[0e-0]");
      assertEquals(new Long(0), v.get(0));
      v = (ArrayList) parse("[0E-0]");
      assertEquals(new Long(0), v.get(0));
      v = (ArrayList) parse("[-0E0]");
      assertEquals(new Long(0), v.get(0));
      v = (ArrayList) parse("[-0e0]");
      assertEquals(new Long(0), v.get(0));
      v = (ArrayList) parse("[-0e-0]");
      assertEquals(new Long(0), v.get(0));
      v = (ArrayList) parse("[-0E-0]");
      assertEquals(new Long(0), v.get(0));
      v = (ArrayList) parse("[1.0e0]");
      assertEquals(new Double(1.0), v.get(0));
      v = (ArrayList) parse("[1.0e-0]");
      assertEquals(new Double(1.0), v.get(0));
      v = (ArrayList) parse("[1.0E0]");
      assertEquals(new Double(1.0), v.get(0));
      v = (ArrayList) parse("[1.0E-0]");
      assertEquals(new Double(1.0), v.get(0));
      v = (ArrayList) parse("[0.0e0]");
      assertEquals(new Double(0.0), v.get(0));
      v = (ArrayList) parse("[0.0e-0]");
      assertEquals(new Double(0.0), v.get(0));
      v = (ArrayList) parse("[0.0E0]");
      assertEquals(new Double(0.0), v.get(0));
      v = (ArrayList) parse("[0.0E-0]");
      assertEquals(new Double(0.0), v.get(0));
      v = (ArrayList) parse("[-1.0e0]");
      assertEquals(new Double(-1.0), v.get(0));
      v = (ArrayList) parse("[-1.0e-0]");
      assertEquals(new Double(-1.0), v.get(0));
      v = (ArrayList) parse("[-1.0E0]");
      assertEquals(new Double(-1.0), v.get(0));
      v = (ArrayList) parse("[-1.0E-0]");
      assertEquals(new Double(-1.0), v.get(0));
      v = (ArrayList) parse("[-0.0e0]");
      assertEquals(new Double(-0.0), v.get(0));
      v = (ArrayList) parse("[-0.0e-0]");
      assertEquals(new Double(-0.0), v.get(0));
      v = (ArrayList) parse("[-0.0E0]");
      assertEquals(new Double(-0.0), v.get(0));
      v = (ArrayList) parse("[-0.0E-0]");
      assertEquals(new Double(-0.0), v.get(0));
      v = (ArrayList) parse("[12.0e10]");
      assertEquals(new Double(120000000000.0), v.get(0));
      v = (ArrayList) parse("[12.0e-10]");
      assertEquals(new Double(0.0000000012), v.get(0));
      v = (ArrayList) parse("[12.0E10]");
      assertEquals(new Double(120000000000.0), v.get(0));
      v = (ArrayList) parse("[12.0E-10]");
      assertEquals(new Double(0.0000000012), v.get(0));
      v = (ArrayList) parse("[-12.0e10]");
      assertEquals(new Double(-120000000000.0), v.get(0));
      v = (ArrayList) parse("[-12.0e-10]");
      assertEquals(new Double(-0.0000000012), v.get(0));
      v = (ArrayList) parse("[-12.0E10]");
      assertEquals(new Double(-120000000000.0), v.get(0));
      v = (ArrayList) parse("[-12.0E-10]");
      assertEquals(new Double(-0.0000000012), v.get(0));
   }

   /****************************************************************************
    * 
    ***************************************************************************/
   @Test
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
   @Test
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
   @Test
   public void testProperArrays() throws Exception {

      ArrayList result;
      // without whitespace
      assertEquals(new ArrayList(), parse("[]"));
      result = (ArrayList) parse("[12]");
      assertEquals(1, result.size());
      assertEquals(new Long(12), result.get(0));
      result = (ArrayList) parse("[12,12.0E-10]");
      assertEquals(2, result.size());
      assertEquals(new Long(12), result.get(0));
      assertEquals(new Double(0.0000000012), result.get(1));
      result = (ArrayList) parse("[\"\"]");
      assertEquals(1, result.size());
      assertEquals("", result.get(0));
      result = (ArrayList) parse("[\"\",\"12.0E-10\"]");
      assertEquals(2, result.size());
      assertEquals("", result.get(0));
      assertEquals("12.0E-10", result.get(1));
      result = (ArrayList) parse("[true]");
      assertEquals(1, result.size());
      assertEquals(Boolean.TRUE, result.get(0));
      result = (ArrayList) parse("[true,true]");
      assertEquals(2, result.size());
      assertEquals(Boolean.TRUE, result.get(0));
      assertEquals(Boolean.TRUE, result.get(1));
      result = (ArrayList) parse("[false]");
      assertEquals(1, result.size());
      assertEquals(Boolean.FALSE, result.get(0));
      result = (ArrayList) parse("[false,false]");
      assertEquals(2, result.size());
      assertEquals(Boolean.FALSE, result.get(0));
      assertEquals(Boolean.FALSE, result.get(1));
      result = (ArrayList) parse("[null]");
      assertEquals(1, result.size());
      assertNull(result.get(0));
      result = (ArrayList) parse("[null,null]");
      assertEquals(2, result.size());
      assertNull(result.get(0));
      assertNull(result.get(1));
      result = (ArrayList) parse("[[]]");
      assertEquals(1, result.size());
      assertEquals(new ArrayList(), result.get(0));
      result = (ArrayList) parse("[[],[]]");
      assertEquals(2, result.size());
      assertEquals(new ArrayList(), result.get(0));
      assertEquals(new ArrayList(), result.get(1));
      result = (ArrayList) parse("[{}]");
      assertEquals(1, result.size());
      assertEquals(new HashMap(), result.get(0));
      result = (ArrayList) parse("[{},{}]");
      assertEquals(2, result.size());
      assertEquals(new HashMap(), result.get(0));
      assertEquals(new HashMap(), result.get(1));
      result = (ArrayList) parse("[[[]]]");
      assertEquals(1, result.size());
      result = (ArrayList) result.get(0);
      assertEquals(1, result.size());
      assertEquals(new ArrayList(), result.get(0));

      // with whitespace
      assertEquals(new ArrayList(), parse("[ ]"));
      result = (ArrayList) parse("[ 12 ]");
      assertEquals(1, result.size());
      assertEquals(new Long(12), result.get(0));
      result = (ArrayList) parse("[ 12 , 12.0E-10 ]");
      assertEquals(2, result.size());
      assertEquals(new Long(12), result.get(0));
      assertEquals(new Double(0.0000000012), result.get(1));
      result = (ArrayList) parse("[ \"\" ]");
      assertEquals(1, result.size());
      assertEquals("", result.get(0));
      result = (ArrayList) parse("[ \"\" , \"12.0E-10\" ]");
      assertEquals(2, result.size());
      assertEquals("", result.get(0));
      assertEquals("12.0E-10", result.get(1));
      result = (ArrayList) parse("[ true ]");
      assertEquals(1, result.size());
      assertEquals(Boolean.TRUE, result.get(0));
      result = (ArrayList) parse("[ true   , true ]");
      assertEquals(2, result.size());
      assertEquals(Boolean.TRUE, result.get(0));
      assertEquals(Boolean.TRUE, result.get(1));
      result = (ArrayList) parse("[ false   ]");
      assertEquals(1, result.size());
      assertEquals(Boolean.FALSE, result.get(0));
      result = (ArrayList) parse("[ false  , false ]");
      assertEquals(2, result.size());
      assertEquals(Boolean.FALSE, result.get(0));
      assertEquals(Boolean.FALSE, result.get(1));
      result = (ArrayList) parse("[   null ]");
      assertEquals(1, result.size());
      assertNull(result.get(0));
      result = (ArrayList) parse("[ null , null ]");
      assertEquals(2, result.size());
      assertNull(result.get(0));
      assertNull(result.get(1));
      result = (ArrayList) parse("[ [ ] ]");
      assertEquals(1, result.size());
      assertEquals(new ArrayList(), result.get(0));
      result = (ArrayList) parse("[   [   ] , [ ] ]");
      assertEquals(2, result.size());
      assertEquals(new ArrayList(), result.get(0));
      assertEquals(new ArrayList(), result.get(1));
      result = (ArrayList) parse("[ { } ]");
      assertEquals(1, result.size());
      assertEquals(new HashMap(), result.get(0));
      result = (ArrayList) parse("[ {  } , {  } ]");
      assertEquals(2, result.size());
      assertEquals(new HashMap(), result.get(0));
      assertEquals(new HashMap(), result.get(1));
      result = (ArrayList) parse("[  [ [    ] ]   ]");
      assertEquals(1, result.size());
      result = (ArrayList) result.get(0);
      assertEquals(1, result.size());
      assertEquals(new ArrayList(), result.get(0));

   }

   /****************************************************************************
    * 
    ***************************************************************************/
   @Test
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
   @Test
   public void testEOFArrays() throws Exception {

      parseEOF("[");
      parseEOF("[2");
      parseEOF("[2,");
   }
   /****************************************************************************
    * 
    ***************************************************************************/
   @Test
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
   @Test
   public void testProperObjects() throws Exception {

      HashMap result;
      // without whitespace
      assertEquals(new HashMap(), parse("{}"));
      result = (HashMap) parse("{\"abc\":true}");
      assertEquals(1, result.size());
      assertEquals(Boolean.TRUE, result.get("abc"));
      result = (HashMap) parse("{\"abc\":false}");
      assertEquals(1, result.size());
      assertEquals(Boolean.FALSE, result.get("abc"));
      result = (HashMap) parse("{\"abc\":null}");
      assertEquals(1, result.size());
      assertNull(result.get("abc"));
      result = (HashMap) parse("{\"abc\":12}");
      assertEquals(1, result.size());
      assertEquals(new Long(12), result.get("abc"));
      result = (HashMap) parse("{\"abc\":-12.0e10}");
      assertEquals(1, result.size());
      assertEquals(new Double(-120000000000.0), result.get("abc"));
      result = (HashMap) parse("{\"abc\":\"\"}");
      assertEquals(1, result.size());
      assertEquals("", result.get("abc"));
      result = (HashMap) parse("{\"\":\"\"}");
      assertEquals(1, result.size());
      assertEquals("", result.get(""));
      result = (HashMap) parse("{\"abc\":{}}");
      assertEquals(1, result.size());
      assertEquals(new HashMap(), result.get("abc"));
      result = (HashMap) parse("{\"abc\":[]}");
      assertEquals(1, result.size());
      assertEquals(new ArrayList(), result.get("abc"));
      result = (HashMap) parse("{\"abc\":true,\"def\":true}");
      assertEquals(2, result.size());
      assertEquals(Boolean.TRUE, result.get("abc"));
      assertEquals(Boolean.TRUE, result.get("def"));
      result = (HashMap) parse("{\"abc\":false,\"def\":false}");
      assertEquals(2, result.size());
      assertEquals(Boolean.FALSE, result.get("abc"));
      assertEquals(Boolean.FALSE, result.get("def"));
      result = (HashMap) parse("{\"abc\":null,\"def\":null}");
      assertEquals(2, result.size());
      assertNull(result.get("abc"));
      assertNull(result.get("def"));
      result = (HashMap) parse("{\"abc\":12,\"def\":12}");
      assertEquals(2, result.size());
      assertEquals(new Long(12), result.get("abc"));
      assertEquals(new Long(12), result.get("def"));
      result = (HashMap) parse("{\"abc\":-12.0e10,\"def\":-12.0e10}");
      assertEquals(2, result.size());
      assertEquals(new Double(-120000000000.0), result.get("abc"));
      assertEquals(new Double(-120000000000.0), result.get("def"));
      result = (HashMap) parse("{\"abc\":\"\",\"def\":\"\"}");
      assertEquals(2, result.size());
      assertEquals("", result.get("abc"));
      assertEquals("", result.get("def"));
      result = (HashMap) parse("{\"abc\":{},\"def\":{}}");
      assertEquals(2, result.size());
      assertEquals(new HashMap(), result.get("abc"));
      assertEquals(new HashMap(), result.get("def"));
      result = (HashMap) parse("{\"abc\":[],\"def\":[]}");
      assertEquals(2, result.size());
      assertEquals(new ArrayList(), result.get("abc"));
      assertEquals(new ArrayList(), result.get("def"));

      // with whitespace
      assertEquals(new HashMap(), parse("{  }"));
      result = (HashMap) parse("{ \"abc\" : true }");
      assertEquals(1, result.size());
      assertEquals(Boolean.TRUE, result.get("abc"));
      result = (HashMap) parse("{ \"abc\" : false }");
      assertEquals(1, result.size());
      assertEquals(Boolean.FALSE, result.get("abc"));
      result = (HashMap) parse("{  \"abc\"    :  null  }");
      assertEquals(1, result.size());
      assertNull(result.get("abc"));
      result = (HashMap) parse("{ \"abc\"  :  12 }");
      assertEquals(1, result.size());
      assertEquals(new Long(12), result.get("abc"));
      result = (HashMap) parse("{ \"abc\" : -12.0e10 }");
      assertEquals(1, result.size());
      assertEquals(new Double(-120000000000.0), result.get("abc"));
      result = (HashMap) parse("{ \"abc\" : \"\" }");
      assertEquals(1, result.size());
      assertEquals("", result.get("abc"));
      result = (HashMap) parse("{ \"\" :   \"\" }");
      assertEquals(1, result.size());
      assertEquals("", result.get(""));
      result = (HashMap) parse("{ \"abc\" : {   } \n\t}");
      assertEquals(1, result.size());
      assertEquals(new HashMap(), result.get("abc"));
      result = (HashMap) parse("{ \"abc\": [ ] }");
      assertEquals(1, result.size());
      assertEquals(new ArrayList(), result.get("abc"));
      result = (HashMap) parse("{  \"abc\"  : true ,\r\n\"def\" : true }");
      assertEquals(2, result.size());
      assertEquals(Boolean.TRUE, result.get("abc"));
      assertEquals(Boolean.TRUE, result.get("def"));
      result = (HashMap) parse("{ \"abc\" : false , \"def\" : false }");
      assertEquals(2, result.size());
      assertEquals(Boolean.FALSE, result.get("abc"));
      assertEquals(Boolean.FALSE, result.get("def"));
      result = (HashMap) parse("{ \"abc\" : null , \"def\": null }");
      assertEquals(2, result.size());
      assertNull(result.get("abc"));
      assertNull(result.get("def"));
      result = (HashMap) parse("{ \"abc\" : 12 , \"def\"  : 12 }");
      assertEquals(2, result.size());
      assertEquals(new Long(12), result.get("abc"));
      assertEquals(new Long(12), result.get("def"));
      result = (HashMap) parse("{  \"abc\"\r\n: -12.0e10 , \"def\" : -12.0e10  }");
      assertEquals(2, result.size());
      assertEquals(new Double(-120000000000.0), result.get("abc"));
      assertEquals(new Double(-120000000000.0), result.get("def"));
      result = (HashMap) parse("{ \"abc\" : \"\",\r\n\t\"def\" : \"\" }");
      assertEquals(2, result.size());
      assertEquals("", result.get("abc"));
      assertEquals("", result.get("def"));
      result = (HashMap) parse("{ \"abc\" : { } , \"def\" :  {  } }");
      assertEquals(2, result.size());
      assertEquals(new HashMap(), result.get("abc"));
      assertEquals(new HashMap(), result.get("def"));
      result = (HashMap) parse("{ \"abc\"  : [\r\n], \"def\" : [\t] }");
      assertEquals(2, result.size());
      assertEquals(new ArrayList(), result.get("abc"));
      assertEquals(new ArrayList(), result.get("def"));
      parse(example); // parses properly
   }

   /****************************************************************************
    * 
    ***************************************************************************/
   @Test
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
   @Test
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
   @Test
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
   @Test
   public void testCreation() throws IOException {

      try {
         new Parser(0,10,10);
         fail();
      } catch (final IllegalArgumentException e) {
         assertTrue(true);
      }
      try {
         new Parser(-1,10,10);
         fail();
      } catch (final IllegalArgumentException e) {
         assertTrue(true);
      }
   }

   /****************************************************************************
    * 
    ***************************************************************************/
   @Test
   public void testParseFormRawSocket() throws Exception {

      new Thread() {

         @Override
         public void run() {
            try {
               final ServerSocket ss = new ServerSocket(60400);
               final Socket s = ss.accept();
               final Writer out = new OutputStreamWriter(s.getOutputStream(), "UTF-8");
               final InputStream in = s.getInputStream();
               out.write("{\"ip\": \"8.8.8.8\"}");
               out.flush();
               in.read();
               out.write("{\"ip\": \"8.8.8.8\"}");
               out.flush();
               out.close();
               in.close();
            } catch (final Exception e) {
               throw new RuntimeException(e);
            }
         }

      }.start();

      final Socket s = new Socket("localhost", 60400);
      final Reader in = new InputStreamReader(s.getInputStream(), "UTF-8");
      final OutputStream out = s.getOutputStream();
      HashMap o = null;
      System.out.println("connected");
      o = (HashMap) this.p.parse(in);
      System.out.println(new Generator().toString(o));
      out.write(1);
      o = (HashMap) this.p.parse(in);
      System.out.println(new Generator().toString(o));
      in.close();
      out.close();
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
   private final Parser p = new Parser(16,10,10);
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
