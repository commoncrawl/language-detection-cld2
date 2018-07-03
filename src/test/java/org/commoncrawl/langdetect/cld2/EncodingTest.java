/*
 * Copyright 2018 commoncrawl.org
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.commoncrawl.langdetect.cld2;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import org.junit.jupiter.api.Test;

public class EncodingTest {

  @Test
  public void testStandardCharsets() {
    assertEquals(Encoding.ISO_8859_1.value(),
        Encoding.convert(StandardCharsets.ISO_8859_1).value());
    assertEquals(Encoding.UTF8.value(),
        Encoding.convert(StandardCharsets.UTF_8).value());
    assertEquals(Encoding.ASCII_7BIT.value(),
        Encoding.convert(StandardCharsets.US_ASCII).value());
  }

  @Test
  public void testCharsetForName() {
    assertEquals(Encoding.ASCII_7BIT.value(),
        Encoding.convert(Charset.forName("US-ASCII")).value());
    assertEquals(Encoding.RUSSIAN_KOI8_R.value(),
        Encoding.convert(Charset.forName("KOI8-R")).value());
  }

  @Test
  public void testEncodingHint() {
    String text = "\u4eba"; // 人 - kanji "person"
    Flags flags = new Flags();
    flags.setBestEffort(true); // because testing a very short text
    CLDHints hints = new CLDHints();
    hints.setEncodingHint(Charset.forName("EUC-JP")); // used in Japan
    Result r1 = Cld2.detect(text, hints, flags, true);
    assertEquals("ja", r1.getLanguageCode());
    hints.setEncodingHint(Charset.forName("Big5")); // used in Taiwan
    Result r2 = Cld2.detect(text, hints, flags, true);
    assertTrue(r2.getLanguageCode().startsWith("zh"));
  }

}
