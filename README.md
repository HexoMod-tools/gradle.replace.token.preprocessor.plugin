# gradle-replace-token-preprocessor-plugin
[![License: MIT](https://img.shields.io/badge/License-MIT-brightgreen.svg?style=flat-square)](https://opensource.org/licenses/MIT)

A simple replace token preprocessor for java

# How to use

The preprocessor is published in [Gradle central](https://plugins.gradle.org/plugin/com.github.hexomod.macro.preprocessor).

Using the plugins DSL:
```gradle
plugins {
  id "com.github.hexomod.replace.token.preprocessor" version "0.3"
}
```

Using legacy plugin application:
```gradle
buildscript {
  repositories {
    maven {
      url "https://plugins.gradle.org/m2/"
    }
  }
  dependencies {
    classpath "com.github.hexomod:ReplaceTokenPreprocessor:0.2"
  }
}

apply plugin: "com.github.hexomod.replace.token.preprocessor"
```


# Usage

```gradle
ext {
    VERSION = "1.0.0"
    VERSION_NUM = "10000"
}

macroPreprocessorSettings {
    verbose = false     // default: false

    //extensions = [ ".properties",  ".yaml",  ".yml" ]
    //extension = ".java"

    java {
        enable = true   // default: true
    }

    resources {
        enable = true   // default: true
    }

    replace '@VERSION@': project.ext.VERSION
    replace '@VERSION_NUM@': project.ext.VERSION_NUM
}
```

# Examples
- [basic](samples/basic)