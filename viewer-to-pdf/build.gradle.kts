plugins {
    kotlin("jvm") version "1.8.0"
    application
}

group = "com.gmail.dev.wasacz"
version = "0.1"

repositories {
    mavenCentral()
}

dependencies {
    implementation("com.itextpdf:itext7-core:7.2.5")
    implementation("org.slf4j:slf4j-nop:2.0.6")
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(17)
}

application {
    mainClass.set("MainKt")
}