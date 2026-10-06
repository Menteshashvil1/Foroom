package com.example.foroom.pages

import android.view.View
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.ViewInteraction
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.closeSoftKeyboard
import androidx.test.espresso.action.ViewActions.replaceText
import androidx.test.espresso.matcher.ViewMatchers.hasSibling
import androidx.test.espresso.matcher.ViewMatchers.isDescendantOfA
import androidx.test.espresso.matcher.ViewMatchers.withId
import com.alternator.foroom.R
import com.example.foroom.Helper.waitUntilVisible
import org.hamcrest.Matcher
import org.hamcrest.Matchers.allOf
import com.example.design_system.R as DesignSystemR

private const val TIMEOUT_SECONDS = 10L

class ChangePasswordPage {
    val passwordInput: Matcher<View> =
        allOf(withId(R.id.passwordInput), hasSibling(withId(R.id.repeatPasswordInput)))
    val repeatPasswordInput: Matcher<View> =
        allOf(withId(R.id.repeatPasswordInput), hasSibling(withId(R.id.passwordInput)))
    val confirmButton: Matcher<View> = withId(DesignSystemR.id.actionButton)

    fun passwordField(): ViewInteraction = waitFor(editTextOf(passwordInput))

    fun repeatPasswordField(): ViewInteraction = waitFor(editTextOf(repeatPasswordInput))

    fun confirmButton(): ViewInteraction = waitFor(confirmButton)

    fun enterPassword(password: String) = apply {
        passwordField().perform(replaceText(password), closeSoftKeyboard())
    }

    fun enterRepeatPassword(password: String) = apply {
        repeatPasswordField().perform(replaceText(password), closeSoftKeyboard())
    }

    fun tapConfirm() = apply {
        confirmButton().perform(click())
    }

    private fun editTextOf(input: Matcher<View>): Matcher<View> =
        allOf(withId(DesignSystemR.id.inputEditText), isDescendantOfA(input))

    private fun waitFor(matcher: Matcher<View>): ViewInteraction =
        onView(matcher).waitUntilVisible(TIMEOUT_SECONDS)
}
