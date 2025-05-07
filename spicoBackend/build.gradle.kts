plugins {
    kotlin("jvm") version "1.9.25"
    kotlin("plugin.spring") version "1.9.25"
    kotlin("plugin.jpa") version "1.9.25"
    kotlin("kapt") version "1.9.25"
    id("org.springframework.boot") version "3.4.5"
    id("io.spring.dependency-management") version "1.1.7"
}

group = "com.ssafy.spico"
version = "0.0.1-SNAPSHOT"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    implementation("org.springframework.boot:spring-boot-starter-validation")

    implementation(kotlin("stdlib-jdk8"))
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")

    testRuntimeOnly("org.junit.platform:junit-platform-launcher")

    // JPA (hibernate 포함)
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")

    // 쿼리 DSL 의존성 추가
    implementation("com.querydsl:querydsl-jpa:5.0.0:jakarta")
    kapt("com.querydsl:querydsl-apt:5.0.0:jakarta")
    kapt("jakarta.annotation:jakarta.annotation-api")
    kapt("jakarta.persistence:jakarta.persistence-api")

    compileOnly("org.projectlombok:lombok")
    annotationProcessor("org.projectlombok:lombok")

    /* jwt */
    implementation("io.jsonwebtoken:jjwt-api:0.12.5")
    runtimeOnly("io.jsonwebtoken:jjwt-impl:0.12.5")
    runtimeOnly("io.jsonwebtoken:jjwt-jackson:0.12.5")
    /* mariaDB */
    implementation("org.mariadb.jdbc:mariadb-java-client:3.5.2")
    /* prometheus */
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    runtimeOnly("io.micrometer:micrometer-registry-prometheus")
}

kotlin {
    compilerOptions {
        freeCompilerArgs.addAll("-Xjsr305=strict")
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}

// QueryDSL 경로 설정
val querydslDir = "$buildDir/generated/querydsl"

// 자바 소스도 src/main/kotlin에서 찾도록 명시
sourceSets {
    main {
        java {
            setSrcDirs(listOf("src/main/kotlin"))
        }

        kotlin.srcDir("build/generated/querydsl") // Q파일 포함
    }
}

// annotationProcessor가 생성하는 Q클래스 경로 지정
tasks.withType<JavaCompile> {
    options.annotationProcessorGeneratedSourcesDirectory = file(querydslDir)
}

// clean 시 생성된 Q클래스 디렉토리 삭제
tasks.named("clean") {
    doLast {
        delete(querydslDir)
    }
}