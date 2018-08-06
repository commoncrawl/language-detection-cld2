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

  /** Force Greek, etc. => quads */
  public static final int kCLDFlagScoreAsQuads = 0x0100;

  /** Debug HTML => stderr */
  public static final int kCLDFlagHtml = 0x0200;

  /** &lt;cr&gt; per chunk if HTML */
  public static final int kCLDFlagCr = 0x0400;

  /** More debug HTML => stderr */
  public static final int kCLDFlagVerbose = 0x0800;

  /** Less debug HTML => stderr */
  public static final int kCLDFlagQuiet = 0x1000;

  /** Echo input => stderr */
  public static final int kCLDFlagEcho = 0x2000;

  /** Give best-effort answer, even on short text */
  public static final int kCLDFlagBestEffort = 0x4000;

  public static final Flags BEST_EFFORT = new Flags();
  static {
    BEST_EFFORT.setBestEffort(true);
  }

  private int flags = 0;

  /**
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
