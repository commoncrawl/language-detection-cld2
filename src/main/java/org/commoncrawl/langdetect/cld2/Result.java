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

import java.util.Arrays;
import java.util.Locale;

/** Holds the result of a call to {@link Cld2#detect(byte[]). */
public class Result {

  // data filled by native call to libcld2
  protected int[] language3 = new int[3];
  protected int[] percent3 = new int[3];
  protected double[] normalizedScore3 = new double[3];
  protected int[] textBytes = new int[1];
  protected boolean[] isReliable = new boolean[1];

  protected int pruneMinTotalTextBytes = 0;
  protected int pruneMinTextPercent = 0;
  protected double pruneMinScore = 0.0;

  // language returned by call to libcld2
  // (language with highest coverage, but not necessarily highest score)
  protected int language = Language.UNKNOWN_LANGUAGE.value();

  protected Result() {
  }

  /**
   * Configure pruning of languages returned by {@link #getLanguageCodes()},
   * {@link #toString()}, {@link #toJSON()}.
   * 
   * @param minTotalTextBytes
   *          min. number of text bytes available to CLD2 language detector,
   *          return empty list of languages if less bytes available
   * @param minTextPercent
   *          min. percent of text covered by a detected languages, languages
   * @param minScore
   *          min. score for language
   */
  public void configurePruning(int minTotalTextBytes, int minTextPercent,
      double minScore) {
    pruneMinTotalTextBytes = minTotalTextBytes;
    pruneMinTextPercent = minTextPercent;
    pruneMinScore = minScore;
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
   * ISO-639-3 code of top detected language
   */
  public String getLanguageCodeISO639_3() {
    return Language.get(language).getCodeISO639_3();
  }

  /**
   * @return array of CLD2-internal language codes of detected languages
   */
  public String[] getLanguageCodes() {
    int[] languages = prunedResults();
    String[] codes = new String[languages.length];
    for (int j = 0; j < languages.length; j++) {
      int i = languages[j];
      codes[j] = Cld2.getLanguageCode(language3[i]);
    }
    return codes;
  }

  /**
   * @return array of detected languages
   */
  public Language[] getLanguages() {
    int[] languages = prunedResults();
    Language[] res = new Language[languages.length];
    for (int j = 0; j < languages.length; j++) {
      int i = languages[j];
      res[j] = Language.get(language3[i]);
    }
    return res;
  }

  /**
   * @param deduplicate
   *          if true deduplicate ISO-639-3 codes (multiple CLD2 language can be
   *          mapped to the same ISO-639-3 code)
   * @return array of (deduplicated) ISO-639-3 codes of detected languages
   */
  public String[] getLanguageCodesISO639_3(boolean deduplicate) {
    int[] languages = prunedResults();
    String[] res = new String[languages.length];
    String[] dedup = { "", "" };
    int k = -1, j = 0;
    for (; j < languages.length; j++) {
      int i = languages[j];
      int l = language3[i];
      String code = Language.get(l).getCodeISO639_3();
      if (code != null && !(deduplicate && ((k >= 0 && dedup[k].equals(code))
          || (k >= 1 && dedup[k - 1].equals(code))))) {
        res[++k] = code;
        if (k < 2) {
          dedup[k] = code;
        }
      }
    }
    if (k < j) {
      return Arrays.copyOf(res, k);
    }
    return res;
  }

  /**
   * @param separator
   *          seperator between language codes
   * @param deduplicate
   *          if true deduplicate ISO-639-3 codes (multiple CLD2 language can be
   *          mapped to the same ISO-639-3 code)
   * @return joined (and deduplicated) ISO-639-3 codes of detected languages
   */
  public String getLanguageCodesISO639_3(String separator, boolean deduplicate) {
    int[] languages = prunedResults();
    StringBuilder sb = new StringBuilder();
    String[] dedup = { "", "" };
    for (int k = -1, j = 0; j < languages.length; j++) {
      int i = languages[j];
      int l = language3[i];
      String code = Language.get(l).getCodeISO639_3();
      if (code != null && !(deduplicate && ((k >= 0 && dedup[k].equals(code))
          || (k >= 1 && dedup[k - 1].equals(code))))) {
        if (++k > 0) {
          sb.append(separator);
        }
        sb.append(code);
        if (k < 2) {
          dedup[k] = code;
        }
      }
    }
    return sb.toString();
  }

  public boolean isReliable() {
    return isReliable[0];
  }

  private int[] prunedResults() {
    int[] r = {-1, -1, -1};
    int j = 0;
    if (textBytes[0] < pruneMinTotalTextBytes) {
      // not enough text for a reliable result
      return new int[0];
    }
    for (int i = 0; i < language3.length; i++) {
      if (percent3[i] < pruneMinTextPercent) {
        continue;
      }
      if (normalizedScore3[i] < pruneMinScore) {
        continue;
      }
      int lang = language3[i];
      if (i > 0 && lang == Language.UNKNOWN_LANGUAGE.value()) {
        // only take the first "unknown language" result
        break;
      }
      r[j++] = i;
    }
    if (j < 3) {
      return Arrays.copyOf(r, j);
    }
    return r;
  }

  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("Result reliable = ");
    sb.append(isReliable());
    sb.append(", text bytes = ");
    sb.append(textBytes[0]);
    for (int i : prunedResults()) {
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

  public String toJSON() {
    StringBuilder sb = new StringBuilder();
    sb.append("{\"reliable\":").append(isReliable());
    sb.append(",\"text-bytes\":").append(textBytes[0]);
    int[] languages = prunedResults();
    if (languages.length > 0) {
      sb.append(",\"languages\":[");
      for (int j = 0; j < languages.length; j++) {
        if (j > 0) {
          sb.append(',');
        }
        int i = languages[j];
        int lang = language3[i];
        Language language = Language.get(lang);
        sb.append("{\"code\":\"").append(language.getCode());
        sb.append("\",\"code-iso-639-3\":\"").append(language.getCodeISO639_3());
        sb.append("\",\"text-covered\":").append(percent3[i]/100.0);
        sb.append(",\"score\":").append(normalizedScore3[i]);
        sb.append(",\"name\":\"").append(Cld2.getLanguageName(lang));
        sb.append("\"}");
      }
      sb.append(']');
    }
    sb.append('}');
    return sb.toString();
  }

}
