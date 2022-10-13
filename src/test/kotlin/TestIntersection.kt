
import base.core.*
import base.shapes.Plane
import base.shapes.Shape
import base.shapes.Sphere
import base.shapes.glass_sphere
import kotlin.math.sqrt
import kotlin.test.*

class TestIntersection {

    @Test
    fun testIntersection() {
        val testSphere: Sphere = Sphere()
        val testIntersection: Intersection = Intersection(3.5, testSphere)
        assertTrue(testIntersection.t.equalsDelta(3.5))
        assertEquals(testIntersection.s, testSphere)
    }

    @Test
    fun testIntersectionAggregation() {
        val testSphere: Sphere = Sphere()
        val testIntersection1: Intersection = Intersection(2.0, testSphere)
        val testIntersection2: Intersection = Intersection(1.0, testSphere)
        val xs: List<Intersection> = intersections(testIntersection1, testIntersection2)
        assertTrue(xs[0].t.equalsDelta(1.0))
        assertTrue(xs[1].t.equalsDelta(2.0))
    }

    @Test
    fun testIntersectionsWhenAllPositive() {
        val testSphere: Sphere = Sphere()
        val testIntersection1: Intersection = Intersection(2.0, testSphere)
        val testIntersection2: Intersection = Intersection(1.0, testSphere)
        val xs: List<Intersection> = intersections(testIntersection1, testIntersection2)
        assertEquals(hit(xs), testIntersection2)
    }

    @Test
    fun testIntersectionsWhenSomeNegative() {
        val testSphere: Sphere = Sphere()
        val testIntersection1: Intersection = Intersection(-2.0, testSphere)
        val testIntersection2: Intersection = Intersection(1.0, testSphere)
        val xs: List<Intersection> = intersections(testIntersection1, testIntersection2)
        assertEquals(hit(xs), testIntersection2)
    }

    @Test
    fun testIntersectionsWhenAllNegative() {
        val testSphere: Sphere = Sphere()
        val testIntersection1: Intersection = Intersection(-2.0, testSphere)
        val testIntersection2: Intersection = Intersection(-1.0, testSphere)
        val xs: List<Intersection> = intersections(testIntersection1, testIntersection2)
        assertNull(hit(xs))
    }

    @Test
    fun testIntersectionsRandomOrder() {
        val testSphere: Sphere = Sphere()
        val testIntersection1: Intersection = Intersection(5.0, testSphere)
        val testIntersection2: Intersection = Intersection(7.0, testSphere)
        val testIntersection3: Intersection = Intersection(-3.0, testSphere)
        val testIntersection4: Intersection = Intersection(2.0, testSphere)
        val xs: List<Intersection> = intersections(testIntersection1, testIntersection2, testIntersection3, testIntersection4)
        assertEquals(hit(xs), testIntersection4)
    }

    @Test
    fun testPreComputations() {
        val r: Ray = Ray(point(0.0, 0.0, -5.0), vector(0.0,0.0,1.0))
        val shape: Sphere = Sphere()
        val i: Intersection = Intersection(4.0, shape)
        val comps: Comps = prepareComputations(i, r)
        assertTrue(comps.t.equalsDelta(i.t))
        assertEquals(comps.obj, i.s)
        assertEquals(comps.p, point(0.0,0.0,-1.0))
        assertEquals(comps.eyeV, vector(0.0,0.0,-1.0))
        assertEquals(comps.normalV, vector(0.0,0.0,-1.0))
    }

    @Test
    fun testIntersectionOnOutside() {
        val r: Ray = Ray(point(0.0, 0.0, -5.0), vector(0.0, 0.0, 1.0))
        val s: Sphere = Sphere()
        val i: Intersection = Intersection(4.0, s)
        val comps: Comps = prepareComputations(i, r)
        assertFalse(comps.inside)
    }

    @Test
    fun testIntersectionOnInside() {
        val r: Ray = Ray(point(0.0, 0.0, 0.0), vector(0.0, 0.0, 1.0))
        val s: Sphere = Sphere()
        val i: Intersection = Intersection(1.0, s)
        val comps: Comps = prepareComputations(i, r)
        assertTrue(comps.inside)
        assertEquals(comps.p, point(0.0, 0.0, 1.0))
        assertEquals(comps.eyeV, vector(0.0, 0.0, -1.0))
        assertEquals(comps.normalV, vector(0.0, 0.0, -1.0))
    }

    @Test
    fun testHitOffsetPoint() {
        val r: Ray = Ray(point(0.0,0.0,-5.0), vector(0.0,0.0,1.0))
        val s: Shape = Sphere()
        s.transform = translationTransformation(0.0,0.0,1.0)
        val i: Intersection = Intersection(5.0, s)
        val comps: Comps = prepareComputations(i, r)
        assertTrue(comps.overPoint.z < - EPSILON/2.0)
        assertTrue(comps.p.z > comps.overPoint.z)
    }

    @Test
    fun testPrecomputingReflectionVector() {
        val s: Shape = Plane()
        val r: Ray = Ray(point(0.0,1.0,-1.0), vector(0.0, -sqrt(2.0)/2.0, sqrt(2.0)/2.0))
        val i: Intersection = Intersection(sqrt(2.0), s)
        val comps: Comps = prepareComputations(i, r)
        assertEquals(vector(0.0, sqrt(2.0)/2.0, sqrt(2.0)/2.0), comps.reflectV)
    }

    @Test
    fun testRefractiveIntersections() {
        val A: Sphere = glass_sphere()
        val B: Sphere = glass_sphere()
        val C: Sphere = glass_sphere()
        A.transform = scalingTransformation(2.0, 2.0, 2.0)
        A.material.refractiveIndex = 1.5
        B.transform = translationTransformation(0.0, 0.0, -0.25)
        B.material.refractiveIndex = 2.0
        C.transform = translationTransformation(0.0, 0.0, 0.25)
        C.material.refractiveIndex = 2.5
        val r: Ray = Ray(point(0.0,0.0,-4.0), vector(0.0,0.0,1.0))
        val xs: List<Intersection> = intersections(Intersection(2.0, A),
                                                    Intersection(2.75, B),
                                                    Intersection(3.25, C),
                                                    Intersection(4.75, B),
                                                    Intersection(5.25, C),
                                                    Intersection(6.0, A))
        val comps0: Comps = prepareComputations(xs[0], r, xs)
        val comps1: Comps = prepareComputations(xs[1], r, xs)
        val comps2: Comps = prepareComputations(xs[2], r, xs)
        val comps3: Comps = prepareComputations(xs[3], r, xs)
        val comps4: Comps = prepareComputations(xs[4], r, xs)
        val comps5: Comps = prepareComputations(xs[5], r, xs)

        assertEquals(comps0.n1, 1.0)
        assertEquals(comps0.n2, 1.5)
        assertEquals(comps1.n1, 1.5)
        assertEquals(comps1.n2, 2.0)
        assertEquals(comps2.n1, 2.0)
        assertEquals(comps2.n2, 2.5)
        assertEquals(comps3.n1, 2.5)
        assertEquals(comps3.n2, 2.5)
        assertEquals(comps4.n1, 2.5)
        assertEquals(comps4.n2, 1.5)
        assertEquals(comps5.n1, 1.5)
        assertEquals(comps5.n2, 1.0)
    }

    @Test
    fun testUnderPoint() {
        val r: Ray = Ray(point(0.0,0.0,-5.0), vector(0.0,0.0,1.0))
        val s: Shape = glass_sphere()
        s.transform = translationTransformation(0.0,0.0,1.0)
        val i: Intersection = Intersection(5.0, s)
        val xs: List<Intersection> = intersections(i)
        val comps: Comps = prepareComputations(i, r, xs)
        assertTrue(comps.underPoint.z > EPSILON/2.0 )
        assertTrue(comps.p.z < comps.underPoint.z)
    }

    @Test
    fun testSchlickUnderTotalInternalReflection() {
        val s: Shape = glass_sphere()
        val r: Ray = Ray(point(0.0,0.0, sqrt(2.0)/2.0), vector(0.0,1.0,0.0))
        val xs: List<Intersection> = intersections(Intersection(-sqrt(2.0)/2.0, s),
                                                    Intersection(sqrt(2.0)/2.0, s))
        val comps: Comps = prepareComputations(xs[1], r, xs)
        val reflectance: Double = schlick(comps)
        assertEquals(1.0, reflectance)
    }

    @Test
    fun testSchlickWithPerpendicularAngle() {
        val s: Shape = glass_sphere()
        val r: Ray = Ray(point(0.0,0.0,0.0), vector(0.0,1.0,0.0))
        val xs: List<Intersection> = intersections(Intersection(-1.0, s),Intersection(1.0, s))
        val comps: Comps = prepareComputations(xs[1], r, xs)
        val reflectance: Double = schlick(comps)
        assertTrue(0.04.equalsDelta(reflectance))
    }

    @Test
    fun testSchlickWithSmallAngle() {
        val s: Shape = glass_sphere()
        val r: Ray = Ray(point(0.0,0.99,-2.0), vector(0.0,0.0,1.0))
        val xs: List<Intersection> = intersections(Intersection(1.8589, s))
        val comps: Comps = prepareComputations(xs[0], r, xs)
        val reflectance: Double = schlick(comps)
        assertTrue(0.48873.equalsDelta(reflectance))
    }
}