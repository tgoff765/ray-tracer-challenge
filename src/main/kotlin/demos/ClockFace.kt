package demos

import base.core.*
import rotateZAxisTransformation
import translationTransformation
import kotlin.math.PI

fun clockface() {
    // orienting this so that z-axis is coming straight out at us, y is + going down, x is + going right
    val canvas: Canvas = Canvas(200, 200)
    // sets the radius of the circle (unit value would be 0,1,0)
    val startingPoint: Tuple = point(0.0, 80.0, 0.0)
    val plotColor: Color = Color(50.0, 162.0, 82.0)
    for (i in 0..11) {
        // we rotate each successive hour by PI / 6 then move it relative to center of board 100, 100 ,0
        val nextHour: Tuple =  translationTransformation(100.0, 100.0, 0.0) *
                rotateZAxisTransformation(PI * (i / 6.0)) *
                                 startingPoint
        canvas.writePixel(nextHour.x.toInt(), nextHour.y.toInt(), plotColor)
    }
    canvas.savePPM("src/base.main/resources/clockface.ppm")
}


fun main() {
    clockface()
}