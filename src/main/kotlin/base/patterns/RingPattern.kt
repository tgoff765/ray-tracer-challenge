package base.patterns

import base.core.*
import kotlin.math.floor
import kotlin.math.pow
import kotlin.math.sqrt

class RingPattern(val a: Color,
                  val b: Color,
                  override var patternTransformation: Matrix = identityMatrix4x4()) : Pattern {

    // implement a pattern that depends on distance in both X and Z directions, forming concentric rings
    override fun colorAt(p: Tuple): Color {
        return if (floor(sqrt(p.x.pow(2.0)  + p.z.pow(2.0))).mod(2.0) == 0.0 ) {
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