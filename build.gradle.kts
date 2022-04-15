import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    application
    kotlin("jvm")
    kotlin("plugin.serialization")
}

group = "io.github.xfy9326"
version = "0.0.1"

application {
    applicationName = rootProject.name
    mainClass.set("io.github.xfy9326.apkupdate.ApplicationKt")
}

java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}

tasks.withType<KotlinCompile> {
    kotlinOptions.apply {
        jvmTarget = JavaVersion.VERSION_11.toString()
        freeCompilerArgs = freeCompilerArgs + "-Xopt-in=kotlinx.serialization.ExperimentalSerializationApi"
    }
}

repositories {
    mavenCentral()
}

dependencies {
    api(project(":APKUpdateBeans-Server"))

    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.1")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.3.2")

    implementation("com.github.ajalt.clikt:clikt:3.4.0")

    implementation("org.xerial:sqlite-jdbc:3.36.0.3")

    implementation("org.jetbrains.exposed:exposed-core:0.37.3")
    implementation("org.jetbrains.exposed:exposed-dao:0.37.3")
    implementation("org.jetbrains.exposed:exposed-jdbc:0.37.3")

    implementation("ch.qos.logback:logback-classic:1.2.11")

    implementation("io.ktor:ktor-server-core:2.0.0")
    implementation("io.ktor:ktor-server-cio:2.0.0")
    implementation("io.ktor:ktor-server-auth:2.0.0")
    implementation("io.ktor:ktor-server-status-pages:2.0.0")
    implementation("io.ktor:ktor-server-content-negotiation:2.0.0")
    implementation("io.ktor:ktor-serialization-kotlinx-json:2.0.0")

    testImplementation(kotlin("test"))
    testImplementation("io.ktor:ktor-server-test-host:2.0.0")
    testImplementation("io.ktor:ktor-client-auth:2.0.0")
    testImplementation("io.ktor:ktor-client-content-negotiation:2.0.0")
}