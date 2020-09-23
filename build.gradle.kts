import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.4.10"
}
group = "dev.ligpo"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}
dependencies {
    implementation("io.undertow", "undertow-core", "2.1.3.Final")

    testImplementation(kotlin("test-junit5"))
    testImplementation("org.junit.jupiter", "junit-jupiter", "5.6.2")
    testImplementation("io.github.rybalkinsd", "kohttp", "0.12.0")
}
tasks.withType<KotlinCompile>() {
    kotlinOptions.jvmTarget = "11"
}

tasks {
    test {
        useJUnitPlatform {
            excludeTags("slow")
        }
        testLogging {
            events("passed", "skipped", "failed")
        }
    }
}