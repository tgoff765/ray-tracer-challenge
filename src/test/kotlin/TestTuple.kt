
import base.core.*
import kotlin.math.sqrt
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue


class TestTuple() {

    @Test
    fun testPointTuple() {
        val testPoint: Tuple = Tuple(4.3, -4.2, 3.1, 1.0)
        assertTrue(testPoint.x.equalsDelta(4.3))
        assertTrue(testPoint.y.equalsDelta(-4.2))
        assertTrue(testPoint.z.equalsDelta(3.1))
        assertTrue(testPoint.w.equalsDelta(1.0))
        assertEquals(testPoint.tupleType(), TupleType.POINT)
    }

    @Test
    fun testVectorTuple() {
        val testVector: Tuple = Tuple(4.3, -4.2, 3.1, 0.0)
        assertTrue(testVector.x.equalsDelta(4.3))
        assertTrue(testVector.y.equalsDelta( -4.2))
        assertTrue(testVector.z.equalsDelta( 3.1))
        assertTrue(testVector.w.equalsDelta( 0.0))
        assertEquals(testVector.tupleType(), TupleType.VECTOR)
    }

    @Test
    fun testPointFactoryFun() {
        val testPointFactoryFun: Tuple = point(4.0, 4.0, -3.0)
        assertEquals(testPointFactoryFun.tupleType(), TupleType.POINT)
    }

    @Test
    fun testVectorFactoryFun() {
        val testVectorFactoryFun: Tuple = vector(4.0, 4.0, -3.0)
        assertEquals(testVectorFactoryFun.tupleType(), TupleType.VECTOR)
    }

    @Test
    fun addTuples() {
        val testVector1: Tuple = vector(1.0,2.0,3.0)
        val testVector2: Tuple = vector(2.0, 3.0,4.0)

        val testPoint1: Tuple = point(9.0, 17.6, 4.1)
        val testPoint2: Tuple = point(1.0, 1.0, 1.0)

        val combinedVec1and2 = testVector1 + testVector2
        val combinedVec1Point1 = testVector1 + testPoint1

        assertTrue(combinedVec1and2.x.equalsDelta( 3.0))
        assertEquals(combinedVec1and2.tupleType(), TupleType.VECTOR)

        assertTrue(combinedVec1Point1.z.equalsDelta(7.1))
        assertEquals(combinedVec1Point1.tupleType(), TupleType.POINT)

        assertFailsWith<Exception>("Cannot add two points together!") { testPoint1 + testPoint2 }
    }

    @Test
    fun subtractTuples() {
        val testVector1: Tuple = vector(1.0,2.0,3.0)
        val testVector2: Tuple = vector(2.0, 3.0,4.0)

        val testPoint1: Tuple = point(9.0, 17.6, 4.1)
        val testPoint2: Tuple = point(1.0, 1.0, 1.0)

        val combinedVec1and2 = testVector1 - testVector2
        val combinedPoint1Vec1 =  testPoint1 - testVector1

        assertTrue(combinedVec1and2.y.equalsDelta( -1.0))
        assertEquals(combinedVec1and2.tupleType(), TupleType.VECTOR)

        assertTrue(combinedPoint1Vec1.x.equalsDelta( 8.0))
        assertEquals(combinedPoint1Vec1.tupleType(), TupleType.POINT)

        assertFailsWith<Exception>("Cannot subtract a base.point from a base.vector!") { testVector2 - testPoint2 }
    }

    @Test
    fun testNegation() {
        val testVector: Tuple = vector(9.0, -8.0, 0.0)
        val negatedTestVector = -testVector
        assertTrue(negatedTestVector.x.equalsDelta( -9.0))
        assertTrue(negatedTestVector.y.equalsDelta( 8.0 ))
        // Note: to keep eye on this, as negative 0.0 is -0.0
        assertTrue(negatedTestVector.z.equalsDelta( 0.0))
        assertEquals(negatedTestVector.tupleType(), TupleType.VECTOR)
    }

    @Test
    fun testScalarMultiplication() {
        val testPoint: Tuple = point(8.0, -7.6, 1.2)
        val multipliedPoint1 = testPoint * -3.5
        val multipliedPoint2 = testPoint * 0.5

        assertTrue(multipliedPoint1.x.equalsDelta( -28.0))
        assertTrue(multipliedPoint1.y.equalsDelta( 26.6))

        assertTrue(multipliedPoint2.x.equalsDelta( 4.0))
        assertTrue(multipliedPoint2.z.equalsDelta( 0.6))
    }

    @Test
    fun testScalarDivision() {
        val testVector: Tuple = vector(-4.2, -7.0, 0.0)
        val dividedVector1: Tuple = testVector / (2.0)
        val dividedVector2: Tuple = testVector / (-4.0)

        assertTrue(dividedVector1.x.equalsDelta(-2.1))
        assertTrue(dividedVector1.z.equalsDelta(0.0))

        assertTrue(dividedVector2.y.equalsDelta(1.75))
        assertTrue(dividedVector2.x.equalsDelta(1.05))
    }

    @Test
    fun testMagnitude() {
        val testUnitVector: Tuple = vector(0.0, 1.0, 0.0)
        val testVector: Tuple = vector(1.0, 2.0, 3.0
        )
        assertTrue(testUnitVector.magnitude().equalsDelta(1.0))
        assertTrue(testVector.magnitude().equalsDelta(sqrt(14.0)))
    }

    @Test
    fun testNormalize() {
        val testNormalizedVector: Tuple = vector(1.0, 2.0, 3.0).normalize()
        assertTrue(testNormalizedVector.x.equalsDelta(1.0/sqrt(14.0)))
        assertTrue(testNormalizedVector.y.equalsDelta(2.0/sqrt(14.0)))
        assertTrue(testNormalizedVector.z.equalsDelta(3.0/sqrt(14.0)))
    }

    @Test
    fun testDot() {
        val testVector1: Tuple = vector(1.0, 2.0, 3.0)
        val testVector2: Tuple = vector(2.0, 3.0, 4.0)
        assertTrue(testVector1.dot(testVector2).equalsDelta(20.0))
    }

    @Test
    fun testCrossProduct() {
        val testVector1: Tuple = vector(1.0, 2.0, 3.0)
        val testVector2: Tuple = vector(2.0, 3.0, 4.0)
        assertEquals(testVector1.crossProduct(testVector2), vector(-1.0,2.0,-1.0))
        assertEquals(testVector2.crossProduct(testVector1), vector(1.0,-2.0,1.0))
    }

    @Test
    fun testReflectVector45() {
        val testVector: Tuple = vector(1.0, -1.0, 0.0)
        val testNormalVector: Tuple = vector(0.0, 1.0, 0.0)
        assertEquals(reflect(testVector, testNormalVector), vector(1.0, 1.0, 0.0))
    }

    @Test
    fun testReflectOffSlanted() {
        val testVector: Tuple = vector(0.0, -1.0, 0.0)
        val testNormalVector: Tuple = vector(sqrt(2.0)/2.0, sqrt(2.0)/2.0, 0.0)
        assertEquals(reflect(testVector, testNormalVector), vector(1.0, 0.0, 0.0))
    }


}