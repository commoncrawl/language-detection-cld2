# Java wrapper for the Compact Language Detector 2 library

The [Compact Language Detector 2](https://github.com/CLD2Owners/cld2) is a native library written in C++ to detect the language of plain-text or HTML documents. Originally written for the Chromium web browser, the library is able to identify 80+ language (or 160+ in the full version). The classification uses identifies a language either by script (e.g., Greek), or uses a Naïve Bayesian classifier operating with 4-letter n-grams ("quadgrams") or (for  CJK languages) single-letter "unigrams". The classifier also accepts external hints, e.g., the top-level domain of a web page or the language code sent in the HTTP header.


## Installation

### Native Library
First, the library libcld2.so (or a .dll on Windows) needs to be installed.

- on Debian-based systems the easiest way is to install the package [libcld-0](https://packages.debian.org/stretch/libcld2-0):
```
apt-get install libcld2-0 libcld2-dev
```
- to compile the CLD2 library from source:
```
git clone https://github.com/CLD2Owners/cld2.git
cd cld/internal/
export CFLAGS="-Wno-narrowing -O3"
./compile_and_test_all.sh
```
If you only want the libraries, `./compile_libs.sh` is sufficient. You may use different compiler flags, the flag `-Wno-narrowing` is required for compilers which follow the C++11 standard.

Both the Debian package and the source build provide two native libraries: libcld2.so and `libcld2_full.so`. The former supports 80+, the latter 160+ languages. However, the `libcld2_full.so` from the Debian package isn't a complete shared library - it only contains the tables used by the classifier. To use the larger tables for 160+ language instead of those for 80+ languages, you must use the [LD_PRELOAD trick](https://stackoverflow.com/questions/426230/what-is-the-ld-preload-trick) and set `LD_PRELOAD=libcld2_full.so`.


### Java Bindings

This project is build and installed using Maven
```
mvn install
```
and can then be used as dependency
```
<dependency>
  <groupId>org.commoncrawl</groupId>
  <artifactId>language-detection-cld2</artifactId>
  <version>0.1-SNAPSHOT</version>
</dependency>
```

To link the Java code with the native libraries, you need to make sure that Java can find the share object:
- either install the native library on a standard library path (already done when the Debian package is used)
- add the directory where your libcld2.so installed to the environment variable `LD_LIBRARY_PATH`
- use the Java option `-Djava.library.path=...`

#### Java Native Access (JNA) and libffi

The CLD2 native functions are accessed via the [Java Native Access (JNA)](https://github.com/java-native-access/jna) which uses the [Foreign Function Interface Library (libffi)](https://sourceware.org/libffi/). JNA is a project dependency but the libffi needs to be present on your system. If not install it, e.g. 
```
apt-get install libffi6
```

## History

This package has derived from https://github.com/deezer/weslang (package [com.deezer.research.cld2](https://github.com/deezer/weslang/tree/master/java/com/deezer/research/cld2)), see the [original README](./README.deezer-weslang).

Further inspirations are taken from [CAFDataProcessing/worker-languagedetection](https://github.com/CAFDataProcessing/worker-languagedetection/tree/develop/language-detection-cld2), but this project depends on a modified version of CLD2 distributed only as a binary.

Modifications/improvements:
- extended interface
- support to pass as arguments Java objects of the classes Locale and Charset
- proper ISO-639-3 language codes for all 160 languages


## License

These bindings are [Apache 2.0 licensed](./LICENSE). Also [CLD2](https://github.com/CLD2Owners/cld2/blob/master/LICENSE), [weslang](https://github.com/deezer/weslang/blob/master/LICENSE) and all dependencies use the Apache 2.0 license.
