import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import Day1

class Day01Test {
    @Test
    fun `test first number ni8ne`() {
        val got = Day1.regexGetFirstNumber.find("ni8ne")!!.value
        val expected = "8"
        assertEquals(expected, got)
    }
    @Test
    fun `test first number abceighthree2`() {
        val got = Day1.regexGetFirstNumber.find("abceighthree2")!!.value
        val expected = "eight"
        assertEquals(expected, got)
    }

    @Test
    fun `test last number abceighthree2`() {
        val got = Day1.regexGetLastNumber.find("abceighthree2".reversed())!!.value
        val expected = "2".reversed()
        assertEquals(expected, got)
    }
    @Test
    fun `test last number abceighthree`() {
        val got = Day1.regexGetLastNumber.find("abceighthree".reversed())!!.value
        val expected = "three".reversed()
        assertEquals(expected, got)
    }

}
