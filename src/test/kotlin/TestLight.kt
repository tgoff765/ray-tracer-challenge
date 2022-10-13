import base.core.*
import kotlin.test.*

class TestLight {

    @Test
    fun testLight() {
        val testLight: Light = Light(point(0.0, 0.0,0.0), Color(1.0, 1.0, 1.0))
        assertEquals(testLight.position, point(0.0, 0.0,0.0))
        assertEquals(testLight.intensity, Color(1.0, 1.0, 1.0))
    }
}