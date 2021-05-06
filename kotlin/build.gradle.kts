import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    java
    kotlin("jvm") version "1.4.30"
    id("me.champeau.jmh") version "0.6.4"
}

group "will"
version "0.1"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.7.1")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.7.1")
    testImplementation("org.junit.jupiter:junit-jupiter-params:5.7.1")
    testImplementation("org.assertj:assertj-core:3.17.1")

    jmh("org.openjdk.jmh:jmh-core:1.29")
    jmh("org.openjdk.jmh:jmh-generator-annprocess:1.29")
}

tasks.jar {
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile>().configureEach {
    kotlinOptions.jvmTarget = "11"
}