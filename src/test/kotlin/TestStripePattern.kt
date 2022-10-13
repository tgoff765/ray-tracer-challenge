
import base.core.*
import base.patterns.StripePattern
import kotlin.test.Test
import kotlin.test.assertEquals
import base.shapes.Sphere

class TestStripePattern {

    @Test
    fun testStripPatternConstantInY() {
        val pattern: StripePattern = StripePattern(WHITE, BLACK)
        assertEquals(WHITE, pattern.colorAt(point(0.0,0.0,0.0)))
        assertEquals(WHITE, pattern.colorAt(point(0.0,1.0,0.0)))
        assertEquals(WHITE, pattern.colorAt(point(0.0,2.0,0.0)))
    }

    @Test
    fun testStripPatternConstantInZ() {
        val pattern: StripePattern = StripePattern(WHITE, BLACK)
        assertEquals(WHITE, pattern.colorAt(point(0.0,0.0,0.0)))
        assertEquals(WHITE, pattern.colorAt(point(0.0,0.0,1.0)))
        assertEquals(WHITE, pattern.colorAt(point(0.0,0.0,2.0)))
    }

    @Test
    fun testStripPatternAlternatesInX() {
        val pattern: StripePattern = StripePattern(WHITE, BLACK)
        assertEquals(WHITE, pattern.colorAt(point(0.0,0.0,0.0)))
        assertEquals(WHITE, pattern.colorAt(point(0.9,0.0,0.0)))
        assertEquals(BLACK, pattern.colorAt(point(1.0,0.0,0.0)))
        assertEquals(BLACK, pattern.colorAt(point(-0.1,0.0,0.0)))
        assertEquals(BLACK, pattern.colorAt(point(-1.0,0.0,0.0)))
        assertEquals(WHITE, pattern.colorAt(point(-1.1,0.0,0.0)))
    }

    @Test
    fun testStripeWithObjectTransformation() {
        val obj: Sphere = Sphere()
        obj.transform = scalingTransformation(2.0,2.0,2.0)
        val pattern: StripePattern = StripePattern(WHITE, BLACK)
        val c: Color = pattern.colorAtObject(obj, point(1.5, 0.0,0.0))
        assertEquals(WHITE, c)
    }

    @Test
    fun testStripeWithPatternTransformation() {
        val obj: Sphere = Sphere()
        val pattern: StripePattern = StripePattern(WHITE, BLACK)
        pattern.patternTransformation = scalingTransformation(2.0,2.0,2.0)
        val c: Color = pattern.colorAtObject(obj, point(1.5, 0.0,0.0))
        assertEquals(WHITE, c)
    }

    @Test
    fun testStripeWithObjectAndPatternTransformation() {
        val obj: Sphere = Sphere()
        obj.transform = scalingTransformation(2.0,2.0,2.0)
        val pattern: StripePattern = StripePattern(WHITE, BLACK)
        pattern.patternTransformation = scalingTransformation(2.0,2.0,2.0)
        val c: Color = pattern.colorAtObject(obj, point(2.5, 0.0,0.0))
        assertEquals(WHITE, c)
    }
}