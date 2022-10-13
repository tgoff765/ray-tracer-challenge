import base.core.Matrix
import base.core.Tuple
import base.core.point
import kotlin.math.cos
import kotlin.math.sin

// Factory functions used to create certain transformations
fun translationTransformation(a: Double, b: Double, c: Double): Matrix {
    // translation matrices are of the form
    // [1 0 0 a] -> x = x + aw
    // [0 1 0 b] -> y = y + bw
    // [0 0 1 c] -> z = z + cw
    // [0 0 0 1] -> w = w
    // Note: if other object we multiply this against is a base.point it will get translated
    // but if it' a base.vector it will be unaffected (since w = 0).
    val returnMatrix: Matrix = Matrix(4,4)
    val returnMatrixValues = mutableListOf(
        mutableListOf(1.0,0.0,0.0,a),
        mutableListOf(0.0,1.0,0.0,b),
        mutableListOf(0.0,0.0,1.0,c),
        mutableListOf(0.0,0.0,0.0,1.0)
    )
    returnMatrix.setAllMatrixValues(returnMatrixValues)
    return returnMatrix
}

fun scalingTransformation(a: Double, b: Double, c: Double): Matrix {
    // scale matrices are of the form
    // [x 0 0 0] -> x = ax
    // [0 y 0 0] -> y = by
    // [0 0 z 0] -> z = cz
    // [0 0 0 1] -> w = w
    val returnMatrix: Matrix = Matrix(4,4)
    val returnMatrixValues = mutableListOf(
        mutableListOf(a,0.0,0.0,0.0),
        mutableListOf(0.0,b,0.0,0.0),
        mutableListOf(0.0,0.0,c,0.0),
        mutableListOf(0.0,0.0,0.0,1.0)
    )
    returnMatrix.setAllMatrixValues(returnMatrixValues)
    return returnMatrix
}

fun rotateXAxisTransformation(r: Double): Matrix {
    // rotate x-axis matrices are of the form
    // [1 0 0 0] -> x = x
    // [0 cos(r) -sin(r) 0] -> y = cos(r)*y -sin(r)*z
    // [0 sin(r) cos(r) 0] -> z = sin(r)*y + cos(r)*Z
    // [0 0 0 1] -> w = w
    val returnMatrix: Matrix = Matrix(4,4)
    val returnMatrixValues = mutableListOf(
        mutableListOf(1.0,0.0,0.0,0.0),
        mutableListOf(0.0, cos(r),-sin(r),0.0),
        mutableListOf(0.0, sin(r), cos(r),0.0),
        mutableListOf(0.0,0.0,0.0,1.0)
    )
    returnMatrix.setAllMatrixValues(returnMatrixValues)
    return returnMatrix
}

fun rotateYAxisTransformation(r: Double): Matrix {
    // rotate y-axis matrices are of the form
    // [cos(r) 0 sin(r) 0] -> x = x*cos(r) + z*sin(r)
    // [0 1 0 0] -> y = y
    // [-sin(r) 0 cos(r) 0] -> z = -sin(r)*y + cos(r)*Z
    // [0 0 0 1] -> w = w
    val returnMatrix: Matrix = Matrix(4,4)
    val returnMatrixValues = mutableListOf(
        mutableListOf(cos(r),0.0,sin(r),0.0),
        mutableListOf(0.0, 1.0,0.0,0.0),
        mutableListOf(-sin(r), 0.0, cos(r),0.0),
        mutableListOf(0.0,0.0,0.0,1.0)
    )
    returnMatrix.setAllMatrixValues(returnMatrixValues)
    return returnMatrix
}

fun rotateZAxisTransformation(r: Double): Matrix {
    // rotate z-axis matrices are of the form
    // [cos(r) -sin(r) 0 0] -> x = x*cos(r) - y*sin(r)
    // [sin(r) cos(r) 0 0] -> y = x*sin(r) + y*cos(r)
    // [0 0 1 0] -> z = z
    // [0 0 0 1] -> w = w
    val returnMatrix: Matrix = Matrix(4,4)
    val returnMatrixValues = mutableListOf(
        mutableListOf(cos(r),-sin(r),0.0,0.0),
        mutableListOf(sin(r), cos(r),0.0,0.0),
        mutableListOf(0.0, 0.0, 1.0,0.0),
        mutableListOf(0.0,0.0,0.0,1.0)
    )
    returnMatrix.setAllMatrixValues(returnMatrixValues)
    return returnMatrix
}

fun shearingTransformation(xy: Double, xz: Double, yx: Double, yz: Double, zx: Double, zy: Double): Matrix {
    // shear matrices are of the form
    // [1 xy xz 0] -> x = x + xy*y + xz*z
    // [yx 1 yz 0] -> y = x*yx + y + yz*z
    // [zx zy 1 0] -> z = x*zx + y*zy + z
    // [0 0 0 1] -> w = w
    // note shear transforms each component in proportion to the other two
    val returnMatrix: Matrix = Matrix(4,4)
    val returnMatrixValues = mutableListOf(
        mutableListOf(1.0,xy,xz,0.0),
        mutableListOf(yx,1.0,yz,0.0),
        mutableListOf(zx,zy,1.0,0.0),
        mutableListOf(0.0,0.0,0.0,1.0)
    )
    returnMatrix.setAllMatrixValues(returnMatrixValues)
    return returnMatrix
}

fun viewTransformation(from: Tuple, to: Tuple, up: Tuple): Matrix {
    // given a position you want the camera to be at (from) and base.point(to) along with the direction of up,
    // creates a matrix transformation that moves the world around as if you were moving a camera in the same manner

    val forward: Tuple = (to - from).normalize()
    val left: Tuple = forward.crossProduct(up.normalize())
    // calculating trueUp allows us to give approximate direction of up instead of breaking out a calculator to
    // calculate the precise value every time
    val trueUp: Tuple = left.crossProduct(forward)
    // given these three vectors we can now construct an orientation matrix
    val orientationMatrix: Matrix = Matrix(4,4)
    val orientationMatrixValues = mutableListOf(
        mutableListOf(left.x,left.y,left.z,0.0),
        mutableListOf(trueUp.x,trueUp.y,trueUp.z,0.0),
        mutableListOf(-forward.x,-forward.y,-forward.z,0.0),
        mutableListOf(0.0,0.0,0.0,1.0)
    )
    orientationMatrix.setAllMatrixValues(orientationMatrixValues)
    // final step is to move the scene into place before orienting it
    return orientationMatrix * translationTransformation(-from.x, -from.y, -from.z)
}

fun main() {
    print(translationTransformation(5.0, -3.0, 2.0) * point(-3.0,4.0,5.0))
}
