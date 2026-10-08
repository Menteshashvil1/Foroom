package com.example.foroom.steps

import androidx.test.espresso.ViewInteraction
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withText
import com.example.foroom.pages.LoginPage
import org.hamcrest.Matchers.allOf
import org.hamcrest.Matchers.equalTo
import org.hamcrest.Matchers.not

class LoginSteps(private val loginPage: LoginPage = LoginPage()) {

    fun verifyLoginScreenDisplayed() = apply {
        loginPage.userNameField().check(matches(isDisplayed()))
        loginPage.passwordField().check(matches(isDisplayed()))
        loginPage.logInButton().check(matches(isDisplayed()))
        loginPage.signUpButton().check(matches(isDisplayed()))
    }

    fun logIn(userName: String, password: String) = apply {
        loginPage.enterUserName(userName)
            .enterPassword(password)
            .tapLogIn()
    }

    fun openRegistration() = apply {
        loginPage.tapSignUp()
    }

    fun verifyUserNameErrorDisplayed() = apply {
        verifyErrorDisplayed(loginPage.userNameError())
    }

    fun verifyPasswordErrorDisplayed() = apply {
        verifyErrorDisplayed(loginPage.passwordError())
    }

    private fun verifyErrorDisplayed(error: ViewInteraction) {
        error.check(matches(allOf(isDisplayed(), withText(not(equalTo(""))))))
    }
}
