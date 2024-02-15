fun main() {
    val n = 10 // Cambia este valor para obtener más o menos números de la serie
    print("Serie Fibonacci de $n números: ")
    for (i in 0 until n) {
        print("${fibonacci(i)} ")
    }
}

fun fibonacci(n: Int): Int {
    return if (n <= 1) {
        n
    } else {
        fibonacci(n - 1) + fibonacci(n - 2)
    }
}