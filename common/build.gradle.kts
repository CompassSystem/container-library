import compass_system.mod_gradle_plugin.ModDependencies
import compass_system.mod_gradle_plugin.Utils.exclusiveRepo

plugins {
    kotlin("jvm") version "1.9.22"
}

group = "compass_system"

val modDependencies = ModDependencies().apply {
    add("emi") {
        val emiVersion = "1.0.29+1.20.4" // https://modrinth.com/mod/emi/versions
        compileOnly("dev.emi:emi-xplat-intermediary:${emiVersion}:api")
    }

    add("jei") {
        val jeiMcVersion = "1.20.2"
        val jeiVersion = "16.0.0.28" // https://modrinth.com/mod/jei/versions
        compileOnly("mezz.jei:jei-$jeiMcVersion-common-api:$jeiVersion")
    }

    add("rei") {
        val reiVersion = "14.0.688" // https://modrinth.com/mod/rei/versions
        compileOnly("me.shedaniel:RoughlyEnoughItems-api:$reiVersion")
        compileOnly("me.shedaniel.cloth:basic-math:0.6.1")
    }
}

allprojects {
    repositories {
        exclusiveRepo("TerraformersMC", "https://maven.terraformersmc.com/") {
            includeGroup("com.terraformersmc")
            includeGroup("dev.emi")
        }

        exclusiveRepo("Jared", "https://maven.blamejared.com/") {
            includeGroup("mezz.jei")
        }

        exclusiveRepo("Shedaniel", "https://maven.shedaniel.me/") {
            includeGroup("me.shedaniel")
            includeGroup("me.shedaniel.cloth")
        }
    }
}

dependencies {
    // todo: replace with just kotlin libraries
    modCompileOnly("net.fabricmc:fabric-language-kotlin:1.10.17+kotlin.1.9.22")

    modDependencies.iterateCompileDependencies { dependency ->
        add("modCompileOnly", dependency) {
            exclude(group = "net.fabricmc")
            exclude(group = "net.fabricmc.fabric-api")
        }
    }
}
