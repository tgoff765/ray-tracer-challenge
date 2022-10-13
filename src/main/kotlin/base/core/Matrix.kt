package base.core

import equalsDelta

class Matrix(val height: Int, val width: Int) {
    // Set all the values of the matrix to be 0.0
    // Note that setting and getting the value in the matrix follows row order
    // E.g. Matrix23 means pull from the 2nd row and the 3rd column
    // Also just like canvas class indices are internally 0 based
    var numbers: MutableList<MutableList<Double>> = MutableList(height) {MutableList(width) {0.0}}

    private fun setMatrixValue(height: Int, width: Int, n: Double) {
        numbers[height - 1][width - 1] = n
    }

    fun setAllMatrixValues(x: MutableList<MutableList<Double>>) {
        // give the user the option to override all values of the matrix at once
        if (x.flatten().size != height * width) {
            throw Exception("Supplied values do not match matrix dimensions!")
        }
        numbers = x
    }

    fun getMatrixValue(height: Int, width: Int): Double {
        return numbers[height - 1][width - 1]
    }

    fun transpose(): Matrix {
        // given a matrix of [a, b] --> [a, c]
        //                   [c, d]     [b, d]
        var transposedMatrix: Matrix = Matrix(width, height)
        var columnIndexToValuesMap :MutableMap<Int,MutableList<Double>> = mutableMapOf()

        // add to our map a key that corresponds to each column index in the current map
        // note that this is 0 indexed
        for (i in 0 until width) {
            columnIndexToValuesMap[i] = mutableListOf()
        }

        var columnCounter: Int = 0
        // iterate through our matrix
        for (i: MutableList<Double> in numbers) {
            for (j: Double in i) {
                columnIndexToValuesMap.getValue(columnCounter).add(j)
                columnCounter++
            }
            // reset column counter after finish traversal of row
            columnCounter = 0
        }

        // now build matrix values of transposed matrix
        var transposedValues: MutableList<MutableList<Double>> = mutableListOf()

        for (i in 0 until width){
            // if value is non-null add it to our transposed values
            columnIndexToValuesMap[i]?.let { transposedValues.add(it) }
        }

        transposedMatrix.setAllMatrixValues(transposedValues)
        return transposedMatrix
    }

    fun determinant(): Double {
        var determinant: Double = 0.0
        // calculate the determinant for 2x2 matrices
        if (height == 2 && width == 2) {
            determinant = (getMatrixValue(1, 1) * getMatrixValue(2, 2)) -
                    (getMatrixValue(1, 2) * getMatrixValue(2, 1))
        } else {
            // Take the first row and for each entry in the multiply each value by its cofactor
            var x: Int = 1
            for (i: Double in numbers[0]) {
               determinant += (i * cofactor(1, x))
               x++
            }
        }
        return determinant
    }

    private fun deleteMatrixRow(rowNum: Int): Matrix {
        val newMatrix: Matrix = Matrix(height - 1, width)
        val newMatrixValues: MutableList<MutableList<Double>> = mutableListOf()
        var x: Int = 1
        for (i: MutableList<Double> in numbers) {
            if (x == rowNum) {
                x++
                continue
            }
            newMatrixValues.add(i)
            x++
        }
        newMatrix.setAllMatrixValues(newMatrixValues)
        return newMatrix
    }

    private fun deleteMatrixColumn(columnNum: Int): Matrix {
        val newMatrix: Matrix = Matrix(height, width - 1)
        val newMatrixValues: MutableList<MutableList<Double>> = mutableListOf()

        for (i: MutableList<Double> in numbers) {

            val copyCurrentRow: MutableList<Double> = i.toMutableList()
            // subtracting 1 because we're using 1-index here
            // important to take a copy here to avoid mutating the original matrix
            copyCurrentRow.removeAt(columnNum - 1)
            newMatrixValues.add(copyCurrentRow)
        }

        newMatrix.setAllMatrixValues(newMatrixValues)
        return newMatrix
    }

    fun subMatrix(rowNum: Int, columnNum: Int): Matrix {
        // returns a copy of the matrix with the given row and column removed
        val matrixRowRemoved: Matrix = deleteMatrixRow(rowNum)
        return matrixRowRemoved.deleteMatrixColumn(columnNum)
    }

    fun minor(rowNum: Int, columnNum: Int): Double {
        // Takes the submatrix of the matrix using supplied rowNum and columnNum and
        // then computes the determinant of that submatrix
        val subMatrix: Matrix = subMatrix(rowNum, columnNum)
        return subMatrix.determinant()
    }

    fun cofactor(rowNum: Int, columnNum: Int): Double {
        // if rowNum + columnNum is an odd number negate minor otherwise return minor
        return if ((rowNum + columnNum) % 2 == 1) {
            -minor(rowNum, columnNum)
        } else {
            minor(rowNum, columnNum)
        }
    }

    fun isInvertible(): Boolean {
        // matrices are invertible iff their determinant is not equal to 0
        return !determinant().equalsDelta(0.0)
    }

    fun inverse(): Matrix {
        if (!isInvertible()) {
            throw Exception("Matrix is not invertible!")
        }
        val newMatrix: Matrix = Matrix(height, width)

        // use this to track the row and column numbers
        var currentRowNum: Int = 1
        var currentColumnNum: Int = 1

        for (row: MutableList<Double> in numbers) {
            for (column: Double in row) {
                val c: Double = cofactor(currentRowNum, currentColumnNum)
                // take the cofactor and divide by determinant of current matrix to get new value
                val newValue: Double = c / determinant()
                // flipping column and row numbers to transpose
                newMatrix.setMatrixValue(currentColumnNum, currentRowNum, newValue)
                currentColumnNum++
            }
            // reset the column num after each run through loop and increment row num
            currentColumnNum = 1
            currentRowNum++
        }
        return newMatrix
    }

    operator fun times(b: Matrix): Matrix {
        // only have to perform this on other 4x4 matrices
        if (width != 4 || b.width != 4 || height != 4 || b.height != 4) {
            throw Exception("Multiplication not defined for matrices that are not 4x4")
        }
        val newMatrix: Matrix = Matrix(4, 4)
        for (row in 1..4) {
            for (column in 1..4) {
                // taking the row from column A and multiplying by the corresponding column from matrix b
                val computedDotProduct: Double = getMatrixValue(row, 1) * b.getMatrixValue(1, column) +
                                                 getMatrixValue(row, 2) * b.getMatrixValue(2, column) +
                                                 getMatrixValue(row, 3) * b.getMatrixValue(3,column) +
                                                 getMatrixValue(row, 4) * b.getMatrixValue(4, column)
                newMatrix.setMatrixValue(row, column, computedDotProduct)
            }
        }
        return newMatrix
    }

    operator fun times(b: Tuple): Tuple {
        // very similar to multiplying by a 4x4 matrix except here there's only 1 column in the second matrix
        if (width != 4 || height != 4 ) {
            throw Exception("Multiplication not defined for matrices that are not 4x4")
        }
        // use this list to store the values
        var values: MutableList<Double> = mutableListOf()
        for (row in 1..4) {
                // taking the row from column A and multiplying by the corresponding column from matrix b
            val computedDotProduct: Double = getMatrixValue(row, 1) * b.x +
                                             getMatrixValue(row, 2) * b.y +
                                             getMatrixValue(row, 3) * b.z +
                                             getMatrixValue(row, 4) * b.w
            values.add(computedDotProduct)
        }
        return Tuple(values[0], values[1], values[2], values[3])
    }

    override fun toString(): String {
        val numValueString: String = numbers.joinToString("\n").trim()
        return """
            Matrix:
            Height: $height
            Width: $width 
            """.trimIndent() + "\nValues: \n$numValueString"
    }

    override fun equals(other: Any?): Boolean {
        if (other !is Matrix) {
            return false
        }

        if (width != other.width || height != other.height) {
            return false
        }

        val thisMatrixValues: List<Double> = numbers.flatten()
        val otherMatrixValues: List<Double> =  other.numbers.flatten()

        // use the zip function to get all pairs from the two different lists and then use the equals delta to compare
        // if every single value is the same
        return thisMatrixValues.zip(otherMatrixValues).all { (x,y) -> x.equalsDelta(y)}
    }

    override fun hashCode(): Int {
        var result = height
        result = 31 * result + width
        result = 31 * result + numbers.hashCode()
        return result
    }
}

fun identityMatrix4x4(): Matrix {
    val thisMatrix: Matrix = Matrix(4,4)
    val testMatrixValues = mutableListOf(
        mutableListOf(1.0,0.0,0.0,0.0),
        mutableListOf(0.0,1.0,0.0,0.0),
        mutableListOf(0.0,0.0,1.0,0.0),
        mutableListOf(0.0,0.0,0.0,1.0)
    )
    thisMatrix.setAllMatrixValues(testMatrixValues)

    return thisMatrix

}

fun main() {
    val testMatrix: Matrix = Matrix(4,4)
    val testMatrixValues = mutableListOf(
        mutableListOf(-5.0,2.0,6.0,-8.0),
        mutableListOf(1.0,-5.0,1.0,8.0),
        mutableListOf(7.0,7.0,-6.0,-7.0),
        mutableListOf(1.0,-3.0,7.0,4.0)
    )
    testMatrix.setAllMatrixValues(testMatrixValues)
    val invertedMatrix: Matrix = testMatrix.inverse()
    print(invertedMatrix)
}