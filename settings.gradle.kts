plugins {
    id("me.champeau.includegit") version "0.3.2" apply true
}

dependencyResolutionManagement {
    repositoriesMode = RepositoriesMode.FAIL_ON_PROJECT_REPOS
    repositories {
        mavenCentral()
        maven("https://hub.spigotmc.org/nexus/content/groups/public/")
    }
}

gitRepositories {
    include("MockBukkit") {
        uri = "https://github.com/PineBale/MockBukkit.git"
        branch = "v1.8-spigot"
    }
}

rootProject.name = "SpigotOfBauble29"
