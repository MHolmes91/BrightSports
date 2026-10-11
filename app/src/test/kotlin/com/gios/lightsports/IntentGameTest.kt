package com.gios.lightsports

import com.gios.lightsports.util.shouldOpenFromIntent
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * The notification's game opens once and then leaves the reader alone (light-reports#688).
 *
 * The bug was a feed refresh walking the reader back into a game they had closed, because
 * nothing remembered that the notification had already been followed. These pin the
 * decision; the composable only acts on it.
 */
class IntentGameTest {

    @Test
    fun `a fresh notification with nothing open opens its game`() {
        assertTrue(shouldOpenFromIntent("401", consumed = false, gameOpen = false))
    }

    @Test
    fun `a spent notification does not reopen a game the reader closed`() {
        assertFalse(shouldOpenFromIntent("401", consumed = true, gameOpen = false))
    }

    @Test
    fun `a launch without a notification opens nothing`() {
        assertFalse(shouldOpenFromIntent(null, consumed = false, gameOpen = false))
    }

    @Test
    fun `a game already on screen is not replaced`() {
        assertFalse(shouldOpenFromIntent("401", consumed = false, gameOpen = true))
        assertFalse(shouldOpenFromIntent("401", consumed = true, gameOpen = true))
    }
}
