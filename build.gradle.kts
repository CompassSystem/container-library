import compass_system.mod_gradle_plugin.Utils.exclusiveRepo

plugins {
    id("mod-gradle-plugin")
}

mod {
    projects(":fabric", ":testfabric")
}

subprojects {
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

        exclusiveRepo("Siphalor", "https://maven.siphalor.de/") {
            includeGroup("de.siphalor")
        }
    }
}