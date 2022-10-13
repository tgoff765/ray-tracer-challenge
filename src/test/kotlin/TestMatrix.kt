import base.core.*
import kotlin.test.*

class TestMatrix {


    @Test
    fun create2x2Matrix() {
        val testMatrix: Matrix = Matrix(2,2)

        val matrixValues = mutableListOf(
            mutableListOf(-3.0, 5.0),
            mutableListOf(1.0, -2.0)
        )
        testMatrix.setAllMatrixValues(matrixValues)
        assertTrue(testMatrix.getMatrixValue(1,1).equalsDelta(-3.0))
        assertTrue(testMatrix.getMatrixValue(1,2).equalsDelta(5.0))
        assertTrue(testMatrix.getMatrixValue(2,1).equalsDelta(1.0))
        assertTrue(testMatrix.getMatrixValue(2,2).equalsDelta(-2.0))
    }

    @Test
    fun create3x3Matrix() {
        val testMatrix: Matrix = Matrix(3,3)

        val matrixValues = mutableListOf(
            mutableListOf(-3.0, 5.0, 0.0),
            mutableListOf(1.0,-2.0,-7.0),
            mutableListOf(0.0,1.0,1.0)
        )

        testMatrix.setAllMatrixValues(matrixValues)
        assertTrue(testMatrix.getMatrixValue(1,1).equalsDelta(-3.0))
        assertTrue(testMatrix.getMatrixValue(2,2).equalsDelta(-2.0))
        assertTrue(testMatrix.getMatrixValue(3,3).equalsDelta(1.0))
    }

    @Test
    fun create4x4Matrix() {
        val testMatrix: Matrix = Matrix(4,4)

        val matrixValues = mutableListOf(
            mutableListOf(1.0,2.0,3.0,4.0),
            mutableListOf(5.5,6.5,7.5,8.5),
            mutableListOf(9.0,10.0,11.0,12.0),
            mutableListOf(13.5,14.5,15.5,16.5)
        )

        testMatrix.setAllMatrixValues(matrixValues)
        assertTrue(testMatrix.getMatrixValue(1,1).equalsDelta(1.0))
        assertTrue(testMatrix.getMatrixValue(1,4).equalsDelta(4.0))
        assertTrue(testMatrix.getMatrixValue(2,1).equalsDelta(5.5))
        assertTrue(testMatrix.getMatrixValue(2,3).equalsDelta(7.5))
        assertTrue(testMatrix.getMatrixValue(3,3).equalsDelta(11.0))
        assertTrue(testMatrix.getMatrixValue(4,1).equalsDelta(13.5))
        assertTrue(testMatrix.getMatrixValue(4,3).equalsDelta(15.5))
    }

    @Test
    fun testEquality() {
        val testMatrix1: Matrix = Matrix(4,4)

        val testMatrix2: Matrix = Matrix(4,4)

        val matrixValues = mutableListOf(
            mutableListOf(1.0,2.0,3.0,4.0),
            mutableListOf(5.0,6.0,7.0,8.0),
            mutableListOf(9.0,8.0,7.0,6.0),
            mutableListOf(5.0,4.0,3.0,2.0)
        )

        testMatrix1.setAllMatrixValues(matrixValues)
        testMatrix2.setAllMatrixValues(matrixValues)
        assertEquals(testMatrix1, testMatrix2)
    }

    @Test
    fun testInEquality() {
        val testMatrix1: Matrix = Matrix(4,4)
        val testMatrix2: Matrix = Matrix(4,4)

        val matrixValues1 = mutableListOf(
            mutableListOf(1.0,2.0,3.0,4.0),
            mutableListOf(5.0,6.0,7.0,8.0),
            mutableListOf(9.0,8.0,7.0,6.0),
            mutableListOf(5.0,4.0,3.0,2.0)
        )

        val matrixValues2 = mutableListOf(
            mutableListOf(1.0,2.0,3.1,4.0),
            mutableListOf(5.0,6.0,7.0,8.0),
            mutableListOf(9.0,6.0,7.0,6.0),
            mutableListOf(5.0,4.0,3.0,2.0)
        )

        testMatrix1.setAllMatrixValues(matrixValues1)
        testMatrix2.setAllMatrixValues(matrixValues2)
        assertNotEquals(testMatrix1, testMatrix2)
    }

    @Test
    fun testMultiply4x4Matrix() {
        val testMatrix1: Matrix = Matrix(4,4)
        val testMatrix2: Matrix = Matrix(4,4)

        val matrixValues1 = mutableListOf(
            mutableListOf(1.0,2.0,3.0,4.0),
            mutableListOf(5.0,6.0,7.0,8.0),
            mutableListOf(9.0,8.0,7.0,6.0),
            mutableListOf(5.0,4.0,3.0,2.0)
        )

        val matrixValues2 = mutableListOf(
            mutableListOf(-2.0,1.0,2.0,3.0),
            mutableListOf(3.0,2.0,1.0,-1.0),
            mutableListOf(4.0,3.0,6.0,5.0),
            mutableListOf(1.0,2.0,7.0,8.0)
        )

        testMatrix1.setAllMatrixValues(matrixValues1)
        testMatrix2.setAllMatrixValues(matrixValues2)

        val multipliedMatrix: Matrix = testMatrix1 * testMatrix2
        assertTrue(multipliedMatrix.getMatrixValue(1,1).equalsDelta(20.0))
        assertTrue(multipliedMatrix.getMatrixValue(1,4).equalsDelta(48.0))
        assertTrue(multipliedMatrix.getMatrixValue(2,1).equalsDelta(44.0))
        assertTrue(multipliedMatrix.getMatrixValue(2,3).equalsDelta(114.0))
        assertTrue(multipliedMatrix.getMatrixValue(3,3).equalsDelta(110.0))
        assertTrue(multipliedMatrix.getMatrixValue(4,1).equalsDelta(16.0))
        assertTrue(multipliedMatrix.getMatrixValue(4,3).equalsDelta(46.0))

    }

    @Test
    fun testMultiplyByTuple() {
        val testMatrix: Matrix = Matrix(4,4)
        val testTuple: Tuple = Tuple(1.0,2.0,3.0,1.0)

        val matrixValues = mutableListOf(
            mutableListOf(1.0,2.0,3.0,4.0),
            mutableListOf(2.0,4.0,4.0,2.0),
            mutableListOf(8.0,6.0,4.0,1.0),
            mutableListOf(0.0,0.0,0.0,1.0)
        )

        testMatrix.setAllMatrixValues(matrixValues)
        val multipliedTuple: Tuple = testMatrix * testTuple
        assertTrue(multipliedTuple.x.equalsDelta(18.0))
        assertTrue(multipliedTuple.y.equalsDelta(24.0))
        assertTrue(multipliedTuple.z.equalsDelta(33.0))
        assertTrue(multipliedTuple.w.equalsDelta(1.0))
    }

    @Test
    fun testMultiplyMatrixByIdentity() {
        val testMatrix: Matrix = Matrix(4,4)
        val identityMatrix: Matrix = Matrix(4,4)

        val matrixValues = mutableListOf(
            mutableListOf(1.0,2.0,3.0,4.0),
            mutableListOf(2.0,4.0,4.0,2.0),
            mutableListOf(8.0,6.0,4.0,1.0),
            mutableListOf(0.0,0.0,0.0,1.0)
        )

        val identityMatrixValues = mutableListOf(
            mutableListOf(1.0,0.0,0.0,0.0),
            mutableListOf(0.0,1.0,0.0,0.0),
            mutableListOf(0.0,0.0,1.0,0.0),
            mutableListOf(0.0,0.0,0.0,1.0)
        )

        testMatrix.setAllMatrixValues(matrixValues)
        identityMatrix.setAllMatrixValues(identityMatrixValues)

        val resultMatrix = testMatrix * identityMatrix
        assertTrue(resultMatrix.getMatrixValue(1,1).equalsDelta(1.0))
        assertTrue(resultMatrix.getMatrixValue(3,3).equalsDelta(4.0))
        assertTrue(resultMatrix.getMatrixValue(3,1).equalsDelta(8.0))
        assertTrue(resultMatrix.getMatrixValue(4,3).equalsDelta(0.0))
    }

    @Test
    fun testMultiplyTupleByIdentity() {
        val identityMatrix: Matrix = Matrix(4,4)
        val testTuple: Tuple = Tuple(1.0,2.0,3.0,4.0)

        val identityMatrixValues = mutableListOf(
            mutableListOf(1.0,0.0,0.0,0.0),
            mutableListOf(0.0,1.0,0.0,0.0),
            mutableListOf(0.0,0.0,1.0,0.0),
            mutableListOf(0.0,0.0,0.0,1.0)
        )

        identityMatrix.setAllMatrixValues(identityMatrixValues)
        val result: Tuple = identityMatrix * testTuple

        assertTrue(result.x.equalsDelta(1.0))
        assertTrue(result.y.equalsDelta(2.0))
        assertTrue(result.z.equalsDelta(3.0))
        assertTrue(result.w.equalsDelta(4.0))
    }

    @Test
    fun testTransposedMatrix() {
        val testMatrix: Matrix = Matrix(4,4)

        val identityMatrixValues = mutableListOf(
            mutableListOf(0.0,9.0,3.0,0.0),
            mutableListOf(9.0,8.0,0.0,8.0),
            mutableListOf(1.0,8.0,5.0,3.0),
            mutableListOf(0.0,0.0,5.0,8.0)
        )

        testMatrix.setAllMatrixValues(identityMatrixValues)
        val transposedMatrix: Matrix = testMatrix.transpose()

        assertTrue(transposedMatrix.getMatrixValue(1,2).equalsDelta(9.0))
        assertTrue(transposedMatrix.getMatrixValue(4,4).equalsDelta(8.0))
        assertTrue(transposedMatrix.getMatrixValue(3,2).equalsDelta(0.0))
        assertTrue(transposedMatrix.getMatrixValue(3,3).equalsDelta(5.0))
    }

    @Test
    fun testTransposedIdentityMatrix() {
        val identityMatrix: Matrix = Matrix(4,4)
        val identityMatrixValues = mutableListOf(
            mutableListOf(1.0,0.0,0.0,0.0),
            mutableListOf(0.0,1.0,0.0,0.0),
            mutableListOf(0.0,0.0,1.0,0.0),
            mutableListOf(0.0,0.0,0.0,1.0)
        )
        identityMatrix.setAllMatrixValues(identityMatrixValues)
        val transposedMatrix: Matrix = identityMatrix.transpose()

        assertTrue(transposedMatrix.getMatrixValue(1,1).equalsDelta(1.0))
        assertTrue(transposedMatrix.getMatrixValue(2,2).equalsDelta(1.0))
        assertTrue(transposedMatrix.getMatrixValue(3,3).equalsDelta(1.0))
        assertTrue(transposedMatrix.getMatrixValue(4,4).equalsDelta(1.0))
    }

    @Test
    fun testDeterminant() {
        val testMatrix: Matrix = Matrix(2,2)
        val testMatrixValues = mutableListOf(
            mutableListOf(1.0,5.0),
            mutableListOf(-3.0,2.0)
        )
        testMatrix.setAllMatrixValues(testMatrixValues)

        assertTrue(testMatrix.determinant().equalsDelta(17.0))
    }

    @Test
    fun testSubMatrix4x4() {
        val testMatrix: Matrix = Matrix(4,4)
        val testMatrixValues = mutableListOf(
            mutableListOf(-6.0,1.0,1.0,6.0),
            mutableListOf(-8.0,5.0,8.0,6.0),
            mutableListOf(-1.0,0.0,8.0,2.0),
            mutableListOf(-7.0,1.0,-1.0,1.0)
        )
        testMatrix.setAllMatrixValues(testMatrixValues)
        val subMatrix: Matrix = testMatrix.subMatrix(3,2)

        assertTrue(subMatrix.getMatrixValue(1,1).equalsDelta(-6.0))
        assertTrue(subMatrix.getMatrixValue(2,2).equalsDelta(8.0))
        assertTrue(subMatrix.getMatrixValue(3,3).equalsDelta(1.0))
        assertTrue(subMatrix.getMatrixValue(1,3).equalsDelta(6.0))
    }

    @Test
    fun testSubMatrix3x3() {
        val testMatrix: Matrix = Matrix(3,3)
        val testMatrixValues = mutableListOf(
            mutableListOf(1.0,5.0,0.0),
            mutableListOf(-3.0,2.0,7.0),
            mutableListOf(0.0,6.0,-3.0)
        )
        testMatrix.setAllMatrixValues(testMatrixValues)
        val subMatrix: Matrix = testMatrix.subMatrix(1,3)
        assertTrue(subMatrix.getMatrixValue(1,1).equalsDelta(-3.0))
        assertTrue(subMatrix.getMatrixValue(1,2).equalsDelta(2.0))
        assertTrue(subMatrix.getMatrixValue(2,1).equalsDelta(0.0))
        assertTrue(subMatrix.getMatrixValue(2,2).equalsDelta(6.0))
    }

    @Test
    fun testMinor() {
        val testMatrix: Matrix = Matrix(3,3)
        val testMatrixValues = mutableListOf(
            mutableListOf(3.0,5.0,0.0),
            mutableListOf(2.0,-1.0,-7.0),
            mutableListOf(6.0,-1.0,5.0)
        )
        testMatrix.setAllMatrixValues(testMatrixValues)
        assertTrue(testMatrix.minor(2,1).equalsDelta(25.0))
    }

    @Test
    fun testCofactor() {
        val testMatrix: Matrix = Matrix(3,3)
        val testMatrixValues = mutableListOf(
            mutableListOf(3.0,5.0,0.0),
            mutableListOf(2.0,-1.0,-7.0),
            mutableListOf(6.0,-1.0,5.0)
        )
        testMatrix.setAllMatrixValues(testMatrixValues)
        assertTrue(testMatrix.minor(1,1).equalsDelta(-12.0))
        assertTrue(testMatrix.cofactor(1,1).equalsDelta(-12.0))
        assertTrue(testMatrix.minor(1,1).equalsDelta(-12.0))
        assertTrue(testMatrix.minor(2,1).equalsDelta(25.0))
        assertTrue(testMatrix.cofactor(2,1).equalsDelta(-25.0))
    }

    @Test
    fun testDeterminantOnLargeMatrices() {
        val testMatrix1: Matrix = Matrix(3,3)
        val testMatrix2: Matrix = Matrix(4,4)
        val testMatrix1Values = mutableListOf(
            mutableListOf(1.0,2.0,6.0),
            mutableListOf(-5.0,8.0,-4.0),
            mutableListOf(2.0,6.0,4.0)
        )
        val testMatrix2Values = mutableListOf(
            mutableListOf(-2.0,-8.0,3.0,5.0),
            mutableListOf(-3.0,1.0,7.0,3.0),
            mutableListOf(1.0,2.0,-9.0,6.0),
            mutableListOf(-6.0,7.0,7.0,-9.0)
        )
        testMatrix1.setAllMatrixValues(testMatrix1Values)
        testMatrix2.setAllMatrixValues(testMatrix2Values)

        assertTrue(testMatrix1.determinant().equalsDelta(-196.0))
        assertTrue(testMatrix2.determinant().equalsDelta(-4071.0))
    }

    @Test
    fun testInvertability() {
        val testMatrix1: Matrix = Matrix(4,4)
        val testMatrix2: Matrix = Matrix(4,4)
        val testMatrix1Values = mutableListOf(
            mutableListOf(6.0,4.0,4.0,4.0),
            mutableListOf(5.0,5.0,7.0,6.0),
            mutableListOf(4.0,-9.0,3.0,-7.0),
            mutableListOf(9.0,1.0,7.0,-6.0)
        )
        val testMatrix2Values = mutableListOf(
            mutableListOf(-4.0,2.0,-2.0,-3.0),
            mutableListOf(9.0,6.0,2.0,6.0),
            mutableListOf(0.0,-5.0,1.0,-5.0),
            mutableListOf(0.0,0.0,0.0,0.0)
        )

        testMatrix1.setAllMatrixValues(testMatrix1Values)
        testMatrix2.setAllMatrixValues(testMatrix2Values)

        assertTrue(testMatrix1.isInvertible())
        assertFalse(testMatrix2.isInvertible())
    }

    @Test
    fun testInverse() {
        val testMatrix: Matrix = Matrix(4,4)
        val testMatrixValues = mutableListOf(
            mutableListOf(-5.0,2.0,6.0,-8.0),
            mutableListOf(1.0,-5.0,1.0,8.0),
            mutableListOf(7.0,7.0,-6.0,-7.0),
            mutableListOf(1.0,-3.0,7.0,4.0)
        )
        testMatrix.setAllMatrixValues(testMatrixValues)
        val invertedMatrix: Matrix = testMatrix.inverse()

        assertTrue(testMatrix.determinant().equalsDelta(532.0))
        assertTrue(testMatrix.cofactor(3,4).equalsDelta(-160.0))
        assertTrue(invertedMatrix.getMatrixValue(4,3).equalsDelta(-160.0/532))
        assertTrue(testMatrix.cofactor(4,3).equalsDelta(105.0))
        assertTrue(invertedMatrix.getMatrixValue(3,4).equalsDelta(105.0/532.0))
    }

    @Test
    fun testInverse2() {
        val testMatrix: Matrix = Matrix(4,4)
        val testMatrixValues = mutableListOf(
            mutableListOf(8.0,-5.0,9.0,2.0),
            mutableListOf(7.0,5.0,6.0,1.0),
            mutableListOf(-6.0,0.0,9.0,6.0),
            mutableListOf(-3.0,0.0,-9.0,-4.0)
        )
        testMatrix.setAllMatrixValues(testMatrixValues)
        val invertedMatrix: Matrix = testMatrix.inverse()

        assertTrue(invertedMatrix.getMatrixValue(4,3).equalsDelta(-0.76923))
        assertTrue(invertedMatrix.getMatrixValue(2,2).equalsDelta(0.12308))
        assertTrue(invertedMatrix.getMatrixValue(1,4).equalsDelta(-0.53846))
    }

    @Test
    fun testInverse3() {
        val testMatrix: Matrix = Matrix(4,4)
        val testMatrixValues = mutableListOf(
            mutableListOf(9.0,3.0,0.0,9.0),
            mutableListOf(-5.0,-2.0,-6.0,-3.0),
            mutableListOf(-4.0,9.0,6.0,4.0),
            mutableListOf(-7.0,6.0,6.0,2.0)
        )
        testMatrix.setAllMatrixValues(testMatrixValues)
        val invertedMatrix: Matrix = testMatrix.inverse()

        assertTrue(invertedMatrix.getMatrixValue(1,3).equalsDelta(0.14444))
        assertTrue(invertedMatrix.getMatrixValue(2,4).equalsDelta(-0.33333))
        assertTrue(invertedMatrix.getMatrixValue(3,3).equalsDelta(-0.10926))
    }

    @Test
    fun testMultiplyByInverse() {
        // verify that A * B = C
        // then C * BInverted = A
        val testMatrixA: Matrix = Matrix(4,4)
        val testMatrixB: Matrix = Matrix(4,4)
        val testMatrixAValues = mutableListOf(
            mutableListOf(3.0,-9.0,7.0,3.0),
            mutableListOf(3.0,-8.0,2.0,-9.0),
            mutableListOf(-4.0,4.0,4.0,1.0),
            mutableListOf(-6.0,5.0,-1.0,1.0)
        )
        val testMatrixBValues = mutableListOf(
            mutableListOf(8.0,2.0,2.0,2.0),
            mutableListOf(3.0,-1.0,7.0,0.0),
            mutableListOf(7.0,0.0,5.0,4.0),
            mutableListOf(6.0,-2.0,0.0,5.0)
        )

        testMatrixA.setAllMatrixValues(testMatrixAValues)
        testMatrixB.setAllMatrixValues(testMatrixBValues)

        val compositeMatrix: Matrix = testMatrixA * testMatrixB
        val backToOriginal: Matrix = compositeMatrix * testMatrixB.inverse()

        assertEquals(testMatrixA, backToOriginal)
    }

}