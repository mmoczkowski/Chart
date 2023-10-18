/*
 * Copyright (C) 2023 Miłosz Moczkowski
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    alias(libs.plugins.jb.compose)
    alias(libs.plugins.chart.jvm)
}

val sampleVersion = "1.0.0"
group = "com.mmoczkowski.chart.sample.basic"
version = sampleVersion

dependencies {
    implementation(projects.cache.api)
    implementation(projects.cache.impl.lru)
    implementation(projects.provider.api)
    implementation(projects.provider.impl.openStreetMap)
    implementation(projects.ui)
    implementation(compose.desktop.currentOs)
    implementation(libs.compose.desktop)
    implementation(libs.compose.material.icons.extended)

}

compose.desktop {
    application {
        mainClass = "com.mmoczkowski.chart.sample.basic.MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "com.mmoczkowski.chart.sample"
            packageVersion = sampleVersion
        }
    }
}
