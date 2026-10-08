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

class LoginPage {
    private val loginScreen: Matcher<View> = hasSibling(withId(R.id.logInButton))

    val userNameInput: Matcher<View> = allOf(withId(R.id.userNameInput), loginScreen)
    val passwordInput: Matcher<View> = allOf(withId(R.id.passwordInput), loginScreen)
    val logInButton: Matcher<View> = withId(R.id.logInButton)
    val signUpButton: Matcher<View> = allOf(withId(R.id.signUpButton), loginScreen)

    fun userNameField(): ViewInteraction = waitFor(editTextOf(userNameInput))

    fun passwordField(): ViewInteraction = waitFor(editTextOf(passwordInput))

    fun logInButton(): ViewInteraction = waitFor(logInButton)

    fun signUpButton(): ViewInteraction = waitFor(signUpButton)

    fun userNameError(): ViewInteraction = waitFor(descriptionOf(userNameInput))

    fun passwordError(): ViewInteraction = waitFor(descriptionOf(passwordInput))

    fun enterUserName(userName: String) = apply {
        userNameField().perform(replaceText(userName), closeSoftKeyboard())
    }

    fun enterPassword(password: String) = apply {
        passwordField().perform(replaceText(password), closeSoftKeyboard())
    }

    fun tapLogIn() = apply {
        logInButton().perform(click())
    }

    fun tapSignUp() = apply {
        signUpButton().perform(click())
    }

    private fun editTextOf(input: Matcher<View>): Matcher<View> =
        allOf(withId(DesignSystemR.id.inputEditText), isDescendantOfA(input))

    private fun descriptionOf(input: Matcher<View>): Matcher<View> =
        allOf(withId(DesignSystemR.id.descriptionTextView), isDescendantOfA(input))

    private fun waitFor(matcher: Matcher<View>): ViewInteraction =
        onView(matcher).waitUntilVisible(TIMEOUT_SECONDS)
}
