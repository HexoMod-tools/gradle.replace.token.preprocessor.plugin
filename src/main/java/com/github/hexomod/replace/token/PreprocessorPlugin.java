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


import com.github.hexomod.replace.token.tasks.PreprocessorJavaTask;
import com.github.hexomod.replace.token.tasks.PreprocessorResourcesTask;
import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.ProjectConfigurationException;
import org.gradle.api.plugins.BasePlugin;
import org.gradle.api.plugins.JavaPlugin;
import org.gradle.api.plugins.JavaPluginConvention;
import org.gradle.api.tasks.SourceSet;
import org.gradle.api.tasks.SourceSetContainer;
import org.gradle.api.tasks.TaskProvider;
import org.gradle.api.tasks.compile.JavaCompile;
import org.gradle.language.jvm.tasks.ProcessResources;
import org.gradle.util.GUtil;

import java.io.File;

@SuppressWarnings({"unused"})
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

        // Configure preprocessors
        project.afterEvaluate( root -> {
            configurePreprocessor(project, extension);
        });
    }

    private PreprocessorExtension configureExtension(Project project) {
        SourceSetContainer sourceSets = project.getConvention().getPlugin(JavaPluginConvention.class).getSourceSets();
        return project.getExtensions().create(
                PreprocessorExtension.NAME
                , PreprocessorExtension.class
                , project
                , sourceSets);
    }

    private void configurePreprocessor(Project project, final PreprocessorExtension extension) {
        for(SourceSet sourceSet : extension.getSourceSets()) {
            if(extension.getEnable() && extension.getJava().getEnable()) {
                final JavaCompile compileTask = (JavaCompile) project.getTasks().findByName(sourceSet.getCompileJavaTaskName());
                registerJavaTask(project, extension, sourceSet, compileTask).get();
            }
            if(extension.getEnable() && extension.getResources().getEnable()) {
                final ProcessResources resourceTask = (ProcessResources) project.getTasks().findByName(sourceSet.getProcessResourcesTaskName());
                registerResourcesTask(project, extension, sourceSet, resourceTask).get();
            }
        }
    }

    private TaskProvider<PreprocessorJavaTask> registerJavaTask(Project project, PreprocessorExtension extension, SourceSet sourceSet, JavaCompile compileTask) {
        return project.getTasks().register(PreprocessorJavaTask.TASK_ID + (sourceSet.getName() == "main" ? "" : GUtil.toCamelCase(sourceSet.getName())), PreprocessorJavaTask.class, preprocessor -> {
            preprocessor.setDescription("Replace variables in source code.");
            preprocessor.setGroup(BasePlugin.BUILD_GROUP);
            preprocessor.setSourceSet(sourceSet);

            File destination = new File(new File(extension.getProcessDir(),"java"), sourceSet.getName());

            preprocessor.setSource(sourceSet.getAllJava());
            preprocessor.setDestinationDir(destination);

            compileTask.setSource(destination);
            compileTask.dependsOn(preprocessor);

            preprocessor.doFirst( task -> {
            });

            preprocessor.doLast( task -> {
            });
        });
    }

    private TaskProvider<PreprocessorResourcesTask> registerResourcesTask(Project project, PreprocessorExtension extension, SourceSet sourceSet, ProcessResources resourcesTask) {
        return project.getTasks().register(PreprocessorResourcesTask.TASK_ID + (sourceSet.getName() == "main" ? "" : GUtil.toCamelCase(sourceSet.getName())), PreprocessorResourcesTask.class, preprocessor -> {
            preprocessor.setDescription("Replace variables in source code.");
            preprocessor.setGroup(BasePlugin.BUILD_GROUP);
            preprocessor.setSourceSet(sourceSet);

            File destination = new File(new File(extension.getProcessDir(),"resources"), sourceSet.getName());

            preprocessor.from(sourceSet.getResources().getSrcDirs());
            preprocessor.setDestinationDir(destination);

            resourcesTask.from(preprocessor.getDestinationDir());
            resourcesTask.dependsOn(preprocessor);

            preprocessor.doFirst( task -> {
            });

            preprocessor.doLast( task -> {
            });
        });
    }
}
