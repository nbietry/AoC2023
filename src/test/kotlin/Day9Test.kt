import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.io.File

class Day09Test {
    @Test
    fun `should return 68 with given entry`() {
        val day9 = Day9(File("inputs/inputDay9_example.txt").readText())
        val got = day9.partOne()
        val expected = "68"
        assertEquals(expected, got)
    }
}
