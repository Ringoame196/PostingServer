plugins {
	id("java")
	kotlin("jvm")
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
	mavenCentral()
}

dependencies {
	testImplementation(platform("org.junit:junit-bom:5.10.0"))
	testImplementation("org.junit.jupiter:junit-jupiter")
	implementation(kotlin("stdlib-jdk8"))
	implementation("io.ktor:ktor-server-core:2.3.3")
	implementation("io.ktor:ktor-server-netty:2.3.3")
}

tasks.test {
	useJUnitPlatform()
}

tasks.jar {
	manifest {
		attributes["Main-Class"] = "com.github.ringoame196.MainKt"
	}
	configurations["compileClasspath"].forEach { file: File ->
		from(zipTree(file.absoluteFile))
	}
	duplicatesStrategy = DuplicatesStrategy.INCLUDE
}

kotlin {
	jvmToolchain(22)
}