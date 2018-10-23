import org.jetbrains.kotlin.gradle.plugin.KotlinPluginWrapper
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    application
    id("org.jetbrains.kotlin.jvm") version "1.3.0-rc-190"
    id ("com.github.johnrengelman.shadow") version "2.0.4"
    id("io.spring.dependency-management") version "1.0.6.RELEASE"
}

// Tweak to be sure to have compiler and dependency versions the same
extra["kotlin.version"] = "1.3.0-rc-190"

repositories {
    mavenCentral()
    maven("http://dl.bintray.com/kotlin/kotlin-eap")
    maven("https://jcenter.bintray.com")
}

application {
    mainClassName = "net.borak.Application"
}

tasks.withType(KotlinCompile::class.java).all {
    kotlinOptions {
        jvmTarget = "1.8"
        freeCompilerArgs = listOf("-Xjsr305=strict", "-Xjvm-default=enable")
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
    compile("org.jetbrains.kotlin:kotlin-stdlib-jdk8:1.3.0-rc-190")
    compile("org.jetbrains.kotlin:kotlin-reflect:1.3.0-rc-190")
    compile("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.0.0-RC1")

    // Test
    compile("org.springframework:spring-test")
    testCompile(group = "junit", name = "junit", version = "4.12")
    testCompile("io.projectreactor:reactor-test")
    testCompile("org.assertj:assertj-core")
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.3.1")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.3.1")
    testCompile(group = "org.mockito", name = "mockito-core", version = "2.23.0")
    testCompile(group = "net.bytebuddy", name = "byte-buddy", version = "1.9.+")
    testCompile(group = "net.bytebuddy", name = "byte-buddy-agent", version = "1.9.+")
    testCompile("com.nhaarman:mockito-kotlin:1.6.0")

    // Logging
    compile("org.slf4j:slf4j-api")
    compile("ch.qos.logback:logback-classic:1.2.3")

    // Web & HTTP
    compile(group = "org.apache.httpcomponents", name = "httpclient", version = "4.5.6")
    compile("org.springframework:spring-webflux")
    compile("io.projectreactor.ipc:reactor-netty")

    // Dependency Injection
    compile("org.springframework:spring-context")

    // Serialization
    compile (group = "com.fasterxml.jackson.core", name = "jackson-databind", version = "2.9.7")
    compile (group = "com.fasterxml.jackson.core", name = "jackson-core", version = "2.9.7")
    compile (group = "com.fasterxml.jackson.core", name = "jackson-annotations", version = "2.9.7")
    compile (group = "com.fasterxml.jackson.datatype", name = "jackson-datatype-joda", version = "2.9.7")
    compile ("com.fasterxml.jackson.module:jackson-module-kotlin:2.9.+")
    compile ("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:2.9.+")

    // Utils
    compile (group = "com.typesafe", name = "config", version = "1.3.3")
    compile (group = "joda-time", name = "joda-time", version = "2.10")
    compile (group = "commons-io", name = "commons-io", version = "2.6")
    compile (group = "org.apache.commons", name = "commons-lang3", version = "3.8.1")
}