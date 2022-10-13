package base.shapes

import base.core.*

interface Shape {
    var transform: Matrix
    var material: Material
    fun intersect(r: Ray): List<Intersection>
    fun normalAt(p: Tuple): Tuple
}