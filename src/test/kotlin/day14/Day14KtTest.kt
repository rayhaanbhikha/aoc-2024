package day14

import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

class Day14KtTest {

    @Test
    fun part1() {
        val res = day14.part1()
        assertEquals(224969976, res)
    }

    @Test
    fun part2() {
        val res = day14.part2()
        assertEquals(7892, res)
    }
}