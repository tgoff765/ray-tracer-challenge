package base.core

import base.shapes.*
import scalingTransformation
import translationTransformation
import kotlin.math.pow
import kotlin.math.sqrt

// World aka scene object composed of shapes and lights

class World {
    val objects: MutableList<Shape> = mutableListOf()
    var light: Light? = null

    fun intersectWorld(r: Ray): List<Intersection> {
        // intersect the world with the ray and return a sorted list of intersections
        val intersections: MutableList<Intersection> = mutableListOf()
        for (o: Shape in this.objects) {
            // for every object in the world intersect it with the ray
            intersections += o.intersect(r)
        }
        return intersections.sorted()
    }

    fun isShadowed(p: Tuple): Boolean {
        // casts a shadow ray from point to light to determine if a point is in shadow or not
        // Step 1: Take the distance between the point and the light source
        val distanceVector: Tuple = light!!.position - p
        val distance: Double = distanceVector.magnitude()
        // Step 2: Calculate the direction from point towards the light
        val direction: Tuple = distanceVector.normalize()
        // Step 3: Intersect world with ray
        // Note: this ray from the point to light is the shadow vector
        val r: Ray = Ray(p, direction)
        val intersections: List<Intersection> = intersectWorld(r)
        // Step 4: Check to see if there was a hit from the shadow vector
        // If there was an intersection and t is less than the distance to the light source
        // the ray must be intersecting an object in between light and the point and thus the point is in shadow
        val hits: Intersection? = hit(intersections)
        return hits != null && hits.t < distance
    }

    fun colorAt(r: Ray, remaining: Int = 5): Color {
        // intersect the world with a given ray and return the color at the intersection
        val worldIntersections: List<Intersection> = intersectWorld(r)
        // if there are no intersections or hit returns null (just negative intersections) then return black
        // TO DO: Might want to refactor this
        return if (worldIntersections.isEmpty() || hit(worldIntersections) == null) {
            Color(0.0,0.0,0.0)
        } else {
            // otherwise return the first intersection where ray intersects world
            val computedIntersections = prepareComputations(hit(worldIntersections)!!, r)
            // return the color at where the intersection hits
            shadeHit(computedIntersections, remaining)
        }
    }

    fun shadeHit(c: Comps, remaining: Int = 5): Color {
        // given a world and our precomputed intersection values, returns the color at an intersection
        // helper function for colorAt
        // check to see if point is in shadow
        val shadowed: Boolean = isShadowed(c.overPoint)

        // have to be careful with recursive calls here
        val surface: Color = c.obj.material.lighting(this.light!!, c.obj, c.overPoint, c.eyeV, c.normalV, shadowed)
        val reflected: Color = reflectedColor(c, remaining)
        val refracted: Color = refractedColor(c, remaining)

        // if surface material is both reflective and transparent add some reflectance
        val material: Material = c.obj.material
        if (material.reflective > 0 && material.transparency > 0) {
            val reflectance: Double = schlick(c)
            return surface + reflected * reflectance + refracted * (1 - reflectance)
        }

        return surface + reflected + refracted
    }

    fun reflectedColor(c: Comps, remaining: Int = 5): Color {
        // calculates the color of the reflection from an intersection
        // if object's material is not reflective, return BLACK (i.e. no reflection)
        // OR if the number of recursion calls left is less than 1 force return BLACK
        // this is to avoid infinite recursion in cases like facing mirrors where reflection vector could
        // occur infinitely
        // base case for recursion
        if (c.obj.material.reflective == 0.0 || remaining < 1) {
            return BLACK
        }
        // otherwise create reflection rays and intersect them with the world
        val reflectRay: Ray = Ray(c.overPoint, c.reflectV)
        // decrement remaining call count before calling colorAt
        val color: Color = colorAt(reflectRay, remaining - 1)
        return color * c.obj.material.reflective
    }

    fun refractedColor(c: Comps, remaining: Int = 5): Color {
        // calculate the color of the refraction ray
        // if object is completely opaque return BLACK or if the number of recursive calls remaining is 0
        if (c.obj.material.transparency == 0.0 || remaining == 0) {
            return BLACK
        }

        // Use Snell's law to determine if total internal refraction has occurred (when the light enters at such
        // am acute angle that it is reflected instead of passing through the medium as a refraction)
        // Return black if this is the case
        val nRatio: Double = c.n1 / c.n2
        val cosineI: Double = c.eyeV.dot(c.normalV)
        val sin2T: Double = nRatio.pow(2) * (1 - cosineI.pow(2))

        if (sin2T > 1.0) {
            return BLACK
        }

        // otherwise calculate the angle of refraction + direction refraction and compute the color of traced ray
        val cosineT: Double = sqrt(1.0 - sin2T)
        val direction: Tuple = c.normalV * ((nRatio * cosineI) - cosineT) - c.eyeV * nRatio
        val refractRay: Ray = Ray(c.underPoint, direction)
        // note function called with remaining - 1 so we reduce the numbers of recursive calls left
        return colorAt(refractRay, remaining - 1) * c.obj.material.transparency
    }

}


fun defaultWorld(): World {
    // default world has a single white light source + 2 spheres at center one with radius of 1, the other
    // with radius of 0.5
    val w: World = World()
    w.light = Light(point(-10.0, 10.0, -10.0), Color(1.0,1.0,1.0))
    val s1: Sphere = Sphere()
    s1.material = Material(color = Color(0.8, 1.0, 0.6), diffuse = 0.7, specular = 0.2)
    val s2: Sphere = Sphere()
    s2.transform = scalingTransformation(0.5, 0.5, 0.5)
    w.objects.add(s1)
    w.objects.add(s2)
    return w
}

fun main() {


    val w: World = defaultWorld()
    val r: Ray = Ray(point(0.0,0.0,-3.0), vector(0.0, -sqrt(2.0)/2.0, sqrt(2.0)/2.0))
    val floor: Shape = Plane()
    floor.transform = translationTransformation(0.0,-1.0,0.0)
    floor.material.reflective = 0.5
    floor.material.transparency = 0.5
    floor.material.refractiveIndex = 1.5
    val ball: Shape = Sphere()
    ball.material.color = Color(1.0,0.0,0.0)
    ball.material.ambient = 0.5
    ball.transform = translationTransformation(0.0, -3.5, -0.5)
    w.objects.add(floor)
    w.objects.add(ball)

    val xs: List<Intersection> = intersections(Intersection(sqrt(2.0), floor))
    val comps: Comps = prepareComputations(xs[0], r, xs)
    val c: Color = w.shadeHit(comps, 5)
    println(c.green)
}