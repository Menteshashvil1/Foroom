package com.example.foroom.Helper

import android.view.View
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.ViewInteraction
import androidx.test.espresso.assertion.ViewAssertions.matches as viewMatches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import org.hamcrest.Matcher
import org.hamcrest.StringDescription

private const val POLL_INTERVAL_MS = 50L

fun ViewInteraction.waitUntil(condition: Matcher<View>, timeoutSec: Long): ViewInteraction {
    val endTime = System.currentTimeMillis() + timeoutSec * 1000
    var lastError: Throwable? = null

    do {
        try {
            check(viewMatches(condition))
            return this
        } catch (error: Throwable) {
            lastError = error
            Thread.sleep(POLL_INTERVAL_MS)
        }
    } while (System.currentTimeMillis() < endTime)

    throw AssertionError(
        "View did not match <${StringDescription.toString(condition)}> within $timeoutSec s",
        lastError
    )
}

fun ViewInteraction.waitUntilStable(
    condition: Matcher<View>,
    stableMs: Long,
    timeoutSec: Long
): ViewInteraction {
    val endTime = System.currentTimeMillis() + timeoutSec * 1000
    var matchedSince: Long? = null
    var lastError: Throwable? = null

    do {
        try {
            check(viewMatches(condition))
            val since = matchedSince ?: System.currentTimeMillis().also { matchedSince = it }
            if (System.currentTimeMillis() - since >= stableMs) return this
        } catch (error: Throwable) {
            lastError = error
            matchedSince = null
        }
        Thread.sleep(POLL_INTERVAL_MS)
    } while (System.currentTimeMillis() < endTime)

    throw AssertionError(
        "View did not stay matching <${StringDescription.toString(condition)}> " +
            "for $stableMs ms within $timeoutSec s",
        lastError
    )
}

fun Matcher<View>.isShownOnScreen(): Boolean = try {
    onView(this).check(viewMatches(isDisplayed()))
    true
} catch (_: Throwable) {
    false
}
