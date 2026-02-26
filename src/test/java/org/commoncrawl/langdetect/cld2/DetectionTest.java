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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.function.Function;

import org.junit.jupiter.api.Test;

public class DetectionTest {

  protected void run(String testFile, Function<String, Result> detectFunc,
      Function<String, String> getDocument) throws IOException {
    BufferedReader reader = openTestData(testFile);
    String line;
    int i = 0;
    while ((line = reader.readLine()) != null) {
      if (++i == 1) {
        // header line
        continue;
      }
      String[] fields = line.split("\t");
      String text = fields[3];
      String expectedCode = fields[0];
      String expectedCodeIso639_3 = fields[1];
      String document = getDocument.apply(text);
      Result res = detectFunc.apply(document);
      String snippet = text.substring(0, Math.min(text.length(), 80));
      assertEquals(expectedCode, res.getLanguageCode(),
          "Wrong language for test " + i + ": " + snippet);
      assertEquals(expectedCodeIso639_3, res.getLanguageCodeISO639_3(),
          "Wrong ISO-639-3 language code for test " + i + ": " + snippet);
    }
  }

  @Test
  public void testPlainText() throws IOException {
    run("tatoeba/tatoeba_long.txt", Cld2::detect, (String t) -> t);
  }

  @Test
  public void testHtml() throws IOException {
    String header1 = "<!DOCTYPE html>\n<html>\n<head>\n<title>";
    String header2 = "</title>\n<meta charset=\"utf-8\">\n</head><body>\n<p>";
    String footer = "</p>\n</body></html>";
    Function<String, String> getDocument = (String text) -> {
      String title = text.substring(0, Math.min(text.length(), 40));
      return header1 + title + header2 + text + footer;
    };
    run("tatoeba/tatoeba_long.txt", (String t) -> {
      return Cld2.detect(t, false);
    }, getDocument);
  }

  @Test
  public void testContentLanguageHint() {
    String text = "\u4eba means person"; // 人 - kanji "person"
    Flags flags = new Flags();
    flags.setBestEffort(true); // because testing a very short text
    CLDHints hints = new CLDHints();
    String[] contentLanguageHints1 = { "en, ja", "en-US,ja", "Japanese,English" };
    for (String clh : contentLanguageHints1) {
      hints.setContentLanguageHint(clh);
      Result r1 = Cld2.detect(text, hints, flags, true);
      assertTrue(Arrays.asList(r1.getLanguageCodes()).contains("ja"));
      assertTrue(Arrays.asList(r1.getLanguageCodes()).contains("en"));
    }
    String[] contentLanguageHints2 = { "en, zh", "en-US,zh_CN", "Chinese,English" };
    for (String clh : contentLanguageHints2) {
      hints.setContentLanguageHint(clh);
      Result r2 = Cld2.detect(text, hints, flags, true);
      for (String langCode : r2.getLanguageCodes()) {
        assertTrue(langCode.startsWith("zh") || langCode.equals("en"));
      }
    }
  }

  private static BufferedReader openTestData(String fileName)
      throws IOException {
    return new BufferedReader(new InputStreamReader(
        DetectionTest.class.getClassLoader().getResourceAsStream(fileName),
        StandardCharsets.UTF_8));
  }

}
