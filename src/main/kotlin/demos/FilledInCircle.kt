package demos

import base.core.*
import translationTransformation
import base.shapes.*

// base.main takeaway from this exercise: important to define world coordinates of objects and rays independent of
// the pixel canvas size 
fun filledInCircle() {
    val rayOrigin: Tuple = point(0.0, 0.0, -5.0)
    // setting up world space coordinate constants
    val wallZ: Double = 10.0
    val wallSize: Double = 7.0
    val canvasPixels: Int = 100
    val pixelSize: Double = wallSize/canvasPixels
    val half: Double = wallSize/2

    // create canvas, color and shape
    val canvas: Canvas = Canvas(canvasPixels, canvasPixels)
    val color: Color = Color(255.0,0.0,0.0)
    val shape: Sphere = Sphere()

    // try moving the sphere left/right/up and down
    shape.transform = translationTransformation(1.0, 0.0, 10.0)

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
            val r: Ray = Ray(rayOrigin, position - rayOrigin)

            // see if there's any intersection between the shape and the current ray
            val inter: List<Intersection> = shape.intersect(r)
            if (hit(inter) != null) {
                canvas.writePixel(x, y, color)
            }
        }
    }
    canvas.savePPM("src/base.main/resources/filledincircle.ppm")
}

fun main() {
    filledInCircle()
}