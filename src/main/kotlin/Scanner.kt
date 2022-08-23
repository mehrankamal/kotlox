

class Scanner(val source: String) {

    // All objects should be immutable and transform only when business rules allow
    // Kotlin pattern: use immutable properties to get values from classes

    private val _tokens = mutableListOf<Token>()
    val tokens: List<Token> = _tokens
    var start = 0
    var current = 0
    var line = 1

    // Command Query: States that a function should do a command or query only. i.e it should update state or return
    // answer to a query but never both

    fun scanTokens() {
        while(!isAtEnd()) {
            start = current
            scanToken()
        }

        _tokens.add(Token(TokenType.EOF, "", null, line))
    }

    private fun isAtEnd(): Boolean {
        return current >= source.length
    }

    private fun scanToken() {
        val c = advance()
        when (c) {
            '(' -> addToken(TokenType.LEFT_PAREN)
            ')' -> addToken(TokenType.RIGHT_PAREN)
            '{' -> addToken(TokenType.LEFT_BRACE)
            '}' -> addToken(TokenType.RIGHT_BRACE)
            ',' -> addToken(TokenType.COMMA)
            '.' -> addToken(TokenType.DOT)
            '-' -> addToken(TokenType.MINUS)
            '+' -> addToken(TokenType.PLUS)
            ';' -> addToken(TokenType.SEMICOLON)
            '*' -> addToken(TokenType.STAR)
            '!' -> addToken(if(match('=')) TokenType.BANG_EQUAL else TokenType.BANG)
            '=' -> addToken(if(match('=')) TokenType.EQUAL_EQUAL else TokenType.EQUAL)
            '<' -> addToken(if(match('=')) TokenType.LESS_EQUAL else TokenType.LESS)
            '>' -> addToken(if(match('=')) TokenType.GREATER_EQUAL else TokenType.GREATER)

            else -> error(line, "Unexpected character: $c")
        }
    }

    private fun advance() = source[current++]

    private fun addToken(type: TokenType) = addToken(type, null)

    private fun addToken(type: TokenType, literal: Any?) {
        val lexeme = source.substring(start, current)
        _tokens.add(Token(type, lexeme, literal, line))
    }

    private fun match(expected: Char): Boolean {
        if (isAtEnd()) return false
        if (source[current] != expected) return false

        current++
        return true
    }
}