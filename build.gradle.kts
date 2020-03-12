
group = "com.flexe"
version = "0.1"

plugins {
    // Apply the Kotlin JVM plugin to add support for Kotlin.
    id("org.jetbrains.kotlin.jvm") version "1.3.61"
}

allprojects {
    repositories {
        mavenCentral()
    }

}

subprojects {
    apply(plugin = "java")
    version = "1.0"

    dependencies {
        // Use the Kotlin test library.
        "testImplementation"("org.jetbrains.kotlin:kotlin-test")
        // Use the Kotlin JUnit integration.
        "testImplementation"("org.jetbrains.kotlin:kotlin-test-junit")
        // mocking with mockito
        "testImplementation"("io.mockk:mockk:1.9.3")
        "testImplementation"("net.bytebuddy:byte-buddy:1.10.8")
        "testImplementation"("net.bytebuddy:byte-buddy-agent:1.10.8")
    }
}

project(":event") {
    dependencies {
        "implementation"(project(":model"))
    }
}

project(":write") {
    dependencies {
        "implementation"(project(":model"))
        "implementation"(project(":event"))
    }
}

project(":read") {
    dependencies {
        "implementation"(project(":model"))
        "implementation"(project(":event"))
    }
}

