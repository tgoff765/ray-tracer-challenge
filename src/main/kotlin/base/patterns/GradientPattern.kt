package base.patterns

import base.core.*
import kotlin.math.floor

class GradientPattern(val a: Color,
                      val b: Color,
                      override var patternTransformation: Matrix = identityMatrix4x4()
) : Pattern {
    // linear interpolation pattern that varies the color from a to b as we move along x axis

    override fun colorAt(p: Tuple): Color {
        val distance: Color = b - a
        val fraction: Double = p.x - floor(p.x)
        return a + distance * fraction
    }

    override fun equals(other: Any?): Boolean {
        if (other !is GradientPattern) {
            return false
        }

        if (a != other.a || b != other.b) {
            return false
        }

        return true
    }

    override fun hashCode(): Int {
        var result = a.hashCode()
        result = 31 * result + b.hashCode()
        return result
    }


}