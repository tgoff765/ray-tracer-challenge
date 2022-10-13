import base.core.*
import base.patterns.Pattern
import base.shapes.Plane
import base.shapes.Shape
import kotlin.test.*
import base.shapes.Sphere
import kotlin.math.sqrt

class TestWorld {

    fun test_pattern(): Pattern {
        class TestPattern: Pattern {
            override var patternTransformation: Matrix = identityMatrix4x4()
        }
        return TestPattern()
    }

    @Test
    fun testEmptyWorld() {
        val w: World = World()
        assertTrue(w.light == null)
        assertEquals(w.objects.size, 0)
    }

    @Test
    fun testDefaultWorld() {
        val l: Light = Light(point(-10.0, 10.0, -10.0), Color(1.0,1.0,1.0))
        val s1: Sphere = Sphere()
        s1.material = Material(color = Color(0.8, 1.0, 0.6), diffuse = 0.7, specular = 0.2)
        val s2: Sphere = Sphere()
        s2.transform = scalingTransformation(0.5, 0.5, 0.5)
        val w: World = defaultWorld()
        assertEquals(w.light, l)
        assertTrue(w.objects.contains(s1))
        assertTrue(w.objects.contains(s2))
    }

    @Test
    fun testIntersectWorldWithRay() {
        val w: World = defaultWorld()
        val r: Ray = Ray(point(0.0, 0.0, -5.0), vector(0.0, 0.0, 1.0))
        val intersectList: List<Intersection> = w.intersectWorld(r)
        assertEquals(intersectList.size, 4)
        assertTrue(intersectList[0].t.equalsDelta(4.0))
        assertTrue(intersectList[1].t.equalsDelta(4.5))
        assertTrue(intersectList[2].t.equalsDelta(5.5))
        assertTrue(intersectList[3].t.equalsDelta(6.0))
    }

    @Test
    fun testShadeIntersection() {
        val w: World = defaultWorld()
        val r: Ray = Ray(point(0.0, 0.0, -5.0), vector(0.0, 0.0, 1.0))
        val s: Shape = w.objects[0]
        val i: Intersection = Intersection(4.0, s)
        val comps: Comps = prepareComputations(i, r)
        val c: Color = w.shadeHit(comps)
        assertEquals(c, Color(0.38066, 0.47582, 0.2855))
    }

    @Test
    fun testShadeIntersectionInside() {
        val w: World = defaultWorld()
        w.light = Light(point(0.0, 0.25, 0.0), Color(1.0, 1.0, 1.0))
        val r: Ray = Ray(point(0.0, 0.0, 0.0), vector(0.0, 0.0, 1.0))
        val s: Shape = w.objects[1]
        val i: Intersection = Intersection(0.5, s)
        val comps: Comps = prepareComputations(i, r)
        val c: Color = w.shadeHit(comps)
        assertEquals(c,(Color(0.90498, 0.90498, 0.90498)))
    }

    @Test
    fun testShadeHitWithShadows() {
        val w: World = defaultWorld()
        w.light = Light(point(0.0, 0.0, -10.0), Color(1.0, 1.0, 1.0))
        val s1: Sphere = Sphere()
        val s2: Sphere = Sphere()
        s2.transform = translationTransformation(0.0, 0.0, 10.0)
        w.objects.add(s1)
        w.objects.add(s2)
        val r: Ray = Ray(point(0.0,0.0, 5.0), vector(0.0,0.0,1.0))
        val i: Intersection = Intersection(4.0, s2)
        val comps: Comps = prepareComputations(i, r)
        val c: Color = w.shadeHit(comps)
        assertEquals(Color(0.1,0.1,0.1), c)
    }

    @Test
    fun testColorIntersectionMiss() {
        val w: World = defaultWorld()
        val r: Ray = Ray(point(0.0, 0.0, -5.0), vector(0.0, 1.0, 0.0))
        val c: Color = w.colorAt(r)
        assertEquals(c, Color(0.0,0.0,0.0))
    }

    @Test
    fun testColorIntersectionHit() {
        val w: World = defaultWorld()
        val r: Ray = Ray(point(0.0, 0.0, -5.0), vector(0.0, 0.0, 1.0))
        val c: Color = w.colorAt(r)
        assertEquals(c, Color(0.38066, 0.47583, 0.2855))
    }

    @Test
    fun testColorIntersectionFromWithinSphere() {
        val w: World = defaultWorld()
        w.objects[0].material.ambient = 1.0
        w.objects[1].material.ambient = 1.0
        val r: Ray = Ray(point(0.0, 0.0, 0.75), vector(0.0, 0.0, -1.0))
        val c: Color = w.colorAt(r)
        assertEquals(c, w.objects[1].material.color)
    }

    @Test
    fun testNoShadow() {
        val w: World = defaultWorld()
        val p: Tuple = point(0.0, 10.0, 0.0)
        assertEquals(false, w.isShadowed(p))
    }

    @Test
    fun testShadowBehindSphere() {
        val w: World = defaultWorld()
        val p: Tuple = point(10.0, -10.0, 10.0)
        assertEquals(true, w.isShadowed(p))
    }

    @Test
    fun testNoShadowLightBetweenSphereAndPoint() {
        val w: World = defaultWorld()
        val p: Tuple = point(-20.0, 20.0, -20.0)
        assertEquals(false, w.isShadowed(p))
    }

    @Test
    fun testNoShadowPointBetweenLightAndSphere() {
        val w: World = defaultWorld()
        val p: Tuple = point(-2.0, 2.0, -2.0)
        assertEquals(false, w.isShadowed(p))
    }

    @Test
    fun testReflectionOffNonReflectiveMaterial() {
        val w: World = defaultWorld()
        val r: Ray = Ray(point(0.0,0.0,0.0), vector(0.0,0.0,1.0))
        val s: Shape = w.objects[1]
        s.material.ambient = 1.0
        val i: Intersection = Intersection(1.0, s)
        val comps: Comps = prepareComputations(i, r)
        val c: Color = w.reflectedColor(comps)
        assertEquals(Color(0.0,0.0,0.0), c)
    }

    @Test
    fun testReflectionOffReflectiveMaterial() {
        val w: World = defaultWorld()
        val s: Shape = Plane()
        s.material.reflective = 0.5
        s.transform = translationTransformation(0.0, -1.0, 0.0)
        w.objects.add(s)
        val r: Ray = Ray(point(0.0,0.0,-3.0), vector(0.0, -sqrt(2.0)/2.0, sqrt(2.0)/2.0))
        val i: Intersection = Intersection(sqrt(2.0), s)
        val comps: Comps = prepareComputations(i, r)
        val c: Color = w.reflectedColor(comps)
        assertEquals(Color(0.19032, 0.2379, 0.14274), c)
    }


    @Test
    fun testReflectionWithShadeHit() {
        val w: World = defaultWorld()
        val s: Shape = Plane()
        s.material.reflective = 0.5
        s.transform = translationTransformation(0.0, -1.0, 0.0)
        w.objects.add(s)
        val r: Ray = Ray(point(0.0,0.0,-3.0), vector(0.0, -sqrt(2.0)/2.0, sqrt(2.0)/2.0))
        val i: Intersection = Intersection(sqrt(2.0), s)
        val comps: Comps = prepareComputations(i, r)
        val c: Color = w.shadeHit(comps)
        assertEquals(Color(0.87677, 0.92436, 0.82918), c)
    }

    @Test
    fun testAvoidInfiniteRecursion() {
        val w: World = World()
        w.light = Light(point(0.0,0.0,0.0), Color(1.0,1.0,1.0))
        val lower: Shape = Plane()
        lower.material.reflective = 1.0
        lower.transform = translationTransformation(0.0,-1.0,0.0)
        val upper: Shape = Plane()
        upper.material.reflective = 1.0
        upper.transform = translationTransformation(0.0,1.0,0.0)
        w.objects.add(lower)
        w.objects.add(upper)
        val r: Ray = Ray(point(0.0,0.0,0.0), vector(0.0,1.0,0.0))
        w.colorAt(r)
        // assert here just verifies that colorAt actually returns successfully
        assertEquals(1,1)
    }

    @Test
    fun testLimitRecursion() {
        val w: World = defaultWorld()
        val s: Shape = Plane()
        s.material.reflective = 0.5
        s.transform = translationTransformation(0.0, -1.0, 0.0)
        w.objects.add(s)
        val r: Ray = Ray(point(0.0,0.0,-3.0), vector(0.0, -sqrt(2.0)/2.0, sqrt(2.0)/2.0))
        val i: Intersection = Intersection(sqrt(2.0), s)
        val comps: Comps = prepareComputations(i, r)
        val c: Color = w.reflectedColor(comps, 0)
        assertEquals(BLACK, c)
    }

    @Test
    fun testRefractionWithOpaque() {
        val w: World = defaultWorld()
        val s: Shape = w.objects[0]
        val r: Ray = Ray(point(0.0, 0.0, -5.0), vector(0.0, 0.0, 1.0))
        val xs: List<Intersection> = intersections(Intersection(4.0, s), Intersection(6.0, s))
        val comps: Comps = prepareComputations(xs[0], r, xs)
        val c: Color = w.refractedColor(comps, 5)
        assertEquals(BLACK, c)
    }

    @Test
    fun testRefractionAtMaxRecursion() {
        val w: World = defaultWorld()
        val s: Shape = w.objects[0]
        s.material.transparency = 1.0
        s.material.refractiveIndex = 1.5
        val r: Ray = Ray(point(0.0, 0.0, -5.0), vector(0.0, 0.0, 1.0))
        val xs: List<Intersection> = intersections(Intersection(4.0, s), Intersection(6.0, s))
        val comps: Comps = prepareComputations(xs[0], r, xs)
        val c: Color = w.refractedColor(comps, 0)
        assertEquals(BLACK, c)
    }

    @Test
    fun testTotalInternalReflection() {
        val w: World = defaultWorld()
        val s: Shape = w.objects[0]
        s.material.transparency = 1.0
        s.material.refractiveIndex = 1.5
        val r: Ray = Ray(point(0.0, 0.0, sqrt(2.0)/2.0), vector(0.0, 1.0, 0.0))
        val xs: List<Intersection> = intersections(Intersection(-sqrt(2.0)/2.0, s), Intersection(sqrt(2.0)/2.0, s))
        val comps: Comps = prepareComputations(xs[1], r, xs)
        val c: Color = w.refractedColor(comps, 5)
        assertEquals(BLACK, c)
    }

    @Test
    fun testRefractedColor() {
        val w: World = defaultWorld()
        val A: Shape = w.objects[0]
        A.material.ambient = 1.0
        A.material.pattern = test_pattern()
        val B: Shape = w.objects[1]
        B.material.transparency = 1.0
        B.material.refractiveIndex = 1.5
        val r: Ray = Ray(point(0.0,0.0,0.1), vector(0.0, 1.0, 0.0))
        val xs: List<Intersection> = intersections(Intersection(-0.9899, A),
                                                   Intersection(-0.4899, B),
                                                   Intersection(0.4899, B),
                                                   Intersection(0.9899, A)
        )
        val comps: Comps = prepareComputations(xs[2], r, xs)
        val c: Color = w.refractedColor(comps, 5)
        assertEquals(c, Color(0.0, 0.99888, 0.04725))
    }

    @Test
    fun testShadeHitWithTransparentMaterial() {
        val w: World = defaultWorld()
        val floor: Shape = Plane()
        floor.transform = translationTransformation(0.0,-1.0,0.0)
        floor.material.transparency = 0.5
        floor.material.refractiveIndex = 1.5
        val ball: Sphere = Sphere()
        ball.material.color = Color(1.0, 0.0, 0.0)
        ball.material.ambient = 0.5
        ball.transform = translationTransformation(0.0, -3.5, -0.5)
        w.objects.add(floor)
        w.objects.add(ball)
        val r: Ray = Ray(point(0.0,0.0,-3.0), vector(0.0, -sqrt(2.0)/2.0, sqrt(2.0)/2.0))
        val xs: List<Intersection> = intersections(Intersection(sqrt(2.0), floor))
        val comps: Comps = prepareComputations(xs[0], r, xs)
        val c: Color = w.shadeHit(comps, 5)
        assertEquals(Color(0.93642, 0.68642, 0.68642), c)
    }

    @Test
    fun testReflectanceColor() {
        val w: World = defaultWorld()
        val r: Ray = Ray(point(0.0,0.0,-3.0), vector(0.0, -sqrt(2.0)/2.0, sqrt(2.0)/2.0))
        val floor: Shape = Plane()
        floor.transform = translationTransformation(0.0,-1.0,0.0)
        floor.material.reflective = 0.5
        floor.material.transparency = 0.5
        floor.material.refractiveIndex = 1.5
        val ball: Shape = Sphere()
        ball.material.color = Color(1.0,0.0,0.0)
        ball.material.ambient = 0.5
        ball.transform = translationTransformation(0.0, -3.5, -0.5)
        w.objects.add(floor)
        w.objects.add(ball)

        val xs: List<Intersection> = intersections(Intersection(sqrt(2.0), floor))
        val comps: Comps = prepareComputations(xs[0], r, xs)
        val c: Color = w.shadeHit(comps, 5)
        assertEquals(c, Color(0.93391, 0.69643, 0.69243))
    }
}






