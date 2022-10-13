import base.core.*
import base.shapes.Plane
import kotlin.test.Test
import kotlin.test.assertEquals

class TestPlane {

    @Test
    fun testNormalVector() {
        val plane: Plane = Plane()
        assertEquals(vector(0.0,1.0,0.0),plane.normalAt(point(0.0,0.0,0.0)))
        assertEquals(vector(0.0,1.0,0.0),plane.normalAt(point(10.0,0.0,-10.0)))
        assertEquals(vector(0.0,1.0,0.0),plane.normalAt(point(-5.0,0.0,150.0)))
    }

    @Test
    fun testIntersectRayParallel() {
        val plane: Plane = Plane()
        val r: Ray = Ray(point(0.0,10.0, 0.0), vector(0.0,0.0,1.0))
        val i: List<Intersection> = plane.intersect(r)
        assertEquals(0,i.size)
    }

    @Test
    fun testIntersectCoplanarRay() {
        val plane: Plane = Plane()
        val r: Ray = Ray(point(0.0,0.0, 0.0), vector(0.0,0.0,1.0))
        val i: List<Intersection> = plane.intersect(r)
        assertEquals(0,i.size)
    }

    @Test
    fun testRayIntersectionAbove() {
        val plane: Plane = Plane()
        val r: Ray = Ray(point(0.0,1.0, 0.0), vector(0.0,-1.0,0.0))
        val i: List<Intersection> = plane.intersect(r)
        assertEquals(1,i.size)
        assertEquals(1.0, i[0].t)
        assertEquals(plane, i[0].s)
    }

    @Test
    fun testRayIntersectionBelow() {
        val plane: Plane = Plane()
        val r: Ray = Ray(point(0.0,-1.0, 0.0), vector(0.0,1.0,0.0))
        val i: List<Intersection> = plane.intersect(r)
        assertEquals(1,i.size)
        assertEquals(1.0, i[0].t)
        assertEquals(plane, i[0].s)
    }

}