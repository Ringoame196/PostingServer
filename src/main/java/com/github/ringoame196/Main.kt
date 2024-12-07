package com.github.ringoame196

import io.ktor.http.*
import io.ktor.server.engine.*
import io.ktor.server.http.content.*
import io.ktor.server.netty.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import java.io.File

object Main {
	@JvmStatic
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
}