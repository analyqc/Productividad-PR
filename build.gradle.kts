plugins {
    kotlin("jvm") version "1.9.22"
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.apache.poi:poi:5.2.3")
    implementation("org.apache.poi:poi-ooxml:5.2.3")
    testImplementation("org.jetbrains.kotlin:kotlin-test")
    testImplementation("io.cucumber:cucumber-java:7.8.1")
    testImplementation("io.cucumber:cucumber-junit:7.8.1")
    testImplementation ("org.mockito:mockito-inline:2.13.0")
    testImplementation("org.mockito:mockito-core:4.9.0")
    testImplementation("org.jetbrains.kotlinx:kotlinx-serialization-csv:0.20.0")
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(21)
}