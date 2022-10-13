import kotlin.math.abs

// Because of floating base.point imprecision and round-off errors rather than comparing for exact equality
// we consider two floating points to be equivalent if the difference between the two is below some
// very small constant EPSILON

const val EPSILON: Double = 0.0001

fun Double.equalsDelta(other: Double) = abs(this - other) <= EPSILON