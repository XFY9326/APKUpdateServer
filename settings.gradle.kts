pluginManagement {
    repositories {
        gradlePluginPortal()
    }

    plugins {
        kotlin("jvm") version "1.6.20"
        kotlin("plugin.serialization") version "1.6.20"
    }
}

rootProject.name = "APKUpdateServer"
include("APKUpdateBeans-Base")
include("APKUpdateBeans-Server")
include("APKUpdateSDK")
include("APKUpdateClient")
