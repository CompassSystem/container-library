pluginManagement {
    repositories {
        maven {
            name = "Fabric Maven"
            url = uri("https://maven.fabricmc.net/")
        }
        maven {
            name = "Architectury Maven"
            url = uri("https://maven.architectury.dev/")
        }
        maven {
            name = "NeoForge Maven"
            url = uri("https://maven.neoforged.net/release")
        }
        gradlePluginPortal()
        mavenLocal()
    }
}

rootProject.name = "container-library"

include(":fabric")
include(":fabric:test")
