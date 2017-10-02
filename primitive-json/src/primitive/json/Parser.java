package primitive.json;

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
//-----------------------------------------------------------------------------
import java.io.EOFException;
import java.io.IOException;
import java.io.Reader;
import java.util.HashMap;
import java.util.ArrayList;

/*******************************************************************************
 * JSON parser. This class is not thread safe but can be reused for parsing 
 * consecutive JSON messages one by one.
 * This parser represents JSON as:
 *   <ul>
 *   <li>java.util.HashMap,</li>
 *   <li>java.util.ArrayList,</li>
 *   <li>java.lang.String,</li>
 *   <li>java.lang.Long,</li>
 *   <li>java.lang.Double,</li>
 *   <li>java.lang.Boolean,</li>
 *   <li>null</li>
 *   </ul>
 * @author lukasz.bownik@gmail.com
 ******************************************************************************/
public final class Parser {

   /****************************************************************************
    * Creates a new parser.
    ***************************************************************************/
   public Parser() {

      this.buffer = new char[16];
      this.bufferSize = this.buffer.length;
      this.initialHashtableSize = 10;
      this.initialVectorSize = 10;
   }

   /****************************************************************************
    * Creates a new parser.
    * @param initialBufferSize initial internal buffer size for string values. 
    * Setting this parameter to a predicted maximum string value length helps 
    * avoiding beffer realocations while parsing. If in doubt, use 15. 
    * This paramater exists only for performance optimization purposes.
    * @param initialHashtableSize initial java.util.Hashtable size. 
    * Setting this parameter to a predicted maximum count of object members helps 
    * avoiding realocations and rehashing while parsing. If in doubt, use 10. 
    * This paramater exists only for performance optimization purposes.
    * @param initialVectorSize initial java.util.Vector size. 
    * Setting this parameter to a predicted maximum count of array elements helps 
    * avoiding realocations while parsing. If in doubt, use 10. 
    * This paramater exists only for performance optimization purposes.
    * @throws java.lang.IllegalArgumentException if initialBufferSize <= 0.
    ***************************************************************************/
   public Parser(final int initialBufferSize, final int initialHashtableSize,
           final int initialVectorSize) {

      if (initialBufferSize <= 0) {
         throw new IllegalArgumentException("initialBufferSize <= 0");
      }
      if (initialHashtableSize <= 0) {
         throw new IllegalArgumentException("initialHashtableSize <= 0");
      }
      if (initialVectorSize <= 0) {
         throw new IllegalArgumentException("initialVectorSize <= 0");
      }
      this.buffer = new char[initialBufferSize];
      this.bufferSize = this.buffer.length;
      this.initialHashtableSize = initialHashtableSize;
      this.initialVectorSize = initialVectorSize;
   }

   /****************************************************************************
    * Parse JSON object.
    * @param reader a reader object.
    * @return java.util.Hashtable if the reader contained JSON object or 
    *    java.util.Vector if the reader contained JSON array.
    * @throws IOException if input error occurs.
    * @throws primitive.json.UnexpectedCharacterException if malformed JSON is
    * encountered.
    * @throws primitive.json.DuplicatedKeyException if JSON object with two 
    * same kays is encountered
    * @throws NullPointerException if reader is null.
    ***************************************************************************/
   public Object parse(final Reader reader) throws IOException {

      this.reader = reader;
      try {
         this.position = -1;
         final int currentChar = read();
         switch (currentChar) {
            case '{':
               return parseObject();
            case '[':
               return parseArray();
            case -1:
               throw new EOFException();
            default:
               throwUnexpected(currentChar);
               return null;
         }
      } finally {
         this.reader = null;
      }
   }

   /****************************************************************************
    * Parse JSON object.
    * @param str a JSON string.
    * @return java.util.Hashtable if the string containes JSON object or 
    *    java.util.Vector if the string containes JSON array.
    * @throws IOException if input error occurs.
    * @throws primitive.json.UnexpectedCharacterException if malformed JSON is
    * encountered.
    * @throws primitive.json.DuplicatedKeyException if JSON object with two 
    * same kays is encountered
    * encountered.
    * @throws NullPointerException if reader is null.
    ***************************************************************************/
   public Object parse(final String str) throws IOException {

      return parse(new StringReader(str));
   }

   /****************************************************************************
    * 
    ***************************************************************************/
   private Object parseValue(int currentChar) throws IOException {

      switch (currentChar) {
         case '{': {
            final Object o = parseObject();
            this.recentChar = consumeWhitespace(read());
            return o;
         }
         case '[': {
            final Object o = parseArray();
            this.recentChar = consumeWhitespace(read());
            return o;
         }
         case 't':
            return parseTrue();
         case 'f':
            return parseFalse();
         case 'n':
            return parseNull();
         case '-':
         case '1':
         case '2':
         case '3':
         case '4':
         case '5':
         case '6':
         case '7':
         case '8':
         case '9':
         case '0':
            return parseNumber(currentChar);
         case '"':
            return parseString();
         case -1:
            throw new EOFException();
         default:
            throwUnexpected(currentChar);
            return null;
      }
   }

   /****************************************************************************
    * 
    ***************************************************************************/
   private Boolean parseTrue() throws IOException {

      int currentChar = read();
      if (currentChar == -1) {
         throw new EOFException();
      }
      if (currentChar != 'r') {
         throwUnexpected(currentChar);
      }
      currentChar = read();
      if (currentChar == -1) {
         throw new EOFException();
      }
      if (currentChar != 'u') {
         throwUnexpected(currentChar);
      }
      currentChar = read();
      if (currentChar == -1) {
         throw new EOFException();
      }
      if (currentChar != 'e') {
         throwUnexpected(currentChar);
      }
      currentChar = read();
      if (!isEndOfValue(currentChar)) {
         throwUnexpected(currentChar);
      }
      this.recentChar = currentChar;
      return Boolean.TRUE;
   }

   /****************************************************************************
    * 
    ***************************************************************************/
   private Boolean parseFalse() throws IOException {

      int currentChar = read();
      if (currentChar == -1) {
         throw new EOFException();
      }
      if (currentChar != 'a') {
         throwUnexpected(currentChar);
      }
      currentChar = read();
      if (currentChar == -1) {
         throw new EOFException();
      }
      if (currentChar != 'l') {
         throwUnexpected(currentChar);
      }
      currentChar = read();
      if (currentChar == -1) {
         throw new EOFException();
      }
      if (currentChar != 's') {
         throwUnexpected(currentChar);
      }
      currentChar = read();
      if (currentChar == -1) {
         throw new EOFException();
      }
      if (currentChar != 'e') {
         throwUnexpected(currentChar);
      }
      currentChar = read();
      if (!isEndOfValue(currentChar)) {
         throwUnexpected(currentChar);
      }
      this.recentChar = currentChar;
      return Boolean.FALSE;
   }

   /****************************************************************************
    * 
    ***************************************************************************/
   private Object parseNull() throws IOException {

      int currentChar = read();
      if (currentChar == -1) {
         throw new EOFException();
      }
      if (currentChar != 'u') {
         throwUnexpected(currentChar);
      }
      currentChar = read();
      if (currentChar == -1) {
         throw new EOFException();
      }
      if (currentChar != 'l') {
         throwUnexpected(currentChar);
      }
      currentChar = read();
      if (currentChar == -1) {
         throw new EOFException();
      }
      if (currentChar != 'l') {
         throwUnexpected(currentChar);
      }
      currentChar = read();
      if (!isEndOfValue(currentChar)) {
         throwUnexpected(currentChar);
      }
      this.recentChar = currentChar;
      return null;
   }

   /****************************************************************************
    * 
    ***************************************************************************/
   private HashMap<String, Object> parseObject() throws IOException {

      final HashMap<String, Object> result = new HashMap<>(this.initialHashtableSize);
      int currentChar = consumeWhitespace(read());
      if (currentChar == -1) {
         throw new EOFException();
      }
      if (currentChar != '}') {
         loop:
         for (;;) {
            if (currentChar != '"') {
               throwUnexpected(currentChar);
            }
            final String key = parseString();
            currentChar = consumeWhitespace(this.recentChar);
            if (currentChar == -1) {
               throw new EOFException();
            }
            if (currentChar != ':') {
               throwUnexpected(currentChar);
            }
            currentChar = read();
            currentChar = consumeWhitespace(currentChar);
            if (currentChar == -1) {
               throw new EOFException();
            }
            if (result.put(key, parseValue(currentChar)) != null) {
               throw new DuplicatedKeyException(key);
            }
            currentChar = consumeWhitespace(this.recentChar);
            switch (currentChar) {
               case -1:
                  throw new EOFException();
               case ',':
                  currentChar = read();
                  currentChar = consumeWhitespace(currentChar);
                  if (currentChar == -1) {
                     throw new EOFException();
                  }
                  break;
               case '}':
                  break loop;
               default:
                  throwUnexpected(currentChar);
            }
         }
      }
      return result;
   }

   /****************************************************************************
    * 
    ***************************************************************************/
   private ArrayList<Object> parseArray() throws IOException {

      final ArrayList<Object> result = new ArrayList<>(this.initialVectorSize);
      int currentChar = read();
      currentChar = consumeWhitespace(currentChar);
      if (currentChar == -1) {
         throw new EOFException();
      }
      if (currentChar != ']') {
         loop:
         for (;;) {
            result.add(parseValue(currentChar));
            currentChar = this.recentChar;
            currentChar = consumeWhitespace(currentChar);
            switch (currentChar) {
               case -1:
                  throw new EOFException();
               case ',':
                  currentChar = read();
                  currentChar = consumeWhitespace(currentChar);
                  break;
               case ']':
                  break loop;
               default:
                  throwUnexpected(currentChar);
            }
         }
      }
      return result;
   }

   /****************************************************************************
    * 
    ***************************************************************************/
   private Object parseNumber(int currentChar) throws IOException {

      int signum = 1;
      long integer = 0;
      if (currentChar == '-') {
         signum = -1;
         currentChar = read();
      }
      if (currentChar == -1) {
         throw new EOFException();
      }
      while (isDigit(currentChar)) {
         integer = 10 * integer + (currentChar - '0');
         currentChar = read();
      }
      if (isEndOfValue(currentChar)) {
         // integer - no exponent
         this.recentChar = currentChar;
         return new Long(integer * signum);
      }
      if (currentChar == '.') {
         // floating point
         currentChar = read();
         if (currentChar == -1) {
            throw new EOFException();
         }
         if (!isDigit(currentChar)) {
            throwUnexpected(currentChar);
         }
         double decimal = 0.1 * (currentChar - '0');
         double factor = 0.01;
         currentChar = read();
         while (isDigit(currentChar)) {
            decimal += factor * (currentChar - '0');
            factor /= 10;
            currentChar = read();
         }
         if (currentChar == 'e' | currentChar == 'E') {
            // floating point with exponent
            currentChar = read();
            int expSignum = 1;
            if (currentChar == '-') {
               expSignum = -1;
               currentChar = read();
            }
            if (currentChar == -1) {
               throw new EOFException();
            }
            if (!isDigit(currentChar)) {
               throwUnexpected(currentChar);
            }
            long exponent = (currentChar - '0');
            currentChar = read();
            while (isDigit(currentChar)) {
               exponent = 10 * exponent + (currentChar - '0');
               currentChar = read();
            }
            if (isEndOfValue(currentChar)) {
               this.recentChar = currentChar;
               if (expSignum > 0) {
                  return new Double((integer + decimal) * signum * pow(exponent));
               } else {
                  return new Double((integer + decimal) * signum / pow(exponent));
               }
            }
            throwUnexpected(currentChar);
         } else {
            // floating point without exponent
            if (isEndOfValue(currentChar)) {
               this.recentChar = currentChar;
               return new Double((integer + decimal) * signum);
            }
            throwUnexpected(currentChar);
         }
      }
      if (currentChar == 'e' | currentChar == 'E') {
         // integer or float with exponent
         currentChar = read();
         int expSignum = 1;
         if (currentChar == '-') {
            expSignum = -1;
            currentChar = read();
         }
         if (currentChar == -1) {
            throw new EOFException();
         }
         if (!isDigit(currentChar)) {
            throwUnexpected(currentChar);
         }
         long exponent = (currentChar - '0');
         currentChar = read();

         while (isDigit(currentChar)) {
            exponent = 10 * exponent + (currentChar - '0');
            currentChar = read();
         }
         if (isEndOfValue(currentChar)) {
            this.recentChar = currentChar;
            if (expSignum > 0) {
               return new Long(integer * pow(exponent) * signum);
            } else {
               if (exponent == 0) {
                  return new Long(integer * signum);
               } else {
                  return new Double(integer * 1.0 / pow(exponent) * signum);
               }
            }
         }
         throwUnexpected(currentChar);
      }
      throwUnexpected(currentChar);
      return null;
   }

   /****************************************************************************
    * 
    ***************************************************************************/
   private static long pow(long exp) {

      long result = 1;
      int base = 10;
      while (exp != 0) {
         if ((exp & 1) != 0) {
            result *= base;
         }
         exp >>= 1;
         base *= base;
      }
      return result;
   }

   /****************************************************************************
    * 
    ***************************************************************************/
   private String parseString() throws IOException {

      this.bufIndex = 0;
      boolean escaped = false;
      int currentChar;
      loop:
      for (;;) {
         currentChar = read();
         switch (currentChar) {
            case -1:
               throw new EOFException();
            case '\\':
               if (escaped) {
                  append('\"');
                  escaped = false;
               } else {
                  escaped = true;
               }
               break;
            case '"':
               if (escaped) {
                  append('\"');
                  escaped = false;
               } else {
                  break loop;
               }
               break;
            case '/':
               append('/');
               escaped = false;
               break;
            case 'b':
               if (escaped) {
                  append('\b');
                  escaped = false;
               } else {
                  append('b');
               }
               break;
            case 'f':
               if (escaped) {
                  append('\f');
                  escaped = false;
               } else {
                  append('f');
               }
               break;
            case 'n':
               if (escaped) {
                  append('\n');
                  escaped = false;
               } else {
                  append('n');
               }
               break;
            case 'r':
               if (escaped) {
                  append('\r');
                  escaped = false;
               } else {
                  append('r');
               }
               break;
            case 't':
               if (escaped) {
                  append('\t');
                  escaped = false;
               } else {
                  append('t');
               }
               break;
            case 'u':
               if (escaped) {
                  int chr = 0;
                  for (int i = 0; i < 4; ++i) {
                     chr <<= 4;
                     currentChar = read();
                     if (currentChar >= '0' & currentChar <= '9') {
                        chr += (currentChar - '0');
                     } else if (currentChar >= 'A' & currentChar <= 'F') {
                        chr += (10 + (currentChar - 'A'));
                     } else if (currentChar >= 'a' & currentChar <= 'f') {
                        chr += (10 + (currentChar - 'a'));
                     } else if (currentChar == -1) {
                        throw new EOFException();
                     } else {
                        throwUnexpected(currentChar);
                     }
                  }
                  append((char) chr);
                  escaped = false;
               } else {
                  append('u');
               }
               break;
            default:
               append((char) currentChar);
               break;
         }
      }
      currentChar = read();
      if (!isEndOfValue(currentChar)) {
         throwUnexpected(currentChar);
      }
      this.recentChar = currentChar;
      return this.bufIndex > 0 ? new String(this.buffer, 0, this.bufIndex) : "";
   }

   /****************************************************************************
    * 
    ***************************************************************************/
   private void throwUnexpected(final int chr) throws IOException {

      throw new UnexpectedCharacterException(this.position, (char) chr);
   }

   /****************************************************************************
    * 
    ***************************************************************************/
   private static boolean isDigit(final int chr) {

      return chr >= '0' & chr <= '9';
   }

   /****************************************************************************
    * 
    ***************************************************************************/
   private static boolean isWhitespace(final int chr) {

      return chr == ' ' | chr == '\t' | chr == '\n' | chr == '\r';
   }

   /****************************************************************************
    * 
    ***************************************************************************/
   private int consumeWhitespace(int chr) throws IOException {

      while (isWhitespace(chr)) {
         chr = read();
      }
      return chr;
   }

   /****************************************************************************
    * 
    ***************************************************************************/
   private static boolean isEndOfValue(final int chr) {

      return chr == -1 | chr == ' ' | chr == '\t' | chr == '\n' | chr == '\r'
              | chr == ']' | chr == '}' | chr == ',' | chr == ':';
   }

   /****************************************************************************
    * 
    ***************************************************************************/
   private void append(final char chr) {

      if (this.bufIndex == this.bufferSize) {
         this.bufferSize *= 2;
         final char[] newBuffer = new char[this.bufferSize];
         System.arraycopy(this.buffer, 0, newBuffer, 0, this.buffer.length);
         this.buffer = newBuffer;
      }
      this.buffer[this.bufIndex++] = chr;
   }

   /****************************************************************************
    * 
    ***************************************************************************/
   private int read() throws IOException {

      ++this.position;
      return this.reader.read();
   }

   /****************************************************************************
    * 
    ***************************************************************************/
   private Reader reader;
   private int recentChar = -1;
   private int position = 0;
   private char[] buffer;
   private int bufferSize;
   private int bufIndex = 0;

   private final int initialVectorSize;
   private final int initialHashtableSize;
}
