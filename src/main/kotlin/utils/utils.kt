package utils

import java.io.File


fun loadInput(day: Int, example: Boolean = false): String {
    var prefix = "./inputs/day${day}"

    prefix += if (!example) {
        "_input.txt"
    } else {
        "_input_example.txt"
    }

    return File(prefix).readText().trim()
}