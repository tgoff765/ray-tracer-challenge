import base.core.*
import base.patterns.StripePattern
import kotlin.math.sqrt
import kotlin.test.*
import base.shapes.Sphere

class TestMaterial {

    @Test
    fun testDefaultMaterial() {
        val m: Material = Material()
        assertEquals(m.color, Color(1.0,1.0,1.0))
        assertEquals(m.ambient, 0.1)
        assertEquals(m.diffuse, 0.9)
        assertEquals(m.specular, 0.9)
        assertEquals(m.shininess, 200.0)
    }

    @Test
    fun testLightingBetweenLightAndSurface() {
        val s: Sphere = Sphere()
        val m: Material = Material()
        val p: Tuple = point(0.0, 0.0, 0.0)
        val eyeV: Tuple = vector(0.0, 0.0, -1.0)
        val normalV: Tuple = vector(0.0, 0.0, -1.0)
        val light: Light = Light(point(0.0, 0.0, -10.0), Color(1.0, 1.0, 1.0))

        assertEquals(m.lighting(light, s, p, eyeV, normalV), Color(1.9, 1.9, 1.9))
    }

    @Test
    fun testEyeAt45Angle() {
        val s: Sphere = Sphere()
        val m: Material = Material()
        val p: Tuple = point(0.0, 0.0, 0.0)
        val eyeV: Tuple = vector(0.0, sqrt(2.0)/2.0, sqrt(2.0)/2.0)
        val normalV: Tuple = vector(0.0, 0.0, -1.0)
        val light: Light = Light(point(0.0, 0.0, -10.0), Color(1.0, 1.0, 1.0))

        assertEquals(m.lighting(light, s, p, eyeV, normalV), Color(1.0, 1.0, 1.0))
    }

    @Test
    fun testLightingAt45Angle() {
        val s: Sphere = Sphere()
        val m: Material = Material()
        val p: Tuple = point(0.0, 0.0, 0.0)
        val eyeV: Tuple = vector(0.0, 0.0, -1.0)
        val normalV: Tuple = vector(0.0, 0.0, -1.0)
        val light: Light = Light(point(0.0, 10.0, -10.0), Color(1.0, 1.0, 1.0))

        assertEquals(m.lighting(light, s, p, eyeV, normalV), Color(0.7364, 0.7364, 0.7364))
    }

    @Test
    fun testEyeInReflectionVector() {
        val s: Sphere = Sphere()
        val m: Material = Material()
        val p: Tuple = point(0.0, 0.0, 0.0)
        val eyeV: Tuple = vector(0.0, -sqrt(2.0)/2.0, -sqrt(2.0)/2.0)
        val normalV: Tuple = vector(0.0, 0.0, -1.0)
        val light: Light = Light(point(0.0, 10.0, -10.0), Color(1.0, 1.0, 1.0))

        assertEquals(m.lighting(light, s, p, eyeV, normalV), Color(1.6364, 1.6364, 1.6364))
    }

    @Test
    fun testLightHidden() {
        val s: Sphere = Sphere()
        val m: Material = Material()
        val p: Tuple = point(0.0, 0.0, 0.0)
        val eyeV: Tuple = vector(0.0, 0.0, -1.0)
        val normalV: Tuple = vector(0.0, 0.0, -1.0)
        val light: Light = Light(point(0.0, 0.0, 10.0), Color(1.0, 1.0, 1.0))

        assertEquals(m.lighting(light,s, p, eyeV, normalV), Color(0.1, 0.1, 0.1))
    }

    @Test
    fun testLightWithShadow() {
        val s: Sphere = Sphere()
        val m: Material = Material()
        val p: Tuple = point(0.0, 0.0, 0.0)
        val eyeVector: Tuple = vector(0.0, 0.0, -1.0)
        val normalVector: Tuple = vector(0.0, 0.0, -1.0)
        val light: Light = Light(point(0.0, 0.0,-10.0), Color(1.0, 1.0, 1.0))
        val inShadow: Boolean = true
        assertEquals(m.lighting(light, s, p, eyeVector, normalVector, inShadow), Color(0.1, 0.1, 0.1))
    }

    @Test
    fun testLightingWithPattern() {
        val s: Sphere = Sphere()
        val m: Material = Material()
        m.pattern = StripePattern(WHITE, BLACK)
        m.ambient = 1.0
        m.diffuse = 0.0
        m.specular = 0.0
        val eyeVector: Tuple = vector(0.0, 0.0, -1.0)
        val normalVector: Tuple = vector(0.0, 0.0, -1.0)
        val light: Light = Light(point(0.0, 0.0,-10.0), Color(1.0, 1.0, 1.0))
        val c1: Color = m.lighting(light, s, point(0.9,0.0,0.0), eyeVector, normalVector, false)
        val c2: Color = m.lighting(light, s,  point(1.1,0.0,0.0), eyeVector, normalVector, false)
        assertEquals(WHITE, c1)
        assertEquals(BLACK, c2)
    }

    @Test
    fun testReflectivityOfDefault() {
        val m: Material = Material()
        assertEquals(0.0, m.reflective)
    }

    @Test
    fun testRefractiveOfDefault() {
        val m: Material = Material()
        assertEquals(m.transparency, 0.0)
        assertEquals(m.refractiveIndex, 1.0)
    }

}