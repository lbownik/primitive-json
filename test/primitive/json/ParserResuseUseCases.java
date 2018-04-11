package primitive.json;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import org.junit.Test;
import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;

/*******************************************************************************
 *
 * @author
 ******************************************************************************/
public class ParserResuseUseCases extends ParserUseCasesBase {

   /****************************************************************************
    * 
    ***************************************************************************/
   @Test
   public void worksProperly_WhenInvokedOverSocket() throws Exception {

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
      o = (HashMap) new Parser().parse(in);
      out.write(1);
      o = (HashMap) new Parser().parse(in);
      in.close();
      out.close();
   }
   /****************************************************************************
    * 
    ***************************************************************************/
   @Test
   public void worksProperly_WhenReused() throws Exception {

      final Parser p = new Parser();

      assertEquals(asList("a", Boolean.TRUE, null), p.parse("[\"a\", true, null]"));
      assertEquals(asMap("b", Boolean.FALSE), p.parse("{\"b\": false}"));
      assertEquals(asList("a", Boolean.TRUE, null), p.parse("[\"a\", true, null]"));
      assertEquals(asMap("b", Boolean.FALSE), p.parse("{\"b\": false}"));
   }
}
