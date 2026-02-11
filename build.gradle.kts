plugins {
    java
    `maven-publish`
    id ("com.github.gmazzo.buildconfig") version "5.6.7"
}

group = "top.mrxiaom.example"
version = "1.0.0"
val targetJavaVersion = 8

repositories {
    mavenCentral()
    maven("https://repo.codemc.io/repository/maven-public/")
    maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
    maven("https://repo.helpch.at/releases/")
    maven("https://jitpack.io")
    maven("https://repo.rosewooddev.io/repository/public/")
}

dependencies {
    compileOnly("org.spigotmc:spigot-api:1.20-R0.1-SNAPSHOT")
    // compileOnly("org.spigotmc:spigot:1.20") // NMS
    compileOnly("org.jetbrains:annotations:24.0.0")

    compileOnly("net.milkbowl.vault:VaultAPI:1.7")
    compileOnly("org.black_ixx:playerpoints:3.2.7")
    compileOnly(files("libs/MPoints-1.2.2.jar"))
    compileOnly("com.github.nulli0n:CoinsEngine-spigot:c32f037025")
    compileOnly("com.github.blank038:NyEconomy:8e3f27c18f")
    compileOnly("me.clip:placeholderapi:2.11.6")
}
buildConfig {
    className("BuildConstants")
    packageName("top.mrxiaom.example.economy")

    buildConfigField("String", "VERSION", "\"${project.version}\"")
}
java {
    val javaVersion = JavaVersion.toVersion(targetJavaVersion)
    if (JavaVersion.current() < javaVersion) {
        toolchain.languageVersion.set(JavaLanguageVersion.of(targetJavaVersion))
    }
    withSourcesJar()
    withJavadocJar()
}
tasks {
    withType<JavaCompile>().configureEach {
        options.encoding = "UTF-8"
        if (targetJavaVersion >= 10 || JavaVersion.current().isJava10Compatible) {
            options.release.set(targetJavaVersion)
        }
    }
    processResources {
        duplicatesStrategy = DuplicatesStrategy.INCLUDE
        from(sourceSets.main.get().resources.srcDirs) {
            expand(mapOf("version" to version))
            include("plugin.yml")
        }
    }
    javadoc {
        (options as StandardJavadocDocletOptions).apply {
            locale("zh_CN")
            encoding("UTF-8")
            docEncoding("UTF-8")
            addBooleanOption("keywords", true)
            addBooleanOption("Xdoclint:none", true)
        }
    }
}
publishing {
    publications {
        create<MavenPublication>("maven") {
            groupId = project.group.toString()
            artifactId = rootProject.name
            version = project.version.toString()

            artifact(tasks["jar"])
            artifact(tasks["sourcesJar"])
            artifact(tasks["javadocJar"])
        }
    }
}
