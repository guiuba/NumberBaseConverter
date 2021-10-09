import java.math.BigDecimal
import java.math.RoundingMode
import kotlin.math.pow

var exitProgram = false
var continueSameConversion = true
fun main() {

    while (!exitProgram) {
        showMenu()
    }
}

fun showMenu() {
    continueSameConversion = true
    println("Enter two numbers in format: {source base} {target base} (To quit type /exit)")
    val input = readLine()!!
    if (input == "/exit") {
        continueSameConversion = false
        exitProgram = true
    } else {
        val inputValues = input.split(" ")
        val sourceBase = inputValues[0].toInt()
        val targetBase = inputValues[1].toInt()
        while (continueSameConversion) {
            collectSourceNumber(sourceBase, targetBase)
        }
    }
}

fun collectSourceNumber(sourceBase: Int, targetBase: Int) {
    println("Enter number in base $sourceBase to " +
            "convert to base $targetBase(To go back type /back)")
    val input = readLine()!!
    if (input == "/back") {
        continueSameConversion = false
        showMenu()
    } else {
        val hasFractional = input.contains(".")
        val integerPart = input.substringBefore(".")
        var integerPartInTargetBase = convertIntPart(integerPart, sourceBase, targetBase)
        var fractionalPartInTargetBase = ""
        if (hasFractional) {
            var fractionalPart = input.substringAfter(".")
            fractionalPartInTargetBase = (convertFracPart(fractionalPart, sourceBase, targetBase))
                .substring(1)
        }
        val finalResult = integerPartInTargetBase + fractionalPartInTargetBase
        println("Conversion result: $finalResult")
    }
}


fun convertIntPart(integerPart: String, sourceBase: Int, targetBase: Int): String {
    //converts integer part of number to decimal and then to new base
    return integerPart.toBigInteger(sourceBase).toString(targetBase)
}

fun convertFracPart(fractionalPart: String, sourceBase: Int, targetBase: Int): String {
    //converts fractional part of number to decimal
    var fracPartInBase10 = BigDecimal(0)
    for (i in fractionalPart.indices) {
        val ch = fractionalPart[i];
        val value = Character.getNumericValue(ch).toDouble()
        fracPartInBase10 += (value / sourceBase.toDouble().pow((i + 1).toDouble())).toBigDecimal()
    }
    // converts fractional part of number from decimal to target base
    var fracPartInTargetBase = "0."
    var aux = fracPartInBase10 // fracPartInTargetBase
    while (aux > BigDecimal.ZERO && fracPartInTargetBase.length < 6) {
        aux *= targetBase.toBigDecimal()
        val wholePart = aux.toBigInteger()
        fracPartInTargetBase += Character.forDigit(wholePart.toInt(), targetBase)
        aux -= wholePart.toBigDecimal()
    }

    if (fracPartInTargetBase.length < 7) {
        while (fracPartInTargetBase.length < 7) {
            fracPartInTargetBase += "0"
        }
    }
    val regex = Regex(".*[a-z].*")
    return if (fracPartInTargetBase == ".") {
        "0.00000"
    } else if (fracPartInTargetBase.matches(regex)) {
        fracPartInTargetBase
    } else {
        BigDecimal(fracPartInTargetBase).setScale(5, RoundingMode.CEILING).toString()
    }
}
