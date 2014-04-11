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
import junit.framework.TestCase;

/*******************************************************************************
 * @author lukasz.bownik@gmail.com
 ******************************************************************************/
public class JSONTest extends TestCase {

   /****************************************************************************
    * 
    ***************************************************************************/
   public void testToString() throws Exception {

      Vector array;
      Hashtable object;
      assertEquals("[]", JSON.toString(new Vector()));
      assertEquals("{}", JSON.toString(new Hashtable()));

      array = new Vector();
      array.addElement("ala");
      array.addElement(new Long(12));
      object = new Hashtable();
      object.put("b", new Double(1.0));
      array.addElement(object);

      assertEquals("[\"ala\",12,{\"b\":1.0}]", JSON.toString(array));

      array = new Vector();
      array.addElement("ala");
      array.addElement(new Long(12));
      object.put("c", array);
      String str = JSON.toString(object);
      assertTrue(str.equals("{\"b\":1.0,\"c\":[\"ala\",12]}")
              || str.equals("{\"c\":[\"ala\",12],\"b\":1.0}"));

      object = new Hashtable();
      object.put("a", Boolean.TRUE);
      assertEquals("{\"a\":true}", JSON.toString(object));
      object = new Hashtable();
      object.put("a", Boolean.TRUE);
      assertEquals("{\"a\":true}", JSON.toString(object));
      object = new Hashtable();
      object.put("a", Boolean.FALSE);
      assertEquals("{\"a\":false}", JSON.toString(object));
      object = new Hashtable();
      object.put("a", Null.value);
      assertEquals("{\"a\":null}", JSON.toString(object));
      object.put("a", "\r\n\t\b");
      assertEquals("{\"a\":\"\\r\\n\\t\\b\"}", JSON.toString(object));
   }

   /****************************************************************************
    * 
    ***************************************************************************/
   public void testToByteArray() throws Exception {

      try {
         JSON.toByteArray(new Hashtable(), null);
         fail();
      } catch (final NullPointerException e) {
         assertTrue(true);
      }
      try {
         JSON.toByteArray(new Hashtable(), "");
         fail();
      } catch (final RuntimeException e) {
         assertTrue(true);
      }
      try {
         JSON.toByteArray(new Hashtable(), "klklk");
         fail();
      } catch (final RuntimeException e) {
         assertTrue(true);
      }
   }

   /****************************************************************************
    * 
    ***************************************************************************/
   public void testParseFormURL() throws Exception {

      final HttpURLConnection c
              = (HttpURLConnection) new URL("http://headers.jsontest.com/").openConnection();
      c.setDoInput(true);
      c.setRequestMethod("GET");
      c.connect();
      final Reader r = new InputStreamReader(c.getInputStream(), "UTF-8");
      try {
         final Hashtable o = (Hashtable) JSON.parse(r);
         System.out.println(JSON.toString(o));
      } finally {
         r.close();
      }

   }

   /****************************************************************************
    * 
    ***************************************************************************/
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
      Hashtable o = null;
      System.out.println("connected");
      o = (Hashtable) JSON.parse(in);
      System.out.println(JSON.toString(o));
      out.write(1);
      o = (Hashtable) JSON.parse(in);
      System.out.println(JSON.toString(o));
      in.close();
      out.close();
   }
}
