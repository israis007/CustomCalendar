package com.pirataram.calendarcustom.tools

class NumberHelper {

    companion object {
        fun isEven(number: Float) = (number.toInt() % 2) == 0

        fun getNextNumber(number: Float) = number + 1f

        fun getHalfNumber(number: Float): Int {
            var n = if (isEven(number)){
                getNextNumber(number).toInt()
            } else
                number.toInt()
            n /= 2
            return n
        }
    }
}