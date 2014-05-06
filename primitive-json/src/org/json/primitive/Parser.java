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

/*******************************************************************************
 * JSON parser.
 * @author lukasz.bownik@gmail.com
 ******************************************************************************/
public final class Parser {

   /****************************************************************************
    * Creates a new parser.
    * @param initialBufferSize initial internal buffer size for string values. 
    * Setting this parameter to a predicted maximum string value length helps 
    * avoiding beffer realocations while parsing. If in doubt, use 15. 
    * This paramater exists only for performance optimization purposes.
    * @throws java.lang.IllegalArgumentException if initialBufferSize <= 0.
    ***************************************************************************/
   public Parser(final int initialBufferSize) {

      if (initialBufferSize <= 0) {
         throw new IllegalArgumentException("initialBufferSize <= 0");
      }
      this.buffer = new char[initialBufferSize];
      this.bufferSize = this.buffer.length;
   }

   /****************************************************************************
    * Parse JSON object.
    * @param reader a reader object.
    * @return java.util.Hashtable if the reader contained JSON object or 
    *    java.util.Vector if the reader contained JSON array.
    * @throws IOException if input error occurs.
    * @throws org.json.primitive.UnexpectedCharacterException if malformed JSON is
    * encountered.
    * @throws NullPointerException if reader is null.
    ***************************************************************************/
   public Object parse(final Reader reader) throws IOException {

      this.reader = reader;
      try {
         this.position = -1;
         this.currentChar = read();
         switch (this.currentChar) {
            case '{':
               return parseObject();
            case '[':
               return parseArray();
            case -1:
               throw new EOFException();
            default:
               throwUnexpected();
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
    * @throws org.json.primitive.UnexpectedCharacterException if malformed JSON is
    * encountered.
    * @throws NullPointerException if reader is null.
    ***************************************************************************/
   public Object parse(final String str) throws IOException {

      return parse(new StringReader(str));
   }

   /****************************************************************************
    * 
    ***************************************************************************/
   private Object parseValue() throws IOException {

      switch (this.currentChar) {
         case '{': {
            final Object o = parseObject();
            this.currentChar = read();
            consumeWhitespace();
            return o;
         }
         case '[': {
            final Object o = parseArray();
            this.currentChar = read();
            consumeWhitespace();
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
            return parseNumber();
         case '"':
            return parseString();
         case -1:
            throw new EOFException();
         default:
            throwUnexpected();
            return null;
      }
   }

   /****************************************************************************
    * 
    ***************************************************************************/
   private Boolean parseTrue() throws IOException {

      this.currentChar = read();
      if (this.currentChar == -1) {
         throw new EOFException();
      }
      if (this.currentChar != 'r') {
         throwUnexpected();
      }
      this.currentChar = read();
      if (this.currentChar == -1) {
         throw new EOFException();
      }
      if (this.currentChar != 'u') {
         throwUnexpected();
      }
      this.currentChar = read();
      if (this.currentChar == -1) {
         throw new EOFException();
      }
      if (this.currentChar != 'e') {
         throwUnexpected();
      }
      this.currentChar = read();
      if (!isEndOfValue(this.currentChar)) {
         throwUnexpected();
      }
      return Boolean.TRUE;
   }

   /****************************************************************************
    * 
    ***************************************************************************/
   private Boolean parseFalse() throws IOException {

      this.currentChar = read();
      if (this.currentChar == -1) {
         throw new EOFException();
      }
      if (this.currentChar != 'a') {
         throwUnexpected();
      }
      this.currentChar = read();
      if (this.currentChar == -1) {
         throw new EOFException();
      }
      if (this.currentChar != 'l') {
         throwUnexpected();
      }
      this.currentChar = read();
      if (this.currentChar == -1) {
         throw new EOFException();
      }
      if (this.currentChar != 's') {
         throwUnexpected();
      }
      this.currentChar = read();
      if (this.currentChar == -1) {
         throw new EOFException();
      }
      if (this.currentChar != 'e') {
         throwUnexpected();
      }
      this.currentChar = read();
      if (!isEndOfValue(this.currentChar)) {
         throwUnexpected();
      }
      return Boolean.FALSE;
   }

   /****************************************************************************
    * 
    ***************************************************************************/
   private Object parseNull() throws IOException {

      this.currentChar = read();
      if (this.currentChar == -1) {
         throw new EOFException();
      }
      if (this.currentChar != 'u') {
         throwUnexpected();
      }
      this.currentChar = read();
      if (this.currentChar == -1) {
         throw new EOFException();
      }
      if (this.currentChar != 'l') {
         throwUnexpected();
      }
      this.currentChar = read();
      if (this.currentChar == -1) {
         throw new EOFException();
      }
      if (this.currentChar != 'l') {
         throwUnexpected();
      }
      this.currentChar = read();
      if (!isEndOfValue(this.currentChar)) {
         throwUnexpected();
      }
      return Null.value;
   }

   /****************************************************************************
    * 
    ***************************************************************************/
   private Hashtable parseObject() throws IOException {

      final Hashtable result = new Hashtable();
      this.currentChar = read();
      consumeWhitespace();
      if (this.currentChar == -1) {
         throw new EOFException();
      }
      if (this.currentChar != '}') {
         loop:
         for (;;) {
            if (this.currentChar != '"') {
               throwUnexpected();
            }
            final String key = parseString();
            consumeWhitespace();
            if (this.currentChar == -1) {
               throw new EOFException();
            }
            if (this.currentChar != ':') {
               throwUnexpected();
            }
            this.currentChar = read();
            consumeWhitespace();
            if (this.currentChar == -1) {
               throw new EOFException();
            }
            result.put(key, parseValue());
            consumeWhitespace();
            switch (this.currentChar) {
               case -1:
                  throw new EOFException();
               case ',':
                  this.currentChar = read();
                  consumeWhitespace();
                  if (this.currentChar == -1) {
                     throw new EOFException();
                  }
                  break;
               case '}':
                  break loop;
               default:
                  throwUnexpected();
            }
         }
      }
      return result;
   }

   /****************************************************************************
    * 
    ***************************************************************************/
   private Vector parseArray() throws IOException {

      final Vector result = new Vector();
      this.currentChar = read();
      consumeWhitespace();
      if (this.currentChar == -1) {
         throw new EOFException();
      }
      if (this.currentChar != ']') {
         loop:
         for (;;) {
            result.addElement(parseValue());
            consumeWhitespace();
            switch (this.currentChar) {
               case -1:
                  throw new EOFException();
               case ',':
                  this.currentChar = read();
                  consumeWhitespace();
                  break;
               case ']':
                  break loop;
               default:
                  throwUnexpected();
            }
         }
      }
      return result;
   }

   /****************************************************************************
    * 
    ***************************************************************************/
   private Object parseNumber() throws IOException {

      int signum = 1;
      long integer = 0;
      if (this.currentChar == '-') {
         signum = -1;
         this.currentChar = read();
      }
      if (this.currentChar == -1) {
         throw new EOFException();
      }
      while (isDigit(this.currentChar)) {
         integer = 10 * integer + (this.currentChar - '0');
         this.currentChar = read();
      }
      if (isEndOfValue(this.currentChar)) {
         // integer - no exponent
         return new Long(integer * signum);
      }
      if (this.currentChar == '.') {
         // floating point
         this.currentChar = read();
         if (this.currentChar == -1) {
            throw new EOFException();
         }
         if (!isDigit(this.currentChar)) {
            throwUnexpected();
         }
         double decimal = 0.1 * (this.currentChar - '0');
         double factor = 0.01;
         this.currentChar = read();
         while (isDigit(this.currentChar)) {
            decimal += factor * (this.currentChar - '0');
            factor /= 10;
            this.currentChar = read();

         }
         if (this.currentChar == 'e' | this.currentChar == 'E') {
            // floating point with exponent
            this.currentChar = read();
            int expSignum = 1;
            if (this.currentChar == '-') {
               expSignum = -1;
               this.currentChar = read();
            }
            if (this.currentChar == -1) {
               throw new EOFException();
            }
            if (!isDigit(this.currentChar)) {
               throwUnexpected();
            }
            long exponent = (this.currentChar - '0');
            this.currentChar = read();
            while (isDigit(this.currentChar)) {
               exponent = 10 * exponent + (this.currentChar - '0');
               this.currentChar = read();
            }
            if (isEndOfValue(this.currentChar)) {
               if (expSignum > 0) {
                  return new Double((integer + decimal) * signum * pow(exponent));
               } else {
                  return new Double((integer + decimal) * signum / pow(exponent));
               }
            }
            throwUnexpected();
         } else {
            // floating point without exponent
            if (isEndOfValue(this.currentChar)) {
               return new Double((integer + decimal) * signum);
            }
            throwUnexpected();
         }
      }
      if (this.currentChar == 'e' | this.currentChar == 'E') {
         // integer or float with exponent
         this.currentChar = read();
         int expSignum = 1;
         if (this.currentChar == '-') {
            expSignum = -1;
            this.currentChar = read();
         }
         if (this.currentChar == -1) {
            throw new EOFException();
         }
         if (!isDigit(this.currentChar)) {
            throwUnexpected();
         }
         long exponent = (this.currentChar - '0');
         this.currentChar = read();

         while (isDigit(this.currentChar)) {
            exponent = 10 * exponent + (this.currentChar - '0');
            this.currentChar = read();
         }
         if (isEndOfValue(this.currentChar)) {
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
         throwUnexpected();
      }
      throwUnexpected();
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
      int current; // claass field skipped for performance reasons
      loop:
      for (;;) {
         current = read();
         switch (current) {
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
                     current = read();
                     if (current >= '0' & current <= '9') {
                        chr += (current - '0');
                     } else if (current >= 'A' & current <= 'F') {
                        chr += (10 + (current - 'A'));
                     } else if (current >= 'a' & current <= 'f') {
                        chr += (10 + (current - 'a'));
                     } else if (current == -1) {
                        throw new EOFException();
                     } else {
                        this.currentChar = current;
                        throwUnexpected();
                     }
                  }
                  append((char) chr);
                  escaped = false;
               } else {
                  append('u');
               }
               break;
            default:
               append((char) current);
               break;
         }
      }
      current = read();
      this.currentChar = current;
      if (!isEndOfValue(current)) {
         throwUnexpected();
      }
      return new String(this.buffer, 0, this.bufIndex);
   }

   /****************************************************************************
    * 
    ***************************************************************************/
   private void throwUnexpected() throws IOException {

      throw new UnexpectedCharacterException(this.position, (char) this.currentChar);
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
   private void consumeWhitespace() throws IOException {

      while (isWhitespace(this.currentChar)) {
         this.currentChar = read();
      }
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
   private int currentChar = -1;
   private int position = 0;
   private char[] buffer;
   private int bufferSize;
   private int bufIndex = 0;
}
