import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

group = "com.flexe.outbound"

plugins {
    id("org.jetbrains.kotlin.jvm") // version "1.3.61"
}

dependencies {
    // Use the Kotlin JDK 8 standard library.
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    // kotlin reflection is required by spring
    implementation("org.jetbrains.kotlin:kotlin-reflect")

    // Use the Kotlin test library.
    testImplementation("org.jetbrains.kotlin:kotlin-test")
    // Use the Kotlin JUnit integration.
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit")
}
