pluginManagement {
    repositories {
        gradlePluginPortal()
    }

    plugins {
        kotlin("jvm") version "1.6.10"
        kotlin("plugin.serialization") version "1.6.10"
    }
}

rootProject.name = "APKUpdateServer"
include("APKUpdateBeans-Base")
include("APKUpdateBeans-Server")
include("APKUpdateSDK")
include("APKUpdateClient")
