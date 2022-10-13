import base.core.*
import kotlin.math.PI
import kotlin.math.sqrt
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import base.shapes.Sphere

class TestSphere {


    @Test
    fun testSimpleIntersections() {
        val testRay: Ray = Ray(point(0.0, 0.0, -5.0), vector(0.0, 0.0, 1.0))
        val testSphere: Sphere = Sphere()
        val intersections : List<Intersection> = testSphere.intersect(testRay)
        assertEquals(intersections.size, 2)
        assertTrue(intersections[0].t.equalsDelta(4.0))
        assertTrue(intersections[1].t.equalsDelta(6.0))
    }

    @Test
    fun testTwoIntersectionsAtSamePoint() {
        val testRay: Ray = Ray(point(0.0, 1.0, -5.0), vector(0.0, 0.0, 1.0))
        val testSphere: Sphere = Sphere()
        val intersections : List<Intersection> = testSphere.intersect(testRay)
        assertEquals(intersections.size, 2)
        assertTrue(intersections[0].t.equalsDelta(5.0))
        assertTrue(intersections[1].t.equalsDelta(5.0))
    }

    @Test
    fun testIntersectionMiss() {
        val testRay: Ray = Ray(point(0.0, 2.0, -5.0), vector(0.0, 0.0, 1.0))
        val testSphere: Sphere = Sphere()
        val intersections : List<Intersection> = testSphere.intersect(testRay)
        assertEquals(intersections.size, 0)
    }

    @Test
    fun testIntersectionFromInside() {
        val testRay: Ray = Ray(point(0.0, 0.0, 0.0), vector(0.0, 0.0, 1.0))
        val testSphere: Sphere = Sphere()
        val intersections : List<Intersection> = testSphere.intersect(testRay)
        assertEquals(intersections.size, 2)
        assertTrue(intersections[0].t.equalsDelta(-1.0))
        assertTrue(intersections[1].t.equalsDelta(1.0))
    }

    @Test
    fun testIntersectionFromBehind() {
        val testRay: Ray = Ray(point(0.0, 0.0, 5.0), vector(0.0, 0.0, 1.0))
        val testSphere: Sphere = Sphere()
        val intersections : List<Intersection> = testSphere.intersect(testRay)
        assertEquals(intersections.size, 2)
        assertTrue(intersections[0].t.equalsDelta(-6.0))
        assertTrue(intersections[1].t.equalsDelta(-4.0))
    }

    @Test
    fun testIntersectionReturnsObjects() {
        val testRay: Ray = Ray(point(0.0, 0.0, -5.0), vector(0.0, 0.0, 1.0))
        val testSphere: Sphere = Sphere()
        val intersections : List<Intersection> = testSphere.intersect(testRay)
        assertEquals(intersections.size, 2)
        assertEquals(intersections[0].s,testSphere)
        assertEquals(intersections[1].s,testSphere)
    }

    @Test
    fun testIntersectionWithScaledSphere() {
        val testRay: Ray = Ray(point(0.0, 0.0, -5.0), vector(0.0, 0.0, 1.0))
        val testSphere: Sphere = Sphere()
        testSphere.transform = scalingTransformation(2.0, 2.0, 2.0)
        val intersections : List<Intersection> = testSphere.intersect(testRay)
        assertEquals(intersections.size, 2)
        assertTrue(intersections[0].t.equalsDelta(3.0))
        assertTrue(intersections[1].t.equalsDelta(7.0))
    }


    @Test
    fun testIntersectionWithTranslatedSphere() {
        val testRay: Ray = Ray(point(0.0, 0.0, -5.0), vector(0.0, 0.0, 1.0))
        val testSphere: Sphere = Sphere()
        testSphere.transform = translationTransformation(5.0, 0.0, 0.0)
        val intersections : List<Intersection> = testSphere.intersect(testRay)
        assertEquals(intersections.size, 0)
    }

    @Test
    fun testNormalOnXAxis() {
        val testSphere: Sphere = Sphere()
        val n: Tuple = testSphere.normalAt(point(1.0, 0.0, 0.0))
        assertEquals(n, vector(1.0, 0.0,0.0))
    }

    @Test
    fun testNormalOnYAxis() {
        val testSphere: Sphere = Sphere()
        val n: Tuple = testSphere.normalAt(point(0.0, 1.0, 0.0))
        assertEquals(n, vector(0.0, 1.0,0.0))
    }

    @Test
    fun testNormalOnZAxis() {
        val testSphere: Sphere = Sphere()
        val n: Tuple = testSphere.normalAt(point(0.0, 0.0, 1.0))
        assertEquals(n, vector(0.0, 0.0,1.0))
    }

    @Test
    fun testNormalOnNonAxis() {
        val testSphere: Sphere = Sphere()
        val n: Tuple = testSphere.normalAt(point(sqrt(3.0)/3.0, sqrt(3.0)/3.0, sqrt(3.0)/3.0))
        assertEquals(n, vector(sqrt(3.0)/3.0, sqrt(3.0)/3.0,sqrt(3.0)/3.0))
    }

    @Test
    fun testNormalIsNormalized() {
        val testSphere: Sphere = Sphere()
        val n: Tuple = testSphere.normalAt(point(sqrt(3.0)/3.0, sqrt(3.0)/3.0, sqrt(3.0)/3.0))
        assertEquals(n, n.normalize())
    }

    @Test
    fun testNormalTranslatedSphere() {
        val testSphere: Sphere = Sphere()
        testSphere.transform = translationTransformation(0.0, 1.0, 0.0)
        val n: Tuple = testSphere.normalAt(point(0.0, 1.70711, -0.70711))
        assertEquals(n, vector(0.0, 0.70711,-0.70711))
    }

    @Test
    fun testNormalRotatedSphere() {
        val testSphere: Sphere = Sphere()
        val transformation: Matrix = scalingTransformation(1.0, 0.5, 1.0) * rotateZAxisTransformation(PI/5)
        testSphere.transform = transformation
        val n: Tuple = testSphere.normalAt(point(0.0, sqrt(2.0)/2.0, -sqrt(2.0)/2.0))
        assertEquals(n, vector(0.0, 0.97014,-0.24254))
    }

    @Test
    fun testSphereHasDefault() {
        val testSphere: Sphere = Sphere()
        assertEquals(testSphere.material, Material())
    }

    @Test
    fun testSphereCanBeAssignedMaterial() {
        val testSphere:Sphere = Sphere()
        val testMaterial: Material = Material(ambient = 1.0)
        testSphere.material = testMaterial
        assertEquals(testSphere.material, testMaterial)
    }
}
