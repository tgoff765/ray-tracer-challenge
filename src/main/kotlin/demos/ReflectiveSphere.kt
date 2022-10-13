package demos

import base.core.*
import base.shapes.Plane
import base.shapes.Shape
import base.shapes.Sphere
import rotateXAxisTransformation
import rotateYAxisTransformation
import scalingTransformation
import translationTransformation
import viewTransformation
import kotlin.math.PI

fun createReflectiveSphere(): Canvas {

    // Step 1: Create floor as flattened sphere with matte texture
    val floor: Sphere = Sphere()
    floor.transform = scalingTransformation(10.0,0.01,10.0)
    floor.material = Material()
    floor.material.color = Color(1.0, 0.9, 0.9)
    floor.material.specular = 0.0
    floor.material.reflective = 0.0

    // Step 2: Create left wall
    val leftWall: Sphere = Sphere()
    // note how transformations are implemented in reverse order!
    leftWall.transform = translationTransformation(0.0,0.0,5.0) *
            rotateYAxisTransformation(-PI/4) *
            rotateXAxisTransformation(PI/2) *
            scalingTransformation(10.0,0.01,10.0)
    leftWall.material = floor.material

    // Step 3: Create right wall
    // same as left wall but rotate about Y axis in opposite direction
    val rightWall: Sphere = Sphere()
    rightWall.transform = translationTransformation(0.0,0.0,5.0) *
            rotateYAxisTransformation(PI/4) *
            rotateXAxisTransformation(PI/2) *
            scalingTransformation(10.0,0.01,10.0)
    rightWall.material = floor.material

    // Step 4: Create middle sphere
    val middle: Sphere = Sphere()
    middle.transform = translationTransformation(-0.5, 1.0, 0.5)
    middle.material = Material()
    middle.material.color = Color(0.1, 1.0, 0.5)
    middle.material.diffuse = 0.7
    middle.material.specular = 0.3
    middle.material.reflective = 0.8

    // Step 5: Create middle right sphere
    val right: Sphere = Sphere()
    right.transform = translationTransformation(1.5, 0.5, -0.5) * scalingTransformation(0.5, 0.5, 0.5)
    right.material = Material()
    right.material.color = Color(0.5, 1.0, 0.1)
    right.material.diffuse = 0.7
    right.material.specular = 0.3
    right.material.reflective = 0.6

    // Step 6: Create middle left sphere
    val left: Sphere = Sphere()
    left.transform = translationTransformation(-1.5, 0.33, -0.75) * scalingTransformation(0.33, 0.33, 0.33)
    left.material = Material()
    left.material.color = Color(1.0, 0.8, 0.1)
    left.material.diffuse = 0.7
    left.material.specular = 0.3
    left.material.reflective = 0.5

    // Step 7: Configure camera, light, and world
    val spheres: List<Sphere>  = mutableListOf(leftWall, rightWall, floor, left, right, middle)

    val sphereWorld: World = World()
    sphereWorld.objects.addAll(spheres)

    sphereWorld.light = Light(point(-10.0, 10.0,-10.0), Color(1.0, 1.0, 1.0))

    val camera: Camera = Camera(300, 300, PI/3)
    camera.transform = viewTransformation(point(0.0, 1.5, -5.0),point(0.0,1.0,0.0), vector(0.0,1.0,0.0))
    return camera.render(sphereWorld)
}



fun main() {
    val image: Canvas = createReflectiveSphere()
    image.savePPM("src/main/resources/sphereWithReflection.ppm")
}