package com.example.foroom.pages

import android.view.View
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.ViewInteraction
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.matcher.ViewMatchers.isDescendantOfA
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import com.alternator.foroom.R
import com.example.foroom.Helper.waitUntilVisible
import org.hamcrest.Matcher
import org.hamcrest.Matchers.allOf
import com.example.design_system.R as DesignSystemR

private const val TIMEOUT_SECONDS = 10L

class ProfilePage {
    val profileTab: Matcher<View> = withId(R.id.homeNavigationProfile)
    val changePasswordItem: Matcher<View> = withId(R.id.changePasswordItem)
    val changeLanguageItem: Matcher<View> = withId(R.id.changeLanguageItem)
    val signOutItem: Matcher<View> = withId(R.id.signOutItem)

    fun profileTab(): ViewInteraction = waitFor(profileTab)

    fun changePasswordItem(): ViewInteraction = waitFor(changePasswordItem)

    fun changeLanguageItem(): ViewInteraction = waitFor(changeLanguageItem)

    fun signOutItem(): ViewInteraction = waitFor(signOutItem)

    fun changeLanguageTitle(text: String): ViewInteraction =
        waitFor(allOf(titleOf(changeLanguageItem), withText(text)))

    fun signOutTitle(text: String): ViewInteraction =
        waitFor(allOf(titleOf(signOutItem), withText(text)))

    fun tapProfileTab() = apply {
        profileTab().perform(click())
    }

    fun tapChangePassword() = apply {
        changePasswordItem().perform(click())
    }

    fun tapChangeLanguage() = apply {
        changeLanguageItem().perform(click())
    }

    fun tapSignOut() = apply {
        signOutItem().perform(click())
    }

    private fun titleOf(item: Matcher<View>): Matcher<View> =
        allOf(withId(DesignSystemR.id.listItemTextView), isDescendantOfA(item))

    private fun waitFor(matcher: Matcher<View>): ViewInteraction =
        onView(matcher).waitUntilVisible(TIMEOUT_SECONDS)
}
