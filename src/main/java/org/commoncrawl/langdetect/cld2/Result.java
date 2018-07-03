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

import java.util.Locale;

/** Holds the result of a call to {@link Cld2#detect(byte[]). */
public class Result {

  // data filled by native call to libcld2
  protected int[] language3 = new int[3];
  protected int[] percent3 = new int[3];
  protected double[] normalizedScore3 = new double[3];
  protected int[] textBytes = new int[1];
  protected boolean[] isReliable = new boolean[1];

  // language returned by call to libcld2
  // (language with highest score)
  protected int language = Language.UNKNOWN_LANGUAGE.value();

  protected Result() {
  }

  /** Best detected {@link Language} */
  public int getLanguage() {
    return language;
  }

  /** Best detected {@link Language} */
  protected void setLanguage(int lang) {
    language = lang;
  }

  /** Name of best detected language */
  public String getLanguageName() {
    return Cld2.getLanguageName(language);
  }

  /**
   * CLD2-internal language code of top detected language, see
   * {@link Language#getCode()}
   */
  public String getLanguageCode() {
    return Cld2.getLanguageCode(language);
  }

  /**
   * ISO-639-1 or ISO-639-2 code of top detected language, optionally with
   * extensions for further subdivision.
   */
  public String getLanguageCodeISO639_3() {
    return Language.get(language).getCodeISO639_3();
  }

  public String[] getLanguageCodes() {
    String[] codes = new String[numResults()];
    for (int i = 0; i < numResults(); i++) {
      codes[i] = Cld2.getLanguageCode(language3[i]);
    }
    return codes;
  }

  public boolean isReliable() {
    return isReliable[0];
  }

  private int numResults() {
    int i = 1;
    for (; i < language3.length; i++) {
      int lang = language3[i];
      if (lang == Language.UNKNOWN_LANGUAGE.value()) {
        break;
      }
    }
    return i;
  }

  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("Result reliable = ");
    sb.append(isReliable());
    sb.append(", text bytes = ");
    sb.append(textBytes[0]);
    for (int i = 0; i < numResults(); i++) {
      int lang = language3[i];
      Language language = Language.get(lang);
      sb.append("\n  ");
      sb.append(language.getCode());
      sb.append('\t');
      sb.append(language.getCodeISO639_3());
      sb.append('\t');
      sb.append(String.format(Locale.ROOT, "%3d%%", percent3[i]));
      sb.append('\t');
      sb.append(String.format(Locale.ROOT, "%.3f", normalizedScore3[i]));
      sb.append('\t');
      sb.append(Cld2.getLanguageName(lang));
    }
    return sb.toString();
  }

}
