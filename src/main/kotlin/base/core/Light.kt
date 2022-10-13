package base.core

import base.core.Tuple

class Light(val position: Tuple, val intensity: Color) {

    override fun equals(other: Any?): Boolean {
        if (other !is Light) {
            return false
        }

        if (position != other.position || intensity != other.intensity) {
            return false
        }
        return true
    }

    override fun hashCode(): Int {
        var result = position.hashCode()
        result = 31 * result + intensity.hashCode()
        return result
    }
}