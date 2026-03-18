plugins {
    kotlin("jvm") version "2.3.20"
}

group = "org.neptrueworks.lox"

dependencies {
    testImplementation(libs.junit.jupiter)
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(21)
}