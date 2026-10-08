package com.example.foroom.pages

import android.view.View
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.ViewInteraction
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.matcher.ViewMatchers.withId
import com.alternator.foroom.R
import com.example.foroom.Helper.waitUntilVisible
import org.hamcrest.Matcher

private const val TIMEOUT_SECONDS = 10L

class ChangeLanguagePage {
    val georgianButton: Matcher<View> = withId(R.id.languageButtonGeo)
    val englishButton: Matcher<View> = withId(R.id.languageButtonEng)

    fun georgianButton(): ViewInteraction = waitFor(georgianButton)

    fun englishButton(): ViewInteraction = waitFor(englishButton)

    fun tapGeorgian() = apply {
        georgianButton().perform(click())
    }

    fun tapEnglish() = apply {
        englishButton().perform(click())
    }

    private fun waitFor(matcher: Matcher<View>): ViewInteraction =
        onView(matcher).waitUntilVisible(TIMEOUT_SECONDS)
}
