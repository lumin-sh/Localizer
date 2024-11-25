plugins {
    kotlin("jvm") version "2.0.21"
    id("org.jetbrains.kotlin.plugin.serialization") version "2.0.0"
    id("maven-publish")
}

group = "sh.lumin.localizer"
version = "1.0.0"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.7.3")
}

tasks {
    test {
        useJUnitPlatform()
    }

    compileJava {
        options.encoding = Charsets.UTF_8.name()
        options.release.set(17)
    }

    javadoc { options.encoding = Charsets.UTF_8.name() }

}

publishing {
    publishing {
        publications {
            create<MavenPublication>("maven") {
                groupId = group.toString()
                artifactId = "localizer"
                version = version.toString()

                from(components["java"])
            }
        }
    }
}

kotlin {
    jvmToolchain(17)
}