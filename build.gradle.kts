plugins {
	id("java")
	kotlin("jvm")
	id("com.github.johnrengelman.shadow") version "8.1.0" // Shadow pluginを追加
}

group = "com.github.ringoame196"
version = "1.0"

repositories {
	mavenCentral()
}

dependencies {
	testImplementation(platform("org.junit:junit-bom:5.10.0"))
	testImplementation("org.junit.jupiter:junit-jupiter")
	implementation(kotlin("stdlib-jdk8"))
	implementation("io.ktor:ktor-server-core:3.0.2")
	implementation("io.ktor:ktor-server-netty:3.0.2")
}

tasks.shadowJar {
	minimize()  // 不要なクラスやリソースを削除
	archiveBaseName.set("PostingServer")
	archiveVersion.set("1.0.0")
	manifest {
		attributes["Main-Class"] = "com.github.ringoame196.MainKt"
	}
	duplicatesStrategy = DuplicatesStrategy.EXCLUDE  // 重複ファイルを除外
}

tasks.test {
	useJUnitPlatform()
}

// 通常のjarタスクは不要ならコメントアウトするか削除しても良い
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
	jvmToolchain(17)
}