package demos

import base.core.*
import base.shapes.Plane
import base.shapes.Shape
import base.shapes.Sphere
import scalingTransformation
import translationTransformation
import viewTransformation
import kotlin.math.PI

fun renderPlaneWithSphere(): Canvas {

    // Step 1: Create floor as plane
    val floor: Plane = Plane()
    floor.transform = scalingTransformation(10.0,10.0,10.0)
    floor.material = Material()
    floor.material.color = Color(1.0, 0.9, 0.9)
    floor.material.specular = 0.0

    // Step 2: Create middle sphere
    val middle: Sphere = Sphere()
    middle.transform = translationTransformation(-0.5, 1.0, 0.5)
    middle.material = Material()
    middle.material.color = Color(0.1, 1.0, 0.5)
    middle.material.diffuse = 0.7
    middle.material.specular = 0.3

    // Step 3: Create middle right sphere
    val right: Sphere = Sphere()
    right.transform = translationTransformation(1.5, 0.5, -0.5) * scalingTransformation(0.5, 0.5, 0.5)
    right.material = Material()
    right.material.color = Color(0.5, 1.0, 0.1)
    right.material.diffuse = 0.7
    right.material.specular = 0.3

    // Step 4: Create middle left sphere
    val left: Sphere = Sphere()
    left.transform = translationTransformation(-1.5, 0.33, -0.75) * scalingTransformation(0.33, 0.33, 0.33)
    left.material = Material()
    left.material.color = Color(1.0, 0.8, 0.1)
    left.material.diffuse = 0.7
    left.material.specular = 0.3

    // Step 5: Configure camera, light, and world
    val shapes: List<Shape>  = mutableListOf(floor, left, right, middle)

    val sphereWorld: World = World()
    sphereWorld.objects.addAll(shapes)

    sphereWorld.light = Light(point(-10.0, 10.0,-10.0), Color(1.0, 1.0, 1.0))

    val camera: Camera = Camera(100, 100, PI/3)
    camera.transform = viewTransformation(point(0.0, 1.5, -5.0),point(0.0,1.0,0.0), vector(0.0,1.0,0.0))
    return camera.render(sphereWorld)
}

fun main() {
    val renderPlaneWithSphere: Canvas = renderPlaneWithSphere()
    renderPlaneWithSphere.savePPM("src/main/resources/sphereWithPlane_2.ppm")
}