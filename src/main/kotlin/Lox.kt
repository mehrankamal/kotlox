import java.io.BufferedReader
import java.io.File
import java.io.IOException
import java.io.InputStreamReader
import java.nio.charset.Charset
import kotlin.system.exitProcess

var hadError = false
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
        
        hadError = false
    }
}

@Throws(IOException::class)
fun runFile(filePath: String) {
    val bytes = File(filePath).readBytes()
    run(String(bytes, Charset.defaultCharset()))

    if (hadError) {
        exitProcess(65)
    }
}


fun run(source: String) {
    val scanner = Scanner(source)
    val tokens = scanner.scanTokens();

    for (token in tokens) {
        println(token)
    }
    print(source)
}

fun error(line: Int, message: String) {
    report(line, "", message)
}

fun report(line: Int, where: String, message: String) {
    System.err.println("[line: $line] Error $where: $message")
    hadError = true
}
