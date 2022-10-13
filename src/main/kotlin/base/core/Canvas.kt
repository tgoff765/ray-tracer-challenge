package base.core

import java.io.File

class Canvas(val width: Int, val height: Int) {

    // canvas colors will be 0-indexed, create a list of lists of the specified size with all pixels set to black
    var colors: MutableList<MutableList<Color>> = MutableList(height) {MutableList(width) {Color(0.0,0.0,0.0)}}
    // P3 is the "magic number for the format we want
    // width/height supplied from canvas, 255 refers to the max pixel color value allowd
    var header: String = "P3\n${width} ${height}\n255\n"

    override fun toString(): String {
        val colorString: String = colors.joinToString("\n").trim()
        // Getting funky results with Kotlin's multiline string function so I broke up the color string seperately
        // before concatenating with the demos.demos.base.main string
        return """
            Canvas:
            Width: $width
            Height: $height 
            """.trimIndent() + "\nColors: \n$colorString"
    }

    fun writePixel(width: Int, height: Int, c: Color): Unit {
        // note because of 0 indexing need to subtract 1 from height and width
        colors[height - 1][width - 1] = c
    }

    fun getPixel(width: Int, height: Int): Color {
        return colors[height - 1][width - 1]
    }

    fun canvasToPPM(): String {
        // get the list of pixels and format to one line
        return header + colors.flatten().map { it.constrainColor() }
            .toString()
            .replace(",", "")
            .replace("[", "")
            .replace("]", "")
            // after 10 occurrences of a series of numbers followed by space, insert in a newline
            // this is to keep lines below the 70 char limit imposed by PPM
            .replace("((\\d*\\s){10})".toRegex(), "$1\n") +
            // adding newline at end because some readers won't read unless its terminated with a new line
            "\n"
    }

    fun savePPM(filepath: String) {
        File(filepath).writeText(canvasToPPM())
    }

}

fun main() {
    val test: Canvas = Canvas(15,15)
    test.writePixel(1,1,Color(1.5,1.0,10000.0))
    print(test.canvasToPPM())
    // should just be an image with 1 white pixel in upper left-hand corner
    test.savePPM("src/base.main/resources/testimage2.ppm")
}