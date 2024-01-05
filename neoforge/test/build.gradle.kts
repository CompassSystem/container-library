import compass_system.mod_gradle_plugin.ModDependencies
import compass_system.mod_gradle_plugin.Utils.modProject

plugins {
    kotlin("jvm") version "1.9.22"
}

group = "compass_system"

val modDependencies = project(":neoforge").ext["mod_dependencies"] as ModDependencies

dependencies {
    modImplementation("thedarkcolour:kotlinforforge-neoforge:4.10.0")
    modProject(":neoforge")

    modDependencies.iterateRuntimeDependencies { dependency -> add("modRuntimeOnly", dependency) }
}
