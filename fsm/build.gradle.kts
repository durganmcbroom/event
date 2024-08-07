group = "com.durganmcbroom"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":api"))
}

task<Jar>("sourcesJar") {
    archiveClassifier.set("sources")
    from(sourceSets.main.get().allSource)
}

task<Jar>("javadocJar") {
    archiveClassifier.set("javadoc")
    from(tasks.dokkaJavadoc)
}

publishing {
    publications {
        create("api", MavenPublication::class) {
            from(components["java"])
            artifact(tasks["sourcesJar"])
            artifact(tasks["javadocJar"])

            artifactId = "event-fsm"

            pom {
                name.set("Event FSM")
                description.set("An extension of the Event API to create and use finite state machines for events.")
                url.set("https://github.com/durganmcbroom/event")

                packaging = "jar"

                developers {
                    developer {
                        id.set("durganmcbroom")
                        name.set("Durgan McBroom")
                    }
                }

                licenses {
                    license {
                        name.set("MIT License")
                        url.set("https://opensource.org/licenses/MIT")
                    }
                }

                scm {
                    connection.set("scm:git:git://github.com/durganmcbroom/event")
                    developerConnection.set("scm:git:ssh://github.com:durganmcbroom/event.git")
                    url.set("https://github.com/durganmcbroom/event")
                }
            }
        }
    }
}