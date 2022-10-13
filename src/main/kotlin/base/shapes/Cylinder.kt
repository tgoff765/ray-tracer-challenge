package base.shapes

import base.core.*
import equalsDelta
import kotlin.math.pow
import kotlin.math.sqrt

class Cylinder(val min: Double = Double.NEGATIVE_INFINITY, val max: Double = Double.POSITIVE_INFINITY) : Shape {

    // by default transform assigned to cylinder is identity matrix
    override var transform: Matrix = identityMatrix4x4()

    // cubes have a default material (defaults of the Material class)
    override var material: Material = Material()

    override fun intersect(r: Ray): List<Intersection> {
        // take inverse of ray to make sure we are intersecting in object space and not world space
        val r2: Ray = transform(r, transform.inverse())
        val a: Double = r2.direction.x.pow(2) + r2.direction.z.pow(2)

        // ray is parallel
        if (a.equalsDelta(0.0)) {
            return listOf()
        }

        val b: Double = 2 * r2.origin.x * r2.direction.x +
                        2 * r2.origin.z * r.direction.z
        val c: Double = r.origin.x.pow(2) + r.origin.z.pow(2) - 1
        val disc: Double = b.pow(2) - 4 * a * c

        // ray does not intersect the cylinder
        if (disc < 0.0) {
            return listOf()
        }
        // otherwise calculate the intersections and return
        val t0: Double = (-b - sqrt(disc)) / (2 * a)
        val t1: Double = (-b + sqrt(disc)) / (2 * a)
        return listOf(Intersection(t0, this), Intersection(t1, this))
    }

    override fun normalAt(p: Tuple): Tuple {
        return vector(p.x, 0.0, p.z)
    }

    override fun equals(other: Any?): Boolean {
        if (other !is Cylinder) {
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