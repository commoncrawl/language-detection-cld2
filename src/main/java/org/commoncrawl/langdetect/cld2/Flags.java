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

/** Public use flags, debug output controls */
public class Flags {

  /**
   * Force Greek, etc. => quads
   * 
   * <blockquote>Normally, several languages are detected solely by their
   * Unicode script. Combined with appropriate lookup tables, this flag forces
   * them instead to be detected via quadgrams. This can be a useful refinement
   * when looking for meaningful text in these languages, instead of just
   * character sets. The default tables do not support this use.. [<a href=
   * "https://github.com/CLD2Owners/cld2/blob/b56fa78a2fe44ac2851bae5bf4f4693a0644da7b/public/compact_lang_det.h#L356">compact_lang_det.h</a>]</blockquote>
   */
  public static final int kCLDFlagScoreAsQuads = 0x0100;

  /**
   * Debug HTML => stderr
   * 
   * <blockquote>For each detection call, write an HTML file to stderr, showing
   * the text chunks and their detected languages.. [<a href=
   * "https://github.com/CLD2Owners/cld2/blob/b56fa78a2fe44ac2851bae5bf4f4693a0644da7b/public/compact_lang_det.h#L362">compact_lang_det.h</a>]</blockquote>
   */
  public static final int kCLDFlagHtml = 0x0200;

  /**
   * &lt;cr&gt; per chunk if HTML
   * 
   * <blockquote>In that HTML file, force a new line for each chunk.. [<a href=
   * "https://github.com/CLD2Owners/cld2/blob/b56fa78a2fe44ac2851bae5bf4f4693a0644da7b/public/compact_lang_det.h#L365">compact_lang_det.h</a>]</blockquote>
   */
  public static final int kCLDFlagCr = 0x0400;

  /**
   * More debug HTML => stderr
   * 
   * <blockquote>In that HTML file, show every lookup entry.. [<a href=
   * "https://github.com/CLD2Owners/cld2/blob/b56fa78a2fe44ac2851bae5bf4f4693a0644da7b/public/compact_lang_det.h#L367">compact_lang_det.h</a>]</blockquote>
   */
  public static final int kCLDFlagVerbose = 0x0800;

  /**
   * Less debug HTML => stderr
   * 
   * <blockquote>In that HTML file, suppress most of the output detail..
   * [<a href=
   * "https://github.com/CLD2Owners/cld2/blob/b56fa78a2fe44ac2851bae5bf4f4693a0644da7b/public/compact_lang_det.h#L369">compact_lang_det.h</a>]</blockquote>
   */
  public static final int kCLDFlagQuiet = 0x1000;

  /**
   * Echo input => stderr
   * 
   * <blockquote>Echo every input buffer to stderr. [<a href=
   * "https://github.com/CLD2Owners/cld2/blob/b56fa78a2fe44ac2851bae5bf4f4693a0644da7b/public/compact_lang_det.h#L371">compact_lang_det.h</a>]</blockquote>
   */
  public static final int kCLDFlagEcho = 0x2000;

  /**
   * Give best-effort answer, even on short text
   * 
   * <blockquote>Give best-effort answer, instead of UNKNOWN_LANGUAGE. May be
   * useful for short text if the caller prefers an approximate answer over
   * none. [<a href=
   * "https://github.com/CLD2Owners/cld2/blob/b56fa78a2fe44ac2851bae5bf4f4693a0644da7b/public/compact_lang_det.h#L373">compact_lang_det.h</a>]</blockquote>
   */
  public static final int kCLDFlagBestEffort = 0x4000;

  public static final Flags BEST_EFFORT = new Flags();
  static {
    BEST_EFFORT.setBestEffort(true);
  }

  private int flags = 0;

  /**
   * <blockquote>Give best-effort answer, instead of UNKNOWN_LANGUAGE. May be
   * useful for short text if the caller prefers an approximate answer over
   * none. [<a href=
   * "https://github.com/CLD2Owners/cld2/blob/b56fa78a2fe44ac2851bae5bf4f4693a0644da7b/public/compact_lang_det.h#L373">compact_lang_det.h</a>]</blockquote>
   * 
   * @param value
   *          if true, give &quot;best-effort answer, even on short text&quot;
   */
  public void setBestEffort(boolean value) {
    if (value) {
      flags |= kCLDFlagBestEffort;
    } else {
      flags &= ~kCLDFlagBestEffort;
    }
  }

  public int get() {
    return flags;
  }

}
