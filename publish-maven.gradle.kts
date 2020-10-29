// import org.gradle.api.tasks.bundling.Jar
//
// plugins {
//     kotlin("jvm") version "1.3.61"
//     id("maven-publish")
//     id("signing")
// }
//
// val sourcesJar by tasks.registering(Jar::class) {
//     classifier = "sources"
//     from(sourceSets.main.get().allSource)
// }
//
//
// tasks.javadocJar(type: Jar) {
//     classifier = "javadoc"
//     java.withSourcesJar()
// }
//
// def ossrhProperties = new Properties()
// def fOssrhProperties = project.rootProject.file("ossrh/ossrh.properties")
// if (fOssrhProperties.exists()) {
//     ossrhProperties.load(fOssrhProperties.newDataInputStream())
// }
//
// ext["signing.keyId"] = ossrhProperties["signing.keyId"]
// ext["signing.password"] = ossrhProperties["signing.password"]
// ext["signing.secretKeyRingFile"] = ossrhProperties["signing.secretKeyRingFile"]
// ext["ossrhUsername"] = ossrhProperties["ossrhUsername"]
// ext["ossrhPassword"] = ossrhProperties["ossrhPassword"]
//
// publishing {
//     publications {
//
//         release(MavenPublication) {
//             artifact("$buildDir/libs/${project.getName()}-${version}.jar")
//             artifact sourcesJar1
//             artifact javadocJar
//
//             pom {
//                 name = "xml-extensions"
//                 description = "A set of extensions for working with `org.w3c.dom` package."
//                 url = "https://github.com/wellusion/xml-extensions"
//                 licenses {
//                     license {
//                         name = "MIT License"
//                         url = "https://opensource.org/licenses/MIT"
//                     }
//                 }
//                 developers {
//                     developer {
//                         id = "wellusion"
//                         name = "wellusion"
//                         email = "wellusion@gmail.com"
//                     }
//                 }
//                 scm {
//                     connection = "scm:git:github.com/wellusion/xml-extensions.git"
//                     developerConnection = "scm:git:ssh://github.com/wellusion/xml-extensions.git"
//                     url = "https://github.com/wellusion/xml-extensions/tree/master"
//                 }
//
//                 withXml {
//                     def dependenciesNode = asNode().appendNode("dependencies")
//
//                     project.configurations.implementation.allDependencies.each {
//                         def dependencyNode = dependenciesNode.appendNode("dependency")
//                         dependencyNode.appendNode("groupId", it.group)
//                         dependencyNode.appendNode("artifactId", it.name)
//                         dependencyNode.appendNode("version", it.version)
//                     }
//                 }
//             }
//         }
//     }
//
//     repositories {
//         maven {
//             name = "sonatype"
//
//             def releasesRepoUrl = "https://oss.sonatype.org/service/local/staging/deploy/maven2/"
//             def snapshotsRepoUrl = "https://oss.sonatype.org/content/repositories/snapshots/"
//             url = version.endsWith("SNAPSHOT") ? snapshotsRepoUrl : releasesRepoUrl
//
//             credentials {
//                 username ossrhUsername
//                 password ossrhPassword
//             }
//         }
//     }
// }
//
// signing {
//     sign publishing.publications
// }