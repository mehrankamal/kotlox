package lox

import com.sun.org.apache.xpath.internal.operations.Bool
import lox.parser.*
import lox.scanner.TokenType
import kotlin.reflect.typeOf

class Interpretter : Visitor<Any?> {
    override fun visitLiteralExpr(expr: Literal): Any? {
        return expr.value
    }

    override fun visitGroupingExpr(expr: Grouping): Any? {
        return evaluate(expr.expression)
    }

    override fun visitUnaryExpr(expr: Unary): Any? {
        val right = evaluate(expr.right)

        return when(expr.operator.type) {
            TokenType.MINUS -> -(right as Double)
            TokenType.BANG -> !isTruthy(right)
            else -> null
        }
    }

    override fun visitBinaryExpr(expr: Binary): Any? {
        val left = evaluate(expr.left)
        val right = evaluate(expr.right)

        return when(expr.operator.type) {
            TokenType.GREATER -> (left as Double) > (right as Double)
            TokenType.GREATER_EQUAL -> (left as Double) >= (right as Double)
            TokenType.LESS -> (left as Double) < (right as Double)
            TokenType.LESS_EQUAL -> (left as Double) <= (right as Double)
            TokenType.EQUAL_EQUAL -> isEqual(left, right)
            TokenType.BANG_EQUAL -> !isEqual(left, right)
            TokenType.MINUS -> left as Double - right as Double
            TokenType.PLUS -> {
                if (left is Double && right is Double)
                    left + right
                else if (left is String && right is String)
                    left + right
                else null
            }
            TokenType.STAR -> left as Double * right as Double
        }
    }

    private fun evaluate(expr: Expr): Any? {
        return expr.accept(this)
    }

    private fun isTruthy(obj: Any?): Boolean {
        if(obj == null) return false
        if(obj is Boolean) return obj
        return true
    }

    private fun isEqual(left: Any?, right: Any?): Boolean {
        if(left == null && right == null) return true
        if(left == null) return false

        return left == right
    }
}