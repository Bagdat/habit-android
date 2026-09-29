package kz.zhb.prayer.impl.usecase

import org.junit.Assert.assertEquals
import org.junit.Test

class DistanceTest {

    @Test
    fun `одна и та же точка — 0 км`() {
        assertEquals(0.0, distanceKm(43.24, 76.94, 43.24, 76.94), 1e-9)
    }

    @Test
    fun `Алматы — Астана около 970 км`() {
        assertEquals(970.0, distanceKm(43.238, 76.945, 51.169, 71.449), 15.0)
    }
}
