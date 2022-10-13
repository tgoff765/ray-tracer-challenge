import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class TestUtilities {

    @Test
    fun testEquals() {
        assertTrue(3.0.equalsDelta(3.0))
        assertTrue((-1.0).equalsDelta(-1.000000001))
        assertFalse(4.0.equalsDelta(5.1))
        assertFalse(1.0.equalsDelta(0.9))
    }
}