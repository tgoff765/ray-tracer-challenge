import base.core.*
import kotlin.test.Test
import kotlin.test.assertTrue


class TestColor {

    @Test
    fun testCreateColor() {
        val testColor: Color = Color(-0.5, 0.4, 1.7)
        assertTrue(testColor.red.equalsDelta(-0.5))
        assertTrue(testColor.green.equalsDelta(0.4))
        assertTrue(testColor.blue.equalsDelta(1.7))
    }

    @Test
    fun testColorAddition() {
        val testColor1: Color = Color(0.9, 0.6, 0.75)
        val testColor2: Color = Color(0.7, 0.1, 0.25)
        val combinedColor: Color = testColor1 + testColor2
        assertTrue(combinedColor.red.equalsDelta(1.6))
        assertTrue(combinedColor.green.equalsDelta(0.7))
        assertTrue(combinedColor.blue.equalsDelta(1.0))
    }

    @Test
    fun testColorSubtraction() {
        val testColor1: Color = Color(0.9, 0.6, 0.75)
        val testColor2: Color = Color(0.7, 0.1, 0.25)
        val combinedColor: Color = testColor1 - testColor2
        assertTrue(combinedColor.red.equalsDelta(0.2))
        assertTrue(combinedColor.green.equalsDelta(0.5))
        assertTrue(combinedColor.blue.equalsDelta(0.5))

    }

    @Test
    fun testColorScalarMultiplication() {
        val scaledColor: Color = Color(0.2, 0.3, 0.4) * 2.0
        assertTrue(scaledColor.red.equalsDelta(0.4))
        assertTrue(scaledColor.green.equalsDelta(0.6))
        assertTrue(scaledColor.blue.equalsDelta(0.8))
    }

    @Test
    fun testColorMultiplication() {
        val mixedColor: Color = Color(1.0, 0.2, 0.4) * Color(0.9, 1.0, 0.1)
        assertTrue(mixedColor.red.equalsDelta(0.9))
        assertTrue(mixedColor.green.equalsDelta(0.2))
        assertTrue(mixedColor.blue.equalsDelta(0.04))
    }

}