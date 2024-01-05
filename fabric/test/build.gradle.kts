import compass_system.mod_gradle_plugin.ModDependencies
import compass_system.mod_gradle_plugin.Utils.modProject

plugins {
    kotlin("jvm") version "1.9.22"
}

group = "compass_system"

val modDependencies = project(":fabric").ext["mod_dependencies"] as ModDependencies

dependencies {
    modImplementation("net.fabricmc:fabric-language-kotlin:1.10.17+kotlin.1.9.22")
    modProject(":fabric")

    modDependencies.iterateRuntimeDependencies { dependency ->
        add("modRuntimeOnly", dependency) {
            exclude(group = "net.fabricmc")
            exclude(group = "net.fabricmc.fabric-api")
        }
    }
}
