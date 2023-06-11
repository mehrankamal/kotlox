package lox

import lox.scanner.Token

class RuntimeError(val token: Token, message: String) : RuntimeException(message) {
}