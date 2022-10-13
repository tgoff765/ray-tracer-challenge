package base.patterns

import base.core.*
import base.shapes.Shape

interface Pattern {
    var patternTransformation: Matrix

    // specific to each pattern, returns the color at a certain point in the pattern
    // by default return the color of the point of intersection (i.e. using the x/y/z points as the RGB values)
    fun colorAt(p: Tuple): Color {
        return Color(p.x, p.y, p.z)
    }
    // returns the pattern color at a point in OBJECT space
    fun colorAtObject(obj: Shape, worldPoint: Tuple): Color {
        // to convert from world to object point multiply by inverse of object
        // point transformations on worldPoint and then multiply by inverse of pattern
        // transformations to get pattern space
        val objPoint: Tuple = obj.transform.inverse() * worldPoint
        val patternPoint: Tuple = patternTransformation.inverse() * objPoint
        return colorAt(patternPoint)
    }
}