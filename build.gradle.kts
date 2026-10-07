plugins {
    `java-library`
}

group = "net.klyde"
version = "1.1"

description = "A dependency-free Java library for formatting and parsing numbers and durations."

repositories {
    mavenCentral()
}

dependencies {
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

tasks.jar {
    manifest {
        attributes(
            "Implementation-Title" to project.name,
            "Implementation-Version" to project.version,
            "Implementation-Vendor" to "myraclez"
        )
    }
}
