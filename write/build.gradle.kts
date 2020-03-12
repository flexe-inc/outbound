import org.jetbrains.kotlin.gradle.tasks.KotlinCompile


plugins {
    // Apply the Kotlin JVM plugin to add support for Kotlin.
    id("org.jetbrains.kotlin.jvm") // version "1.3.61"
    id("org.springframework.boot") version "2.2.0.RELEASE"
    id("io.spring.dependency-management") version "1.0.8.RELEASE"
    kotlin("plugin.spring") version "1.3.61"
    application
}

group = "com.flexe.outbound"

dependencies {
    // Use the Kotlin JDK 8 standard library.
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    // app spring-boot web framework
    implementation("org.springframework.boot:spring-boot-starter-web")
    // kotlin reflection is required by spring
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    // required to talk to the event store
    implementation("com.github.msemys:esjc:2.1.0")
    // jackson for kotlin
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.10.3")


    // Use the Kotlin test library.
    testImplementation("org.jetbrains.kotlin:kotlin-test")
    // Use the Kotlin JUnit integration.
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit")
    // mocking with mockito
    testImplementation("io.mockk:mockk:1.9.3")
    testImplementation("net.bytebuddy:byte-buddy:1.10.8")
    testImplementation("net.bytebuddy:byte-buddy-agent:1.10.8")
}

application {
    mainClassName = "com.flexe.outbound.write.WriteKt"
}
