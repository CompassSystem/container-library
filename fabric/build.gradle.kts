import compass_system.mod_gradle_plugin.ModDependencies
import compass_system.mod_gradle_plugin.Utils.exclusiveRepo

group = "compass_system"

val modDependencies = ModDependencies().apply {
    add("amecs") {
        val amecsMcVersion = "1.20"
        val amecsApiVersion = "1.3.9+mc1.20-pre1" // https://maven.siphalor.de/de/siphalor/amecsapi-1.20/
        val amecsVersion = "1.3.10+mc.1.20.1" // https://maven.siphalor.de/de/siphalor/amecs-1.20/
        compileOnly("de.siphalor:amecsapi-$amecsMcVersion:$amecsApiVersion")
        runtimeOnly("de.siphalor:amecs-$amecsMcVersion:$amecsVersion")
    }

    add("emi") {
        val emiVersion = "1.0.28+1.20.1" // https://modrinth.com/mod/emi/versions
        compileOnly("dev.emi:emi-fabric:${emiVersion}:api")
        runtimeOnly("dev.emi:emi-fabric:${emiVersion}")
    }

    add("inventory_profiles", "inventory-profiles-next") {
        val ipnVersion = "fabric-1.20-1.10.9" // https://modrinth.com/mod/inventory-profiles-next/versions
        val libIpnVersion = "fabric-1.20-4.0.1" // https://modrinth.com/mod/libipn/versions
        implementation("maven.modrinth:inventory-profiles-next:$ipnVersion")
        implementation("maven.modrinth:libipn:$libIpnVersion")
    }

    add("jei") {
        val jeiMcVersion = "1.20.1"
        val jeiVersion = "15.2.0.27" // https://modrinth.com/mod/jei/versions
        compileOnly("mezz.jei:jei-$jeiMcVersion-common-api:$jeiVersion")
        compileOnly("mezz.jei:jei-$jeiMcVersion-fabric-api:$jeiVersion")
        runtimeOnly("mezz.jei:jei-$jeiMcVersion-fabric:$jeiVersion")
    }

    add("modmenu") {
        val modmenuVersion = "7.2.2" // https://modrinth.com/mod/modmenu/versions
        implementation("com.terraformersmc:modmenu:$modmenuVersion")
    }

    add("rei") {
        val reiVersion = "12.0.684" // https://modrinth.com/mod/rei/versions
        implementation("me.shedaniel:RoughlyEnoughItems-fabric:$reiVersion")
    }
}

modDependencies.enableMods()

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

dependencies {
    modImplementation("net.fabricmc:fabric-language-kotlin:1.10.17+kotlin.1.9.22")

    modDependencies.iterateCompileDependencies { dependency ->
        add("modCompileOnly", dependency) {
            exclude(group = "net.fabricmc")
            exclude(group = "net.fabricmc.fabric-api")
        }
    }

    modDependencies.iterateRuntimeDependencies { dependency ->
        add("modRuntimeOnly", dependency) {
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

