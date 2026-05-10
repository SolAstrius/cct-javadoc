plugins {
    java
    `maven-publish`
}

group = "cc.tweaked"
version = "1.10.0-mainthread.2"

java {
    withJavadocJar()
    withSourcesJar()

    toolchain { languageVersion = JavaLanguageVersion.of(25) }
}

repositories {
    mavenCentral()
    exclusiveContent {
        forRepositories(maven("https://maven.squiddev.cc"))
        filter {
            includeGroup("cc.tweaked")
        }
    }
}

dependencies {
    compileOnly("com.google.code.findbugs:jsr305:3.0.2")
    implementation("com.google.auto.service:auto-service:1.0.1")

    testImplementation("cc.tweaked:cc-tweaked-1.20.1-core-api:1.113.1")

    testImplementation(platform("org.junit:junit-bom:6.0.2"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.test {
    useJUnitPlatform()
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            from(components["java"])
        }
    }

    repositories {
        maven("https://maven.squiddev.cc") {
            name = "SquidDev"
            credentials(PasswordCredentials::class)
        }
        maven("https://maven.pkg.github.com/SolAstrius/cct-javadoc") {
            name = "GitHubPackages"
            credentials {
                username = providers.gradleProperty("gpr.user")
                    .orElse(providers.environmentVariable("GITHUB_ACTOR"))
                    .orNull
                password = providers.gradleProperty("gpr.token")
                    .orElse(providers.environmentVariable("GITHUB_TOKEN"))
                    .orNull
            }
        }
    }
}
