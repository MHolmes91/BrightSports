package com.gios.lightsports.util

/**
 * Whether the game a score notification pointed at should be opened now.
 *
 * Pure on purpose, and kept out of the composable so it can be tested without a phone.
 * The launch intent's game id stays the same for the whole session, so on its own it says
 * nothing about whether the reader has already been taken there. `consumed` is what does:
 * once the notification's game has opened, the intent is spent, and backing out to the
 * feed has to stay backed out however many times the feed refetches underneath. That
 * missing flag is what kept reopening the game on the feed (light-reports#688).
 *
 * `gameOpen` holds it off while some game is already on screen, so the intent never
 * replaces one the reader chose.
 */
fun shouldOpenFromIntent(gameId: String?, consumed: Boolean, gameOpen: Boolean): Boolean =
    gameId != null && !consumed && !gameOpen
