import org.jetbrains.kotlin.gradle.plugin.KotlinPluginWrapper
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    application
    id("org.jetbrains.kotlin.jvm") version "1.2.71"
    id ("com.github.johnrengelman.shadow") version "2.0.4"
    id("io.spring.dependency-management") version "1.0.5.RELEASE"
}

// Tweak to be sure to have compiler and dependency versions the same
extra["kotlin.version"] = "1.2.71"

repositories {
    mavenCentral()
}

application {
    mainClassName = "net.borak.Application"
}

tasks.withType(KotlinCompile::class.java).all {
    kotlinOptions {
        jvmTarget = "1.8"
        freeCompilerArgs = listOf("-Xjsr305=strict")
    }
}

val test by tasks.getting(Test::class) {
    useJUnitPlatform()
}

dependencyManagement {
    imports {
        mavenBom("org.springframework.boot:spring-boot-dependencies:2.0.2.RELEASE")
    }
}

dependencies {
    // Kotlin
    compile("org.jetbrains.kotlin:kotlin-stdlib-jdk8:1.2.71")
    compile("org.jetbrains.kotlin:kotlin-reflect:1.2.71")

    // Test
    compile("org.springframework:spring-test")
    testCompile(group = "junit", name = "junit", version = "4.12")
    testCompile("io.projectreactor:reactor-test")
    testCompile("org.assertj:assertj-core")
    testImplementation("org.junit.jupiter:junit-jupiter-api")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")

    // Logging
    compile("org.slf4j:slf4j-api")
    compile("ch.qos.logback:logback-classic:1.2.3")

    // Web & HTTP
    compile("io.ktor:ktor-server-netty:0.9.5")
    compile(group = "org.apache.httpcomponents", name = "httpclient", version = "4.5.6")
    compile("org.springframework:spring-webflux")
    compile("io.projectreactor.ipc:reactor-netty")

    // Dependency Injection
    implementation("org.koin:koin-core:1.0.1")
    compile("org.springframework:spring-context")

    // Serialization
    compile (group = "com.fasterxml.jackson.core", name = "jackson-databind", version = "2.9.7")
    compile (group = "com.fasterxml.jackson.core", name = "jackson-core", version = "2.9.7")
    compile (group = "com.fasterxml.jackson.core", name = "jackson-annotations", version = "2.9.7")
    compile (group = "com.fasterxml.jackson.datatype", name = "jackson-datatype-joda", version = "2.9.7")
    compile ("com.fasterxml.jackson.module:jackson-module-kotlin:2.9.+")
    compile ("com.fasterxml.jackson.datatype:jackson-datatype-jsr310")

    // Utils
    compile (group = "com.typesafe", name = "config", version = "1.3.3")
    compile (group = "joda-time", name = "joda-time", version = "2.10")
    compile (group = "commons-io", name = "commons-io", version = "2.6")
    compile (group = "org.apache.commons", name = "commons-lang3", version = "3.8.1")
}