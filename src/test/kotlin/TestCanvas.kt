import base.core.*
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class TestCanvas {

    @Test
    fun testCreateCanvas() {
        val testCanvas: Canvas = Canvas(10, 20)
        assertEquals(testCanvas.width, 10)
        assertEquals(testCanvas.height, 20)
        assertTrue(testCanvas.colors.flatten().all { it == Color(0.0,0.0,0.0) })
    }

    @Test
    fun testWriteToCanvas() {
        val testCanvas: Canvas = Canvas(10, 20)
        testCanvas.writePixel(2,3, Color(1.0,0.0,0.0))
        assertEquals(testCanvas.getPixel(2,3), Color(1.0, 0.0,0.0))

    }
}