plugins {
	`java-library`
}
allprojects {
	group = "moe.nea"
	version = "1.0-SNAPSHOT"

	repositories {
		mavenCentral()
	}
}
dependencies {
	api("org.jspecify:jspecify:1.0.0")
}
allprojects {
	afterEvaluate {
		if (project.plugins.hasPlugin(JavaBasePlugin::class.java))
			dependencies {
				testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.1")
				testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.8.1")
			}
	}
	tasks.withType(Test::class) {
		useJUnitPlatform()
	}
}