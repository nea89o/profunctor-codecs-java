allprojects {
	group = "moe.nea.pcj"
	version = "1.0-SNAPSHOT"

	repositories {
		mavenCentral()
	}

	tasks.withType(Test::class) {
		useJUnitPlatform()
	}
}
subprojects {
	apply(plugin = "org.gradle.java-library")
	apply(plugin = "org.gradle.maven-publish")
	dependencies {
		"api"("org.jspecify:jspecify:1.0.0")
		"testImplementation"("org.junit.jupiter:junit-jupiter-api:5.8.1")
		"testRuntimeOnly"("org.junit.jupiter:junit-jupiter-engine:5.8.1")
	}

	configure<PublishingExtension> {
		publications {
			create<MavenPublication>("maven") {
				from(components[("java")])
			}
		}
	}
}