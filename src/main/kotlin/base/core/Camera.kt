package base.core

import base.core.Tuple
import base.core.point
import kotlin.math.tan


class Camera(var hSize: Int, var vSize: Int, var fieldOfView: Double, var transform: Matrix = identityMatrix4x4()) {
    // hSize and vSize define the horizontal and vertical components of the camera's viewing plane
    // field of view is the angle of the camera wrt the scene
    // transform is a matrix describing how the scene should be oriented

    // pixel canvas properties
    var pixelSize: Double = 0.0
    var halfHeight: Double = 0.0
    var halfWidth: Double = 0.0
    var halfView: Double = 0.0
    var aspect: Double = 0.0

    init {
        // NOTE: Be very careful with types here, we use Int for horizontal and vertical sizes in terms of pixels
        // but need the double precision for the aspect (used with other double values further down)
        aspect= (hSize).toDouble() / (vSize).toDouble()
        halfView = tan(fieldOfView / 2)
        if (aspect >= 1) {
            halfWidth = halfView
            halfHeight = halfView / aspect
        } else {
            halfWidth = halfView * aspect
            halfHeight = halfView
        }
        pixelSize = (halfWidth * 2) / hSize
    }

    fun rayForPixel(x: Int, y: Int): Ray {
        // Given a camera and base.point on canvas, returns a ray that starts at camera and passes through pixel on canvas
        // calc the offset from the edge of the canvas to the pixel's center
        val xOffset: Double = (x + 0.5) * this.pixelSize
        val yOffset: Double = (y + 0.5) * this.pixelSize

        // the untransformed coordinates of the pixel in world space
        val worldX: Double = this.halfWidth - xOffset
        val worldY: Double = this.halfHeight - yOffset

        // using camera matrix, transform the canvas base.point and the origin
        // and then compute the ray's direction base.vector
        val pixel: Tuple = this.transform.inverse() * point(worldX, worldY, -1.0)
        val origin: Tuple = this.transform.inverse() * point(0.0, 0.0, 0.0)
        val direction: Tuple = (pixel - origin).normalize()
        return Ray(origin, direction)
    }

    fun render(w: World): Canvas {
        // render an image given a camera and a world
        val image: Canvas = Canvas(this.hSize, this.vSize)
        // go from 1 to h and v sizes as the write to pixel abstracts away details
        // of writing to right coordinates on canvas
        for (y in 1 .. this.vSize) {
            for (x in 1 .. this.hSize) {
                // for every pixel in the canvas, cast a ray to the pixel from camera and
                // set color of pixel on canvas
                val r: Ray = rayForPixel(x, y)
                val color: Color = w.colorAt(r)
                image.writePixel(x,y,color)
            }
        }
        return image
    }
}



