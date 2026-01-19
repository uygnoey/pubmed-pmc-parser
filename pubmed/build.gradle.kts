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

description = "PubMed XML parser with streaming support"

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
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")

    // GZip 압축 파일 처리
    implementation("org.apache.commons:commons-compress:1.26.0")
    implementation("commons-codec:commons-codec:1.15")
}

tasks.test {
    useJUnitPlatform()

    // 대용량 파일 파싱을 위한 메모리 증가
    minHeapSize = "512m"
    maxHeapSize = "2048m"  // 2GB
}

// JAR 설정
tasks.jar {
    archiveBaseName.set("pubmed-pmc-parser-pubmed")
    manifest {
        attributes(
            "Automatic-Module-Name" to "com.brillianttiger.bio.parser.pubmed"
        )
    }
}

// Javadoc 추가 설정
tasks.javadoc {
    title = "PubMed & PMC Parser - PubMed Module"
    (options as StandardJavadocDocletOptions).apply {
        overview = "src/main/java/overview.html"
        group("PubMed Models", "com.brillianttiger.bio.parser.pubmed.model")
        group("PubMed Parsers", "com.brillianttiger.bio.parser.pubmed.parser")
        group("PubMed Validation", "com.brillianttiger.bio.parser.pubmed.validation")
    }
}

// Fat JAR 생성 (PubMed 단독 실행 가능)
tasks.register<Jar>("fatJar") {
    archiveClassifier.set("all")
    archiveBaseName.set("pubmed-parser")
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
            "Main-Class" to "com.brillianttiger.bio.parser.pubmed.PubmedXmlParser"
        )
    }
}
