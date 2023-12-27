import java.io.File

fun main() {

    val linesPart1 = File("inputs/inputDay19.txt").readText()
    val linesPart2 = File("inputs/inputDay19_example.txt").readText()
    println("Part1: " + Day19(linesPart1).partOne())
    //println("Part2: " + Day19(linesPart2).partTwo())
}

class Day19(input: String) {
    data class Rule(val name: String, val conditions: List<Condition>)
    data class Condition(val op1: String, val opSign: String, val op2: String, val result: String)
    private fun extractConditions(input: String): List<Rule> {
        val regexRules = "(\\w+)\\{([^{}]+)}".toRegex()
        val regexCondition = "(\\w+)([<>]=?|==)(\\d+):(.+)".toRegex()
        return regexRules.findAll(input).map { matchResult ->
            val (name, conditionString) = matchResult.destructured
            val conditions = conditionString.split(',').map {
                val conditions = regexCondition.find(it)
                conditions?.let {
                    val (variable, operator, value, result) = it.destructured
                    Condition(variable, operator, value, result)
                }?:Condition(it,"","","")
            }
            Rule(name, conditions)
        }.toList()
    }
    private fun evaluateCondition(data: Map<String, Int>, workflow : List<Rule>): String{
        var currentRule = workflow.find { it.name == "in" }
        var finalResult = ""
        while (currentRule != null){
            var result = ""
            for (condition in currentRule.conditions.dropLast(1)) {
                when (condition.opSign) {
                    "<" -> if (data[condition.op1]!! < condition.op2.toInt()){
                        result = condition.result
                        break
                    }
                    ">" -> if (data[condition.op1]!! > condition.op2.toInt()){
                        result = condition.result
                        break
                    }
                }
            }
            if (result == "") result = currentRule.conditions.last().op1
            finalResult = result
            currentRule = workflow.find { it.name == result }
        }

        return finalResult
    }
    private fun extractXMASValues(input: String): Map<String, Int> {
        val regex = """(\w)=(\d+)""".toRegex()
        val matches = regex.findAll(input)
        val values = mutableMapOf<String, Int>()
        matches.forEach { matchResult ->
            val (variable, value) = matchResult.destructured
            values[variable] = value.toIntOrNull() ?: 0 // Assuming default value as 0 if parsing fails
        }
        return values
    }

    private val workflow = input.split("\n\n")[0]
    private val ratings = input.split("\n\n")[1]
    fun partOne():String{
        val rulesList = extractConditions(workflow)
        return ratings.lines().map { rating ->
            val xmasValues = extractXMASValues(rating)
            val conditionResult = evaluateCondition(xmasValues, rulesList)

            println(conditionResult to xmasValues.values)
            conditionResult to xmasValues.values.sum()
        }.filter { it.first == "A" }.sumOf { it.second }.toString()
    }
    fun partTwo():String{
        TODO()
    }

}