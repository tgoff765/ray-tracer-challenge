package base.core

import equalsDelta
import kotlin.math.pow
import kotlin.math.sqrt

enum class TupleType { POINT, VECTOR }

class Tuple(var x: Double, var y: Double, var z: Double, var w: Double) {
    // Tuples represent a Features.base.point or a Features.base.vector in 3-Dimensional space using a left-hand coordinate system

    fun tupleType() : TupleType {
        return if (w.equalsDelta(1.0)) TupleType.POINT else TupleType.VECTOR
    }

    override fun equals(other: Any?): Boolean {
        // use our epsilon double function to test if two tuples are equal
        return (((other is Tuple) && x.equalsDelta(other.x)
                && y.equalsDelta(other.y)
                && z.equalsDelta(other.z)
                && (tupleType() == other.tupleType())))
    }


    // operator functions to override core operations on tuples (+, -, negation, *, / etc)
    operator fun plus(b : Tuple): Tuple {
        // rules of addition: base.point + base.vector = base.point (moving base.point along base.vector path)
        //                    base.vector + base.vector = another base.vector
        //                    base.point + base.point --> NOT ALLOWED
        if (w + b.w > 1) {
            throw Exception("Cannot add two points together!")
        }
        // otherwise just add the individual components of the two tuples together
        return Tuple(x + b.x, y + b.y, z + b.z, w + b.w)
    }

    operator fun minus(b: Tuple): Tuple {
        // rules of subtraction: base.point - base.point = base.vector (base.vector that points along base.vector path from p2 -> p1)
        //                       base.point - base.vector = base.point (base.point moved back by base.vector)
        //                       base.vector - base.point --> NOT ALLOWED!
        if (w - b.w < 0) {
            throw Exception("Cannot subtract a base.point from a base.vector!")
            // otherwise just subtract the individual components of the two tuples together
        }
        return Tuple(x - b.x, y - b.y, z - b.z, w - b.w)
    }

    operator fun times(b: Double): Tuple {
        // scalar multiplication
        return Tuple(x * b, y * b, z * b, w * w)
    }

    operator fun div(b: Double): Tuple {
        // scalar division
        return Tuple(x / b, y / b, z / b , w / b )
    }

    operator fun unaryMinus(): Tuple {
        // negate the x,y,and z variables
        return Tuple(-x,-y, -z,-w)
    }

    // more advanced operations on Tuples
    fun magnitude(): Double {
        // magnitude represents the distance covered walking from the origin to the base.vector
        // Special case: magnitude of unit vectors is 1
        return sqrt(x.pow(2) + y.pow(2) + z.pow(2) + w.pow(2))
    }

    fun normalize(): Tuple {
        // return a version of the tuple that has been scaled down to a magnitude of 1
        val currentMagnitude: Double = magnitude()
        return Tuple(x / currentMagnitude, y / currentMagnitude, z / currentMagnitude, w / currentMagnitude)
    }

    fun dot(b: Tuple): Double {
        // calculates the dot product between two vectors, which represents the cosine of the angle the two vectors make
        return x * b.x + y * b.y + z * b.z + w * b.w
    }

    fun crossProduct(b: Tuple): Tuple {
        // returns a base.vector orthogonal to the two vectors
        // NOTE: order matters here, i.e. a.crossProduct(b) != b.crossProduct(a)
        // Also this operation can be applied to any base.Tuple, but has no relevant meaning for Points
        // which is why we use the base.vector factory function to always return a base.vector
        return vector(y * b.z - z *  b.y,
                    z * b.x - x * b.z,
                    x *b.y - y * b.x)
    }

    override fun toString(): String {
        return "${tupleType()}(x:$x, y: $y, z:$z)"

    }
    override fun hashCode(): Int {
        var result = x.hashCode()
        result = 31 * result + y.hashCode()
        result = 31 * result + z.hashCode()
        result = 31 * result + w.hashCode()
        return result
    }

}

// Factory functions to return specific types of tuples
fun point(x: Double, y: Double, z: Double) : Tuple {
    return Tuple(x, y, z, 1.0)
}

fun vector(x: Double, y: Double, z: Double) : Tuple {
    return Tuple(x, y, z, 0.0)
}

// takes a base.vector v and returns the reflected base.vector over the normal
fun reflect(v: Tuple, normal: Tuple): Tuple {
    return v - normal * 2.0 * v.dot(normal)
}


fun main() {
    val testVector1: Tuple = vector(1.0, -1.0, 0.0)
    val testNormalVector: Tuple = vector(0.0, 1.0, 0.0)
    print(reflect(testVector1, testNormalVector))
}





