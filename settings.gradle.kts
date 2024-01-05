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
            url = uri("https://maven.neoforged.net/releases")
        }
        gradlePluginPortal()
        mavenLocal()
    }
}

rootProject.name = "container-library"

include(":fabric", ":fabric:test")
include(":neoforge", ":neoforge:test")
