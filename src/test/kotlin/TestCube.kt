import base.core.*
import kotlin.test.Test
import kotlin.test.assertEquals
import base.shapes.Cube

class TestCube {

    @Test
    fun testRayIntersectionWithCube() {
        val c: Cube = Cube()
        // 7 intersection tests, one for each face of cube + from inside cube
        // +x direction
        val r1: Ray = Ray(point(5.0, 0.5, 0.0), vector(-1.0,0.0,0.0))
        val xs1: List<Intersection> = c.intersect(r1)
        assertEquals(xs1.size, 2)
        assertEquals(xs1[0].t, 4.0)
        assertEquals(xs1[1].t, 6.0)
        // -x direction
        val r2: Ray = Ray(point(-5.0, 0.5, 0.0), vector(1.0,0.0,0.0))
        val xs2: List<Intersection> = c.intersect(r2)
        assertEquals(xs2.size, 2)
        assertEquals(xs2[0].t, 4.0)
        assertEquals(xs2[1].t, 6.0)
        // +y direction
        val r3: Ray = Ray(point(0.5, 5.0, 0.0), vector(0.0,-1.0,0.0))
        val xs3: List<Intersection> = c.intersect(r3)
        assertEquals(xs3.size, 2)
        assertEquals(xs3[0].t, 4.0)
        assertEquals(xs3[1].t, 6.0)
        // -y direction
        val r4: Ray = Ray(point(0.5, -5.0, 0.0), vector(0.0,1.0,0.0))
        val xs4: List<Intersection> = c.intersect(r4)
        assertEquals(xs4.size, 2)
        assertEquals(xs4[0].t, 4.0)
        assertEquals(xs4[1].t, 6.0)
        // +z direction
        val r5: Ray = Ray(point(0.5, 0.0, 5.0), vector(0.0,0.0,-1.0))
        val xs5: List<Intersection> = c.intersect(r5)
        assertEquals(xs5.size, 2)
        assertEquals(xs5[0].t, 4.0)
        assertEquals(xs5[1].t, 6.0)
        // -z direction
        val r6: Ray = Ray(point(0.5, 0.0, -5.0), vector(0.0,0.0,1.0))
        val xs6: List<Intersection> = c.intersect(r6)
        assertEquals(xs6.size, 2)
        assertEquals(xs6[0].t, 4.0)
        assertEquals(xs6[1].t, 6.0)
        // inside
        val r7: Ray = Ray(point(0.0, 0.5, 0.0), vector(0.0,0.0,1.0))
        val xs7: List<Intersection> = c.intersect(r7)
        assertEquals(xs7.size, 2)
        assertEquals(xs7[0].t, -1.0)
        assertEquals(xs7[1].t, 1.0)
    }

    @Test
    fun testRayMissWithCube() {
        val c: Cube = Cube()
        // 6 intersection tests, all misses
        val r1: Ray = Ray(point(-2.0, 0.0, 0.0), vector(0.2673,0.5345,0.8018))
        val xs1: List<Intersection> = c.intersect(r1)
        assertEquals(xs1.size, 0)
        val r2: Ray = Ray(point(0.0, -2.0, 0.0), vector(0.8018,0.2673,0.5345))
        val xs2: List<Intersection> = c.intersect(r2)
        assertEquals(xs2.size, 0)
        val r3: Ray = Ray(point(0.0, 0.0, -2.0), vector(0.5345,0.8018,0.2673))
        val xs3: List<Intersection> = c.intersect(r3)
        assertEquals(xs3.size, 0)

        val r4: Ray = Ray(point(2.0, 0.0, 2.0), vector(0.0,0.0,-1.0))
        val xs4: List<Intersection> = c.intersect(r4)
        assertEquals(xs4.size, 0)

        val r5: Ray = Ray(point(0.0, 2.0, 2.0), vector(0.0,-1.0,0.0))
        val xs5: List<Intersection> = c.intersect(r5)
        assertEquals(xs5.size, 0)

        val r6: Ray = Ray(point(2.0, 2.0, 0.0), vector(-1.0,0.0,0.0))
        val xs6: List<Intersection> = c.intersect(r6)
        assertEquals(xs6.size, 0)
    }

    @Test
    fun testNormalOnCube() {
        val c: Cube = Cube()
        // 8 different normal vector tests
        val p1: Tuple = point(1.0, 0.5, -0.8)
        val n1: Tuple = c.normalAt(p1)
        assertEquals(vector(1.0,0.0,0.0), n1)

        val p2: Tuple = point(-1.0, -0.2, 0.9)
        val n2: Tuple = c.normalAt(p2)
        assertEquals(vector(-1.0,0.0,0.0), n2)

        val p3: Tuple = point(-0.4, 1.0, -0.1)
        val n3: Tuple = c.normalAt(p3)
        assertEquals(vector(0.0,1.0,0.0), n3)

        val p4: Tuple = point(0.3, -1.0, -0.7)
        val n4: Tuple = c.normalAt(p4)
        assertEquals(vector(0.0,-1.0,0.0), n4)

        val p5: Tuple = point(-0.6, 0.3, 1.0)
        val n5: Tuple = c.normalAt(p5)
        assertEquals(vector(0.0,0.0,1.0), n5)

        val p6: Tuple = point(0.4, 0.4, -1.0)
        val n6: Tuple = c.normalAt(p6)
        assertEquals(vector(0.0,0.0,-1.0), n6)

        val p7: Tuple = point(1.0, 1.0, 1.0)
        val n7: Tuple = c.normalAt(p7)
        assertEquals(vector(1.0,0.0,0.0), n7)

        val p8: Tuple = point(-1.0, -1.0, -1.0)
        val n8: Tuple = c.normalAt(p8)
        assertEquals(vector(-1.0,0.0,0.0), n8)

    }
}