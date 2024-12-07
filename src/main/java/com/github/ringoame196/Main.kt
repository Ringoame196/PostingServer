package com.github.ringoame196

import io.ktor.server.application.*
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
				// ルートにアクセスされた場合、ファイルを公開
				static("/") {
					// JARファイル直下のパスを指定
					val jarDir = File(System.getProperty("user.dir"))
					val publicDir = File(jarDir, "public")

					if (!publicDir.exists()) publicDir.mkdirs()

					// 静的ファイルを提供
					staticRootFolder = publicDir
					files(".")
				}

				// 任意のエンドポイントを作成可能
				get("/") {
					call.respondText("ファイルサーバーが稼働中です")
				}
			}
			println("${port}番でサーバーを公開しました")
		}.start(wait = true)
	}
}