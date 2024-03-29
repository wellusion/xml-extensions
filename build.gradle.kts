import org.gradle.kotlin.dsl.*
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.jetbrains.kotlin.jvm") version "1.4.10"
    java

    id("maven-publish")
    id("signing")
}

group = "com.github.wellusion"
version = "1.12"

repositories {
    mavenCentral()
}

var slf4jVer = "1.7.30"
var commonsIoVer = "2.8.0"
var junitVer = "4.13.1"

dependencies {
    implementation("org.slf4j", "slf4j-api", slf4jVer)

    testImplementation("commons-io", "commons-io", commonsIoVer)
    testImplementation("junit", "junit", junitVer)
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

// maven-publish
val sourcesJar by tasks.registering(Jar::class) {
    dependsOn(tasks.classes)
    archiveClassifier.set("sources")
    from(sourceSets.main.get().allSource)
}

val javadocJar by tasks.registering(Jar::class) {
    archiveClassifier.set("javadoc")
    from(tasks.javadoc)
}

publishing {
    publications.register<MavenPublication>("publication") {
        artifact("$buildDir/libs/${project.name}-${version}.jar")
        artifact(sourcesJar)
        artifact(javadocJar)

        pom {
            name.set("xml-extensions")
            description.set("A set of extensions for working with `org.w3c.dom` package.")
            url.set("https://github.com/wellusion/xml-extensions")
            licenses {
                license {
                    name.set("MIT License")
                    url.set("https://opensource.org/licenses/MIT")
                    distribution.set("repo")
                }
            }
            developers {
                developer {
                    id.set("wellusion")
                    name.set("wellusion")
                    email.set("wellusion@gmail.com")
                }
            }
            scm {
                connection.set("scm:git:github.com/wellusion/xml-extensions.git")
                developerConnection.set("scm:git:ssh://github.com/wellusion/xml-extensions.git")
                url.set("https://github.com/wellusion/xml-extensions/tree/master")
            }

            withXml {
                val dependenciesNode = asNode().appendNode("dependencies")
                configurations.implementation.get().allDependencies.forEach {
                    val dependencyNode = dependenciesNode.appendNode("dependency")
                    dependencyNode.appendNode("groupId", it.group)
                    dependencyNode.appendNode("artifactId", it.name)
                    dependencyNode.appendNode("version", it.version)
                }
            }
        }
    }

    repositories {
        maven {
            name = "sonatype"

            val releasesRepoUrl = uri("https://oss.sonatype.org/service/local/staging/deploy/maven2/")
            val snapshotsRepoUrl = uri("https://oss.sonatype.org/content/repositories/snapshots/")
            url = if (version.toString().endsWith("SNAPSHOT")) snapshotsRepoUrl else releasesRepoUrl

            if (hasProperty("ossrhUsername") && hasProperty("ossrhPassword")) {
                credentials {
                    username = property("ossrhUsername") as String
                    password = property("ossrhPassword") as String
                }
            }
        }
    }
}

signing {
    sign(publishing.publications)
}