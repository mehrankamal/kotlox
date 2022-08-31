package tools

import java.io.IOException
import java.io.PrintWriter
import kotlin.system.exitProcess


object GenerateAst {
    @Throws(IOException::class)
    @JvmStatic
    fun main(args: Array<String>) {
        if (args.size != 1) {
            System.err.println("Usage: generate_ast <output directory>")
            exitProcess(64)
        }
        val outputDir = args[0]
        defineAst(
            outputDir, "Expr", listOf<String>(
                "Binary   : Expr left, Token operator, Expr right",
                "Grouping : Expr expression",
                "Literal  : Any value",
                "Unary    : Token operator, Expr right"
            )
        )
    }

    @Throws(IOException::class)
    private fun defineAst(
        outputDir: String, baseName: String, types: List<String>
    ) {
        val path = "$outputDir/$baseName.kt"
        val writer = PrintWriter(path, "UTF-8")
        writer.println("package lox.parser")
        writer.println()
        writer.println("import lox.scanner.Token")
        writer.println()
        writer.println("abstract class $baseName {}")
        writer.println()

        for (type in types) {
            val (className, fields) = type.split(":").map { it.trim() }
            defineType(writer, baseName, className, fields)
        }

        writer.close()
    }

    private fun defineType(
        writer: PrintWriter, baseName: String,
        className: String, fields: String
    ) {
        var initializer = ""

        // Store parameters in fields.
        //From: Expr left, Token operator, Expr right
        //To:   val right: Expr, val operator:Token, val right: Expr

        for (field: String in fields.split(", ")) {
            val (type, name) = field.split(" ").map { it.trim() }
            initializer += " val $name:$type,"
        }

        initializer = initializer.dropLast(1)

        writer.println(
            "class $className($initializer) : $baseName() {}"
        )
        writer.println()
    }
}