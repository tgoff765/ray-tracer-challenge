package demos

import base.core.*
import base.patterns.CheckerPattern
import base.shapes.Cube
import base.shapes.Plane
import rotateXAxisTransformation
import rotateYAxisTransformation
import scalingTransformation
import translationTransformation
import viewTransformation
import kotlin.math.PI

fun renderCubes(): Canvas {

    // create plain floor
    val floor: Plane = Plane()
    floor.transform = scalingTransformation(10.0,10.0,10.0)
    floor.material = Material()
    floor.material.specular = 0.3
    floor.material.reflective = 0.6
    floor.material.pattern = CheckerPattern(Color(0.0, 0.0, 0.0), Color(1.0, 1.0, 1.0))

    // left Cube
    val left: Cube = Cube()
    left.transform = rotateXAxisTransformation(PI/3) * scalingTransformation(1.0,10.0,1.0) * translationTransformation(-1.0, 0.0,1.0)
    left.material = Material()
    left.material.color = Color(0.3, 0.0, 0.6)
    left.material.reflective = 0.05


    // create world
    val sphereWorld: World = World()
    sphereWorld.objects.add(floor)
    sphereWorld.objects.add(left)
    sphereWorld.light = Light(point(-10.0, 10.0,-10.0), Color(1.0, 1.0, 1.0))
    val camera: Camera = Camera(300, 300, PI/3)
    camera.transform = viewTransformation(point(0.0, 1.5, -5.0),point(0.0,1.0,0.0), vector(0.0,1.0,0.0))
    return camera.render(sphereWorld)
}

fun main() {
    renderCubes().savePPM("src/main/resources/cubes.ppm")
}