package com.example.foroom.steps

import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import com.example.foroom.pages.RegistrationPage

class RegistrationSteps(private val registrationPage: RegistrationPage = RegistrationPage()) {

    fun verifyRegistrationScreenDisplayed() = apply {
        registrationPage.userNameField().check(matches(isDisplayed()))
        registrationPage.passwordField().check(matches(isDisplayed()))
        registrationPage.repeatPasswordField().check(matches(isDisplayed()))
        registrationPage.signUpButton().check(matches(isDisplayed()))
    }

    fun fillCredentials(userName: String, password: String) = apply {
        registrationPage.enterUserName(userName)
            .enterPassword(password)
            .enterRepeatPassword(password)
    }

    fun selectAvatar(index: Int) = apply {
        registrationPage.tapAvatar(index)
        registrationPage.avatar(index).check(matches(registrationPage.isSelectedAvatar()))
    }

    fun submit() = apply {
        registrationPage.tapSignUp()
    }

    fun verifyHomeScreenDisplayed() = apply {
        registrationPage.homeContainer().check(matches(isDisplayed()))
        registrationPage.homeNavigationBar().check(matches(isDisplayed()))
    }
}
