import org.gradle.kotlin.dsl.*
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

gradle.apply(from = "publish-maven.gradle.kts")

plugins {
    id("org.jetbrains.kotlin.jvm") version "1.3.61"
    java
}

group = "com.github.wellusion"
version = "1.01-SNAPSHOT"

repositories {
    mavenCentral()
    maven { url =  uri("https://plugins.gradle.org/m2/") }
}

var slf4jVer = "1.7.30"
var commonsIoVer = "2.6"

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("org.slf4j", "slf4j-api", slf4jVer)

    testImplementation("commons-io", "commons-io", commonsIoVer)
    testImplementation("junit", "junit", "4.13")
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

