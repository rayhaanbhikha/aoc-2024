package utils

enum class Colors(val ansiValue: String) {
    Red("\u001B[31m"),
    Green("\u001B[32m"),
    Yellow("\u001B[33m"),
    Blue("\u001B[34m"),
    Reset("\u001B[0m"),
    Beige("\u001B[35m")
}