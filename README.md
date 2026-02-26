# Java wrapper for the Compact Language Detector 2 library

The [Compact Language Detector 2](https://github.com/CLD2Owners/cld2) is a native library written in C++ to detect the language of plain-text or HTML documents. Originally written for the Chromium web browser, the library is able to identify 80+ language (or 160+ in the full version). A language is identified either by script (e.g., Greek), or using a Naïve Bayesian classifier operating with 4-letter n-grams ("quadgrams") or (for CJK languages) single-letter "unigrams". The classifier also accepts external hints, e.g., the top-level domain of a web page or the language code sent in the HTTP header. The input (either text or HTML document) must be already in UTF-8 encoding.


## Installation

This project supports four build profiles:

| Profile       | Description                                                              | Platforms        |
| ------------- | ----------------------------------------------------------------------- | ----------------|
| *(default)*   | No native library bundled. Requires system library or `-Djava.library.path` | Any             |
| `system`     | Use system-installed libcld2 (Debian package)                          | Linux (Debian)  |
| `standard`   | Clone and build CLD2 from source, bundle into JAR                       | Linux, macOS    |
| `full`       | Build from source with full language support (160+)                     | Linux           |

### System Library (Linux/Debian)

Install the native library via apt:
```
apt-get install libcld2-0 libcld2-dev
```

Then build with the `system` profile:
```
mvn clean verify -Psystem
```

### Build from Source

For Linux or macOS, use the `standard` profile to clone and build CLD2 from source:
```
mvn clean verify -Pstandard
```

This clones [lfoppiano/CLD2](https://github.com/lfoppiano/CLD2) and builds `libcld2`, then bundles it into the JAR.

**Prerequisites:**
- Linux: `build-essential`, `git`
- macOS: Xcode Command Line Tools (includes git, clang++)

### Full Language Support (160+ languages)

The `full` profile is **Linux only**. It builds both `libcld2` and `libcld2_full` from source and uses `LD_PRELOAD` to load the full language tables during testing:

```
mvn clean verify -Pfull
```

The `libcld2_full` library only contains the classifier tables for 160+ languages — it is not a standalone library. At runtime, use `LD_PRELOAD=libcld2_full.so` to override the standard tables in `libcld2`. For Hadoop Map-Reduce jobs, pass `-Dmapreduce.reduce.env=LD_PRELOAD=libcld2_full.so`.

**Why Linux only?** The macOS equivalent (`DYLD_INSERT_LIBRARIES`) does not work because System Integrity Protection (SIP) strips all `DYLD_*` environment variables from child processes, including the JVM forked by Maven Surefire.

### Using Without Maven Profiles

If not using a profile, you must provide the native library yourself:

1. **Install system library** (see above), then:
   ```
   mvn clean verify -Djava.library.path=/usr/lib/x86_64-linux-gnu
   ```

2. **Or use JNA's classpath loading**: Place `libcld2.so`/`libcld2.dylib` on the classpath and JNA will find it.


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

To link the Java code with the native libraries when using the default build (without profiles), you need to make sure that Java can find the shared object:
- either install the native library on a standard library path (already done when the Debian package is used)
- add the directory where your libcld2.so installed to the environment variable `LD_LIBRARY_PATH`
- use the Java option `-Djava.library.path=...`

#### Java Native Access (JNA) and libffi

The CLD2 native functions are accessed via the [Java Native Access (JNA)](https://github.com/java-native-access/jna) which uses the [Foreign Function Interface Library (libffi)](https://sourceware.org/libffi/). JNA is a project dependency but libffi needs to be present on your system:
- Linux (Debian/Ubuntu): `apt-get install libffi-dev`
- macOS: `brew install libffi`

#### Platform Support

The bindings have been tested on Linux (x86-64, ARM64) and macOS (Intel, Apple Silicon).


## History

This package has derived from https://github.com/deezer/weslang (package [com.deezer.research.cld2](https://github.com/deezer/weslang/tree/master/java/com/deezer/research/cld2)), see the [original README](./README.deezer-weslang).

Further inspirations are taken from [CAFDataProcessing/worker-languagedetection](https://github.com/CAFDataProcessing/worker-languagedetection/tree/develop/language-detection-cld2), but this project depends on a modified version of CLD2 distributed only as a binary.

Modifications/improvements:
- extended interface
- support to pass as arguments Java objects of the classes Locale and Charset
- proper ISO-639-3 language codes for all 160 languages


## License

These bindings are [Apache 2.0 licensed](./LICENSE). Also [CLD2](https://github.com/CLD2Owners/cld2/blob/master/LICENSE), [weslang](https://github.com/deezer/weslang/blob/master/LICENSE) and all dependencies use the Apache 2.0 license.
