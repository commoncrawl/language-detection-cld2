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

import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.NativeLibrary;
import com.sun.jna.Platform;
import com.sun.jna.ptr.PointerByReference;


/**
 * JNA Wrapper for library <b>Cld2</b>
 */
public interface Cld2Library extends Library {

  String CLD2_LIBRARY_VARIANT_PROPERTY = "cld2.library";
  String VARIANT_STANDARD = "cld2";
  String VARIANT_FULL = "cld2_full";

  static String getLibraryName() {
      return System.getProperty(CLD2_LIBRARY_VARIANT_PROPERTY, VARIANT_STANDARD);
  }

  String JNA_LIBRARY_NAME = getLibraryName();

  static NativeLibrary loadLibrary() {
    // Try to load from jna.library.path first (allows override)
    try {
      return NativeLibrary.getInstance(JNA_LIBRARY_NAME);
    } catch (UnsatisfiedLinkError e) {
      // Try to load from classpath resources
      String resourcePath = "/" + Platform.RESOURCE_PREFIX + "/" + JNA_LIBRARY_NAME + 
          (Platform.isMac() ? ".dylib" : ".so");
      try {
        return NativeLibrary.getInstance(resourcePath);
      } catch (UnsatisfiedLinkError e2) {
        // Fall back to system library search
        return NativeLibrary.getInstance(Cld2Library.JNA_LIBRARY_NAME);
      }
    }
  }

  NativeLibrary JNA_NATIVE_LIB = loadLibrary();

  Cld2Library INSTANCE = (Cld2Library) Native.load(Cld2Library.JNA_LIBRARY_NAME,
      Cld2Library.class);

  //String LanguageName(int lang);
  String _ZN4CLD212LanguageNameENS_8LanguageE(int lang);

  //String LanguageCode(int lang);
  String _ZN4CLD212LanguageCodeENS_8LanguageE(int lang);

  //int GetLanguageFromName(String src);
  int _ZN4CLD219GetLanguageFromNameEPKc(String src);

  //int ExtDetectLanguageSummary(String buffer, int buffer_length, byte is_plain_text, CLDHints cld_hints, int flags, IntBuffer language3, IntBuffer percent3, DoubleBuffer normalized_score3, PointerByReference resultchunkvector, IntBuffer text_bytes, ByteBuffer is_reliable);
  // libc++ (macOS) uses NSt3__16vector
  int _ZN4CLD224ExtDetectLanguageSummaryEPKcibPKNS_8CLDHintsEiPNS_8LanguageEPiPdPNSt3__16vectorINS_11ResultChunkENS9_9allocatorISB_EEEES7_Pb(
    byte[] buffer, int bufferLength, boolean isPlainText, CLDHints cldHints, int flags,
    int[] language3, int[] percent3, double[] normalizedScore3,
    PointerByReference resultchunkvector, int[] textBytes, boolean[] isReliable);

  // libstdc++ (Linux) uses St6vector
  int _ZN4CLD224ExtDetectLanguageSummaryEPKcibPKNS_8CLDHintsEiPNS_8LanguageEPiPdPSt6vectorINS_11ResultChunkESaISA_EES7_Pb(
    byte[] buffer, int bufferLength, boolean isPlainText, CLDHints cldHints, int flags,
    int[] language3, int[] percent3, double[] normalizedScore3,
    PointerByReference resultchunkvector, int[] textBytes, boolean[] isReliable);

  //String DetectLanguageVersion();
  String _ZN4CLD221DetectLanguageVersionEv();

}
