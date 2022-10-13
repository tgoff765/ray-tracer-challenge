import base.core.*
import base.patterns.CheckerPattern
import base.patterns.GradientPattern
import base.patterns.Pattern
import kotlin.test.Test
import kotlin.test.assertEquals


class TestCheckerPattern {

    @Test
    fun testCheckersShouldRepeatX() {
        val p: Pattern = CheckerPattern(WHITE, BLACK)
        assertEquals(WHITE, p.colorAt(point(0.0,0.0,0.0)))
        assertEquals(WHITE, p.colorAt(point(0.99,0.0,0.0)))
        assertEquals(BLACK, p.colorAt(point(1.01,0.0,0.0)))
    }

    @Test
    fun testCheckersShouldRepeatY() {
        val p: Pattern = CheckerPattern(WHITE, BLACK)
        assertEquals(WHITE, p.colorAt(point(0.0,0.0,0.0)))
        assertEquals(WHITE, p.colorAt(point(0.0,0.99,0.0)))
        assertEquals(BLACK, p.colorAt(point(0.00,1.01,0.0)))
    }

    @Test
    fun testCheckersShouldRepeatZ() {
        val p: Pattern = CheckerPattern(WHITE, BLACK)
        assertEquals(WHITE, p.colorAt(point(0.0,0.0,0.0)))
        assertEquals(WHITE, p.colorAt(point(0.0,0.0,0.99)))
        assertEquals(BLACK, p.colorAt(point(0.00,0.0,1.01)))
    }
}