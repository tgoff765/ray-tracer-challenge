package demos

import base.core.*

// demonstration of the base.Tuple class to calculate position of a projectile over time intervals (called Ticks here)

data class Projectile(var position: Tuple, var velocity: Tuple)

data class Environment(var gravity: Tuple, var windSpeed: Tuple)

fun tick(env: Environment, proj: Projectile): Projectile {
    // calculate the position and velocity of a projectile
    // and return an instance of the demos.Projectile data class to represent it
    val position: Tuple = proj.position + proj.velocity
    val velocity: Tuple = proj.velocity + env.gravity + env.windSpeed
    return Projectile(position, velocity)
}

fun main() {
    // projectile starts one unit above the origin
    // velocity is normalized to 1 unit/demos.tick
    var p: Projectile = Projectile(point(0.0, 1.0, 0.0), vector(1.0,1.8,0.0).normalize() * 11.25)
    val e: Environment = Environment(vector(0.0, -0.1, 0.0), vector(-0.01, 0.0, 0.0))
    var tickCounter: Int = 0
    val canvas: Canvas = Canvas(900, 550)

    while (p.position.y >= 0) {
        p = tick(e, p)
        val xPosition: Int = p.position.x.toInt()
        val yPosition: Int = p.position.y.toInt()
        val plotColor: Color = Color(50.0, 162.0, 82.0)
        // Plot the base.point in our canvas
        // because y-coordinates are higher the lower we are in canvas, need to subtract y coordinate from
        // canvas height in order to get correct position
        // also need to add guardrails to make sure we don't try to plot a base.point outside the canvas
        if (xPosition >= 0 && xPosition <= canvas.width && yPosition <= canvas.height && yPosition >= 0) {
            canvas.writePixel(xPosition, (canvas.height - yPosition), plotColor)
        }
        tickCounter++
    }
    // Save projectile trajectory to a file
    canvas.savePPM("src/base.main/resources/projectile.ppm")

}
