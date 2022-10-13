package base.patterns

import base.core.*
import kotlin.math.floor

class StripePattern(val a: Color, val b: Color, override var patternTransformation: Matrix = identityMatrix4x4()) :
    Pattern {
    // class represents horizontal bars of alternating colors in the x direction

    // returns the pattern color of the point in WORLD space
    override fun colorAt(p: Tuple): Color {
        // pattern only alternates in X
        return if (floor(p.x).mod(2.0) == 0.0 ) {
            a
        } else {
            b
        }
    }

    override fun equals(other: Any?): Boolean {
        if (other !is StripePattern) {
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


