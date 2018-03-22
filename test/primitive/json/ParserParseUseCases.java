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

import java.io.Reader;
import java.util.List;
import java.util.Map;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

/*******************************************************************************
 * @author lukasz.bownik@gmail.com
 ******************************************************************************/
@SuppressWarnings("rawtypes")
public class ParserParseUseCases extends ParserUseCasesBase {

   /****************************************************************************
    * 
    ***************************************************************************/
   @Test
   public void throwsNullPointerException_ForNullArgument()
           throws Exception {

      try {
         new Parser().parse((Reader) null);
         fail("NullPointertException failed.");
      } catch (final NullPointerException e) {
         assertTrue(true);
      }
      try {
         new Parser().parse((String) null);
         fail("NullPointertException failed.");
      } catch (final NullPointerException e) {
         assertTrue(true);
      }
   }
   /****************************************************************************
    * 
    ***************************************************************************/
   @Test
   public void returnsEmptyMap_ForEmptyObject()
           throws Exception {

      assertEmptyMap("{}");
      assertEmptyMap("{  \r\t\n}");
      assertEmptyMap("    {}");
   }
   /****************************************************************************
    * 
    ***************************************************************************/
   @Test
   public void throwsEOException_ForUnfinishedEmptyObject()
           throws Exception {

      assertEOF("{");
   }
   /****************************************************************************
    * 
    ***************************************************************************/
   @Test
   public void returnsEmptyList_ForEmptyArray()
           throws Exception {

      assertEmptyList("[]");
      assertEmptyList("[   \r\t\n]");
      assertEmptyList("   []");
   }
   /****************************************************************************
    * 
    ***************************************************************************/
   @Test
   public void throwsEOException_ForUnfinishedEmptyArray()
           throws Exception {

      assertEOF("[");
      assertEOF("[null");
   }
   /****************************************************************************
    * 
    ***************************************************************************/
   @Test
   public void returnsNull_ForProperInput()
           throws Exception {

      List result = (List) parse("[null]");

      assertEquals(1, result.size());
      assertNull(result.get(0));

      result = (List) parse("[   null   ]");

      assertEquals(1, result.size());
      assertNull(result.get(0));
   }
   /****************************************************************************
    * 
    ***************************************************************************/
   @Test
   public void throwsEOF_ForUnfinishedNull()
           throws Exception {

      assertEOF("[n");
      assertEOF("[nu");
      assertEOF("[nul");
      assertEOF("[null");
   }
   /****************************************************************************
    * 
    ***************************************************************************/
   @Test
   public void throwsUnexpected_ForUnexpectedCharacterInNull()
           throws Exception {

      assertUnexpected("[nx", 'x');
      assertUnexpected("[nux", 'x');
      assertUnexpected("[nulx", 'x');
      assertUnexpected("[nullx", 'x');

      assertUnexpected("[n ", ' ');
      assertUnexpected("[nu ", ' ');
      assertUnexpected("[nul ", ' ');
   }
   /****************************************************************************
    * 
    ***************************************************************************/
   @Test
   public void returnsTrue_ForProperInput()
           throws Exception {

      List result = (List) parse("[true]");

      assertEquals(1, result.size());
      assertEquals(Boolean.TRUE, result.get(0));

      result = (List) parse("[  true   ]");

      assertEquals(1, result.size());
      assertEquals(Boolean.TRUE, result.get(0));
   }
   /****************************************************************************
    * 
    ***************************************************************************/
   @Test
   public void throwsEOF_ForUnfinishedTrue()
           throws Exception {

      assertEOF("[t");
      assertEOF("[tr");
      assertEOF("[tru");
      assertEOF("[true");
   }
   /****************************************************************************
    * 
    ***************************************************************************/
   @Test
   public void throwsUnexpected_ForUnexpectedCharacterInTrue()
           throws Exception {

      assertUnexpected("[tx", 'x');
      assertUnexpected("[trx", 'x');
      assertUnexpected("[trux", 'x');
      assertUnexpected("[truex", 'x');

      assertUnexpected("[t ", ' ');
      assertUnexpected("[tr ", ' ');
      assertUnexpected("[tru ", ' ');
   }
   /****************************************************************************
    * 
    ***************************************************************************/
   @Test
   public void returnsFalse_ForProperInput()
           throws Exception {

      List result = (List) parse("[false]");

      assertEquals(1, result.size());
      assertEquals(Boolean.FALSE, result.get(0));

      result = (List) parse("[   false  ]");

      assertEquals(1, result.size());
      assertEquals(Boolean.FALSE, result.get(0));
   }
   /****************************************************************************
    * 
    ***************************************************************************/
   @Test
   public void throwsEOF_ForUnfinishedFales()
           throws Exception {

      assertEOF("[f");
      assertEOF("[fa");
      assertEOF("[fal");
      assertEOF("[fals");
      assertEOF("[false");
   }
   /****************************************************************************
    * 
    ***************************************************************************/
   @Test
   public void throwsUnexpected_ForUnexpectedCharacterInFalse()
           throws Exception {

      assertUnexpected("[fx", 'x');
      assertUnexpected("[fax", 'x');
      assertUnexpected("[falx", 'x');
      assertUnexpected("[falsx", 'x');
      assertUnexpected("[falsex", 'x');

      assertUnexpected("[f ", ' ');
      assertUnexpected("[fa ", ' ');
      assertUnexpected("[fal ", ' ');
      assertUnexpected("[fals ", ' ');
   }

   /****************************************************************************
    * 
    ***************************************************************************/
   @Test
   public void returnsInteger_ForProperInput()
           throws Exception {

      assertLongEquals(0L, "[0]");
      assertLongEquals(0L, "[-0]");

      assertLongEquals(0L, "[  0  ]");
      assertLongEquals(0L, "[  -0  ]");

      assertLongEquals(1L, "[1]");
      assertLongEquals(-1L, "[-1]");

      assertLongEquals(123478900001L, "[123478900001]");
      assertLongEquals(-123478900001L, "[-123478900001]");

      assertLongEquals(1 * 10, "[1E1]");
      assertLongEquals(1 * 10, "[1e1]");

      assertLongEquals(1 * 10, "[1E+1]");
      assertLongEquals(1 * 10, "[1e+1]");

      assertLongEquals(1, "[1E0]");
      assertLongEquals(1, "[1e0]");

      assertLongEquals(1, "[1E-0]");
      assertLongEquals(1, "[1e-0]");

      assertLongEquals(1, "[1E+0]");
      assertLongEquals(1, "[1e+0]");

      assertLongEquals(120000000000L, "[12E10]");
      assertLongEquals(120000000000L, "[12e10]");

      assertLongEquals(-120000000000L, "[-12E10]");
      assertLongEquals(-120000000000L, "[-12e10]");

      assertLongEquals(0, "[0E0]");
      assertLongEquals(0, "[0e0]");

      assertLongEquals(0, "[-0E0]");
      assertLongEquals(0, "[-0e0]");

      assertLongEquals(0, "[0E-0]");
      assertLongEquals(0, "[0e-0]");

      assertLongEquals(0, "[-0E-0]");
      assertLongEquals(0, "[-0e-0]");

      assertLongEquals(0, "[-0E+0]");
      assertLongEquals(0, "[-0e+0]");
   }

   /****************************************************************************
    * 
    ***************************************************************************/
   @Test
   public void throwsEOF_ForUnfinishedInteger()
           throws Exception {

      assertEOF("[1");
      assertEOF("[-");
      assertEOF("[1E");
      assertEOF("[1e");
      assertEOF("[1E-");
      assertEOF("[1e-");
      assertEOF("[1E+");
      assertEOF("[1e+");
   }
   /****************************************************************************
    * 
    ***************************************************************************/
   @Test
   public void throwsUnexpected_ForUnexpectedCharacterInInteger()
           throws Exception {

      assertUnexpected("[-]", ']');
      assertUnexpected("[1E]", ']');
      assertUnexpected("[1e]", ']');
      assertUnexpected("[1E-]", ']');
      assertUnexpected("[1e-]", ']');
      assertUnexpected("[1E+]", ']');
      assertUnexpected("[1e+]", ']');
   }
   /****************************************************************************
    * 
    ***************************************************************************/
   @Test
   public void returnsDouble_ForProperInput()
           throws Exception {

      assertDoubleEquals(0.1, "[1E-1]");
      assertDoubleEquals(0.1, "[1e-1]");

      assertDoubleEquals(-0.1, "[-1E-1]");
      assertDoubleEquals(-0.1, "[-1e-1]");

      assertDoubleEquals(1.0, "[1.0]");
      assertDoubleEquals(-1.0, "[-1.0]");

      assertDoubleEquals(0.0, "[0.0]");
      assertDoubleEquals(-0.0, "[-0.0]");

      assertDoubleEquals(123243324.43434, "[123243324.43434]");
      assertDoubleEquals(-123243324.43434, "[-123243324.43434]");

      assertDoubleEquals(1.0, "[1.0E0]");
      assertDoubleEquals(1.0, "[1.0E+0]");
      assertDoubleEquals(1.0, "[1.0E-0]");

      assertDoubleEquals(1.0, "[1.0e0]");
      assertDoubleEquals(1.0, "[1.0e+0]");
      assertDoubleEquals(1.0, "[1.0e-0]");

      assertDoubleEquals(-1.0, "[-1.0E0]");
      assertDoubleEquals(-1.0, "[-1.0E+0]");
      assertDoubleEquals(-1.0, "[-1.0E-0]");

      assertDoubleEquals(-1.0, "[-1.0e0]");
      assertDoubleEquals(-1.0, "[-1.0e+0]");
      assertDoubleEquals(-1.0, "[-1.0e-0]");

      assertDoubleEquals(0.0, "[0.0E0]");
      assertDoubleEquals(0.0, "[0.0E+0]");
      assertDoubleEquals(0.0, "[0.0E-0]");

      assertDoubleEquals(0.0, "[0.0e0]");
      assertDoubleEquals(0.0, "[0.0e+0]");
      assertDoubleEquals(0.0, "[0.0e-0]");

      assertDoubleEquals(-0.0, "[-0.0E0]");
      assertDoubleEquals(-0.0, "[-0.0E+0]");
      assertDoubleEquals(-0.0, "[-0.0E-0]");

      assertDoubleEquals(-0.0, "[-0.0e0]");
      assertDoubleEquals(-0.0, "[-0.0e+0]");
      assertDoubleEquals(-0.0, "[-0.0e-0]");

      assertDoubleEquals(120000000000.0, "[12.0E10]");
      assertDoubleEquals(120000000000.0, "[12.0E+10]");
      assertDoubleEquals(120000000000.0, "[12.0e10]");
      assertDoubleEquals(120000000000.0, "[12.0e+10]");

      assertDoubleEquals(-120000000000.0, "[-12.0E10]");
      assertDoubleEquals(-120000000000.0, "[-12.0E+10]");
      assertDoubleEquals(-120000000000.0, "[-12.0e10]");
      assertDoubleEquals(-120000000000.0, "[-12.0e+10]");

      assertDoubleEquals(0.0000000012, "[12.0E-10]");
      assertDoubleEquals(0.0000000012, "[12.0e-10]");
      assertDoubleEquals(-0.0000000012, "[-12.0E-10]");
      assertDoubleEquals(-0.0000000012, "[-12.0e-10]");
   }
   /****************************************************************************
    * 
    ***************************************************************************/
   @Test
   public void throwsEOf_ForUnfinishedDouble()
           throws Exception {

      assertEOF("[1.");
      assertEOF("[1.0E");
      assertEOF("[1.0e");
      assertEOF("[1.0E-");
      assertEOF("[1.0e-");
      assertEOF("[1.0E+");
      assertEOF("[1.0e+");
   }
   /****************************************************************************
    * 
    ***************************************************************************/
   @Test
   public void throwsUnexpected_ForUnexpectedCharacterInDouble()
           throws Exception {

      assertUnexpected("[.]", '.');
      assertUnexpected("[-.]", '.');
      assertUnexpected("[1.]", ']');
      assertUnexpected("[1.e]", 'e');
      assertUnexpected("[1.E]", 'E');
      assertUnexpected("[1.0-]", '-');
      assertUnexpected("[1.0E]", ']');
      assertUnexpected("[1.0e]", ']');
      assertUnexpected("[1.0E+]", ']');
      assertUnexpected("[1.0e+]", ']');
      assertUnexpected("[1.0E-]", ']');
      assertUnexpected("[1.0e-]", ']');
      assertUnexpected("[1.0E-1x", 'x');
      assertUnexpected("[1.0e-1x", 'x');
   }
   /****************************************************************************
    * 
    ***************************************************************************/
   @Test
   public void returnString_ForProperInput()
           throws Exception {

      assertStringEquals("", "[\"\"]");
      assertStringEquals(" ", "[\" \"]");
      assertStringEquals("\b", "[\"\b\"]");
      assertStringEquals("\f", "[\"\f\"]");
      assertStringEquals("\r", "[\"\r\"]");
      assertStringEquals("\n", "[\"\n\"]");
      assertStringEquals("\t", "[\"\t\"]");

      assertStringEquals("\\", "[\"\\\\\"]");
      assertStringEquals("\b", "[\"\\b\"]");
      assertStringEquals("\f", "[\"\\f\"]");
      assertStringEquals("\r", "[\"\\r\"]");
      assertStringEquals("\n", "[\"\\n\"]");
      assertStringEquals("\t", "[\"\\t\"]");
      assertStringEquals("\b\f\n\r\t", "[\"\\b\\f\\n\\r\\t\"]");

      assertStringEquals("bfnrt", "[\"bfnrt\"]");
      assertStringEquals("'", "[\"'\"]");
      assertStringEquals("\"", "[\"\\\"\"]");
      assertStringEquals("/", "[\"\\/\"]");

      assertStringEquals("abcśżą", "[\"abcśżą\"]");
      assertStringEquals("!@#$%^&*()_-+=~`.,;:'[]{}|/?", "[\"!@#$%^&*()_-+=~`.,;:'[]{}|/?\"]");
      assertStringEquals("ą", "[\"\\u0105\"]");
      assertStringEquals("ą", "[\"\\u0105\"]");
      assertStringEquals("bąb", "[\"b\\u0105b\"]");
      assertStringEquals("ą", "[\"\\u0105\"]");
      assertStringEquals("bśb", "[\"b\\u015Bb\"]");
      assertStringEquals("bśb", "[\"b\\u015bb\"]");
      assertStringEquals("http://feedburner.google.com/fb/a/mailverify?uri=JavaCodeGeeks&loc=en_US",
              "[\"http://feedburner.google.com/fb/a/mailverify?uri=JavaCodeGeeks&loc=en_US\"]");
   }
   /****************************************************************************
    * 
    ***************************************************************************/
   @Test
   public void throwsEOf_ForUnfinishedString()
           throws Exception {

      assertEOF("[\"");
      assertEOF("[\" ");
      assertEOF("[\" ");
      assertEOF("[\"\b");
      assertEOF("[\"\f");
      assertEOF("[\"\r");
      assertEOF("[\"\n");
      assertEOF("[\"\t");
      assertEOF("[\"\\\\]");
      assertEOF("[\"\\b");
      assertEOF("[\"\\f");
      assertEOF("[\"\\r");
      assertEOF("[\"\\n");
      assertEOF("[\"\\t");
      assertEOF("[\"\\u");

      assertEOF("[\"\\");
      assertEOF("[\"\\\\");
      assertEOF("[\"\\/");
      assertEOF("[\"\\\"");

      assertEOF("[\"\\u0");
      assertEOF("[\"\\u00");
      assertEOF("[\"\\u000");
      assertEOF("[\"\\u0000");

      assertEOF("[\"b");
      assertEOF("[\"ś");
      assertEOF("[\"!");
      assertEOF("[\"'");
   }
   /****************************************************************************
    * 
    ***************************************************************************/
   @Test
   public void throwsUnexpected_ForUnexpectedCharacterInString()
           throws Exception {

      assertUnexpected("[\"\\x", 'x');
      assertUnexpected("[\"\\:", ':');
      assertUnexpected("[\"\\ ", ' ');
      assertUnexpected("[\"\\ś", 'ś');
      
      assertUnexpected("[\"\\ux", 'x');
      assertUnexpected("[\"\\u0x", 'x');
      assertUnexpected("[\"\\u00x", 'x');
      assertUnexpected("[\"\\u000x", 'x');
   }
//   /****************************************************************************
//    * 
//    ***************************************************************************/
//   @Test
//   public void testProperArrays() throws Exception {
//
//      ArrayList result;
//      // without whitespace
//      assertEquals(new ArrayList(), parse("[]"));
//      result = (ArrayList) parse("[12]");
//      assertEquals(1, result.size());
//      assertEquals(new Long(12), result.get(0));
//      result = (ArrayList) parse("[12,12.0E-10]");
//      assertEquals(2, result.size());
//      assertEquals(new Long(12), result.get(0));
//      assertEquals(new Double(0.0000000012), result.get(1));
//      result = (ArrayList) parse("[\"\"]");
//      assertEquals(1, result.size());
//      assertEquals("", result.get(0));
//      result = (ArrayList) parse("[\"\",\"12.0E-10\"]");
//      assertEquals(2, result.size());
//      assertEquals("", result.get(0));
//      assertEquals("12.0E-10", result.get(1));
//      result = (ArrayList) parse("[true]");
//      assertEquals(1, result.size());
//      assertEquals(Boolean.TRUE, result.get(0));
//      result = (ArrayList) parse("[true,true]");
//      assertEquals(2, result.size());
//      assertEquals(Boolean.TRUE, result.get(0));
//      assertEquals(Boolean.TRUE, result.get(1));
//      result = (ArrayList) parse("[false]");
//      assertEquals(1, result.size());
//      assertEquals(Boolean.FALSE, result.get(0));
//      result = (ArrayList) parse("[false,false]");
//      assertEquals(2, result.size());
//      assertEquals(Boolean.FALSE, result.get(0));
//      assertEquals(Boolean.FALSE, result.get(1));
//      result = (ArrayList) parse("[null]");
//      assertEquals(1, result.size());
//      assertNull(result.get(0));
//      result = (ArrayList) parse("[null,null]");
//      assertEquals(2, result.size());
//      assertNull(result.get(0));
//      assertNull(result.get(1));
//      result = (ArrayList) parse("[[]]");
//      assertEquals(1, result.size());
//      assertEquals(new ArrayList(), result.get(0));
//      result = (ArrayList) parse("[[],[]]");
//      assertEquals(2, result.size());
//      assertEquals(new ArrayList(), result.get(0));
//      assertEquals(new ArrayList(), result.get(1));
//      result = (ArrayList) parse("[{}]");
//      assertEquals(1, result.size());
//      assertEquals(new HashMap(), result.get(0));
//      result = (ArrayList) parse("[{},{}]");
//      assertEquals(2, result.size());
//      assertEquals(new HashMap(), result.get(0));
//      assertEquals(new HashMap(), result.get(1));
//      result = (ArrayList) parse("[[[]]]");
//      assertEquals(1, result.size());
//      result = (ArrayList) result.get(0);
//      assertEquals(1, result.size());
//      assertEquals(new ArrayList(), result.get(0));
//
//      // with whitespace
//      assertEquals(new ArrayList(), parse("[ ]"));
//      result = (ArrayList) parse("[ 12 ]");
//      assertEquals(1, result.size());
//      assertEquals(new Long(12), result.get(0));
//      result = (ArrayList) parse("[ 12 , 12.0E-10 ]");
//      assertEquals(2, result.size());
//      assertEquals(new Long(12), result.get(0));
//      assertEquals(new Double(0.0000000012), result.get(1));
//      result = (ArrayList) parse("[ \"\" ]");
//      assertEquals(1, result.size());
//      assertEquals("", result.get(0));
//      result = (ArrayList) parse("[ \"\" , \"12.0E-10\" ]");
//      assertEquals(2, result.size());
//      assertEquals("", result.get(0));
//      assertEquals("12.0E-10", result.get(1));
//      result = (ArrayList) parse("[ true ]");
//      assertEquals(1, result.size());
//      assertEquals(Boolean.TRUE, result.get(0));
//      result = (ArrayList) parse("[ true   , true ]");
//      assertEquals(2, result.size());
//      assertEquals(Boolean.TRUE, result.get(0));
//      assertEquals(Boolean.TRUE, result.get(1));
//      result = (ArrayList) parse("[ false   ]");
//      assertEquals(1, result.size());
//      assertEquals(Boolean.FALSE, result.get(0));
//      result = (ArrayList) parse("[ false  , false ]");
//      assertEquals(2, result.size());
//      assertEquals(Boolean.FALSE, result.get(0));
//      assertEquals(Boolean.FALSE, result.get(1));
//      result = (ArrayList) parse("[   null ]");
//      assertEquals(1, result.size());
//      assertNull(result.get(0));
//      result = (ArrayList) parse("[ null , null ]");
//      assertEquals(2, result.size());
//      assertNull(result.get(0));
//      assertNull(result.get(1));
//      result = (ArrayList) parse("[ [ ] ]");
//      assertEquals(1, result.size());
//      assertEquals(new ArrayList(), result.get(0));
//      result = (ArrayList) parse("[   [   ] , [ ] ]");
//      assertEquals(2, result.size());
//      assertEquals(new ArrayList(), result.get(0));
//      assertEquals(new ArrayList(), result.get(1));
//      result = (ArrayList) parse("[ { } ]");
//      assertEquals(1, result.size());
//      assertEquals(new HashMap(), result.get(0));
//      result = (ArrayList) parse("[ {  } , {  } ]");
//      assertEquals(2, result.size());
//      assertEquals(new HashMap(), result.get(0));
//      assertEquals(new HashMap(), result.get(1));
//      result = (ArrayList) parse("[  [ [    ] ]   ]");
//      assertEquals(1, result.size());
//      result = (ArrayList) result.get(0);
//      assertEquals(1, result.size());
//      assertEquals(new ArrayList(), result.get(0));
//
//   }
//
//   /****************************************************************************
//    * 
//    ***************************************************************************/
//   @Test
//   public void testErronousArrays() throws Exception {
//
//      //nulll
//      assertUnexpected("[,", ',');
//      assertUnexpected("[ :", ':');
//      assertUnexpected("[ }", '}');
//      assertUnexpected("[ 1,]", ']');
//   }
//
//   /****************************************************************************
//    * 
//    ***************************************************************************/
//   @Test
//   public void testEOFArrays() throws Exception {
//
//      assertEOF("[");
//      assertEOF("[2");
//      assertEOF("[2,");
//   }
//   /****************************************************************************
//    * 
//    ***************************************************************************/
//   @Test
//   public void testDuplicatedKey() throws Exception {
//
//      try {
//         parse("{\"a\":1,\"a\":2}");
//         fail();
//      } catch (final DuplicatedKeyException e) {
//         assertEquals("a", e.key);
//      }
//   }
//
//   /****************************************************************************
//    * 
//    ***************************************************************************/
//   @Test
//   public void testProperObjects() throws Exception {
//
//      HashMap result;
//      // without whitespace
//      assertEquals(new HashMap(), parse("{}"));
//      result = (HashMap) parse("{\"abc\":true}");
//      assertEquals(1, result.size());
//      assertEquals(Boolean.TRUE, result.get("abc"));
//      result = (HashMap) parse("{\"abc\":false}");
//      assertEquals(1, result.size());
//      assertEquals(Boolean.FALSE, result.get("abc"));
//      result = (HashMap) parse("{\"abc\":null}");
//      assertEquals(1, result.size());
//      assertNull(result.get("abc"));
//      result = (HashMap) parse("{\"abc\":12}");
//      assertEquals(1, result.size());
//      assertEquals(new Long(12), result.get("abc"));
//      result = (HashMap) parse("{\"abc\":-12.0e10}");
//      assertEquals(1, result.size());
//      assertEquals(new Double(-120000000000.0), result.get("abc"));
//      result = (HashMap) parse("{\"abc\":\"\"}");
//      assertEquals(1, result.size());
//      assertEquals("", result.get("abc"));
//      result = (HashMap) parse("{\"\":\"\"}");
//      assertEquals(1, result.size());
//      assertEquals("", result.get(""));
//      result = (HashMap) parse("{\"abc\":{}}");
//      assertEquals(1, result.size());
//      assertEquals(new HashMap(), result.get("abc"));
//      result = (HashMap) parse("{\"abc\":[]}");
//      assertEquals(1, result.size());
//      assertEquals(new ArrayList(), result.get("abc"));
//      result = (HashMap) parse("{\"abc\":true,\"def\":true}");
//      assertEquals(2, result.size());
//      assertEquals(Boolean.TRUE, result.get("abc"));
//      assertEquals(Boolean.TRUE, result.get("def"));
//      result = (HashMap) parse("{\"abc\":false,\"def\":false}");
//      assertEquals(2, result.size());
//      assertEquals(Boolean.FALSE, result.get("abc"));
//      assertEquals(Boolean.FALSE, result.get("def"));
//      result = (HashMap) parse("{\"abc\":null,\"def\":null}");
//      assertEquals(2, result.size());
//      assertNull(result.get("abc"));
//      assertNull(result.get("def"));
//      result = (HashMap) parse("{\"abc\":12,\"def\":12}");
//      assertEquals(2, result.size());
//      assertEquals(new Long(12), result.get("abc"));
//      assertEquals(new Long(12), result.get("def"));
//      result = (HashMap) parse("{\"abc\":-12.0e10,\"def\":-12.0e10}");
//      assertEquals(2, result.size());
//      assertEquals(new Double(-120000000000.0), result.get("abc"));
//      assertEquals(new Double(-120000000000.0), result.get("def"));
//      result = (HashMap) parse("{\"abc\":\"\",\"def\":\"\"}");
//      assertEquals(2, result.size());
//      assertEquals("", result.get("abc"));
//      assertEquals("", result.get("def"));
//      result = (HashMap) parse("{\"abc\":{},\"def\":{}}");
//      assertEquals(2, result.size());
//      assertEquals(new HashMap(), result.get("abc"));
//      assertEquals(new HashMap(), result.get("def"));
//      result = (HashMap) parse("{\"abc\":[],\"def\":[]}");
//      assertEquals(2, result.size());
//      assertEquals(new ArrayList(), result.get("abc"));
//      assertEquals(new ArrayList(), result.get("def"));
//
//      // with whitespace
//      assertEquals(new HashMap(), parse("{  }"));
//      result = (HashMap) parse("{ \"abc\" : true }");
//      assertEquals(1, result.size());
//      assertEquals(Boolean.TRUE, result.get("abc"));
//      result = (HashMap) parse("{ \"abc\" : false }");
//      assertEquals(1, result.size());
//      assertEquals(Boolean.FALSE, result.get("abc"));
//      result = (HashMap) parse("{  \"abc\"    :  null  }");
//      assertEquals(1, result.size());
//      assertNull(result.get("abc"));
//      result = (HashMap) parse("{ \"abc\"  :  12 }");
//      assertEquals(1, result.size());
//      assertEquals(new Long(12), result.get("abc"));
//      result = (HashMap) parse("{ \"abc\" : -12.0e10 }");
//      assertEquals(1, result.size());
//      assertEquals(new Double(-120000000000.0), result.get("abc"));
//      result = (HashMap) parse("{ \"abc\" : \"\" }");
//      assertEquals(1, result.size());
//      assertEquals("", result.get("abc"));
//      result = (HashMap) parse("{ \"\" :   \"\" }");
//      assertEquals(1, result.size());
//      assertEquals("", result.get(""));
//      result = (HashMap) parse("{ \"abc\" : {   } \n\t}");
//      assertEquals(1, result.size());
//      assertEquals(new HashMap(), result.get("abc"));
//      result = (HashMap) parse("{ \"abc\": [ ] }");
//      assertEquals(1, result.size());
//      assertEquals(new ArrayList(), result.get("abc"));
//      result = (HashMap) parse("{  \"abc\"  : true ,\r\n\"def\" : true }");
//      assertEquals(2, result.size());
//      assertEquals(Boolean.TRUE, result.get("abc"));
//      assertEquals(Boolean.TRUE, result.get("def"));
//      result = (HashMap) parse("{ \"abc\" : false , \"def\" : false }");
//      assertEquals(2, result.size());
//      assertEquals(Boolean.FALSE, result.get("abc"));
//      assertEquals(Boolean.FALSE, result.get("def"));
//      result = (HashMap) parse("{ \"abc\" : null , \"def\": null }");
//      assertEquals(2, result.size());
//      assertNull(result.get("abc"));
//      assertNull(result.get("def"));
//      result = (HashMap) parse("{ \"abc\" : 12 , \"def\"  : 12 }");
//      assertEquals(2, result.size());
//      assertEquals(new Long(12), result.get("abc"));
//      assertEquals(new Long(12), result.get("def"));
//      result = (HashMap) parse("{  \"abc\"\r\n: -12.0e10 , \"def\" : -12.0e10  }");
//      assertEquals(2, result.size());
//      assertEquals(new Double(-120000000000.0), result.get("abc"));
//      assertEquals(new Double(-120000000000.0), result.get("def"));
//      result = (HashMap) parse("{ \"abc\" : \"\",\r\n\t\"def\" : \"\" }");
//      assertEquals(2, result.size());
//      assertEquals("", result.get("abc"));
//      assertEquals("", result.get("def"));
//      result = (HashMap) parse("{ \"abc\" : { } , \"def\" :  {  } }");
//      assertEquals(2, result.size());
//      assertEquals(new HashMap(), result.get("abc"));
//      assertEquals(new HashMap(), result.get("def"));
//      result = (HashMap) parse("{ \"abc\"  : [\r\n], \"def\" : [\t] }");
//      assertEquals(2, result.size());
//      assertEquals(new ArrayList(), result.get("abc"));
//      assertEquals(new ArrayList(), result.get("def"));
//      parse(example); // parses properly
//   }
//
//   /****************************************************************************
//    * 
//    ***************************************************************************/
//   @Test
//   public void testErronousObjects() throws Exception {
//
//      //nulll
//      assertUnexpected("{,", ',');
//      assertUnexpected("{ :", ':');
//      assertUnexpected("{ ]", ']');
//      assertUnexpected("{ \"a\":}", '}');
//      assertUnexpected("{ \"a\": \"b\",}", '}');
//   }
//
//   /****************************************************************************
//    * 
//    ***************************************************************************/
//   @Test
//   public void testEOFObjects() throws Exception {
//
//      //nulll
//      assertEOF("{");
//      assertEOF("");
//      assertEOF("{ \"a\":");
//      assertEOF("{ \"a\": \"b\",");
//   }
//
//   /****************************************************************************
//    * 
//    ***************************************************************************/
//   @Test
//   public void testNonJSON() throws IOException {
//      
//      assertUnexpected("xyz", 'x');
//   }
//   /****************************************************************************
//    * 
//    ***************************************************************************/
//   @Test
//   public void testParseFormRawSocket() throws Exception {
//
//      new Thread() {
//
//         @Override
//         public void run() {
//            try {
//               final ServerSocket ss = new ServerSocket(60400);
//               final Socket s = ss.accept();
//               final Writer out = new OutputStreamWriter(s.getOutputStream(), "UTF-8");
//               final InputStream in = s.getInputStream();
//               out.write("{\"ip\": \"8.8.8.8\"}");
//               out.flush();
//               in.read();
//               out.write("{\"ip\": \"8.8.8.8\"}");
//               out.flush();
//               out.close();
//               in.close();
//            } catch (final Exception e) {
//               throw new RuntimeException(e);
//            }
//         }
//
//      }.start();
//
//      final Socket s = new Socket("localhost", 60400);
//      final Reader in = new InputStreamReader(s.getInputStream(), "UTF-8");
//      final OutputStream out = s.getOutputStream();
//      HashMap o = null;
//      o = (HashMap) this.p.parse(in);
//      out.write(1);
//      o = (HashMap) this.p.parse(in);
//      in.close();
//      out.close();
//   }
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
