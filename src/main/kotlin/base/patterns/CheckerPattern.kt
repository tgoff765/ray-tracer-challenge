package base.patterns

import base.core.*
import kotlin.math.floor

class CheckerPattern(val a: Color,
                     val b: Color,
                        override var patternTransformation: Matrix = identityMatrix4x4()) : Pattern {
    // implements a 3D checker pattern on an object

    override fun colorAt(p: Tuple): Color {
        return if ((floor(p.x) + floor(p.y) + floor(p.z)).mod(2.0) == 0.0) {
            a
        } else {
            b
        }
    }

    override fun equals(other: Any?): Boolean {
        if (other !is RingPattern) {
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