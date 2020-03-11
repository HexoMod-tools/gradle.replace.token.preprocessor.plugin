/*
 * This file is part of ReplaceTokenPreprocessor, licensed under the MIT License (MIT).
 *
 * Copyright (c) 2019 Hexosse <https://github.com/hexomod-tools/gradle.replace.token.preprocessor.plugin>
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.github.hexomod.replace.token;

import org.junit.Test;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static org.junit.Assert.assertEquals;

public class PreprocessorTest {

    private Set<String> extensions = new HashSet<String>() {
        {
            add("java");
            add("properties");
            add("yaml");
            add("yml");
        }
    };

    private Map<String, Object> replace = new HashMap<String, Object>() {{
        put("@VAR_STRING@", "value_string");
        put("@VAR_BOOL@", true);
        put("@VAR_INT@", 1);
        put("@VAR_DOUBLE@", 1.32);
    }};

    @Test
    public void processLine() {
        Preprocessor preprocessor = new Preprocessor(extensions, replace);
        assertEquals(preprocessor.processLine("VAR_STRING: @VAR_STRING@"), "VAR_STRING: value_string");
        assertEquals(preprocessor.processLine("VAR_BOOL: @VAR_BOOL@"), "VAR_BOOL: true");
        assertEquals(preprocessor.processLine("VAR_INT: @VAR_INT@"), "VAR_INT: 1");
        assertEquals(preprocessor.processLine("VAR_DOUBLE: @VAR_DOUBLE@"), "VAR_DOUBLE: 1.32");
    }

}