rootProject.name = "io.noroutine.oauth2-demo"

pluginManagement {
    repositories {
        maven(url = "https://nexus.nrtn.dev/repository/maven-public")
    }
}

@Suppress("UnstableApiUsage")
dependencyResolutionManagement {
    repositoriesMode = RepositoriesMode.FAIL_ON_PROJECT_REPOS
    repositories {
        maven(url = "https://nexus.nrtn.dev/repository/maven-public/")
    }
}