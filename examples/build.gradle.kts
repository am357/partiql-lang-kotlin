/*
 * Copyright Amazon.com, Inc. or its affiliates. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License").
 * You may not use this file except in compliance with the License.
 * A copy of the License is located at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * or in the "license" file accompanying this file. This file is distributed
 * on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language governing
 * permissions and limitations under the License.
 */

plugins {
    id(Plugins.application)
    id(Plugins.conventions)
}

application {
    mainClass.set("org.partiql.examples.util.Main")
}

dependencies {
    implementation(project(":lang"))
    implementation(Deps.awsSdkS3)
}

// TODO: Once we upgrade kotlin version to 1.6+, we need to change the compile option to -opt-in
// Version 1.7+ removes the requirement for such compiler option.
tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
    kotlinOptions
        .freeCompilerArgs += "-Xopt-in=kotlin.RequiresOptIn"
}
