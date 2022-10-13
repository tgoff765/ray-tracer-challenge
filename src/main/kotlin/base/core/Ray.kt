package base.core

class Ray(var origin: Tuple, var direction: Tuple) {

    fun position(t: Double): Tuple {
        // to find the position of a base.point in a given position, take
        // the origin and multiply direction base.vector by t (aka time)
        return origin + direction * t
    }
}

fun transform(r: Ray, m: Matrix): Ray {
    // returns a new ray which has been transformed by a transformation matrix
    return Ray(m*r.origin, m*r.direction)
}