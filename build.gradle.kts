plugins {
    id("mod-gradle-plugin")
}

mod {
    projects(":common", ":common:test", ":fabric", ":fabric:test", ":neoforge", ":neoforge:test")
}