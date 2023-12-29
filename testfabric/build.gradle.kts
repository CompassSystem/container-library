plugins {
    kotlin("jvm") version "1.9.22"
}

group = "compass_system"


dependencies {
    modImplementation("net.fabricmc:fabric-language-kotlin:1.10.17+kotlin.1.9.22")
    api(project(":fabric", configuration = "namedElements"))
    implementation(findProject(":fabric")!!.sourceSets.getByName("client").output)
}
