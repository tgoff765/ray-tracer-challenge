package base.core

import equalsDelta

class Color(var red: Double, var green: Double, var blue: Double) {

    override fun equals(other: Any?): Boolean {
        // use our epsilon double function to test if two tuples are equal
        return (((other is Color)
                && red.equalsDelta(other.red)
                && green.equalsDelta(other.green)
                && blue.equalsDelta(other.blue)))
    }

    override fun hashCode(): Int {
        var result = red.hashCode()
        result = 31 * result + green.hashCode()
        result = 31 * result + blue.hashCode()
        return result
    }

    // TO DO: This to Int conversion for a string is needed for rendering need to remove this dependency
    override fun toString(): String {
        return "${red.toInt()} ${green.toInt()} ${blue.toInt()}"
    }

    operator fun plus(b: Color): Color {
        return Color(red + b.red, green + b.green, blue + b.blue)
    }

    operator fun minus(b: Color): Color {
        return Color(red - b.red, green - b.green, blue - b.blue)
    }

    operator fun times(b: Double): Color {
        return Color(red * b, green * b, blue * b)
    }

    operator fun times(b: Color): Color {
        // used to represent mixing two different colors together
        return Color(red * b.red, green * b.green, blue * b.blue)
    }

    fun constrainColor(): Color {
        // helper function to scale values to 0 - 255 of RGB
        var newRed: Double = red * 255
        var newGreen: Double = green * 255
        var newBlue: Double = blue * 255

        // check to see if each value is above or below scale
        if (newRed > 255.0) {
            newRed = 255.0
        } else if (newRed < 0.0) {
            newRed = 0.0
        }

        if (newGreen > 255.0) {
            newGreen = 255.0
        } else if (newGreen < 0.0) {
            newGreen = 0.0
        }

        if (newBlue > 255.0) {
            newBlue = 255.0
        } else if (newBlue < 0.0) {
            newBlue = 0.0
        }
        return Color(newRed, newGreen, newBlue)
    }

}

// RGB constants for convenience
val BLACK = Color(0.0,0.0,0.0)
val WHITE = Color(1.0, 1.0,1.0)

fun main() {
    print(Color(1.0, 0.2, 0.4) + Color(0.9, 1.0, 0.1))
}