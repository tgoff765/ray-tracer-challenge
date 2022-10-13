package base.shapes

import EPSILON
import base.core.*
import kotlin.math.abs

class Plane : Shape {
    // To simplify planes extend infinity in the x and z directions
    // We will then use transformations to rotate the plane around

    // by default transform assigned to plane is identity matrix
    override var transform: Matrix = identityMatrix4x4()

    // planes have a default material (defaults of the Material class)
    override var material: Material = Material()

    override fun normalAt(p: Tuple): Tuple {
        // because planes have no curvature, the normal vector at every point is constant
        return vector(0.0, 1.0, 0.0)
    }

    override fun intersect(r: Ray): List<Intersection> {
        // note that we are returning a list of intersections but the intersection of a ray and a plane
        // can have at most 1 intersection since we treat the case of a coplanar ray as having 0 intersections
        // (instead of infinitely many)

        // make sure we take the inverse of ray before running rest of intersect
        // this is to ensure we are working with intersection with object space not world space anymore
        // 2022.10.07 super important, don't forget to do this for future shapes!!
        val ray2: Ray = transform(r, transform.inverse())

        val i: MutableList<Intersection> = mutableListOf()
        // check if ray is parallel with Ray (i.e. its y slope is also 0)
        if (abs(ray2.direction.y) < EPSILON) {
            // if ray is parallel return an empty list
            return i
        }
        // otherwise to find intersection -originy / directiony
        // essentially the distance of the ray's origin to the y = 0 point along ray
        val t: Double = -ray2.origin.y / ray2.direction.y

        i.add(Intersection(t, this))
        return i
    }

    override fun equals(other: Any?): Boolean {
        if (other !is Plane) {
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