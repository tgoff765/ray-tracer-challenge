package base.shapes

import base.core.*
import base.shapes.*
import kotlin.math.pow
import kotlin.math.sqrt

class Sphere : Shape {
    // make life easier we're going to assume:
    // 1. Every sphere starts in the center at (0,0,0)
    // 2. Every sphere has a radii of 1

    // by default transform assigned to sphere is identity matrix
    override var transform: Matrix = identityMatrix4x4()

    // spheres have a default material (defaults of the Material class)
    override var material: Material = Material()

    override fun equals(other: Any?): Boolean {
        if (other !is Sphere) {
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

    override fun intersect(r: Ray): List<Intersection> {
        // 2022.10.04 refactored this to be an instance method, that calculates an intersection with the given shape
        // this is now part of the shape interface so every subtype shape now must implement this
        // given a ray this function returns a list of t values where the ray intersects the sphere
        // calculate the base.vector from the ray's origin to center of sphere

        // make sure we take the inverse of ray before running rest of intersect
        // this is to ensure we are working with intersection with object space not world space anymore
        val ray2: Ray = transform(r, transform.inverse())

        val sphereToRay: Tuple = ray2.origin - point(0.0, 0.0, 0.0)

        val a: Double = ray2.direction.dot(ray2.direction)
        val b: Double = 2 * ray2.direction.dot(sphereToRay)
        val c: Double = sphereToRay.dot(sphereToRay) - 1

        val discriminant: Double  = b.pow(2) - 4 * a * c

        if (discriminant < 0) {
            return listOf()
        }

        val t1: Double = (-b - sqrt(discriminant)) / (2 * a)
        val t2: Double = (-b + sqrt(discriminant)) / (2 * a)

        // make sure we return both intersections in increasing order
        return listOf(Intersection(t1, this), Intersection(t2, this)).sorted()
    }

    override fun normalAt(p: Tuple): Tuple {
        // convert the world base.point to object base.point by reversing the transform
        val objectPoint: Tuple = this.transform.inverse() * p
        // calculate the normal by subtracting object base.point from object center (now 0,0,0)
        val objectNormal: Tuple = objectPoint - point(0.0, 0.0, 0.0)
        val worldNormal: Tuple = this.transform.inverse().transpose() * objectNormal
        // avoid having the transform cause any issue with changing base.vector to a base.point
        worldNormal.w = 0.0
        return worldNormal.normalize()
    }
}

// helper function to produce a glass sphere
fun glass_sphere(): Sphere {
    val s: Sphere = Sphere()
    s.material.transparency = 1.0
    s.material.refractiveIndex = 1.5
    return s
}


fun main() {
    val w: World = defaultWorld()
    val r: Ray = Ray(point(0.0, 0.0, -5.0), vector(0.0, 0.0, 1.0))
    val intersectList: List<Intersection> = w.intersectWorld(r)
    println(intersectList.size)
}