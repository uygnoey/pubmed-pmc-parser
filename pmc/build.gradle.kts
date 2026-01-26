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

description = "PMC (PubMed Central) XML parser with JATS 1.4 support"

dependencies {
    // Common 모듈 의존성
    api(project(":common"))

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
    testImplementation("org.mockito:mockito-core:5.8.0")
    testImplementation("org.mockito:mockito-junit-jupiter:5.8.0")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")

    // TAR.GZ 압축 파일 처리 (PMC는 tar.gz 아카이브 사용)
    implementation("org.apache.commons:commons-compress:1.26.0")
    implementation("commons-codec:commons-codec:1.15")
}

tasks.test {
    useJUnitPlatform()
}

// JAR 설정
tasks.jar {
    archiveBaseName.set("pubmed-pmc-parser-pmc")
    manifest {
        attributes(
            "Automatic-Module-Name" to "io.brillianttiger.bio.parser.pmc"
        )
    }
}

// Javadoc 설정 - CI에서는 실행 안 함
tasks.javadoc {
    enabled = false  // Disable javadoc generation completely
}

// Fat JAR 생성 (PMC 단독 실행 가능)
tasks.register<Jar>("fatJar") {
    archiveClassifier.set("all")
    archiveBaseName.set("pmc-parser")
    from(sourceSets.main.get().output)
    dependsOn(configurations.runtimeClasspath)
    from({
        configurations.runtimeClasspath.get()
            .filter { it.name.endsWith("jar") }
            .map { zipTree(it) }
    })
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE

    manifest {
        attributes(
            "Main-Class" to "io.brillianttiger.bio.parser.pmc.PmcXmlParser"
        )
    }
}
