import base.core.*
import kotlin.test.Test
import kotlin.test.assertEquals
import base.shapes.Cylinder
import kotlin.test.assertTrue

class TestCylinder {

    @Test
    fun testRayMiss() {
        val cyl: Cylinder = Cylinder()
        // 3 ray test misses
        val r1: Ray = Ray(point(1.0,0.0,0.0), vector(0.0, 1.0, 0.0).normalize())
        val xs1: List<Intersection> = cyl.intersect(r1)
        assertEquals(0, xs1.size)
        val r2: Ray = Ray(point(0.0,0.0,0.0), vector(0.0, 1.0, 0.0).normalize())
        val xs2: List<Intersection> = cyl.intersect(r2)
        assertEquals(0, xs2.size)
        val r3: Ray = Ray(point(0.0,0.0,-5.0), vector(1.0, 1.0, 1.0).normalize())
        val xs3: List<Intersection> = cyl.intersect(r3)
        assertEquals(0, xs3.size)
    }

    @Test
    fun testRayHit() {
        val cyl: Cylinder = Cylinder()
        // 3 ray test hits
        val r1: Ray = Ray(point(1.0,0.0,-5.0), vector(0.0, 0.0, 1.0).normalize())
        val xs1: List<Intersection> = cyl.intersect(r1)
        assertEquals(2, xs1.size)
        assertEquals(5.0, xs1[0].t)
        assertEquals(5.0, xs1[1].t)
        val r2: Ray = Ray(point(0.0,0.0,-5.0), vector(0.0, 0.0, 1.0).normalize())
        val xs2: List<Intersection> = cyl.intersect(r2)
        assertEquals(2, xs2.size)
        assertEquals(4.0, xs2[0].t)
        assertEquals(6.0, xs2[1].t)
        val r3: Ray = Ray(point(0.5,0.0,-5.0), vector(0.1, 1.0, 1.0).normalize())
        val xs3: List<Intersection> = cyl.intersect(r3)
        assertEquals(2, xs3.size)
        assertTrue(6.80798.equalsDelta(xs3[0].t))
        assertTrue(7.08872.equalsDelta(xs3[1].t))
    }

    @Test
    fun testFindNormal() {
        val cyl: Cylinder = Cylinder()
        // 4 tests for calculating the normal
        assertEquals(vector(1.0,0.0,0.0),cyl.normalAt(point(1.0,0.0,0.0)))
        assertEquals(vector(0.0,0.0,-1.0),cyl.normalAt(point(0.0,5.0,-1.0)))
        assertEquals(vector(0.0,0.0,1.0),cyl.normalAt(point(0.0,-2.0,1.0)))
        assertEquals(vector(-1.0,0.0,0.0),cyl.normalAt(point(-1.0,1.0,0.0)))
    }

    @Test
    fun testDefaultMinAndMax() {
        val cyl: Cylinder = Cylinder()
        assertEquals(cyl.min, Double.NEGATIVE_INFINITY)
        assertEquals(cyl.max, Double.POSITIVE_INFINITY)
    }
}