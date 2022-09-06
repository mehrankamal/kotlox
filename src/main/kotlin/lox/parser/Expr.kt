package lox.parser

import lox.scanner.Token

abstract class Expr

class Binary( val left:Expr, val operator:Token, val right:Expr) : Expr()

class Grouping( val expression:Expr) : Expr()

class Literal( val value:Any) : Expr()

class Unary( val operator:Token, val right:Expr) : Expr()

interface Visitor<R> {
	fun visitBinaryExpr (expr: Binary): R
	fun visitGroupingExpr (expr: Grouping): R
	fun visitLiteralExpr (expr: Literal): R
	fun visitUnaryExpr (expr: Unary): R
}
