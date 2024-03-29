package lox

import lox.parser.*
import lox.scanner.Token
import lox.scanner.TokenType
import runtimeError

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
            TokenType.MINUS -> {
                checkNumberOperand(expr.operator, right)
                -(right as Double)
            }
            TokenType.BANG -> !isTruthy(right)
            else -> null
        }
    }

    override fun visitBinaryExpr(expr: Binary): Any? {
        val left = evaluate(expr.left)
        val right = evaluate(expr.right)

        return when(expr.operator.type) {
            TokenType.GREATER -> {
                checkNumberOperands(expr.operator, left, right)
                (left as Double) > (right as Double)
            }
            TokenType.GREATER_EQUAL -> {
                checkNumberOperands(expr.operator, left, right)
                (left as Double) >= (right as Double)
            }
            TokenType.LESS -> {
                checkNumberOperands(expr.operator, left, right)
                (left as Double) < (right as Double)
            }
            TokenType.LESS_EQUAL -> {
                checkNumberOperands(expr.operator, left, right)
                (left as Double) <= (right as Double)
            }
            TokenType.EQUAL_EQUAL -> {
                checkNumberOperands(expr.operator, left, right)
                isEqual(left, right)
            }
            TokenType.BANG_EQUAL -> {
                checkNumberOperands(expr.operator, left, right)
                !isEqual(left, right)
            }
            TokenType.MINUS -> {
                checkNumberOperands(expr.operator, left, right)
                left as Double - right as Double
            }
            TokenType.PLUS -> {
                if (left is Double && right is Double)
                    left + right
                else if (left is String && right is String)
                    left + right
                else throw RuntimeError(expr.operator, "Operands must be two numbers or two strings")
            }
            TokenType.STAR -> {
                checkNumberOperands(expr.operator, left, right)
                left as Double * right as Double
            }
            else -> null
        }
    }

    private fun stringify(obj: Any?): String {
        if(obj == null) return "nil";

        if(obj is Double) {
            var text = obj.toString()
            if(text.endsWith(".0")) {
                text = text.substring(0, text.length - 2)
            }
            return text
        }

        return obj.toString()
    }

    fun interpret(expression: Expr?) {
        try {
            val value = evaluate(expression)
            println(stringify(value))
        } catch (error: RuntimeError) {
            runtimeError(error)
        }
    }

    private fun checkNumberOperand(operator: Token, operand: Any?) {
        if(operand is Double) return
        throw RuntimeError(operator, "Operand must be a number")
    }

    private fun checkNumberOperands(operator: Token, left: Any?, right: Any?) {
        if(left is Double && right is Double) return
        throw RuntimeError(operator, "Operands must be a numbers.")
    }

    private fun evaluate(expr: Expr?): Any? {
        return expr?.accept(this)
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