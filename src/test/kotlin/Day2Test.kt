import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import Day2

class Day02Test {
    @Test
    fun `should return 8 with given entry`() {
        val day2 = Day2()
        val input = listOf("Game 1: 3 blue, 4 red; 1 red, 2 green, 6 blue; 2 green",
            "Game 2: 1 blue, 2 green; 3 green, 4 blue, 1 red; 1 green, 1 blue",
            "Game 3: 8 green, 6 blue, 20 red; 5 blue, 4 red, 13 green; 5 green, 1 red",
            "Game 4: 1 green, 3 red, 6 blue; 3 green, 6 red; 3 green, 15 blue, 14 red",
            "Game 5: 6 red, 1 blue, 3 green; 2 blue, 1 red, 2 green")
        val got = day2.partOne(input)
        val expected = "8"
        assertEquals(expected, got)
    }

    @Test
    fun `should return 2286 with given entry`() {
        val day2 = Day2()
        val input = listOf("Game 1: 3 blue, 4 red; 1 red, 2 green, 6 blue; 2 green",
            "Game 2: 1 blue, 2 green; 3 green, 4 blue, 1 red; 1 green, 1 blue",
            "Game 3: 8 green, 6 blue, 20 red; 5 blue, 4 red, 13 green; 5 green, 1 red",
            "Game 4: 1 green, 3 red, 6 blue; 3 green, 6 red; 3 green, 15 blue, 14 red",
            "Game 5: 6 red, 1 blue, 3 green; 2 blue, 1 red, 2 green")
        val got = day2.partTwo(input)
        val expected = "2286"
        assertEquals(expected, got)
    }
}
