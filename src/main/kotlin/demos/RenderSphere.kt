package demos

import base.core.*
import base.shapes.Sphere

// base.main takeaway from this exercise: important to define world coordinates of objects and rays independent of
// the pixel canvas size
fun renderSphere() {
    val rayOrigin: Tuple = point(0.0, 0.0, -5.0)
    // setting up world space coordinate constants
    val wallZ: Double = 10.0
    val wallSize: Double = 7.0
    val canvasPixels: Int = 1000
    val pixelSize: Double = wallSize/canvasPixels
    val half: Double = wallSize/2

    // create canvas, and shape
    val canvas: Canvas = Canvas(canvasPixels, canvasPixels)
    val shape: Sphere = Sphere()

    // assign material to sphere
    shape.material = Material(color = Color(0.3, 0.2, 1.0))

    // add a light source
    val light: Light = Light(point(-10.0, 10.0, -10.0), Color(1.0, 1.0, 1.0))


    // for each row of pixels in the canvas, calculate the world coordinates
    for (y in 0 until canvasPixels) {
        // top = + half, bottom = -half, 3.5 to -3.5
        val worldY: Double = half - pixelSize * y
        for (x in 0 until canvasPixels) {
            // left = - half, right = +half, -3.5 to 3.5
            // negative because we start from left (which is x = -3.5)
            val worldX: Double = -half + pixelSize * x
            // describe the base.point the ray will target
            // reminder that z is set to z=10 for the entire wall
            val position: Tuple = point(worldX, worldY, wallZ)
            // create a ray starting from the common origin going to current destination
            // make sure to normalize ray here
            val r: Ray = Ray(rayOrigin, (position - rayOrigin).normalize())

            // see if there's any intersection between the shape and the current ray
            val inter: List<Intersection> = shape.intersect(r)
            if (hit(inter) != null) {
                val hit: Intersection = hit(inter)!!
                // if we find a hit, find the eye base.vector from the closest hit
                val point: Tuple = r.position(hit.t)
                if (hit.s is Sphere) {
                    val normal: Tuple = hit.s.normalAt(point)
                    val eye: Tuple = -r.direction
                    val color: Color = hit.s.material.lighting(light, hit.s, point, eye, normal)
                    canvas.writePixel(x, y, color)
                }
            }
        }
    }
    canvas.savePPM("src/base.main/resources/renderedSphere.ppm")
}

fun main() {
    renderSphere()
}