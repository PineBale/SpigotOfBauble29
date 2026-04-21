plugins {
    `java-library`
    `maven-publish`
    jacoco
}

group = "dev.pinebale.minecraft"
version = "0.1"

dependencies {
    api("org.spigotmc:spigot-api:1.8.8-R0.1-SNAPSHOT")
    compileOnly("org.projectlombok:lombok:${project.findProperty("lombok_version")}")
    annotationProcessor("org.projectlombok:lombok:${project.findProperty("lombok_version")}")

    testImplementation("org.junit.jupiter:junit-jupiter-api:${project.findProperty("junit_version")}")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:${project.findProperty("junit_version")}")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    testImplementation("dev.pinebale.minecraft.com.github.MockBukkit:MockBukkit")
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
    withSourcesJar()
    withJavadocJar()
}

tasks {
    named<Javadoc>("javadoc") {
        options.encoding = "UTF-8"

        (options as? StandardJavadocDocletOptions)?.apply {
            links(
                "https://guava.dev/releases/17.0/api/docs/",
                "https://helpch.at/docs/1.8.8/"
            )
        }
    }

    named<Jar>("jar") {
        manifest {
            attributes["Built-By"] = ""
            attributes["Created-By"] = ""
        }
    }

    test {
        useJUnitPlatform()
        finalizedBy(jacocoTestReport)
    }

    jacocoTestReport {
        dependsOn(test)
        reports {
            xml.required = true
            html.required = true
            csv.required = true
        }
    }
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}
