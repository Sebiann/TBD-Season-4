import xyz.jpenilla.resourcefactory.bukkit.BukkitPluginYaml
import java.io.BufferedReader

val commitHash = Runtime
    .getRuntime()
    .exec(arrayOf("git", "rev-parse", "--short", "HEAD"))
    .let { process ->
        process.waitFor()
        val output = process.inputStream.use {
            it.bufferedReader().use(BufferedReader::readText)
        }
        process.destroy()
        output.trim()
    }

plugins {
    kotlin("jvm") version "2.1.0"
    id("com.gradleup.shadow") version "8.3.5"
    id("io.papermc.paperweight.userdev") version "2.0.0-beta.11"
    id("xyz.jpenilla.run-paper") version "2.3.1" // Adds runServer and runMojangMappedServer tasks for testing
    id("xyz.jpenilla.resource-factory-bukkit-convention") version "1.2.0" // Generates plugin.yml based on the Gradle config
}

group = "net.tbdsmp"
version = "Build-$commitHash"
description = "The future is now!"

bukkitPluginYaml {
    main = "SeasonFourPlugin"
    load = BukkitPluginYaml.PluginLoadOrder.STARTUP
    authors.add("Austin Albrecht")
    apiVersion = "1.21.4"
}

kotlin {
    jvmToolchain(21)
    compilerOptions {
        javaParameters = true
    }
}

repositories {
    maven {
        name = "papermc"
        url = uri("https://repo.papermc.io/repository/maven-public/")
    }
}

dependencies {
    implementation(kotlin("stdlib"))
    paperweight.paperDevBundle("1.21.4-R0.1-SNAPSHOT")
}

tasks {
    compileJava {
        options.release = 21
    }
    javadoc {
        options.encoding = Charsets.UTF_8.name() // We want UTF-8 for everything
    }

    shadowJar {
        isEnableRelocation = true
        relocationPrefix = "net.tbdsmp.minecrafttwo.shade"
    }
}

