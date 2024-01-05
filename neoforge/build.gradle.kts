import compass_system.mod_gradle_plugin.ModDependencies
import compass_system.mod_gradle_plugin.Utils.exclusiveRepo

plugins {
    kotlin("jvm") version "1.9.22"
}

group = "compass_system"

val modDependencies = ModDependencies().apply {
    add("emi") {
        val emiVersion = "1.0.29+1.20.4" // https://modrinth.com/mod/emi/versions
        compileOnly("dev.emi:emi-neoforge:${emiVersion}:api")
        runtimeOnly("dev.emi:emi-neoforge:${emiVersion}")
    }

    add("inventory_profiles", "inventory-profiles-next") {
        val ipnVersion = "forge-1.20.2-1.10.9" // https://modrinth.com/mod/inventory-profiles-next/versions
        val libIpnVersion = "forge-1.20-4.0.1" // https://modrinth.com/mod/libipn/versions
        implementation("maven.modrinth:inventory-profiles-next:$ipnVersion")
        implementation("maven.modrinth:libipn:$libIpnVersion")
    }

//    add("jei") {
//        val jeiMcVersion = "1.20.2"
//        val jeiVersion = "16.0.0.28" // https://modrinth.com/mod/jei/versions
//        compileOnly("mezz.jei:jei-$jeiMcVersion-common-api:$jeiVersion")
//        compileOnly("mezz.jei:jei-$jeiMcVersion-fabric-api:$jeiVersion")
//        runtimeOnly("mezz.jei:jei-$jeiMcVersion-fabric:$jeiVersion")
//    }

    add("rei") {
        val reiVersion = "14.0.688" // https://modrinth.com/mod/rei/versions
        compileOnly("me.shedaniel:RoughlyEnoughItems-api-neoforge:$reiVersion")
        runtimeOnly("me.shedaniel:RoughlyEnoughItems-neoforge:$reiVersion")
    }
}

modDependencies.enableMods()

ext["mod_dependencies"] = modDependencies

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
    modImplementation("thedarkcolour:kotlinforforge-neoforge:4.10.0")

    modDependencies.iterateCompileDependencies { dependency -> add("modCompileOnly", dependency) }
}

tasks {
    getByName<Jar>("jar") {
        from("LICENSE")
    }
}

modrinth {
    dependencies {
        required.project("kotlin-for-forge")

        modDependencies.getModrinthIds().forEach {
            optional.project(it)
        }
    }
}

