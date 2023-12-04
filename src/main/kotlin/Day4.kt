import java.io.File
import nico.extractIntNumbers
import kotlin.math.pow

fun main() {
    val day4 = Day4()
    run part1@{
        val lines = File("inputs/inputDay4.txt").readLines()
        println("Part1: " + day4.partOne(lines))
    }
    run part2@{
        val lines = File("inputs/inputDay4.txt").readLines()
        println("Part2: " + day4.partTwo(lines))
    }
}

class Day4{
    data class Card(val cardNumber: Int, val winingNumbers: List<Int>, val myNumbers: List<Int>){
        fun calculateScore(): Int{
            return myNumbers.count { number ->
                winingNumbers.contains(number)
            }
        }
    }
    fun partOne(lines: List<String>): String {
        val data = lines.mapIndexed { indexLine, line ->
            Card(indexLine, extractIntNumbers(line.split('|').first()).drop(1), extractIntNumbers(line.split('|').last()))
        }
        return data.sumOf { number ->
            2.0.pow(number.calculateScore() - 1).toInt()
        }.toString()

    }
    private fun retrieveCardsCopy(initialCardList: List<Card>, inputCard: Card): List<Card>{
        val cardCopyList = mutableListOf<Card>()
        cardCopyList.add(inputCard)
        for (i in (inputCard.cardNumber + 1) .. (inputCard.cardNumber + inputCard.calculateScore())){
            cardCopyList.addAll(retrieveCardsCopy(initialCardList, initialCardList[i]))
        }
        return cardCopyList
    }
    fun partTwo(lines: List<String>): String {
        val finalCardList = mutableListOf<Card>()
        val initialCardList =  lines.mapIndexed { indexLine, line ->
            Card(indexLine, extractIntNumbers(line.split('|').first()).drop(1), extractIntNumbers(line.split('|').last()))
        }
        for(card in initialCardList)
            finalCardList.addAll(retrieveCardsCopy(initialCardList, card))

        return finalCardList.count().toString()
    }
}