package base.core

import base.core.Tuple
import base.patterns.Pattern
import base.core.reflect
import base.shapes.Shape
import kotlin.math.pow

class Material(
    var color: Color = Color(1.0, 1.0, 1.0),
    var ambient: Double = 0.1,
    var diffuse: Double = 0.9,
    var specular: Double = 0.9,
    var shininess: Double = 200.0,
    var reflective: Double = 0.0,
    var transparency: Double = 0.0,
    var refractiveIndex: Double = 1.0,
    var pattern: Pattern? = null) {

    override fun equals(other: Any?): Boolean {
        if (other !is Material) {
            return false
        }

        if (color != other.color || ambient != other.ambient || diffuse != other.diffuse
            || specular != other.specular || shininess != other.shininess) {
            return false
        }

        return true
    }

    override fun hashCode(): Int {
        var result = color.hashCode()
        result = 31 * result + ambient.hashCode()
        result = 31 * result + diffuse.hashCode()
        result = 31 * result + specular.hashCode()
        result = 31 * result + shininess.hashCode()
        return result
    }

    // Given material we are casting to + light, position, eye and normal vectors, returns the color to shade point
    fun lighting(light: Light, obj: Shape, p: Tuple, eyeV: Tuple, normalV: Tuple, inShadow: Boolean = false): Color {
        // check if material has pattern color to use
        val colorToUse: Color = if (pattern != null) {
            pattern!!.colorAtObject(obj, p)
        } else {
            // otherwise just use the material's color
            color
        }

        // combine the surface color with the light's color and intensity
        val effectiveColor: Color = colorToUse * light.intensity
        // compute the ambient contribution
        val ambient: Color = effectiveColor * ambient

        // if point is in shadow, we return only the ambient lighting and ignore the diffuse and specular components
        if (inShadow) {
            return ambient
        }

        // find the direction of the light source
        val lightV: Tuple = (light.position - p).normalize()
        val diffuseContrib: Color
        val specularContrib: Color
        // lightDotNormal represents the cosine of the angle between the light base.vector and the normal base.vector.
        // A negative number means the light is on the other side of the surface
        val lightDotNormal: Double = lightV.dot(normalV)
        if (lightDotNormal < 0) {
            diffuseContrib = BLACK
            specularContrib = BLACK
        } else {
            // Compute the diffuse contribution
            diffuseContrib = effectiveColor * diffuse *lightDotNormal
            // reflectDotEye represents the cosine of the angle between the reflection base.vector and the eye base.vector. A negative
            // number means the light reflects away from the eye.
            val reflectV: Tuple = reflect(-lightV, normalV)
            val reflectDotEye: Double = reflectV.dot(eyeV)

            specularContrib = if (reflectDotEye <= 0.0) {
                BLACK
            } else {
                // compute the specular contribution
                val factor: Double = reflectDotEye.pow(shininess)
                light.intensity * specular * factor
            }
        }

        return ambient + diffuseContrib + specularContrib
    }
}

