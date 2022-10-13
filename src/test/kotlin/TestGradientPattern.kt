import base.core.*
import base.patterns.GradientPattern
import base.patterns.Pattern
import kotlin.test.Test
import kotlin.test.assertEquals


class TestGradientPattern {

    @Test
    fun testGradientPattern() {
        val p: Pattern = GradientPattern(WHITE,BLACK)
        assertEquals(WHITE, p.colorAt(point(0.0,0.0,0.0)))
        assertEquals(Color(0.75, 0.75, 0.75), p.colorAt(point(0.25,0.0,0.0)))
        assertEquals(Color(0.5, 0.5,0.5), p.colorAt(point(0.5,0.0,0.0)))
        assertEquals(Color(0.25, 0.25, 0.25), p.colorAt(point(0.75,0.0,0.0)))
    }
}