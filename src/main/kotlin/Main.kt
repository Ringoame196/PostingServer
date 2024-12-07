package com.github.ringoame196

import io.ktor.http.HttpStatusCode
import io.ktor.server.engine.embeddedServer
import io.ktor.server.http.content.static
import io.ktor.server.http.content.staticRootFolder
import io.ktor.server.http.content.files
import io.ktor.server.netty.Netty
import io.ktor.server.response.respondFile
import io.ktor.server.response.respondText
import io.ktor.server.routing.get
import io.ktor.server.routing.routing
import java.io.File

fun main(args: Array<String>) {
	val defaultPort = 8080
	val port = args.getOrNull(0)?.toIntOrNull() ?: defaultPort

	// サーバーを起動
	embeddedServer(Netty, port = port) {
		routing {
			static("/") {
				val jarDir = File(System.getProperty("user.dir"))
				val publicDir = File(jarDir, "public")

				if (!publicDir.exists()) publicDir.mkdirs()

				staticRootFolder = publicDir
				files(".")
			}

			// URL指定されたファイルを取得するエンドポイント
			get("/{filename}") {
				val jarDir = File(System.getProperty("user.dir"))
				val publicDir = File(jarDir, "public")
				val fileName = call.parameters["filename"]

				if (fileName.isNullOrBlank()) {
					call.respondText("Please specify file name", status = HttpStatusCode.Companion.BadRequest)
					return@get
				}

				val file = File(publicDir, fileName)

				if (file.exists() && file.isFile) {
					call.respondFile(file)
				} else {
					call.respondText(
						"file does not exist",
						status = HttpStatusCode.NotFound
					)
				}
			}

			get("/") {
				call.respondText("server online")
			}
		}
		println("${port}番でサーバーを公開しました")
	}.start(wait = true)
}