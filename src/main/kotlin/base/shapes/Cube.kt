package base.shapes

import EPSILON
import base.core.*
import kotlin.Double.Companion.POSITIVE_INFINITY
import kotlin.Double.Companion.NEGATIVE_INFINITY
import kotlin.math.abs

data class CubeIntersection(val tMin: Double, val tMax: Double )

class Cube : Shape {
    // assume all cubes are axis-aligned bounding boxes, i.e. its sides are all aligned with the scene's axes

    // by default transform assigned to cube is identity matrix
    override var transform: Matrix = identityMatrix4x4()

    // cubes have a default material (defaults of the Material class)
    override var material: Material = Material()

    private fun checkAxis(origin: Double, direction: Double): CubeIntersection {
        // helper function to find the tMin and tMax intersections of a line
        val tMinNumerator: Double = (-1 - origin)
        val tMaxNumerator: Double = (1 - origin)
        var tMin: Double
        var tMax: Double

        if (abs(direction) >= EPSILON) {
            tMin = tMinNumerator / direction
            tMax = tMaxNumerator / direction
        } else {
            // if direction is essentially 0 we make the tMin/tMax infinity
            tMin = -1 * tMinNumerator * NEGATIVE_INFINITY
            tMax = tMaxNumerator * POSITIVE_INFINITY
        }

        // if tMin ends up being higher, reassign value to tMax
        if (tMin > tMax) {
            tMin = tMax.also { tMax = tMin }
        }

        return CubeIntersection(tMin, tMax)
    }

    override fun intersect(r: Ray): List<Intersection> {
        // take inverse of ray to make sure we are intersecting in object space and not world space
        val r2: Ray = transform(r, transform.inverse())

        // intersections will with Ray will always occur at the largest minimum
        // and smallest maximum (picture in book helps with this)
        val xIntersection: CubeIntersection = checkAxis(r2.origin.x, r.direction.x)
        val yIntersection: CubeIntersection = checkAxis(r2.origin.y, r.direction.y)
        val zIntersection: CubeIntersection = checkAxis(r2.origin.z, r.direction.z)

        val tMin: Double = maxOf(xIntersection.tMin, yIntersection.tMin, zIntersection.tMin)
        val tMax: Double = minOf(xIntersection.tMax, yIntersection.tMax, zIntersection.tMax)

        // if tMin > tMax there can't be an intersection
        if (tMin > tMax) {
            return listOf()
        }

        return listOf(Intersection(tMin, this), Intersection(tMax, this))
    }

    override fun normalAt(p: Tuple): Tuple {
        // get the object point
        val objectPoint: Tuple = this.transform.inverse() * p

        // take the max absolute value component, this will tell us what axis of the face
        // of the cube we're taking the normal of
        val maxC: Double = maxOf(abs(objectPoint.x), abs(objectPoint.y), abs(objectPoint.z))

        if (maxC == abs(objectPoint.x)) {
            return vector(objectPoint.x, 0.0,0.0)
        } else if (maxC == abs(objectPoint.y)) {
            return vector(0.0, objectPoint.y, 0.0)
        } else {
            return vector(0.0, 0.0, objectPoint.z)
        }
    }

    override fun equals(other: Any?): Boolean {
        if (other !is Cube) {
            return false
        }

        if (transform != other.transform || material != other.material) {
            return false
        }
        return true
    }

    override fun hashCode(): Int {
        var result = transform.hashCode()
        result = 31 * result + material.hashCode()
        return result
    }
}

fun main() {
    val c: Cube = Cube()
    val r1: Ray = Ray(point(5.0, 0.5, 0.0), vector(-1.0,0.0,0.0))
    val xs1: List<Intersection> = c.intersect(r1)
    println(xs1.size)
}
