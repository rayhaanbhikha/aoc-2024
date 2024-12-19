package utils

import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

class LinkedListNodeTest {

    private fun generateLinkedList(): LinkedListNode<Int> {
        return (0..9).toList().toLinkedList() ?: throw Exception("linked list not created")
    }

    @Test
    fun `should add a number to the list`() {
        var linkedList = generateLinkedList()
        linkedList.add(10)
        for(i in 0..10) {
            assertEquals(i, linkedList.value)
            if (linkedList.next == null) break
            linkedList = linkedList.next!!
        }
    }

    @Test
    fun `should insert a number to the list`() {
        val linkedList = generateLinkedList()
        linkedList.insertAfterIndex(4, 20)
        assertEquals(20, linkedList.get(5)?.value)
        assertEquals(4, linkedList.get(5)?.prev?.value)
    }

    @Test
    fun `should delete a number from the list`() {
        val linkedList = generateLinkedList()
        linkedList.delete(7)
        val numbers = linkedList.map { it.value }
        assertEquals(listOf(0,1,2,3,4,5,6,8,9), numbers)
    }

    @Test
    fun `should index nodes correctly`() {
        val list = (0..9).toList().toLinkedList()!!
        assertEquals(5,  list.get(5)?.index)
    }
}