package utils

class LinkedListIterator<T>(rootNode: LinkedListNode<T>) : Iterator<LinkedListNode<T>> {
    private var currentNode: LinkedListNode<T>? = rootNode

    override fun hasNext(): Boolean {
        return currentNode != null
    }

    override fun next(): LinkedListNode<T> {
        val valueToReturn = currentNode!!
        currentNode = currentNode?.next
        return valueToReturn
    }

}

class LinkedListNode<T>(var value: T) : Iterable<LinkedListNode<T>> {
    var prev: LinkedListNode<T>? = null
    var next: LinkedListNode<T>? = null

    fun get(index: Int): LinkedListNode<T>? {
        return when {
            index < 0 -> null
            index == 0 -> this
            else -> return next?.get(index-1)
        }
    }

    private fun add(otherNode: LinkedListNode<T>) {
        if (this.next != null) {
            return next!!.add(otherNode)
        }

        next = otherNode
        otherNode.prev = this
    }

    fun add(value: T) {
        add(LinkedListNode(value))
    }

    fun insert(value: T) {
        insert(LinkedListNode(value))
    }


    fun insertAfterIndex(index: Int, value: T) {
        when {
            index < 0 -> return
            index == 0 -> insert(newNode = LinkedListNode(value))
            else -> next?.insertAfterIndex(index-1, value)
        }
    }

    fun insert(newNode: LinkedListNode<T>) {
        if (next == null) {
            add(newNode)
            return
        }

        val nextNode = next

        next = newNode
        newNode.next = nextNode
    }

    fun delete(index: Int) {
        if (index < 0) return

        if (index == 0) {
            prev?.next = next
            next?.prev = prev
            return
        }

        next?.delete(index - 1)
    }

    fun traverse(cb: (value: T) -> Unit) {
        cb(value)
        if (next != null) {
            next!!.traverse(cb)
        }
    }

    fun walk(cb: (node: LinkedListNode<T>) -> Unit) {
        cb(this)
        if (next != null) {
            next!!.walk(cb)
        }
    }

    fun print() {
        this.walk {
            print(it.value)
        }
        println()
    }

    override fun iterator(): Iterator<LinkedListNode<T>> {
        return LinkedListIterator(this)
    }
}

fun <T> List<T>.toLinkedList(): LinkedListNode<T>? {
    if (this.isEmpty()) {
        return null
    }

    val rootNode = LinkedListNode(this[0])
    this.drop(1).forEach {
        rootNode.add(it)
    }
    return rootNode
}