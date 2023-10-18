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

import com.mmoczkowski.chart.publicationId
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.publish.PublishingExtension
import org.gradle.api.publish.maven.MavenPublication
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.get

class MavenConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("maven-publish")
            }
            project.extensions.configure<PublishingExtension> {
                publications {
                    create(project.publicationId, MavenPublication::class.java) {
                        from(components["java"])

                        groupId = "com.mmoczkowski"
                        artifactId = "chart-${project.path.substring(1).replace(":", "-")}"
                        version = System.getenv("VERSION")
                        description = "Compose Desktop Map library"

                        pom {
                            name.set("Chart")
                            description.set("Compose Desktop Map library")
                            url.set("http://github.com/mmoczkowski/chart")
                            licenses {
                                license {
                                    name.set("The Apache License, Version 2.0")
                                    url.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
                                }
                            }
                            developers {
                                developer {
                                    id.set("mmoczkowski")
                                    name.set("Miłosz Moczkowski")
                                    email.set("milosz.moczkowski@gmail.com")
                                }
                            }
                            scm {
                                url.set("https://github.com/mmoczkowski")
                            }
                        }

                    }
                }
                repositories {
                    maven {
                        val ossrhRepositoryUrl = System.getenv("OSSHR_REPOSITORY")
                        if (ossrhRepositoryUrl != null) {
                            url = uri(ossrhRepositoryUrl)
                            credentials {
                                username = System.getenv("OSSHR_USERNAME")
                                password = System.getenv("OSSHR_PASSWORD")
                            }
                        }
                    }
                }
            }
        }
    }
}
