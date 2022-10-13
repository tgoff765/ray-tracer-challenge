import base.core.*
import kotlin.math.PI
import kotlin.math.sqrt
import kotlin.test.Test
import kotlin.test.assertEquals

class TestCamera {

    @Test
    fun testDefaultCameraConstruction() {
        val testCamera: Camera = Camera(160, 120, PI/2)
        assertEquals(testCamera.hSize, 160)
        assertEquals(testCamera.vSize, 120)
        assertEquals(testCamera.fieldOfView, PI/2)
        assertEquals(testCamera.transform, identityMatrix4x4())
    }

    @Test
    fun testPixelSizeForHorizontalCanvas() {
        val testCamera: Camera = Camera(200, 125, PI/2)
        testCamera.pixelSize.equalsDelta(0.01)
    }

    @Test
    fun testPixelSizeForVerticalCanvas() {
        val testCamera: Camera = Camera(125, 200, PI/2)
        testCamera.pixelSize.equalsDelta(0.01)
    }

    @Test
    fun testConstructRayThroughCenter() {
        val testCamera: Camera = Camera(201, 101, PI/2)
        val r: Ray = testCamera.rayForPixel(100, 50)
        assertEquals(r.origin, point(0.0,0.0,0.0))
        assertEquals(r.direction, vector(0.0,0.0,-1.0))
    }

    @Test
    fun testConstructRayThroughCenter2() {
        val testCamera: Camera = Camera(201, 101, PI/2)
        val r: Ray = testCamera.rayForPixel(0, 0)
        assertEquals(r.origin, point(0.0,0.0,0.0))
        assertEquals(r.direction, vector(0.66519,0.33259,-0.66851))
    }

    @Test
    fun testConstructRayThroughTransformedCamera() {
        val testCamera: Camera = Camera(201, 101, PI/2)
        testCamera.transform = rotateYAxisTransformation(PI/4) * translationTransformation(0.0, -2.0, 5.0)
        val r: Ray = testCamera.rayForPixel(100, 50)
        assertEquals(r.origin, point(0.0,2.0,-5.0))
        assertEquals(r.direction, vector(sqrt(2.0)/2,0.0,-sqrt(2.0)/2))
    }

    @Test
    fun testRenderWorld() {
        val w: World = defaultWorld()
        val c: Camera = Camera(11, 11, PI/2)
        val from: Tuple = point(0.0,0.0,-5.0)
        val to: Tuple = point(0.0,0.0,0.0)
        val up: Tuple = vector(0.0,1.0,0.0)
        c.transform = viewTransformation(from, to, up)
        val image: Canvas = c.render(w)
        assertEquals(image.getPixel(5,5), Color(0.38066, 0.47583, 0.2855))
    }
}