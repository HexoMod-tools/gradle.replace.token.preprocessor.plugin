# gradle-replace-token-preprocessor-plugin
[![License: MIT](https://img.shields.io/badge/License-MIT-brightgreen.svg?style=flat-square)](https://opensource.org/licenses/MIT)

A simple replace token preprocessor for java

# How to use

The preprocessor is published in Gradle central
```gradle
plugin {
    id: 'com.github.hexomod.replace.token.preprocessor'
}
```

# Usage

```gradle
ext {
    VERSION = "1.0"
}

macroPreprocessorSettings {
    verbose true

    replace '@VERSION@': project.ext.VERSION
}
```

# Internal test sample
- [sample](sample)