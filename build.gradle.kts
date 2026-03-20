plugins {
    kotlin("jvm") version "2.3.20"
}

group = "org.neptrueworks.lox"

dependencies {
    testImplementation(libs.junit.jupiter)
}

tasks.compileKotlin {
    compilerOptions {
        freeCompilerArgs.add("-Xwarning-level=NOTHING_TO_INLINE:disabled")
        freeCompilerArgs.add("-XXLanguage:+ExplicitBackingFields")
    }
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(21)
}