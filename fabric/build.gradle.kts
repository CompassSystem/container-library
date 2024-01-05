import compass_system.mod_gradle_plugin.ModDependencies
import compass_system.mod_gradle_plugin.Utils.exclusiveRepo
import compass_system.mod_gradle_plugin.Utils.modProject

plugins {
    kotlin("jvm") version "1.9.22"
}

group = "compass_system"

val modDependencies = ModDependencies().apply {
    add("amecs") {
        val amecsMcVersion = "1.20"
        val amecsApiVersion = "1.5.6+mc1.20.2" // https://maven.siphalor.de/de/siphalor/amecsapi-1.20/
        val amecsVersion = "1.3.11+mc.1.20.4" // https://maven.siphalor.de/de/siphalor/amecs-1.20/
        compileOnly("de.siphalor:amecsapi-$amecsMcVersion:$amecsApiVersion")
        runtimeOnly("de.siphalor:amecs-$amecsMcVersion:$amecsVersion")
    }

    add("emi") {
        val emiVersion = "1.0.28+1.20.4" // https://modrinth.com/mod/emi/versions
        compileOnly("dev.emi:emi-fabric:${emiVersion}:api")
        runtimeOnly("dev.emi:emi-fabric:${emiVersion}")
    }

    add("inventory_profiles", "inventory-profiles-next") {
        val ipnVersion = "fabric-1.20.3-pre2-1.10.9" // https://modrinth.com/mod/inventory-profiles-next/versions
        val libIpnVersion = "fabric-1.20.3-pre2-4.0.1" // https://modrinth.com/mod/libipn/versions
        implementation("maven.modrinth:inventory-profiles-next:$ipnVersion")
        implementation("maven.modrinth:libipn:$libIpnVersion")
    }

    add("jei") {
        val jeiMcVersion = "1.20.2"
        val jeiVersion = "16.0.0.28" // https://modrinth.com/mod/jei/versions
        compileOnly("mezz.jei:jei-$jeiMcVersion-common-api:$jeiVersion")
        compileOnly("mezz.jei:jei-$jeiMcVersion-fabric-api:$jeiVersion")
        runtimeOnly("mezz.jei:jei-$jeiMcVersion-fabric:$jeiVersion")
    }

    add("modmenu") {
        val modmenuVersion = "9.0.0" // https://modrinth.com/mod/modmenu/versions
        implementation("com.terraformersmc:modmenu:$modmenuVersion")
    }

    add("rei") {
        val reiVersion = "14.0.680" // https://modrinth.com/mod/rei/versions
        compileOnly("me.shedaniel:RoughlyEnoughItems-api-fabric:$reiVersion")
        compileOnly("me.shedaniel.cloth:basic-math:0.6.1")
        runtimeOnly("me.shedaniel:RoughlyEnoughItems-fabric:$reiVersion")
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

        exclusiveRepo("Siphalor", "https://maven.siphalor.de/") {
            includeGroup("de.siphalor")
        }
    }
}

dependencies {
    modImplementation("net.fabricmc:fabric-language-kotlin:1.10.17+kotlin.1.9.22")

    modProject(":common")

    modDependencies.iterateCompileDependencies { dependency ->
        add("modCompileOnly", dependency) {
            exclude(group = "net.fabricmc")
            exclude(group = "net.fabricmc.fabric-api")
        }
    }
}

tasks {
    getByName<Jar>("jar") {
        from("LICENSE")
    }
}

modrinth {
    dependencies {
        required.project("fabric-api")
        required.project("fabric-lanugage-kotlin")

        modDependencies.getModrinthIds().forEach {
            optional.project(it)
        }
    }
}

