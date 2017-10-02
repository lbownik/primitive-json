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

import java.io.IOException;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/*******************************************************************************
 * @author lukasz.bownik@gmail.com
 ******************************************************************************/
public final class StringWriterTest {

   /****************************************************************************
    * 
    ***************************************************************************/
   @Test
   public void testConstructor() throws Exception {

      try {
         new StringWriter(0);
         fail();
      } catch (final IllegalArgumentException e) {
         assertTrue(true);
      }
      try {
         new StringWriter(-1);
         fail();
      } catch (final IllegalArgumentException e) {
         assertTrue(true);
      }
      new StringWriter(1);
   }

   /****************************************************************************
    * 
    ***************************************************************************/
   @Test
   public void testWriteInt() throws IOException {

      final StringWriter writer = new StringWriter(1);
      assertEquals(0, writer.getSize());
      writer.write('a');
      assertEquals(1, writer.getSize());
      assertEquals("a", writer.toString());
      writer.write('a');
      assertEquals(2, writer.getSize());
      assertEquals("aa", writer.toString());
   }

   /****************************************************************************
    * 
    ***************************************************************************/
   @Test
   public void testWriteString() throws IOException {

      final StringWriter writer = new StringWriter(1);
      assertEquals(0, writer.getSize());
      writer.write("a");
      assertEquals(1, writer.getSize());
      assertEquals("a", writer.toString());
      writer.write("aa");
      assertEquals(3, writer.getSize());
      assertEquals("aaa", writer.toString());
   }

   /****************************************************************************
    * 
    ***************************************************************************/
   @Test
   public void testWriteStringOffset() throws IOException {

      final StringWriter writer = new StringWriter(1);
      assertEquals(0, writer.getSize());
      writer.write("a", 0, 1);
      assertEquals(1, writer.getSize());
      assertEquals("a", writer.toString());
      writer.write("aa", 1, 1);
      assertEquals(2, writer.getSize());
      assertEquals("aa", writer.toString());
      writer.write("aa", 1, 0);
      assertEquals(2, writer.getSize());
      assertEquals("aa", writer.toString());
   }

   /****************************************************************************
    * 
    ***************************************************************************/
   @Test
   public void testWriteStringOffsetErrors() throws IOException {

      final StringWriter writer = new StringWriter(1);
      assertEquals(0, writer.getSize());
      try {
         writer.write("a", -1, 1);
         fail();
      } catch (final IndexOutOfBoundsException e) {
         assertTrue(true);
      }
      try {
         writer.write("a", 1, 1);
         fail();
      } catch (final IndexOutOfBoundsException e) {
         assertTrue(true);
      }
      try {
         writer.write("a", 2, 1);
         fail();
      } catch (final IndexOutOfBoundsException e) {
         assertTrue(true);
      }
      try {
         writer.write("a", 0, 2);
         fail();
      } catch (final IndexOutOfBoundsException e) {
         assertTrue(true);
      }
      try {
         writer.write("a", 1, -1);
         fail();
      } catch (final IndexOutOfBoundsException e) {
         assertTrue(true);
      }
   }

   /****************************************************************************
    * 
    ***************************************************************************/
   @Test
   public void testWriteNullString() throws IOException {

      final StringWriter writer = new StringWriter(1);
      assertEquals(0, writer.getSize());
      writer.write((String) null);
      assertEquals(4, writer.getSize());
      assertEquals("null", writer.toString());
   }

   /****************************************************************************
    * 
    ***************************************************************************/
   @Test
   public void testWriteCArrayOffset() throws IOException {

      final StringWriter writer = new StringWriter(1);
      assertEquals(0, writer.getSize());
      writer.write("a".toCharArray(), 0, 1);
      assertEquals(1, writer.getSize());
      assertEquals("a", writer.toString());
      writer.write("aa".toCharArray(), 1, 1);
      assertEquals(2, writer.getSize());
      assertEquals("aa", writer.toString());
   }

   /****************************************************************************
    * 
    ***************************************************************************/
   @Test
   public void testWriteCArrayOffsetErrors() throws IOException {

      final StringWriter writer = new StringWriter(1);
      assertEquals(0, writer.getSize());
      try {
         writer.write("a".toCharArray(), -1, 1);
         fail();
      } catch (final IndexOutOfBoundsException e) {
         assertTrue(true);
      }
      try {
         writer.write("a".toCharArray(), 1, 1);
         fail();
      } catch (final IndexOutOfBoundsException e) {
         assertTrue(true);
      }
      try {
         writer.write("a".toCharArray(), 2, 1);
         fail();
      } catch (final IndexOutOfBoundsException e) {
         assertTrue(true);
      }
      try {
         writer.write("a".toCharArray(), 0, 2);
         fail();
      } catch (final IndexOutOfBoundsException e) {
         assertTrue(true);
      }
      try {
         writer.write("a".toCharArray(), 1, -1);
         fail();
      } catch (final IndexOutOfBoundsException e) {
         assertTrue(true);
      }
   }
   /****************************************************************************
    * 
    ***************************************************************************/
}
