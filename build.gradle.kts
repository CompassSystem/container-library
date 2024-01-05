plugins {
    id("mod-gradle-plugin")
}

mod {
    projects(":fabric", ":fabric:test", ":neoforge", ":neoforge:test")
}