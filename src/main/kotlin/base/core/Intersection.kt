package base.core

import EPSILON
import base.core.Ray
import base.core.Tuple
import base.shapes.Sphere
import base.shapes.Shape
import base.shapes.glass_sphere
import equalsDelta
import scalingTransformation
import translationTransformation
import kotlin.math.pow
import kotlin.math.sign
import kotlin.math.sqrt

class Intersection(val t: Double, val s: Shape): Comparable<Intersection> {
    // Intersection class encapsulates an intersection t and an object s
    override fun compareTo(other: Intersection): Int = when {
        this.t != other.t -> this.t compareTo other.t // compareTo() in the infix form
        else -> 0
    }

    override fun equals(other: Any?): Boolean {
        if (other !is Intersection) {
            return false
        }

        if (t != other.t || s != other.s) {
            return false
        }
        return true
    }

    override fun hashCode(): Int {
        var result = t.hashCode()
        result = 31 * result + s.hashCode()
        return result
    }


}

// data class to store some pre computed values
data class Comps(val t: Double,
                 val obj: Shape,
                 val p: Tuple,
                 val eyeV: Tuple,
                 val normalV: Tuple,
                 val inside: Boolean,
                 // n1 and n2 are the refractive indices of the ray entering and exiting the material
                 val n1: Double = 0.0,
                 val n2: Double = 0.0,
                 val reflectV: Tuple = vector(0.0,0.0,0.0),
                 val overPoint: Tuple = point(0.0, 0.0, 0.0),
                 val underPoint: Tuple = point(0.0,0.0,0.0))

fun intersections(vararg i: Intersection): List<Intersection> {
    // take a variable number of intersection objects and return them as a sorted list
    return i.asList().sorted()
}

fun hit(i: List<Intersection>): Intersection? {
    // given a list of intersections returns the intersection with the lowest non-negative t value
    // this can return null if all hits are negative (i.e. behind the origin of ray)

    // 2022.10.07 adding sign filter here, before we fudge the equality with the equals
    // delta we have to make sure the sign is not negative
    // this should help us stop returning very small negative intersection values like -1E5
    // sign(it.t) != -1.0 && (it.t.equalsDelta(0.0) ||
    // i.filter { !it.t.equalsDelta(0.0)  && sign(it.t) == 1.0
    if (i.filter { it.t >= 0.0 }.sorted().isNotEmpty()) {
        return i.filter { it.t >= 0.0}.sorted()[0]
    } else {
        return null
    }
}

fun prepareComputations(i: Intersection, r: Ray, xs: List<Intersection> = listOf(i)): Comps {
    // function computes some values from an intersection of a shape and a ray
    // and saves them to our comps data class so they can be reused
    val t: Double = i.t
    val s: Shape = i.s
    val point: Tuple = r.position(i.t)
    val eyeV: Tuple = -r.direction
    var normalV: Tuple = s.normalAt(r.position(i.t))
    // mathematically determine if normal points away from eye base.vector iff the dot product of the two vectors
    // is negative
    val inside: Boolean
    if (normalV.dot(eyeV) < 0) {
        inside = true
        normalV = -normalV
    } else {
        inside = false
    }
    // calculate the reflection vector from point of intersection
    val reflectV: Tuple = reflect(r.direction, normalV)
    // point used to move points slightly above surface to prevent self shadowing
    val overPoint: Tuple = point + normalV * EPSILON
    // point used to move points slightly below surface to prevent issues with refraction
    val underPoint: Tuple = point - normalV * EPSILON
    // calculate n1 and n2
    var n1: Double = 0.0
    var n2: Double = 0.0

    val containers: MutableList<Shape> = mutableListOf()

    for (inter: Intersection in xs) {
        // for each intersection
        if (inter == i) {
            if (containers.isEmpty()) {
                n1 = 1.0
            } else {
                n1 = containers.last().material.refractiveIndex
            }
        }

        if (inter.s in containers) {
            containers.remove(inter.s)
        } else {
            containers.add(inter.s)
        }

        if (inter == i) {
            if (containers.isEmpty()) {
                n2 = 1.0
            } else {
                n2 = containers.last().material.refractiveIndex
            }
        }
    }


    return Comps(t, s, point, eyeV, normalV, inside, n1, n2, reflectV, overPoint, underPoint)
}

fun schlick(c: Comps): Double {
    // returns the fraction of light that is reflected given surface information at the hit
    // find the cosine of the angle between the eye and the normal vectors
    var cos: Double = c.eyeV.dot(c.normalV)

    // total internal reflection can only occur if n1 > n2
    if (c.n1 > c.n2) {
        val n: Double = c.n1 / c.n2
        val sin2T: Double = n.pow(2.0) * (1.0 - cos.pow(2.0))

        if (sin2T > 1.0) {
           return 1.0
        }

        // compute cosine of thetaT using trig identity
        val cosT: Double = sqrt(1.0 - sin2T)

        // when n1 > n2, use cosine(thetaT) instead
        cos = cosT
    }

    val r0: Double = ((c.n1 - c.n2) / (c.n1 + c.n2)).pow(2.0)

    return r0 + (1 - r0) * (1 - cos).pow(5.0)
}

fun main() {
    val A: Sphere = glass_sphere()
    val B: Sphere = glass_sphere()
    val C: Sphere = glass_sphere()
    A.transform = scalingTransformation(2.0, 2.0, 2.0)
    A.material.refractiveIndex = 1.5
    B.transform = translationTransformation(0.0, 0.0, -0.25)
    B.material.refractiveIndex = 2.0
    C.transform = translationTransformation(0.0, 0.0, 0.25)
    C.material.refractiveIndex = 2.5
    val r: Ray = Ray(point(0.0,0.0,-4.0), vector(0.0,0.0,1.0))
    val xs: List<Intersection> = intersections(Intersection(2.0, A),
        Intersection(2.75, B),
        Intersection(3.25, C),
        Intersection(4.75, B),
        Intersection(5.25, C),
        Intersection(6.0, A))
    val comps1: Comps = prepareComputations(xs[1], r, xs)
    println(comps1.n1)

}