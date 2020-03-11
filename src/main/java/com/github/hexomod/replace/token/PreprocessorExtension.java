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

import org.gradle.api.Project;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

@SuppressWarnings({"WeakerAccess","unused"})
public class PreprocessorExtension {

    /**
     * Name of the extension to use in build.gradle
     */
    public static final String EXTENSION_NAME = "replaceTokenPreprocessorSettings";
    /**
     * The current project
     */
    private final Project project;

    /**
     * Enable logging to console while preprocessing files
     */
    private boolean verbose;

    /**
     * Source root folders to process
     * If empty, default project source folder will be used.
     */
    private Set<String> sources;

    /**
     * Resources root folders to process
     * If empty, default project source folder will be used.
     */
    private Set<String> resources;

    /**
     * Target folder to place the preprocessed files
     */
    private File target;

    /**
     * File extensions to process
     */
    private Set<String> extensions = new HashSet<String>() {
        {
            add("java");
            add("properties");
            add("yaml");
            add("yml");
        }
    };

    /**
     * Map of variables
     */
    private Map<String, Object> replace = new LinkedHashMap<>();


    /**
     * @param project Current project
     */
    public PreprocessorExtension(final Project project) {
        this.project = project;
        this.verbose = false;
        this.sources = new LinkedHashSet<>();
        this.resources = new LinkedHashSet<>();
        this.target = new File(project.getBuildDir(), "preprocessor/replace");
    }


    public Project getProject() {
        return this.project;
    }


    public void setVerbose(boolean verbose) {
        this.verbose = verbose;
    }

    public boolean isVerbose() {
        return this.verbose;
    }


    public void setSources(String sources) {
        setSources(new HashSet<>(Collections.singletonList(sources)));
    }

    public void setSources(List<String> sources) {
        setSources(new HashSet<>(sources));
    }

    public void setSources(Set<String> sources) {
        this.sources.clear();
        setSource(sources);
    }

    public void setSource(String source) {
        setSource(new HashSet<>(Collections.singletonList(source)));
    }

    public void setSource(List<String> source) {
        setSource(new HashSet<>(source));
    }

    public void setSource(Set<String> sources) {
        // Check that all sources are valid
        for (String dir : sources) {
            Path path = new File(dir).toPath();
            if(Files.isDirectory(path) && Files.exists(path)) {
                this.sources.add(path.toAbsolutePath().toString());
                continue;
            }
            try {
                path = new File(this.project.getRootDir(), dir).toPath();
                if(Files.isDirectory(path) && Files.exists(path)) {
                    this.sources.add(path.toAbsolutePath().toString());
                }
            }
            catch (Exception ignored) {}
        }
    }

    public Set<String> getSources() {
        return this.sources;
    }


    public void setResources(String resources) {
        setResources(new HashSet<>(Collections.singletonList(resources)));
    }

    public void setResources(List<String> resources) {
        setResources(new HashSet<>(resources));
    }

    public void setResources(Set<String> resources) {
        this.resources.clear();
        setResource(resources);
    }

    public void setResource(String resource) {
        setResource(new HashSet<>(Collections.singletonList(resource)));
    }

    public void setResource(List<String> resource) {
        setResource(new HashSet<>(resource));
    }

    public void setResource(Set<String> resources) {
        // Check that all resources are valid
        for (String dir : resources) {
            Path path = new File(dir).toPath();
            if(Files.isDirectory(path) && Files.exists(path)) {
                this.resources.add(path.toAbsolutePath().toString());
                continue;
            }
            try {
                path = new File(this.project.getRootDir(), dir).toPath();
                if(Files.isDirectory(path) && Files.exists(path)) {
                    this.resources.add(path.toAbsolutePath().toString());
                }
            }
            catch (Exception ignored) {}
        }
    }

    public Set<String> getResources() {
        return this.resources;
    }


    public void setTarget(File target) {
        this.target = target;
    }

    public void setTarget(String target) {
        String buildName = this.project.getBuildDir().getName();
        if(target.startsWith(buildName)) {
            setTarget(new File(this.project.getBuildDir().getParentFile(), target));
        }
        else {
            setTarget(new File(this.project.getBuildDir(), target));
        }
    }

    public File getTarget() {
        return this.target;
    }


    public void setExtensions(String extension) {
        setExtensions(Collections.singletonList(extension));
    }

    public void setExtensions(List<String> extensions) {
        this.extensions.clear();
        setExtension(extensions);
    }

    public void setExtension(String extension) {
        if(extension.startsWith(".")) {
            this.extensions.add(extension.substring(1));
        }
        else {
            this.extensions.add(extension);
        }
    }

    public void setExtension(List<String> extensions) {
        for(String extension : extensions) {
            setExtension(extension);
        }
    }

    public Set<String> getExtensions() {
        return extensions;
    }


    public void setReplace(Map<String, Object> replace) {
        this.replace.putAll(replace);
    }

    public Map<String, Object> getReplace() {
        return this.replace;
    }
}
