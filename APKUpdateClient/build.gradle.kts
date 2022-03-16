import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    application
    kotlin("jvm")
    kotlin("plugin.serialization")
}

group = "io.github.xfy9326"
version = "0.0.1"

val projectMainClass = "io.github.xfy9326.apkupdate.client.ApplicationKt"
val projectAuthor = "XFY9326"

application {
    applicationName = project.name
    mainClass.set(projectMainClass)
}

tasks.register<Jar>("assembleJar") {
    dependsOn("jar")

    manifest {
        attributes["Main-Class"] = projectMainClass
        attributes["Specification-Title"] = project.name
        attributes["Specification-Version"] = project.version
        attributes["Specification-Vendor"] = projectAuthor
    }

    destinationDirectory.set(File(project.buildDir, "distributions"))
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE

    from(tasks.compileJava.get().destinationDirectory.get())
    from(tasks.compileKotlin.get().destinationDirectory.get())
    from(tasks.processResources.get().destinationDir)
    from(
        configurations.compileClasspath.get().map {
            if (it.isDirectory) it else zipTree(it)
        }
    )
}

repositories {
    mavenCentral()
}

java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}

tasks.withType<KotlinCompile> {
    kotlinOptions.apply {
        jvmTarget = JavaVersion.VERSION_11.toString()
    }
}

dependencies {
    api(project(":APKUpdateBeans-Server"))
    implementation("com.github.ajalt.clikt:clikt:3.4.0")
    implementation("io.ktor:ktor-client-cio:1.6.8")
    implementation("io.ktor:ktor-client-serialization:1.6.8")
    implementation("io.ktor:ktor-client-auth:1.6.8")
}