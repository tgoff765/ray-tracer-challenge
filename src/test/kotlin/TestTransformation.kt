import base.core.*
import kotlin.math.PI
import kotlin.math.sqrt
import kotlin.test.*

class TestTransformation {

    @Test
    fun testTranslation() {
        val testResult: Tuple = translationTransformation(5.0, -3.0, 2.0) * point(-3.0,4.0,5.0)
        assertEquals(testResult, point(2.0, 1.0, 7.0))
    }

    @Test
    fun testInverseTranslation() {
        val testTranslation: Matrix = translationTransformation(5.0, -3.0, 2.0)
        val inverseTestTranslation: Matrix = testTranslation.inverse()
        val testPoint: Tuple = point(-3.0, 4.0, 5.0)
        // multiply by inverse should move us backwards
        assertEquals(inverseTestTranslation * testPoint, point(-8.0, 7.0, 3.0))
    }

    @Test
    fun testTranslationNotEffectVectors() {
        val testResult: Tuple = translationTransformation(5.0, -3.0, 2.0) * vector(-3.0, 4.0, 5.0)
        assertEquals(testResult, vector(-3.0, 4.0, 5.0))
    }

    @Test
    fun testScalingPoint() {
        assertEquals(scalingTransformation(2.0, 3.0, 4.0) * point(-4.0, 6.0, 8.0),
                    point(-8.0, 18.0, 32.0)
        )
    }

    @Test
    fun testScalingVector() {
        assertEquals(scalingTransformation(2.0, 3.0, 4.0) * vector(-4.0, 6.0, 8.0),
                    vector(-8.0, 18.0, 32.0)
        )
    }

    @Test
    fun testInverseScaling() {
        val testScaling: Matrix = scalingTransformation(2.0, 3.0, 4.0)
        val testInverseScaling: Matrix = testScaling.inverse()
        val testVector: Tuple = vector(-4.0, 6.0, 8.0)
        // multiply by inverse should scale by 1/values
        assertEquals(testInverseScaling * testVector, vector(-2.0, 2.0, 2.0))
    }

    @Test
    fun testReflection() {
        // reflection is just scaling by a negative value
        assertEquals(scalingTransformation(-1.0, 1.0, 1.0) * point(2.0, 3.0, 4.0),
            point(-2.0, 3.0, 4.0)
        )
    }

    @Test
    fun testRotateAroundX() {
        // half quarter rotation
        assertEquals(rotateXAxisTransformation(PI/4) * point(0.0, 1.0, 0.0),
                    point(0.0, sqrt(2.0)/2.0, sqrt(2.0)/2.0)
        )
        // full quarter rotation
        assertEquals(rotateXAxisTransformation(PI/2) * point(0.0, 1.0, 0.0),
            point(0.0, 0.0, 1.0)
        )
    }

    @Test
    fun testInverseRotateAroundX() {
        val testPoint: Tuple = point(0.0, 1.0, 0.0)
        val testRotation: Matrix = rotateXAxisTransformation(PI/4)
        val testInverseRotation: Matrix = testRotation.inverse()

        assertEquals(testInverseRotation * testPoint, point(0.0, sqrt(2.0)/2.0, -sqrt(2.0)/2.0))
    }

    @Test
    fun testRotateAroundY() {
        // half quarter
        assertEquals(rotateYAxisTransformation(PI / 4) * point(0.0, 0.0, 1.0),
            point(sqrt(2.0)/2.0, 0.0, sqrt(2.0)/2.0)
        )
        // full quarter
        assertEquals(rotateYAxisTransformation(PI / 2) * point(0.0, 0.0, 1.0),
            point(1.0, 0.0, 0.0)
        )
    }

    @Test
    fun testRotateAroundZ() {
        // half quarter
        assertEquals(rotateZAxisTransformation(PI / 4) * point(0.0, 1.0, 0.0),
            point(-sqrt(2.0)/2.0, sqrt(2.0)/2.0, 0.0)
        )
        // full quarter
        assertEquals(rotateZAxisTransformation(PI / 2) * point(0.0, 1.0, 0.0),
            point(-1.0, 0.0, 0.0)
        )
    }

    @Test
    fun testShearing() {
        assertEquals(shearingTransformation(1.0,0.0,0.0,0.0,0.0,0.0) * point(2.0,3.0,4.0),
            point(5.0,3.0,4.0)
        )
        assertEquals(shearingTransformation(0.0,0.0,1.0,0.0,0.0,0.0) * point(2.0,3.0,4.0),
            point(2.0,5.0,4.0)
        )
        assertEquals(shearingTransformation(0.0,0.0,0.0,1.0,0.0,0.0) * point(2.0,3.0,4.0),
            point(2.0,7.0,4.0)
        )
        assertEquals(shearingTransformation(0.0,0.0,0.0,0.0,1.0,0.0) * point(2.0,3.0,4.0),
            point(2.0,3.0,6.0)
        )
    }

    @Test
    fun testChainedTransformations() {
        // note that chaining must be done in reverse order since A*B is not same as B*A
        val testPoint: Tuple = point(1.0,0.0,1.0)
        val testRotateX: Matrix = rotateXAxisTransformation(PI / 2)
        val testScaling: Matrix = scalingTransformation(5.0,5.0,5.0)
        val testTranslation: Matrix = translationTransformation(10.0,5.0,7.0)

        assertEquals(testRotateX*testPoint, point(1.0,-1.0,0.0))
        assertEquals(testScaling*testRotateX*testPoint, point(5.0,-5.0,0.0))
        assertEquals(testTranslation*testScaling*testRotateX*testPoint, point(15.0,0.0,7.0))
    }

    @Test
    fun testDefaultOrientation() {
        val from: Tuple = point(0.0,0.0,0.0)
        val to: Tuple = point(0.0, 0.0, -1.0)
        val up: Tuple = vector(0.0, 1.0, 0.0)
        val t: Matrix = viewTransformation(from, to, up)
        assertEquals(t, identityMatrix4x4())
    }

    @Test
    fun testTransformationMovesAroundWorld() {
        val from: Tuple = point(0.0,0.0,8.0)
        val to: Tuple = point(0.0, 0.0, 0.0)
        val up: Tuple = vector(0.0, 1.0, 0.0)
        val t: Matrix = viewTransformation(from, to, up)
        assertEquals(t, translationTransformation(0.0,0.0,-8.0))
    }
}