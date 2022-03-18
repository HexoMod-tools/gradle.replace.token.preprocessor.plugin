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

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

@SuppressWarnings({"WeakerAccess", "unused"})
public class Preprocessor {

    private final List<String> extensions;
    private final Map<String, Object> replace;

    public Preprocessor(Set<String> extensions, Map<String, Object> replace) {
        this.extensions = new ArrayList<>(extensions);
        this.replace = replace;
    }

    public void process(File inFile, File outFile) throws IOException {
        String fileExtension = FilenameUtils.getExtension(inFile.getName());
        // First check if the file need to be processed
        // If not, the file is just copied to its destination
        if (!this.extensions.contains(fileExtension)) {
            if (!inFile.equals(outFile)) {
                FileUtils.copyFile(inFile, outFile);
            }
        }
        // If yes, the file is processed
        else {
            //
            try {
                // Convert input file to list of lines
                List<String> lines = FileUtils.readLines(inFile, StandardCharsets.UTF_8);
                // Process lines
                lines = processLines(lines);
                // Create parent folder if needed
                FileUtils.forceMkdirParent(outFile);
                // Write output file
                FileUtils.writeLines(outFile, StandardCharsets.UTF_8.toString(), lines, "\n", false);
            } catch (Exception e) {
                throw new RuntimeException("Failed to convert file " + inFile, e);
            }
        }
    }

    List<String> processLines(List<String> lines) {
        List<String> newLines = new ArrayList<>();
        // Loop through all lines
        for (String line : lines) {
            newLines.add(processLine(line));
        }
        return newLines;
    }

    String processLine(String line) {
        final String[] newLine = {line};
        this.replace.forEach((key, value) -> {
            newLine[0] = newLine[0].replace(key, value.toString());
        });
        return newLine[0];
    }
}
