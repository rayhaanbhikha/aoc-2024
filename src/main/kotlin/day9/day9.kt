package day9

import utils.LinkedListNode
import utils.loadInput
import utils.toLinkedList


data class FileBlock(var id: Int, var size: Int, val index: Int = 0, var hasMoved: Boolean = false) {
    fun isEmpty() = id == -1
}

fun part1(): Int {
    val input = loadInput(9).split("").toMutableList()
//    val input = "2333133121414131402".split("").toMutableList()
//    val input = "12345".split("").toMutableList()

    input.removeFirst()
    input.removeLast()

    var fileID = 0

    val fileBlocks = input.mapIndexedNotNull { index, value ->
        val numOfBlocks = value.toInt()
        if (numOfBlocks == 0) return@mapIndexedNotNull null
        if (index % 2 == 0) {
            fileID++
            FileBlock(fileID - 1, numOfBlocks)
        } else {
            FileBlock(-1, numOfBlocks)
        }
    }

//    fileBlocks.map { block -> List(block.size){ if(block.isEmpty()) "." else block.id }.joinToString("") }.joinToString("").also { println(it) }

    var fileBlockLeftIndex = 0
    var fileBlockRightIndex = fileBlocks.size - 1

    val newFileBlocks = mutableListOf<FileBlock>()

    while (fileBlockLeftIndex <= fileBlockRightIndex) {
        val fileBlockLeft = fileBlocks[fileBlockLeftIndex]
        val fileBlockRight = fileBlocks[fileBlockRightIndex]

        if (!fileBlockLeft.isEmpty()) {
            newFileBlocks.add(fileBlockLeft)
            fileBlockLeftIndex++
//            newFileBlocks.map { block -> List(block.size){ if(block.isEmpty()) "." else block.id }.joinToString("") }.joinToString("").also { println(it) }
            continue
        }

        if (fileBlockRight.isEmpty()) {
            fileBlockRightIndex--
            continue
        }

        // if empty file block size is smaller than right
        when {
            // empty file blocks is larger than the fileblock we're on.
            fileBlockLeft.size > fileBlockRight.size -> {
                // create another copy of the fileBlockRight and place it in.
                newFileBlocks.add(fileBlockRight.copy())

                // add another empty block with the remaining.
                fileBlockLeft.size -= fileBlockRight.size
//                newFileBlocks.add(fileBlockLeft.copy(size = fileBlockLeft.size - fileBlockRight.size))

//                fileBlockLeftIndex++
                fileBlockRightIndex--
            }

            fileBlockLeft.size < fileBlockRight.size -> {
                newFileBlocks.add(fileBlockLeft.copy(id = fileBlockRight.id))

                fileBlockRight.size -= fileBlockLeft.size

                fileBlockLeftIndex++
            }

            fileBlockLeft.size == fileBlockRight.size -> {
                newFileBlocks.add(fileBlockRight.copy())

                fileBlockLeftIndex++
                fileBlockRightIndex--
            }
        }


//        newFileBlocks.map { block -> List(block.size){ if(block.isEmpty()) "." else block.id }.joinToString("") }.joinToString("").also { println(it) }
    }

    newFileBlocks
        .mapNotNull { block -> if (block.isEmpty()) null else List(block.size) { block.id } }
        .flatten()
        .foldIndexed(0L) { index, acc, i ->
            acc + (index * i)
        }
        .also {
            println(it)
        }

    return 0
}

fun part2(): Int {
    val input = loadInput(9).split("").toMutableList()
//    val input = "2333133121414131402".split("").toMutableList()
//    val input = "12345".split("").toMutableList()

    input.removeFirst()
    input.removeLast()

    var fileID = 0

    val fileBlocks = input.mapIndexedNotNull { index, value ->
        val numOfBlocks = value.toInt()
        if (numOfBlocks == 0) return@mapIndexedNotNull null
        if (index % 2 == 0) {
            fileID++
            FileBlock(fileID - 1, numOfBlocks, index)
        } else {
            FileBlock(-1, numOfBlocks, index)
        }
    }


    val fileBlockList = fileBlocks.toLinkedList()
    // TODO: should be able to just get last node.
    var fileBlockRightNode = fileBlockList?.get(fileBlocks.lastIndex)

    while (fileBlockRightNode != null) {
        if (fileBlockRightNode.prev == null) {
            // no more filled blocked.
            break
        }

        if (fileBlockRightNode.value.isEmpty()) {
            fileBlockRightNode = fileBlockRightNode.prev
            continue
        }

        val emptyBlockNode: LinkedListNode<FileBlock>? = fileBlockList?.firstOrNull {
            it.value.isEmpty() && it.value.size >= fileBlockRightNode!!.value.size && it.index < fileBlockRightNode!!.index
        }

        // can't find an empty block node on the left.
        if (emptyBlockNode == null) {
            fileBlockRightNode = fileBlockRightNode.prev
            continue
        }

        val emptyBlock = emptyBlockNode.value

        when {
            // we have enough empty space.
            emptyBlock.size == fileBlockRightNode.value.size -> {
                val fileBlock = fileBlockRightNode.value.copy()
                val newEmptyFileBlockNode = LinkedListNode(FileBlock(id = -1, size = fileBlockRightNode.value.size))
                newEmptyFileBlockNode.index = fileBlockRightNode.index
                fileBlockRightNode.insert(newEmptyFileBlockNode)
                fileBlockRightNode.delete()

                val emptyBlockNodeIndex = emptyBlockNode.index
                val newFileBlockNode = LinkedListNode(fileBlock)
                newFileBlockNode.index = emptyBlockNodeIndex

                emptyBlockNode.insert(newFileBlockNode)
                emptyBlockNode.delete()
                fileBlockRightNode = fileBlockRightNode.prev
            }

            emptyBlock.size > fileBlockRightNode.value.size -> {
                val fileBlock = fileBlockRightNode.value.copy()
                val newEmptyFileBlockNode = LinkedListNode(FileBlock(id = -1, size = fileBlockRightNode.value.size))
                newEmptyFileBlockNode.index = fileBlockRightNode.index
                fileBlockRightNode.insert(newEmptyFileBlockNode)
                fileBlockRightNode.delete()

                val remainingSize = emptyBlock.size - fileBlock.size

                val emptyBlockNodeIndex = emptyBlockNode.index
                val newFileBlockNode = LinkedListNode(fileBlock)
                newFileBlockNode.index = emptyBlockNodeIndex

                emptyBlockNode.insert(newFileBlockNode)

                val remainingEmptyBlock = LinkedListNode(FileBlock(id = -1, size = remainingSize))
                remainingEmptyBlock.index = emptyBlockNodeIndex
                newFileBlockNode.insert(remainingEmptyBlock)
                emptyBlockNode.delete()

                fileBlockRightNode = fileBlockRightNode.prev
            }
        }
    }

    fileBlockList!!
        .map { it.value }
        .map { block -> List(block.size) { if (block.isEmpty()) -1 else block.id } }
        .flatten()
        .foldIndexed(0L) { index, acc, i ->
            if (i == -1) acc else acc + (index * i)
        }
        .also {
            println(it)
        }

    return 0
}
