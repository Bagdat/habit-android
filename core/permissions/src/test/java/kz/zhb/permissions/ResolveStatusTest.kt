package kz.zhb.permissions

import org.junit.Assert.assertEquals
import org.junit.Test

class ResolveStatusTest {

    @Test
    fun `granted wins over everything`() {
        assertEquals(
            PermissionStatus.Granted,
            resolveStatus(granted = true, shouldShowRationale = true, deniedForever = true),
        )
    }

    @Test
    fun `never asked - denied without rationale`() {
        assertEquals(
            PermissionStatus.Denied(shouldShowRationale = false),
            resolveStatus(granted = false, shouldShowRationale = false, deniedForever = false),
        )
    }

    @Test
    fun `denied once - rationale`() {
        assertEquals(
            PermissionStatus.Denied(shouldShowRationale = true),
            resolveStatus(granted = false, shouldShowRationale = true, deniedForever = false),
        )
    }

    @Test
    fun `denied forever - no rationale and flag set`() {
        assertEquals(
            PermissionStatus.PermanentlyDenied,
            resolveStatus(granted = false, shouldShowRationale = false, deniedForever = true),
        )
    }

    @Test
    fun `rationale again after revoke - not permanently denied`() {
        assertEquals(
            PermissionStatus.Denied(shouldShowRationale = true),
            resolveStatus(granted = false, shouldShowRationale = true, deniedForever = true),
        )
    }
}
