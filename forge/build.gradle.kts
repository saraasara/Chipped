architectury {
    forge()
}

loom {
    forge {
        mixinConfig("chipped.mixins.json")
    }
    runs {
        create("data") {
            data()
            programArgs("--all", "--mod", "chipped")
            programArgs("--output", project(":common").file("src/main/generated/resources").absolutePath)
            programArgs("--existing", project(":common").file("src/main/resources").absolutePath)
        }
    }
}

val common: Configuration by configurations.creating {
    configurations.compileClasspath.get().extendsFrom(this)
    configurations.runtimeClasspath.get().extendsFrom(this)
    configurations["developmentForge"].extendsFrom(this)
}

dependencies {
    val minecraftVersion: String by project
    val forgeVersion: String by project
    val reiVersion: String by project

    forge(group = "net.minecraftforge", name = "forge", version = "$minecraftVersion-$forgeVersion")
    modLocalRuntime(group = "me.shedaniel", name = "RoughlyEnoughItems-forge", version = reiVersion)
    modCompileOnly(group = "me.shedaniel", name = "RoughlyEnoughItems-api-forge", version = reiVersion)
    modCompileOnly(group = "me.shedaniel", name = "RoughlyEnoughItems-default-plugin", version = reiVersion)

    common(project(":common", configuration = "namedElements")) {
        isTransitive = false
    }
    shadowCommon(project(path = ":common", configuration = "transformProductionForge")) {
        isTransitive = false
    }
}

tasks.processResources {
    inputs.property("version", version)

    filesMatching("META-INF/mods.toml") {
        expand("version" to version)
    }
}
