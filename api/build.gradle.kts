group = "com.durganmcbroom"
version = "1.0-SNAPSHOT"

task<Jar>("sourcesJar") {
    archiveClassifier.set("sources")
    from(sourceSets.main.get().allSource)
}

task<Jar>("javadocJar") {
    archiveClassifier.set("javadoc")
    from(tasks.dokkaJavadoc)
}

publishing {
    publications.withType<MavenPublication> {
        from(components["java"])
        artifact(tasks["sourcesJar"])
        artifact(tasks["javadocJar"])

        pom {
            name.set("Event Api")
            description.set("An Api for receiving, dispatching, transforming all other things related to events!")
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