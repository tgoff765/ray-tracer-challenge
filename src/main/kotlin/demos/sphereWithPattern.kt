package demos

import base.core.*
import base.patterns.GradientPattern
import base.shapes.Plane
import rotateYAxisTransformation
import scalingTransformation
import translationTransformation
import viewTransformation
import kotlin.math.PI
import base.shapes.Sphere

fun renderSphereWithPattern(): Canvas {
    // create Sphere at origin
    val patternSphere: Sphere = Sphere()
    patternSphere.transform = scalingTransformation(2.0,2.0,2.0) *
            translationTransformation(0.0,2.0,3.0) *
            rotateYAxisTransformation(PI/4)
    patternSphere.material = Material()
    patternSphere.material.pattern = GradientPattern(Color(1.0, 0.0, 0.0),
                                                   Color(0.0, 0.0, 1.0),
                                                   scalingTransformation(0.15,0.15,0.15))
    patternSphere.material.specular = 0.0

    // create plain floor
    val floor: Plane = Plane()
    floor.transform = scalingTransformation(10.0,10.0,10.0)
    floor.material = Material()
    floor.material.color = Color(1.0, 0.9, 0.9)
    floor.material.specular = 0.0

    // create world
    val sphereWorld: World = World()
    sphereWorld.objects.add(floor)
    sphereWorld.objects.add(patternSphere)
    sphereWorld.light = Light(point(-10.0, 10.0,-10.0), Color(1.0, 1.0, 1.0))
    val camera: Camera = Camera(500, 500, PI /3)
    camera.transform = viewTransformation(point(0.0, 1.5, -5.0),point(0.0,1.0,0.0), vector(0.0,1.0,0.0))
    return camera.render(sphereWorld)
}


fun main() {
    renderSphereWithPattern().savePPM("src/main/resources/sphereWithGradient.ppm")
}