/*
 * Copyright 2015 Max Kaeufer
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.amkay.gradle.gitflow.strategy

import com.github.amkay.gradle.gitflow.dsl.GitflowPluginExtension
import com.github.amkay.gradle.gitflow.version.NearestVersionLocator
import com.github.amkay.gradle.gitflow.version.VersionWithType
import com.github.amkay.gradle.gitflow.version.VersionWithTypeBuilder
import org.ajoberstar.grgit.Grgit

import static com.github.amkay.gradle.gitflow.version.VersionType.DEVELOP

/**
 * The strategy to use when Gitflow's <code>develop</code> branch is the current branch.
 *
 * @author Max Kaeufer
 */
class BranchDevelopStrategy extends AbstractStrategy {

    private static final String CONFIG_BRANCH_DEVELOP  = 'develop'
    private static final String DEFAULT_BRANCH_DEVELOP = 'develop'


    @Override
    protected VersionWithType doInfer(final Grgit grgit, final GitflowPluginExtension ext) {
        def nearestVersion = new NearestVersionLocator().locate(grgit)

        new VersionWithTypeBuilder(nearestVersion)
          .branch(ext.preReleaseIds.develop)
          .distanceFromRelease()
          .sha(grgit, ext)
          .dirty(grgit, ext)
          .type(DEVELOP)
          .build()
    }

    @Override
    boolean canInfer(final Grgit grgit) {
        grgit.branch.current.name == getDevelopBranchName(grgit)
    }

    private static String getDevelopBranchName(final Grgit grgit) {
        getBranchName(grgit, CONFIG_BRANCH_DEVELOP) ?: DEFAULT_BRANCH_DEVELOP
    }

}
