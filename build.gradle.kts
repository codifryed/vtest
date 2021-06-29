plugins {
    java
    kotlin("jvm") version "1.5.20"
    application
    id("com.github.johnrengelman.shadow") version "6.1.0"
}

group = "com.guyboldon"
version = "1.0"

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation("com.github.ajalt.clikt", "clikt", "3.2.0")
}

application {
    mainClass.set("com.guyboldon.vtest.MainKt")
    mainClassName = "com.guyboldon.vtest.MainKt"
}
