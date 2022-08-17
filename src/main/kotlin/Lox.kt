import java.io.BufferedReader
import java.io.File
import java.io.IOException
import java.io.InputStreamReader
import java.nio.charset.Charset
import kotlin.jvm.Throws
import kotlin.system.exitProcess

fun main(args: Array<String>) {

    when (args.size) {
        0 -> runREPL()
        1 -> runFile(args[0])
        else -> {
            println("Usage: kotlox [script-path]")
            exitProcess(64)
        }
    }
}

@Throws(IOException::class)
fun runREPL() {
    val inputStream = InputStreamReader(System.`in`)
    val streamReader = BufferedReader(inputStream)

    while (true) {
        print("> ")

        val line = streamReader.readLine() ?: break
        run(line)
    }
}

@Throws(IOException::class)
fun runFile(filePath: String) {
    val bytes = File(filePath).readBytes()
    run(String(bytes, Charset.defaultCharset()))
}



fun run(source: String) {

}