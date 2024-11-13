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

tasks.test {
	useJUnitPlatform()
}