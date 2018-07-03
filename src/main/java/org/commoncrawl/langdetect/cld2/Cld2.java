/*
 * Copyright 2014-present Deezer.com, 2018- CommonCrawl.org
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

import java.nio.charset.StandardCharsets;

/**
 * Public interface for the CLD2 library.
 */
public class Cld2 {

  public static int getLanguageFromName(String name) {
    return Cld2Library.INSTANCE._ZN4CLD219GetLanguageFromNameEPKc(name);
  }

  public static String getLanguageName(int language) {
    return Cld2Library.INSTANCE._ZN4CLD212LanguageNameENS_8LanguageE(language);
  }

  public static String getLanguageCode(int language) {
    return Cld2Library.INSTANCE._ZN4CLD212LanguageCodeENS_8LanguageE(language);
  }

  public static String version() {
    return Cld2Library.INSTANCE._ZN4CLD221DetectLanguageVersionEv();
  }

  /**
   * Encode input as native (null-terminated) bytes using UTF-8 as character
   * encoding.
   * 
   * @param text
   *          input string
   * @return null-terminated UTF-8-encoded bytes
   */
  public static byte[] encodeNative(String text) {
    byte[] jbytes = text.getBytes(StandardCharsets.UTF_8);
    byte[] cbytes = new byte[jbytes.length+1];
    System.arraycopy(jbytes, 0, cbytes, 0, jbytes.length);
    return cbytes;
  }

  public static Result detect(String text) {
    return detect(encodeNative(text));
  }

  /**
   * Detect language.
   * 
   * @param bytes
   *          input text as null-terminated UTF-8-encoded bytes
   * @return detection result
   */
  public static Result detect(byte[] bytes) {
    return detect(bytes, CLDHints.NO_HINTS, 0, true);
  }

  public static Result detect(String text, CLDHints hints) {
    return detect(encodeNative(text), hints);
  }

  public static Result detect(String text, boolean isPlainText) {
    return detect(encodeNative(text), CLDHints.NO_HINTS, 0, isPlainText);
  }

  public static Result detect(byte[] bytes, CLDHints hints) {
    return detect(bytes, hints, 0, true);
  }

  public static Result detect(byte[] bytes, CLDHints hints, Flags flags,
      boolean isPlainText) {
    return detect(bytes, hints, flags.get(), isPlainText);
  }

  public static Result detect(String text, CLDHints hints, Flags flags,
      boolean isPlainText) {
    return detect(encodeNative(text), hints, flags, isPlainText);
  }

  public static Result detect(String text, CLDHints hints, int flags,
      boolean isPlainText) {
    return detect(encodeNative(text), hints, flags, isPlainText);
  }

  /**
   * Detect language.
   * 
   * @param bytes
   *          input text as null-terminated UTF-8-encoded bytes
   * @param hints
   *          external hints (outside context) from context of web page
   * @param flags
   *          modify behavior of CLD2 library call
   * @param isPlainText
   *          whether to detect language of plain-text document or HTML page
   * @return detection result
   */
  public static Result detect(byte[] bytes, CLDHints hints, int flags,
      boolean isPlainText) {
    Result res = new Result();
    int language = Cld2Library.INSTANCE._ZN4CLD224ExtDetectLanguageSummaryEPKcibPKNS_8CLDHintsEiPNS_8LanguageEPiPdPSt6vectorINS_11ResultChunkESaISA_EES7_Pb(
        bytes,
        bytes.length,
        isPlainText,
        hints,
        flags,
        res.language3,
        res.percent3,
        res.normalizedScore3,
        null, // Supposed to be a vector of ResultChunks, but it is not direct to pass vectors.
        res.textBytes,
        res.isReliable);
    res.setLanguage(language);

    return res;
  }
}
