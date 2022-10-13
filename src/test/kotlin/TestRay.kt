
import base.core.*
import kotlin.test.Test
import kotlin.test.assertEquals

class TestRay {

    @Test
    fun testRayCreation() {
        val testRay: Ray = Ray(point(1.0, 2.0, 3.0), vector(4.0,5.0,6.0))
        assertEquals(testRay.origin, point(1.0, 2.0, 3.0))
        assertEquals(testRay.direction, vector(4.0,5.0,6.0))
    }

    @Test
    fun testRayOutput() {
        val testRay: Ray = Ray(point(2.0, 3.0, 4.0), vector(1.0,0.0,0.0))
        assertEquals(testRay.position(0.0), point(2.0, 3.0, 4.0))
        assertEquals(testRay.position(1.0), point(3.0, 3.0, 4.0))
        assertEquals(testRay.position(-1.0), point(1.0, 3.0, 4.0))
        assertEquals(testRay.position(2.5), point(4.5, 3.0, 4.0))
    }

    @Test
    fun testTranslateRay() {
        val testRay: Ray = Ray(point(1.0, 2.0, 3.0), vector(0.0,1.0,0.0))
        val translationMatrix: Matrix = translationTransformation(3.0, 4.0, 5.0)
        val translatedRay: Ray = transform(testRay, translationMatrix)

        assertEquals(translatedRay.origin, point(4.0, 6.0, 8.0))
        assertEquals(translatedRay.direction, vector(0.0,1.0,0.0))
    }

    @Test
    fun testScaleRay() {
        val testRay: Ray = Ray(point(1.0, 2.0, 3.0), vector(0.0,1.0,0.0))
        val translationMatrix: Matrix = scalingTransformation(2.0, 3.0, 4.0)
        val translatedRay: Ray = transform(testRay, translationMatrix)

        assertEquals(translatedRay.origin, point(2.0, 6.0, 12.0))
        assertEquals(translatedRay.direction, vector(0.0,3.0,0.0))
    }
}