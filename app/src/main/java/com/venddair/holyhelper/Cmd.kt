package com.venddair.holyhelper

object Cmd {
    fun execute(command: String): String {
        val process = ProcessBuilder("sh", "-c", command)
            .redirectErrorStream(true)
            .start()

        return process.inputStream.bufferedReader().readText().trim()
    }
}
