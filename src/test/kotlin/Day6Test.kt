import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class Day06Test {
    @Test
    fun `should return 6 with given entry`() {
        val day6 = Day6()
        val got = day6.getDistance(1L, 7L)
        val expected = 6L
        assertEquals(expected, got)
    }

    @Test
    fun `should return 12 with given entry`() {
        val day6 = Day6()
        val got = day6.getDistance(3L, 7L)
        val expected = 12L
        assertEquals(expected, got)
    }

}
