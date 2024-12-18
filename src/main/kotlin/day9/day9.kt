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
    }.toMutableList()

    val fileBlockList = fileBlocks.toLinkedList()
//    val emptyFileBlocks = fileBlockList?.filter { it.value.isEmpty() }?.toMutableList()
    var fileBlockRightNode = fileBlockList?.get(fileBlocks.lastIndex)
    var fileBlockRightNodeIndex = fileBlocks.lastIndex
    var currentFileBlockRightID = -1

    fileBlockList!!
        .map { it.value }
        .map { block -> List(block.size) { if (block.isEmpty()) "." else block.id } }
        .flatten()
        .joinToString("")
        .also {
            println(it)
        }


//    while (fileBlockRightNode != null) {
//        if (fileBlockRightNode.value.isEmpty() || fileBlockRightNode.prev == null) {
//            fileBlockRightNode = fileBlockRightNode.prev
//            fileBlockRightNodeIndex--
//            continue
//        }
//
//        var emptyBlockNode: LinkedListNode<FileBlock>? = null
//
//
//        run breaking@{
//            fileBlockList?.forEachIndexed { index, fileBlock ->
//                if (
//                // must be an empty block
//                    fileBlock.value.isEmpty() &&
//                    // must not be an empty block created by moving a right.
//                    !fileBlock.value.hasMoved &&
//                    // empty block must have enough capacity to move the current fileBlock
//                    fileBlock.value.size >= fileBlockRightNode!!.value.size &&
//                    // index of the empty block must be less than the current file block.
//                    index <= fileBlockRightNodeIndex
//                ) {
//                    emptyBlockNode = fileBlock
//                    return@breaking
//                }
//            }
//        }
//
//        if (emptyBlockNode == null) {
//            fileBlockRightNode = fileBlockRightNode.prev
//            fileBlockRightNodeIndex--
//            continue
//        }
//
//        val emptyBlock = emptyBlockNode!!.value
//
////        println("$emptyBlock - ${fileBlockRightNode.value}")
//
//        when {
//            // we have enough empty space.
//            emptyBlock.size == fileBlockRightNode.value.size -> {
//                emptyBlock.id = fileBlockRightNode.value.id
//                emptyBlock.size = fileBlockRightNode.value.size
//
//                fileBlockRightNode.value.id = -1
//                fileBlockRightNode.value.hasMoved = true
//
//                fileBlockRightNode = fileBlockRightNode.prev
//                fileBlockRightNodeIndex--
//            }
//
//            emptyBlock.size > fileBlockRightNode.value.size -> {
//                val remainingSize = emptyBlock.size - fileBlockRightNode.value.size
//                emptyBlock.id = fileBlockRightNode.value.id
//                emptyBlock.size = fileBlockRightNode.value.size
//
//                fileBlockRightNode.value.id = -1
//                fileBlockRightNode.value.hasMoved = true
//                fileBlockRightNode = fileBlockRightNode.prev
//                fileBlockRightNodeIndex--
//
//                emptyBlockNode?.insert(FileBlock(id = -1, size = remainingSize))
//            }
//        }
//
////        fileBlockList!!
////            .map { it.value }
////            .map { block -> List(block.size) { if (block.isEmpty()) "." else block.id } }
////            .flatten()
////            .joinToString("")
////            .also {
////                println(it)
////            }
//    }
//
//    fileBlockList!!
//        .map { it.value }
//        .map { block -> List(block.size) { if (block.isEmpty()) -1 else block.id } }
//        .flatten()
//        .foldIndexed(0L) { index, acc, i ->
//            if (i == -1) acc else acc + (index * i)
//        }
//        .also {
//            println(it)
//        }

    return 0
}
