import base.core.*
import base.patterns.Pattern
import base.patterns.RingPattern
import base.patterns.StripePattern
import kotlin.test.Test
import kotlin.test.assertEquals
import base.shapes.Sphere

class TestRingPattern {
    @Test
    fun testRingPattern() {
        val p: Pattern = RingPattern(WHITE, BLACK)
        assertEquals(WHITE, p.colorAt(point(0.0,0.0,0.0)))
        assertEquals(BLACK, p.colorAt(point(1.0,0.0,0.0)))
        assertEquals(BLACK, p.colorAt(point(0.0,0.0,1.0)))
        assertEquals(BLACK, p.colorAt(point(0.708,0.0,0.708)))
    }
}