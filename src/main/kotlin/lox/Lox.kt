import lox.AstPrinter
import lox.Interpretter
import lox.RuntimeError
import lox.parser.Parser
import lox.scanner.Scanner
import lox.scanner.Token
import lox.scanner.TokenType
import java.io.BufferedReader
import java.io.File
import java.io.IOException
import java.io.InputStreamReader
import java.nio.charset.Charset
import kotlin.system.exitProcess


var hadError = false
var hadRuntimeError = false
val interpretter = Interpretter()

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

    if (hadError) exitProcess(65)
    if(hadRuntimeError) exitProcess(70)
}

fun run(source: String) {
    val scanner = Scanner(source)
    scanner.scanTokens()

    val tokens = scanner.tokens

    val parser = Parser(tokens)
    val expression = parser.parse()

    if (hadError) return

    interpretter.interpret(expression)
}

fun error(line: Int, message: String) {
    report(line, "", message)
}

fun report(line: Int, where: String, message: String) {
    System.err.println("[line: $line] Error $where: $message")
    hadError = true
}

fun error(token: Token, message: String) {
    if (token.type === TokenType.EOF) {
        report(token.line, " at end", message)
    } else {
        report(token.line, " at '${token.lexeme}'", message)
    }
}

fun runtimeError(error: RuntimeError) {
    System.err.println(error.message + "\n[Line ${error.token.line}]")
    hadRuntimeError = true
}
