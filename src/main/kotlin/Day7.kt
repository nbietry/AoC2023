import java.io.File

class Day7 {
    companion object{
        //Build a comparator of hands counting each cards
        val handComparator = compareBy<Hand> { hand ->
            //Attribute values to hands
            when {
                hand.countCards()[0] >= 5 -> 6                                          //Five of a kind
                hand.countCards()[0] >= 4 -> 5                                          //Four of a kind
                hand.countCards()[0] + hand.countCards()[1] >= 5 -> 4//Full house
                hand.countCards()[0] >= 3 -> 3                                          //Three of a kind
                hand.countCards()[0] + hand.countCards()[1] >= 4 -> 2 //Two pair
                hand.countCards()[0] >= 2 -> 1                                         //One pair
                else ->  0                                                              //High card
            }
        }.thenComparator{ a, b -> //if there is a draw check all positions one by one and return the first difference
            for(i in 0..<minOf(a.cards.size, b.cards.size)){
                if(a.cards[i] != b.cards[i]) return@thenComparator a.cards[i] - b.cards[i]
            }
            a.cards.size - b.cards.size
        }
        val handComparatorJoker = compareBy<Hand> { hand ->
            val jokerCount = hand.cards.count  { it < 0 }
            //Attribute values to hands and add jokers to the best combination
            when {
                hand.countCardsJoker().getOrElse(0){0} + jokerCount >= 5 -> 6                                          //Five of a kind
                hand.countCardsJoker().getOrElse(0){0} + jokerCount >= 4 -> 5                                          //Four of a kind
                hand.countCardsJoker().getOrElse(0){0} + jokerCount + hand.countCardsJoker().getOrElse(1){0} >= 5 -> 4//Full house
                hand.countCardsJoker().getOrElse(0){0} + jokerCount >= 3 -> 3                                          //Three of a kind
                hand.countCardsJoker().getOrElse(0){0} + jokerCount + hand.countCardsJoker().getOrElse(1){0} >= 4 -> 2 //Two pair
                hand.countCardsJoker().getOrElse(0){0} + jokerCount  >= 2 -> 1                                         //One pair
                else ->  0                                                              //High card
            }
        }.thenComparator{ a, b -> //if there is a draw check all positions one by one and return the first difference
            for(i in 0..<minOf(a.cards.size, b.cards.size)){
                if(a.cards[i] != b.cards[i]) return@thenComparator a.cards[i] - b.cards[i]
            }
            a.cards.size - b.cards.size
        }
        data class Hand(val cards: List<Int>, val bid: Int){
            //Count number of card of each type and sort
            fun countCards(): List<Int>{
                return cards.groupingBy { it }.eachCount().values.sortedDescending()
            }
            //Count number of card of each type and sort removing joker from counting
            fun countCardsJoker(): List<Int>{
                return cards.filter { it >= 0 }.groupingBy { it }.eachCount().values.sortedDescending()
            }
        }
    }
    private fun readInput(lines: List<String>,possibleCardList: String) = lines.map { line ->
        val (hand, bid) = line.split(' ')
        Hand(hand.map(possibleCardList::indexOf), bid.toInt())//.also { println(it.toString() + " " + it.countCards()) }
    }
    fun partOne(lines: List<String>, possibleCardList: String): Int {
        //Read input data and generate data class structure
        val hands = readInput(lines, possibleCardList)
        //Sort hand by value and apply calculation
        return hands.sortedWith(handComparator).foldIndexed(0) { index, acc, hand ->
            (hand.bid * (index+1)) + acc
        }
    }
    fun partTwo(lines: List<String>, possibleCardList: String): Int {
        //Read input data and generate data class structure
        val hands = readInput(lines, possibleCardList)
        //Sort hand by value adding jokers and apply calculation
        return hands.sortedWith(handComparatorJoker).foldIndexed(0) { index, acc, hand ->
            (hand.bid * (index+1)) + acc
        }
    }

}

fun main() {
    val day7 = Day7()
    val lines = File("inputs/inputDay7.txt").readLines()

    println("Part1: " + day7.partOne(lines,"123456789TJQKA").toString())
    println("Part2: " + day7.partTwo(lines,"123456789TQKA").toString())
}