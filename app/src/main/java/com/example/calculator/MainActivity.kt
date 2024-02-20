package com.example.calculator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import java.util.*


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        var math_operation: TextView = findViewById(R.id.math_operation);
        var result_text: TextView = findViewById(R.id.result_text);
        var btn_0: TextView = findViewById(R.id.btn_0);
        var btn_1: TextView = findViewById(R.id.btn_1);
        var btn_2: TextView = findViewById(R.id.btn_2);
        var btn_3: TextView = findViewById(R.id.btn_3);
        var btn_4: TextView = findViewById(R.id.btn_4);
        var btn_5: TextView = findViewById(R.id.btn_5);
        var btn_6: TextView = findViewById(R.id.btn_6);
        var btn_7: TextView = findViewById(R.id.btn_7);
        var btn_8: TextView = findViewById(R.id.btn_8);
        var btn_9: TextView = findViewById(R.id.btn_9);
        var btn_AC: TextView = findViewById(R.id.btn_AC);
        var btn_dot: TextView = findViewById(R.id.btn_dot);
        var btn_back: TextView = findViewById(R.id.btn_back);
        var btn_plus: TextView = findViewById(R.id.btn_plus);
        var btn_minus: TextView = findViewById(R.id.btn_minus);
        var btn_mul: TextView = findViewById(R.id.btn_mul);
        var btn_del: TextView = findViewById(R.id.btn_del);
        var btn_eq: TextView = findViewById(R.id.btn_eq);
        btn_0.setOnClickListener { setText("0") }
        btn_1.setOnClickListener { setText("1") }
        btn_2.setOnClickListener { setText("2") }
        btn_3.setOnClickListener { setText("3") }
        btn_4.setOnClickListener { setText("4") }
        btn_5.setOnClickListener { setText("5") }
        btn_6.setOnClickListener { setText("6") }
        btn_7.setOnClickListener { setText("7") }
        btn_8.setOnClickListener { setText("8") }
        btn_9.setOnClickListener { setText("9") }
        btn_plus.setOnClickListener { setText("+") }
        btn_minus.setOnClickListener { setText("-") }
        btn_mul.setOnClickListener { setText("*") }
        btn_del.setOnClickListener { setText("/") }
        btn_AC.setOnClickListener {
            math_operation.text = ""
            result_text.text = ""
        }
        btn_back.setOnClickListener {
            val str = math_operation.text.toString()
            if(str.isNotEmpty()){
                math_operation.text = str.substring(0, str.length-1)
            }
            result_text.text = ""
        }
        btn_eq.setOnClickListener {
            val expression = math_operation.text.toString()
            try {
                val result = evaluateExpression(expression)
                result_text.text = "${resultToString(result)}"
            } catch (e: Exception){
                result_text.text = "${e.message}"
            }
        }
        btn_dot.setOnClickListener {
            addDot()
        }
    }
    fun addDot() {
        var math_operation: TextView = findViewById(R.id.math_operation);
        val currentText = math_operation.text.toString()
        if (currentText.last() in listOf('0', '1', '2', '3', '4', '5', '6', '7', '8', '9')) {
            math_operation.append(".")
        }
    }
    fun setText(str: String){
        var math_operation: TextView = findViewById(R.id.math_operation);
        math_operation.append(str);
    }
    private fun Double.isInt(): Boolean {
        return this % 1 == 0.0
    }
    private fun resultToString(result: Double): String {
        return if (result.isInt()) {
            result.toInt().toString()
        } else {
            result.toString()
        }
    }
private fun evaluateExpression(expression: String): Double {
    val stackNum = Stack<Double>()
    val stackOp = Stack<Char>()
    val opsPrecedence = mapOf('+' to 1, '-' to 1, '*' to 2, '/' to 2)
    fun applyOp(op: Char) {
        val b = stackNum.pop()
        val a = stackNum.pop()
        if(op == '/'&& b == 0.0){
            throw ArithmeticException("It cannot be divided by 0")
        }
        when (op) {
            '+' -> stackNum.push(a + b)
            '-' -> stackNum.push(a - b)
            '*' -> stackNum.push(a * b)
            '/' -> stackNum.push(a / b)
        }
    }

    var num = ""
    var hasDecimalPoint = false
    expression.forEach { char ->
        when {
            char.isDigit() -> {
                num += char
            }
            char == '.' && !hasDecimalPoint && num.isNotEmpty() -> {
                num += char
                hasDecimalPoint = true
            }
            char in opsPrecedence.keys -> {
                if (num.isNotEmpty()) {
                    stackNum.push(num.toDouble())
                    num = ""
                    hasDecimalPoint = false
                }
                while (stackOp.isNotEmpty() && opsPrecedence[stackOp.peek()]!! >= opsPrecedence[char]!!) {
                    applyOp(stackOp.pop())
                }
                stackOp.push(char)
            }
            else -> throw IllegalArgumentException("Invalid character in expression: $char")
        }
    }
    if (num.isNotEmpty()) {
        stackNum.push(num.toDouble())
    }

    while (stackOp.isNotEmpty()) {
        applyOp(stackOp.pop())
    }

    return stackNum.pop()
}
}

