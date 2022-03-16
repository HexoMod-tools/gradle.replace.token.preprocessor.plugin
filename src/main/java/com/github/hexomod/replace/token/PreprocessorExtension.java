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

import com.github.hexomod.replace.token.extensions.Java;
import com.github.hexomod.replace.token.extensions.Resources;
import com.github.hexomod.replace.token.extensions.SourceType;
import groovy.lang.Closure;
import org.gradle.api.Action;
import org.gradle.api.Project;
import org.gradle.api.internal.project.ProjectInternal;
import org.gradle.api.tasks.SourceSetContainer;
import org.gradle.internal.Actions;
import org.gradle.util.ConfigureUtil;

import java.io.File;
import java.util.*;

@SuppressWarnings({"WeakerAccess","unused"})
public class PreprocessorExtension extends SourceType {

    /**
     * Name of the extension to use in build.gradle
     */
    public static final String NAME = "replaceTokenPreprocessorSettings";

    /**
     * The current project
     */
    private final Project project;

    /**
     * Project SourceSet
     */
    private final SourceSetContainer sourceSets;

    /**
     * Map of variables to repalce
     */
    private Map<String, Object> replace = new LinkedHashMap<>();

    /**
     * Directory where files will be processed
     */
    private File processDir;

    /**
     * Enable logging to console while preprocessing files
     */
    private boolean verbose;

    /**
     * java files configuration
     */
    private final Java java;

    /**
     * resources files configuration
     */
    private final Resources resources;

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


    public PreprocessorExtension(ProjectInternal project, SourceSetContainer sourceSets) {
        this.project = project;
        this.sourceSets = sourceSets;
        this.processDir = new File(project.getBuildDir(), "preprocessor/replace");
        this.verbose = false;
        this.java = new Java();
        this.resources = new Resources();
    }


    public SourceSetContainer getSourceSets() {
        return sourceSets;
    }


    public Map<String, Object> getReplace() {
        return this.replace;
    }

    public void setReplace(Map<String, Object> replace) {
        this.replace.putAll(replace);
    }


    public File getProcessDir() {
        return processDir;
    }

    public void setProcessDir(File processDir) {
        this.processDir = processDir;
    }

    public void setProcessDir(String processDir) {
        String buildName = this.project.getBuildDir().getName();
        if(processDir.startsWith(buildName)) {
            setProcessDir(new File(this.project.getBuildDir().getParentFile(), processDir));
        }
        else {
            setProcessDir(new File(this.project.getBuildDir(), processDir));
        }
    }


    public boolean getVerbose() {
        return verbose;
    }

    public void setVerbose(boolean verbose) {
        this.verbose = verbose;
    }


    public Set<String> getExtensions() {
        return extensions;
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


    public Java getJava() {
        return java;
    }

    public Java java(Closure closure) {
        return ConfigureUtil.configure(closure, java);
    }

    public Java java(Action<? super Java> action) {
        return Actions.with(java, action);
    }


    public Resources getResources() {
        return resources;
    }

    public Resources resources(Closure closure) {
        return ConfigureUtil.configure(closure, resources);
    }

    public Resources resources(Action<? super Resources> action) {
        return Actions.with(resources, action);
    }


    // Print out a string if verbose is enabled
    public void log(String msg) {
        if(getVerbose()) {
            System.out.println(msg);
        }
    }
}
