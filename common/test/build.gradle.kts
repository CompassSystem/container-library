import compass_system.mod_gradle_plugin.ModDependencies
import compass_system.mod_gradle_plugin.Utils.modProject

plugins {
    kotlin("jvm") version "1.9.22"
}

group = "compass_system"

dependencies {
    modCompileOnly("net.fabricmc:fabric-language-kotlin:1.10.17+kotlin.1.9.22")
}
