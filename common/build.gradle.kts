plugins {
    `java-library`
    jacoco
    checkstyle
    `maven-publish`
    id("io.freefair.lombok")
}

repositories {
    mavenCentral()
}

description = "Common utilities and base classes for PubMed and PMC parsers"

dependencies {
    // Lombok
    compileOnly("org.projectlombok:lombok:1.18.36")
    annotationProcessor("org.projectlombok:lombok:1.18.36")
    testCompileOnly("org.projectlombok:lombok:1.18.36")
    testAnnotationProcessor("org.projectlombok:lombok:1.18.36")

    // Logging
    implementation("org.slf4j:slf4j-api:2.0.9")
    runtimeOnly("ch.qos.logback:logback-classic:1.4.14")

    // Testing
    testImplementation("org.junit.jupiter:junit-jupiter:5.10.1")
    testImplementation("org.assertj:assertj-core:3.24.2")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")

    // Apache Commons IO (파일 유틸리티)
    implementation("commons-io:commons-io:2.15.1")

    // Apache Commons Lang (문자열 유틸리티)
    implementation("org.apache.commons:commons-lang3:3.14.0")
}

tasks.test {
    useJUnitPlatform()
}

// JAR 설정
tasks.jar {
    archiveBaseName.set("pubmed-pmc-parser-common")
    manifest {
        attributes(
            "Automatic-Module-Name" to "com.brillianttiger.bio.parser.common"
        )
    }
}

// Javadoc 추가 설정
tasks.javadoc {
    title = "PubMed & PMC Parser - Common Module"
    (options as StandardJavadocDocletOptions).apply {
        overview = "src/main/java/overview.html"
    }
}
