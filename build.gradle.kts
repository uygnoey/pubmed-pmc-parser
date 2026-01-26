import org.gradle.api.tasks.testing.Test
import org.gradle.testing.jacoco.tasks.JacocoReport
import org.gradle.testing.jacoco.tasks.JacocoCoverageVerification
import org.gradle.testing.jacoco.plugins.JacocoTaskExtension
import org.gradle.api.plugins.JavaPlugin
import org.gradle.api.tasks.SourceSetContainer
import org.gradle.api.publish.PublishingExtension
import org.gradle.api.publish.maven.MavenPublication
import org.gradle.api.tasks.javadoc.Javadoc
import org.gradle.external.javadoc.StandardJavadocDocletOptions
import org.gradle.api.plugins.quality.Checkstyle
import org.gradle.api.plugins.quality.CheckstyleExtension

plugins {
    id("io.freefair.lombok") version "8.11" apply false
}

group = "io.brillianttiger.bio"
version = "1.0.0-SNAPSHOT"

allprojects {
    repositories {
        mavenCentral()
    }
}

subprojects {
    repositories {
        mavenCentral()
    }

    group = rootProject.group
    version = rootProject.version

    // 서브프로젝트의 플러그인이 적용된 후 설정
    afterEvaluate {
        // Java 플러그인 설정
        extensions.findByType<JavaPluginExtension>()?.apply {
            toolchain {
                languageVersion.set(JavaLanguageVersion.of(21))
            }
            withJavadocJar()
            withSourcesJar()
        }

        // 공통 의존성
        dependencies {
            // Lombok
            add("compileOnly", "org.projectlombok:lombok:1.18.36")
            add("annotationProcessor", "org.projectlombok:lombok:1.18.36")
            add("testCompileOnly", "org.projectlombok:lombok:1.18.36")
            add("testAnnotationProcessor", "org.projectlombok:lombok:1.18.36")

            // Logging
            add("implementation", "org.slf4j:slf4j-api:2.0.9")
            add("runtimeOnly", "ch.qos.logback:logback-classic:1.4.14")

            // Testing
            add("testImplementation", "org.junit.jupiter:junit-jupiter:5.10.1")
            add("testImplementation", "org.assertj:assertj-core:3.24.2")
            add("testRuntimeOnly", "org.junit.platform:junit-platform-launcher")
        }

        // Test 설정
        tasks.findByName("test")?.let { test ->
            (test as Test).apply {
                useJUnitPlatform()
                tasks.findByName("jacocoTestReport")?.let { finalizedBy(it) }
                workingDir = projectDir
            }
        }

        // JaCoCo 설정
        tasks.findByName("jacocoTestReport")?.let { report ->
            (report as JacocoReport).apply {
                tasks.findByName("test")?.let { dependsOn(it) }
                reports {
                    xml.required.set(true)
                    html.required.set(true)
                    csv.required.set(false)
                }
            }
        }

        // JaCoCo 커버리지 검증
        if (tasks.findByName("jacocoTestReport") != null &&
            tasks.findByName("jacocoTestCoverageVerification") == null) {
            tasks.register<JacocoCoverageVerification>("jacocoTestCoverageVerification") {
                tasks.findByName("jacocoTestReport")?.let { dependsOn(it) }
                violationRules {
                    rule {
                        limit {
                            minimum = "0.80".toBigDecimal()
                        }
                    }
                }
            }
        }

        // Checkstyle 설정 (서브모듈에서 이미 설정되어 있으므로 추가 설정 불필요)

        // Javadoc 설정
        tasks.findByName("javadoc")?.let { javadoc ->
            (javadoc as Javadoc).apply {
                options {
                    encoding = "UTF-8"
                    (this as StandardJavadocDocletOptions).apply {
                        locale = "en_US"
                        charSet = "UTF-8"
                        docEncoding = "UTF-8"
                        links(
                            "https://docs.oracle.com/en/java/javase/21/docs/api/",
                            "https://javadoc.io/doc/org.slf4j/slf4j-api/latest/"
                        )
                        addStringOption("Xdoclint:none", "-quiet")
                    }
                }
            }
        }

        // JAR manifest 설정
        tasks.findByName("jar")?.let { jar ->
            (jar as Jar).apply {
                manifest {
                    attributes(
                        "Implementation-Title" to project.name,
                        "Implementation-Version" to project.version,
                        "Implementation-Vendor" to "Brilliant Tiger",
                        "Built-By" to System.getProperty("user.name"),
                        "Built-JDK" to System.getProperty("java.version"),
                        "Created-By" to "Gradle ${gradle.gradleVersion}"
                    )
                }
            }
        }

        // Maven 퍼블리싱 설정
        extensions.findByType<PublishingExtension>()?.apply {
            publications {
                create<MavenPublication>("maven") {
                    from(components["java"])

                    pom {
                        name.set("${project.group}:${project.name}")
                        description.set("PubMed and PMC XML Parser Library")
                        url.set("https://github.com/brillianttiger/pubmed-pmc-parser")

                        licenses {
                            license {
                                name.set("The Apache License, Version 2.0")
                                url.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
                            }
                        }

                        developers {
                            developer {
                                id.set("brillianttiger")
                                name.set("Brilliant Tiger")
                                email.set("dev@brillianttiger.com")
                            }
                        }

                        scm {
                            connection.set("scm:git:git://github.com/brillianttiger/pubmed-pmc-parser.git")
                            developerConnection.set("scm:git:ssh://github.com/brillianttiger/pubmed-pmc-parser.git")
                            url.set("https://github.com/brillianttiger/pubmed-pmc-parser")
                        }
                    }
                }
            }
        }
    }
}

// 루트 프로젝트 JaCoCo 통합 리포트
tasks.register<JacocoReport>("jacocoRootReport") {
    dependsOn(subprojects.map { it.tasks.named("test") })

    executionData.setFrom(
        subprojects.map {
            it.tasks.withType<Test>().map { test ->
                test.extensions.getByType<JacocoTaskExtension>().destinationFile!!
            }
        }.flatten()
    )

    subprojects.forEach { project ->
        project.plugins.withType<JavaPlugin> {
            val sourceSets = project.the<SourceSetContainer>()
            sourceDirectories.from(sourceSets["main"].allSource.srcDirs)
            classDirectories.from(sourceSets["main"].output)
        }
    }

    reports {
        xml.required.set(true)
        html.required.set(true)
        csv.required.set(false)
        html.outputLocation.set(layout.buildDirectory.dir("reports/jacoco/html"))
    }
}

// Clean 태스크
tasks.register<Delete>("cleanAll") {
    delete(layout.buildDirectory)
    dependsOn(subprojects.map { it.tasks.named("clean") })
}

// 전체 테스트 실행
tasks.register("testAll") {
    dependsOn(subprojects.map { it.tasks.named("test") })
    finalizedBy("jacocoRootReport")
}

// 전체 빌드
tasks.register("buildAll") {
    dependsOn(subprojects.map { it.tasks.named("build") })
}

// Checkstyle 전체 실행
tasks.register("checkstyleAll") {
    dependsOn(subprojects.flatMap { it.tasks.withType<Checkstyle>() })
}
