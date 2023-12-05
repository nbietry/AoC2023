import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import Day5.Range

class Day05Test {
    @Test
    fun `should return 51 with given entry`() {
        val day5 = Day5()
        val ranges = listOf(
            Range(start = 98, end = 100, diff = 48),
            Range(start=50, end=98, diff=-2))
        val got = day5.findRange(99L, ranges)
        val expected = 51L
        assertEquals(expected, got)
    }

    @Test
    fun `should return 40 with given entry`() {
        val day5 = Day5()
        val ranges = listOf(
            Range(start = 98, end = 100, diff = 48),
            Range(start=50, end=98, diff=-2))
        val got = day5.findRange(40L, ranges)
        val expected = 40L
        assertEquals(expected, got)
    }

}
