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


import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.ProjectConfigurationException;
import org.gradle.api.plugins.JavaPlugin;
import org.gradle.api.plugins.JavaPluginConvention;
import org.gradle.api.tasks.CacheableTask;
import org.gradle.api.tasks.SourceSet;
import org.gradle.api.tasks.SourceSetContainer;
import org.gradle.api.tasks.TaskProvider;
import org.gradle.api.tasks.compile.JavaCompile;
import org.gradle.language.jvm.tasks.ProcessResources;

import java.io.File;
import java.util.Collections;
import java.util.stream.Collectors;


@SuppressWarnings({"unused"})
@CacheableTask
public class PreprocessorPlugin implements Plugin<Project> {

    @Override
    public void apply(Project project) {

        // Preprocessor require either java plugin or java-library plugin
        if(!project.getPluginManager().hasPlugin("java") && !project.getPluginManager().hasPlugin("java-library")) {
            throw new ProjectConfigurationException("The \"java\" or \"java-library\" plugin is required by MacroPreprocessor plugin.",new java.lang.Throwable("void apply(Project project)"));
        }

        // Make sure java plugin is applied
        project.getPluginManager().apply(JavaPlugin.class);

        // Configure extension
        PreprocessorExtension extension = configureExtension(project);

        // Register and configure preprocessors task
        project.afterEvaluate( root -> {
            RegisterPreprocessors(project, extension);
        });
    }

    private PreprocessorExtension configureExtension(Project project) {
        return project.getExtensions().create(
                PreprocessorExtension.NAME
                , PreprocessorExtension.class
                , project);
    }

    private void RegisterPreprocessors(Project project, PreprocessorExtension extension) {
        // Get all sourceSet to create one preprocessor per sourceSet
        final SourceSetContainer sourceSets = project.getConvention().getPlugin(JavaPluginConvention.class).getSourceSets();

        // Register each preprocessor
        for(SourceSet sourceSet : sourceSets) {
            // Java files
            if(extension.getEnable() && extension.getJava().getEnable()) {
                final JavaCompile compileTask = (JavaCompile) project.getTasks().findByName(sourceSet.getCompileJavaTaskName());
                PreprocessorTask preprocessor = RegisterJavaPreprocessor(project, extension, sourceSet, compileTask).get();
                compileTask.dependsOn(preprocessor);
            }
            // Resources files
            if(extension.getEnable() && extension.getResources().getEnable()) {
                final ProcessResources resourceTask = (ProcessResources) project.getTasks().findByName(sourceSet.getProcessResourcesTaskName());
                PreprocessorTask preprocessor = RegisterResourcesPreprocessor(project, extension, sourceSet, resourceTask).get();
                resourceTask.dependsOn(preprocessor);
            }
        }
    }

    private TaskProvider<PreprocessorTask> RegisterJavaPreprocessor(Project project, PreprocessorExtension extension, SourceSet sourceSet, JavaCompile compileTask) {
        return project.getTasks().register(PreprocessorTask.getJavaTaskName(sourceSet), PreprocessorTask.class, preprocessor -> {
            preprocessor.setDescription("Replace variables in source code.");
            preprocessor.setGroup("preprocessor");
            preprocessor.setSourceSet(sourceSet);
            preprocessor.from(sourceSet.getJava().getSrcDirs());
            preprocessor.exclude(sourceSet.getResources().getSrcDirs().stream().map(File::getPath).collect(Collectors.toList()));
            preprocessor.setDestinationDir(new File(new File(extension.getProcessDir(), sourceSet.getName()),"java"));

            sourceSet.getJava().setSrcDirs(Collections.singletonList(preprocessor.getDestinationDir()));

            preprocessor.doFirst( task -> {
            });

            preprocessor.doLast( task -> {
            });
        });
    }

    private TaskProvider<PreprocessorTask> RegisterResourcesPreprocessor(Project project, PreprocessorExtension extension, SourceSet sourceSet, ProcessResources resourcesTask) {
        return project.getTasks().register(PreprocessorTask.getResourceTaskName(sourceSet), PreprocessorTask.class, preprocessor -> {
            preprocessor.setDescription("Replace variables in source code.");
            preprocessor.setGroup("preprocessor");
            preprocessor.setSourceSet(sourceSet);
            preprocessor.from(sourceSet.getResources().getSrcDirs());
            preprocessor.setDestinationDir(new File(new File(extension.getProcessDir(), sourceSet.getName()),"resources"));

            sourceSet.getResources().setSrcDirs(Collections.singletonList(preprocessor.getDestinationDir()));

            preprocessor.doFirst( task -> {
            });

            preprocessor.doLast( task -> {
            });
        });
    }
}
